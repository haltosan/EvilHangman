package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EvilHangmanGame implements IEvilHangmanGame{

    public static void main(String[] args) throws EmptyDictionaryException, IOException {
        EvilHangmanGame eh = new EvilHangmanGame();
        File fBoi = new File("small.txt");
        eh.startGame(fBoi, 6);
        System.out.println(eh.words);

        System.out.println(Arrays.toString(EvilHangmanGame.genPatterns('a', 4)));
    }

    private Set<String> words;
    private final SortedSet<Character> guessedLetters;

    public EvilHangmanGame() {
        this.words = new HashSet<>();
        this.guessedLetters = new TreeSet<>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);
        while(scanner.hasNext()){
            String word = scanner.next();
            if(word.length() == wordLength) {
                words.add(word);
            }
        }
        scanner.close();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        return null;
    }

    private Set<String> findMatches(String pattern){
        Set<String> results = new HashSet<>();
        Pattern curPattern;
        curPattern = Pattern.compile(pattern);
        for(String word : words){
            if(curPattern.matcher(word).find()){
                results.add(word);
            }
        }
        return results;
    }

    private static String[] genPatterns(char letter, int wordLength){
        final int length = (int)Math.pow(2, wordLength);
        String[] patterns = new String[length];
        for(int i = 0; i < length; i++){
            patterns[i] = EvilHangmanGame.mask(i, wordLength, letter);
        }
        return patterns;
    }

    private static String mask(int i, int wordLength, char letter){
        int index = 0;
        StringBuilder out = new StringBuilder();
        for(int l = 0; l < wordLength; l++){
            out.append(i % 2 == 0 ? "[^" + letter + "]" : letter);
            i = i >> 1;
        }
        return out.toString();
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
