package Entities;

import java.util.Random;
import org.lwjgl.util.Rectangle;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.Display;

public class Ground extends Entity
{

    private String intersect_side;
    private static enum State  { START, LEFT, RIGHT };

    private Player player;
    private Rectangle box;
    private State state;
    private int groundNum;
    
    //private float speed;        // pixels / ms

    public Ground(int place, Random rand, Player player)
    {
        this.groundNum = place;
        this.player = player;
        int heck = (int)(10*rand.nextFloat());
        System.out.println("Ground entity generated with rand: " + heck);
        if ( heck > 6) {
            heck = 6;
        }
        box = new Rectangle(
                            place*(heck+(heck*100)), 
                            Display.getHeight() + player.getRectangle().getHeight(), 
                            500, 
                            100);
        state = State.START;
        //this.speed = speed;
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
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            box.translate((int)(.50*delta), 0);
            
        }

        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            box.translate((int)(-.50*delta), 0);
        }
        
        
    }
    
    @Override 
    public boolean intersects(Rectangle other) {
        return box.intersects(other);
    }
    
    @Override
    public Entity.E_TYPE getEntityType() {
        return E_TYPE.GROUND;
    }
    
    /** 
     * Returns the side of a 2D object the provided object intersects with.
     * @param other The object to test intersection with
     * @return The side of this Ground object `other` intersects with. See E_Entity SIDES.
     */
    public Entity.SIDES intersectSide(Rectangle other) {
        if (!this.intersects(other)) {
            return null;
        } else {
            
            // Other is left of ground (on edge)
            if ( (other.getX() + other.getWidth()) <= (box.getX() + other.getWidth()) ) {
                if ( other.getY() + other.getHeight() < box.getY() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_LEFT);
                    return SIDES.TOP_LEFT;
                }
                if ( other.getY() > box.getY() + box.getHeight() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_LEFT);
                    return SIDES.BOTTOM_LEFT;
                }
            } else
            
            // Other is right of ground (on edge)
            if ( (other.getX() + other.getWidth()) >= (box.getX() + box.getWidth()) ) {
                if ( other.getY() + other.getHeight() < box.getY() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP_RIGHT);
                    return SIDES.TOP_RIGHT;
                }
                if ( other.getY() > box.getY() + box.getHeight() ) {
                    System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM_RIGHT);
                    return SIDES.BOTTOM_RIGHT;
                }
            } else
            
            // Other is left of ground
            if ( (other.getX() + other.getWidth()) <= box.getX() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.LEFT);
                return SIDES.LEFT;
            } else
            
            // Other is right of ground
            if ( other.getX() >= (box.getX() + box.getWidth()) ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.RIGHT);
                return SIDES.RIGHT;
            } else
            
            // Other is top of ground
            if ( other.getY() + other.getHeight() <= box.getY() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.TOP);
                return SIDES.TOP;
            } else
                
            // Other is bottom of ground
            if ( other.getY() >= box.getX() + box.getHeight() ) {
                System.out.println("Player intersected with ground " + groundNum + " on side: " + SIDES.BOTTOM);
                return SIDES.BOTTOM;
            }
            
        }
        
        // intersection may be inside ground or 
        // no intersection exists that was not accounted for.
        return null;
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
