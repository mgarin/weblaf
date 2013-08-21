/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.examples.content;

import com.alee.examples.content.presentation.PresentationStep;
import com.alee.utils.ThreadUtils;

import javax.swing.*;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This abstract class provides additional methods for WebLaF demo example classes.
 *
 * @author Mikle Garin
 */

public abstract class DefaultExample implements Example
{
    // Mouse buttons icons
    public static final ImageIcon lmb = new ImageIcon ( DefaultExample.class.getResource ( "icons/mouse/lmb.png" ) );
    public static final ImageIcon mmb = new ImageIcon ( DefaultExample.class.getResource ( "icons/mouse/mmb.png" ) );
    public static final ImageIcon rmb = new ImageIcon ( DefaultExample.class.getResource ( "icons/mouse/rmb.png" ) );
    public static final ImageIcon cursor = new ImageIcon ( DefaultExample.class.getResource ( "icons/mouse/cursor.png" ) );

    // Example icons cache
    private static final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon> ();

    // Presentation end runnable
    private Runnable onPresentationEnd;

    // Should stop presentation immediately
    private boolean shouldStop = false;

    // Presentation thread
    private Thread presentation;

    /**
     * Retuns null list of resources by default.
     *
     * @return null
     */
    @Override
    public List<Object> getResources ()
    {
        return null;
    }

    /**
     * Returns false because live presentation is disabled by default.
     *
     * @return false
     */
    @Override
    public boolean isPresentationAvailable ()
    {
        return false;
    }

    /**
     * Returns null list of live presentation steps by default.
     *
     * @return null
     */
    public List<PresentationStep> getPresentationSteps ()
    {
        return null;
    }

    /**
     * Starts live presentation in a separate thread.
     * This call will retrieve presentation steps and guide user through.
     */
    @Override
    public void startPresentation ()
    {
        // Ignore if there are no available steps
        final List<PresentationStep> presentationSteps = getPresentationSteps ();
        if ( presentationSteps == null || presentationSteps.size () == 0 )
        {
            return;
        }

        // Start live presentation in a separate thread
        presentation = new Thread ( new Runnable ()
        {
            @Override
            public void run ()
            {
                for ( PresentationStep step : presentationSteps )
                {
                    // Step start
                    if ( shouldStop )
                    {
                        shouldStop = false;
                        return;
                    }
                    step.getOnStart ().run ();

                    // Step duration (0 = unending step, requires user assistance to proceed)
                    int duration = step.getDuration ();
                    ThreadUtils.sleepSafely ( duration > 0 ? duration : Long.MAX_VALUE );

                    // Step end
                    step.getOnEnd ().run ();
                    if ( shouldStop )
                    {
                        shouldStop = false;
                        return;
                    }
                }

                // Presentation end
                if ( shouldStop )
                {
                    shouldStop = false;
                    return;
                }
                onPresentationEnd.run ();
            }
        } );
        presentation.start ();
    }

    /**
     * Forces live presentation to proceed to next step.
     */
    @Override
    public void nextPresentationStep ()
    {
        if ( presentation.isAlive () )
        {
            presentation.interrupt ();
        }
    }

    /**
     * Stops live presentation.
     */
    @Override
    public void stopPresentation ()
    {
        if ( presentation.isAlive () )
        {
            shouldStop = true;
            presentation.interrupt ();
        }
    }

    /**
     * Sets a runnable that should be executed at the end of the presentation.
     * You might want to cleanup some variables or components here.
     *
     * @param runnable runnable
     */
    @Override
    public void doWhenPresentationFinished ( Runnable runnable )
    {
        this.onPresentationEnd = runnable;
    }

    /**
     * Returns FeatureState.release state by default.
     *
     * @return FeatureState.release
     */
    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.release;
    }

    /**
     * Returns false to disable space filling by the example by default.
     *
     * @return false
     */
    @Override
    public boolean isFillWidth ()
    {
        return false;
    }

    /**
     * Returns icon loaded from example icons package.
     *
     * @param path path to the icon inside icons package
     * @return loaded icon
     */
    public ImageIcon loadIcon ( String path )
    {
        return loadIcon ( getClass (), path );
    }

    /**
     * Returns icon loaded from example icons package.
     *
     * @param nearClass example class
     * @param path      path to the icon inside icons package
     * @return loaded icon
     */
    public ImageIcon loadIcon ( Class nearClass, String path )
    {
        String key = nearClass.getCanonicalName () + ":" + path;
        if ( !iconsCache.containsKey ( key ) )
        {
            iconsCache.put ( key, new ImageIcon ( nearClass.getResource ( "icons/" + path ) ) );
        }
        return iconsCache.get ( key );
    }

    /**
     * Returns resource file url which is located in resources package.
     *
     * @param path path to the resource file inside resources package
     * @return resource file url
     */
    public URL getResource ( String path )
    {
        return getClass ().getResource ( "resources/" + path );
    }
}