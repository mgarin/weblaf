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
import com.alee.extended.painter.Painter;
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

                // Hiding left side
                if ( lastWasButton )
                {
                    if ( horizontal )
                    {
                        ui.setDrawLeft ( false );
                        ui.setDrawLeftLine ( false );
                    }
                    else
                    {
                        ui.setDrawTop ( false );
                        ui.setDrawTopLine ( false );
                    }
                }

                // Hiding right side
                if ( i < components.length - 1 &&
                        ( isWebStyledButton ( components[ i + 1 ] ) || isWebButtonGroup ( components[ i + 1 ] ) ) )
                {
                    if ( horizontal )
                    {
                        ui.setDrawRight ( false );
                        ui.setDrawRightLine ( true );
                    }
                    else
                    {
                        ui.setDrawBottom ( false );
                        ui.setDrawBottomLine ( true );
                    }
                }

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
                            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                            if ( horizontal )
                            {
                                ui.setDrawLeft ( false );
                                ui.setDrawLeftLine ( false );
                            }
                            else
                            {
                                ui.setDrawTop ( false );
                                ui.setDrawTopLine ( false );
                            }
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
                            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                            if ( horizontal )
                            {
                                ui.setDrawRight ( false );
                                ui.setDrawRightLine ( true );
                            }
                            else
                            {
                                ui.setDrawBottom ( false );
                                ui.setDrawBottomLine ( true );
                            }
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

    /**
     * Multi-update methods for some of WebButtonUI properties
     */

    public void setButtonsFocusable ( final boolean focusable )
    {
        setFocusable ( WebButtonGroup.this, focusable );
    }

    protected void setFocusable ( final WebButtonGroup group, final boolean focusable )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebButtonGroup ( component ) )
            {
                setFocusable ( ( WebButtonGroup ) component, focusable );
            }
            else
            {
                component.setFocusable ( focusable );
            }
        }
    }

    public void setButtonsForeground ( final Color foreground )
    {
        setForeground ( WebButtonGroup.this, foreground );
    }

    protected void setForeground ( final WebButtonGroup group, final Color foreground )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebButtonGroup ( component ) )
            {
                setForeground ( ( WebButtonGroup ) component, foreground );
            }
            else
            {
                component.setForeground ( foreground );
            }
        }
    }

    public void setButtonsSelectedForeground ( final Color selectedForeground )
    {
        setSelectedForeground ( WebButtonGroup.this, selectedForeground );
    }

    protected void setSelectedForeground ( final WebButtonGroup group, final Color selectedForeground )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setSelectedForeground ( selectedForeground );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setSelectedForeground ( ( WebButtonGroup ) component, selectedForeground );
            }
        }
    }

    public void setButtonsDrawTop ( final boolean drawTop )
    {
        setDrawTop ( WebButtonGroup.this, drawTop );
    }

    protected void setDrawTop ( final WebButtonGroup group, final boolean drawTop )
    {
        if ( orientation == HORIZONTAL )
        {
            for ( final Component component : group.getComponents () )
            {
                setDrawTop ( component, drawTop );
            }
        }
        else
        {
            setDrawTop ( getFirstComponent (), drawTop );
        }
    }

    protected void setDrawTop ( final Component component, final boolean drawTop )
    {
        if ( isWebStyledButton ( component ) )
        {
            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawTop ( drawTop );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawTop ( ( WebButtonGroup ) component, drawTop );
        }
    }

    public void setButtonsDrawLeft ( final boolean drawLeft )
    {
        setDrawLeft ( WebButtonGroup.this, drawLeft );
    }

    protected void setDrawLeft ( final WebButtonGroup group, final boolean drawLeft )
    {
        if ( orientation == VERTICAL )
        {
            for ( final Component component : group.getComponents () )
            {
                setDrawLeft ( component, drawLeft );
            }
        }
        else
        {
            setDrawLeft ( getFirstComponent (), drawLeft );
        }
    }

    protected void setDrawLeft ( final Component component, final boolean drawLeft )
    {
        if ( isWebStyledButton ( component ) )
        {
            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawLeft ( drawLeft );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawLeft ( ( WebButtonGroup ) component, drawLeft );
        }
    }

    public void setButtonsDrawBottom ( final boolean drawBottom )
    {
        setDrawBottom ( WebButtonGroup.this, drawBottom );
    }

    protected void setDrawBottom ( final WebButtonGroup group, final boolean drawBottom )
    {
        if ( orientation == HORIZONTAL )
        {
            for ( final Component component : group.getComponents () )
            {
                setDrawBottom ( component, drawBottom );
            }
        }
        else
        {
            setDrawBottom ( getLastComponent (), drawBottom );
        }
    }

    protected void setDrawBottom ( final Component component, final boolean drawBottom )
    {
        if ( isWebStyledButton ( component ) )
        {
            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawBottom ( drawBottom );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawBottom ( ( WebButtonGroup ) component, drawBottom );
        }
    }

    public void setButtonsDrawRight ( final boolean drawRight )
    {
        setDrawRight ( WebButtonGroup.this, drawRight );
    }

    protected void setDrawRight ( final WebButtonGroup group, final boolean drawRight )
    {
        if ( orientation == VERTICAL )
        {
            for ( final Component component : group.getComponents () )
            {
                setDrawRight ( component, drawRight );
            }
        }
        else
        {
            setDrawRight ( getLastComponent (), drawRight );
        }
    }

    protected void setDrawRight ( final Component component, final boolean drawRight )
    {
        if ( isWebStyledButton ( component ) )
        {
            final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawRight ( drawRight );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawRight ( ( WebButtonGroup ) component, drawRight );
        }
    }

    public void setButtonsDrawSides ( final boolean top, final boolean left, final boolean bottom, final boolean right )
    {
        setButtonsDrawTop ( top );
        setButtonsDrawLeft ( left );
        setButtonsDrawBottom ( bottom );
        setButtonsDrawRight ( right );
    }

    public void setButtonsRolloverDarkBorderOnly ( final boolean rolloverDarkBorderOnly )
    {
        setRolloverDarkBorderOnly ( WebButtonGroup.this, rolloverDarkBorderOnly );
    }

    protected void setRolloverDarkBorderOnly ( final WebButtonGroup group, final boolean rolloverDarkBorderOnly )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverDarkBorderOnly ( ( WebButtonGroup ) component, rolloverDarkBorderOnly );
            }
        }
    }

    public void setButtonsRolloverShine ( final boolean rolloverShine )
    {
        setRolloverShine ( WebButtonGroup.this, rolloverShine );
    }

    protected void setRolloverShine ( final WebButtonGroup group, final boolean rolloverShine )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverShine ( rolloverShine );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverShine ( ( WebButtonGroup ) component, rolloverShine );
            }
        }
    }

    public void setButtonsShineColor ( final Color shineColor )
    {
        setShineColor ( WebButtonGroup.this, shineColor );
    }

    protected void setShineColor ( final WebButtonGroup group, final Color shineColor )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setShineColor ( shineColor );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setShineColor ( ( WebButtonGroup ) component, shineColor );
            }
        }
    }

    public void setButtonsRound ( final int round )
    {
        setRound ( WebButtonGroup.this, round );
    }

    protected void setRound ( final WebButtonGroup group, final int round )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRound ( round );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRound ( ( WebButtonGroup ) component, round );
            }
        }
    }

    public void setButtonsRolloverShadeOnly ( final boolean rolloverShadeOnly )
    {
        setRolloverShadeOnly ( WebButtonGroup.this, rolloverShadeOnly );
    }

    protected void setRolloverShadeOnly ( final WebButtonGroup group, final boolean rolloverShadeOnly )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverShadeOnly ( rolloverShadeOnly );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverShadeOnly ( ( WebButtonGroup ) component, rolloverShadeOnly );
            }
        }
    }

    public void setButtonsShadeWidth ( final int shadeWidth )
    {
        setShadeWidth ( WebButtonGroup.this, shadeWidth );
    }

    protected void setShadeWidth ( final WebButtonGroup group, final int shadeWidth )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setShadeWidth ( shadeWidth );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setShadeWidth ( ( WebButtonGroup ) component, shadeWidth );
            }
        }
    }

    public void setButtonsShadeColor ( final Color shadeColor )
    {
        setShadeColor ( WebButtonGroup.this, shadeColor );
    }

    protected void setShadeColor ( final WebButtonGroup group, final Color shadeColor )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setShadeColor ( shadeColor );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setShadeColor ( ( WebButtonGroup ) component, shadeColor );
            }
        }
    }

    public void setButtonsInnerShadeWidth ( final int innerShadeWidth )
    {
        setInnerShadeWidth ( WebButtonGroup.this, innerShadeWidth );
    }

    protected void setInnerShadeWidth ( final WebButtonGroup group, final int innerShadeWidth )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setInnerShadeWidth ( innerShadeWidth );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setInnerShadeWidth ( ( WebButtonGroup ) component, innerShadeWidth );
            }
        }
    }

    public void setButtonsInnerShadeColor ( final Color innerShadeColor )
    {
        setInnerShadeColor ( WebButtonGroup.this, innerShadeColor );
    }

    protected void setInnerShadeColor ( final WebButtonGroup group, final Color innerShadeColor )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setInnerShadeColor ( innerShadeColor );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setInnerShadeColor ( ( WebButtonGroup ) component, innerShadeColor );
            }
        }
    }

    public void setButtonsLeftRightSpacing ( final int leftRightSpacing )
    {
        setLeftRightSpacing ( WebButtonGroup.this, leftRightSpacing );
    }

    protected void setLeftRightSpacing ( final WebButtonGroup group, final int leftRightSpacing )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setLeftRightSpacing ( leftRightSpacing );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setLeftRightSpacing ( ( WebButtonGroup ) component, leftRightSpacing );
            }
        }
    }

    public void setButtonsRolloverDecoratedOnly ( final boolean rolloverDecoratedOnly )
    {
        setRolloverDecoratedOnly ( WebButtonGroup.this, rolloverDecoratedOnly );
    }

    protected void setRolloverDecoratedOnly ( final WebButtonGroup group, final boolean rolloverDecoratedOnly )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverDecoratedOnly ( rolloverDecoratedOnly );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverDecoratedOnly ( ( WebButtonGroup ) component, rolloverDecoratedOnly );
            }
        }
    }

    public void setButtonsUndecorated ( final boolean undecorated )
    {
        setUndecorated ( WebButtonGroup.this, undecorated );
    }

    protected void setUndecorated ( final WebButtonGroup group, final boolean undecorated )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setUndecorated ( undecorated );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setUndecorated ( ( WebButtonGroup ) component, undecorated );
            }
        }
    }

    public void setButtonsPainter ( final Painter painter )
    {
        setPainter ( WebButtonGroup.this, painter );
    }

    protected void setPainter ( final WebButtonGroup group, final Painter painter )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setPainter ( painter );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setPainter ( ( WebButtonGroup ) component, painter );
            }
        }
    }

    public void setButtonsMoveIconOnPress ( final boolean moveIconOnPress )
    {
        setMoveIconOnPress ( WebButtonGroup.this, moveIconOnPress );
    }

    protected void setMoveIconOnPress ( final WebButtonGroup group, final boolean moveIconOnPress )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setMoveIconOnPress ( moveIconOnPress );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setMoveIconOnPress ( ( WebButtonGroup ) component, moveIconOnPress );
            }
        }
    }

    public void setButtonsDrawFocus ( final boolean drawFocus )
    {
        setDrawFocus ( WebButtonGroup.this, drawFocus );
    }

    protected void setDrawFocus ( final WebButtonGroup group, final boolean drawFocus )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setDrawFocus ( drawFocus );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setDrawFocus ( ( WebButtonGroup ) component, drawFocus );
            }
        }
    }

    public void setButtonsMargin ( final Insets margin )
    {
        setMargin ( WebButtonGroup.this, margin );
    }

    public void setButtonsMargin ( final int top, final int left, final int bottom, final int right )
    {
        setButtonsMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setButtonsMargin ( final int spacing )
    {
        setButtonsMargin ( spacing, spacing, spacing, spacing );
    }

    protected void setMargin ( final WebButtonGroup group, final Insets margin )
    {
        for ( final Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                final WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setMargin ( margin );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setMargin ( ( WebButtonGroup ) component, margin );
            }
        }
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