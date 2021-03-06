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
package World.Statics;

import World.Statics.Statics;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

public class Ground extends Statics {
    private static final Logger LOG = Logger.getLogger(Ground.class.getName());

    private final int groundNum;
    private int heck;

    /**
     * Generate a new ground Entity that the player can walk on.
     * @param place For multiples, describes the location in a list this ground is at.
     * @param rand A random float to help in generation.
     */
    public Ground(int place, float rand)
    {
        groundNum = place;
        
        this.heck = ((int)(10*rand)+(place));
        
        /*
        System.out.println("Ground entity generated with rand: " + this.heck);
        if ( this.heck > 3+(2*place)) {
            this.heck = 3+((2*place));
        }
        */
        
        loadTexture("sprites/ground.png");
        hitbox = new Rectangle(place*256, 
                                (int)(Display.getHeight() - (this.heck*10)), 
                                256, 
                                512);
    }
    
    @Override
    public OBJECT_TYPE getType() {
        return OBJECT_TYPE.GROUND;
    }
    
    
    /** 
     * Returns the side of a 2D object the provided object intersects with.
     * @param other The object to test intersection with
     * @return The side of this Ground object `other` intersects with. See E_Entity SIDES.
     */
    /*
    public Entity.SIDES intersectSide(Entity other) {
        if (!this.intersects(other)) {
            return null;
        } else {
            
            Rectangle inter = super.intersection(other);
            
            // check other is left edge.
            if (inter.getX() + inter.getWidth() <= super.getX() + super.getWidth()/2) {
                // Intersection is left side of ground
                if (inter.getY() + inter.getHeight() <= super.getY() + super.getHeight()/2) {
                    // Player is top side of ground 
                }
            }
            
            // OLD CODE; REWRITE ABOVE.
            // Other is left of ground (on edge)
            if ( (other.getX() + other.getWidth()) <= (super.getX() + other.getWidth()) ) {
                if ( other.getY() + other.getHeight() < super.getY() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_LEFT);
                    return SIDES.TOP_LEFT;
                }
                if ( other.getY() > super.getY() + super.getHeight() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_LEFT);
                    return SIDES.BOTTOM_LEFT;
                }
            } else
            
            // Other is right of ground (on edge)
            if ( (other.getX() + other.getWidth()) >= (super.getX() + super.getWidth()) ) {
                if ( other.getY() + other.getHeight() < super.getY() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_RIGHT);
                    return SIDES.TOP_RIGHT;
                }
                if ( other.getY() > super.getY() + super.getHeight() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_RIGHT);
                    return SIDES.BOTTOM_RIGHT;
                }
            } else
            
            // Other is left of ground
            if ( (other.getX() + other.getWidth()) <= super.getX() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.LEFT);
                return SIDES.LEFT;
            } else
            
            // Other is right of ground
            if ( other.getX() >= (super.getX() + super.getWidth()) ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.RIGHT);
                return SIDES.RIGHT;
            } else
            
            // Other is top of ground
            if ( other.getY() + other.getHeight() <= super.getY() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP);
                return SIDES.TOP;
            } else
                
            // Other is bottom of ground
            if ( other.getY() >= super.getY() + super.getHeight() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM);
                return SIDES.BOTTOM;
            }
            
        }
        
        // intersection may be inside ground or 
        // no intersection exists that was not accounted for.
        return null;
    }
    */
}
