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

import java.util.LinkedList;

/**
 * This is a specialized stack that can have a set size. When the amount of
 * items in the stack reaches the max, the stack will act like a queue and 
 * remove items from the bottom of the stack. However, the only way to
 * interact with the stack is via standard FIFO idels. 
 */
public class HistoryStack<E> {

    private LinkedList<E> stack;
    private int stack_max = 200;

    /**
     * Default constructor. max size of history is 200 by default.
     */
    public HistoryStack() {
        stack = new LinkedList<>();
    }

    /**
     * This constructor allows setting a custom history size.
     * @param size The desired number actions to save.
     */
    public HistoryStack(int size) {
        stack_max = size;
        stack = new LinkedList<>();
    }

    public void setStackSize(int size) {
        stack_max = size;
    }

    /**
     * Adds an element to the top of the stack. If the stack has reached
     * full capacity, then remove an element from the bottom FIFO style.
     * @param e the object to push.
     */
    public void push(E e) {

        if (stack.size() >= stack_max && !stack.isEmpty()) {
            stack.removeLast();
        }
        
        stack.addFirst(e);
    }

    /**
     * Pops off the most recently added item. LIFO style
     * @return The popped object
     */
    public E pop() {
        return stack.removeFirst();
    }

    public int size() {
        return stack.size();
    }


}