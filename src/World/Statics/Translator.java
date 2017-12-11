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

import org.lwjgl.opengl.Display;
import World.Entities.Player;

/**
 * This is a special entity that is actually two different entities.
 * It is useful in translating from world coordinates to viewport coordinates.
 * @author Ellie
 */
public class Translator extends Statics{

    private Player player;

    private Translator staticPoint;
    private Translator movablePoint;

    private int x_old, x_new, y_old, y_new;

    private int offset_x;
    private int offset_y;

    /** 
     * Default constructor that creates a 1x1 hitbox to test against.
     * this hitbox could be placed in top corner to help translate 
     * viewport coordinates to world coordinates.
     */
    private Translator() {
        super(0,0,1,1);
    }

    /**
     * creates two points to measure displacement.
     */
    public Translator(Player p) {
        player = p;
        staticPoint = new Translator();
        movablePoint = new Translator();

        x_new = player.getX();
        y_new = player.getY();

        movablePoint.setX(x_new);
        movablePoint.setY(y_new);

        staticPoint.setX(x_new);
        staticPoint.setY(y_new);

        offset_x = 0;
        offset_y = 0;
    }

    /**
     * @return the offset_x
     */
    public int getOffset_x() {
        return offset_x;
    }

    /**
     * @return the offset_y
     */
    public int getOffset_y() {
        return offset_y;
    }

    public void update(float delta) {
        x_new = player.getX();
        y_new = player.getY();

        movablePoint.setX(x_new);
        movablePoint.setY(y_new);

        offset_x = movablePoint.getX() - staticPoint.getX();
        offset_y = movablePoint.getY() - staticPoint.getY();

        x_old = x_new;
        y_old = y_new;
    }

    @Override
    public void draw() {
        movablePoint.drawSingle();
        staticPoint.drawSingle();
    }

    public void drawSingle() {
        super.draw();
    }

}
