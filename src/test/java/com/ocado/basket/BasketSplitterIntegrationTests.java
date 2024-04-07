package com.ocado.basket;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BasketSplitterIntegrationTests {

    @Test
    public void splitOptimalityTest() throws IOException {
        // Given
        String configJson = "C:\\Stuff\\InterviewTasks\\basket-splitter\\src\\test\\java\\resources\\optimality-test.json";
        BasketSplitter basketSplitter = new BasketSplitter(configJson);

        // When
        Map<String, List<String>> result = basketSplitter.split(List.of("item0", "item1", "item2", "item3", "item4", "item5", "item6", "item7", "item8", "item9", "item10", "item11", "item12", "item13"));

        // Then
        assertEquals(2, result.size());
    }

    @Test
    public void splitLargestGroupTest() throws IOException {
        // Given
        String configJson = "C:\\Stuff\\InterviewTasks\\basket-splitter\\src\\test\\java\\resources\\largest-delivery-size-test.json";
        BasketSplitter basketSplitter = new BasketSplitter(configJson);

        // When
        Map<String, List<String>> result = basketSplitter.split(Arrays.asList("item0", "item1", "item2", "item3"));
        int largestDeliverySize = 0;
        for (String deliveryOption : result.keySet()) {
            if (result.get(deliveryOption).size() > largestDeliverySize) {
                largestDeliverySize = result.get(deliveryOption).size();
            }
        }

        // Then
        assertEquals(3, largestDeliverySize);
    }

}
