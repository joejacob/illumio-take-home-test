# Coding Assignment - Avenger

Role: Illumio NCG Back-end Software Engineer, PCE Team

### General Comments

I was unable to finish the coding challenge within 75 minutes, so I just added method descriptions/logic explanations with my remaining time to explain my thought process/algorithms.


### Design and Algorithmic Decisions

All firewall rules were stored in a `RuleStore` object. This object was designed to be as compact and as flexible as possible, leaving the opportunity for introducing additional directions/protocols and adding/removing rules, if desired. The tree-like structure is as follows:

* A `RuleStore` object has a list of `Directions`.
* A `Direction` object has a list of `Protocols`.
* A `Protocol` object has a PriorityQueue of `Ports`.
* A `Port` object has a PriorityQueue of `IpAddresses`.
* An `IpAddress` object has a trie of `IpOctets`. 

This design allows for a trie-esque traversal within the `RuleStore` object. In order to simplify the distinction between no-range and range rules for `Ports` and `IpAddresses`, any no-range rules were made into ranges by making the range's bounds identical. This allowed for a cleaner handling of searching for `Ports` and `IpAddresses`. Additionally, the `IpAddress` value was split into its individual octets (casted as `chars`) so that searching for a matching IP address would be similar to searching within a prefix tree. Ultimately, this structure allows for fast searching/matching that is significantly faster than iterating over every single rule.

Since nodes in `RuleStore` can be identical at different levels, those nodes were reused to store the unique values of lower levels within the tree. For example, if two rules were both 'inbound', `RuleStore` would use the same `Direction` node to store the rules' values, which is possible by allowing `Protocol` and `Port` and `IPAddress` to have unique "child" nodes. The ability to reuse nodes reduces the space required to store all of the rules. 