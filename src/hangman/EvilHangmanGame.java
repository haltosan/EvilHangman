package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.regex.Pattern;

public class EvilHangmanGame implements IEvilHangmanGame{

    private HashSet<String> words;
    private final SortedSet<Character> guessedLetters;
    private final HashSet<String> patterns;
    private String[] curPatterns;
    private int wordLength;
    private int lastLettersGuessed;

    public EvilHangmanGame() {
        this.words = new HashSet<>();
        this.guessedLetters = new TreeSet<>();
        this.patterns = new HashSet<>();
        wordLength = 0;
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        this.wordLength = wordLength;
        Scanner scanner = new Scanner(dictionary);
        while(scanner.hasNext()){
            String word = scanner.next();
            if(word.length() == wordLength) {
                words.add(word);
            }
        }
        if(words.isEmpty()){
            throw new EmptyDictionaryException();
        }
        scanner.close();
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        guess = Character.toLowerCase(guess);
        if(guessedLetters.contains(guess)){
            throw new GuessAlreadyMadeException();
        }
        guessedLetters.add(guess);
        Set<String> out = getLargestPartition(guess);
        words = new HashSet<>(out);

        return out;
    }

    public String isGameDone(){
        if(words.size() > 1){
            return null;
        }
        int letterCount = 0;
        for(String pattern : patterns){
            int caretCount = 0;
            for(int i = 0; i < pattern.length(); i++){
                if(pattern.charAt(i) == '^'){
                    caretCount++;
                }
            }
            letterCount += wordLength - caretCount;
        }
        if(letterCount >= wordLength){
            return words.toString();
        }
        return null;
    }

    public String endGame(){
        return words.iterator().next();
    }

    private Set<String> getLargestPartition(char letter){
        String[][] partitions = getPartitions(letter);
        int maxLen = -1;
        int maxI = -1;
        for(int i = partitions.length - 1; i >= 0; i--){
            if(partitions[i].length >= maxLen){
                maxI = i;
                maxLen = partitions[i].length;
            }
        }
        patterns.add(this.curPatterns[maxI]);
        return new HashSet<>(List.of(partitions[maxI]));
    }

    private String[][] getPartitions(char letter){
        int length = (int)Math.pow(2, wordLength);
        String[][] out = new String[length][];
        this.curPatterns = new String[length];
        int i = 0;
        for(String pattern : treeTraversal(letter)){
            Set<String> matches = findMatches(pattern);
            out[i] = matches.toArray(new String[0]);
            curPatterns[i] = pattern;
            i++;
        }
        return out;
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

    private static String mask(boolean[] i, char letter){
        StringBuilder out = new StringBuilder();
        for(boolean digit : i){
            out.insert(0, (digit ? letter : "[^"+letter+"]" )); //true means we have the char at that spot
        }
        return out.toString();
    }

    private String[] treeTraversal(char letter){
        String[] out = new String[(int)Math.pow(2, wordLength)];
        int outIndex = 0;
        Queue<StructTreeNode> queue = new ArrayDeque<>();
        queue.add(new StructTreeNode(new boolean[wordLength], 0));
        while(!queue.isEmpty()){
            StructTreeNode top = queue.remove(); //remove top and visit node
            out[outIndex] = mask(top.o, letter);
            outIndex++;
            for(int i = top.i; i < wordLength; i++){ //add each child to the queue
                boolean[] o = top.o.clone();
                o[i] = true; //child generation
                queue.add(new StructTreeNode(o, i+1)); //child generation cont.
            }
        }

        return out;
    }

    public String getPatterns(){
        //System.out.println("gp words:" + words.toString());
        char[] word = new char[wordLength];
        for(String pattern : patterns){
            int patternIndex = 0;
            for(int i = 0; i < wordLength; i++){
                if(pattern.charAt(patternIndex) == '['){
                    patternIndex += 4; //len("[.^]")
                }
                else if(pattern.charAt(patternIndex) == '^'){
                    patternIndex += 2;
                }
                else if(pattern.charAt(patternIndex) == ']'){
                    patternIndex += 1;
                }
                else{
                    //System.out.println("gp pattern:" + pattern);
                    word[i] =  pattern.charAt(patternIndex); //we found the letter and need to insert to final word
                    patternIndex += 1;
                }
            }
        }

        return Arrays.toString(word);
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
