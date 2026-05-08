package quizapp;

import quizapp.model.Question;
import quizapp.service.QuizManager;
import quizapp.ui.QuizGUI;
import quizapp.util.FileHandler;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.ArrayList;

// Entry point for QuizFlow application
// Initializes questions, sets up UI theme, and launches the GUI
public class Main {

    // Main method - runs when the application starts
    public static void main(String[] args) {

        // Load questions from the questions.txt file
        String filename = "questions.txt";
        ArrayList<Question> questions = FileHandler.loadQuestions(filename);

        // Validate that questions were successfully loaded
        if (questions == null || questions.isEmpty()) {
            System.err.println(
                    "Failed to load questions. Please verify " + filename + " is present and properly formatted.");
            return; // Exit if no questions available
        }

        // Attempt to set Nimbus look and feel for modern UI appearance
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Silently ignore if Nimbus theme cannot be set (will use default theme)
        }

        // Create quiz manager with loaded questions
        QuizManager sessionManager = new QuizManager(questions);

        // Sort questions by text length (shorter questions first)
        sessionManager.sortQuestionsByLength();

        // Launch GUI on the Event Dispatch Thread (required for Swing)
        SwingUtilities.invokeLater(() -> {
            // Create and display the main quiz window
            QuizGUI quizInterface = new QuizGUI(sessionManager);
            quizInterface.setVisible(true);
        });

    }
}
