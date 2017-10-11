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

import java.util.Random;
import org.lwjgl.util.Rectangle;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

public class Ground extends Entity
{

    // private String intersect_side;
    // private static enum State  { START, LEFT, RIGHT };

    //private Player player;
    private Texture tex;
    private final Rectangle box;
    private int heck;
    private final int groundNum;
    private final float wr,hr; 
    
    //private float speed;        // pixels / ms

    public Ground(int place, Random rand, int playerHeight)
    {
        groundNum = place;
        
        heck = ((int)(10*rand.nextFloat())+(place));
        System.out.println("Ground entity generated with rand: " + heck);
        if ( heck > 3+(2*place)) {
            heck = 3+((2*place));
        }
        
        try {
            tex =
                TextureLoader.getTexture("PNG",
                                         ResourceLoader.getResourceAsStream("res/ground.png"));
            
            box = new Rectangle(
                            place*256, 
                            Display.getHeight() - (heck*10), 
                            256, 
                            512);
            
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        
        wr = 1.0f*tex.getImageWidth() / tex.getTextureWidth();
        hr = 1.0f*tex.getImageHeight() / tex.getTextureHeight();
        
    }

    @Override
    public void draw()
    {
        float x = (float)box.getX();
        float y = (float)box.getY();
        float w = (float)box.getWidth();
        float h = (float)box.getWidth();


        // draw the square

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
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

        GL11.glEnd();

    }

    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            box.translate((int)(.50*delta), 0);
            
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            box.translate((int)(-.50*delta), 0);
        }
        
        
    }
    
    @Override 
    public boolean intersects(Rectangle other) {
        return box.intersects(other);
    }
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.GROUND;
    }
    
    /**
     * Gets the intersection rectangle of the collision between two objects.
     * @param other
     * @return 
     */
    private Rectangle intersection(Rectangle other) {
        Rectangle inter = new Rectangle();
        
        inter = box.intersection(box, inter);
        
        return inter;
        
    }
    
    /** 
     * Returns the side of a 2D object the provided object intersects with.
     * @param other The object to test intersection with
     * @return The side of this Ground object `other` intersects with. See E_Entity SIDES.
     */
    public Entity.SIDES intersectSide(Rectangle other) {
        if (!this.intersects(other)) {
            return null;
        } else {
            
            Rectangle inter = new Rectangle();
            box.intersection(other, inter);
            
            // check other is left edge.
            //if (inter.getX() + inter.getWidth() <= box.getX() + inter.getWidth()) {
                
           // }
            
            // OLD CODE; REWRITE ABOVE.
            // Other is left of ground (on edge)
            if ( (other.getX() + other.getWidth()) <= (box.getX() + other.getWidth()) ) {
                if ( other.getY() + other.getHeight() < box.getY() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_LEFT);
                    return SIDES.TOP_LEFT;
                }
                if ( other.getY() > box.getY() + box.getHeight() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_LEFT);
                    return SIDES.BOTTOM_LEFT;
                }
            } else
            
            // Other is right of ground (on edge)
            if ( (other.getX() + other.getWidth()) >= (box.getX() + box.getWidth()) ) {
                if ( other.getY() + other.getHeight() < box.getY() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_RIGHT);
                    return SIDES.TOP_RIGHT;
                }
                if ( other.getY() > box.getY() + box.getHeight() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_RIGHT);
                    return SIDES.BOTTOM_RIGHT;
                }
            } else
            
            // Other is left of ground
            if ( (other.getX() + other.getWidth()) <= box.getX() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.LEFT);
                return SIDES.LEFT;
            } else
            
            // Other is right of ground
            if ( other.getX() >= (box.getX() + box.getWidth()) ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.RIGHT);
                return SIDES.RIGHT;
            } else
            
            // Other is top of ground
            if ( other.getY() + other.getHeight() <= box.getY() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP);
                return SIDES.TOP;
            } else
                
            // Other is bottom of ground
            if ( other.getY() >= box.getY() + box.getHeight() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM);
                return SIDES.BOTTOM;
            }
            
        }
        
        // intersection may be inside ground or 
        // no intersection exists that was not accounted for.
        return null;
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
    
    /**
     * Gets the rectangle for this object
     * @return box
     */
    public Rectangle getRectangle () {
        return box;
    }
    
    public int getHeck() {
        return heck;
    }


}
