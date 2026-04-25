package quizapp;

import quizapp.model.Question;
import quizapp.service.QuizManager;
import quizapp.ui.QuizGUI;
import quizapp.util.FileHandler;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.util.ArrayList;


public class Main {
    
    public static void main(String[] args) {
        
        String filename = "questions.txt";
        ArrayList<Question> questions = FileHandler.loadQuestions(filename);
        
        if (questions == null || questions.isEmpty()) {
            System.err.println("Failed to load questions. Please verify " + filename + " is present and properly formatted.");
            return;
        }
        
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // Ignore error and fall back to Swing Default
        }

        QuizManager sessionManager = new QuizManager(questions);
        
        sessionManager.sortQuestionsByLength();

        SwingUtilities.invokeLater(() -> {
            QuizGUI quizInterface = new QuizGUI(sessionManager);
            quizInterface.setVisible(true);
        });
        
    }
}
