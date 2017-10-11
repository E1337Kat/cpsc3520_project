import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

class Baddies extends Entity {
    private static enum State  { START, LEFT, RIGHT };

    private Rectangle box;
    private State state;
    private float speed;        // pixels / ms
    private Texture sprite;

    public Baddies(float width)
    {
        try {
            sprite =
                TextureLoader.getTexture("PNG",
                                         ResourceLoader.getResourceAsStream("res/duck.png"));
            box =  new Rectangle(   
                                    0,
                                    0,
                                    (int)width, 
                                    (int)(width * sprite.getImageHeight() / sprite.getImageWidth())
                                );
            
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        box = new Rectangle(50, Display.getHeight()-50, 50, 50);
        state = State.START;
        this.speed = speed;
    }

    public void draw()
    {
        float x = (float)box.getX();
        float y = (float)box.getY();
        float w = (float)box.getWidth();
        float h = (float)box.getWidth();


        // draw the square
            
        GL11.glColor3f(0,1,0);
        GL11.glBegin(GL11.GL_QUADS);

        GL11.glVertex2f(x, y);
        GL11.glVertex2f(x+w, y);
        GL11.glVertex2f(x+w, y+w);
        GL11.glVertex2f(x, y+w);

        GL11.glEnd();

    }

    @Override
    public void update(float delta, Entity intersect)
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
    public boolean intersects(Rectangle other) {
        return true;
    }

    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.NPC;
    }
    
    
}
