package com.example.supersantarun;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameThread extends Thread {

    SurfaceHolder surfaceHolder; //Surfaceholder object reference
    boolean isRunning; // Flag to detect whether the thread is running or not
    long startTime, loopTime; // Loop start time and loop duration
    long DELAY = 33; // Delay in milliseconds between screen refreshes

    public GameThread(SurfaceHolder surfaceHolder){
        this.surfaceHolder = surfaceHolder;
        isRunning = true;
    }

    @Override
    public void run() {
        // Looping until the boolean is false
        while(isRunning){
            startTime = SystemClock.uptimeMillis();
            // lock the canvas
            Canvas canvas = surfaceHolder.lockCanvas(null);
            if(canvas != null){
                synchronized (surfaceHolder){
                    AppConstants.getGameEngine().updateAndDrawBackgroundImage(canvas);
                    AppConstants.getGameEngine().updateAndDrawPath(canvas);
                    AppConstants.getGameEngine().updateAndDrawPlayer(canvas);
                    AppConstants.getGameEngine().updateAndDrawObstacles(canvas);
                    AppConstants.getGameEngine().tapToStart(canvas);
                    // unlock the canvas
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            // loop time
            loopTime = SystemClock.uptimeMillis() - startTime;
            // Pause here to make sure we update the right amount per second
            if(loopTime < DELAY){
                try{
                    Thread.sleep(DELAY - loopTime);
                }catch(InterruptedException e){
                    Log.e("Interrupted","Interrupted while sleeping");
                }
            }
        }
    }

    // Return whether the thread is running
    public boolean isRunning(){
        return isRunning;
    }

    // Sets the thread state, false = stopped, true = running
    public void setIsRunning(boolean state){
        isRunning = state;
    }
}
