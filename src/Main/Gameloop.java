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

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Mouse;



public class Gameloop
{
    public static final int TARGET_FPS=100;
    public static final int SCR_WIDTH=800;
    public static final int SCR_HEIGHT=600;

    public static void main(String[] args) throws LWJGLException
    {
        initGL(SCR_WIDTH, SCR_HEIGHT);
        
        // hide the mouse cursor
        Mouse.setGrabbed(true);

        Menu gameMenu = new Menu();
        gameMenu.addItem("Play", new Overworld());
        gameMenu.addSpecial("Exit", Menu.DO_EXIT);

        Scene currScene = gameMenu;


        while ( currScene.go() )
        {
            currScene = currScene.nextScene();

            if (currScene == null)
            {
                currScene = gameMenu;
            }
        }

        AudioManager.getInstance().destroy();
        
        Display.destroy();
    }
    

    public static void initGL(int width, int height) throws LWJGLException
    {
        // open window of appropriate size
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.create();
        Display.setVSyncEnabled(true);
        
        // enable 2D textures
        GL11.glEnable(GL11.GL_TEXTURE_2D);              
     
        // set "clear" color to black
        GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);         

        // enable alpha blending
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
         
        // set viewport to entire window
        GL11.glViewport(0,0,width,height);
         
        // set up orthographic projectionr
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, width, height, 0, 1, -1);
        // GLU.gluPerspective(90f, 1.333f, 2f, -2f);
        // GL11.glTranslated(0, 0, -500);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
}
