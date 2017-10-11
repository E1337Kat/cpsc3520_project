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

import Entities.Background;
import Entities.Ground;
import Entities.Player;
import Entities.Entity;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.lwjgl.LWJGLException;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;
import org.lwjgl.input.Mouse;



public class Gameloop
{
    public static final int TARGET_FPS=100;
    public static final int SCR_WIDTH=800;
    public static final int SCR_HEIGHT=600;
//    public static final String PATH_TO_BG = "res/parallax_mountain_pack/layers/";
//    
//    private static Background background0;
//    private static Background background1;
//    private static Background background2;
//    private static Background background3;
//    private static Background background4;
//    private static Player player;

    public static void main(String[] args) throws LWJGLException
    {
        initGL(SCR_WIDTH, SCR_HEIGHT);
        
        // hide the mouse cursor
        Mouse.setGrabbed(true);

        Menu gameMenu = new Menu();
        gameMenu.addItem("Play", new Overworld());
        gameMenu.addSpecial("Exit", Menu.DO_EXIT);

        Scene currScene = gameMenu;


        while ( currScene.go() )
        {
            currScene = currScene.nextScene();

            if (currScene == null)
            {
                currScene = gameMenu;
            }
        }

        AudioManager.getInstance().destroy();
        
//        // Load Player
//        player = new Player(32f);
//
//        background0 = new Background(0, PATH_TO_BG + "parallax-mountain-bg.png");
//        background1 = new Background(1, PATH_TO_BG + "parallax-mountain-mountain-far.png");
//        background2 = new Background(2, PATH_TO_BG + "parallax-mountain-mountains.png");
//        background3 = new Background(3, PATH_TO_BG + "parallax-mountain-trees.png");
//        background4 = new Background(4, PATH_TO_BG + "parallax-mountain-foreground-trees.png");

//        // Load Ground tiles
//        List<Ground> ground = new LinkedList<>();
//        Random rand = new Random();
//        for (int i = 0; i < 10; i++ ) {
//            ground.add (new Ground(i, rand, 64));
//            int k;
//            if (i==0)
//                k = 0;
//            else
//                k = ground.get(i-1).getHeck();
//            int j = ground.get(i).getHeck();
//            //int k = ground.get(i-1).getHeck();
//            System.out.println("Distance between ground " +
//                            (i-1) + " & " + i +
//                            " is " + 
//                            ((i*(j+(j*100))) - ( (i-1)*(k+(k*100))+500)));
//        }
//        
//        player.setEntities(ground);
//
//        int x1 = player.getX();
//        int y1 = player.getY();
//        
//        long time = (Sys.getTime()*1000)/Sys.getTimerResolution(); // ms
//        long delta = 1000/100
//                ;  // Fixed delta of 16.6
//        while (! Display.isCloseRequested())
//        {
//            int x2 = player.getX();
//            int y2 = player.getY();
//            long time2 = (Sys.getTime()*1000)/
//                Sys.getTimerResolution(); // ms
//            
//            long passed = time2-time;
//            
//
//            long accumulator =+ passed;
//            while ( accumulator >= delta) {
//                
//                
//            
//                // Update the background tiles
//                background0.update(delta);
//                background1.update(delta);
//                background2.update(delta);
//                background3.update(delta);
//                background4.update(delta);
//
//
//                // check for collision and update player accordingly
//                player.update(delta);
//
//
//                // log current x position of player on screen 
//                // ignore if unchaged
//                // NOTE: X never changes, so will never be displayed
//                if ( x2 != x1 ) {
//                    System.out.println("Player x: " + player.getX());
//                }
//
//                // log current y position of player on screen
//                // ignore if unchanged
//                // NOTE: changes a lot during jumps
//                if ( y2 != y1 ) {
//                    System.out.println("Player y: " + player.getY());
//                }
//
//                // Update the other entities
//                for  (Ground g : ground) {
//                    g.update(delta);
//                }
//
//
//                Display.update();
//                accumulator =- delta;
//            }
//            
//            // After updating and before drawing, we take the player's 
//            // position and we 
//            float translate_y = player.getY() - Display.getHeight() / 2;
//            float translate_x = player.getX() + Display.getWidth() / 2;
//            Display.sync(TARGET_FPS);
//            
//            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
//            
//            
//
//            // 50% Parallax scrolling
//            // Pushes current matrix used for screen operations, then
//            // translates it by half. 
//            GL11.glPushMatrix();
//            GL11.glTranslatef(translate_x / 2, -translate_y / 2, 0);
//            
//            // Draw the background
//            background0.draw();
//            background1.draw();
//            background2.draw();
//            background3.draw();
//            background4.draw();
//            
//            GL11.glPopMatrix();
//            // END 50% Parallax
//            
//            // Can add additional stuff between matrix stuff as needed.
//            
//            // 100% Parallax scrolling
//            // 
//            GL11.glPushMatrix();
//            GL11.glTranslatef(translate_x, -translate_y /2, 0);
//            
//            
//            // Draw the other entities
//            for  (Entity e : ground) {
//                e.draw();
//            }
//            
//            // Draw the Player
//            player.draw();
//            
//            GL11.glPopMatrix();
//            // END 100% Parallax
//
//
//            // Update delta
//            time = time2;
//            x1 = x2;
//            y1 = y2;
//        }

        Display.destroy();
    }
    

    public static void initGL(int width, int height) throws LWJGLException
    {
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
        // GL11.glTranslated(0, 0, -500);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
