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

import org.lwjgl.util.vector.Vector2f;

/** 
 * This class represents an object with a specific point the pulls other entities toward it.
 * 
 */
public class GravPoint extends Entity {



    private static float grav_strength = 1.0f;
    private int x_coord, y_coord;

    public GravPoint(int x, int y) {
        super("sprites/grav.png", 10, 10, x, y);
    }

    public GravPoint(int x, int y, String spritePath) {
        super(spritePath, 10, 10, x, y);
    }

    public GravPoint(int x, int y, float strength) {
        super("sprites/grav.png", 10, 10, x, y);
        grav_strength = strength;
    }

    public GravPoint(int x, int y, float strength, String spritePath) {
        super(spritePath, 10, 10, x, y);
        grav_strength = strength;
    }

    /**
     * Finds the theta (angle) to a GravPoint from the given ccoordinates.
     * Note that the angle is flipped as (0,0) is in top left instead of bottom left.
     */
    public double getAngle(int init_x, int init_y){
        double theta;
        int x = -Math.abs(init_x-x_coord);
        int y = Math.abs(init_y-y_coord);

        if (x != 0) {
            theta = Math.atan(y/x);
        } else {
            theta = 0;
        }

        return theta;
    }

    public double getDistance(int init_x, int init_y) {
        double distance = Math.sqrt( (init_x+getX())^2 + (init_y+getY())^2 );
        return distance;
    }

    public Vector2f getGravVector(double distance, double theta) {
        // short-circuit eval for theta... if you have no angle, then there is no vector.
        if (theta == 0) {
            return new Vector2f(0, 0);
        }

        double gravWeight = 1/distance;

        double x_vec = gravWeight * Math.cos(theta);
        double y_vec = gravWeight * Math.sin(theta);

        return new Vector2f((float)x_vec, (float)y_vec);
    }

    public Vector2f getGravVector(InertialEntity movable) {
        int x = movable.getX();
        int y = movable.getY();

        double theta = getAngle(x, y);
        // short-circuit eval for theta... if you have no angle, then there is no vector.
        if (theta == 0) {
            return new Vector2f(0, 0);
        }

        double dist = getDistance(x, y);

        double gravWeight = (grav_strength*movable.mass)/(dist*dist) ;

        double x_vec = gravWeight * Math.cos(theta);
        double y_vec = gravWeight * Math.sin(theta);

        return new Vector2f((float)x_vec, (float)y_vec);
    }

}