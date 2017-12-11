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
package World.Entities;

import Main.HistoryStack;
import Main.IntegerOrderedPair;
import Scenes.Overworld;
import World.Statics.Ground;
import World.Entities.Items.Pickups;
import World.WorldObject;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;


/**
 * The player entity. This is the only entity in the game that is controlled by
 * the player. It handles all the animations for a sprite sheet internally.
 * @author Ellie Peterson
 */
public class Player extends InertialEntity {
    
    private static Map<Action, Boolean> ActionMap;
    private static Map< Action , Integer > ControlMap ;
    private static ControlScheme control = ControlScheme.ARROWS;
    private static final Logger LOG = Logger.getGlobal();
    private static final int MAX_REWIND_TIME = 200; // In 10 milliseconds units. How many movements should be stored.
    
    /**
     * Describes the entities that intersect the player at that moment
     */
    private List<WorldObject> entities;
    
    /**
     * Describes the items currently in the player's possession.
     */
    private List<Pickups> items;

    /** 
     * holds a stack of the last movements/actions
     */
    private HistoryStack<RewindState> history;
    private boolean hasRewind = true;
    private RewindState rewindState;
    private int rewindTime;
    
    
    private State state = State.FALLING;
    private Anim anim = Anim.STAND_CENTER;
    
    private final float staticFriction = 0.002f;
    private final float airFriction = 0.0001f;
    
    private float p_mass = 165.137f;
    private float leftforce = -1.3f;
    private float rightforce = 1.3f;
    private float jumpforce = -9.0f;
    private float jumpleftforce = -0.5f;
    private float jumprightforce = 0.5f;
    private float doublejumpforce = 0;
    private float fallingforce = 7.5f;

    private int jumpTime;
    private int itemCount;
    
    private boolean hasDoubleJump = false;


    /** 
     * Default Constructor - 
     * Creates a new PLayable character Entity at (0,0) with a mass of 165.125u.
     * The Sprite is set to a default sprite (as there is only one PC).
     * The one variable that can change is the spriteSize which describes
     * the size of the sprite being used. Since using a specific spriteSheet,
     * we set the size of the sprite here.
     */
    public Player() {
        super("sprites/heck.png", 64f, 0, 0, 165.125f);
        setUpMaps();
        entities = new LinkedList<>();
        items = new LinkedList<>();
        itemCount = 0;
        setX(getX()+100);
        setY(getY()+(Display.getHeight()-10));
        xSpriteCoord = 0;
        ySpriteCoord = 2;

        history = new HistoryStack<>(MAX_REWIND_TIME);
        
        Keyboard.enableRepeatEvents(true);
    }
    
    public Player(Player p) {
        super("sprites/heck.png", 64f, 0, 0, 165.125f);
        setUpMaps();
    }

    public Player(int x) {
        super("sprites/heck.png", 64f, x, x, 0f);
        xSpriteCoord = 0;
        ySpriteCoord = 2;
        
    }
    
    /**
     * Complete rewrite of update to include better key press handling.
     * Should also revise the state machine logic
     * @param delta delta to update with
     */
    @Override
    public void update(float delta) {

        // if (getX() >= 1000000000 || getX() <= 1000000000) {
        //     LOG.log(Level.SEVERE, "Integer Overflow Detected.");
        // }
        LOG.log(Level.FINER, "Player momentum on update: " + momentumForce().toString());

        
        WorldObject intersect = null;
        
        // check for first intersection 
        // (may check for more later)
        for (WorldObject e : entities) {
            if (e.intersects(this)){
                intersect = e;
                LOG.log(Level.FINER, 
                        "Player intersected with an Entity.");
                break;
            }
        }


        //<editor-fold defaultstate="collapsed" desc="keypress">
        // Determines the keyboard input to use. Sets the Action to true 
        // if the key is used, and false if not used.
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
                    case Keyboard.KEY_R:
                        ActionMap.put(Action.REWIND, true);
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
                    case Keyboard.KEY_R:
                        ActionMap.put(Action.REWIND, false);
                        break;
                }
            }
        }
        //</editor-fold>
        
        //<editor-fold defaultstate="collapsed" desc="LEVEL STATE">
        /*
            Describes actions to take if the Player state is level. When level 
        we check that the player is intersecting a ground tile, and if not we 
        put the player into the falling state. When intersecting ground, we 
        use the ActionMap above to determine if wetake an action. left we add 
        force to move left, and right we add force to move right. If not 
        moving left or right, we set all the vectors to zero. Idealliy we 
        would set the friction in the opposite direction to the current 
        velocity vector. On the Crouch action (outside of the left and right 
        actions), we simply show the crouch anim. We only show the first frame 
        as it looks silly otherwise. Beyond that, if the player jumps, then we 
        set the state to jumping and go from there. \n 
        Each iteration through we increase the xSpriteCoord to give the 
        sense of animation."
        */
        
        if (state == State.LEVEL) {
            LOG.log(Level.FINEST, 
                    "Player momentum on level: " + 
                    momentumForce().toString());
            xSpriteCoord++;
            
            
            
            if (xSpriteCoord >= 9)
                xSpriteCoord = 0;
            
            
            // check if intersect on falling
            if ( (intersect != null) && intersect.intersects(this) ) {
                
                // check what object collided with
                switch (intersect.getType()) {
                    case GROUND:
                        hasDoubleJump = true;
                        
                        // move
                        if (ActionMap.get(Action.LEFT)) {
                            anim = Anim.WALK_LEFT;
                            moving = true;
                            addForce(new Vector2f(leftforce,0));
                            LOG.log(Level.FINEST, 
                                    "Player momentum on walk left: " + 
                                    momentumForce().toString());
                        } else if (ActionMap.get(Action.RIGHT)) {
                            moving = true;
                            anim = Anim.WALK_RIGHT;
                            addForce(new Vector2f(rightforce,0));
                            LOG.log(Level.FINEST, 
                                    "Player momentum on walk right: " + 
                                    momentumForce().toString());
                        } else if (ActionMap.get(Action.REWIND) && hasRewind) {
                            // rewindState = new RewindState(this.velocity, this.getX(), this.getY(), state, jumpTime, hasDoubleJump);
                            state = State.REWINDING;
                            hasRewind = false;
                            moving = true;
                            anim = Anim.JUMP_CENTER;
                            break;
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
                            jumpTime = 5;
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
                state = State.FALLING;
            }
        }
//</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="JUMPING STATE">
        /*
            When jumping, we do not care about what we intersect with (for 
        now). We simply check the direction the player is trying to go (via the
        ActionMap) and give a small force in that direction. We also set the
        appropriate animation to use. Along with that, we can allow the player
        to crouch while jumping. this does freeze the animation right now, 
        but I think it makes sense, tbh. We increment the animation 
        ySpriteCoord to give the player movement and then add an upward 
        force in the y direction. We use a jumpTime decrementor to know when to
        put the player into the falling state Because of some wonkiness, I 
        force the player into falling if they are too high.
        */
        if (state == State.JUMPING) {
            LOG.log(Level.FINEST, 
                    "Player momentum on jump: " + 
                    momentumForce().toString());

            if (ActionMap.get(Action.LEFT)) {
                anim = Anim.JUMP_LEFT;
                moving = true;
                addForce(new Vector2f(jumpleftforce,0));
                LOG.log(Level.FINEST, 
                        "Player momentum on jump left: " + 
                        momentumForce().toString());
            } else if (ActionMap.get(Action.RIGHT)) {
                anim = Anim.JUMP_RIGHT;
                moving = true;
                addForce(new Vector2f(jumprightforce,0));
                LOG.log(Level.FINEST, 
                        "Player momentum on jump right: " + 
                        momentumForce().toString());
            } else if (ActionMap.get(Action.REWIND) && hasRewind) {
                // rewindState = new RewindState(this.velocity, this.getX(), this.getY(), state, jumpTime, hasDoubleJump);
                state = State.REWINDING;
                hasRewind = false;
                moving = true;
                anim = Anim.JUMP_CENTER;
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

            addForce(0,jumpforce);
            jumpTime--;

            // check the current force's y vector.
            // if it is greater than 0, then the player is no longer falling
            if ((momentumForce().getY() > 0) || (jumpTime == 0)) {
                LOG.log(Level.FINER, 
                        "Player reached top of jump arch.");
                state = State.FALLING;
            }
            
            if (getX() < -100) {
                // Force into fall, too high
                LOG.log(Level.FINER, 
                        "Player is too high! Forcing into fall!");
                velocity.set(0, 0);
                accel.set(0, 0);
                curr_force.set(0, 0);
                state = State.FALLING;
            }

            
        }
        //</editor-fold>

        //<editor-fold defaultstate="collapsed" desc="FALLING STATE">
        /*
            In the fallign state, we add a force depending on the action 
        that the player is taking like during the other states. Left adds a 
        force in the -x direction, and right in the +x direction. If the player
        intersects with a Ground entity whilst falling, then we set the 
        vectors to be zero. Code may be added to determine if player life 
        should be accounted for or not w.r.t. fall velocity, etc. We use the 
        same animation as jumping to animate falling, so that is the same as
        above. The three biggest things are the double jump, adding a downward
        force and checking if the player falls off the level.
        */
        if (state == State.FALLING) {
            LOG.log(Level.FINEST, 
                    "Player momentum on fall: " + 
                    momentumForce().toString());

            // check if intersect on falling
            if ((intersect != null) && intersect.intersects(this) ) {

                // check what object collided with
                switch (intersect.getType()) {
                    case GROUND:
                        LOG.log(Level.FINER, "Player Collided with Ground.");
                        moving = false;
                        LOG.log(Level.FINEST, 
                                "Player momentum on collison with Ground: " + 
                                momentumForce().toString());
                        velocity.set(0, 0);
                        accel.set(0,0);
                        curr_force.set(0, 0);
                        super.setY( intersect.getY()-63 );
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
                    addForce(new Vector2f(jumpleftforce,0));
                    LOG.log(Level.FINEST, 
                            "Player momentum on falling to the left: " + 
                            momentumForce().toString());
                } else if (ActionMap.get(Action.RIGHT)) {
                    anim = Anim.JUMP_RIGHT;
                    moving = true;
                    addForce(new Vector2f(jumprightforce,0));
                    LOG.log(Level.FINEST, 
                            "Player momentum on falling to the right: " + 
                            momentumForce().toString());
                } else if (ActionMap.get(Action.REWIND) && hasRewind) {
                    // rewindState = new RewindState(this.velocity, this.getX(), this.getY(), state, jumpTime, hasDoubleJump);
                    state = State.REWINDING;
                    hasRewind = false;
                    moving = true;
                    anim = Anim.JUMP_CENTER;
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
                        LOG.log(Level.FINEST, 
                                "Player using their second jump");

                        LOG.log(Level.FINEST, 
                                "Player momentum on second jump: " + 
                                momentumForce().toString());
                        jumpTime = 4;
                        state = State.JUMPING;
                        this.hasDoubleJump = false;
                    }
                }
                
                addForce(0,fallingforce);
                addGravity();

                // check if player falls off the level
                if (super.getY()  > Display.getHeight()*2) {
                    LOG.log(Level.FINEST, "Player momentum on death: " + 
                            momentumForce().toString());
                    velocity.set(0, 0);
                    accel.set(0, 0);
                    curr_force.set(0,0);
                    super.setY(0);
                    state = State.FALLING;
                }
            }

        }
        //</editor-fold>
        
        if (state != state.REWINDING) {
            history.push(new RewindState(this.velocity, this.getX(), this.getY(), state, jumpTime, hasDoubleJump));
        }
        
        //<editor-fold defaultstate="collapsed" desc="REWINDING STATE">
        /*
            This is the rewinding state. This state can be used to get out of a "sticky"
            situation or to simply get a redo. 
         */
        if (state == State.REWINDING) {
            LOG.log(Level.FINE, "HistorySize on rewind: " + history.size());
            RewindState action = history.pop();
            if (history.size() > 0) {
                hitbox.setLocation(action.player_x, action.player_y);
            } else {
                state = action.state;
                hasDoubleJump = action.prev_hasDoubleJump;
                velocity = action.velocity;
                jumpTime = action.prev_jumpTime;
                hasRewind = true;
            }
            return;
        }
        //</editor-fold>

        setAnim(anim);
        super.update(delta);

    }

    public void updateMap(float delta) {
        
            
        curr_force.set(0, 0);
        accel.set(0,0);
        velocity.set(0,0);
        
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState()) {
                switch (Keyboard.getEventKey()) {
                    case Keyboard.KEY_RIGHT:
                        if (Overworld.comp[0] ) {
                            moving = true;
                            anim = Anim.WALK_RIGHT;
                            // translate right once
                            hitbox.translate(10, 7);
                            if (hitbox.getX() >= 375 && hitbox.getY() >= 275) {
                                hitbox.setX(375);
                                hitbox.setY(275);
                            }
                        } else ;
                        if (Overworld.comp[1]) {
                            moving = true;
                            anim = Anim.WALK_RIGHT;
                            // translate up to last.
                            hitbox.translate(10, 7);
                            if (hitbox.getX() >= 675 && hitbox.getY() >= 475) {
                                hitbox.setX(675);
                                hitbox.setY(475);
                            }
                        }   
                        if (Overworld.comp[2]) {
                            // game complete.
                        }   
                        break;
                    case Keyboard.KEY_LEFT:
                        if (Overworld.comp[0] || Overworld.comp[1]) {
                            moving = true;
                            anim = Anim.WALK_LEFT;
                            // translate right once
                            hitbox.translate(-10, -7);
                        } else ;
                       
                        if (hitbox.getX() <= 75 && hitbox.getY() <= 75) {
                            hitbox.setX(75);
                            hitbox.setY(75);
                        }   
                        break;
                    default:
                        xSpriteCoord = 0;
                        moving = false;
                        anim = Anim.IDLE_CENTER;
                        break;
                }
            } 
            
            if (!Keyboard.getEventKeyState()) {
                xSpriteCoord = 0;
                moving = false;
                anim = Anim.IDLE_CENTER;
            }
        }
        
        if (anim == Anim.WALK_LEFT || anim == Anim.WALK_RIGHT) {
            xSpriteCoord++;
            if (xSpriteCoord >= 9)
                xSpriteCoord = 0;
        }
        
        curr_force.set(0, 0);
        accel.set(0,0);
        velocity.set(0,0);
        setAnim(anim);
    }
    
    @Override
    public OBJECT_TYPE getType() {
        return OBJECT_TYPE.PLAYER;
    }
    
    /**
     * Adds an item to the player's inventory
     * @param item the item to add
     */
    public void addItem(Pickups item) {
        itemCount++;
        this.items.add(item);
        LOG.log(Level.FINE, "Adding Item to Inventory");
    }
    
    /**
     * Adds a ground entity which the player collides with.
     * @param object Intersecting ground tile.
     */
    public void intersectObject(WorldObject object) {
        this.entities.add(object);
        
    }
    
    /**
     * Gets the current count of items in the player's inventory.
     * @return itemCount.
     */
    public int getItemCount() {
        return this.itemCount;
    }

    /**
     * Sets the animation to use. The animation is based off the ySpriteCoord,
     * so this method selects that.
     * @param a the Anim enum to use.
     */
    @Override
    protected void setAnim(Anim a) {
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
    
    /**
     * Sets up the ActionMap and ControlMap. this method may be brought out
     * of the player class and put into it's own class that will handle all
     * input for the entire game. For sake of simplicity, we assume that
     * only arrow keys (or space) will be used. We also set all the actions
     * to false and will change in the update method.
     */
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
        
        ControlMap.put(Player.Action.LEFT, left);
        ControlMap.put(Action.RIGHT, right);
        ControlMap.put(Action.JUMP, up);
        ControlMap.put(Action.CROUCH, down);
        ControlMap.put(Action.JUMP, Keyboard.KEY_SPACE);
        
        ActionMap.put(Action.LEFT, false);
        ActionMap.put(Action.RIGHT, false);
        ActionMap.put(Action.JUMP, false);
        ActionMap.put(Action.CROUCH, false);
        ActionMap.put(Action.REWIND, false);
    }
    
    /**
     * Player interaction state.
     */
    private static enum State  { LEVEL, JUMPING, FALLING, WALKING, REWINDING }
    
    /**
     * Describes the action which a player character can take. Like state, 
     * but used to associate controls with an action
     */
    private static enum Action { LEFT, RIGHT, JUMP, CROUCH, REWIND }
    
    /**
     * Describes the control scheme to use. Ideally this would exist in the 
     * top most layer and be set by the player for each input character.
     * Currently we only use ARROWS, but future implementations with allow 
     * for player choice.
     */
    private static enum ControlScheme { WASD, ESDF, ARROWS, NUMPAD }

    private static class RewindState {
        private int curr_element = 0;
        public Vector2f velocity;
        public int player_x;
        public int player_y;
        public State state;
        private int prev_jumpTime;
        private boolean prev_hasDoubleJump = false;

        public RewindState() {}

        public RewindState(Vector2f v, int x, int y, State s, int jumpTime, boolean hasDoubleJump) {
            velocity = v;
            player_x = x;
            player_y = y;
            state = s;
            prev_jumpTime = jumpTime;
            prev_hasDoubleJump = hasDoubleJump;
            curr_element = 6;
        }

        public Object[] popState() {
            return new Object[]{velocity, player_x, player_y, state, prev_jumpTime, prev_hasDoubleJump};
        }
    }
}


