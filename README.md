Miklós Gergő 
# Önálló laboratórium dokumentáció

Neo4j + Spring + GraphQL + React

## Feladatkiírás
Gyakorló alkalmazás általános iskolások számára
Manapság egyre nagyobb társadalmi problémát jelent a tanulók egyenlőként kezelése, egyre több szülő kér felmentést valamilyen tárgyból tanulási zavarokkal küzdő gyermekének. Mivel minden gyerek eltérő kompetenciával rendelkezik, ezért egyéni törődést, figyelmet is igényel.

A feladat egy olyan gyakorló webalkalmazás készítése, amely alkalmazkodik a tanuló képességeihez, valamint lehetővé teszi a tanító (vagy a szülők) számára a tanulók haladásának követését. Az alkalmazás célja, hogy olyan gyakorló feladatsorok egyszerű készítését tegye lehetővé, amelyek önállóan végezhetők legyenek, nehézsége folyamatosan alkalmazkodjon a tanuló kompetenciájához, valamint a megfelelő személyek figyelmeztetést kapjanak, ha megoldás közben probléma lép fel.

## Bevezetés
Az alkalmazásban minden csoportok köré épül, ezekhez tartoznak a tanulók, tanárok és tesztek. A tanulók teszteket tudnak oldani, a tanárok teszteket tudnak létrehozni és láthatják a tanulók teszt megoldása közbeni állapotait, mint hogy elkezdte-e már vagy baj van-e.

A tesztek fő célja nem a tudásfelmérés, hanem a gyakorlás. A kapott feladatok tanulónként egyediek, alkalmazkodnak a tanuló korábban oldott tesztjei, valamint kapott feladatai eredményeihez. Ennek alapja az, hogy egy teszten belül a feladatok szintekhez vannak rendelve, és a teszt megoldásához a tanulónak tulajdonképpen szintugrásokat kell végrehajtania, a kompetenciájától függő sebességgel (jelenleg ez még nincs megvalósítva).

Teszteket létrehozni egyszerű, egy feladattárból lehet feladatot kiválasztani és megadni, hogy a teszt melyik szintjéhez tartozzon. A feladattárba bárki létrehozhat feladatokat, az teljesen nyilvános.

## Technológiák
Egyik alapvető célom a Spring keretrendszerrel való megismerkedés, ezért eköré építettem fel a többi választott technológiát is. Érdekes kihívásnak tartottam REST helyett az egyre népszerűbb GraphQL választani a kommunikáció és a végpontok megvalósítására. Segítségével a kliens pontosan meghatározhatja, milyen adatra van szüksége, amit kihasználva sok előnyre tehetünk szert: kevesebb kérés, fölösleges adatok nélkül. Az adatok tárolására mindenképpen gráfadatbázist választottam a modellben lévő sok, és összetett kapcsolat miatt, amelyek az alkalmazás jellege miatt erősen ki is vannak használva. Végül a Spring támogatottsága miatt a Neo4j adatbáziskezelőre esett a választás. Megvalósításkor alapvetően a szerveroldali fejlesztésre szeretném helyezni a hangsúlyt, ezért kliensoldalon az dinamikus tartalmat támogató, de egyszerű React könyvtárat használom. 

# Fejlesztés:
## Backend

### Spring architekctúra
A szerveroldal alapvetően az általános tervezési konvenciókat követtem, azaz három rétegből, a kiszolgálási, az üzletilogika és az adatelérési rétegből áll, a Spring keretrendszerre épülve. A Spring modulok sok mindent nyújtanak Java alkalmazás fejlesztéskor: függőség injektálást (annotációkkal), adatbáziselérés és tranzakciókezelés egységes absztrakcióját, objektum-relációs leképezést támogató eszközöket, webszolgáltatásokat támogató eszközöket, tesztelést és még sok más mindent.
A gyorsabb alkalmazásfejlesztés érdekében a Spring Bootot használtam, ezzel megspórolva nehézkes kézi konfigurációkat.

### GraphQL 
Spring keretrendszerben nagy támogatottsága van az eredetileg JavaScripthez írt GraphQL-nek a GraphQL-Java (és Spring Boot Starter) kreatív nevű könyvtárnak köszönhetően, ami egy teljes előre konfigurált szervert ad a fejlesztőnek. A GraphQL-nek, ha jól használjuk, sok előnye lehet a REST-tel szemben kliens oldalon, mert a séma alapján deklaratív módon pontosan megfogalmazhatja, milyen adatokra van szüksége, ezáltal kevesebb kérés történik, fölösleges adatok nélkül. Azonban mindez a szerver oldalon sok többletmunkával és új problémákkal járhat akár minden területen, ahogy ez velem is történt, így nem feltétlenül éri meg az alkalmazása. Ezért is szól jelen dokumentáció nagy része erről.

REST végpontok helyett hasonló feladatot ellátó Query- és MutationResolvereket kell létrehozni, GraphQL sémában az alkalmazás modelljét meg kell ismételni, valamint ez alapján a modell összes objektumához (például esetemben felhasználók, csoportok, tesztek stb.) is resolvereket kell létrehozni, amelyek az objektumok adattagjait állítják be.
Tehát a GraphQL-nek vannak hátrányai is, újabb sémaismétlések kellenek, resolverek miatt bonyolultabb az autentikáció és autorizáció, és megjelenik a jól ismert N+1 Probléma is:

### N+1 probléma
Ha felhasználókat és azok csoportjait szeretnénk megkapni, akkor REST-tel ez két külön kérés lenne és GraphQL-lel csak egy. De mi a probléma a GraphQL kéréssel? Az N felhasználó mindösszesen 1 adatbáziskérés, azonban a resolverek működése szerint azok csoportjaik kérés mindegy egyes felhasználóra egyesével fog megtörténni, azaz további N adatbáziskérés, amin sokszor a párhuzamos végrehajtás sem tud segíteni. Így a válaszadás akár lassabb is lehet, mint két külön REST kérés.

Az N+1 problémára a DataLoader nevű (könyvtár és) osztály segítségével oldottam meg, amely annyit csinál, hogy összevárja az kéréseket egy kötegbe, és megfelelő időpontban egyetlen egy adatbáziskérésként hajtja végre azokat. Azonban ez további megkötésekkel jár. Esetünkben a felhasználó objektumnak előre ismernie kell a hozzá tartozó csoportok azonosítóját, az  adatbázisnak képesnek kell lennie kötegelt lekérdezésekre, megfelelő sorrendben kell vissza adni a csoportokat, null értékek nélkül.

### Hibakezelés
Alapesetben a GraphQL-Javában végponton keresztül történő kérésre nem tudunk egyedi hibaüzenettel válaszolni, csupán a hibás szintaktikájú kérésre vonatkozó eseményekről értesül pontosan a kliens, minden más esetben (kivételnél) „Internal server error” hibaüzenet jelenik meg, ez az úgynevezett „exception shielding” technika. Erre megoldásként három osztályt kell létrehoznunk: egy saját kivételt, ahhoz egy adaptert, amely elrejti a kliens elől az érzékeny információkat (például eldobás helye), valamint egy saját kivételkezelőt is az alap kivételkezelő működését megváltozta úgy, hogy a saját kivételeink is eljussanak a klienshez.
Fontos még figyelembe venni, hogy a REST http válaszaival ellentétben, ha egy GraphQL lekérdezés egy nem kért hibát produkál, attól még kért adatok részlegesen megérkezhetnek, fontos információkat tartalmazva.

### Adatbázis 
A megszokottól eltérően a végére hagytam az adatbázist, ami azzal magyarázható, hogy fejlesztés közben is inkább utólag véglegesítettem azt, leginkább a GraphQL elején még nem ismert sajátosságai miatt.
A gráfadatbázisok a nevükből is érthető módon csúcsokat (címke, tulajdonság) és köztük menő éleket tárolnak, lehetővé téve az ún. „nagy teljesítményű (index free) join” műveleteket, ezért esett végül erre a választásom a relációs vagy dokumentum adatbázisok helyett.
A Neo4j gráfadatbáziskezelőhöz létezik Spring Data implementáció, azaz megfelelő annotációkkal (például NodeEntitiy, Relationship) használva automatikus objektum-gráf leképezést (OGM) kapunk. Egy megfelelő Neo4jRepository interfészből leszármaztatva szintén sok kódot megspórolhatunk, mivel az adatelérési logikát a függvények neveivel adhatjuk meg (például findUserByName(String name)). Azonban esetünkben ez nem mindig elég, például, ha szükségünk van egy felhasználó csoportjainak azonosítójára is (amit a DataLoader megkövetel), akkor a Query annotációval saját Cypher nyelvű lekérdezést kell megvalósítanunk:


Jól látszik, hogy a GraphQL alkalmazása a lekérdezésekre és a DTO-k (Data Transfer Object) szerkezetére is kihat, ha az N+1 problémát is megakarjuk oldani. Természetesen ezt nem kell minden esetben kötelezően megtennünk, mérlegelhetünk is, de így tulajdonképpen egy háromrétegű objektummodellel dolgozunk: entitások az adatbázisnak, DTO-k a resolvereknek, valamint a GraphQL séma által meghatározott (valódi DTO) objektumok a kliensnek.
Kép userDTO és User

## Frontend

### Röviden
Ebben a félévben a backend oldalra koncentráltam, de természetesen a frontend oldalról is szeretnénk említeni néhány szót. A frontendem egy single-page aplication, a felhasználói felületet statikus részei az alkalmazás megnyitásakor töltődnek le, majd később csak a dinamikus adatok mozognak a szerver és a kliens oldal között JavaScript hívások segítségével. Ezt a React könyvtárral valósítottam meg.

Működésének lényege, hogy a felhasználói felület egy reprezentációját az úgynevezett virtuális DOM-ban (Document Object Model) tartja karban, abban képes gyors művelet végrehajtásra és  majd ezt képezi le tényleges felületi elemekre. Egy React alkalmazás általában, és nálam is, a megjelenítésért felelős komponensekből és a backendhez irányuló kéréseket kezelő szervízekből áll. Ezekhez a kérésekhez a sok funkcióval rendelkező (mint például kliens oldali cache-elés), de egyszerű, Reacthez fejlesztett Apollo Clientet választottam.


## Továbbfejlesztés
Korábban már említett hiányosságok, több feladattípus, autentikáció és autorizáció, mobil, Docker, tesztelés, Redux
Összefoglalás

Habár még sok fejlesztési lehetőség maradt, azonban mindezt összegezve én úgy gondolom sikerült egy jól működő alkalmazást felépítenem a tervek szerint, valamint rengeteg tapasztalatot szereznem, hiszen az alkalmazott technológiák többségével korábban még nem foglalkoztam.
