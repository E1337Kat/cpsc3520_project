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
package Scenes;

import World.Entities.Player;
import World.Statics.Background;
import World.Statics.LevelEnd;
import World.Statics.Platform;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author Ellie
 */
public class House extends Scene {
    
    private static List<Background> bg_tiles;
    private static Platform floor;
    private static Player player;
    private static LevelEnd end;
    private static int d_width= Display.getWidth();
    private static int d_height = Display.getHeight();
    
    private int x2,y2;
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(House.class.getName());
    private boolean finLevel = false;
    
    public House() {
        
        bg_tiles = new LinkedList<>();
        
        for (int i = 0; i < 10; i++) {
            bg_tiles.add(new Background("interior_large.png", i, 0, 2.0f, 2.0f));
        }
        
        floor = new Platform(0, 
                             Display.getHeight()-20, 
                             10*(Display.getHeight()/2));
        
        player = new Player();
        end = new LevelEnd(10*(Display.getHeight()/2));
        
        player.intersectObject(floor);
        x2 = player.getX();
        y2 = player.getY();
    }
    
    @Override
    public Scene nextScene() {
        return Overworld.getOverworld();
    }

    @Override
    public boolean drawFrame(float delta) {
        
        player.update(delta);
        
        if (end.intersects(player)) {
            Overworld.comp[0] = true;
            finLevel = true;
        }
        
        if (finLevel) {
            Overworld.comp[1] = true;
            return false;
        }
        
        // After updating and before drawing, we take the player's 
        // position and we 
        float p_x = player.getX();
        float p_y = player.getY();
        
        float translate_y = d_height/2;
        float translate_x = d_width/2;
        
        // log current x position of player on screen 
        // ignore if unchaged
        // NOTE: X never changes, so will never be displayed
        if ( x2 != (int)p_x ) {
            LOG.log(Level.FINER, "Player has x: {0}", (int)p_x);
        }

        // log current y position of player on screen
        // ignore if unchanged
        // NOTE: changes a lot during jumps
        if ( y2 != (int)p_y ) {
            LOG.log(Level.FINER, "Player has y: {0}", (int)p_y);
        }

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(-(p_x-translate_x), 0, 0);
            for (Background b : bg_tiles) {
                b.drawTiled(0, 3, 3);
            }
            floor.draw();
            player.draw();
        GL11.glPopMatrix();
        
        x2 = (int)p_x;
        y2 = (int)p_y;
    
        return !Keyboard.isKeyDown(Keyboard.KEY_ESCAPE);
    }
    
}
