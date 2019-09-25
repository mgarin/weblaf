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

package com.alee.laf.label;

import com.alee.api.annotations.NotNull;
import com.alee.api.jdk.Objects;
import com.alee.laf.AbstractUIInputListener;
import com.alee.laf.UIAction;
import com.alee.laf.UIActionMap;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentInputMapUIResource;
import javax.swing.plaf.InputMapUIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Basic UI input listener for {@link WLabelUI} implementation.
 * It is partially based on Swing {@link javax.swing.plaf.basic.BasicLabelUI} but cleaned up and optimized.
 *
 * @param <C> {@link JLabel} type
 * @param <U> {@link WLabelUI} type
 * @author Hans Muller
 * @author Mikle Garin
 */
public class WLabelInputListener<C extends JLabel, U extends WLabelUI<C>> extends AbstractUIInputListener<C, U>
        implements LabelInputListener<C>, PropertyChangeListener
{
    @Override
    public void install ( @NotNull final C component )
    {
        super.install ( component );

        // Installing listeners
        component.addPropertyChangeListener ( this );

        // Installing ActionMap
        final UIActionMap actionMap = new UIActionMap ();
        actionMap.put ( new Action ( component, Action.PRESS ) );
        actionMap.put ( new Action ( component, Action.RELEASE ) );
        SwingUtilities.replaceUIActionMap ( component, actionMap );

        // Installing InputMap
        installInputMap ( component );
    }

    @Override
    public void uninstall ( @NotNull final C component )
    {
        // Uninstalling InputMap
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_IN_FOCUSED_WINDOW, null );
        SwingUtilities.replaceUIInputMap ( component, JComponent.WHEN_FOCUSED, null );

        // Uninstalling ActionMap
        SwingUtilities.replaceUIActionMap ( component, null );

        // Uninstalling listeners
        component.removePropertyChangeListener ( this );

        super.uninstall ( component );
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent e )
    {
        final String prop = e.getPropertyName ();
        if ( Objects.equals ( prop, WebLabel.DISPLAYED_MNEMONIC_PROPERTY, WebLabel.LABEL_FOR_PROPERTY ) )
        {
            installInputMap ( ( C ) e.getSource () );
        }
    }

    /**
     * Installs label {@link InputMap}.
     *
     * @param label {@link JLabel}
     */
    protected void installInputMap ( @NotNull final C label )
    {
        final int mnemonic = label.getDisplayedMnemonic ();
        final Component labelFor = label.getLabelFor ();
        if ( mnemonic != 0 && labelFor != null )
        {
            InputMap inputMap = SwingUtilities.getUIInputMap ( label, JComponent.WHEN_IN_FOCUSED_WINDOW );
            if ( inputMap == null )
            {
                inputMap = new ComponentInputMapUIResource ( label );
                SwingUtilities.replaceUIInputMap ( label, JComponent.WHEN_IN_FOCUSED_WINDOW, inputMap );
            }
            inputMap.clear ();
            inputMap.put ( KeyStroke.getKeyStroke ( mnemonic, ActionEvent.ALT_MASK, false ), "press" );
        }
        else
        {
            final InputMap inputMap = SwingUtilities.getUIInputMap ( label, JComponent.WHEN_IN_FOCUSED_WINDOW );
            if ( inputMap != null )
            {
                inputMap.clear ();
            }
        }
    }

    /**
     * Actions for {@link JLabel}.
     * When the accelerator is pressed, temporarily make {@link JLabel} focusTraversable by registering a WHEN_FOCUSED action for the
     * release of the accelerator. Then give it focus so it can prevent unwanted keyTyped events from getting to other components.
     *
     * @param <L> {@link JLabel} type
     */
    public static class Action<L extends JLabel> extends UIAction<L>
    {
        /**
         * Supported actions.
         */
        public static final String PRESS = "press";
        public static final String RELEASE = "release";

        /**
         * Constructs new label {@link Action} with the specified name.
         *
         * @param label {@link JLabel}
         * @param name  {@link Action} name
         */
        public Action ( @NotNull final L label, @NotNull final String name )
        {
            super ( label, name );
        }

        @Override
        public void actionPerformed ( final ActionEvent e )
        {
            final L label = ( L ) e.getSource ();
            final String key = getName ();
            if ( Objects.equals ( key, PRESS ) )
            {
                doPress ( label );
            }
            else if ( Objects.equals ( key, RELEASE ) )
            {
                doRelease ( label );
            }
        }

        /**
         * Performs on-press action.
         *
         * @param label {@link JLabel}
         */
        protected void doPress ( final L label )
        {
            final Component labelFor = label.getLabelFor ();
            if ( labelFor != null && labelFor.isEnabled () )
            {
                InputMap inputMap = SwingUtilities.getUIInputMap ( label, JComponent.WHEN_FOCUSED );
                if ( inputMap == null )
                {
                    inputMap = new InputMapUIResource ();
                    SwingUtilities.replaceUIInputMap ( label, JComponent.WHEN_FOCUSED, inputMap );
                }

                final int mnemonic = label.getDisplayedMnemonic ();
                inputMap.put ( KeyStroke.getKeyStroke ( mnemonic, ActionEvent.ALT_MASK, true ), RELEASE );
                inputMap.put ( KeyStroke.getKeyStroke ( KeyEvent.VK_ALT, 0, true ), RELEASE );

                label.requestFocus ();
            }
        }

        /**
         * Performs on-release action.
         *
         * @param label {@link JLabel}
         */
        protected void doRelease ( final L label )
        {
            final Component labelFor = label.getLabelFor ();
            if ( labelFor != null && labelFor.isEnabled () )
            {
                final InputMap inputMap = SwingUtilities.getUIInputMap ( label, JComponent.WHEN_FOCUSED );
                if ( inputMap != null )
                {
                    inputMap.remove ( KeyStroke.getKeyStroke ( label.getDisplayedMnemonic (), ActionEvent.ALT_MASK, true ) );
                    inputMap.remove ( KeyStroke.getKeyStroke ( KeyEvent.VK_ALT, 0, true ) );
                }

                if ( labelFor instanceof Container && ( ( Container ) labelFor ).isFocusCycleRoot () )
                {
                    labelFor.requestFocus ();
                }
                else
                {
                    SwingUtils.compositeRequestFocus ( labelFor );
                }
            }
        }
    }
}