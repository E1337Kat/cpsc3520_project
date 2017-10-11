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

import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;
import org.newdawn.slick.util.ResourceLoader;
import org.lwjgl.openal.AL;

import java.util.HashMap;
import java.io.IOException;

/** A utility class for caching sounds from files and enabling the
 * straightforward triggering of those sounds (samples and loops)
 * @author Craig Tanis
 */
public class AudioManager 
{
    private HashMap<String, AudioWrapper> sounds;
    private enum AudioType { LOOP, SAMPLE };
    
    private static AudioManager instance;

    /** @return the singleton AudioManager instance
     */
    public static AudioManager getInstance()
    {
        if (instance == null) instance = new AudioManager();
        return instance;
    }

    /** A handle on a loaded audio object, to play the same sound repeatedly
     * without having to look it up
     */
    public class AudioWrapper
    {
        private Audio sound;
        private AudioType type;

        private AudioWrapper(AudioType t, Audio a)
        {
            type = t;
            sound = a;
        }

        /** play this sound at full volume
         */
        public void play()
        {
            play(1.0f);
            
        }
        
        /** Play this sound
            @param vol value between 0.0 and 1.0 to specify relative volume of sound
        */
        public void play(float vol)
        {
            switch (type)
            {
             case LOOP:
                 sound.playAsMusic(1.0f, vol, true);
                 break;

             case SAMPLE:
                 sound.playAsSoundEffect(1.0f, vol, false);
                 break;
            }
        }

    }


    private AudioManager()
    {
        sounds = new HashMap<String, AudioWrapper>();
    }

    
    /** load a one-shot sample
        @param name identifier for loaded sound
        @param path path to file
    */
    public void loadSample(String name, String path) throws IOException
    {
        // extract file extension
        String mode =  path.substring(path.lastIndexOf('.')+1).toUpperCase();

        Audio tmp = AudioLoader.getAudio(mode, ResourceLoader.getResourceAsStream(path));
        sounds.put(name, new AudioWrapper(AudioType.SAMPLE, tmp));
    }
    
    /** load a looping music track
        @param name identifier for loaded sound
        @param path path to file
    */
    public void loadLoop(String name, String path) throws IOException
    {
        // extract file extension
        String mode =  path.substring(path.lastIndexOf('.')+1).toUpperCase();

        Audio tmp = AudioLoader.getStreamingAudio(mode, ResourceLoader.getResource(path));
        sounds.put(name, new AudioWrapper(AudioType.LOOP, tmp));          
    }
    

    /** get sound associated with name identifier
        @param name the loaded sound identifier
    */
    public AudioWrapper get(String name)
    {
        return sounds.get(name);
    }

    /** play sound associated with name identifier
        @param name the loaded sound identifier
    */
    public void play(String name)
    {
        sounds.get(name).play();
    }


    /** play sound associated with name identifier
        @param name the loaded sound identifier
        @param vol volume of sound (0.0 to 1.0)
    */
    public void play(String name, float vol)
    {
        sounds.get(name).play(vol);
    }


    /** call this once per frame
     */
    public void update()
    {
        SoundStore.get().poll(0);
    }

    /** call this to clean up
     */
    public void destroy()
    {
        AL.destroy();
    }
}
