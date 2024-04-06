package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        String configAbsolutePath = "D:\\Pobrane\\Zadanie\\Zadanie\\config.json";
        BasketSplitter splitter = new BasketSplitter(configAbsolutePath);

        String clientBasketAbsolutePath = "D:\\Pobrane\\Zadanie\\Zadanie\\basket-2.json";
        Path path = Paths.get(clientBasketAbsolutePath);
        String json = Files.readString(path);
        ObjectMapper objectMapper = new ObjectMapper();
        List<String> clientBasket = objectMapper.readValue(json, new TypeReference<List<String>>() {});

        Map<String, List<String>> result = splitter.split(clientBasket);
        for (String deliveryOption : result.keySet()) {
            System.out.println(deliveryOption + ": " + Arrays.toString(result.get(deliveryOption).toArray()));
        }
    }
}