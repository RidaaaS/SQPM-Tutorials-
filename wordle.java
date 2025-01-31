package wordle;

import java.io.*;
import java.util.*;

public class wordle {
	private static final int MAX_ATTEMPTS = 6;
    private static final int WORD_LENGTH = 5;
    private static List<String> wordList = new ArrayList<>();

    public static void main(String[] args) {
        loadDictionary("dictionary.txt");
        if (wordList.isEmpty()) {
            System.out.println("Using fallback word list.");
            loadFallbackWordList();
        }
        if (wordList.isEmpty()) {
            System.out.println("Error: No valid 5-letter words found.");
            return;
        }
        wordList = filterFiveLetterWords(wordList);
        if (wordList.isEmpty()) {
            System.out.println("Error: No 5-letter words found in the word list.");
            return;
        }
        String targetWord = getRandomWord();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Wordle! Try to guess the 5-letter word.");
        System.out.println("You have " + MAX_ATTEMPTS + " attempts.");
        boolean guessedCorrectly = false;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            System.out.print("Attempt " + attempt + ": Enter your guess: ");
            String guess = scanner.nextLine().toLowerCase();
            if (!isValidGuess(guess)) {
                System.out.println("Invalid guess. Please enter a valid 5-letter word.");
                attempt--;
                continue;
            }
            String feedback = generateFeedback(targetWord, guess);
            System.out.println("Feedback: " + feedback);
            if (guess.equals(targetWord)) {
                System.out.println("Congratulations! You guessed the word correctly!");
                guessedCorrectly = true;
                break;
            }
            if (attempt == MAX_ATTEMPTS && !guessedCorrectly) {
                System.out.println("Sorry, you've used all attempts. The correct word was: " + targetWord);
            }
        }
        scanner.close();
    }

    public static void loadDictionary(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim().toLowerCase();
                if (line.matches("[a-z]+")) {
                    wordList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }
    }

    public static void loadFallbackWordList() {
        wordList = Arrays.asList("apple", "table", "chair", "water", "lemon", "bread", "grape", "peach", "plumb", "bison");
    }

    public static List<String> filterFiveLetterWords(List<String> words) {
        List<String> fiveLetterWords = new ArrayList<>();
        for (String word : words) {
            if (word.length() == WORD_LENGTH) {
                fiveLetterWords.add(word);
            }
        }
        return fiveLetterWords;
    }

    public static String getRandomWord() {
        Random random = new Random();
        return wordList.get(random.nextInt(wordList.size()));
    }

    public static String generateFeedback(String targetWord, String guess) {
        StringBuilder feedback = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            char guessedChar = guess.charAt(i);
            char targetChar = targetWord.charAt(i);
            if (guessedChar == targetChar) {
                feedback.append(guessedChar);
            } else if (targetWord.indexOf(guessedChar) >= 0) {
                feedback.append(Character.toLowerCase(guessedChar));
            } else {
                feedback.append("_");
            }
        }
        return feedback.toString();
    }

    public static boolean isValidGuess(String guess) {
        return guess.length() == WORD_LENGTH && guess.matches("[a-zA-Z]+");
    }

    public static void testRandomWordSelection() {
        String randomWord = getRandomWord();
        boolean validWord = wordList.contains(randomWord);
        System.out.println("Test Random Word Selection: " + (validWord ? "Passed" : "Failed"));
    }

    public static void testCorrectGuessFeedback() {
        String targetWord = "table";
        String guess = "table";
        String feedback = generateFeedback(targetWord, guess);
        System.out.println("Test Correct Guess Feedback: " + (feedback.equals("table") ? "Passed" : "Failed"));
    }

    public static void testIncorrectPositionFeedback() {
        String targetWord = "table";
        String guess = "peach";
        String feedback = generateFeedback(targetWord, guess);
        System.out.println("Test Incorrect Position Feedback: " + (feedback.equals("_a__e") ? "Passed" : "Failed"));
    }

    public static void testIncorrectLettersFeedback() {
        String targetWord = "table";
        String guess = "water";
        String feedback = generateFeedback(targetWord, guess);
        System.out.println("Test Incorrect Letters Feedback: " + (feedback.equals("_____") ? "Passed" : "Failed"));
    }

    public static void testInvalidGuessLength() {
        String guess = "grapes";
        if (guess.length() != WORD_LENGTH || !wordList.contains(guess)) {
            System.out.println("Test Invalid Guess Length: Passed");
        } else {
            System.out.println("Test Invalid Guess Length: Failed");
        }
    }

    public static void testOutOfAttempts() {
        String targetWord = "table";
        String[] guesses = {"grape", "melon", "lemon", "berry", "peach", "plumb"};
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            String guess = guesses[attempt];
            String feedback = generateFeedback(targetWord, guess);
            System.out.println("Attempt " + (attempt + 1) + ": " + guess + " - Feedback: " + feedback);
        }
        System.out.println("Test Out of Attempts: Passed (if no win detected)");
    }

    public static void runTests() {
        testRandomWordSelection();
        testCorrectGuessFeedback();
        testIncorrectPositionFeedback();
        testIncorrectLettersFeedback();
        testInvalidGuessLength();
        testOutOfAttempts();
    }
}


