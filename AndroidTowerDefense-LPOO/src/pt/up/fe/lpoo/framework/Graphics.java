package pt.up.fe.lpoo.framework;

import android.content.res.AssetManager;
import android.graphics.Paint;

/**
 * Information about the framework interface can be found at
 *  http://www.kilobolt.com/day-5-the-android-game-framework-part-i.html
 */
public interface Graphics {
    public static enum ImageFormat {
        ARGB8888, ARGB4444, RGB565
    }
    public Image newImage(String fileName, ImageFormat format);

    public void clearScreen(int color);

    public void drawLine(int x, int y, int x2, int y2, int color);

    public void drawRect(int x, int y, int width, int height, int color);
    
    /**
     * This function was added to the framework to allow drawing circles
     * @param x x position of the circle's center
     * @param y y position of the circle's center
     * @param rad radius of the circle
     * @param color color with which to paint the circle
     */
    public void drawCircle(float x, float y, float rad, int color);

    public void drawImage(Image image, int x, int y, int srcX, int srcY,
            int srcWidth, int srcHeight);

    public void drawImage(Image Image, int x, int y);

    void drawString(String text, int x, int y, Paint paint);

    public int getWidth();

    public int getHeight();

    public void drawARGB(int i, int j, int k, int l);

	void drawScaledImage(Image Image, int x, int y, int width, int height,
			int srcX, int srcY, int srcWidth, int srcHeight);
	
	public AssetManager getAssets();
}