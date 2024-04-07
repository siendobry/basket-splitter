## Optimal basket splitter

### An optimal implementation of a basket splitting algorithm.

### Overview
BasketSplitter class can be constructed with an absolute filepath (String) to a JSON file containing `item -> delivery options` relationships, e. g.:
`{
  "Flower": ["Flower Shop1", "Flower Shop2"]
}`,
which means that Flower item can be delivered with Flower Shop1 or Flower Shop2

BasketSplitter's split method takes in a list of items (List<String>) and generates most optimal mappings of form: `delivery option -> item list`  
Split method's return type is Map<String, List<String>>
