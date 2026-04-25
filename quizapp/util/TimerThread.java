package quizapp.util;

import javax.swing.SwingUtilities;

/**
 * TimerThread handles countdown logic in a separate background thread.
 */
public class TimerThread extends Thread {
    private int timeRemaining;
    private boolean running;
    private Runnable onTick;
    private Runnable onTimeUp;

    /**
     * @param seconds Total seconds to count down
     * @param onTick Callback executed on EDT every second
     * @param onTimeUp Callback executed on EDT when time reaches 0
     */
    public TimerThread(int seconds, Runnable onTick, Runnable onTimeUp) {
        this.timeRemaining = seconds;
        this.running = true;
        this.onTick = onTick;
        this.onTimeUp = onTimeUp;
    }

    public int getTimeRemaining() {
        return timeRemaining;
    }

    // Stop timer when question is answered prematurely
    public void stopTimer() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running && timeRemaining > 0) {
            try {
                // Sleep for 1 second
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Thread was interrupted (e.g. user answered question before time ran out)
                running = false;
                break;
            }
            
            if (!running) break;

            timeRemaining--;

            // Update GUI safely on the Event Dispatch Thread
            SwingUtilities.invokeLater(onTick);
        }

        // If time runs out while still running, trigger timeout
        if (running && timeRemaining <= 0) {
            SwingUtilities.invokeLater(onTimeUp);
        }
    }
}
