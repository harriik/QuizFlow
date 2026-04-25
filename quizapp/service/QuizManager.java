package quizapp.service;

import quizapp.model.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

/**
 * QuizManager handles the core logic of iterating over questions and keeping track of the score.
 */
public class QuizManager {
    private ArrayList<Question> questions;
    private Iterator<Question> questionIterator;
    private Question currentQuestion;
    private int score;
    private int totalQuestions;
    private int processedCount;

    public QuizManager(ArrayList<Question> questions) {
        this.questions = questions;
        this.score = 0;
        this.totalQuestions = questions.size();
        this.processedCount = 0; // Tracks questions completely verified or timed out
        
        // Sorting using Comparable (by difficulty)
        Collections.sort(this.questions);
        
        this.questionIterator = this.questions.iterator();
        this.currentQuestion = null;
    }

    public void sortQuestionsByLength() {
        Collections.sort(this.questions, new Comparator<Question>() {
            @Override
            public int compare(Question q1, Question q2) {
                return Integer.compare(q1.getText().length(), q2.getText().length());
            }
        });
        this.questionIterator = this.questions.iterator();
    }

    public boolean hasNextQuestion() {
        return questionIterator.hasNext();
    }

    public Question getNextQuestion() {
        if (hasNextQuestion()) {
            currentQuestion = questionIterator.next();
            return currentQuestion;
        }
        return null;
    }

    public void checkAnswer(int selectedIndex) {
        if (currentQuestion != null && selectedIndex == currentQuestion.getCorrectIndex()) {
            score++;
        }
    }
    
    public void incrementProcessedCount() {
        if (processedCount < totalQuestions) {
            processedCount++;
        }
    }

    public int getScore() {
        return score;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }
    
    public int getProcessedCount() {
        return processedCount;
    }
}
