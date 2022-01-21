package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class EvilHangmanGame implements IEvilHangmanGame{

    public static void main(String[] args) throws EmptyDictionaryException, IOException {
        final int wordLength = 6;

        EvilHangmanGame eh = new EvilHangmanGame();
        File fBoi = new File("small.txt");
        eh.startGame(fBoi, wordLength);
        System.out.println(Arrays.toString(EvilHangmanGame.treeTraversal(3, 'a')));

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

    private String[] geLargestPartition(char letter, int wordLength){
        String[][] partitions = getPartitions(letter, wordLength);
        System.out.println(Arrays.deepToString(partitions)); //todo: remove
        int maxLen = -1;
        int maxI = -1;
        for(int i = partitions.length - 1; i >= 0; i--){
            if(partitions[i].length >= maxLen){
                maxI = i;
                maxLen = partitions[i].length;
            }
        }

        return partitions[maxI];
    }

    private String[][] getPartitions(char letter, int wordLength){
        int length = (int)Math.pow(2, wordLength);
        String[][] out = new String[length][];
        int i = 0;
        for(String pattern : EvilHangmanGame.genPatterns(letter, wordLength)){
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

    private static String[] genPatterns(char letter, int wordLength){
        final int length = (int)Math.pow(2, wordLength);
        String[] patterns = new String[length];
        int index = 0;
        for(int bitIndex = 0; bitIndex < wordLength; bitIndex++) {
            for (int i = 0; i < length; i++) {
                if(bitCount(i) == bitIndex) {
                    patterns[index] = EvilHangmanGame.mask(i, wordLength, letter);
                    index++;
                }
            }
        }
        return patterns;
    }

    private static String mask(int i, int wordLength, char letter){
        int index = 0;
        StringBuilder out = new StringBuilder();
        for(int l = 0; l < wordLength; l++){
            out.insert(0, (i % 2 == 0 ? "[^" + letter + "]" : letter));
            i = i >> 1;
        }
        return out.toString();
    }
    private static String mask(boolean[] i, char letter){
        StringBuilder out = new StringBuilder();
        for(boolean digit : i){
            out.insert(0, (digit ? letter : "[^"+letter+"]" )); //true means we have the char at that spot
        }
        return out.toString();
    }

    private static String[] treeTraversal(int wordLength, char letter){
        int length = (int)Math.pow(2, wordLength);
        String[] out = new String[length];
        int outIndex = 0;
        Queue<StructTreeNode> queue = new ArrayDeque<>();
        StructTreeNode blankStr = new StructTreeNode(wordLength, 0);
        queue.add(blankStr);
        while(!queue.isEmpty()){
            StructTreeNode top = queue.remove();
            out[outIndex] = mask(top.o, letter);
            outIndex++;
            for(int i = top.i; i < wordLength; i++){
                boolean[] o = top.o.clone();
                o[i] = true;
                queue.add(new StructTreeNode(o, i+1));
            }
        }

        return out;
    }

    private static int bitCount(int n){
        int count = 0;
        while(n != 0){
            if(n%2 == 1){
                count++;
            }
            n = n >> 1;
        }
        return count;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return guessedLetters;
    }
}
