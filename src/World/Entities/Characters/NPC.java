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
package World.Entities.Characters;

import World.Entities.InertialEntity;
import java.util.logging.Logger;

/**
 * An abstract subclass that describes a parent class to all NPCs. 
 * @author Ellie Peterson
 */
public abstract class NPC extends InertialEntity {
    
    private static final Logger LOG = Logger.getLogger(NPC.class.getName());
    
    /**
     * Constructs a new NPC with the supplied parameters.
     * @param pngpath The path to the sprite.
     * @param width The width that should be used. Ideally == PC spriteSize.
     * @param mass The mass with which to create the NPC with.
     */
    public NPC(String pngpath, int width, float mass) {
        super(pngpath, width, mass);
    }
    
    /**
     * Default animation for the NPC. Will simply walk back and forth 
     * +-100px from the point at which it was created.
     * @param delta the delta to update over.
     */
    @Override
    public void update(float delta) {
        
    }
    
    /**
     * An abstract method used mostly as a helper method to aid in working out
     * the AI a particular entity should have. An example is shown below. The 
     * parameter can be used however you may want.
     * <pre class="brush: java">
     * {@code
     *     {@literal @}Override
     *      public abstract void describeAI(float whatever) {
     *          when state1 = new when("a cause happens");
     *          state1.doThis(1, "react");
     *          state1.thenThis(2, "react again");
     *          state1.endAt("doing this");
     *          if (state1.correctResponse() ) {
     *              this.state = State.whatever;
     *          } else {
     *              state1.wrongResponse("Appropriate handling");
     *          }
     *          ...
     *          when state2 = new when("a different cause happens");
     *          // Do whatever
     *          ...
     *          if (state1.correctResponse() && state2.correctResponse() && ...) {
     *              return true
     *          }
     *          return false;
     *      }
     * }
     * </pre>
     * @param whatever A float that can be used for any use desired.
     * @param state The state of the current AI.
     * @return True if the AI is implemented properly.
     */
    public abstract boolean describeAI(float whatever, State state);
    
    public static enum State { LEFT, RIGHT };
    
}
