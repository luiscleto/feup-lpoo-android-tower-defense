package pt.up.fe.lpoo.framework;

import java.util.List;

/**
 * Information about the framework interface can be found at
 *  http://www.kilobolt.com/day-5-the-android-game-framework-part-i.html
 */
public interface Input {
    public static class TouchEvent {
        public static final int TOUCH_DOWN = 0;
        public static final int TOUCH_UP = 1;
        public static final int TOUCH_DRAGGED = 2;
        public static final int TOUCH_HOLD = 3;

        public int type;
        public int x, y;
        public int pointer;
    }
    public boolean isTouchDown(int pointer);

    public int getTouchX(int pointer);

    public int getTouchY(int pointer);

    public List<TouchEvent> getTouchEvents();

}
 