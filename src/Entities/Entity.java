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


import java.io.IOException;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;



public class Entity {
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
    
    protected Rectangle hitbox;
    protected Texture texture = null;
    
    // Static texture
    protected float wr, hr;
    protected float r=1,g=1,b=1;
    private boolean active = true;
    
    // Animated texture
    protected boolean animated = false;
    protected boolean moving = false;
    protected int xSpriteCoord, ySpriteCoord; 
    protected float spriteSize = 64f;
    
    public Entity() {
        hitbox = new Rectangle(); // empty rectangle
    }

    public Entity(int x, int y, int w, int h) {
        hitbox = new Rectangle(x,y,w,h); // non-empty rectangle
    }
    
    public Entity(String texturePath, int width, int x, int y) {
        loadTexture(texturePath);
        hitbox = new Rectangle(x, y,
                               width,
                               (int)(width * texture.getImageHeight()* 1.0f/texture.getImageWidth()));
        
    }
    
    public Entity(String texturePath, int width, int height, int x, int y) {
        loadTexture(texturePath);
        hitbox = new Rectangle(x, y,
                               width,
                               (int)(width * texture.getImageHeight()* 1.0f/texture.getImageWidth()));
    }
    
    /**
     * Similar to the other constructor, except this one allows for 
     * sprite animations 
     * @param spriteSize
     * @param x
     * @param y
     * @param spritePath 
     */
    public Entity(float spriteSize, int x, int y, String spritePath) {
        this.spriteSize = spriteSize;
        animated = true;
        loadTexture(spritePath);
        hitbox = new Rectangle(x,y,(int)spriteSize,(int)spriteSize);
    }

    /*******PUBLIC MEMBERS********?

    /**
     * 
     * @param path 
     */
    public void loadTexture(String path) {
        if (animated) {
            try {
                texture = TextureLoader.getTexture("PNG",
                                                   ResourceLoader.getResourceAsStream("res/heck.png"));
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to load texture.");
            }
        } else {
            try {

                // load texture as png from res/ directory (this can throw IOException)
                texture = TextureLoader.getTexture("PNG",
                                                   ResourceLoader.getResourceAsStream(path));

                // textures come in as a power of 2.  use these ratios to
                // calculate texture offsets for sprite based on box size
                wr = (1.0f)*texture.getImageWidth() / texture.getTextureWidth();
                hr = (1.0f)*texture.getImageHeight() / texture.getTextureHeight();
            } catch(IOException e) {
                throw new RuntimeException("failed to load Texture.");
            }
        }
    }

    public void setRGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    public void init() {
    }

    public void destroy() {
    }
    
    public void update(float delta) {
        
    }

    public void draw() {
        if (animated) {
            float x=(float)hitbox.getX();
            float y=(float)hitbox.getY();
            float w=(float)hitbox.getWidth();
            float h=(float)hitbox.getHeight();

            // draw this rectangle using the loaded sprite
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
            GL11.glColor3f(1, 1,1);

            // Calculates animation. xSpriteCoord and ySpriteCoord are the x & y value of
            // particular sprite animation segment I want to use. since the sprite 
            // sheet I have picked is a 64x64 texture, I multiple the coord by 64
            // to get the actual texture x and y coord needed. I can then add 64 to
            // that to get the width or height. I then divide all that by the 
            // texture width or height just like with wr and hr previously.

            GL11.glBegin(GL11.GL_QUADS);

            if (moving) {
                GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/texture.getTextureWidth() , (ySpriteCoord * spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x, y);

                GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/texture.getTextureWidth(), (ySpriteCoord * spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x+w, y);

                GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/texture.getTextureWidth(), (ySpriteCoord*spriteSize + spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x+w, y+h);

                GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/texture.getTextureWidth() , (ySpriteCoord*spriteSize + spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x, y+h);
            } else {
                xSpriteCoord = 0;

                GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/texture.getTextureWidth() , (ySpriteCoord * spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x, y);

                GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/texture.getTextureWidth(), (ySpriteCoord * spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x+w, y);

                GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/texture.getTextureWidth(), (ySpriteCoord*spriteSize + spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x+w, y+h);

                GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/texture.getTextureWidth() , (ySpriteCoord*spriteSize + spriteSize)/texture.getTextureHeight() );
                GL11.glVertex2f(x, y+h);
            }



            GL11.glEnd();

            // unbind the sprite so that other objects can be drawn
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        } else {
            float x=(float)hitbox.getX();
            float y=(float)hitbox.getY();
            float w=(float)hitbox.getWidth();
            float h=(float)hitbox.getHeight();


            GL11.glColor3f(r,g,b);

            if (texture == null) {
                GL11.glBegin(GL11.GL_QUADS);
                GL11.glVertex2f(x, y);
                GL11.glVertex2f(x+w, y);
                GL11.glVertex2f(x+w, y+h);
                GL11.glVertex2f(x, y+h);
                GL11.glEnd();
            } else {
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());

                GL11.glBegin(GL11.GL_QUADS);

                GL11.glTexCoord2f(0,0);
                GL11.glVertex2f(x, y);

                GL11.glTexCoord2f(wr,0);
                GL11.glVertex2f(x+w, y);

                GL11.glTexCoord2f(wr,hr);
                GL11.glVertex2f(x+w, y+h);

                GL11.glTexCoord2f(0,hr);
                GL11.glVertex2f(x, y+h);

                GL11.glEnd();

                // unbind the sprite so that other objects can be drawn
                GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

            }
        }
    }
    
    public void drawAt(float x, float y, float dx, float dy) {
        
    }
    
    // override this if you want to be able to see if your entity interacts
    // with another rectangle
    public boolean intersects(Entity other) {
        return hitbox.intersects(other.hitbox);
    }
    
    
    
    
    
    public Rectangle intersection(Entity other) {
        Rectangle rval = new Rectangle();
        return hitbox.intersection(other.hitbox, rval);
    }


    public boolean testCollision(Entity other) {
        if (hitbox.intersects(other.hitbox)) {
            onCollision(other);
            return true;
        } else {
            return false;
        }
    }

    public void onCollision(Entity other) {}

    public boolean isActive() {
        return active;
    }
    
    /**
     * Gets the rectangle for this object
     * @return box
     */
    public Rectangle getRectangle () {
        return hitbox;
    }
    
    public E_TYPE getEntityType () {
        return E_TYPE.OTHER;
    }
    
    
    /*******PROTECTED MEMBERS*******?

    /**
     * 
     */
    protected void deactivate() {
        active = false;
    }
    
    protected void translate(float x, float y) {
        hitbox.translate((int) x, (int) y);
    }
    
    protected int getX() {
        return hitbox.getX();
    }
    
    protected int getY() {
        return hitbox.getY();
    }
    
    protected int getWidth() {
        return hitbox.getWidth();
    }
    
    protected int getHeight() {
        return hitbox.getHeight();
    }
    
    protected void setX(int x) {
        hitbox.setX(x);
    }
    
    protected void setY(int y) {
        hitbox.setY(y);
    }
    
    protected void setWidth(int w) {
        hitbox.setWidth(w);
    }
    
    protected void setHeight(int h) {
        hitbox.setHeight(h);
    }

    
}
