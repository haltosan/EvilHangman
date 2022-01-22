package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        File fileBoi = new File(args[0]);
        int wordLength = Integer.parseInt(args[1]);
        try {
            evilHangmanGame.startGame(fileBoi, wordLength);
        }
        catch (EmptyDictionaryException exception){
            System.out.println("No words found with the given dictionary and word length.");
            System.exit(1);
        }
        catch (IOException exception){
            System.out.println("Some IOException occurred. See stack trace.");
            exception.printStackTrace();
        }

        int guesses = Integer.parseInt(args[2]);

        Scanner input = new Scanner(System.in);
        while(guesses > 0){
            System.out.println("You have " + guesses + " guesses left");
            System.out.print("> ");
            String guess = input.next();
            try {
                System.out.println(evilHangmanGame.makeGuess(guess.charAt(0))); //todo: allow the pattern to pass up somehow so we can show the user
                guesses--;
            }
            catch (GuessAlreadyMadeException exception){
                System.out.println("Guess already made");
            }
        }
    }

}
