package com.brnleehng.worldrunner.StepDetector;

/**
 * Created by JoshDesktop on 2/12/2015.
 */
public interface StepListener {

    /**
     * Called when a step has been detected.  Given the time in nanoseconds at
     * which the step was detected.
     */
    public void step(long timeNs);

}