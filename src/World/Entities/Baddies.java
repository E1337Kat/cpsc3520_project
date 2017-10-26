package World.Entities;

import World.Entities.Entity;
import java.util.logging.Logger;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;

class Baddies extends InertialEntity {
    private static final Logger LOG = Logger.getLogger(Baddies.class.getName());

    private Rectangle box;
    private State state;
    private float speed;        // pixels / ms

    public Baddies(float width)
    {
        super("res/duck.png", (int)width, 1.0f);
        
        state = State.START;
        this.speed = speed;
    }

    @Override
    public void update(float delta)
    {
        switch (state)
        {
         case START:
             state = State.RIGHT;

         case RIGHT:

             box.translate((int)(speed*delta), 0);

             if (box.getX() + box.getWidth() >= Display.getWidth())
             {
                 state = State.LEFT;
             }

             break;

         case LEFT:
             box.translate((int)(-speed*delta), 0);
             if (box.getX() <= 0)
             {
                 state = State.RIGHT;
             }
             break;             
        }
        
    }
    
    @Override 
    public boolean intersects(Entity other) {
        return true;
    }

    @Override
    public Entity.OBJECT_TYPE getType() {
        return OBJECT_TYPE.NPC;
    }
    private static enum State  { START, LEFT, RIGHT }
    
    
}
