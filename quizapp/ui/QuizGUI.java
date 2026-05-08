package quizapp.ui;

import quizapp.model.Question;
import quizapp.service.QuizManager;
import quizapp.util.TimerThread;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

// Main GUI class for the Quiz application using Swing framework
public class QuizGUI extends JFrame {
    private QuizManager quizManager; // Manages quiz state and question logic
    private JPanel mainPanel; // Container for multiple panels (Welcome, Quiz, Score)
    private CardLayout cardLayout; // Layout to switch between different screens

    // UI Components for displaying questions and options
    private JTextArea questionLabel; // Displays the current question text
    private JRadioButton[] optionButtons; // Array of 4 radio buttons for answer options
    private ButtonGroup buttonGroup; // Groups radio buttons so only one can be selected
    private JButton nextButton; // Button to proceed to next question
    private JLabel timerLabel; // Displays remaining time for current question
    private JProgressBar progressBar; // Shows quiz progress (current question / total)
    private TimerThread timerThread; // Separate thread for countdown timer
    
    // Time limit per question in seconds
    private static final int TIME_PER_QUESTION = 15;
    
    // Color scheme constants for consistent UI appearance
    private final Color bgColor = new Color(243, 246, 249); // Light gray background
    private final Color cardColor = Color.WHITE; // White for card panels
    private final Color primaryColor = new Color(41, 128, 185); // Blue for primary elements
    private final Color textColor = new Color(44, 62, 80); // Dark gray for text

    // Constructor: Initializes the GUI with quiz manager and creates all panels
    public QuizGUI(QuizManager manager) {
        this.quizManager = manager;
        setupFrame(); // Configure window properties
        
        // Setup CardLayout to enable switching between different screens
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(bgColor);
        
        // Add welcome and quiz panels to the main panel
        mainPanel.add(createWelcomePanel(), "Welcome"); // Welcome screen with start button
        mainPanel.add(createQuizPanel(), "Quiz"); // Quiz screen with questions and options
        
        add(mainPanel);
        cardLayout.show(mainPanel, "Welcome"); // Display welcome screen first
    }

    // Configure the main window properties (title, size, close behavior, position)
    private void setupFrame() {
        setTitle("QuizFlow");
        setSize(750, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Close application when window closes
        setLocationRelativeTo(null); // Center window on screen
    }

    // Creates the welcome screen panel shown when application starts
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        
        // GridBagConstraints for center alignment
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Decorative icon at the top
        JLabel mainIcon = new JLabel("\u2728");
        mainIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        mainIcon.setForeground(new Color(241, 196, 15));
        mainIcon.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Main title
        JLabel titleLabel = new JLabel("Welcome to QuizFlow");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        titleLabel.setForeground(primaryColor);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Subtitle describing the application
        JLabel subtitleLabel = new JLabel("Test your knowledge and beat the clock in this interactive quiz.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 45, 0));
        
        // Panel to hold action buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bgColor);
        
        // START QUIZ button - transitions to quiz screen
        JButton startButton = new JButton("START QUIZ");
        stylePrimaryButton(startButton, primaryColor);
        startButton.setPreferredSize(new Dimension(200, 55));
        
        // RULES button - shows quiz rules in a dialog
        JButton rulesButton = new JButton("RULES");
        stylePrimaryButton(rulesButton, new Color(149, 165, 166));
        rulesButton.setPreferredSize(new Dimension(150, 55));
        
        // Start button listener: switch to quiz panel and load first question
        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Quiz");
            loadNextQuestion();
        });
        
        // Rules button listener: display quiz rules in info dialog
        rulesButton.addActionListener(e -> {
             String rulesText = "<html><body style='width: 300px; padding: 10px;'>" +
                    "<h2 style='color: #2980b9;'>QuizFlow Rules</h2>" +
                    "<ul>" +
                    "<li>You have <b>" + TIME_PER_QUESTION + " seconds</b> to answer each question.</li>" +
                    "<li>If you run out of time, you have to skip the question.</li>" +
                    "<li>Think fast and aim for a 100% score!</li>" +
                    "</ul></body></html>";
            JOptionPane.showMessageDialog(this, rulesText, "How to Play", JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(rulesButton);
        
        // Add all components to panel in centered layout
        panel.add(mainIcon, gbc);
        panel.add(titleLabel, gbc);
        panel.add(subtitleLabel, gbc);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }

    // Creates the main quiz screen with question, options, timer, and progress
    private JPanel createQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        // TOP PANEL: Contains logo, timer, and progress bar
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(cardColor);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(223, 230, 233)),
                new EmptyBorder(15, 25, 15, 25)
        ));

        // Inner panel for logo and timer side by side
        JPanel topInnerPanel = new JPanel(new BorderLayout());
        topInnerPanel.setBackground(cardColor);
        
        // App logo on the left
        JLabel logoLabel = new JLabel("QuizFlow");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoLabel.setForeground(primaryColor);
        topInnerPanel.add(logoLabel, BorderLayout.WEST);

        // Timer display on the right (shows remaining seconds)
        timerLabel = new JLabel("Time: " + TIME_PER_QUESTION + "s");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timerLabel.setForeground(new Color(231, 76, 60)); // Red color for emphasis
        topInnerPanel.add(timerLabel, BorderLayout.EAST);
        
        topPanel.add(topInnerPanel, BorderLayout.NORTH);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        
        // Progress bar showing quiz completion (e.g., "3 / 10 Completed")
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113)); // Green progress color
        progressBar.setBackground(new Color(236, 240, 241));
        progressBar.setPreferredSize(new Dimension(panel.getWidth(), 20));
        progressBar.setBorderPainted(false);
        topPanel.add(progressBar, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);

        // CENTER PANEL: Contains the question and answer options
        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(bgColor);

        // Content card - styled panel for question and options
        JPanel contentCard = new JPanel();
        contentCard.setLayout(new BoxLayout(contentCard, BoxLayout.Y_AXIS));
        contentCard.setBackground(cardColor);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 218, 226), 1, true),
            new EmptyBorder(40, 50, 40, 50)
        ));
        contentCard.setPreferredSize(new Dimension(650, 320));

        // Text area to display the current question
        questionLabel = new JTextArea("Loading...");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        questionLabel.setForeground(textColor);
        questionLabel.setBackground(cardColor);
        questionLabel.setLineWrap(true); // Wrap long questions to multiple lines
        questionLabel.setWrapStyleWord(true);
        questionLabel.setEditable(false);
        questionLabel.setFocusable(false);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentCard.add(questionLabel);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30))); // Spacing below question

        // Array of 4 radio buttons for answer options (only one can be selected at a time)
        optionButtons = new JRadioButton[4];
        buttonGroup = new ButtonGroup();
        
        Font optionFont = new Font("Segoe UI", Font.PLAIN, 18);
        for (int i = 0; i < 4; i++) {
            optionButtons[i] = new JRadioButton();
            optionButtons[i].setFont(optionFont);
            optionButtons[i].setForeground(textColor);
            optionButtons[i].setBackground(cardColor);
            optionButtons[i].setFocusPainted(false);
            optionButtons[i].setCursor(new Cursor(Cursor.HAND_CURSOR));
            optionButtons[i].setAlignmentX(Component.LEFT_ALIGNMENT);
            buttonGroup.add(optionButtons[i]); // Add to group for mutual exclusivity
            contentCard.add(optionButtons[i]);
            contentCard.add(Box.createRigidArea(new Dimension(0, 15))); // Spacing between options
        } 
        
        // Center the content card in the wrapper panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(contentCard, gbc);
        panel.add(centerWrapper, BorderLayout.CENTER);

        // BOTTOM PANEL: Contains the "Next Question" button
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(bgColor);
        bottomPanel.setBorder(new EmptyBorder(10, 30, 20, 32));
        
        // Next button - advances to next question after checking answer
        nextButton = new JButton("Next Question >");
        stylePrimaryButton(nextButton, primaryColor);
        nextButton.setPreferredSize(new Dimension(200, 50));

        // Next button listener: record answer and move to next question
        nextButton.addActionListener(e -> handleNext(true));
        
        bottomPanel.add(nextButton);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // Apply consistent button styling (color, font, cursor, border)
    private void stylePrimaryButton(JButton button, Color bg) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false); // Remove focus border
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Show hand cursor on hover
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    // Load and display the next question from quiz manager
    private void loadNextQuestion() {
        if (quizManager.hasNextQuestion()) {
            // Get the next question object from quiz manager
            Question q = quizManager.getNextQuestion();
            
            // Display question text
            questionLabel.setText(q.getText());
            
            // Populate the 4 radio buttons with answer options
            String[] options = q.getOptions();
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setActionCommand(String.valueOf(i + 1)); // Store option index (1-4)
            }
            
            // Clear any previously selected option
            buttonGroup.clearSelection();
            
            // Update progress bar to show current quiz progress
            int currentProgress = (int) (((double) quizManager.getProcessedCount() / quizManager.getTotalQuestions()) * 100);
            progressBar.setValue(currentProgress);
            progressBar.setString(quizManager.getProcessedCount() + " / " + quizManager.getTotalQuestions() + " Completed");
            
            // Start the countdown timer for this question
            startTimer();
            mainPanel.requestFocusInWindow();
        } else {
            // No more questions - show final score screen
            showFinalScore();
        }
    }

    // Start a new countdown timer for the current question
    private void startTimer() {
        // Stop and interrupt any existing timer thread
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.stopTimer();
            timerThread.interrupt();
        }
        
        // Reset timer display
        timerLabel.setText("Time: " + TIME_PER_QUESTION + "s");

        // Create new timer thread with callbacks for tick and timeout
        timerThread = new TimerThread(TIME_PER_QUESTION, 
            // onTick callback: Update timer display every second
            () -> {
                int timeRemaining = timerThread.getTimeRemaining();
                timerLabel.setText("Time: " + timeRemaining + "s");
                
                // Change color to dark red when time is running out (5 seconds or less)
                if (timeRemaining <= 5) {
                    timerLabel.setForeground(new Color(192, 57, 43));
                } else {
                    timerLabel.setForeground(textColor);
                }
            },
            // onTimeUp callback: Show warning and advance to next question
            () -> {
                JOptionPane.showMessageDialog(this, "Time is up! Moving forward...", "Timeout", JOptionPane.WARNING_MESSAGE);
                handleNext(false); // false = don't check user answer (timeout)
            }
        );
        timerThread.start();
    }

    // Handle moving to next question and recording the user's answer if provided
    private void handleNext(boolean checkUserAnswer) {
        // Stop the current timer
        if (timerThread != null) {
            timerThread.stopTimer();
            timerThread.interrupt();
        }
        
        // If button was clicked (not timeout), check the user's selected answer
        if (checkUserAnswer) {
            int selectedIndex = -1;
            // Find which radio button is selected (if any)
            for (int i = 0; i < 4; i++) {
                if (optionButtons[i].isSelected()) {
                    selectedIndex = i + 1; // Convert 0-based to 1-based index
                    break;
                }
            }
            // Submit answer to quiz manager for scoring
            if (selectedIndex != -1) {
                quizManager.checkAnswer(selectedIndex);
            }
        }
        
        // Mark question as processed regardless of whether answer was provided
        quizManager.incrementProcessedCount();
        
        // Load and display the next question
        loadNextQuestion();
    }

    // Display the final score screen after quiz completion
    private void showFinalScore() {
        // Stop any remaining timer
        if (timerThread != null) {
            timerThread.stopTimer();
        }
        
        // Get final score from quiz manager
        int score = quizManager.getScore();
        int total = quizManager.getTotalQuestions();
        
        // Create and display score panel
        JPanel scorePanel = createScorePanel(score, total);
        mainPanel.add(scorePanel, "Score");
        cardLayout.show(mainPanel, "Score");
    }

    // Create the final score panel showing results and feedback
    private JPanel createScorePanel(int score, int total) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        // Calculate percentage and choose appropriate greeting message
        double percentage = ((double) score / total) * 100;
        String greeting = percentage >= 80 ? "Outstanding!" : (percentage >= 50 ? "Good Job!" : "Keep Practicing.");
        
        // Display motivational message based on performance
        JLabel greetingLabel = new JLabel(greeting);
        greetingLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        greetingLabel.setForeground(primaryColor);
        greetingLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        // Display the final score (e.g., "You scored 8 out of 10.")
        JLabel scoreLabel = new JLabel(String.format("You scored %d out of %d.", score, total));
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        scoreLabel.setForeground(textColor);
        scoreLabel.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        // Exit button to close the application
        JButton exitButton = new JButton("EXIT APP");
        stylePrimaryButton(exitButton, new Color(231, 76, 60)); // Red color for exit
        exitButton.setPreferredSize(new Dimension(200, 55));
        
        // Exit button listener: terminate the application
        exitButton.addActionListener(e -> System.exit(0));
        
        panel.add(greetingLabel, gbc);
        panel.add(scoreLabel, gbc);
        panel.add(exitButton, gbc);
        
        return panel;
    }
}
