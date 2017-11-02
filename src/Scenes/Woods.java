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

import Main.AudioManager;
import World.Statics.Background;
import World.Statics.Ground;
import World.Entities.Items.IndieCD;
import World.Entities.Player;
import World.Statics.LevelEnd;
import java.awt.Font;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

/**
 *
 * @author Ellie
 */
public class Woods extends Scene {
    
    
    public static AudioManager audio = AudioManager.getInstance();

    public static final String PATH_TO_BG = "parallax_mountain_pack/layers/";
    
    // Entities that exist on this level.
    private static Background background0;
    private static Background background1;
    private static Background background2;
    private static Background background3;
    private static Background background4;
    private static IndieCD localNatives;
    private static Player player;
    private static List<Ground> ground;
    private static List<IndieCD> musics;
    
    private static LevelEnd levelEnd;
    private boolean finLevel = false;
    
    private int x2,y2;
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Woods.class.getName());
    private final int d_width = Display.getWidth();
    private final int d_height = Display.getHeight();
    TrueTypeFont itemCountFont = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 24), true);
    
    /**
     * Creates a new level
     */
    public Woods() {
        try {
            audio.loadSample("strum", "audio/188037__antumdeluge__guitar-strumming.wav");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error occured loading audio sample: {0}", e.getMessage());
        }
        
        // Load Player
        player = new Player();
        
        background0 = new Background(0, PATH_TO_BG + "parallax-mountain-bg.png");
        background1 = new Background(1, PATH_TO_BG + "parallax-mountain-mountain-far.png");
        background2 = new Background(2, PATH_TO_BG + "parallax-mountain-mountains.png");
        background3 = new Background(3, PATH_TO_BG + "parallax-mountain-trees.png");
        background4 = new Background(4, PATH_TO_BG + "parallax-mountain-foreground-trees.png");
        
        levelEnd = new LevelEnd(2560);

        // Load Ground tiles
        ground = new LinkedList<>();
        musics = new LinkedList<>();
        Random rand = new Random();
        float f;
        for (int i = 0; i < 10; i++ ) {
            f = rand.nextFloat();
            ground.add (new Ground(i, f));
            musics.add(new IndieCD(i*80, f));
        }
        localNatives = new IndieCD(50, rand.nextFloat());
        player.addGravity();

        x2 = player.getX();
        y2 = player.getY();
    }
    
    @Override
    public Scene nextScene() {
        return Overworld.getOverworld();
    }
    
    @Override
    public boolean drawFrame(float delta) {
        
        
        
        // Update player and pickups on main thread
        player.update(delta);
        
        // Check for intersects after updating on a new thread.
        Runnable checkInter = () -> {
             // Update the other entities
            for  (Ground g : ground) {
                if (g.intersects(player)){
                    player.intersectObject(g);
                }
            }
            
            for (IndieCD cd : musics) {
                if (cd.intersects(player)) {
                    if (cd.pickupItem()) {
                        audio.play("strum");
                        player.addItem(cd);
                    }
                }
            }
            
            if (localNatives.intersects(player)) {
                if (localNatives.pickupItem()) {
                    audio.play("strum");
                    player.addItem(localNatives);
                }
            }
            
            if (levelEnd.intersects(player)) {
                Overworld.comp[0] = true;
                finLevel = true;
            }
        };
        
        checkInter.run();
        
        Thread thread2 = new Thread(checkInter);
        thread2.start();
        
        if (finLevel) {
            return false;
        }
        
        // After updating and before drawing, we take the player's 
        // position and we 
        float p_x = player.getX();
        float p_y = player.getY();
        
        float translate_y = d_height/2;
        float translate_x = d_width/2;
        
        
        // log current x position of player on screen 
        // ignore if unchaged
        // NOTE: X never changes, so will never be displayed
        if ( x2 != (int)p_x ) {
            LOG.log(Level.FINER, "Player has x: {0}", (int)p_x);
        }

        // log current y position of player on screen
        // ignore if unchanged
        // NOTE: changes a lot during jumps
        if ( y2 != (int)p_y ) {
            LOG.log(Level.FINER, "Player has y: {0}", (int)p_y);
        }

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);


        /*
            Here we have decided to implement parrallax scrolling using 
            matrix rtanslations of various speeds for the bg, and constant 
            for all but the player.
        */
        
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
            
            GL11.glTranslatef(-(p_x-translate_x), -(p_y-translate_y-100), 0);
            // Draw the other entities
            for  (Ground e : ground) {
                e.draw();
            }
            
            for (IndieCD m : musics) {
                m.draw();
            }
            localNatives.draw();
            player.draw();
            
        GL11.glPopMatrix();
        // END 100% Parallax
        
        //Add the Item count to the top left
        String s = "Items: " + player.getItemCount();
        TextureImpl.bindNone();
        itemCountFont.drawString(40, 40, s, Color.yellow);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        String t = "Use the arrow keys! Space can also Jump, too!";
        TextureImpl.bindNone();
        itemCountFont.drawString(40, 60, t, Color.yellow);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        x2 = (int)p_x;
        y2 = (int)p_y;

        return !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
        
        
    }
    
}

