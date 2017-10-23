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

import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

/**
 * This is an Indie CD... probably has some slow banjo strumming.
 * @author Ellie
 */
public class IndieCD extends Pickups {
    
    private static final String PATH = "res/sprites/indie_disk.png";
    
    public IndieCD (int num, float rand) {
        super(PATH, (int)(rand*10)*num, Display.getHeight()-100-(int)(rand*10));
        
    } 
    
    
    @Override 
    public boolean intersects(Entity other) {
        if (taken) {
            return false;
        } else {
            return super.intersects(other);
        }
    }
//    @Override
//    public boolean intersects(Entity other) {
//        Rectangle heck = intersection(this);
//        
//        if (    (this.getX() >= heck.getX()) && 
//                (this.getX()+this.getWidth() <= heck.getX()+heck.getWidth()) &&
//                (this.getY() >= heck.getY()) && 
//                (this.getY()+this.getHeight() <= heck.getY()+heck.getHeight())
//            ) 
//        {
//            return true;
//        } else {
//            return false;
//        }
//    }
   
}
