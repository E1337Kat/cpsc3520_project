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

/**
 * This class represents the superclass of all items which the PC 
 * can pick up in her travels. 
 * @author Ellie Peterson
 */
public class Pickups extends Entity {
    private static final Logger LOG = Logger.getLogger(Pickups.class.getName());
    protected boolean taken;
    
    /**
     * Initializes a new Entity that can be pick up and even used by the player.
     * @param pngpath path to the sprite
     * @param x initial x location (In world coords)
     * @param y initial y location (In world coords)
     */
    public Pickups (String pngpath, int x, int y) {
        super(pngpath, 16, x, y);
        taken = false;
    }
    
    /**
     * Default constructor. places the object in the world with 
     * no width at (0,0).
     * @param pngpath path to the sprite to use
     */
    public Pickups (String pngpath) {
        super(pngpath, 0, 0, 0);
        taken = false;
    }
    
    @Override
    public void update (float delta) {
        if (taken) 
            return;
        super.update(delta);
    }
    
    @Override
    public void draw() {
        if (taken) {
            return;
        }
        super.draw();
    }
    
    @Override
    public Entity.OBJECT_TYPE getType() {
        return Entity.OBJECT_TYPE.PICKUP;
    }
    
    /**
     * Picks up the item
     * @return true if the object is taken.
     */
    public boolean pickupItem() {
        if(taken = true) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * In theory cleans up the entity that was used to free memory. 
     * I think this works.
     */
    public void cleanUp() {
        this.deactivate();
        this.destroy();
    }
}
