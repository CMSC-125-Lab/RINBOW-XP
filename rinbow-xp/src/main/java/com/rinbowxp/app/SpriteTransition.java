package com.rinbowxp.app;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpriteTransition {
    private ResourceManager resourceManager;
    private int currentStage = 0;  // 0-8
    private int currentFrame = -1;  // -1 for stage GIF, 0-7 for transition frames
    private static final int TOTAL_STAGES = 9;
    private static final int FRAMES_PER_TRANSITION = 8;
    
    private boolean isTransitioning = false;  // Is currently playing transition frames
    private Timer transitionTimer;
    private int transitionSpeed = 2;  // milliseconds between frames
    private Runnable onFrameChange;  // Callback to repaint when frame changes

    public SpriteTransition(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    /**
     * Set callback to be called when the displayed image changes
     */
    public void setOnFrameChange(Runnable callback) {
        this.onFrameChange = callback;
    }

    /**
     * Set the speed of transition animation (milliseconds between frames)
     */
    public void setTransitionSpeed(int speedMs) {
        this.transitionSpeed = speedMs;
        // If a transition is currently running, apply the new delay immediately
        if (transitionTimer != null && transitionTimer.isRunning()) {
            transitionTimer.setDelay(this.transitionSpeed);
        }
    }

    /**
     * Get the current image (either a stage GIF or transition frame)
     */
    public ImageIcon getCurrentImage() {
        try {
            if (currentFrame == -1) {
                // Show stage GIF
                String stageName = "stage" + currentStage;
                System.out.println("Showing: " + stageName + ".gif");
                return resourceManager.getImageIcon(stageName);
            } else {
                // Show transition frame
                String transitionName = "transition-" + currentStage + "/stage" + currentStage + "_f" + (currentFrame + 1);
                System.out.println("Showing transition frame: " + transitionName + ".png");
                return resourceManager.getImageIcon(transitionName);
            }
        } catch (Exception e) {
            System.err.println("Failed to load sprite - Stage: " + currentStage + ", Frame: " + currentFrame);
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    /**
     * Start the transition sequence:
     * 1. Play all 8 frames from transition-X folder (one by one with delay)
     * 2. Then switch to stage(X+1).gif
     */
    public void next() {
        if (isTransitioning) {
            System.out.println("Already transitioning, ignoring next() call");
            return;
        }

        System.out.println("Starting transition from stage" + currentStage);
        isTransitioning = true;
        currentFrame = 0;  // Start at first transition frame
        
        // Stop any existing timer
        if (transitionTimer != null) {
            transitionTimer.stop();
        }

        // Create timer to advance frames
        transitionTimer = new Timer(transitionSpeed, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentFrame++;

                if (currentFrame >= FRAMES_PER_TRANSITION) {
                    // Finished all transition frames, move to next stage GIF
                    currentFrame = -1;
                    currentStage = (currentStage + 1) % TOTAL_STAGES;
                    isTransitioning = false;
                    ((Timer) e.getSource()).stop();
                    System.out.println("Transition complete! Now showing stage" + currentStage + ".gif");
                } else {
                    System.out.println("Transition frame " + (currentFrame + 1) + "/8");
                }

                // Notify to repaint
                if (onFrameChange != null) {
                    onFrameChange.run();
                }
            }
        });
        // Ensure the timer starts immediately and uses the current delay
        transitionTimer.setInitialDelay(0);
        transitionTimer.setDelay(transitionSpeed);
        transitionTimer.setRepeats(true);
        transitionTimer.start();

        // Initial repaint
        if (onFrameChange != null) {
            onFrameChange.run();
        }
    }

    /**
     * Get the current stage (0-8)
     */
    public int getCurrentStage() {
        return currentStage;
    }

    /**
     * Get the current frame (-1 for stage GIF, 0-7 for transition frames)
     */
    public int getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Check if currently transitioning
     */
    public boolean isTransitioning() {
        return isTransitioning;
    }

    /**
     * Reset to stage 0
     */
    public void reset() {
        if (transitionTimer != null) {
            transitionTimer.stop();
        }
        currentStage = 0;
        currentFrame = -1;
        isTransitioning = false;
    }
}
