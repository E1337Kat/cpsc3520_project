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
package World;

import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;

/**
 * The parent class to all Entities, statics, NPCs, etc.
 * @author Ellie
 */
public abstract class WorldObject {
    
    public Rectangle hitbox;
    protected Texture texture = null;
    
    /**
     * Gets the type of an Entity. Needs to be overriden for better interaction
     * between entities.
     * @return The type of the Entity.
     */
    public OBJECT_TYPE getType () {
        return OBJECT_TYPE.OTHER;
    }
    
    /**
     * Checks if this Entity intersects with another entity. Override if you 
     * want to implement further code.
     * @param other The other Entity to check against.
     * @return true if the entities are intersecting.
     */
    public boolean intersects(WorldObject other) {
        return hitbox.intersects(other.hitbox);
    }
    
    /**
     * Gets the intersection between two objects.
     * @param other The other Entity to get the intersection with.
     * @return The rectangle representing the intersection.
     */
    public Rectangle intersection(WorldObject other) {
        Rectangle l = new Rectangle();
        return hitbox.intersection(other.hitbox, l);
    }
    
    /**
     * Gets the Entities x coordinate.
     * @return The Entity's x coordinate.
     */
    public final int getX() {
        return hitbox.getX();
    }
    
    /**
     * Gets the Entities y coordinate.
     * @return The Entity's y coordinate.
     */
    public final int getY() {
        return hitbox.getY();
    }
    
    /**
     * Gets the Entities width.
     * @return The Entity's width.
     */
    public final int getWidth() {
        return hitbox.getWidth();
    }
    
    /**
     * Gets the Entities height.
     * @return The Entity's height.
     */
    public final int getHeight() {
        return hitbox.getHeight();
    }
    
    
    /**
     * For collision. It represents the side of a 2D object.
     */
    protected static enum SIDES { TOP,
    BOTTOM,
    LEFT,
    RIGHT,
    TOP_LEFT,
    TOP_RIGHT,
    BOTTOM_LEFT,
    BOTTOM_RIGHT }
    
    
    /**
     * Describes the type of the entity in question.
     */
    protected static enum OBJECT_TYPE { BACKGROUND,
    PLAYER,
    GROUND,
    NPC,
    PICKUP,
    OTHER
    };
}
