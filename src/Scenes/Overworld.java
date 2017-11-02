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


import Main.AudioManager;
import World.Statics.Background;
import World.Entities.Player;
import World.Statics.LevelEnd;
import java.awt.Font;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.TextureImpl;

/**
 * Scene of a level that the player can play in.
 * @author Ellie
 */
public class Overworld extends Scene{
    
    private static Overworld lazySinglton;
    
    public static boolean[] comp = { false, false, false };
    
    public static AudioManager audio = AudioManager.getInstance();

    public static final String BG = "ShitWorld.png";
    
    private static Background background;
    private static Player player;
    
    private static List<LevelEnd> places;
    private static List<Scene> levels;
    
    private static Scene next;
    TrueTypeFont instructions = new TrueTypeFont(new Font("Times New Roman", Font.BOLD, 24), true);
    
    private static final java.util.logging.Logger LOG = java.util.logging.Logger.getLogger(Overworld.class.getName());
    
    /**
     * Creates a new level
     */
    private Overworld() {
        
        try {
            audio.loadSample("strum", "audio/188037__antumdeluge__guitar-strumming.wav");
        } catch (IOException e) {
            LOG.log(Level.SEVERE, "Error Occured with Audio Loading: {0}", e);
        }
        
        places = new LinkedList<>();
        levels = new LinkedList<>();
        
        
        levels.add(new Woods());
        levels.add(new House());
        levels.add(new CoffeeShop());
        
        
        background = new Background(BG);
        
        places.add(new LevelEnd(75,75, 1));
        places.add(new LevelEnd(375,275, 2));
        places.add(new LevelEnd(675,475, 3));
        
        // Load Player
        player = new Player(75);
    }
    
    /**
     * A lazy singlton method because why the fuck not. ¯\_(ツ)_/¯
     * @return the only Overworld instance allowed ever. :3
     */
    public static Overworld getOverworld() {
        if ( lazySinglton == null )
            lazySinglton = new Overworld();
        return lazySinglton;
    }
    
    /**
     * Sets the nextScene to load if needed.
     * @return The scene to load.
     */
    @Override
    public Scene nextScene() {
        if (next != null) {
            return next;
        }
        return null;
    }
    
    @Override
    public boolean drawFrame(float delta) {
        
       
        // Update player 
        player.updateMap(delta);
          
        
        for (LevelEnd p : places) {
            if (p.intersects(player)) {
                next = levels.get((p.levelNum)-1);
            }
        }
        
        if (comp[0])
            places.get(0).setRGB(0, 1, 0);
        if (comp[1])
            places.get(1).setRGB(0, 1, 0);
        if (comp[2])
            places.get(2).setRGB(0, 1, 0);
        
        background.draw();
        
        places.forEach((p) -> { 
            p.draw();
        });
        
        player.draw();
        
        //Add the Item count to the top left
        String s = "Use the left and right arrow keys to move "
                + "and space to select a level.";
        TextureImpl.bindNone();
        instructions.drawString(100, 50, s, Color.yellow);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
            return false;
        if (Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) {
            next = null;
            return false;
        }
        
        if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
            next = new House();
            return false;
        }
        return true;
    }
    
}
