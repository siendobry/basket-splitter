package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonParser {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    private JsonParser() {}

    public static List<String> parseList(String json) throws IOException{
        return objectMapper.readValue(json, new TypeReference<List<String>>() {});
    }

    public static Map<String, List<String>> parseObject(String json) throws IOException{
        return objectMapper.readValue(json, new TypeReference<HashMap<String, List<String>>>() {});
    }
}
