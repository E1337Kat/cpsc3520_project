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

/**
 * This class represents the superclass of all items which the PC 
 * can pick up in her travels. 
 * @author Ellie
 */
public class Pickups extends Entity {
    protected boolean taken;
    
    public Pickups (String pngpath, int x, int y) {
        super(pngpath, 16, x, y);
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
    public Entity.E_TYPE getEntityType() {
        return Entity.E_TYPE.PICKUP;
    }
    
    public boolean pickupItem() {
        taken = true;
        
        return true;
    }
    
    public void cleanUp() {
        this.deactivate();
    }
}
