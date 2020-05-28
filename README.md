Miklós Gergő 
# Önálló laboratórium dokumentáció

Neo4j + Spring + GraphQL + React

### Feladatkiírás
#### Álatlános iskolások oktatását segítő webalkalmazás
Manapság egyre nagyobb problémát jelent a tanulók egyenlőként kezelése, egyre több szülő kér felmentést valamilyen tárgyból a tanulási zavarokkal küzdő gyermekének. Mivel minden tanuló eltérő kompetenciákkal rendelkezik, ezért eltérő figyelmet is igényel.

A feladat egy olyan gyakorló webalkalmazás készítése, amely lehetővé teszi a adaptív feladatsorok egyszerű készítését, valamint biztosítja a tanár (vagy a szülő) számára a tanulók haladásának követését. 

### Bevezetés
Az alkalmazásban minden csoportok köré épül, ezekhez tartoznak a tanulók, tanárok és tesztek. A tanulók teszteket tudnak oldani, a tanárok teszteket tudnak létrehozni és láthatják a tanulók megoldása közbeni állapotait, például, hogy valakinek segítségre van-e szüksége, vagy épp végzett-e már.

A tesztek fő célja nem a tudásfelmérés, hanem a gyakorlás. A kapott feladatok tanulónként egyediek, alkalmazkodnak a tanuló korábbi teljesítményeihez. Ennek alapja az, hogy egy teszten belül a feladatok szintekhez vannak rendelve, és a teszt megoldásához a tanulónak tulajdonképpen szintugrásokat kell végrehajtania, a saját tempójában (jelenleg ez még nincs megvalósítva).

Teszteket létrehozni egyszerű, egy nyilvános feladattárból választhatunk ki feladatokat, azt megadva, hogy a teszt melyik szintjéhez tartozzon.

![extensions](imgs/onlab/Dia3.PNG)

### Technológiák és architektúra
Egyik alapvető célom a Spring keretrendszerrel való megismerkedés volt és emellett érdekes kihívásnak tartottam REST helyett az egyre népszerűbb GraphQL alkalmazni a kommunikáció megvalósítására, amely segítségével nagyban leegyszerűsíthető a kliensoldali fejlesztés. Az adatok tárolására mindenképpen gráfadatbázist szerettem volna használni a modellben lévő sok és összetett kapcsolat miatt, és végül a Spring támogatottsága miatt a Neo4j adatbáziskezelőre esett a választás. Alapvetően a szerveroldali fejlesztésre szerettem volna helyezni a hangsúlyt, ezért kliensoldalon a dinamikus megjelenítést támogató, viszont egyszerű React könyvtárat használtam.  

![extensions](imgs/onlab/Dia4.PNG)

# Fejlesztés:
## Backend

### Spring architektúra
Szerveroldalon alapvetően az általános tervezési konvenciókat követtem, azaz három rétegből áll, a kiszolgálási, az üzletilogika és az adatelérési rétegből áll, a Spring keretrendszert felhasználva. A Spring modulok sok eszközt nyújtottak a Java alkalmazásom fejlesztésekor: függőség injektálást, adatbáziselérés és tranzakciókezelés egységes absztrakcióját, webszolgáltatásokat támogató eszközöket és még sok más mindent.

(A gyorsabb alkalmazásfejlesztés érdekében Spring Bootot használtam, ezzel megspórolva nehézkes kézi konfigurációkat.)

![extensions](imgs/onlab/Dia5.PNG)

### GraphQL 
Spring keretrendszerben nagy támogatottsága van (az eredetileg JavaScripthez létehozott) GraphQL-nek a kreatív nevű GraphQL-Java könyvtárnak köszönhetően, amely egy teljes előre konfigurált szervert ad a fejlesztőnek. A GraphQL-nek, ha jól használjuk, sok előnye lehet a REST-tel szemben, mivel kliensoldalon a séma alapján deklaratív módon pontosan megadhatjuk, milyen adatokra van szükségünk, ezáltal kevesebb kérés történik, fölösleges adatok nélkül. Azonban mindez a szerveroldalon sok többletmunkával és új problémákkal járhat, ahogy ez velem is történt (dokumentumom nagy része ezért erről is szól), így nem feltétlenül éri meg az alkalmazása.

REST kontrollerek helyett hasonló feladatot ellátó Query- és MutationResolvereket kell létrehozni, a GraphQL sémában az alkalmazás objektummodelljét meg kell ismételni, valamint ez alapján a modell összes objektumához is ObjectResolvereket kell létrehozni, amelyek feladata, hogy az adott objektumok adattagjainak értékét állítsák be.

Tehát a GraphQL-nek nem csak előnyei, de hátrányai is vannak. Újabb sémaismétlések kellenek, resolverek miatt bonyolultabb az autorizáció, és megjelenik a jól ismert N+1 Probléma is:

### GraphQL N+1 probléma
Ha csoportokat és azok tesztjeit szeretnénk megkapni, az REST-tel két külön kérés lenne, de GraphQL-lel csak egy. De mi a probléma a GraphQL kéréssel? Az N csoport összesen 1 adatbáziskérés, azonban a resolverek működése szerint minden külön csoportra külön fog megtörténni a tesztek kérése, ami további N adatbáziskérés. Így a válaszidő akár hosszabb is lehet, mint két külön REST kérés (párhuzamosságot figyelembe véve is).

![extensions](imgs/onlab/Dia7.PNG)

Az N+1 problémát a DataLoader nevű osztály segítségével oldottam meg, amely annyit csinál, hogy összevárja a kéréseket egy kötegbe és megfelelő időpontban egyetlen egy adatbáziskérésként hajtja végre azokat. Azonban ez további megkötésekkel jár. Esetünkben a csoport objektumoknak előre ismerniük kell a hozzá tartozó tesztek azonosítóját, az  adatbázisnak képesnek kell lennie kötegelt lekérdezésekre és megfelelő sorrendben kell visszaadni az eredményt, null értékek nélkül.

![extensions](imgs/onlab/Dia8.PNG)

### Hibakezelés
Alapesetben a kliensoldali kérésre nem tudunk egyedi hibaüzenettel válaszolni (csupán a hibás szintaktikájú kérésre vonatkozó eseményekről értesül pontosan a kliens), minden esetben (kivételnél) „Internal server error” hibaüzenet jelenik meg, ez az úgynevezett „exception shielding” technika. Erre megoldásként három osztályt kell létrehoznunk: egy saját kivételt, ahhoz egy új adaptert, amely elrejti a kliens elől az érzékeny információkat, és egy saját kivételkezelőt is az alap kivételkezelő működését megváltoztatva úgy, hogy a saját kivételeink is eljussanak a klienshez.

Fontos még figyelembe venni, hogy a REST http válaszaival ellentétben, ha egy GraphQL lekérdezés egy nem kért hibát produkál, attól még kért adatok részlegesen megérkezhetnek, fontos információkat tartalmazva.

### Adatbázis 
A megszokottól eltérően az dokumentum végére hagytam az adatbázist, ami azzal magyarázható, hogy fejlesztés közben is utólag véglegesítettem azt a GraphQL még nem ismert sajátosságai miatt.

Az alkalmazásom sokszor használ kapcsolódó objektumokat, és a gráfadatbázisok lehetővé teszik az ún. „nagy teljesítményű (index free) join” műveleteket, így végül a relációs vagy dokumentum adatbázisok helyett a gráfadatbázisokra esett a választásom.

A Neo4j gráfadatbáziskezelőhöz létezik Spring Data implementáció, azaz megfelelő annotációkkat használva automatikus objektum-gráf leképezést kapunk. Egy megfelelő interfészből leszármaztatva szintén sok kódolást megspórolhatunk, mivel így az adatelérési logikát a függvények neveivel is megadhatjuk. Azonban esetemben ez nem mindig volt elég, például, ha szükségem volt egy csoport tesztjeinek azonosítójára is (amit a DataLoader megkövetel), akkor saját Cypher nyelvű lekérdezést kellett írnom:

![extensions](imgs/onlab/Dia10.PNG)

Jól látszik, hogy a GraphQL alkalmazása a lekérdezésekre és a DTO-k szerkezetére is kihat, ha az N+1 problémát is megakarjuk oldani. Természetesen mérlegelhetünk is, nem kell minden esetben kötelezően megtennünk, de így tulajdonképpen egy háromrétegű objektummodellel dolgozunk: entitások az adatbázisnak, DTO-k a resolvereknek, valamint a GraphQL séma által meghatározott objektumok a kliensnek.

![extensions](imgs/onlab/Dia11.PNG)

## Frontend

### Röviden
A frontend oldalról is szeretnénk említeni néhány szót, amelyet a React könyvtárral valósítottam meg, amelynek működésének lényege, hogy a felhasználói felületet egy virtuális DOM-ban tartja karban, így azon képes műveletek gyors végrehajtásra. A React alkalmazásom két fő egységből áll, a megjelenítésért felelős komponensekből és a backendhez irányuló kéréseket kezelő kiszolgálókból. (Ezekhez a kérésekhez az egyszerű, Reacthez fejlesztett Apollo Clientet választottam.) Az alkalmazás felületének kialakításakor figyelmet fordítottam arra, hogy gyerekbarát, így könnyen kezelhető legyen, valamint igyekeztem élénk és barátságos színeket is használni.

![extensions](imgs/onlab/Dia12.PNG)

## Továbbfejlesztés
A hiányosságok bepótlása mellett (autentikáció, tesztelés) további fejlesztési terv a többféle feladattípus és az, hogy tanulókhoz szülőket is lehessen rendelni, hogy ők is lássák gyermekeik állapotait feladatsoronként, ezáltal támogatva az otthon való gyakorlást is.
(+ mobil, docker)

## Összefoglalás
Habár még sok fejlesztési lehetőség maradt, azonban mindezt összegezve én úgy gondolom sikerült teljesítenem a kitűzött célokat, valamint rengeteg tapasztalatot szereznem, hiszen az alkalmazott technológiák többségével korábban még nem is foglalkoztam.
