package quizapp.util;

import quizapp.model.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * FileHandler takes care of loading questions and parsing the text dataset.
 */
public class FileHandler {
    
    /**
     * Reads questions from a pipe-delimited text file.
     * @param filename Path to the questions text file
     * @return ArrayList of loaded Question objects
     */
    public static ArrayList<Question> loadQuestions(String filename) {
        ArrayList<Question> questions = new ArrayList<>();
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Ignore empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Parse format: Question|Option1|Option2|Option3|Option4|CorrectIndex|Difficulty
                String[] parts = line.split("\\|");
                
                if (parts.length == 7) {
                    String text = parts[0];
                    String op1 = parts[1];
                    String op2 = parts[2];
                    String op3 = parts[3];
                    String op4 = parts[4];
                    int correctIndex = Integer.parseInt(parts[5]);
                    int difficulty = Integer.parseInt(parts[6]);
                    
                    Question q = new Question(text, op1, op2, op3, op4, correctIndex, difficulty);
                    questions.add(q);
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading the questions file: " + e.getMessage());
        } catch (NumberFormatException e) {
            System.err.println("Error parsing integers in questions file. Check index/difficulty format.");
        }
        
        return questions;
    }
}
