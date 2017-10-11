package Entities;


import org.lwjgl.util.Rectangle;



public class Entity
{
    /**
     * For collision. It represents the side of a 2D object.
     */
    public static enum SIDES { TOP, 
                                BOTTOM, 
                                LEFT, 
                                RIGHT, 
                                TOP_LEFT, 
                                TOP_RIGHT, 
                                BOTTOM_LEFT, 
                                BOTTOM_RIGHT };
    
    
    public static enum E_TYPE { BACKGROUND,
                                PLAYER,
                                GROUND,
                                NPC,
                                OTHER
    };
    
    public void update(float delta, Entity intersect)
    {
        
    }


    public void draw()
    {
        
    }
    
    // override this if you want to be able to see if your entity interacts
    // with another rectangle
    public boolean intersects(Rectangle other)
    {
        return false;
    }
    
    public E_TYPE getEntityType () {
        return E_TYPE.OTHER;
    }
}
