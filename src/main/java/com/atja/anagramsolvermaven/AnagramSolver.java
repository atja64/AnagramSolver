package com.atja.anagramsolvermaven;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

/**
 * An anagram solving program. Uses the list of words from
 * https://github.com/dwyl/english-words to create a database file using MapDB
 * which acts as a dictionary to check the validity of potential words.
 *
 * @author Ashley Allen
 */
public class AnagramSolver {

	private static final String SOURCE_DIR = System.getProperty("user.dir") + "\\src\\main\\java\\com\\atja\\anagramsolvermaven";
	private static final String WORDS_NAME = "words_alpha.txt";
	private static final String DATABASE_NAME = "dictionaryDB";
	private static DB dictionaryDB;
	private static HTreeMap<String, Boolean> dictionary;
	private static List<String> solutions;

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) {
		initlialise();

		String[] anagrams = {"i", "it", "was", "hand", "mouth", "elbows", "cranium", "specific", "jazziness"/*, "hinderance"*/};
		for (String anagram : anagrams) {
			solutions = new ArrayList<>();
			long startTime = System.nanoTime();
			solveAnagram(anagram, 0, anagram.length());
			System.out.println("Solutions for " + anagram + ": " + solutions.toString());
			System.out.println("Time taken: " + (System.nanoTime() - startTime) / 1_000_000 + "ms");
		}

		dictionaryDB.close();
	}

	/**
	 * Create/opens the dictionary database, creates/opens the dictionary
	 * HashMap and populates it if empty
	 */
	private static void initlialise() {
		dictionaryDB = DBMaker.fileDB(SOURCE_DIR + "\\" + DATABASE_NAME).make();
		dictionary = dictionaryDB.hashMap("dictionary", Serializer.STRING, Serializer.BOOLEAN).createOrOpen();
		if (dictionary.isEmpty()) {
			populateDictionary();
		} else {
			System.out.println("Dictionary already populated");
		}
	}

	/**
	 * Populates the dictionary HashMap with the words from the Words file
	 */
	private static void populateDictionary() {
		try (Scanner scanner = new Scanner(new File(SOURCE_DIR + "\\" + WORDS_NAME))) {
			System.out.println("Populating dictionary");
			int counter = 0;
			while (scanner.hasNext()) {
				dictionary.put(scanner.nextLine(), true);
				if (counter % 1000 == 0) {
					System.out.println(counter + " words added");
				}
				counter++;
			}
			dictionaryDB.commit();
			System.out.println("Dictionary populated");
		} catch (FileNotFoundException ex) {
			Logger.getLogger(AnagramSolver.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	/**
	 * Returns whether the given word is present in the dictionary HashMap
	 *
	 * @param word the word to check
	 * @return true if the word is present
	 */
	private static boolean isInDictionary(String word) {
		return dictionary.keySet().contains(word);
	}

	/**
	 * The main recursive function to solve the anagram. Essentially this
	 * function checks whether the letter currently being checked is at the end
	 * of the word. If so it checks whether the current word is in the
	 * dictionary and adds it to the list of solutions if so. It then iterates
	 * through each letter after the current one and swaps them (as long as the
	 * letters aren't the same) before calling solveAnagram() again on the new
	 * word. Solutions are stored in the global solutions ArrayList.
	 *
	 * @param anagram the word currently being checked
	 * @param start the letter currently being checked
	 * @param length the length of the word (to avoid multiple calls to
	 * .length()
	 */
	private static void solveAnagram(String anagram, int start, int length) {
//		System.out.println("solveAnagram(" + anagram + ", " + start + ", " + length + ")");
		if (start == length - 1) {
			if (isInDictionary(anagram)) {
				solutions.add(anagram);
			}
			return;
		}
		for (int i = start; i < length; i++) {
			if (anagram.charAt(start) != anagram.charAt(i) || start == i) {
				solveAnagram(swapChar(anagram, start, i), start + 1, length);
			} else {
//				System.out.println(anagram.charAt(start) + " matched " + anagram.charAt(i));
			}
		}
	}

	/**
	 * Swaps two characters in a given string.
	 *
	 * @param string the string in question
	 * @param i1 the index of the first character to swap
	 * @param i2 the index of the second character to swap
	 * @return the original string with the two characters swapped
	 */
	private static String swapChar(String string, int i1, int i2) {
		char[] charArray = string.toCharArray();
		char temp = charArray[i1];
		charArray[i1] = charArray[i2];
		charArray[i2] = temp;
		return new String(charArray);
	}

}
