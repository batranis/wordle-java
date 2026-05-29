import edu.willamette.cs1.wordle.WordleDictionary;
import edu.willamette.cs1.wordle.WordleGWindow;

import java.awt.Color;

public class Wordle {

    private WordleGWindow gw;
    private int row;
    private String secretWord;

    //creates base program like window, enter listener, and picks a target word
    public Wordle() {
        gw = new WordleGWindow();
        gw.addEnterListener((guess) -> enterAction(guess));

        row = 1;

        //picks random target word
        int random = (int) (Math.random() * WordleDictionary.FIVE_LETTER_WORDS.length);
        secretWord = WordleDictionary.FIVE_LETTER_WORDS[random];

        /* MILESTONE 1
        for (int i = 0; i < secretWord.length(); i++) {
            String letter = secretWord.substring(i, i+1);
            gw.setSquareLetter(0, i, letter);
        }
         */
    }

    //when enter is pressed it checks if it's within the wordlist and then colors it if it is
    public void enterAction(String guess) {
        guess = guess.toLowerCase();

        if (!inWordList(guess)) {
            gw.showMessage("Not in word list!");
        } else {
            gw.showMessage("In word list!");
            setColors(guess);
            keyboardColor(guess);
            nextOrEnd(guess);
        }
    }

    //loops through the dictionary to see if the guess is valid
    private boolean inWordList(String guess) {
        for (int i = 0; i < WordleDictionary.FIVE_LETTER_WORDS.length; i++) {
            if (guess.equals(WordleDictionary.FIVE_LETTER_WORDS[i])) {
                return true;
            }
        }
        return false;
    }

    //sets square colors with gray as the default, green for matches, and yellow for letters in wrong position
    private void setColors(String guess) {
        for (int i = 0; i < 5; i++) {
            gw.setSquareColor(row - 1, i, Color.gray);
        }

        for (int i = 0; i < 5; i++) {
            String guessLetter = guess.substring(i, i + 1);
            String targetLetter = secretWord.substring(i, i + 1);
            if (guessLetter.equals(targetLetter)) {
                gw.setSquareColor(row - 1, i, Color.green);
            }
        }

        String leftoverLetters = unusedLetters(guess);

        for (int i = 0; i < 5; i++) {
            String guessLetter = guess.substring(i, i + 1);
            String targetLetter = secretWord.substring(i, i + 1);

            //If the letter doesn't match, it checks if it is in the word at all. If it is, it gets colored yellow and then removed so it doesn't make two squares yellow.
            if (!guessLetter.equals(targetLetter)) {
                int place = leftoverLetters.indexOf(guessLetter);
                if (place != -1) {
                    gw.setSquareColor(row - 1, i, Color.yellow);
                    leftoverLetters = leftoverLetters.substring(0, place) + leftoverLetters.substring(place + 1);
                }
            }
        }
    }

    //makes a string of letters that weren't matched perfectly and uses it to figure out yellow squares
    private String unusedLetters(String guess) {
        String leftoverLetters = "";
        for (int i = 0; i < 5; i++) {
            String guessLetter = guess.substring(i, i + 1);
            String targetLetter = secretWord.substring(i, i + 1);
            if (!guessLetter.equals(targetLetter)) {
                leftoverLetters += targetLetter;
            }
        }
        return leftoverLetters;
    }

    //colors the keyboard keys gray if not in word, green if perfect match, and yellow if in wrong position
    private void keyboardColor(String guess) {
        for (int i = 0; i < 5; i++) {
            String guessLetter = guess.substring(i, i + 1);
            boolean letterFound = false;
            for (int j = 0; j < 5; j++) {
                if (guessLetter.equals(secretWord.substring(j, j + 1))) {
                    letterFound = true;
                }
            }
            if (!letterFound) {
                gw.setKeyColor(guessLetter.toUpperCase(), Color.gray);
            }
        }

        for (int i = 0; i < 5; i++) {
            String guessLetter = guess.substring(i, i + 1);
            String targetLetter = secretWord.substring(i, i + 1);
            if (guessLetter.equals(targetLetter)) {
                gw.setKeyColor(guessLetter.toUpperCase(), Color.green);
            } else if (gw.getSquareColor(row - 1, i).equals(Color.yellow)) {
                if (gw.getKeyColor(guessLetter.toUpperCase()) != Color.green) {
                    gw.setKeyColor(guessLetter.toUpperCase(), Color.yellow);
                }
            }
        }
    }

    //shows win message or shows the word or advances to the next row depending on varying conditions
    private void nextOrEnd(String guess) {
        if (guess.equals(secretWord)) {
            gw.showMessage("Good job, you found it!");
        } else if (row == 6) {
            gw.showMessage("The word was: " + secretWord);
        } else {
            gw.setCurrentRow(row++);
        }
    }

    public static void main(String[] args) {
        new Wordle();
    }

}