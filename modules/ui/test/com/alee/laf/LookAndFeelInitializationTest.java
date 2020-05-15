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

package com.alee.laf;

import com.alee.utils.CoreSwingUtils;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;

/**
 * Set of JUnit tests for {@link WebLookAndFeel}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public class LookAndFeelInitializationTest
{
    /**
     * {@link WebLookAndFeel} installation and uninstallation test through {@link UIManager}.
     */
    @Test
    public void installUninstallBasic ()
    {
        CoreSwingUtils.invokeAndWait ( new Runnable ()
        {
            @Override
            public void run ()
            {
                try
                {
                    // Installing WebLookAndFeel
                    UIManager.setLookAndFeel ( new WebLookAndFeel () );

                    // Uninstalling WebLookAndFeel
                    UIManager.setLookAndFeel ( MetalLookAndFeel.class.getCanonicalName () );
                }
                catch ( final Exception e )
                {
                    throw new RuntimeException ( e );
                }
            }
        } );
    }

    /**
     * {@link WebLookAndFeel} installation and uninstallation test through custom L&F class methods.
     */
    @Test
    public void installUninstallCustom ()
    {
        CoreSwingUtils.invokeAndWait ( new Runnable ()
        {
            @Override
            public void run ()
            {
                try
                {
                    // Installing WebLookAndFeel
                    WebLookAndFeel.install ();

                    // Uninstalling WebLookAndFeel
                    WebLookAndFeel.uninstall ();
                }
                catch ( final Exception e )
                {
                    throw new RuntimeException ( e );
                }
            }
        } );
    }
}