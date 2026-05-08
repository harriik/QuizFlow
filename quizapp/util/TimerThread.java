package quizapp.util;

import javax.swing.SwingUtilities;

// Thread class that manages a countdown timer for quiz questions
// Executes callbacks on the EDT (Event Dispatch Thread) to update GUI safely
public class TimerThread extends Thread {
    private int timeRemaining; // Seconds left on the timer
    private boolean running; // Flag to control timer execution
    private Runnable onTick; // Callback executed every second (on EDT)
    private Runnable onTimeUp; // Callback executed when time reaches 0 (on EDT)

    /**
     * Constructor for countdown timer thread
     * @param seconds Total seconds to count down
     * @param onTick Callback executed on EDT every second (updates GUI with time remaining)
     * @param onTimeUp Callback executed on EDT when time reaches 0 (handles timeout)
     */
    public TimerThread(int seconds, Runnable onTick, Runnable onTimeUp) {
        this.timeRemaining = seconds;
        this.running = true;
        this.onTick = onTick;
        this.onTimeUp = onTimeUp;
    }

    // Get the current time remaining (thread-safe read)
    public int getTimeRemaining() {
        return timeRemaining;
    }

    // Stop the timer by setting running flag to false
    public void stopTimer() {
        this.running = false;
    }

    /**
     * Main timer execution method
     * Counts down from initial seconds to 0, executing callbacks via SwingUtilities.invokeLater()
     * to ensure GUI updates happen on the Event Dispatch Thread
     */
    @Override
    public void run() {
        // Loop while timer is running and time remains
        while (running && timeRemaining > 0) {
            try {
                // Sleep for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // If interrupted, stop the timer
                running = false;
                break;
            }
            
            // Check if timer was stopped during sleep
            if (!running) break;

            // Decrement time counter
            timeRemaining--;

            // Execute onTick callback on the EDT (safe for Swing updates)
            SwingUtilities.invokeLater(onTick);
        }

        // If timer completed normally (time reached 0), execute timeout callback
        if (running && timeRemaining <= 0) {
            SwingUtilities.invokeLater(onTimeUp);
        }
    }
}
