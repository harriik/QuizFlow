package quizapp.util;

import javax.swing.SwingUtilities;

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

    public void stopTimer() {
        this.running = false;
    }

    @Override
    public void run() {
        while (running && timeRemaining > 0) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                running = false;
                break;
            }
            
            if (!running) break;

            timeRemaining--;

            SwingUtilities.invokeLater(onTick);
        }

        if (running && timeRemaining <= 0) {
            SwingUtilities.invokeLater(onTimeUp);
        }
    }
}
