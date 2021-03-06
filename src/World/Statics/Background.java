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

import World.Entities.Entity;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

/**
 *
 * @author ellie
 */
public class Background extends Statics {
    private static final Logger LOG = Logger.getLogger(Background.class.getName());
    private float foregroundness;
    private int x,y,w,h;
    private List<Background> extendedBG;
    private boolean heck = false;

    /**
     * Sets the background at a certain 'foregroundness'. 'Foregroundness' 
     * here is 0 to some n, and represnts how close the background is to the
     * player. A foregroundness of 0 is the furthest background from the player,
     * and is static. each subsequent background will move quicker as it is 
     * closer to the foreground
     * @param foregroundness how close to the foreground some bg is.
     * @param relativeFilePath relative path to the bg texture from the 
     *        game run location.
     */
    public Background(int foregroundness, String relativeFilePath) {
        
        this.foregroundness = (float)foregroundness/10;
        
        String[] s = {"", "" };
        s[0] = Integer.toString(foregroundness);
        s[1] = Float.toString(this.foregroundness);
        LOG.log(Level.FINE, "Foregroundness for bg {0} is: {1}", s);
        
        
        x = 0;
        y = 0;        
        w = Display.getWidth() + 50;
        h = Display.getHeight() + 50;
        
        if (foregroundness != 0) {
            extendedBG = new LinkedList<>();
            for (int i=0; i<10; i++) {
                extendedBG.add( new Background(relativeFilePath, x+w*i, y, w, h) );
            }
        } else {
            loadTexture(relativeFilePath);
            hitbox = new Rectangle(x,y,w,h);
        }
        
    }
    
    public Background(String pngPath){
        x = 0;
        y = 0;        
        w = Display.getWidth();
        h = Display.getHeight();
        
        loadTexture(pngPath);
        hitbox = new Rectangle(x,y,w,h);
    }
    
    public Background(String pngPath, int repeatNum, int topLeft, float width, float height) {
        super(repeatNum*(Display.getHeight()/2), 
                0, 
                Display.getHeight()/2, 
                Display.getHeight());
        spriteSize = 200f;
        loadTiledTexture(pngPath);
    }
    
    private Background (String s, int x, int y, int w, int h) {
        super(s,w,h,x,y);
    }
    
    @Override
    public void draw() {

        if (heck) {
            super.draw();
        } else {
            if (foregroundness != 0.0f) {
                for (Background b : extendedBG) {
                    heck = true;
                    b.draw();
                    heck = false;
                }
            } else {
                super.draw();
            }
        }
    }
    
    @Override
    public Entity.OBJECT_TYPE getType() {
        return OBJECT_TYPE.BACKGROUND;
    }
}

