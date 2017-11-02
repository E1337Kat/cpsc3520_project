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

import World.WorldObject;
import java.io.IOException;
import java.util.logging.Logger;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

/**
 * Generally a good class to display static objects, such as backgrounds
 * or maybe even trees/ground. Since these are static objects, there's no need 
 * to have to update the objects. Only draw. :)
 * @author Ellie
 */
public class Statics extends WorldObject {
    private static final Logger LOG = Logger.getLogger(Statics.class.getName());
    
    // Static texture
    protected float wr, hr;
    protected float r=1,g=1,b=1;
    private boolean active = true;
    protected float spriteSize;
    
    //********Constructors********//
    
    /**
     * Default constructor creates a new hitbox.
     */
    public Statics() {
        hitbox = new Rectangle(); // empty rectangle
    }
    
    /**
     * Creates a new hitbox given the supplied dimensions.
     * @param x X coordinate to place at.
     * @param y Y coordinate to place at.
     * @param width Width of the hitbox.
     * @param height Height of the hitbox.
     */
    public Statics(int x, int y, int width, int height) {
        hitbox = new Rectangle(x,y,width,height); // non-empty rectangle
    }
    
    /**
     * Creates an entity with a proportionally correct entity with some
     * sprite texture
     * @param texturePath path to the texture to use.
     * @param width Width of the hitbox.
     * @param x X coordinate of the Entity.
     * @param y Y coordinate of the Entity.
     */
    public Statics(String texturePath, int width, int x, int y) {
        loadTexture(texturePath);
        hitbox = new Rectangle(x, y,
                               width,
                               (int)(width * texture.getImageHeight()* 1.0f/texture.getImageWidth()));
    }
    
    /**
     * Generates an entity given the supplied parameters and applies a texture.
     * @param texturePath path to the texture to apply
     * @param width Width of the Entity to create
     * @param height Height of the Entity to make
     * @param x X coordinate of the Entity.
     * @param y Y coordinate of the Entity.
     */
    public Statics(String texturePath, int width, int height, int x, int y) {
        loadTexture(texturePath);
        hitbox = new Rectangle(x, y,
                               width,
                               height);
    }

    //*******PUBLIC MEMBERS********//

    /**
     * loads a texture into the game. Handles both sprite sheets and static
     * textures.
     * @param path The path to the texture to load.
     */
    public final void loadTexture(String path) {
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
    
    public final void loadTiledTexture(String path) {
        try {
            texture = TextureLoader.getTexture("PNG", 
                                             ResourceLoader.getResourceAsStream(path));
        } catch (IOException e) {
            throw new RuntimeException("failded to load texture.");
        }
    }

    /**
     * Sets the RGB value of the hitbox. 
     * @param r The red value to use.
     * @param g The green value to use.
     * @param b The blue value to use.
     */
    public void setRGB(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }
    
    /**
     * Initializes the object.
     */
    public void init() {
    }

    /**
     * Destroys the object.
     */
    public void destroy() {
    }

    /**
     * Draws the Entity to the screen. Takes into account whether the Entity
     * is animated or not.
     */
    public void draw() {
        
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
    
    public void drawTiled(float origin, float width, float height) {
        float x=(float)hitbox.getX();
        float y=(float)hitbox.getY();
        float w=(float)hitbox.getWidth();
        float h=(float)hitbox.getHeight();

        // draw this rectangle using the loaded sprite
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
        GL11.glColor3f(1, 1,1);

        GL11.glBegin(GL11.GL_QUADS);

        
            GL11.glTexCoord2f( (origin*spriteSize)/texture.getTextureWidth() , (origin * spriteSize)/texture.getTextureHeight() );
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f( (origin*spriteSize + width*spriteSize)/texture.getTextureWidth(), (origin * spriteSize)/texture.getTextureHeight() );
            GL11.glVertex2f(x+w, y);

            GL11.glTexCoord2f( (origin*spriteSize + width*spriteSize)/texture.getTextureWidth(), (origin*spriteSize + height*spriteSize)/texture.getTextureHeight() );
            GL11.glVertex2f(x+w, y+h);

            GL11.glTexCoord2f( (origin*spriteSize)/texture.getTextureWidth() , (origin*spriteSize + height*spriteSize)/texture.getTextureHeight() );
            GL11.glVertex2f(x, y+h);
        
        GL11.glEnd();

        // unbind the sprite so that other objects can be drawn
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    }
    
    /**
     * Draws the Entity at the specified coordinates. Must be Overriden to use.
     * @param x The x coord to draw the Entity at.
     * @param y The y coord to draw the Entity at.
     * @param dx The dx coord to draw the Entity with.
     * @param dy The dy coord to draw the Entity with.
     */
    public void drawAt(float x, float y, float dx, float dy) {
        
    }
    
    /**
     * Checks if this Entity intersects with another entity. Override if you 
     * want to implement further code.
     * @param other The other Entity to check against.
     * @return true if the entities are intersecting.
     */
    @Override
    public boolean intersects(WorldObject other) {
        return hitbox.intersects(other.hitbox);
    }
    
    /**
     * Gets the intersection between two onjects.
     * @param other The other Entity to get the intersection with.
     * @return The rectangle representing the intersection.
     */
    @Override
    public Rectangle intersection(WorldObject other) {
        Rectangle rval = new Rectangle();
        return hitbox.intersection(other.hitbox, rval);
    }

//    /**
//     * Tests the collision between objects
//     * @param other The other entity to test against.
//     * @return true if collided and calls onCollision(other). false otherwise.
//     */
//    public boolean testCollision(WorldObject other) {
//        if (hitbox.intersects(other.hitbox)) {
//            onCollision(other);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    /**
//     * Describes an action to take on collision of Entities. Should be 
//     * overriden for best results.
//     * @param other The other entity that was collided with.
//     */
//    public void onCollision(Entity other) {}

//    /**
//     * Returns if the Entity is active
//     * @return active.
//     */
//    public boolean isActive() {
//        return active;
//    }
//    
//    /**
//     * Gets the Entities x coordinate.
//     * @return The Entity's x coordinate.
//     */
//    public final int getX() {
//        return hitbox.getX();
//    }
//    
//    /**
//     * Gets the Entities y coodinate.
//     * @return The Entity's y coordinate.
//     */
//    public final int getY() {
//        return hitbox.getY();
//    }
//    
//    /**
//     * Gets the Entities width.
//     * @return The Entity's width.
//     */
//    public final int getWidth() {
//        return hitbox.getWidth();
//    }
//    
//    /**
//     * Gets the Entities height.
//     * @return The Entity's height.
//     */
//    public final int getHeight() {
//        return hitbox.getHeight();
//    }
    
    //*******PROTECTED MEMBERS*******//

    /**
     * Deactivates the Entity.
     */
    protected void deactivate() {
        active = false;
    }
    
    /**
     * Translate the entity to a given position
     * @param x X coord to translate to.
     * @param y Y coord to translate to.
     */
    protected void translate(float x, float y) {
        hitbox.translate((int) x, (int) y);
    }
    
    /**
     * Sets the X coord of an Entity.
     * @param x X coord to set to.
     */
    protected void setX(int x) {
        hitbox.setX(x);
    }
    
    /**
     * Sets the Y coord of an Entity.
     * @param y The Y coord to set to.
     */
    protected void setY(int y) {
        hitbox.setY(y);
    }
    
    /**
     * Sets the width of an Entity.
     * @param width The width to set to.
     */
    protected void setWidth(int width) {
        hitbox.setWidth(width);
    }
    
    /**
     * Sets the height of an Entity.
     * @param height The height to set to. 
     */
    protected void setHeight(int height) {
        hitbox.setHeight(height);
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
    protected static enum E_TYPE { BACKGROUND,
    GROUND,
    OTHER
    };
}
