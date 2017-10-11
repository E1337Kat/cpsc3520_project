package Main;

import Entities.Player;
import Entities.Ground;
import Entities.Entity;
import Entities.Background;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.lwjgl.LWJGLException;

import java.util.List;
import java.util.LinkedList;
import java.util.Random;



public class Gameloop
{
    public static final int TARGET_FPS=100;
    public static final int SCR_WIDTH=800;
    public static final int SCR_HEIGHT=600;
    
    private static Background background;
    private static Player player;

    public static void main(String[] args) throws LWJGLException
    {
        initGL(SCR_WIDTH, SCR_HEIGHT);
        
        // Load Player
        player = new Player(100);

        background = new Background();
        
        // Load Ground tiles
        List<Ground> ground = new LinkedList<>();
        Random rand = new Random();
        for (int i = 0; i < 10; i++ ) {
            ground.add (new Ground(i, rand, player));
        }

        int x1 = player.getX();
        int y1 = player.getY();
        
        long time = (Sys.getTime()*1000)/Sys.getTimerResolution(); // ms
        while (! Display.isCloseRequested())
        {
            int x2 = player.getX();
            int y2 = player.getY();
            long time2 = (Sys.getTime()*1000)/
                Sys.getTimerResolution(); // ms
            float delta = (float)(time2-time);

            // Update the background tiles
            background.update(delta, null);
            
            
            // check for collision and update player accordingly
            for (Entity e : ground) {
                if ( e.intersects(player.getRectangle()) ) {
                    player.update(delta, e);
                } else {
                    player.update(delta, null);
                }
            }
            
            // log current x position of player on screen 
            // ignore if unchaged
            // NOTE: X never changes, so will never be displayed
            if ( x2 != x1 ) {
                System.out.println("Player x: " + player.getX());
            }
            
            // log current y position of player on screen
            // ignore if unchanged
            // NOTE: changes a lot during jumps
            if ( y2 != y1 ) {
                System.out.println("Player y: " + player.getY());
            }
            
            // Update the other entities
            for  (Ground g : ground) {
                if ( player.intersects(g.getRectangle())) {
                    g.update(delta, player);
                } else {
                    g.update(delta, null);
                }
            }


            Display.update();
            
            // After updating and before drawing, we take the player's 
            // position and we 
            float translate_y = player.getY() - Display.getHeight() / 2;
            float translate_x = player.getX() + Display.getWidth() / 2;
            Display.sync(TARGET_FPS);
            
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);

            // 50% Parallax scrolling
            // Pushes current matrix used for screen operations, then
            // translates it by half. 
            GL11.glPushMatrix();
            GL11.glTranslatef(translate_x / 2, -translate_y / 2, 0);
            
            // Draw the background
            background.draw();
            
            GL11.glPopMatrix();
            // END 50% Parallax
            
            // Can add additional stuff between matrix stuff as needed.
            
            // 100% Parallax scrolling
            // 
            GL11.glPushMatrix();
            GL11.glTranslatef(translate_x, -translate_y, 0);
            
            // Draw the Player
            player.draw();
            
            // Draw the other entities
            for  (Entity e : ground) {
                e.draw();
            }
            GL11.glPopMatrix();
            // END 100% Parallax


            // Update delta
            time = time2;
            x1 = x2;
            y1 = y2;
        }

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
