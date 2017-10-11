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

import java.util.HashSet;
import java.util.List;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public class Player extends Entity {
    
    private static enum State  { LEVEL, JUMPING, FALLING, WALKING };
    private static enum Anim { WALK_LEFT, WALK_RIGHT, JUMP_LEFT, JUMP_RIGHT,
                               JUMP_CENTER, CROUCH_CENTER, STAND_CENTER,
                               STAND_LEFT, STAND_RIGHT };
    
    private final float spriteSize = 64f;
    private float xSpriteCoord;
    private float ySpriteCoord;
    
    private List<Entity> entities;
    private Rectangle box;
    private Texture tex;
    
    private State state = State.LEVEL;
    private Anim anim = Anim.STAND_CENTER;
    
    private int jumpTime;
    private int jumpTimeTex;
    
    private boolean moving;
    private boolean hasDoubleJump = false;


    public Player(float spriteSize) {
        xSpriteCoord = 0;
        ySpriteCoord = 2;
        //this.entities = entities;
        

        try {
            tex =
                TextureLoader.getTexture("PNG",
                                         ResourceLoader.getResourceAsStream("res/heck.png"));
            
            
            box =  new Rectangle(   
                                    0,
                                    0,
                                    (int)spriteSize, 
                                    (int)spriteSize
                                );
            
            
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void draw() {
        
        // Update the x coord each draw... might not want to have this here.
        //xSpriteCoord++;  

        float x=(float)box.getX();
        float y=(float)box.getY();
        float w=(float)box.getWidth();
        float h=(float)box.getHeight();

        // draw this rectangle using the loaded sprite
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());
        GL11.glColor3f(1, 1,1);
        
        // Calculates animation. xSpriteCoord and ySpriteCoord are the x & y value of
        // particular sprite animation segment I want to use. since the sprite 
        // sheet I have picked is a 64x64 texture, I multiple the coord by 64
        // to get the actual texture x and y coord needed. I can then add 64 to
        // that to get the width or height. I then divide all that by the 
        // texture width or height just like with wr and hr previously.

        GL11.glBegin(GL11.GL_QUADS);

        if (moving) {
            GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/tex.getTextureWidth() , (ySpriteCoord * spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/tex.getTextureWidth(), (ySpriteCoord * spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x+w, y);

            GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/tex.getTextureWidth(), (ySpriteCoord*spriteSize + spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x+w, y+h);

            GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/tex.getTextureWidth() , (ySpriteCoord*spriteSize + spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x, y+h);
        } else {
            xSpriteCoord = 0;
            
            GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/tex.getTextureWidth() , (ySpriteCoord * spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/tex.getTextureWidth(), (ySpriteCoord * spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x+w, y);

            GL11.glTexCoord2f( (xSpriteCoord*spriteSize + spriteSize)/tex.getTextureWidth(), (ySpriteCoord*spriteSize + spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x+w, y+h);

            GL11.glTexCoord2f( (xSpriteCoord*spriteSize)/tex.getTextureWidth() , (ySpriteCoord*spriteSize + spriteSize)/tex.getTextureHeight() );
            GL11.glVertex2f(x, y+h);
        }
        
        

        GL11.glEnd();

        // unbind the sprite so that other objects can be drawn
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

    }
    
    @Override
    public void update(float delta) {
        
        
        Entity intersect = null;
        
        // check for first intersection 
        // (may check for more later)
        for (Entity e : entities) {
            if (e.intersects(box)){
                intersect = e;
                break;
            }
        }
        
        if (state == State.LEVEL) {
            
            xSpriteCoord++;  
            
            
            
            if (xSpriteCoord >= 9 && moving)
                xSpriteCoord = 0;
            if (!moving)
                xSpriteCoord = 0;
            
            // System.out.println("Player is Level.");
            // check if intersect with ground
            if ( intersect != null ) {
                
                // check what object collided with
                switch (intersect.getEntityType()) {
                    case GROUND:
                        // Player is safe on top of rectangle
                        this.hasDoubleJump = true;
                        box.setY( ((Ground)intersect).getY() );                       
                        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                            anim = Anim.WALK_LEFT;
                        } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                            anim = Anim.WALK_RIGHT;
                        } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                            anim = Anim.CROUCH_CENTER;
                        } else {
                            anim = Anim.STAND_CENTER;
                        }




                        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && 
                                (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                                    !Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                            moving = false;
                            jumpTime = 14;
                            jumpTimeTex = jumpTime/7;
                            state = State.JUMPING;
                        }

                        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&
                                (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                                    Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                            moving = true;
                            jumpTime = 14;
                            jumpTimeTex = jumpTime/7;
                            state = State.JUMPING;
                        }  
                        break;
                    case NPC:
                        // TODO: implement baddies
                        System.out.println("Heck, you hit an NPC.");
                        break;
                    default:
                        System.out.println("Heck, you hit something.");
                        break;
                }
                
            } else {
                state = State.FALLING;
            }
            
        }
        
        if (state == State.JUMPING) {
            
            // Check direction for animations;
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                anim = Anim.JUMP_LEFT;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                anim = Anim.JUMP_RIGHT;
            } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                anim = Anim.CROUCH_CENTER;
            } else { // Neither left or right is held down.
                anim = Anim.JUMP_CENTER;
            }
            
            // Just jumping
            if (jumpTime <= 0) {
                state = State.FALLING;
            }
            jumpTime--;
            
            // Attempt to change animation frame smoothly.
            if((jumpTimeTex > 0) && (jumpTime%jumpTimeTex == 0)) {
                
                xSpriteCoord++;
                if (xSpriteCoord >= 7)
                    xSpriteCoord =0;
            }
            if (moving) {
                for (double i=0; i < 0.50; i=i+0.1) {
                    box.translate(0, (int)(-i*delta));
                }

            } else {
                box.translate(0, (int)(-0.50*delta));
            }
            
        }
        
        if (state == State.FALLING) {
            
            // check if intersect on falling
            if ((intersect != null) && intersect.intersects(box) ) {
                
                // check what object collided with
                switch (intersect.getEntityType()) {
                    case GROUND:
                        System.out.println("Player collided with Ground");
                        box.setY( ((Ground)intersect).getY() ); 
                        state = State.LEVEL;
                        break;
                    case NPC:
                        // TODO: implement baddies
                        System.out.println("Heck, you hit an NPC.");
                        break;
                    default:
                        System.out.println("Heck, you hit something.");
                        break;
                }
                
            } else { // If not intersecting, fall regularly.
            

                // check for direction for animations
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))  {
                    anim = Anim.JUMP_LEFT;
                } else  if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    anim = Anim.JUMP_RIGHT;
                } else if ((Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) 
                        || !(Keyboard.isKeyDown(Keyboard.KEY_LEFT) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT))) {
                    anim = Anim.JUMP_CENTER;
                } else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
                    anim = Anim.CROUCH_CENTER;
                } else {
                    anim = Anim.STAND_CENTER;
                }
                
                // Check to see if player has a double jump still
                if (hasDoubleJump) {
                    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && 
                            (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                                !Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                        moving = false;
                        jumpTime = 10;
                        state = State.JUMPING;
                        this.hasDoubleJump = false;
                    }

                    if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&
                            (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                                Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                        moving = true;
                        jumpTime = 10;
                        state = State.JUMPING;
                        this.hasDoubleJump = false;
                    }
                    
                    
                }
                box.translate(0, (int)(.5 * delta));
                
                // check if player falls off the level
                if (box.getY()  > Display.getHeight() *2) {

                    
                    //TODO: decrement lives. 
                    System.out.println("You done goofed now. I just reset your position.");
                    jumpTime = 70;
                    state = State.JUMPING;
                    
                    
                }
            }
        }
        
        switch (anim) {
            case JUMP_LEFT:
                moving = true;
                ySpriteCoord = 1;
                break;
            case JUMP_CENTER:
                moving = true;
                ySpriteCoord = 2;
                break;
            case JUMP_RIGHT:
                moving = true;
                ySpriteCoord = 3;
                break;
            case WALK_LEFT:
                moving = true;
                ySpriteCoord = 9;
                break;
            case WALK_RIGHT:
                moving = true;
                ySpriteCoord = 11;
                break;
            case STAND_LEFT:
                moving = false;
                ySpriteCoord = 1;
                break;
            case STAND_CENTER:
                moving = false;
                ySpriteCoord = 2;
                break;
            case STAND_RIGHT:
                moving = false;
                ySpriteCoord = 3;
                break;
            case CROUCH_CENTER:
                ySpriteCoord = 20;
                break;
            default:
                moving = false;
                ySpriteCoord = 2;
                break;
        }
    }
    
    public void setEntities(List entities) {
        this.entities = entities;
    }
    
    // override Entity method since we can answer this question
    @Override
    public boolean intersects(Rectangle other)
    {
        return box.intersects(other);
    }
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.PLAYER;
    }

    /**
     * Gets the x position for this object
     * @return x position
     */
    public int getX () {
        return box.getX();
    }
    
    /**
     * Gets the y position for this object
     * @return y position
     */
    public int getY () {
        return box.getY();
    }
    
    /**
     * Gets the rectangle for this object
     * @return box
     */
    public Rectangle getRectangle () {
        return box;
    }
}
