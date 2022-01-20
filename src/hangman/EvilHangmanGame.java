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
        eh.startGame(fBoi, 3);
        System.out.println(eh.words);

        System.out.println(eh.findMatches("a[^a][^a]"));
        for(int i = 0; i < 3*3-1; i++){  //correct way to gen all possible regex's
            System.out.println(eh.mask(i, 3, 'a'));
        }
    }

    //TODO: make both private
    public Set<String> words;
    public SortedSet<Character> guessedLetters;

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

    //TODO: make private
    public Set<String> findMatches(String pattern){
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

    //TODO: genPatterns
    public String[] genPatterns(char letter, int wordLength){
        /*
        i = 0;
        do a mask over each digit in i_2
        if 1, select char
        else exclude char
         */


        return null;
    }

    //TODO: make private

    /**
     *
     * @param i mask index; see genPatterns for clarity
     * @param wordLength
     * @return regex for the given mask index
     */
    public String mask(int i, int wordLength, char letter){
        int index = 0;
        String out = "";
        for(int l = 0; l < wordLength; l++){
            out = out + (i%2 == 0 ? "[^"+letter+"]" : letter);
            i = i >> 1;
        }
        return out;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
