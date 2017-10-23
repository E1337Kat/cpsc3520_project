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
import Entities.Entity;
import Entities.Ground;
import Entities.IndieCD;
import Entities.Player;
import com.sun.media.jfxmedia.logging.Logger;
import java.awt.Font;
import java.io.IOException;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author Ellie
 */
public class Overworld extends Scene{
    
    private final int d_width = Display.getWidth();
    private final int d_height = Display.getHeight();
    
    public static AudioManager audio = AudioManager.getInstance();

    public static final String PATH_TO_BG = "res/parallax_mountain_pack/layers/";
    
    private static Background background0;
    private static Background background1;
    private static Background background2;
    private static Background background3;
    private static Background background4;
    private static IndieCD localNatives;
    private static Player player;
    private static List<Ground> ground;
    private static List<IndieCD> musics;
    
//    private float translate_y_2 = 0;
//    private float translate_x_2 = 0;
//    private float translate_y_1 = 0;
//    private float translate_x_1 = 0;
    
//    private int x1,x2,y1,y2;
    
    
    public Overworld() {
        
        try {
            audio.loadSample("strum", "res/audio/188037__antumdeluge__guitar-strumming.wav");
        } catch (IOException e) {
            Logger.logMsg(Logger.ERROR, e.getMessage());
        }
        
        // Load Player
        player = new Player(64f);
        
        background0 = new Background(0, PATH_TO_BG + "parallax-mountain-bg.png");
        background1 = new Background(1, PATH_TO_BG + "parallax-mountain-mountain-far.png");
        background2 = new Background(2, PATH_TO_BG + "parallax-mountain-mountains.png");
        background3 = new Background(3, PATH_TO_BG + "parallax-mountain-trees.png");
        background4 = new Background(4, PATH_TO_BG + "parallax-mountain-foreground-trees.png");

        // Load Ground tiles
        ground = new LinkedList<>();
        musics = new LinkedList<>();
        Random rand = new Random();
        float f;
        for (int i = 0; i < 10; i++ ) {
            f = rand.nextFloat();
            ground.add (new Ground(i, f, 64));
            musics.add(new IndieCD(i*80, f));
        }
        localNatives = new IndieCD(50, rand.nextFloat());
        
//        player.setEntities(ground);
        player.addGravity();
        
//        x1 = player.getX();
//        y1 = player.getY();
        
//        translate_x_1 = d_width/2;
//        translate_y_1 = d_height/2;

    }
    
    @Override
    public boolean drawFrame(float delta) {
        
        // Update the background tiles and other static entities asynchronously.
        Runnable updateE = () -> {
            background0.update(delta);
            background1.update(delta);
            background2.update(delta);
            background3.update(delta);
            background4.update(delta);
        };
        
        updateE.run();

        Thread thread = new Thread(updateE);
        thread.start();
        
        // Update player and pickups on main thread
        localNatives.update(delta);
        player.update(delta);
        
        // Check for intersects after updating on a new thread.
        Runnable checkInter = () -> {
             // Update the other entities
            for  (Ground g : ground) {
                if (g.intersects(player)){
                    player.intersectGround(g);
                }
            }
            
            for (IndieCD cd : musics) {
                if (cd.intersects(player)) {
                    if (cd.pickupItem()) {
                        audio.play("strum");
                        player.addItem(cd);
                        cd.cleanUp();
                    }
                }
            }
            
            if (localNatives.intersects(player)) {
                if (localNatives.pickupItem()) {
                    audio.play("strum");
                    player.addItem(localNatives);
                    localNatives.cleanUp();
                }
            }
        };
        
        checkInter.run();
        
        Thread thread2 = new Thread(checkInter);
        thread2.start();
        
        
//        background0.update(delta);
//        background1.update(delta);
//        background2.update(delta);
//        background3.update(delta);
//        background4.update(delta);
//        // Update the other entities
//        for  (Entity g : ground) {
//            if (g.intersects(player)){
//                
//            }
//        }
//        
//        if (localNatives.intersects(player)) {
//            if (localNatives.pickupItem()) {
//                player.addItem(localNatives);
//            }
//        }
        
        // After updating and before drawing, we take the player's 
        // position and we 
        float p_x = player.getX();
        float p_y = player.getY();
        
//        x2 = player.getX();
//        y2 = player.getY();
        
        float translate_y = d_height/2;
        float translate_x = d_width/2;
        
        
//        // log current x position of player on screen 
//        // ignore if unchaged
//        // NOTE: X never changes, so will never be displayed
//        if ( x2 != x1 ) {
//            System.out.println("Player x: " + x2 );
//        }
//
//        // log current y position of player on screen
//        // ignore if unchanged
//        // NOTE: changes a lot during jumps
//        if ( y2 != y1 ) {
//            System.out.println("Player y: " + y2 );
//        }

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);



        // 50% Parallax scrolling
        // Pushes current matrix used for screen operations, then
        // translates it by half. 
        
        GL11.glPushMatrix();
            GL11.glTranslatef(0, 0, 0);
            background0.draw();
            
            
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
            GL11.glTranslatef(-(p_x-translate_x)/6, 0, 0);
            background1.draw();
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
            GL11.glTranslatef(-(p_x-translate_x)/5, 0, 0);
            background2.draw();
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
            GL11.glTranslatef(-(p_x-translate_x)/4, 0, 0);
            background3.draw();
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
            GL11.glTranslatef(-(p_x-translate_x)/3, 0, 0);
            background4.draw();
        GL11.glPopMatrix();
        

        GL11.glPushMatrix();
        
//            if ( (-(((x2/d_width)-d_width)+translate_x_1)) <= 0 || (-(((x2/d_width)-d_width)+translate_x_1)) >= d_width ) {
//                // Player is too close to left or right of screen.
//                if ( (-(((y2/d_height)-d_height)+translate_y_1+100)) <= 0 || (-(((y2/d_height)-d_height)+translate_y_1+100)) >= d_height ) {
//                    // Player is too close to top or bottom of screen.
//                    GL11.glTranslatef(-(x2-translate_x_1), -(y2-translate_y_1-100), 0);
//                } else { 
//                    GL11.glTranslatef(-(x2-translate_x_1), -(y2-translate_y_1-100)/2, 0);
////                    translate_y_2 = (y2-translate_y_1-100)/2;
//                }
//            } else {
//                if ( (-(((y2/d_height)-d_height)+translate_y_1+100)) <= 0 || (-(((y2/d_height)-d_height)+translate_y_1+100)) >=  d_height ) {
//                    // Player is too close to top or bottom of screen.
//                    GL11.glTranslatef(-(x2-translate_x_1)/2, -(y2-translate_y_1-100), 0);
////                    translate_x_2 = (x2-translate_x_1)/2;
//                } else {
//                    GL11.glTranslatef(-(x2-translate_x_1)/2, -(y2-translate_y_1-100)/2, 0);
////                    translate_x_2 = (x2-translate_x_1)/2;
////                    translate_y_2 = (y2-translate_y_1-100)/2;
//                }
//            }
//            if ( -(x2-translate_x_1) <= (-((x2/(d_width/4))-(d_width/4)+translate_x_1)) || -(x2-translate_x_1) >= (-((x2/(3*(d_width/4)))-3*(d_width/4)+translate_x_1))  ) {
//                // Player is too close to left or right of screen.
//                if ( -(y2-translate_y_2-100) <= (-((y2/((d_height/4)+100))-((d_height/4)+100)+translate_y_1)) || -(y2-translate_y_1-100) >= (-((y2/((3*(d_height/4))+100))-(3*(d_height/4))+100+translate_y_1)) ) {
//                    // Player is too close to top or bottom of screen.
//                    GL11.glTranslatef(-(x2-translate_x_1), -(y2-translate_y_1-100), 0);
//                } else { 
//                    GL11.glTranslatef(-(x2-translate_x_1), -(y2-translate_y_1-100)/2, 0);
////                    translate_y_2 = (y2-translate_y_1-100)/2;
//                }
//            } else {
//                if ( -(y2-translate_y_2-100) <= (-((y2/((d_height/4)+100))-((d_height/4)+100)+translate_y_1)) || -(y2-translate_y_1-100) >= (-((y2/((3*(d_height/4))+100))-(3*(d_height/4))+100+translate_y_1)) ) {
//                    // Player is too close to top or bottom of screen.
//                    GL11.glTranslatef(-(x2-translate_x_1)/2, -(y2-translate_y_1-100), 0);
////                    translate_x_2 = (x2-translate_x_1)/2;
//                } else {
//                    GL11.glTranslatef(-(x2-translate_x_1)/2, -(y2-translate_y_1-100)/2, 0);
////                    translate_x_2 = (x2-translate_x_1)/2;
////                    translate_y_2 = (y2-translate_y_1-100)/2;
//                }
//            }
            
            GL11.glTranslatef(-(p_x-translate_x), -(p_y-translate_y-200), 0);
            // Draw the other entities
            for  (Entity e : ground) {
                e.draw();
            }
            
            for (IndieCD m : musics) {
                m.draw();
            }
            localNatives.draw();
            player.draw();
            
        GL11.glPopMatrix();
        // END 100% Parallax
        
        //Add the Item count to the Back layer which is static
            String s = "Items: " + player.getItemCount();
            TrueTypeFont itemCountFont = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 24), true);
            
            itemCountFont.drawString(100, 50, s, Color.yellow);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
//        x1 = x2;
//        y1 = y2;
//        
//        translate_x_1 = translate_x_2;
//        translate_y_1 = translate_y_2;

        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE))
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    
}
