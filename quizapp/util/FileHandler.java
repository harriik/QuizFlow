package quizapp.util;

import quizapp.model.Question;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


// Utility class for loading and parsing quiz questions from a file
public class FileHandler {
    
    // Load questions from a text file where each line contains one question and its options
    // File format: question|option1|option2|option3|option4|correctIndex|difficulty
    public static ArrayList<Question> loadQuestions(String filename) {
        ArrayList<Question> questions = new ArrayList<>(); // List to store parsed questions
        
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            // Read file line by line
            while ((line = br.readLine()) != null) {
                // Skip empty lines
                if (line.trim().isEmpty()) {
                    continue;
                }
                
                // Split line by pipe delimiter to extract question components
                String[] parts = line.split("\\|");
                
                // Validate that line has exactly 7 parts (question + 4 options + correct index + difficulty)
                if (parts.length == 7) {
                    // Extract each part from the split array
                    String text = parts[0]; // Question text
                    String op1 = parts[1]; // Option 1
                    String op2 = parts[2]; // Option 2
                    String op3 = parts[3]; // Option 3
                    String op4 = parts[4]; // Option 4
                    int correctIndex = Integer.parseInt(parts[5]); // Index of correct answer (1-4)
                    int difficulty = Integer.parseInt(parts[6]); // Difficulty level (1-3)
                    
                    // Create Question object and add to list
                    Question q = new Question(text, op1, op2, op3, op4, correctIndex, difficulty);
                    questions.add(q);
                }
            }
        } catch (IOException e) {
            // Print error if file cannot be read
            System.err.println("Error reading the questions file: " + e.getMessage());
        } catch (NumberFormatException e) {
            // Print error if correct index or difficulty cannot be parsed as integers
            System.err.println("Error parsing integers in questions file. Check index/difficulty format.");
        }
        
        return questions; // Return list (empty if file couldn't be read or no valid questions found)
    }
}
