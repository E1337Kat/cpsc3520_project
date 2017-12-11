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

import org.lwjgl.opengl.Display;

import World.Entities.Player;

/**
 *
 * @author Ellie
 */
public class LevelEnd extends Statics{

    public int levelNum;
    private boolean trackPlayer = false;
    private Player player;
    
    
    public LevelEnd(int levelLength) {
        super(levelLength, Display.getHeight()-100, 100, Display.getHeight());
        
    }
    
    public LevelEnd(int x, int y, int levelNum){
        super(x,y,64,64);
        this.levelNum = levelNum;
    }

    /** 
     * Default constructor that creates a 1x1 hitbox to test against.
     * this hitbox could be placed in top corner to help translate 
     * viewport coordinates to world coordinates.
     */
    public LevelEnd(Player p) {
        super(0,0,1,1);
        player = p;
    }

    public void setOnPlayer(boolean val) {
        trackPlayer = val;
    }

    @Override
    public void draw() {
        if (trackPlayer) {
            setX(player.getX());
            setY(player.getY());
        }
        super.draw();
    }
}
