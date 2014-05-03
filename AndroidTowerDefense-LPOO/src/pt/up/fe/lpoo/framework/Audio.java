package pt.up.fe.lpoo.framework;

/**
 * Information about the framework interface can be found at
 *  http://www.kilobolt.com/day-5-the-android-game-framework-part-i.html
 */
public interface Audio {
    public Music createMusic(String file);

    public Sound createSound(String file);
}
 