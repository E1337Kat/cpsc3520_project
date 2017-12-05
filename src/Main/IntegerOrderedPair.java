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
package Main;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Ellie
 * @param <L>
 * @param <R>
 */
public class IntegerOrderedPair {
    
    private int l,r;
    
    /**
     * Creates a new pair
     * @param left
     * @param right 
     */
    public IntegerOrderedPair(int left, int right) {
        l=left;
        r=right;
    }
    
    
    /**
     * gets the left side of the pair
     * @return 
     */
    public int getLeft() {return l;}
    
    /** 
     * gets the right side of the pair
     * @return 
     */
    public int getRight() {return r;}
    
    @Override
    public String toString() {
        return "Pair: (" + l + ", " + r + ")";
    }
}
