package com.ocado.basket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BasketSplitter {

    private final Map<String, List<String>> itemsDeliveryOptions;

    public BasketSplitter(String absolutePathToConfigFile) throws IOException {
        Path path = Paths.get(absolutePathToConfigFile);
        String json = Files.readString(path);

        this.itemsDeliveryOptions = JsonParser.parseObject(json);
    }

    public Map<String, List<String>> split(List<String> items) {
        Map<String, Set<String>> bestBasketSplit = new HashMap<>();
        Map<String, Set<String>> deliveryOptions = new HashMap<>();
        Map<String, Integer> itemCounts = new HashMap<>();

        for (String item : items) {
            addItemToDeliveryOptions(deliveryOptions, item);

            // in case a client has many items of the same type in the online basket
            increaseItemCount(itemCounts, item);
        }

        List<String> deliveryOptionsNames = deliveryOptions.keySet().stream().toList();
        int largestDeliverySize = 0;

        for (int i = 0; i < Math.pow(2, deliveryOptions.size()); ++i) {
            Set<String> chosenDeliveryOptions = generateMaskBasedSubset(deliveryOptionsNames, i);

            if (checkSetCoverage(items, deliveryOptions, chosenDeliveryOptions)) {
                NameValueTuple maxSizeDelivery = getLargestDeliveryOption(deliveryOptions, chosenDeliveryOptions, itemCounts);
                if (   (bestBasketSplit.isEmpty() || chosenDeliveryOptions.size() < bestBasketSplit.size())
                    || (chosenDeliveryOptions.size() == bestBasketSplit.size()
                        && maxSizeDelivery.value() > largestDeliverySize)
                ) {
                    bestBasketSplit = generateSplit(deliveryOptions, chosenDeliveryOptions, maxSizeDelivery.name());
                    largestDeliverySize = maxSizeDelivery.value();
                }
            }
        }

        return includeItemRepetitionsSplit(bestBasketSplit, itemCounts);
    }


    protected void increaseItemCount(
        Map<String, Integer> itemCounts,
        String item
    ) {
        if (!itemCounts.containsKey(item)) {
            itemCounts.put(item, 0);
        }
        itemCounts.replace(item, itemCounts.get(item) + 1);
    }

    protected void addItemToDeliveryOptions(
        Map<String, Set<String>> deliveryOptions,
        String item
    ) {
        for (String deliveryOption : this.itemsDeliveryOptions.get(item)) {
            if (!deliveryOptions.containsKey(deliveryOption)) {
                deliveryOptions.put(deliveryOption, new HashSet<>());
            }
            deliveryOptions.get(deliveryOption).add(item);
        }
    }

    protected Set<String> generateMaskBasedSubset(
        List<String> originalList,
        int mask
    ) {
        Set<String> subset = new HashSet<>();
        int j = 0;

        while (mask > 0) {
            if (mask % 2 == 1) {
                subset.add(originalList.get(j));
            }

            mask /= 2;
            ++j;
        }

        return subset;
    }

    protected boolean checkSetCoverage(
        Collection<String> set,
        Map<String, Set<String>> subsets,
        Collection<String> chosenSubsets
    ) {
        Set<String> setCopy = new HashSet<>(set);

        for (String subset : chosenSubsets) {
            setCopy.removeAll(subsets.get(subset));
        }

        return setCopy.isEmpty();
    }

    protected NameValueTuple getLargestDeliveryOption(
        Map<String, Set<String>> deliveryOptions,
        Set<String> chosenDeliveryOptions,
        Map<String, Integer> itemCounts
    ) {
        String largestDeliveryOption = null;
        int largestDeliverySize = 0;

        for (String deliveryOption : chosenDeliveryOptions) {
            int currentDeliverySize = 0;
            for (String item : deliveryOptions.get(deliveryOption)) {
                currentDeliverySize += itemCounts.get(item);
            }

            if (largestDeliveryOption == null || currentDeliverySize > largestDeliverySize) {
                largestDeliveryOption = deliveryOption;
                largestDeliverySize = currentDeliverySize;
            }
        }

        return new NameValueTuple(largestDeliveryOption, largestDeliverySize);
    }

    protected Map<String, Set<String>> generateSplit(
        Map<String, Set<String>> deliveryOptions,
        Set<String> chosenDeliveryOptions,
        String maxSizeDelivery
    ) {
        Map<String, Set<String>> bestSplitBasket = new HashMap<>();
        bestSplitBasket.put(maxSizeDelivery, new HashSet<>(deliveryOptions.get(maxSizeDelivery)));
        Set<String> addedItems = new HashSet<>(bestSplitBasket.get(maxSizeDelivery));
        chosenDeliveryOptions.remove(maxSizeDelivery);

        for (String chosenDeliveryOption : chosenDeliveryOptions) {
            Set<String> reducedDeliveryOption = new HashSet<>(deliveryOptions.get(chosenDeliveryOption));
            reducedDeliveryOption.removeAll(addedItems);

            if (reducedDeliveryOption.isEmpty()) {
                break;
            }

            bestSplitBasket.put(chosenDeliveryOption, reducedDeliveryOption);
            addedItems.addAll(bestSplitBasket.get(chosenDeliveryOption));
        }

        return bestSplitBasket;
    }

    protected Map<String, List<String>> includeItemRepetitionsSplit(
        Map<String, Set<String>> originalSplit,
        Map<String, Integer> itemCounts
    ) {
        Map<String, List<String>> split = new HashMap<>();

        for (String deliveryOption : originalSplit.keySet()) {
            split.put(deliveryOption, new ArrayList<>());

            for (String item : originalSplit.get(deliveryOption)) {
                for (int i = 0; i < itemCounts.get(item); ++i) {
                    split.get(deliveryOption).add(item);
                }
            }
        }

        return split;
    }

}










