package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.regex.Pattern;

public class EvilHangmanGame implements IEvilHangmanGame{

    public static void main(String[] args) throws EmptyDictionaryException, IOException, GuessAlreadyMadeException {
        final int wordLength = 7;

        EvilHangmanGame eh = new EvilHangmanGame();
        File fBoi = new File("small.txt");
        eh.startGame(fBoi, wordLength);
        System.out.println(eh.makeGuess('a'));
        System.out.println(eh.makeGuess('e'));
        System.out.println(eh.makeGuess('i'));
        System.out.println(eh.makeGuess('o'));

    }

    private Set<String> words;
    private final SortedSet<Character> guessedLetters;
    private int wordLength;

    public EvilHangmanGame() {
        this.words = new HashSet<>();
        this.guessedLetters = new TreeSet<>();
        wordLength = 0;
    }

    //todo: add word selection / win detection
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

        return new HashSet<String>(List.of(partitions[maxI]));
    }

    private String[][] getPartitions(char letter){
        int length = (int)Math.pow(2, wordLength);
        String[][] out = new String[length][];
        int i = 0;
        for(String pattern : treeTraversal(letter)){
            Set<String> matches = findMatches(pattern);
            out[i] = matches.toArray(new String[0]);
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

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
