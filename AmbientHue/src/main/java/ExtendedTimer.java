import javafx.beans.property.SimpleLongProperty;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A java.util.Timer modified to fit the program's needs.
 * It is basicly java.util.Timer: it performs an action every x milliseconds
 * But it has added functionalities:
 *      * It can be simply stopped
 *      * It can perform an action from the ActionIF interface
 *      * It can stop performing the action without completely being cancelled so that it can continue to perform the action later
 */
public class ExtendedTimer extends Timer {
    private boolean running, start; // If the action must be performed and if the timer is running
    private SimpleLongProperty period; // The time to wait until the action is performed again (in milliseconds)
    private TimerTask timerTask; // The TimerTask object (which launches the action). Used to completely stop the timer

    /**
     * Constructor of the ExtendedTimer class
     */
    public ExtendedTimer(){
        super();

        // Set the defaults variables
        running = false;
        start();
        period = new SimpleLongProperty(100); // Default wait time is 100 ms
    }

    /**
     * Takes an action to perform and performs it as explained in the class's comment
     * @param actionIF the action to perform
     * @param period the time to wait until the action is performed again. The action is performed every {period} milliseconds
     */
    public void schedule(ActionIF actionIF, long period){
        this.schedule(actionIF, 0, period);
    }

    /**
     * Takes an action to perform and performs it as explained in the class's comment
     * @param actionIF the action to perform
     * @param delay the time to wait before launching the action for the first time in milliseconds
     * @param period the time to wait until the action is performed again. The action is performed every {period} milliseconds
     */
    public void schedule(ActionIF actionIF, long delay, long period) {
        // Set the time to wait between two actions
        setPeriod(period);

        // Create the TimerTask
        timerTask = new TimerTask() {
            /**
             * This method is called every {period} milliseconds, waiting {delay} milliseconds before performing for the first time
             */
            @Override
            public void run() {
                // If the thread must be stopped, don't make anything
                while(isStarted()) {
                    try {
                        // If the action must be performed
                        if (isRunning()) {
                            try {
                                // Perform the action
                                actionIF.run();
                            } catch (Exception exc) {
                                exc.printStackTrace();
                            }
                        }

                        // Wait {period} milliseconds before performing the action again
                        Thread.sleep(getPeriod().getValue());
                    }
                    catch(Exception exc){
                        exc.printStackTrace();
                    }
                }
            }
        };
        super.schedule(timerTask, delay);
    }

    /**
     * Tells the thread that it will not be stopped
     */
    public void start(){
        start = true;
    }

    /**
     * Tells the thread that it will be stopped
     */
    public void stop(){
        start = false;
        if(timerTask != null){
            timerTask.cancel();
        }
        cancel();
    }

    /**
     * Tells whether the thread will be stopped or not
     * @return {true} if the thread can safely run. {false} otherwise
     */
    public boolean isStarted() {
        return start;
    }

    /**
     * Set the time to wait between performing the action another time
     * @param period the time to wait in milliseconds
     */
    public void setPeriod(long period) {
        this.period.setValue(period);
    }

    /**
     * Gives the period time, the time to wait between the action is performed again in milliseconds
     * @return the time to wait in milliseconds
     */
    public SimpleLongProperty getPeriod() {
        return period;
    }

    /**
     * Set if the action must be executed or not
     * @param running {true} if the action must be executed, {false} otherwise
     */
    public void setRunning(boolean running) {
        this.running = running;
    }

    /**
     * Tells whether the action is being performed
     * @return {true} if the action is performed, {false} otherwise
     */
    public boolean isRunning() {
        return running;
    }
}
