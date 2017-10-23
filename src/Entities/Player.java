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

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;

public class Player extends InertialEntity {
    
    private static enum State  { LEVEL, JUMPING, FALLING, WALKING };
    private static enum Anim { WALK_LEFT, WALK_RIGHT, JUMP_LEFT, JUMP_RIGHT,
                               JUMP_CENTER, CROUCH_CENTER, STAND_CENTER,
                               STAND_LEFT, STAND_RIGHT, IDLE_LEFT,
                               IDLE_RIGHT, IDLE_CENTER };
    
    //** For Controls
    private static enum Action { LEFT, RIGHT, JUMP, CROUCH } ;
    private static enum ControlScheme { WASD, ESDF, ARROWS, NUMPAD };
    private static Map<Action, Boolean> ActionMap;
    private static Map< Action , Integer > ControlMap ;
    private static ControlScheme control = ControlScheme.ARROWS;
    
    private List<Entity> entities;
    
    private List<Pickups> items;
    
    
    private State state = State.FALLING;
    private Anim anim = Anim.STAND_CENTER;
    
    private final float staticFriction = 0.002f;
    private final float airFriction = 0.0001f;
    private int jumpTime;
    private int itemCount;
    
    private boolean hasDoubleJump = false;


    public Player(float spriteSize) {
        super(spriteSize, 165.125f, "res/sprites/heck.png", 0, 0);
        setUpMaps();
        entities = new LinkedList<>();
        items = new LinkedList<>();
        itemCount = 0;
        setX(getX()+100);
        setY(getY()+(Display.getHeight()-10));
        xSpriteCoord = 0;
        ySpriteCoord = 2;
        
        Keyboard.enableRepeatEvents(true);
    }
    
    /**
     * Complete rewrite of update to include better key press handling.
     * Should also revise the state machine logic
     * @param delta 
     */
    @Override
    public void update(float delta) {
        
        Entity intersect = null;
        
        // check for first intersection 
        // (may check for more later)
        for (Entity e : entities) {
            if (e.intersects(this)){
                intersect = e;
//                System.out.println("Intersected");
                break;
            }
        }
        
        //<editor-fold defaultstate="collapsed" desc="keypress">
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_LEFT:
                        ActionMap.put(Action.LEFT, true);
                        break;
                    case Keyboard.KEY_RIGHT:
                        ActionMap.put(Action.RIGHT, true);
                        break;
                    case Keyboard.KEY_SPACE:
                        ActionMap.put(Action.JUMP, true);
                        break;
                    case Keyboard.KEY_UP:
                        ActionMap.put(Action.JUMP, true);
                        break;
                    case Keyboard.KEY_DOWN:
                        ActionMap.put(Action.CROUCH, true);
                        break;
                }
            }
            if (!Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_LEFT:
                        ActionMap.put(Action.LEFT, false);
                        break;
                    case Keyboard.KEY_RIGHT:
                        ActionMap.put(Action.RIGHT, false);
                        break;
                    case Keyboard.KEY_SPACE:
                        ActionMap.put(Action.JUMP, false);
                        break;
                    case Keyboard.KEY_UP:
                        ActionMap.put(Action.JUMP, false);
                        break;
                    case Keyboard.KEY_DOWN:
                        ActionMap.put(Action.CROUCH, false);
                        break;
                }
            }
        }
//</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="LEVEL">
        if (state == State.LEVEL) {
//            System.out.println("Player momentum on level: " + momentumForce().toString());
            xSpriteCoord++;
            
            
            
            if (xSpriteCoord >= 9)
                xSpriteCoord = 0;
            
            
            // check if intersect on falling
            if ( (intersect != null) && intersect.intersects(this) ) {
                
                // check what object collided with
                switch (intersect.getEntityType()) {
                    case GROUND:
                        hasDoubleJump = true;
                        
                        // move
                        if (ActionMap.get(Action.LEFT)) {
                            anim = Anim.WALK_LEFT;
                            moving = true;
                            addForce(new Vector2f(-1.5f,0));
//                            System.out.println("Player momentum on walk left: " + momentumForce().toString());
                        } else if (ActionMap.get(Action.RIGHT)) {
                            moving = true;
                            anim = Anim.WALK_RIGHT;
                            addForce(new Vector2f(1.5f,0));
//                            System.out.println("Player momentum on walk left: " + momentumForce().toString());
                        } else {
                            curr_force.set(0,0);
                            accel.set(0,0);
                            velocity.set(0,0);
                            anim = Anim.STAND_CENTER;
                            moving = false;
                        }
                        
                        // crouch
                        if (ActionMap.get(Action.CROUCH)) {
                            anim = Anim.CROUCH_CENTER;
                            moving = false;
                            // TODO translate down
                        }
                        
                        // jump
                        if (ActionMap.get(Action.JUMP)) {
                            jumpTime = 7;
                            state = State.JUMPING;
                        }
                        break;
                    case NPC:
                        // TODO: implement baddies
                        System.out.println("Heck, you hit an NPC.");
                        break;
                    case PICKUP:
                        System.out.println("Ohhh, a shiney!");
                        Rectangle r = intersect.intersection(this);
                        if (    (r.getY() > this.getY()) &&
                                (r.getY()+r.getHeight() > this.getY() + this.getHeight()) &&
                                (r.getX() > this.getX()) &&
                                (r.getX() + r.getWidth() > this.getX() + this.getWidth()) ) {
                            System.out.println("Ohhh, a shiney!");
                            
                        }
                        break;
                    default:
                        System.out.println("Heck, you hit something.");
                        break;
                }
            } else {
                // not intersecting anything, so we fall.
//                System.out.println("Player momentum on fall from level: " + momentumForce().toString());
                state = State.FALLING;
            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="JUMPING">
        if (state == State.JUMPING) {
//            System.out.println("Player momentum on jump: " + momentumForce().toString());

            if (ActionMap.get(Action.LEFT)) {
                anim = Anim.JUMP_LEFT;
                moving = true;
                addForce(new Vector2f(-.5f,0));
//                System.out.println("Player momentum on jump left: " + momentumForce().toString());
            } else if (ActionMap.get(Action.RIGHT)) {
                anim = Anim.JUMP_RIGHT;
                moving = true;
                addForce(new Vector2f(.5f,0));
//                System.out.println("Player momentum on jump right: " + momentumForce().toString());
            } else {
                anim = Anim.JUMP_CENTER;
                moving = true;
            }

            if (ActionMap.get(Action.CROUCH)) {
                anim = Anim.CROUCH_CENTER;
                moving = false;
            }
            
            
            xSpriteCoord++;
            if (xSpriteCoord >= 7)
                xSpriteCoord =0;


//            addGravity();
            addForce(0,-10.0f);
            
            jumpTime--;

            // check the current force's y vector.
            // if it is greater than 0, then the player is no longer falling
            if ((momentumForce().getY() > 0) || (jumpTime == 0)) {
//                System.out.println("Reached top of jump arch.");
                state = State.FALLING;
            }
            
            if (getX() < 0) 
                // Force into fall, too high
                state = State.FALLING;

            
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="FALLING">
        if (state == State.FALLING) {
//            System.out.println("Player momentum on falling: " + momentumForce().toString());

            // check if intersect on falling
            if ((intersect != null) && intersect.intersects(this) ) {

                // check what object collided with
                switch (intersect.getEntityType()) {
                    case GROUND:
//                        System.out.println("Player collided with Ground while falling.");
                        moving = false;
                        velocity.set(0, 0);
                        accel.set(0,0);
                        curr_force.set(0, 0);
                        super.setY( intersect.getY() );
//                        System.out.println("Player momentum after collision: " + momentumForce().toString());
                        state = State.LEVEL;
                        break;
                    case NPC:
                        // TODO: implement baddies
                        System.out.println("Heck, you hit an NPC.");
                        break;
                    case PICKUP:
                        System.out.println("Ohhh, a shiney!");
                        break;
                    default:
                        System.out.println("Heck, you hit something.");
                        break;
                }

            } else { // If not intersecting, fall regularly.

                if (ActionMap.get(Action.LEFT)) {
                    anim = Anim.JUMP_LEFT;
                    moving = true;
                    addForce(new Vector2f(-.5f,0));
//                    System.out.println("Player momentum on jump left: " + momentumForce().toString());
                } else if (ActionMap.get(Action.RIGHT)) {
                    anim = Anim.JUMP_RIGHT;
                    moving = true;
                    addForce(new Vector2f(.5f,0));
//                    System.out.println("Player momentum on jump right: " + momentumForce().toString());
                } else {
                    anim = Anim.JUMP_CENTER;
                    moving = true;
                }

                if (ActionMap.get(Action.CROUCH)) {
                    anim = Anim.CROUCH_CENTER;
                    moving = false;
                }
                
                xSpriteCoord++;
                if (xSpriteCoord >= 7)
                    xSpriteCoord =0;
                // Check to see if player has a double jump still
                if (hasDoubleJump) {
                    if (ActionMap.get(Action.JUMP)) {
//                        System.out.println("Using Double Jump.");
//                        velocity.set(0, 0);
//                        accel.set(0, 0);
//                        curr_force.set(0,0);
//                        System.out.println("Player momentum on double jump start: " + momentumForce().toString());
                        jumpTime = 7;
                        state = State.JUMPING;
                        this.hasDoubleJump = false;
                    }
                }
                
                addForce(0,7.5f);
                addGravity();

                // check if player falls off the level
                if (super.getY()  > Display.getHeight()*2) {
                    velocity.set(0, 0);
                    accel.set(0, 0);
                    curr_force.set(0,0);
                    super.setY(0);
//                    System.out.println("Player momentum on death: " + momentumForce().toString());
                    state = State.FALLING;
                }
            }

        }
        //</editor-fold>

        
        
        setAnim(anim);
        super.update(delta);
    }
    
    
    
    
    
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.PLAYER;
    }
    
    public void addItem(Pickups item) {
        itemCount++;
        this.items.add(item);
    }
    
    public void intersectGround(Ground ground) {
        this.entities.add(ground);
    }

    /**
     * Gets the x position for this object
     * @return x position
     */
    public int getX () {
        return super.getX();
    }
    
    /**
     * Gets the y position for this object
     * @return y position
     */
    public int getY () {
        return super.getY();
    }
    
    public int getItemCount() {
        return this.itemCount;
    }
    
    private void setAnim(Anim a) {
        switch (a) {
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
            case WALK_LEFT:
                moving = true;
                ySpriteCoord = 9;
                break;
            case WALK_RIGHT:
                moving = true;
                ySpriteCoord = 11;
                break;
            case IDLE_LEFT:
                
                moving = true;
                ySpriteCoord = 17;
                break;
            case IDLE_CENTER:
                moving = true;
                ySpriteCoord = 18;
                break;
            case IDLE_RIGHT:
                moving = true;
                ySpriteCoord = 19;
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
    
    private void setUpMaps() {
        int up, down, left, right;
        if (control == ControlScheme.ARROWS) {
            up = Keyboard.KEY_UP;
            down = Keyboard.KEY_DOWN;
            left = Keyboard.KEY_LEFT;
            right = Keyboard.KEY_RIGHT;
        } else {
            up = Keyboard.KEY_UP;
            down = Keyboard.KEY_DOWN;
            left = Keyboard.KEY_LEFT;
            right = Keyboard.KEY_RIGHT;
        }
        ActionMap = new HashMap<>();
        ControlMap = new HashMap<>();
//        
//        ControlMap.put(left, Player.Action.LEFT);
//        ControlMap.put(right, Action.RIGHT);
//        ControlMap.put(up, Action.JUMP);
//        ControlMap.put(down, Action.CROUCH);
//        ControlMap.put(Keyboard.KEY_SPACE, Action.JUMP);
        
        
        ControlMap.put(Player.Action.LEFT, left);
        ControlMap.put(Action.RIGHT, right);
        ControlMap.put(Action.JUMP, up);
        ControlMap.put(Action.CROUCH, down);
        ControlMap.put(Action.JUMP, Keyboard.KEY_SPACE);
        
        ActionMap.put(Action.LEFT, false);
        ActionMap.put(Action.RIGHT, false);
        ActionMap.put(Action.JUMP, false);
        ActionMap.put(Action.CROUCH, false);
    }
}
