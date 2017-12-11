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
// import World.WorldMap;
import World.Statics.Background;
import World.Statics.Ground;
import World.Statics.Platform;
import World.Entities.Items.IndieCD;
import World.Entities.GravPoint;
import World.Entities.Player;
import World.Statics.LevelEnd;
import World.Statics.Translator;

import java.awt.Font;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
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
    private static List<GravPoint> gravPoints;
    
    private static LevelEnd levelEnd;
    private boolean finLevel = false;

    private static Translator offset;


    // private WorldMap woodsMap;
    
    private int x_old, y_old;
    private static final java.util.logging.Logger LOG = Logger.getGlobal();
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

        // woodsMap = new WorldMap(1000,1000);
        
        // Load Player
        player = new Player();
        
        background0 = new Background(0, PATH_TO_BG + "parallax-mountain-bg.png");
        background1 = new Background(1, PATH_TO_BG + "parallax-mountain-mountain-far.png");
        background2 = new Background(2, PATH_TO_BG + "parallax-mountain-mountains.png");
        background3 = new Background(3, PATH_TO_BG + "parallax-mountain-trees.png");
        background4 = new Background(4, PATH_TO_BG + "parallax-mountain-foreground-trees.png");
        
        levelEnd = new LevelEnd(2560);

        offset = new Translator(player);

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

        gravPoints = new LinkedList<>();

        Mouse.setGrabbed(false);

        x_old = player.getX();
        y_old = player.getY();
    }
    
    @Override
    public Scene nextScene() {
        return Overworld.getOverworld();
    }
    
    @Override
    public boolean drawFrame(float delta) {
        
        while (Mouse.next())
        {
            // Get a standard x and y for where a click was placed.  
            int x_mouse = (int)(Mouse.getEventX());
            int y_mouse = (int)(Display.getHeight()-Mouse.getEventY());

            /**************************
             * (0,0)            (m,0) *
             *                        *
             *                        *
             * (0,n)            (m,n) *
             **************************/

            // set so that wherever clicked, the new grav well be at the center of a block of 10.
            x_mouse += offset.getOffset_x();
            y_mouse += offset.getOffset_y();
            
            x_mouse = (x_mouse - (x_mouse%10) + 5);
            y_mouse = (y_mouse - (y_mouse%10) + 5);

            if (Mouse.getEventButtonState()) {
                int buttonClick = Mouse.getEventButton();

                if (buttonClick == 0) {
                    LOG.log(Level.FINE, "New grav well placed at: (" + x_mouse + ", " + y_mouse + ").");
                    gravPoints.add(new GravPoint(x_mouse, y_mouse));
                }

                if (buttonClick == 1) {
                    // x_mouse += viewportTranslation.getX();
                    // y_mouse += viewportTranslation.getY();
                    
                    // x_mouse = (x_mouse - (x_mouse%10) + 5);
                    // y_mouse = (y_mouse - (y_mouse%10) + 5);
                    // System.out.println("Mouse event (in world) at: (" + x_mouse + ", " + y_mouse + ").");
                }
            } else {
                // Implements Click and drag functionality.
                // if (Mouse.isButtonDown(0) && (row2 != row || col2 != col) ) {
                //     obstacles[row][col] = !obstacles[row][col];
                // }
            }

        }

        for (GravPoint g : gravPoints) {
            player.addForce(g.getGravVector(player));
        }
        
        // Update player and pickups on main thread
        // player.update(delta);

        player.update(delta);        
        offset.update(delta);
        
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
        float x_new = player.getX();
        float y_new = player.getY();
        
        float translate_y = d_height/2;
        float translate_x = d_width/2;
        
        
        // log current x position of player on screen 
        // ignore if unchaged
        // NOTE: X never changes, so will never be displayed
        if ( x_old != (int)x_new ) {
            LOG.log(Level.FINER, "Player has x: " + (int)x_new);
        }

        // log current y position of player on screen
        // ignore if unchanged
        // NOTE: changes a lot during jumps
        if ( y_old != (int)y_new ) {
            LOG.log(Level.FINER, "Player has y: " + (int)y_new);
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
            GL11.glTranslatef(-(x_new-translate_x)/6, 0, 0);
            background1.draw();
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
            GL11.glTranslatef(-(x_new-translate_x)/5, 0, 0);
            background2.draw();
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
            GL11.glTranslatef(-(x_new-translate_x)/4, 0, 0);
            background3.draw();
        GL11.glPopMatrix();
        
        GL11.glPushMatrix();
            GL11.glTranslatef(-(x_new-translate_x)/3, 0, 0);
            background4.draw();
        GL11.glPopMatrix();

        GL11.glPushMatrix();
            GL11.glTranslatef(-(x_new-100), -(y_new-(Display.getHeight()-10)), 0);
            for (GravPoint grav : gravPoints) {
                grav.draw();
            }

        GL11.glPopMatrix();

        GL11.glPushMatrix();
            GL11.glTranslatef(-(x_new-translate_x), -(y_new-translate_y-100), 0);

            // offset.draw();

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
        
        x_old = (int)x_new;
        y_old = (int)y_new;

        return !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
        
        
    }
    
}

