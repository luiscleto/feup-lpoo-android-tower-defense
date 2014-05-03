package pt.up.fe.lpoo.towerdefense;

import pt.up.fe.lpoo.framework.Game;
import pt.up.fe.lpoo.framework.Graphics;
import pt.up.fe.lpoo.framework.Screen;
import pt.up.fe.lpoo.framework.Graphics.ImageFormat;

/**
 * Inital screen to be used while launching the game. Loads the splash loading screen and changes the current screen
 *  to the loading screen
 * @author Joao Marinheiro
 * @author Luis Cleto
 * @see pt.up.fe.lpoo.framework.Screen
 */
public class SplashLoadingScreen extends Screen {
	/**
	 * Constructor for the SplashLoadingScreen class. Uses the superclass constructor only
	 * @param game Game object corresponding to this screen
	 */
    public SplashLoadingScreen(Game game) {
        super(game);
    }
    
    /**
     * Loads the splash loading screen and sets the current screen to the loading screen
     */
    @Override
    public void update(float deltaTime) {
        Graphics g = game.getGraphics();
        Assets.splash= g.newImage(Assets.imagesFolderName+"splash.jpg", ImageFormat.RGB565);

        
        game.setScreen(new LoadingScreen(game));

    }

    /**
     * Just a stub. There is nothing to paint in this screen as its loading the loading screen
     */
    @Override
    public void paint(float deltaTime) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void backButton() {

    }
}