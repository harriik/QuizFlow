package quizapp.model;

/**
 * Question model class that implements Comparable to sort by difficulty.
 */
public class Question implements Comparable<Question> {
    private String text;
    private String[] options;
    private int correctIndex;
    private int difficulty;

    public Question(String text, String op1, String op2, String op3, String op4, int correctIndex, int difficulty) {
        this.text = text;
        
        // Storing options in an array
        this.options = new String[]{op1, op2, op3, op4};
        this.correctIndex = correctIndex;
        this.difficulty = difficulty;
    }

    public String getText() {
        return text;
    }

    public String[] getOptions() {
        return options;
    }

    public int getCorrectIndex() {
        return correctIndex;
    }

    public int getDifficulty() {
        return difficulty;
    }

    // Sort by difficulty ascending
    @Override
    public int compareTo(Question o) {
        return Integer.compare(this.difficulty, o.difficulty);
    }
}
