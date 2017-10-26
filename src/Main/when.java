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

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A helper class to describe what an Entity is supposed to do. Ideally will
 * serve as the start of a testing framework. 
 * @author Ellie
 */
public class when {
    private String actual;
    private final String expected;
    private int step;
    private boolean didStep1;
    private static final Logger LOG = Logger.getLogger(when.class.getName());
    
    /**
     * Constructor that creates an object that describes an event.
     * @param somethingHappens The cause
     * @param expectedResult The expected result
     */
    public when(String somethingHappens, String expectedResult) {
        expected = expectedResult;
        LOG.log(Level.INFO, 
                "The event {0} took place.", 
                somethingHappens);
        step = 0;
    }

    /**
     * A response to take to an event
     * @param action The action to perform.
     */
    public void doThis(String action) {
        LOG.log(Level.INFO, 
                "For the event, we do step {0} in which we {1}", 
                new Object[]{step, action});
        didStep1 = true;
    }
    
    /**
     * Any subsequent responses to an event.
     * @param action The action to perform
     * @throws java.lang.Exception When step one is not done.
     */
    public void thenThis(String action) throws Exception {
        if (didStep1) {
            step++;
            doThis(action);
        } else {
            throw new Exception("Step one was never completed.");
        }
    }
    
    /**
     * Describes the end point of the event.
     * @param actualResult The actual result of the actions.
     */
    public void endAt(String actualResult) {
        actual = actualResult;
        LOG.log(Level.INFO, 
                "For the event, after step {0} we are {1}", 
                new Object[]{step, actualResult});
    }
    
    /**
     * determines if we followed the correct steps.
     * @return true if the response is good.
     */
    public boolean correctResponse() {
        return expected.equals(actual);
    }
    
    /**
     * Describes actions that should be taken to correct for bad responses.
     * @param appropriateHandling The appropriate handling to use
     */
    public void wrongResponse(String appropriateHandling) {
        LOG.log(Level.INFO, 
                "For the event, after step {0} we need to {1}", 
                new Object[]{step, appropriateHandling});
    }
}