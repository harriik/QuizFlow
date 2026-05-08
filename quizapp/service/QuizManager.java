package quizapp.service;

import quizapp.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/*
 * QuizManager handles the complete quiz process:
 * - Stores questions
 * - Navigates through questions
 * - Checks answers
 * - Maintains score and progress
 */
public class QuizManager {

    // List containing all quiz questions
    private ArrayList<Question> questions;

    // Iterator used for sequential traversal of questions
    private Iterator<Question> questionIterator;

    // Stores currently active question
    private Question currentQuestion;

    // Stores user's score
    private int score;

    // Total number of quiz questions
    private int totalQuestions;

    // Number of processed questions
    private int processedCount;

    /*
     * Constructor:
     * Initializes quiz data, score, counters,
     * and sorts questions using natural ordering.
     */
    public QuizManager(ArrayList<Question> questions) {

        this.questions = questions;
        this.score = 0;

        // Store total question count
        this.totalQuestions = questions.size();

        // Initially no questions processed
        this.processedCount = 0;

        /*
         * Sort questions based on Comparable
         * implementation inside Question class.
         */
        Collections.sort(this.questions);

        // Create iterator for question navigation
        this.questionIterator = this.questions.iterator();

        this.currentQuestion = null;
    }

    /*
     * Sorts questions based on question text length.
     * Shorter questions appear first.
     */
    public void sortQuestionsByLength() {

        Collections.sort(this.questions, new Comparator<Question>() {

            @Override
            public int compare(Question q1, Question q2) {

                // Compare length of question text
                return Integer.compare(
                        q1.getText().length(),
                        q2.getText().length()
                );
            }
        });

        // Reset iterator after sorting
        this.questionIterator = this.questions.iterator();
    }

    /*
     * Checks whether more questions are available.
     */
    public boolean hasNextQuestion() {
        return questionIterator.hasNext();
    }

    /*
     * Returns the next question from iterator.
     * Also updates currentQuestion.
     */
    public Question getNextQuestion() {

        if (hasNextQuestion()) {

            // Move to next question
            currentQuestion = questionIterator.next();

            return currentQuestion;
        }

        // No more questions available
        return null;
    }

    /*
     * Validates user's selected answer.
     * Score increases if answer is correct.
     */
    public void checkAnswer(int selectedIndex) {

        if (currentQuestion != null &&
                selectedIndex == currentQuestion.getCorrectIndex()) {

            score++;
        }
    }

    /*
     * Tracks how many questions are processed.
     * Prevents count from exceeding total questions.
     */
    public void incrementProcessedCount() {

        if (processedCount < totalQuestions) {
            processedCount++;
        }
    }

    // Returns current score
    public int getScore() {
        return score;
    }

    // Returns total number of questions
    public int getTotalQuestions() {
        return totalQuestions;
    }

    // Returns number of processed questions
    public int getProcessedCount() {
        return processedCount;
    }
}