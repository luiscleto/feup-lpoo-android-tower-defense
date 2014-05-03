package pt.up.fe.lpoo.framework.implementation;

import java.util.List;

import pt.up.fe.lpoo.framework.Input;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.View;

/**
 * Information about the implementation of the framework can be found at
 *  http://www.kilobolt.com/day-6-the-android-game-framework-part-ii.html
 */
public class AndroidInput implements Input {    
    TouchHandler touchHandler;

    public AndroidInput(Context context, View view, float scaleX, float scaleY) {
        if(VERSION.SDK_INT < 5) 
            touchHandler = new SingleTouchHandler(view, scaleX, scaleY);
        else
            touchHandler = new MultiTouchHandler(view, scaleX, scaleY);        
    }


    @Override
    public boolean isTouchDown(int pointer) {
        return touchHandler.isTouchDown(pointer);
    }

    @Override
    public int getTouchX(int pointer) {
        return touchHandler.getTouchX(pointer);
    }

    @Override
    public int getTouchY(int pointer) {
        return touchHandler.getTouchY(pointer);
    }



    @Override
    public List<TouchEvent> getTouchEvents() {
        return touchHandler.getTouchEvents();
    }
    
}