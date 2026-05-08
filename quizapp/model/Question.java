package quizapp.model;

/*
 * Question class represents a single quiz question.
 * It stores:
 * - Question text
 * - Four answer options
 * - Correct answer index
 * - Difficulty level
 *
 * Implements Comparable to allow sorting
 * questions based on difficulty.
 */
public class Question implements Comparable<Question> {

    // Stores question statement
    private String text;

    // Array containing 4 answer options
    private String[] options;

    // Stores index of correct answer
    private int correctIndex;

    // Difficulty level of question
    private int difficulty;

    /*
     * Constructor:
     * Initializes question details and options.
     */
    public Question(String text,
                    String op1,
                    String op2,
                    String op3,
                    String op4,
                    int correctIndex,
                    int difficulty) {

        // Assign question text
        this.text = text;

        // Store all options into array
        this.options = new String[]{op1, op2, op3, op4};

        // Correct answer position
        this.correctIndex = correctIndex;

        // Difficulty level
        this.difficulty = difficulty;
    }

    // Returns question text
    public String getText() {
        return text;
    }

    // Returns all answer options
    public String[] getOptions() {
        return options;
    }

    // Returns correct option index
    public int getCorrectIndex() {
        return correctIndex;
    }

    // Returns difficulty level
    public int getDifficulty() {
        return difficulty;
    }

    /*
     * compareTo() method is used for sorting.
     * Questions are sorted based on difficulty.
     *
     * Smaller difficulty value -> easier question
     * Larger difficulty value -> harder question
     */
    @Override
    public int compareTo(Question o) {

        return Integer.compare(
                this.difficulty,
                o.difficulty
        );
    }
}