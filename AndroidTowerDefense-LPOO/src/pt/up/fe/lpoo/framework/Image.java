package pt.up.fe.lpoo.framework;

import pt.up.fe.lpoo.framework.Graphics.ImageFormat;

/**
 * Information about the framework interface can be found at
 *  http://www.kilobolt.com/day-5-the-android-game-framework-part-i.html
 */
public interface Image {
    public int getWidth();
    public int getHeight();
    public ImageFormat getFormat();
    public void dispose();
}