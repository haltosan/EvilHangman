package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class EvilHangman { //evilHangman dictFile wordLen guessMax

    public static void main(String[] args) {
        EvilHangmanGame evilHangmanGame = new EvilHangmanGame();
        String fileName = "small.txt";
        File fileBoi = new File(fileName/*args[0]*/);
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
            System.out.println("You have guessed " + evilHangmanGame.getGuessedLetters());
            System.out.println("You have " + guesses + " guesses left");
            System.out.println("Here is your word so far: " + evilHangmanGame.getPatterns());
            System.out.print("> ");
            String guess = input.next();
            char guessLetter = guess.charAt(0);
            if(guessLetter < 'a' || guessLetter > 'z'){
                System.out.println("Please input [a-z]{1}\n");
                continue;
            }
            try {
                evilHangmanGame.makeGuess(guessLetter);
                guesses--;
            }
            catch (GuessAlreadyMadeException exception){
                System.out.println("Guess already made");
            }
            String isGameDone = evilHangmanGame.isGameDone();
            if(isGameDone != null){
                System.out.println("You won. Here's the word.");
                System.out.println(isGameDone);
                System.exit(0);
            }
        }
        System.out.println("You lost! This was the word.");
        System.out.println(evilHangmanGame.endGame());
    }

}
