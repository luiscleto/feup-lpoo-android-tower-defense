package pt.up.fe.lpoo.towerdefense;

import java.util.ArrayList;

import pt.up.fe.lpoo.framework.Image;

/**
 * Class containing animation frames and durations of each frame
 * @author Joao Marinheiro
 * @author Luis Cleto
 */
public class Animation {

    private ArrayList<AnimFrame> frames;
    private long totalDuration;

    /**
     * Default constructor for the Animation class. Initializes the variables.
     */
    public Animation() {
        frames = new ArrayList<AnimFrame>();
        totalDuration = 0;
    }

    /**
     * Adds a new frame to the animation sequence
     * @param image image to be added to the animation
     * @param duration duration during which the image should be displayed
     */
    public synchronized void addFrame(Image image, long duration) {
        totalDuration += duration;
        frames.add(new AnimFrame(image, totalDuration));
    }

    /**
     * Returns the image corresponding to the specified animation time
     * @param animTime animation time used to determine which frame of the animation to use
     * @return returns the image of the animation at a specified animation time
     */
    public synchronized Image getImage(long animTime) {
    	int currentFrame=0;
    	 if (frames.size() > 1) {
             if (animTime >= totalDuration) {
                 animTime = animTime % totalDuration;

             }

             while (animTime > frames.get(currentFrame).endTime) {
                 currentFrame++;

             }
         }
        return frames.get(currentFrame).image;
    }
    
    /**
     * @return returns the total duration of the animation
     */
    public long getTotalDuration(){
    	return this.totalDuration;
    }

    private class AnimFrame {

        Image image;
        long endTime;

        public AnimFrame(Image image, long endTime) {
            this.image = image;
            this.endTime = endTime;
        }
    }
}