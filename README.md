# QuizFlow - Java Swing Quiz Application

QuizFlow is a clean, interactive desktop quiz application built purely with core Java and Swing. It features an automated countdown timer, seamless user interface layouts, file-based question management, and dynamic scoring mechanics.

## Features
- **Dynamic Quiz Engine**: Automatically loads and parses questions, options, and difficulty levels from a delimited text file (`questions.txt`).
- **Timed Constraints**: A customized multithreaded Timer executes in the background, enforcing a 15-second time limit per question before automatically advancing the board.
- **Progress Tracking**: Contains a visual progress bar indicating completion status based on internal generic `Iterator` tracking.
- **Beautiful Swing UI**: Transitions fluidly between Welcome, Quiz, and Score screens utilizing `CardLayout`. Polished with vibrant colors, padding, and the Java *Nimbus* LookAndFeel.

## Project Architecture
The codebase strictly adheres to architectural separation of concerns through packages:
- `quizapp.model` - Core data entities (`Question.java` implementing `Comparable`).
- `quizapp.service` - Iteration and quiz state management (`QuizManager.java`).
- `quizapp.ui` - Front-end interface mapping (`QuizGUI.java`).
- `quizapp.util` - I/O File handling and background thread concurrency (`TimerThread.java`).

## How to Run Locally

1. Open your terminal or command prompt.
2. Navigate to the root directory where `Main.java` is located.
3. Compile the application:
   ```powershell
   javac quizapp/Main.java quizapp/service/QuizManager.java quizapp/ui/QuizGUI.java
   ```
4. Start the application:
   ```powershell
   java quizapp.Main
   ```
