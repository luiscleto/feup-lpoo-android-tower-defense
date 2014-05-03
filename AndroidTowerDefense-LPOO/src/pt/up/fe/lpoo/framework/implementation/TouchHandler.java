package pt.up.fe.lpoo.framework.implementation;

import java.util.List;

import pt.up.fe.lpoo.framework.Input.TouchEvent;

import android.view.View.OnTouchListener;

/**
 * Information about the implementation of the framework can be found at
 *  http://www.kilobolt.com/day-6-the-android-game-framework-part-ii.html
 */
public interface TouchHandler extends OnTouchListener {
    public boolean isTouchDown(int pointer);
    
    public int getTouchX(int pointer);
    
    public int getTouchY(int pointer);
    
    public List<TouchEvent> getTouchEvents();
    
}