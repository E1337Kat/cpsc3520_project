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
import org.lwjgl.opengl.Display;

public class Ground extends Entity {

    private final int groundNum;

    public Ground(int place, Random rand, int playerHeight)
    {
        super("res/ground.png", 
                ((int)(10*rand.nextFloat())+(place))*256, 
                (Display.getHeight() - (((int)(10*rand.nextFloat())+(place))*10)), 
                256, 
                512);
        groundNum = place;
        
//        heck = ((int)(10*rand.nextFloat())+(place));
//        System.out.println("Ground entity generated with rand: " + heck);
//        if ( heck > 3+(2*place)) {
//            heck = 3+((2*place));
//        }
//        
        
    }


    @Override
    public void update(float delta)
    {
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            super.translate((int)(.50*delta), 0);
            
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            super.translate((int)(-.50*delta), 0);
        }
        
        
    }
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.GROUND;
    }
    
    /** 
     * Returns the side of a 2D object the provided object intersects with.
     * @param other The object to test intersection with
     * @return The side of this Ground object `other` intersects with. See E_Entity SIDES.
     */
//    public Entity.SIDES intersectSide(Entity other) {
//        if (!this.intersects(other)) {
//            return null;
//        } else {
//            
//            Rectangle inter = new Rectangle();
//            box.intersection(other, inter);
//            
//            // check other is left edge.
//            //if (inter.getX() + inter.getWidth() <= super.getX() + inter.getWidth()) {
//                
//           // }
//            
//            // OLD CODE; REWRITE ABOVE.
//            // Other is left of ground (on edge)
//            if ( (other.getX() + other.getWidth()) <= (super.getX() + other.getWidth()) ) {
//                if ( other.getY() + other.getHeight() < super.getY() ) {
//                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_LEFT);
//                    return SIDES.TOP_LEFT;
//                }
//                if ( other.getY() > super.getY() + super.getHeight() ) {
//                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_LEFT);
//                    return SIDES.BOTTOM_LEFT;
//                }
//            } else
//            
//            // Other is right of ground (on edge)
//            if ( (other.getX() + other.getWidth()) >= (super.getX() + super.getWidth()) ) {
//                if ( other.getY() + other.getHeight() < super.getY() ) {
//                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_RIGHT);
//                    return SIDES.TOP_RIGHT;
//                }
//                if ( other.getY() > super.getY() + super.getHeight() ) {
//                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_RIGHT);
//                    return SIDES.BOTTOM_RIGHT;
//                }
//            } else
//            
//            // Other is left of ground
//            if ( (other.getX() + other.getWidth()) <= super.getX() ) {
//                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.LEFT);
//                return SIDES.LEFT;
//            } else
//            
//            // Other is right of ground
//            if ( other.getX() >= (super.getX() + super.getWidth()) ) {
//                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.RIGHT);
//                return SIDES.RIGHT;
//            } else
//            
//            // Other is top of ground
//            if ( other.getY() + other.getHeight() <= super.getY() ) {
//                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP);
//                return SIDES.TOP;
//            } else
//                
//            // Other is bottom of ground
//            if ( other.getY() >= super.getY() + super.getHeight() ) {
//                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM);
//                return SIDES.BOTTOM;
//            }
//            
//        }
//        
//        // intersection may be inside ground or 
//        // no intersection exists that was not accounted for.
//        return null;
//    }
    
    /**
     * Gets the x position for this object
     * @return x position
     */
    public int getX () {
        return super.getX();
    }
    
    /**
     * Gets the y position for this object
     * @return y position
     */
    public int getY () {
        return super.getY();
    }
    
    /**
     * Gets the rectangle for this object
     * @return box
     */
    public Rectangle getRectangle () {
        return super.getRectangle();
    }
    
}
