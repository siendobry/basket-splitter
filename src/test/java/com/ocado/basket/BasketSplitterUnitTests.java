package com.ocado.basket;


import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class BasketSplitterUnitTests {

    private final BasketSplitter basketSplitter;

    public BasketSplitterUnitTests() throws IOException {
        this.basketSplitter = new BasketSplitter("C:\\Stuff\\InterviewTasks\\basket-splitter\\src\\main\\resources\\config.json");
    }

    @Test
    public void increaseItemCountTest() {
        // Given
        HashMap<String, Integer> itemCounts = new HashMap<>();
        itemCounts.put("test", 0);

        // When
        basketSplitter.increaseItemCount(itemCounts, "test");

        // Then
        assertEquals(1, itemCounts.get("test"));
    }

    @Test
    public void addItemToDeliveryOptionsTest() {
        // Given
        HashMap<String, Set<String>> deliveryOptions = new HashMap<>();
        deliveryOptions.put("test", new HashSet<>());

        // When
        basketSplitter.addItemToDeliveryOptions(deliveryOptions, "Fond - Chocolate");

        // Then
        assertTrue(deliveryOptions.get("Pick-up point").contains("Fond - Chocolate"));
    }

    @Test
    public void generateMaskBasedSubsetTest() {
        // Given
        int mask = 3;
        ArrayList<String> deliveryOptionsNames = new ArrayList<>();
        deliveryOptionsNames.add("test1");
        deliveryOptionsNames.add("test2");
        deliveryOptionsNames.add("test3");

        // When
        Set<String> subset = basketSplitter.generateMaskBasedSubset(deliveryOptionsNames, mask);

        // Then
        assertTrue(subset.containsAll(Arrays.asList("test1", "test2")));
    }

    @Test
    public void checkSetCoveragePositiveTest() {
        // Given
        Collection<String> set = Arrays.asList("A", "B", "C");

        Map<String, Set<String>> subsets = new HashMap<>();
        subsets.put("X", new HashSet<>(Arrays.asList("A", "B")));
        subsets.put("Y", new HashSet<>(Arrays.asList("B", "C")));

        Collection<String> chosenSubsets = Arrays.asList("X", "Y");

        // When
        boolean result = basketSplitter.checkSetCoverage(set, subsets, chosenSubsets);

        // Then
        assertTrue(result);
    }

    @Test
    public void checkSetCoverageNegativeTest() {
        // Given
        Collection<String> set = Arrays.asList("A", "B", "C");

        Map<String, Set<String>> subsets = new HashMap<>();
        subsets.put("X", new HashSet<>(Arrays.asList("A", "B")));
        subsets.put("Y", new HashSet<>(Arrays.asList("B", "C")));

        Collection<String> chosenSubsets = List.of("X");

        // When
        boolean result = basketSplitter.checkSetCoverage(set, subsets, chosenSubsets);

        // Then
        assertFalse(result);
    }

    @Test
    public void getLargestDeliveryOptionTest() {
        // Given
        Map<String, Set<String>> deliveryOptions = new HashMap<>();
        deliveryOptions.put("Pick-up point", new HashSet<>(List.of("Fond - Chocolate")));
        deliveryOptions.put("Delivery", new HashSet<>(Arrays.asList("Fond - Chocolate", "Fond - Vanilla")));

        Map<String, Integer> itemCounts = new HashMap<>();
        itemCounts.put("Fond - Chocolate", 1);
        itemCounts.put("Fond - Vanilla", 1);

        // When
        NameValueTuple result = basketSplitter.getLargestDeliveryOption(deliveryOptions, deliveryOptions.keySet(), itemCounts);

        // Then
        assertEquals("Delivery", result.name());
        assertEquals(2, result.value());
    }

    @Test
    public void generateSplitTest() {
        Map<String, Set<String>> deliveryOptions = new HashMap<>();
        deliveryOptions.put("Pick-up point", new HashSet<>(List.of("Fond - Chocolate")));
        deliveryOptions.put("Delivery", new HashSet<>(Arrays.asList("Fond - Chocolate", "Fond - Vanilla")));

        Set<String> chosenDeliveryOptions = new HashSet<>(Arrays.asList("Pick-up point", "Delivery"));

        String maxSizeDelivery = "Delivery";

        // When
        Map<String, Set<String>> result = basketSplitter.generateSplit(deliveryOptions, chosenDeliveryOptions, maxSizeDelivery);

        // Then
        assertEquals(1, result.size());
        assertEquals(2, result.get("Delivery").size());
    }

    @Test
    public void includeItemRepetitionsSplitTest() {
        Map<String, Set<String>> deliveryOptions = new HashMap<>();
        deliveryOptions.put("Pick-up point", new HashSet<>(List.of("Fond - Chocolate")));
        deliveryOptions.put("Delivery", new HashSet<>(Arrays.asList("Fond - Chocolate", "Fond - Vanilla")));

        Set<String> chosenDeliveryOptions = new HashSet<>(Arrays.asList("Pick-up point", "Delivery"));

        String maxSizeDelivery = "Delivery";

        Map<String, Set<String>> split = basketSplitter.generateSplit(deliveryOptions, chosenDeliveryOptions, maxSizeDelivery);

        Map<String, Integer> itemCounts = new HashMap<>();
        itemCounts.put("Fond - Chocolate", 1);
        itemCounts.put("Fond - Vanilla", 2);

        // When
        Map<String, List<String>> result = basketSplitter.includeItemRepetitionsSplit(split, itemCounts);

        // Then
        assertEquals(1, result.size());
        assertEquals(3, result.get("Delivery").size());
    }
}
