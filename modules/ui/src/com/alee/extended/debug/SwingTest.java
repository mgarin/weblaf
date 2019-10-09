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

package com.alee.extended.debug;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.style.Skin;
import com.alee.utils.CoreSwingUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;

/**
 * Simple utility for quickly running test Swing code.
 *
 * @author Mikle Garin
 */
public final class SwingTest
{
    /**
     * Runs {@link Runnable} within Event Dispatch Thread after installin {@link WebLookAndFeel}.
     *
     * @param runnable {@link Runnable}
     */
    public static void run ( @NotNull final Runnable runnable )
    {
        run ( WebLookAndFeel.class.getCanonicalName (), null, runnable );
    }

    /**
     * @param runnable {@link Runnable}
     * @param skin     {@link Skin} {@link Class}
     */
    public static void run ( @NotNull final Class<? extends Skin> skin,
                             @NotNull final Runnable runnable )
    {
        run ( WebLookAndFeel.class.getCanonicalName (), skin, runnable );
    }

    /**
     * @param runnable    {@link Runnable}
     * @param lookAndFeel L&F {@link Class} canonical name
     */
    public static void run ( @NotNull final String lookAndFeel,
                             @NotNull final Runnable runnable )
    {
        run ( lookAndFeel, null, runnable );
    }

    /**
     * @param runnable    {@link Runnable}
     * @param lookAndFeel L&F {@link Class} canonical name
     * @param skin        {@link Skin} {@link Class}, only useful for {@link WebLookAndFeel}
     */
    private static void run ( @NotNull final String lookAndFeel, @Nullable final Class<? extends Skin> skin,
                              @NotNull final Runnable runnable )
    {
        CoreSwingUtils.enableEventQueueLogging ();
        SwingUtilities.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                if ( lookAndFeel.equals ( WebLookAndFeel.class.getCanonicalName () ) )
                {
                    if ( skin != null )
                    {
                        WebLookAndFeel.install ( skin );
                    }
                    else
                    {
                        WebLookAndFeel.install ();
                    }
                }
                else
                {
                    try
                    {
                        UIManager.setLookAndFeel ( lookAndFeel );
                    }
                    catch ( final ClassNotFoundException e )
                    {
                        LoggerFactory.getLogger ( SwingTest.class ).error ( "L&F class not found: " + lookAndFeel, e );
                    }
                    catch ( final InstantiationException e )
                    {
                        LoggerFactory.getLogger ( SwingTest.class ).error ( "Unable to instantiate L&F class: " + lookAndFeel, e );
                    }
                    catch ( final IllegalAccessException e )
                    {
                        LoggerFactory.getLogger ( SwingTest.class ).error ( "Unable to access L&F class: " + lookAndFeel, e );
                    }
                    catch ( final UnsupportedLookAndFeelException e )
                    {
                        LoggerFactory.getLogger ( SwingTest.class ).error ( "Unsupported L&F: " + lookAndFeel, e );
                    }
                }
                runnable.run ();
            }
        } );
    }
}