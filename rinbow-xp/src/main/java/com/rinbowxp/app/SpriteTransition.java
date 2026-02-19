package com.rinbowxp.app;

import javax.swing.ImageIcon;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class SpriteTransition {
    private ResourceManager resourceManager;
    private int currentStage = 0;       // 0-8
    private static final int TOTAL_STAGES = 9;
    private static final int TRANSITION_DISPLAY_MS = 1400; // how long transitionX.gif is shown
    private static final int FINAL_STAGE_LINGER_MS = 5000; // how long stage8.gif is shown before game over

    private boolean isTransitioning = false;
    private Timer transitionTimer;
    private Runnable onFrameChange;

    // Called once after stage8.gif has been shown for FINAL_STAGE_LINGER_MS.
    // GameSession sets this to the lambda that navigates to the Game Over panel.
    private Runnable onFinalStageReady;

    private enum DisplayMode { STAGE, TRANSITION }
    private DisplayMode displayMode = DisplayMode.STAGE;

    public SpriteTransition(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public void setOnFrameChange(Runnable callback) {
        this.onFrameChange = callback;
    }

    public void setOnFinalStageReady(Runnable callback) {
        this.onFinalStageReady = callback;
    }

    public ImageIcon getCurrentImage() {
        try {
            if (displayMode == DisplayMode.TRANSITION) {
                String key = "transition" + currentStage;
                System.out.println("[SpriteTransition] Showing: " + key + ".gif");
                return resourceManager.getImageIcon(key);
            } else {
                String key = "stage" + currentStage;
                System.out.println("[SpriteTransition] Showing: " + key + ".gif");
                return resourceManager.getImageIcon(key);
            }
        } catch (Exception e) {
            System.err.println("[SpriteTransition] Failed to load - stage=" + currentStage + ", mode=" + displayMode);
            System.err.println("Error: " + e.getMessage());
            return null;
        }
    }

    public void next() {
        if (isTransitioning) {
            System.out.println("[SpriteTransition] Already transitioning, ignoring next() call");
            return;
        }

        if (transitionTimer != null) {
            transitionTimer.stop();
            transitionTimer = null;
        }

        System.out.println("[SpriteTransition] Starting transition from stage" + currentStage);
        isTransitioning = true;
        displayMode = DisplayMode.TRANSITION;

        // Step 1: show transitionX.gif immediately
        fireFrameChange();

        // Step 2: after TRANSITION_DISPLAY_MS, advance to the next stage GIF
        transitionTimer = new Timer(TRANSITION_DISPLAY_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((Timer) e.getSource()).stop();
                transitionTimer = null;

                currentStage = (currentStage + 1) % TOTAL_STAGES;
                displayMode = DisplayMode.STAGE;
                isTransitioning = false;

                System.out.println("[SpriteTransition] Transition complete! Now showing stage" + currentStage + ".gif");
                fireFrameChange();

                // Step 3: if this is the final stage (stage8), linger then fire game-over callback
                if (currentStage == TOTAL_STAGES - 1 && onFinalStageReady != null) {
                    System.out.println("[SpriteTransition] Final stage reached. Waiting " + FINAL_STAGE_LINGER_MS + "ms before game over...");
                    Timer lingerTimer = new Timer(FINAL_STAGE_LINGER_MS, new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent ev) {
                            ((Timer) ev.getSource()).stop();
                            System.out.println("[SpriteTransition] Linger complete. Firing onFinalStageReady.");
                            onFinalStageReady.run();
                        }
                    });
                    lingerTimer.setRepeats(false);
                    lingerTimer.start();
                }
            }
        });
        transitionTimer.setRepeats(false);
        transitionTimer.start();
    }

    public void reset() {
        if (transitionTimer != null) {
            transitionTimer.stop();
            transitionTimer = null;
        }
        currentStage = 0;
        displayMode = DisplayMode.STAGE;
        isTransitioning = false;
        System.out.println("[SpriteTransition] Reset to stage0.");
    }

    public int getCurrentStage() {
        return currentStage;
    }

    public boolean isTransitioning() {
        return isTransitioning;
    }

    private void fireFrameChange() {
        if (onFrameChange != null) {
            onFrameChange.run();
        }
    }
}