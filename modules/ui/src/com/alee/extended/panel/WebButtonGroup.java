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

package com.alee.extended.panel;

import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebButtonUI;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ContainerAdapter;
import java.awt.event.ContainerEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * This component allows quick visual web-styled buttons grouping. It also contains all UI methods from the buttons.
 * Those methods apply specific value to all sub-buttons and sub-groups when called. Also this component groups buttons by default.
 *
 * @author Mikle Garin
 */

public class WebButtonGroup extends WebPanel implements SwingConstants
{
    /**
     * Settings.
     */
    protected int orientation = -1;
    protected boolean group = false;

    /**
     * Runtime variables.
     */
    protected UnselectableButtonGroup buttonGroup;
    protected PropertyChangeListener enabledStateListener;

    public WebButtonGroup ( final JComponent... component )
    {
        this ( HORIZONTAL, component );
    }

    public WebButtonGroup ( final boolean group, final JComponent... component )
    {
        this ( HORIZONTAL, group, component );
    }

    public WebButtonGroup ( final int orientation, final JComponent... components )
    {
        this ( orientation, false, components );
    }

    public WebButtonGroup ( final int orientation, final boolean group, final JComponent... components )
    {
        super ();

        this.group = group;

        // Default button group settings
        putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );
        setOpaque ( false );
        setOrientation ( orientation );

        // Container listener
        addContainerListener ( new ContainerAdapter ()
        {
            @Override
            public void componentAdded ( final ContainerEvent e )
            {
                // Group added button(s)
                if ( group )
                {
                    groupButtons ( e.getComponent () );
                }

                // Updating buttons styling
                updateButtonsStyling ();
            }

            @Override
            public void componentRemoved ( final ContainerEvent e )
            {
                // Ungroup removed button(s)
                if ( group )
                {
                    ungroupButtons ( e.getComponent () );
                }

                // Updating buttons styling
                updateButtonsStyling ();
            }
        } );

        // Buttons enabled state listener
        enabledStateListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                final Object source = evt.getSource ();
                if ( source instanceof JButton )
                {
                    final JButton src = ( JButton ) source;
                    final int zOrder = getComponentZOrder ( src );
                    if ( zOrder != -1 )
                    {
                        if ( zOrder > 0 )
                        {
                            getComponent ( zOrder - 1 ).repaint ();
                        }
                        if ( zOrder < getComponentCount () - 1 )
                        {
                            getComponent ( zOrder + 1 ).repaint ();
                        }
                    }
                }
            }
        };

        // Adding component to panel
        add ( components );
    }

    public WebButton getWebButton ( final int index )
    {
        return ( WebButton ) getComponent ( index );
    }

    public WebButton getWebButton ( final String name )
    {
        for ( final Component component : getComponents () )
        {
            if ( component.getName ().equals ( name ) )
            {
                return ( WebButton ) component;
            }
        }
        return null;
    }

    public boolean isAnySelected ()
    {
        return buttonGroup.getSelection () != null && buttonGroup.getSelection ().isSelected ();
    }

    public boolean isGroup ()
    {
        return group;
    }

    public void setGroup ( final boolean group )
    {
        if ( this.group != group )
        {
            this.group = group;
            if ( group )
            {
                groupButtons ( this );
            }
            else
            {
                ungroupButtons ( this );
            }
        }
    }

    public UnselectableButtonGroup getButtonGroup ()
    {
        if ( buttonGroup == null )
        {
            buttonGroup = new UnselectableButtonGroup ();
            buttonGroup.setUnselectable ( false );
        }
        return buttonGroup;
    }

    protected void groupButtons ( final Component component )
    {
        if ( isButton ( component ) )
        {
            getButtonGroup ().add ( ( AbstractButton ) component );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            final WebButtonGroup innerGroup = ( WebButtonGroup ) component;
            for ( final Component innerComponent : innerGroup.getComponents () )
            {
                groupButtons ( innerComponent );
            }
        }
    }

    protected void ungroupButtons ( final Component component )
    {
        if ( isButton ( component ) )
        {
            getButtonGroup ().remove ( ( AbstractButton ) component );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            final WebButtonGroup innerGroup = ( WebButtonGroup ) component;
            for ( final Component innerComponent : innerGroup.getComponents () )
            {
                ungroupButtons ( innerComponent );
            }
        }
    }

    public boolean isUnselectable ()
    {
        return getButtonGroup ().isUnselectable ();
    }

    public void setUnselectable ( final boolean unselectable )
    {
        getButtonGroup ().setUnselectable ( unselectable );
    }

    public int getOrientation ()
    {
        return orientation;
    }

    public void setOrientation ( final int orientation )
    {
        if ( this.orientation != orientation )
        {
            this.orientation = orientation;

            // Updating layout
            setLayout ( orientation == HORIZONTAL ? new HorizontalFlowLayout ( 0, false ) :
                    new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 0, true, false ) );

            // Updating buttons styling
            updateButtonsStyling ();
        }
    }

    protected void updateButtonsStyling ()
    {
        // Updating button UI's
        final Component[] components = getComponents ();
        final boolean horizontal = orientation == HORIZONTAL;
        boolean lastWasButton = false;
        for ( int i = 0; i < components.length; i++ )
        {
            if ( isWebStyledButton ( components[ i ] ) )
            {
                // Web UI
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) components[ i ] ).getUI ();

                // todo FIX
                //                // Hiding left side
                //                if ( lastWasButton )
                //                {
                //                    if ( horizontal )
                //                    {
                //                        ui.setDrawLeft ( false );
                //                        ui.setDrawLeftLine ( false );
                //                    }
                //                    else
                //                    {
                //                        ui.setDrawTop ( false );
                //                        ui.setDrawTopLine ( false );
                //                    }
                //                }
                //
                //                // Hiding right side
                //                if ( i < components.length - 1 &&
                //                        ( isWebStyledButton ( components[ i + 1 ] ) || isWebButtonGroup ( components[ i + 1 ] ) ) )
                //                {
                //                    if ( horizontal )
                //                    {
                //                        ui.setDrawRight ( false );
                //                        ui.setDrawRightLine ( true );
                //                    }
                //                    else
                //                    {
                //                        ui.setDrawBottom ( false );
                //                        ui.setDrawBottomLine ( true );
                //                    }
                //                }

                lastWasButton = true;
            }
            else if ( isWebButtonGroup ( components[ i ] ) )
            {
                // WebButtonGroup
                final WebButtonGroup wbg = ( WebButtonGroup ) components[ i ];

                // Hiding left side
                if ( lastWasButton )
                {
                    for ( final Component component : wbg.getComponents () )
                    {
                        if ( isWebStyledButton ( component ) )
                        {
                            // todo FIX
                            //                            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                            //                            if ( horizontal )
                            //                            {
                            //                                ui.setDrawLeft ( false );
                            //                                ui.setDrawLeftLine ( false );
                            //                            }
                            //                            else
                            //                            {
                            //                                ui.setDrawTop ( false );
                            //                                ui.setDrawTopLine ( false );
                            //                            }
                        }
                    }
                }

                // Hiding right side
                if ( i < components.length - 1 &&
                        ( isWebStyledButton ( components[ i + 1 ] ) || isWebButtonGroup ( components[ i + 1 ] ) ) )
                {
                    for ( final Component component : wbg.getComponents () )
                    {
                        if ( isWebStyledButton ( component ) )
                        {
                            // todo FIX
//                            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
//                            if ( horizontal )
//                            {
//                                ui.setDrawRight ( false );
//                                ui.setDrawRightLine ( true );
//                            }
//                            else
//                            {
//                                ui.setDrawBottom ( false );
//                                ui.setDrawBottomLine ( true );
//                            }
                        }
                    }
                }

                lastWasButton = true;
            }
            else
            {
                lastWasButton = false;
            }
        }
    }

    @Override
    protected void addImpl ( final Component comp, final Object constraints, final int index )
    {
        super.addImpl ( comp, constraints, index );
        comp.addPropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, enabledStateListener );
    }

    @Override
    public void remove ( final int index )
    {
        getComponent ( index ).removePropertyChangeListener ( WebLookAndFeel.ENABLED_PROPERTY, enabledStateListener );
        super.remove ( index );
    }

    protected boolean isButton ( final Component component )
    {
        return component instanceof AbstractButton;
    }

    protected boolean isWebStyledButton ( final Component component )
    {
        return isButton ( component ) && ( ( AbstractButton ) component ).getUI () instanceof WebButtonUI;
    }

    protected boolean isWebButtonGroup ( final Component component )
    {
        return component instanceof WebButtonGroup;
    }

    @Override
    public void setEnabled ( final boolean enabled )
    {
        super.setEnabled ( enabled );
        for ( final Component component : getComponents () )
        {
            component.setEnabled ( enabled );
        }
    }
}