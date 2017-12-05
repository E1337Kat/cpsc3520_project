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
package Main;

import Scenes.Menu;
import Scenes.Overworld;
import Scenes.Scene;
import Scenes.Woods;
import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;

import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;


/**
 * Gameloop for the game. It is abstracted so that this will be the same
 * for any game made.
 * @author Ellie Peterson
 * @author Craig Tanis
 */
public class GameLoop {
    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;
    
    public static final int TARGET_FPS=100;
    public static final int SCR_WIDTH=800;
    public static final int SCR_HEIGHT=600;
    
    private static final Logger LOG = Logger.getLogger(GameLoop.class.getName());

    /**
     * Starts a new instance of the game loop. 
     * @param args Any arguments that are passed to the game on startup.
     * @throws LWJGLException A lwjgl exception for lwjgl stuff.
     */
    public static void main(String[] args) throws LWJGLException {
        // get the global logger to configure it
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        logger.setLevel(Level.FINEST);
        
        try {
            fileTxt = new FileHandler("game_log.log");
            
            // create a TXT formatter
            formatterTxt = new SimpleFormatter();
            fileTxt.setFormatter(formatterTxt);
            logger.addHandler(fileTxt);
        } catch (IOException e) {
            File file = new File("game_log.log");
            
            try {
                if (file.createNewFile()) {
                    System.out.println("Saved from IOException.");
                }
                
                // create a TXT formatter
                formatterTxt = new SimpleFormatter();
                fileTxt.setFormatter(formatterTxt);
                logger.addHandler(fileTxt);
            } catch (IOException e2) {
                System.out.println("ERROR: Log file can not be created. Error occured at: " + e2);
                System.out.println("Further Info: " + e);
            }
            
            
        }

        initGL(SCR_WIDTH, SCR_HEIGHT);
        
        // hide the mouse cursor
        Mouse.setGrabbed(true);

        Menu gameMenu = new Menu();
        gameMenu.addItem("Play", Overworld.getOverworld());
        gameMenu.addSpecial("Exit", Menu.DO_EXIT);

        Scene currScene = gameMenu;


        while ( currScene.go() ) {
            currScene = currScene.nextScene();

            if (currScene == null) {
                currScene = gameMenu;
            }
        }

        AudioManager.getInstance().destroy();
        
        Display.destroy();
    }
    
    /**
     * Initializes the OpenGL stuff to build the screen
     * @param width The width to set to display to.
     * @param height The height to set the display to.
     * @throws LWJGLException Generic exception in lwjgl stuff.
     */
    public static void initGL(int width, int height) throws LWJGLException {
        // open window of appropriate size
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create();
        Display.setVSyncEnabled(true);
        
        // enable 2D textures
        GL11.glEnable(GL11.GL_TEXTURE_2D);              
     
        // set "clear" color to black
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);         

        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         
        // set viewport to entire window
        GL11.glViewport(0,0,width,height);
         
        // set up orthographic projectionr
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        // GLU.gluPerspective(90f, 1.333f, 2f, -2f);
        
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();
    }
}
