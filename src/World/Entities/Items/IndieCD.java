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
package World.Entities.Items;

import World.Entities.Entity;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;

/**
 * This is an Indie CD... probably has some slow banjo strumming.
 * @author Ellie
 */
public class IndieCD extends Pickups {
    
    private static final String PATH = "sprites/indie_disk.png";

    private static final Logger LOG = Logger.getLogger(IndieCD.class.getName());
    
    /**
     * Creates a new IndieCD object. Kinda generates world locations 
     * dynamically.
     * @param num For adding multiples, place in the list this occurs.
     * @param rand A random float to generate the location of the Entity.
     */
    public IndieCD (int num, float rand) {
        super(PATH, (int)(rand*10)*num, Display.getHeight()-100-(int)(rand*10));
        
    } 
    
    /**
     * Quick and dirty method that checks intersection. returns false if the 
     * CD has been taken, returns the super method otherwise.
     * @param other The other entity to test against.
     * @return Whether the entity intersects.
     */
    @Override 
    public boolean intersects(Entity other) {
        if (taken) {
            return false;
        } else {
            return super.intersects(other);
        }
    }
}
