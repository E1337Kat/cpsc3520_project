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
    private float theta;

    public GravPoint(int x, int y) {

    }

    public GravPoint(int x, int y, String spritePath) {

    }

    public GravPoint(int x, int y, float strength) {

    }

    public GravPoint(int x, int y, float strength, String spritePath) {

    }

    /**
     * Finds the theta (angle) to a GravPoint from the given ccoordinates.
     * Note that the angle is flipped as (0,0) is in top left instead of bottom left.
     */
    public float getAngle(int init_x, int init_y){
        theta = (float)Math.atan(init_y-y_coord/init_x-x_coord);
        return theta;
    }

    // public Vector2f getMagnitudeVector() {

    // }

}