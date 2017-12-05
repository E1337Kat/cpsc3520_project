/*
 * Copyright (C) 2017 Ellie
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package Scenes;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;

/**
 * Describes a scene that can be created. Modified to implement a more robust 
 * go loop.
 * @author Craig Tanis
 * @author Ellie Peterson
 */
public abstract class Scene
{
    private final long delta = 10;  // Fixed delta of 10
    
    private boolean doExit = false;
    private int targetFPS = 100;
    private long lastloop, currloop, passed, accumulator;

    /**
     * return false if the game should be quit
     * @param delta The delta to update at
     * @return Returns true if drawn correctly
     */
    public abstract boolean drawFrame(float delta);

    /**
     * null typically means Game should load menu
     * @return The scene to load next.
     */
    public Scene nextScene() { return null; }

    /**
     * Exits the game
     */
    protected void exit()
    {
        doExit=true;
    };

    /**
     * sets the FPS
     * @param fps The fps to target
     */
    protected void setFPS(int fps)
    {
        targetFPS = fps;
    }

    /**
     * returns false when game should be exited
     * @return true when keep going
     */
    public boolean go()
    {
        lastloop = (Sys.getTime()*1000 / Sys.getTimerResolution());

        
        boolean keepGoing = true;
        do
        {
            Display.sync(targetFPS);   // 60 FPS
            
            
            
            currloop = (Sys.getTime()*1000)/
                Sys.getTimerResolution(); // ms
            
            //long time = (Sys.getTime()*1000 / Sys.getTimerResolution());
            
            
            passed = currloop-lastloop;
            

            accumulator =+ passed;
            while ( accumulator >= delta) {
                
                keepGoing = drawFrame(delta);
            
                // UPDATE DISPLAY
                Display.update();
                
//                AudioManager.getInstance().update();
                
                accumulator =- delta;
            }

            

            if (Display.isCloseRequested() || doExit)
            {
                return false;
            }


        } while (keepGoing);

        return true;
    }
}

