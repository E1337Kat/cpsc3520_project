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
package Entities;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 *
 * @author ellie
 */
//import java.awt.font.NumericShaper.Range;
import Entities.Entity;
import java.util.LinkedList;
import java.util.List;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Rectangle;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;
import org.w3c.dom.ranges.Range;

public class Background extends Entity {
    
    private static enum State { FALLING, JUMPING };
    private State state;
    
    private Rectangle box;
    private Texture back;
    private float foregroundness;
    private float wr, hr;
    private List<Rectangle> boxes;
    
    private int x,y,w,h;

    /**
     * Sets the background at a certain 'foregroundness'. 'Foregroundness' 
     * here is 0 to some n, and represnts how close the background is to the
     * player. A foregroundness of 0 is the furthest background from the player,
     * and is static. each subsequent background will move quicker as it is 
     * closer to the foreground
     * @param foregroundness how close to the foreground some bg is.
     * @param relativeFilePath relative path to the bg texture from the 
     *        game run location.
     */
    public Background(int foregroundness, String relativeFilePath) {
        
        this.foregroundness = (float)foregroundness/10;
        System.out.println("Foregroundness for bg " + 
                            foregroundness + 
                            " is: " + 
                            this.foregroundness);
        
        try {
            back =
                TextureLoader.getTexture("PNG",
                                         ResourceLoader.getResourceAsStream(relativeFilePath));
            
            
            x = (0 - Display.getWidth()/4 - 50);
            y = (0 + Display.getHeight()/6 - 50);
            w = Display.getWidth() + 50;
            h = Display.getHeight() + 50;
            
            
            if (foregroundness != 0) {
                boxes = new LinkedList<>();
                for (int i=0; i<10; i++) {
                    boxes.add( new Rectangle(x+w*i, y, w, h) );
                }
            } else {
                box =  new Rectangle(x, y, w, h);
            }
            
            wr = 1.0f*back.getImageWidth() / back.getTextureWidth();
            hr = 1.0f*back.getImageHeight() / back.getTextureHeight();
            
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void draw() {

        
        if (foregroundness != 0.0f) {
            for (Rectangle r : boxes) {
                float x=(float)r.getX();
                float y=(float)r.getY();
                float w=(float)r.getWidth();
                float h=(float)r.getHeight();
                //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, back.getTextureID());
                GL11.glColor3f(1, 1,1);

                GL11.glBegin(GL11.GL_QUADS);


                GL11.glTexCoord2f(0,0);
                GL11.glVertex2f(x, y);

                GL11.glTexCoord2f(wr,0);
                GL11.glVertex2f(x+w, y);

                GL11.glTexCoord2f(wr,hr);
                GL11.glVertex2f(x+w, y+h);

                GL11.glTexCoord2f(0,hr);
                GL11.glVertex2f(x, y+h);
            }
        } else {
            float x=(float)box.getX();
            float y=(float)box.getY();
            float w=(float)box.getWidth();
            float h=(float)box.getHeight();
            
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, back.getTextureID());
            GL11.glColor3f(1, 1,1);

            GL11.glBegin(GL11.GL_QUADS);


            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f(wr,0);
            GL11.glVertex2f(x+w, y);

            GL11.glTexCoord2f(wr,hr);
            GL11.glVertex2f(x+w, y+h);

            GL11.glTexCoord2f(0,hr);
            GL11.glVertex2f(x, y+h);
        }

        GL11.glEnd();

        // unbind the sprite so that other objects can be drawn
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        

    }
    
    @Override
    public void update(float delta) {
        
        //if (state == State.LEVEL) {
        
        
        // draw this rectangle using the loaded sprite
        if (this.foregroundness != 0.0f) {
            for (Rectangle r : boxes) {
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) 
                    r.translate((int)(this.foregroundness*delta), 0);
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) 
                    r.translate((int)(-this.foregroundness*delta), 0);
            }
        } else {
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) 
                box.translate((int)(this.foregroundness*delta), 0);
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) 
                box.translate((int)(-this.foregroundness*delta), 0);
        }
        
    }
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.BACKGROUND;
    }
    
    /**
     * Gets the x position for this object
     * @return x position
     */
    public int getX () {
        return box.getX();
    }
    
    /**
     * Gets the y position for this object
     * @return y position
     */
    public int getY () {
        return box.getY();
    }
}

