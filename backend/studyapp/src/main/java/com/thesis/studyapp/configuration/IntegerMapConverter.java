package com.thesis.studyapp.configuration;

import org.neo4j.ogm.typeconversion.AttributeConverter;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class IntegerMapConverter implements AttributeConverter<Map<Integer, String>, Map<String, Object>> {

    @Override public Map<String, Object> toGraphProperty(Map<Integer, String> integerMap) {
        if (integerMap != null) {
            return integerMap.keySet().stream()
                    .collect(Collectors.toMap(i -> Integer.toString(i), integerMap::get));
        } else {
            return new HashMap<>();
        }
    }

    @Override public Map<Integer, String> toEntityAttribute(Map<String, Object> stringMap) {
        return stringMap.keySet().stream()
                .collect(Collectors.toMap(Integer::valueOf, i -> (String) stringMap.get(i)));
    }


    //    @Override public List<String> toGraphProperty(Map<Integer, String> integerStringMap) {
//        return new ArrayList<>();
//    }
//
//    @Override public Map<Integer, String> toEntityAttribute(List<String> s) {
//        return new HashMap<>();
//    }

//    @Override public List<Pair> toGraphProperty(Map<Integer, String> integerStringMap) {
//        return integerStringMap.keySet().stream()
//                .map(i -> new Pair(i, integerStringMap.get(i)))
//                .collect(Collectors.toList());
//    }
//
//    @Override public Map<Integer, String> toEntityAttribute(List<Pair> pairList) {
//        return pairList.stream()
//                .collect(Collectors.toMap(Pair::getPairInteger, Pair::getPairString));
//    }
//
//    @Data
//    @AllArgsConstructor
//    class Pair {
//        public Integer pairInteger;
//        public String pairString;
//    }
}
