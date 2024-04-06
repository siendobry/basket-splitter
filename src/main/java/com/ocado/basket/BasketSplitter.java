package com.ocado.basket;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BasketSplitter {

    private final HashMap<String, List<String>> itemsDeliveryOptions;

    public BasketSplitter(String absolutePathToConfigFile) throws IOException {
        Path path = Paths.get(absolutePathToConfigFile);
        String json = Files.readString(path);

        ObjectMapper objectMapper = new ObjectMapper();
        this.itemsDeliveryOptions = objectMapper.readValue(
            json,
            new TypeReference<HashMap<String, List<String>>>() {
        });
    }

    public Map<String, List<String>> split(List<String> items) {
        Map<String, List<String>> splitBasket = new HashMap<>();
        HashMap<String, Set<String>> deliveryOptions = new HashMap<>();
        HashMap<String, Integer> itemCounts = new HashMap<>();

        for (var item : items) {
            addItemToDeliveryOptions(deliveryOptions, item);

            // in case a client has many items of the same type in the online basket
            increaseItemCount(itemCounts, item);
        }

        String[] deliveryOptionsNames = (String[]) deliveryOptions.keySet().toArray();
        Map<String, List<String>> bestSplitBasket = new HashMap<>();
        int largestDeliveryOption = 0;
        for (int i = 0; i < Math.pow(2, deliveryOptions.size()); ++i) {
            List<String> chosenDeliveryOptions = new ArrayList<>();

            int tmp = i;
            int j = 0;
            while (tmp > 0) {
                if (tmp % 2 == 1) {
                    chosenDeliveryOptions.add(deliveryOptionsNames[j]);
                }
                tmp /= 2;
                ++j;
            }

            Set<String> itemsCopy = new HashSet<>(items);
            for (String chosenDeliveryOption : chosenDeliveryOptions) {
                itemsCopy.removeAll(deliveryOptions.get(chosenDeliveryOption));
            }

            if (itemsCopy.isEmpty()) {
                int maxSizeDelivery = 0;
                for (String chosenDeliveryOption : chosenDeliveryOptions) {
                    if (deliveryOptions.get(chosenDeliveryOption).size() > maxSizeDelivery) {
                        maxSizeDelivery = deliveryOptions.get(chosenDeliveryOption).size();
                    }
                }

                if (maxSizeDelivery > largestDeliveryOption) {
                    bestSplitBasket = new HashMap<>();
                    for (String chosenDeliveryOption : chosenDeliveryOptions) {
//                        bestSplitBasket.put(chosenDeliveryOption, );
                    }
                }
            }
        }

        return splitBasket;
    }

//    public Map<String, List<String>> split(List<String> items) {
//        Map<String, List<String>> splitBasket = new HashMap<>();
//        HashMap<String, Set<String>> deliveryOptions = new HashMap<>();
//        HashMap<String, Integer> itemCounts = new HashMap<>();
//
//        for (var item : items) {
//            addItemToDeliveryOptions(deliveryOptions, item);
//
//            // in case a client has many items of the same type in the online basket
//            increaseItemCount(itemCounts, item);
//        }
//
//        for (int i = deliveryOptions.size(); i > 0; --i) {
//            String largestDeliveryOption = getLargestDeliveryOption(deliveryOptions);
//
//            // if all items have already been assigned there is no point in further calculations
//            if (deliveryOptions.get(largestDeliveryOption).isEmpty()) {
//                break;
//            }
//
//            List<String> deliveryOptionItems = new ArrayList<>(deliveryOptions.get(largestDeliveryOption).size());
//            for (String item : deliveryOptions.get(largestDeliveryOption)) {
//                for (int j = 0; j < itemCounts.get(item); ++i) {
//                    deliveryOptionItems.add(item);
//                }
//            }
//            splitBasket.put(largestDeliveryOption, deliveryOptionItems);
//
//            removeAssignedItems(deliveryOptions, largestDeliveryOption);
//        }
//
//        return splitBasket;
//    }

    protected void increaseItemCount(HashMap<String, Integer> itemCounts, String item) {
        if (!itemCounts.containsKey(item)) {
            itemCounts.put(item, 0);
        }
        itemCounts.replace(item, itemCounts.get(item) + 1);
    }

    protected void addItemToDeliveryOptions(HashMap<String, Set<String>> deliveryOptions, String item) {
        for (var deliveryOption : this.itemsDeliveryOptions.get(item)) {
            if (!deliveryOptions.containsKey(deliveryOption)) {
                deliveryOptions.put(deliveryOption, new HashSet<>());
            }
            deliveryOptions.get(deliveryOption).add(item);
        }
    }

    protected String getLargestDeliveryOption(HashMap<String, Set<String>> deliveryOptions) {
        String largestDeliveryOption = null;
        for (String deliveryOption : deliveryOptions.keySet()) {
            if (largestDeliveryOption == null || deliveryOptions.get(deliveryOption).size() > deliveryOptions.get(largestDeliveryOption).size()) {
                largestDeliveryOption = deliveryOption;
            }
        }

        return largestDeliveryOption;
    }

    protected void removeAssignedItems(HashMap<String, Set<String>> deliveryOptions, String largestDeliveryOption) {
        Set<String> largestDelivery = deliveryOptions.remove(largestDeliveryOption);
        for (String deliveryOption : deliveryOptions.keySet()) {
            deliveryOptions.get(deliveryOption).removeAll(largestDelivery);
        }
    }

}


















