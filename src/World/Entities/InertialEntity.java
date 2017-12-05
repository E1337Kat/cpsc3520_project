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

import Main.IntegerOrderedPair;
import World.Entities.Entity;
import org.lwjgl.util.vector.Vector2f;

/**
 * Abstract subclass of the Entity Abstract class. Add vector based motion
 * to the Entity for "realistic" physics.
 * @author Ellie Peterson
 * @author Craig Tanis
 */
public abstract class InertialEntity extends Entity {
    public static final Vector2f GRAVITY=new Vector2f(0f, .001f);

    protected Vector2f velocity;          // pixels/ms
    protected Vector2f lastDelta;
    protected Vector2f accel;
    protected Vector2f curr_force;
    protected float mass;

    /**
     * Creates an Inertial Entity at (0, 0) with the mass and texture to use.
     * @param pngpath Texture to apply to the Entity.
     * @param width Width of the Entity.
     * @param mass Mass of the Entity.
     */
    public InertialEntity(String pngpath, int width, float mass) {
        super(pngpath, width, 0, 0);
        init(mass);
    }
    
    /**
     * Creates an Inertial Entity at (0, 0) with such a mass.
     * @param width Width of the Entity.
     * @param height Height of the Entity.
     * @param mass Mass of the Entity.
     */
    public InertialEntity(int width, int height, float mass) {
        super(0, 0, width, height);
        init(mass);
    }
    
    /**
     * Inertial Entity with animations
     * @param spriteSize The size to create the sprite with.
     * @param mass the mass to use.
     * @param spriteSheetPath the Path to the spritesheet with animations
     */
    public InertialEntity(String spriteSheetPath, float spriteSize, float mass ) {
        super(spriteSheetPath, spriteSize,0,0);
        init(mass);
    }
    
    /** 
     * Creates an Inertial Entity at the specified coordinates
     * @param spriteSheetPath The path to the sprite sheet to use.
     * @param spriteSize The size of the animation on the sprite sheet.
     * @param x X coordinate to place the Entity.
     * @param y Y coordinate to place the Entity.
     * @param mass Mass of the Entity.
     */
    public InertialEntity(String spriteSheetPath, float spriteSize, int x, int y, float mass) {
        super(spriteSheetPath, spriteSize, x, y);
        init(mass);
    }
    
    private void init(float mass) {
        velocity = new Vector2f(0,0);
        this.mass=mass;
        this.curr_force = new Vector2f(0,0);        
        this.accel = new Vector2f(0,0);
    }

    /**
     * updates the Entity to add the vecotr based physics motion. Override this
     * method to add controls then call the supermethod to utilize the 
     * new physics. Example Below:
     * <pre>
     * {@code 
     *      \@Override
     *      public void update(delta) {
     *          // Update how you want
     *          ...
     *          super.update(delta)
     *      }
     * }
     * </pre>
     * @param delta_ms The delta to use in calulations.
     */
    @Override
    public void update(float delta_ms) {
        float dt = delta_ms;

        int x = hitbox.getX();
        int y = hitbox.getY();

        lastDelta = new Vector2f(velocity.getX()*dt, velocity.getY()*dt);

        x += lastDelta.getX();
        y += lastDelta.getY();

        hitbox.setLocation(x,y);

        curr_force.scale(dt / mass);
        accel = new Vector2f(velocity);
        
        Vector2f.add(velocity, curr_force, velocity);
        
        accel.scale(-1);
        Vector2f.add(velocity, accel, accel);
        accel.scale(1.0f / dt);

        // zero out
        curr_force = new Vector2f(0,0);
    }

    /**
     * Adds Gravity to the current force.
     */
    public void addGravity()
    {                                                 
        addForce((Vector2f)new Vector2f(GRAVITY).scale(1f/mass));
    }
    
    // unit vector
    public void addGravityPoint(IntegerOrderedPair point)
    {                
        double gravX, gravY;
        double y = point.getRight()-hitbox.getY();
        double x = point.getLeft()-hitbox.getX();
        
        double theta = Math.atan(y/x);
        
        gravX = Math.cos(theta);
        gravY = Math.sin(theta);
        addForce((Vector2f)new Vector2f(.01f*(float)gravX, .01f*(float)gravY).scale(1f/mass));
    }
    
    /**
     * Calculates and then adds friction to the current velocity vector.
     * @param fric_coeff The friction coefficient to use.
     */
    public void addFriction(float fric_coeff) {
        
        Vector2f f = velocity.normalise(null);
        f.scale(fric_coeff);
        addForce(f);
    }
    
    /**
     * Removes friction from the current velocity... kinda pointless tbh.
     * @param fric_coeff The friction coefficient to use
     */
    public void removeFriction(float fric_coeff) {
        velocity.scale(1/fric_coeff);
        velocity.scale(velocity.length());
    }
    
    /**
     * Adds a force in the supplied directions. This method still uses the 
     * window coord system, so -x is left, +x is right, -y is up, +y is down.
     * @param x X direction magnitude to add to the current force.
     * @param y Y direction magnitude to add to the current force.
     */
    public void addForce(float x, float y) {
        addForce(new Vector2f(x,y));
    }

    /**
     * Add a vector to the current force on an Entity.
     * @param force The force vector to add.
     */
    public void addForce(Vector2f force)
    {
        Vector2f.add(curr_force, force, curr_force);
    }

    /**
     * Gets the current momentum force the Entity is experiencing.
     * @return The momentum force on the Entity.
     */
    public Vector2f momentumForce()
    {
        Vector2f f = new Vector2f(accel);
        f.scale(mass);
        return f;
    }

}

