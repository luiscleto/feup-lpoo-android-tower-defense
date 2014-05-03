package pt.up.fe.lpoo.framework.implementation;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Information about the implementation of the framework can be found at
 *  http://www.kilobolt.com/day-6-the-android-game-framework-part-ii.html
 */
public class AndroidFastRenderView extends SurfaceView implements Runnable {
    AndroidGame game;
    Bitmap framebuffer;
    Thread renderThread = null;
    SurfaceHolder holder;
    volatile boolean running = false;
    
    public AndroidFastRenderView(Context context){
    	super(context);
    }
    public AndroidFastRenderView(AndroidGame game, Bitmap framebuffer) {
        super(game);
        this.game = game;
        this.framebuffer = framebuffer;
        this.holder = getHolder();
    }

    public void resume() { 
        running = true;
        renderThread = new Thread(this);
        renderThread.start();   

    }      
    
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();
        while(running) {  
            if(!holder.getSurface().isValid())
                continue;           
            

            float deltaTime = (System.nanoTime() - startTime) / 10000000.000f;
            startTime = System.nanoTime();
            
            if (deltaTime > 3.15){
                deltaTime = (float) 3.15;
           }
     

            game.getCurrentScreen().update(deltaTime);
            game.getCurrentScreen().paint(deltaTime);
          
            
            
            Canvas canvas = holder.lockCanvas();
            canvas.getClipBounds(dstRect);
            canvas.drawBitmap(framebuffer, null, dstRect, null);                           
            holder.unlockCanvasAndPost(canvas);
            
            
        }
    }

    public void pause() {                        
        running = false;                        
        while(true) {
            try {
                renderThread.join();
                break;
            } catch (InterruptedException e) {
                // retry
            }
            
        }
    }     
    
  
}