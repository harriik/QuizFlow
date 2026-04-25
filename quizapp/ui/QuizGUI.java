package quizapp.ui;

import quizapp.model.Question;
import quizapp.service.QuizManager;
import quizapp.util.TimerThread;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class QuizGUI extends JFrame {
    private QuizManager quizManager;
    private JPanel mainPanel;
    private CardLayout cardLayout;

    private JLabel questionLabel;
    private JRadioButton[] optionButtons;
    private ButtonGroup buttonGroup;
    private JButton nextButton;
    private JLabel timerLabel;
    private JProgressBar progressBar;
    private TimerThread timerThread;
    
    private static final int TIME_PER_QUESTION = 15;
    
    private final Color bgColor = new Color(243, 246, 249);
    private final Color cardColor = Color.WHITE;
    private final Color primaryColor = new Color(41, 128, 185);
    private final Color textColor = new Color(44, 62, 80);

    public QuizGUI(QuizManager manager) {
        this.quizManager = manager;
        setupFrame();
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(bgColor);
        
        mainPanel.add(createWelcomePanel(), "Welcome");
        mainPanel.add(createQuizPanel(), "Quiz");
        
        add(mainPanel);
        cardLayout.show(mainPanel, "Welcome");
    }

    private void setupFrame() {
        setTitle("QuizFlow");
        setSize(750, 580);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
    }

    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        JLabel mainIcon = new JLabel("\u2728"); 
        mainIcon.setFont(new Font("Segoe UI", Font.PLAIN, 48));
        mainIcon.setForeground(new Color(241, 196, 15));
        mainIcon.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel titleLabel = new JLabel("Welcome to QuizFlow");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        titleLabel.setForeground(primaryColor);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        JLabel subtitleLabel = new JLabel("Test your knowledge and beat the clock in this interactive quiz.");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        subtitleLabel.setForeground(new Color(127, 140, 141));
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 45, 0));
        
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(bgColor);
        
        JButton startButton = new JButton("START QUIZ");
        stylePrimaryButton(startButton, primaryColor);
        startButton.setPreferredSize(new Dimension(200, 55));
        
        JButton rulesButton = new JButton("RULES");
        stylePrimaryButton(rulesButton, new Color(149, 165, 166)); 
        rulesButton.setPreferredSize(new Dimension(150, 55));
        
        startButton.addActionListener(e -> {
            cardLayout.show(mainPanel, "Quiz");
            loadNextQuestion();
        });
        
        rulesButton.addActionListener(e -> {
             String rulesText = "<html><body style='width: 300px; padding: 10px;'>" +
                    "<h2 style='color: #2980b9;'>QuizFlow Rules</h2>" +
                    "<ul>" +
                    "<li>You have <b>" + TIME_PER_QUESTION + " seconds</b> to answer each question.</li>" +
                    "<li>If you run out of time, the quiz will automatically advance.</li>" +
                    "<li>Think fast and aim for a 100% score!</li>" +
                    "</ul></body></html>";
            JOptionPane.showMessageDialog(this, rulesText, "How to Play", JOptionPane.INFORMATION_MESSAGE);
        });
        
        buttonPanel.add(startButton);
        buttonPanel.add(rulesButton);
        
        panel.add(mainIcon, gbc);
        panel.add(titleLabel, gbc);
        panel.add(subtitleLabel, gbc);
        panel.add(buttonPanel, gbc);
        
        return panel;
    }

    private JPanel createQuizPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(bgColor);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(cardColor);
        topPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(223, 230, 233)),
                new EmptyBorder(15, 25, 15, 25)
        ));

        JPanel topInnerPanel = new JPanel(new BorderLayout());
        topInnerPanel.setBackground(cardColor);
        
        JLabel logoLabel = new JLabel("QuizFlow");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        logoLabel.setForeground(primaryColor);
        topInnerPanel.add(logoLabel, BorderLayout.WEST);

        timerLabel = new JLabel("Time: " + TIME_PER_QUESTION + "s");
        timerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        timerLabel.setForeground(new Color(231, 76, 60)); 
        topInnerPanel.add(timerLabel, BorderLayout.EAST);
        
        topPanel.add(topInnerPanel, BorderLayout.NORTH);
        topPanel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.CENTER);
        
        progressBar = new JProgressBar(0, 100);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setForeground(new Color(46, 204, 113));
        progressBar.setBackground(new Color(236, 240, 241));
        progressBar.setPreferredSize(new Dimension(panel.getWidth(), 20));
        progressBar.setBorderPainted(false);
        topPanel.add(progressBar, BorderLayout.SOUTH);

        panel.add(topPanel, BorderLayout.NORTH);

        JPanel centerWrapper = new JPanel(new GridBagLayout());
        centerWrapper.setBackground(bgColor);

        JPanel contentCard = new JPanel();
        contentCard.setLayout(new BoxLayout(contentCard, BoxLayout.Y_AXIS));
        contentCard.setBackground(cardColor);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 218, 226), 1, true),
            new EmptyBorder(40, 50, 40, 50)
        ));
        contentCard.setPreferredSize(new Dimension(650, 320));

        questionLabel = new JLabel("Loading...");
        questionLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        questionLabel.setForeground(textColor);
        questionLabel.putClientProperty("html.disable", null);
        questionLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        contentCard.add(questionLabel);
        contentCard.add(Box.createRigidArea(new Dimension(0, 30)));

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
            buttonGroup.add(optionButtons[i]);
            contentCard.add(optionButtons[i]);
            contentCard.add(Box.createRigidArea(new Dimension(0, 15)));
        } 
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1; gbc.weighty = 1;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        centerWrapper.add(contentCard, gbc);
        panel.add(centerWrapper, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.setBackground(bgColor);
        bottomPanel.setBorder(new EmptyBorder(10, 30, 20, 32));
        
        nextButton = new JButton("Next Question >");
        stylePrimaryButton(nextButton, primaryColor);
        nextButton.setPreferredSize(new Dimension(200, 50));

        nextButton.addActionListener(e -> handleNext(true));
        
        bottomPanel.add(nextButton);
        
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    private void stylePrimaryButton(JButton button, Color bg) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(bg);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
    }

    private void loadNextQuestion() {
        if (quizManager.hasNextQuestion()) {
            Question q = quizManager.getNextQuestion();
            
            questionLabel.setText("<html><div style='width: 500px;'>" + q.getText() + "</div></html>");
            
            String[] options = q.getOptions();
            for (int i = 0; i < 4; i++) {
                optionButtons[i].setText(options[i]);
                optionButtons[i].setActionCommand(String.valueOf(i + 1));
            }
            
            buttonGroup.clearSelection();
            
            int currentProgress = (int) (((double) quizManager.getProcessedCount() / quizManager.getTotalQuestions()) * 100);
            progressBar.setValue(currentProgress);
            progressBar.setString(quizManager.getProcessedCount() + " / " + quizManager.getTotalQuestions() + " Completed");
            
            startTimer();
            mainPanel.requestFocusInWindow();
        } else {
            showFinalScore();
        }
    }

    private void startTimer() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.stopTimer();
            timerThread.interrupt();
        }
        
        timerLabel.setText("Time: " + TIME_PER_QUESTION + "s");

        timerThread = new TimerThread(TIME_PER_QUESTION, 
            () -> {
                int timeRemaining = timerThread.getTimeRemaining();
                timerLabel.setText("Time: " + timeRemaining + "s");
                
                if (timeRemaining <= 5) {
                    timerLabel.setForeground(new Color(192, 57, 43));
                } else {
                    timerLabel.setForeground(textColor); 
                }
            },
            () -> {
                JOptionPane.showMessageDialog(this, "Time is up! Moving forward...", "Timeout", JOptionPane.WARNING_MESSAGE);
                handleNext(false); 
            }
        );
        timerThread.start();
    }

    private void handleNext(boolean checkUserAnswer) {
        if (timerThread != null) {
            timerThread.stopTimer();
            timerThread.interrupt();
        }
        
        if (checkUserAnswer) {
            int selectedIndex = -1;
            for (int i = 0; i < 4; i++) {
                if (optionButtons[i].isSelected()) {
                    selectedIndex = i + 1;
                    break;
                }
            }
            if (selectedIndex != -1) {
                quizManager.checkAnswer(selectedIndex);
            }
        }
        
        quizManager.incrementProcessedCount();
        
        loadNextQuestion();
    }

    private void showFinalScore() {
        if (timerThread != null) {
            timerThread.stopTimer();
        }
        
        int score = quizManager.getScore();
        int total = quizManager.getTotalQuestions();
        
        JPanel scorePanel = createScorePanel(score, total);
        mainPanel.add(scorePanel, "Score");
        cardLayout.show(mainPanel, "Score");
    }

    private JPanel createScorePanel(int score, int total) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(bgColor);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        
        double percentage = ((double) score / total) * 100;
        String greeting = percentage >= 80 ? "Outstanding!" : (percentage >= 50 ? "Good Job!" : "Keep Practicing.");
        
        JLabel greetingLabel = new JLabel(greeting);
        greetingLabel.setFont(new Font("Segoe UI", Font.BOLD, 46));
        greetingLabel.setForeground(primaryColor);
        greetingLabel.setBorder(new EmptyBorder(0, 0, 10, 0));
        
        JLabel scoreLabel = new JLabel(String.format("You scored %d out of %d.", score, total));
        scoreLabel.setFont(new Font("Segoe UI", Font.PLAIN, 28));
        scoreLabel.setForeground(textColor);
        scoreLabel.setBorder(new EmptyBorder(0, 0, 40, 0));
        
        JButton exitButton = new JButton("EXIT APP");
        stylePrimaryButton(exitButton, new Color(231, 76, 60)); 
        exitButton.setPreferredSize(new Dimension(200, 55));
        
        exitButton.addActionListener(e -> System.exit(0));
        
        panel.add(greetingLabel, gbc);
        panel.add(scoreLabel, gbc);
        panel.add(exitButton, gbc);
        
        return panel;
    }
}
