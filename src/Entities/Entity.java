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


import org.lwjgl.util.Rectangle;



public class Entity
{
    /**
     * For collision. It represents the side of a 2D object.
     */
    public static enum SIDES { TOP, 
                                BOTTOM, 
                                LEFT, 
                                RIGHT, 
                                TOP_LEFT, 
                                TOP_RIGHT, 
                                BOTTOM_LEFT, 
                                BOTTOM_RIGHT };
    
    
    public static enum E_TYPE { BACKGROUND,
                                PLAYER,
                                GROUND,
                                NPC,
                                OTHER
    };
    
    public void update(float delta)
    {
        
    }


    public void draw()
    {
        
    }
    
    // override this if you want to be able to see if your entity interacts
    // with another rectangle
    public boolean intersects(Rectangle other)
    {
        return false;
    }
    
    public E_TYPE getEntityType () {
        return E_TYPE.OTHER;
    }
}
