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

package com.alee.managers.style;

import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.LabelDescriptor;
import com.alee.laf.label.WLabelUI;
import com.alee.laf.label.WebLabelUI;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.LafUtils;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * Set of JUnit tests for {@link ComponentDescriptor}.
 *
 * @author Mikle Garin
 */
@FixMethodOrder ( MethodSorters.JVM )
public final class ComponentDescriptorTest
{
    /**
     * Initializes {@link WebLookAndFeel}.
     */
    @BeforeClass
    public static void initialize ()
    {
        CoreSwingUtils.invokeAndWait ( new Runnable ()
        {
            @Override
            public void run ()
            {
                WebLookAndFeel.setForceSingleEventsThread ( true );
                WebLookAndFeel.install ();
            }
        } );
    }

    /**
     * Tests {@link ComponentDescriptor} customization.
     */
    @Test
    public void customDescriptor ()
    {
        CoreSwingUtils.invokeAndWait ( new Runnable ()
        {
            @Override
            public void run ()
            {
                // Testing default JLabel UI
                final JLabel defaultLabel = new JLabel ();
                checkUI ( defaultLabel, WebLabelUI.class );

                // Registering new descriptor
                StyleManager.registerComponentDescriptor ( new MyLabelDescriptor () );

                // Testing custom JLabel UI
                final JLabel myLabel = new JLabel ();
                checkUI ( myLabel, MyLabelUI.class );

                // Registering new descriptor
                StyleManager.registerComponentDescriptor ( new LabelDescriptor () );

                // Testing restored default JLabel UI
                final JLabel restoredLabel = new JLabel ();
                checkUI ( restoredLabel, WebLabelUI.class );
            }
        } );
    }

    /**
     * Asserts UI class type.
     *
     * @param component {@link JComponent}
     * @param uiClass   expected {@link ComponentUI}
     */
    private void checkUI ( final JComponent component, final Class<? extends ComponentUI> uiClass )
    {
        final ComponentUI ui = LafUtils.getUI ( component );
        if ( !uiClass.isInstance ( ui ) )
        {
            throw new StyleException ( String.format (
                    "UI class '%s' is expected instead of '%s' in component: %s",
                    uiClass, ui.getClass (), component
            ) );
        }
    }

    /**
     * Custom {@link ComponentDescriptor} for {@link JLabel}.
     */
    public static class MyLabelDescriptor extends AbstractComponentDescriptor<JLabel, WLabelUI>
    {
        /**
         * Constrcuts new {@link MyLabelDescriptor}.
         */
        public MyLabelDescriptor ()
        {
            super ( "label", JLabel.class, "LabelUI", WLabelUI.class, MyLabelUI.class, StyleId.label );
        }
    }

    /**
     * Custom UI for {@link JLabel}.
     * It doesn't add anything new, just exists for class type check.
     */
    public static class MyLabelUI extends WebLabelUI
    {
        /**
         * Returns an instance of the {@link MyLabelUI} for the specified component.
         * This tricky method is used by {@link UIManager} to create component UIs when needed.
         *
         * @param c component that will use UI instance
         * @return instance of the {@link MyLabelUI}
         */
        @SuppressWarnings ( "unused" )
        public static ComponentUI createUI ( final JComponent c )
        {
            return new MyLabelUI ();
        }
    }

    /**
     * Destroys {@link WebLookAndFeel}.
     */
    @AfterClass
    public static void destroy ()
    {
        CoreSwingUtils.invokeAndWait ( new Runnable ()
        {
            @Override
            public void run ()
            {
                WebLookAndFeel.uninstall ();
            }
        } );
    }
}