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

import org.lwjgl.util.vector.Vector2f;


public abstract class InertialEntity extends Entity
{
    public static final Vector2f GRAVITY=new Vector2f(0f, .001f);

    protected Vector2f velocity;          // pixels/ms
    protected Vector2f lastDelta;
    protected Vector2f accel;
    protected Vector2f curr_force;
    protected float mass;

    public InertialEntity(String pngpath, int width, float mass)
    {
        super(pngpath, width, 0, 0);
        init(mass);
    }
    
    public InertialEntity(int width, int height, float mass)
    {
        super(0, 0, width, height);
        init(mass);
    }
    
    /**
     * Inertial Entity with animations
     * @param spriteSize
     * @param mass
     * @param pngpath 
     */
    public InertialEntity(float spriteSize, float mass, String pngpath) {
        super(spriteSize,0,0, pngpath);
        init(mass);
    }
    
    private void init(float mass)
    {
        velocity = new Vector2f(0,0);
        this.mass=mass;
        this.curr_force = new Vector2f(0,0);        
        this.accel = new Vector2f(0,0);
    }


    public void update(float delta_ms)
    {
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

    // unit vector
    public void addGravity()
    {                                                 
        addForce((Vector2f)new Vector2f(GRAVITY).scale(1f/mass));
    }

    public void addForce(Vector2f force)
    {
        Vector2f.add(curr_force, force, curr_force);
    }

    public Vector2f momentumForce()
    {
        Vector2f f = new Vector2f(accel);
        accel.scale(mass);
        return accel;
    }

}

