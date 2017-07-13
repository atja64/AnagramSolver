# AnagramSolver

**A program that solves anagrams**

## Overview:

This program uses a list of words to create a database file using MapDB which 
acts as a dictionary to check the validity of potential words. The algorithm
uses recursion and therefore becomes quite slow for words more than 8 letters
in length.

## Instructions:

Edit the 'anagrams' string array in the source file to solve whichever anagrams you like.

## Dependancies

This program uses [MapDB](https://github.com/jankotek/mapdb/) to store and access the dictionary.

The words were taken from [here](https://github.com/dwyl/english-words).

## To-Do:

* Make the algorithm better for longer words
* Implement a UI so that users can enter words to solve

## Licenses

This software is licensed under the MIT License. Please see the [LICENSE](LICENSE) file for more information.

MapDB is licensed under the Apache-2.0 License. Please see the [LICENSE_Apache](LICENSE_Apache) file for more information.
