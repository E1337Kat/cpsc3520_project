import java.util.HashSet;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.newdawn.slick.util.ResourceLoader;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public class Player extends Entity {
    
    protected static enum State  { LEVEL, JUMPING, FALLING };
    private static enum Direction { LEFT, RIGHT };
    
   
    private Rectangle box;
    private Texture sprite;
    private State state = State.FALLING;
    private Direction dir = Direction.RIGHT;
    private int jumpTime;
    private boolean moving;

    public Player(float width) {

        try {
            sprite =
                TextureLoader.getTexture("PNG",
                                         ResourceLoader.getResourceAsStream("res/duck.png"));
            box =  new Rectangle(   
                                    0,
                                    Display.getHeight(),
                                    (int)width, 
                                    (int)(width * sprite.getImageHeight() / sprite.getImageWidth())
                                );
            
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        
    }

    public void draw() {

        
        float x=(float)box.getX();
        float y=(float)box.getY();
        float w=(float)box.getWidth();
        float h=(float)box.getHeight();

        // draw this rectangle using the loaded sprite
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, sprite.getTextureID());
        GL11.glColor3f(1, 1,1);

        GL11.glBegin(GL11.GL_QUADS);

        if (dir == Direction.RIGHT) {
            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f(1,0);
            GL11.glVertex2f(x+w, y);

            GL11.glTexCoord2f(1,1);
            GL11.glVertex2f(x+w, y+h);

            GL11.glTexCoord2f(0,1);
            GL11.glVertex2f(x, y+h);
        } else {
            GL11.glTexCoord2f(1,0);
            GL11.glVertex2f(x, y);

            GL11.glTexCoord2f(0,0);
            GL11.glVertex2f(x+w, y);

            GL11.glTexCoord2f(0,1);
            GL11.glVertex2f(x+w, y+h);

            GL11.glTexCoord2f(1,1);
            GL11.glVertex2f(x, y+h);
        }

        GL11.glEnd();

        // unbind the sprite so that other objects can be drawn
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);

    }
    
    @Override
    public void update(float delta, Entity intersect) {
        
        if (state == State.LEVEL) {
            
            // check if intersect with ground
            if ( intersect != null ) {
                
                // check what object collided with
                switch (intersect.getEntityType()) {
                    case GROUND:
                        // check if intersects with top of object
                        if ((((Ground)intersect).intersectSide(box) == SIDES.TOP ) || 
                            (((Ground)intersect).intersectSide(box) == SIDES.TOP_LEFT) ||
                            (((Ground)intersect).intersectSide(box) == SIDES.TOP_RIGHT)) 
                        {
                            // Player is safe on top of rectangle
                            box.setY( ((Ground)intersect).getY() );                       
                            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
                                dir = Direction.LEFT;
                            
                            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
                                dir = Direction.RIGHT;

                            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) && 
                                    (!Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                                        !Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                                moving = false;
                                jumpTime = 20;
                                state = State.JUMPING;
                            }

                            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) &&
                                    (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) || 
                                        Keyboard.isKeyDown(Keyboard.KEY_LEFT))) {
                                moving = true;
                                jumpTime = 20;
                                state = State.JUMPING;
                            }
                        } else {
                            state = State.FALLING;                                          // Player fucked up and fell through the cracks
                        }   
                        break;
                    case NPC:
                        // TODO: implement baddies
                        System.out.println("Heck, you hit an NPC.");
                        break;
                    default:
                        System.out.println("Heck, you hit something.");
                        break;
                }
                
            } else {
                state = State.FALLING;
            }
            
        }
        
        if (state == State.JUMPING) {
            
            // check if intersect on jumping
            if (intersect != null ) {
                
                // check what object collided with
                switch (intersect.getEntityType()) {
                    case GROUND:
                        
                        // check if intersects with top of object
                        // if jumping... hardly seems possible
                        if ((((Ground)intersect).intersectSide(box) == SIDES.TOP ) || 
                            (((Ground)intersect).intersectSide(box) == SIDES.TOP_LEFT) ||
                            (((Ground)intersect).intersectSide(box) == SIDES.TOP_RIGHT)) 
                        {
                            // Player is safe on top of rectangle
                            box.setY( ((Ground)intersect).getY() );
                            state = State.LEVEL;
                        } else {
                            // Player fucked up and fell through the cracks
                            state = State.FALLING;
                        }   
                        break;
                    case NPC:
                        // TODO: implement baddies
                        // System.out.println("Heck, you hit an NPC.");
                        break;
                    default:
                        //System.out.println("Heck, you hit something.");
                        break;
                }
            } else { // If not intersecting, jump as usual
                
                // Just jumping
                if (jumpTime <= 0) {
                    state = State.FALLING;
                }
                jumpTime--;
                if (moving) {
                    for (double i=0; i < 0.50; i=i+0.1) {
                        box.translate(0, (int)(-i*delta));
                    }

                } else {
                    box.translate(0, (int)(-0.50*delta));
                }
            }
        }
        
        if (state == State.FALLING) {
            
            
            // check if intersect on falling
            if ((intersect != null) && intersect.intersects(box) ) {
                
                // check what object collided with
                switch (intersect.getEntityType()) {
                    case GROUND:
                        System.out.println("Player collided with Ground");
                        // check if intersects with top of object
                        if ((((Ground)intersect).intersectSide(box) == SIDES.TOP ) || 
                            (((Ground)intersect).intersectSide(box) == SIDES.TOP_LEFT) ||
                            (((Ground)intersect).intersectSide(box) == SIDES.TOP_RIGHT)) 
                        {
                            // Player is safe on top of rectangle
                            box.setY( ((Ground)intersect).getY() ); 
                            state = State.LEVEL;
                        } else {
                            // Player fucked up and fell through the cracks
                            state = State.FALLING;
                        }   
                        break;
                    case NPC:
                        // TODO: implement baddies
                        System.out.println("Heck, you hit an NPC.");
                        break;
                    default:
                        System.out.println("Heck, you hit something.");
                        break;
                }
                
            } else { // If not intersecting, fall regularly.
            
                if (moving) {
                    for (double i=0; i < 0.50; i=i+0.1) {
                        box.translate(0, (int)(i*delta));
                        i=i+.01;
                    }
                } else {
                    box.translate(0, (int)(.50*delta));
                }
                // check if player falls off the level
                if (box.getY() + box.getHeight() > (Display.getHeight() + box.getHeight())) {

                    //TODO: decrement lives. 
                    box.setY(Display.getHeight() - 50);
                    System.out.println("You done goofed now. I just reset your position.");
                    state = State.LEVEL;
                }
            }
        }
        
    }
    
    // override Entity method since we can answer this question
    @Override
    public boolean intersects(Rectangle other)
    {
        return box.intersects(other);
    }
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.PLAYER;
    }

    /**
     * Gets the x position for this object
     * @return x position
     */
    public int getX () {
        return box.getX();
    }
    
    /**
     * Gets the y position for this object
     * @return y position
     */
    public int getY () {
        return box.getY();
    }
    
    /**
     * Gets the rectangle for this object
     * @return box
     */
    public Rectangle getRectangle () {
        return box;
    }
}
