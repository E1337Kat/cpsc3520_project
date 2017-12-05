// /*
//  * Copyright (C) 2017 Ellie
//  *
//  * This program is free software: you can redistribute it and/or modify
//  * it under the terms of the GNU General Public License as published by
//  * the Free Software Foundation, either version 3 of the License, or
//  * (at your option) any later version.
//  *
//  * This program is distributed in the hope that it will be useful,
//  * but WITHOUT ANY WARRANTY; without even the implied warranty of
//  * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  * GNU General Public License for more details.
//  *
//  * You should have received a copy of the GNU General Public License
//  * along with this program.  If not, see <http://www.gnu.org/licenses/>.
//  */
// package World;

// import World.Statics.*;
// import World.Entities.Entity;

// /**
//  * Represents a map of the world. 
//  */
// public class WorldMap {

//     /**
//      * Constant value describing how many pixel are in a row or col. the area of 
//      * a row col is PX_PER_UNIT^2.
//      */
//     private static final int PX_PER_UNIT = 64;
//     private int rows, cols;
//     private boolean[][] movables;
//     private boolean[][] immovables;
//     private Statics[][] statics;
//     private Entity[][] entities;

//     private float[][] grav_influence;


//     public WorldMap(int r, int c){
//         rows = r;
//         cols = c;


//         grav_influence = new float[rows][cols];
//         movables = new boolean[rows][cols];
//         immovables = new boolean[rows][cols];
//     }

//     public void getRowCol(int x, int y) {

//     }

//     /**
//      * adds a movable entity to the map
//      * @param row row number to place at.
//      * @param col col to place at
//      * @return true when added correctly.
//      */
//     public void addChar(int row, int col, Entity e) {
//         if (row < 0 || row >= rows || col < 0 || col >= cols)
//         {
//             throw new RuntimeException("attempt to set entity outside of world");
//         }
//         movables[row][col] = true;
//         entities[row][col] = e;
//     }

//     public void addStatic(int row, int col, Statics e) {
//         if (row < 0 || row >= rows || col < 0 || col >= cols)
//         {
//             throw new RuntimeException("attempt to set entity outside of world");
//         }
//         immovables[row][col] = true;
//         statics[row][col] = e;
//     }

//     public void removeStatic(int row, int col, Statics e) {
//         if (row < 0 || row >= rows || col < 0 || col >= cols)
//         {
//             throw new RuntimeException("attempt to set entity outside of world");
//         }
//         if(statics[row][col] == e)
//         {
//             statics[row][col] = null;
//         }
//     }
// }