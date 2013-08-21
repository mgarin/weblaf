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
import com.alee.laf.button.WebButton;
import com.alee.laf.button.WebButtonUI;
import com.alee.laf.panel.WebPanel;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.UnselectableButtonGroup;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 11/28/11 Time: 5:07 PM
 * <p/>
 * This component allows quick visual web-styled buttons grouping. It also contains all UI methods from the buttons. Those methods apply
 * specific value to all sub-buttons and sub-groups when called. Also this component groups buttons by default.
 */

public class WebButtonGroup extends WebPanel implements SwingConstants
{
    // todo Fix all "add" methods so buttons could be added dynamically

    private int orientation;
    private UnselectableButtonGroup buttonGroup;

    public WebButtonGroup ( JComponent... component )
    {
        this ( HORIZONTAL, component );
    }

    public WebButtonGroup ( boolean group, JComponent... component )
    {
        this ( HORIZONTAL, group, component );
    }

    public WebButtonGroup ( int orientation, JComponent... components )
    {
        this ( orientation, false, components );
    }

    public WebButtonGroup ( int orientation, boolean group, JComponent... components )
    {
        super ();
        putClientProperty ( SwingUtils.HANDLES_ENABLE_STATE, true );

        // Transparent by default
        setOpaque ( false );

        // Update orientation and layout
        setOrientation ( orientation );

        // Adding component to panel   
        if ( components != null )
        {
            for ( JComponent component : components )
            {
                if ( component != null )
                {
                    add ( component );
                    if ( group )
                    {
                        if ( isButton ( component ) )
                        {
                            getButtonGroup ().add ( ( AbstractButton ) component );
                        }
                        else if ( isWebButtonGroup ( component ) )
                        {
                            WebButtonGroup innerGroup = ( WebButtonGroup ) component;
                            for ( Component innerComponent : innerGroup.getComponents () )
                            {
                                if ( isButton ( innerComponent ) )
                                {
                                    getButtonGroup ().add ( ( AbstractButton ) innerComponent );
                                }
                            }
                        }
                    }
                }
            }
        }

        // Updating UIs
        updateButtons ();
    }

    public WebButton getWebButton ( int index )
    {
        return ( WebButton ) getComponent ( index );
    }

    public WebButton getWebButton ( String name )
    {
        for ( Component component : getComponents () )
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

    public UnselectableButtonGroup getButtonGroup ()
    {
        if ( buttonGroup == null )
        {
            buttonGroup = new UnselectableButtonGroup ();
            buttonGroup.setUnselectable ( false );
        }
        return buttonGroup;
    }

    public boolean isUnselectable ()
    {
        return getButtonGroup ().isUnselectable ();
    }

    public void setUnselectable ( boolean unselectable )
    {
        getButtonGroup ().setUnselectable ( unselectable );
    }

    public int getOrientation ()
    {
        return orientation;
    }

    public void setOrientation ( int orientation )
    {
        this.orientation = orientation;

        // Updating layout
        boolean horizontal = orientation == HORIZONTAL;
        setLayout (
                horizontal ? new HorizontalFlowLayout ( 0, false ) : new VerticalFlowLayout ( VerticalFlowLayout.TOP, 0, 0, true, false ) );

        // Updating UIs
        updateButtons ();
    }

    public void updateButtons ()
    {
        // Updating button UI's
        Component[] components = getComponents ();
        boolean horizontal = orientation == HORIZONTAL;
        boolean lastWasButton = false;
        for ( int i = 0; i < components.length; i++ )
        {
            if ( isWebStyledButton ( components[ i ] ) )
            {
                // Web UI
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) components[ i ] ).getUI ();

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
                if ( i < components.length - 1 && isWebStyledButton ( components[ i + 1 ] ) )
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
                WebButtonGroup wbg = ( WebButtonGroup ) components[ i ];

                // Hiding left side
                if ( lastWasButton )
                {
                    for ( Component component : wbg.getComponents () )
                    {
                        if ( isWebStyledButton ( component ) )
                        {
                            WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
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
                if ( i < components.length - 1 && isWebButtonGroup ( components[ i + 1 ] ) )
                {
                    for ( Component component : wbg.getComponents () )
                    {
                        if ( isWebStyledButton ( component ) )
                        {
                            WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
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

    private void restoreSides ( boolean horizontal, WebButtonUI ui )
    {
        if ( getParent () instanceof WebButtonGroup )
        {
            // Ignore affected by parent sides
            if ( horizontal )
            {
                ui.setDrawLeft ( true );
                ui.setDrawLeftLine ( false );
                ui.setDrawRight ( true );
                ui.setDrawRightLine ( false );
            }
            else
            {
                ui.setDrawTop ( true );
                ui.setDrawTopLine ( false );
                ui.setDrawBottom ( true );
                ui.setDrawBottomLine ( false );
            }
        }
        else
        {
            // Restore all sides
            ui.setDrawSides ( true, true, true, true );
            ui.setDrawLines ( false, false, false, false );
        }
    }

    private boolean isWebStyledButton ( Component component )
    {
        return isButton ( component ) && ( ( AbstractButton ) component ).getUI () instanceof WebButtonUI;
    }

    private boolean isButton ( Component component )
    {
        return component instanceof AbstractButton;
    }

    private boolean isWebButtonGroup ( Component component )
    {
        return component instanceof WebButtonGroup;
    }

    /**
     * Multi-update methods for some of WebButtonUI properties
     */

    public void setButtonsFocusable ( boolean focusable )
    {
        setFocusable ( WebButtonGroup.this, focusable );
    }

    private void setFocusable ( WebButtonGroup group, boolean focusable )
    {
        for ( Component component : group.getComponents () )
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

    public void setButtonsForeground ( Color foreground )
    {
        setForeground ( WebButtonGroup.this, foreground );
    }

    private void setForeground ( WebButtonGroup group, Color foreground )
    {
        for ( Component component : group.getComponents () )
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

    public void setButtonsSelectedForeground ( Color selectedForeground )
    {
        setSelectedForeground ( WebButtonGroup.this, selectedForeground );
    }

    private void setSelectedForeground ( WebButtonGroup group, Color selectedForeground )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setSelectedForeground ( selectedForeground );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setSelectedForeground ( ( WebButtonGroup ) component, selectedForeground );
            }
        }
    }

    public void setButtonsDrawTop ( boolean drawTop )
    {
        setDrawTop ( WebButtonGroup.this, drawTop );
    }

    private void setDrawTop ( WebButtonGroup group, boolean drawTop )
    {
        if ( orientation == HORIZONTAL )
        {
            for ( Component component : group.getComponents () )
            {
                setDrawTop ( component, drawTop );
            }
        }
        else
        {
            setDrawTop ( getFirstComponent (), drawTop );
        }
    }

    private void setDrawTop ( Component component, boolean drawTop )
    {
        if ( isWebStyledButton ( component ) )
        {
            WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawTop ( drawTop );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawTop ( ( WebButtonGroup ) component, drawTop );
        }
    }

    public void setButtonsDrawLeft ( boolean drawLeft )
    {
        setDrawLeft ( WebButtonGroup.this, drawLeft );
    }

    private void setDrawLeft ( WebButtonGroup group, boolean drawLeft )
    {
        if ( orientation == VERTICAL )
        {
            for ( Component component : group.getComponents () )
            {
                setDrawLeft ( component, drawLeft );
            }
        }
        else
        {
            setDrawLeft ( getFirstComponent (), drawLeft );
        }
    }

    private void setDrawLeft ( Component component, boolean drawLeft )
    {
        if ( isWebStyledButton ( component ) )
        {
            WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawLeft ( drawLeft );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawLeft ( ( WebButtonGroup ) component, drawLeft );
        }
    }

    public void setButtonsDrawBottom ( boolean drawBottom )
    {
        setDrawBottom ( WebButtonGroup.this, drawBottom );
    }

    private void setDrawBottom ( WebButtonGroup group, boolean drawBottom )
    {
        if ( orientation == HORIZONTAL )
        {
            for ( Component component : group.getComponents () )
            {
                setDrawBottom ( component, drawBottom );
            }
        }
        else
        {
            setDrawBottom ( getLastComponent (), drawBottom );
        }
    }

    private void setDrawBottom ( Component component, boolean drawBottom )
    {
        if ( isWebStyledButton ( component ) )
        {
            WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawBottom ( drawBottom );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawBottom ( ( WebButtonGroup ) component, drawBottom );
        }
    }

    public void setButtonsDrawRight ( boolean drawRight )
    {
        setDrawRight ( WebButtonGroup.this, drawRight );
    }

    private void setDrawRight ( WebButtonGroup group, boolean drawRight )
    {
        if ( orientation == VERTICAL )
        {
            for ( Component component : group.getComponents () )
            {
                setDrawRight ( component, drawRight );
            }
        }
        else
        {
            setDrawRight ( getLastComponent (), drawRight );
        }
    }

    private void setDrawRight ( Component component, boolean drawRight )
    {
        if ( isWebStyledButton ( component ) )
        {
            WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
            ui.setDrawRight ( drawRight );
        }
        else if ( isWebButtonGroup ( component ) )
        {
            setDrawRight ( ( WebButtonGroup ) component, drawRight );
        }
    }

    public void setButtonsDrawSides ( boolean top, boolean left, boolean bottom, boolean right )
    {
        setButtonsDrawTop ( top );
        setButtonsDrawLeft ( left );
        setButtonsDrawBottom ( bottom );
        setButtonsDrawRight ( right );
    }

    public void setButtonsRolloverDarkBorderOnly ( boolean rolloverDarkBorderOnly )
    {
        setRolloverDarkBorderOnly ( WebButtonGroup.this, rolloverDarkBorderOnly );
    }

    private void setRolloverDarkBorderOnly ( WebButtonGroup group, boolean rolloverDarkBorderOnly )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverDarkBorderOnly ( rolloverDarkBorderOnly );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverDarkBorderOnly ( ( WebButtonGroup ) component, rolloverDarkBorderOnly );
            }
        }
    }

    public void setButtonsRolloverShine ( boolean rolloverShine )
    {
        setRolloverShine ( WebButtonGroup.this, rolloverShine );
    }

    private void setRolloverShine ( WebButtonGroup group, boolean rolloverShine )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverShine ( rolloverShine );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverShine ( ( WebButtonGroup ) component, rolloverShine );
            }
        }
    }

    public void setButtonsShineColor ( Color shineColor )
    {
        setShineColor ( WebButtonGroup.this, shineColor );
    }

    private void setShineColor ( WebButtonGroup group, Color shineColor )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setShineColor ( shineColor );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setShineColor ( ( WebButtonGroup ) component, shineColor );
            }
        }
    }

    public void setButtonsRound ( int round )
    {
        setRound ( WebButtonGroup.this, round );
    }

    private void setRound ( WebButtonGroup group, int round )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRound ( round );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRound ( ( WebButtonGroup ) component, round );
            }
        }
    }

    public void setButtonsRolloverShadeOnly ( boolean rolloverShadeOnly )
    {
        setRolloverShadeOnly ( WebButtonGroup.this, rolloverShadeOnly );
    }

    private void setRolloverShadeOnly ( WebButtonGroup group, boolean rolloverShadeOnly )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverShadeOnly ( rolloverShadeOnly );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverShadeOnly ( ( WebButtonGroup ) component, rolloverShadeOnly );
            }
        }
    }

    public void setButtonsShadeWidth ( int shadeWidth )
    {
        setShadeWidth ( WebButtonGroup.this, shadeWidth );
    }

    private void setShadeWidth ( WebButtonGroup group, int shadeWidth )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setShadeWidth ( shadeWidth );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setShadeWidth ( ( WebButtonGroup ) component, shadeWidth );
            }
        }
    }

    public void setButtonsShadeColor ( Color shadeColor )
    {
        setShadeColor ( WebButtonGroup.this, shadeColor );
    }

    private void setShadeColor ( WebButtonGroup group, Color shadeColor )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setShadeColor ( shadeColor );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setShadeColor ( ( WebButtonGroup ) component, shadeColor );
            }
        }
    }

    public void setButtonsInnerShadeWidth ( int innerShadeWidth )
    {
        setInnerShadeWidth ( WebButtonGroup.this, innerShadeWidth );
    }

    private void setInnerShadeWidth ( WebButtonGroup group, int innerShadeWidth )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setInnerShadeWidth ( innerShadeWidth );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setInnerShadeWidth ( ( WebButtonGroup ) component, innerShadeWidth );
            }
        }
    }

    public void setButtonsInnerShadeColor ( Color innerShadeColor )
    {
        setInnerShadeColor ( WebButtonGroup.this, innerShadeColor );
    }

    private void setInnerShadeColor ( WebButtonGroup group, Color innerShadeColor )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setInnerShadeColor ( innerShadeColor );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setInnerShadeColor ( ( WebButtonGroup ) component, innerShadeColor );
            }
        }
    }

    public void setButtonsLeftRightSpacing ( int leftRightSpacing )
    {
        setLeftRightSpacing ( WebButtonGroup.this, leftRightSpacing );
    }

    private void setLeftRightSpacing ( WebButtonGroup group, int leftRightSpacing )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setLeftRightSpacing ( leftRightSpacing );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setLeftRightSpacing ( ( WebButtonGroup ) component, leftRightSpacing );
            }
        }
    }

    public void setButtonsRolloverDecoratedOnly ( boolean rolloverDecoratedOnly )
    {
        setRolloverDecoratedOnly ( WebButtonGroup.this, rolloverDecoratedOnly );
    }

    private void setRolloverDecoratedOnly ( WebButtonGroup group, boolean rolloverDecoratedOnly )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setRolloverDecoratedOnly ( rolloverDecoratedOnly );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setRolloverDecoratedOnly ( ( WebButtonGroup ) component, rolloverDecoratedOnly );
            }
        }
    }

    public void setButtonsUndecorated ( boolean undecorated )
    {
        setUndecorated ( WebButtonGroup.this, undecorated );
    }

    private void setUndecorated ( WebButtonGroup group, boolean undecorated )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setUndecorated ( undecorated );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setUndecorated ( ( WebButtonGroup ) component, undecorated );
            }
        }
    }

    public void setButtonsPainter ( Painter painter )
    {
        setPainter ( WebButtonGroup.this, painter );
    }

    private void setPainter ( WebButtonGroup group, Painter painter )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setPainter ( painter );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setPainter ( ( WebButtonGroup ) component, painter );
            }
        }
    }

    public void setButtonsMoveIconOnPress ( boolean moveIconOnPress )
    {
        setMoveIconOnPress ( WebButtonGroup.this, moveIconOnPress );
    }

    private void setMoveIconOnPress ( WebButtonGroup group, boolean moveIconOnPress )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setMoveIconOnPress ( moveIconOnPress );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setMoveIconOnPress ( ( WebButtonGroup ) component, moveIconOnPress );
            }
        }
    }

    public void setButtonsDrawFocus ( boolean drawFocus )
    {
        setDrawFocus ( WebButtonGroup.this, drawFocus );
    }

    private void setDrawFocus ( WebButtonGroup group, boolean drawFocus )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setDrawFocus ( drawFocus );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setDrawFocus ( ( WebButtonGroup ) component, drawFocus );
            }
        }
    }

    public void setButtonsMargin ( Insets margin )
    {
        setMargin ( WebButtonGroup.this, margin );
    }

    public void setButtonsMargin ( int top, int left, int bottom, int right )
    {
        setButtonsMargin ( new Insets ( top, left, bottom, right ) );
    }

    public void setButtonsMargin ( int spacing )
    {
        setButtonsMargin ( spacing, spacing, spacing, spacing );
    }

    private void setMargin ( WebButtonGroup group, Insets margin )
    {
        for ( Component component : group.getComponents () )
        {
            if ( isWebStyledButton ( component ) )
            {
                WebButtonUI ui = ( WebButtonUI ) ( ( AbstractButton ) component ).getUI ();
                ui.setMargin ( margin );
            }
            else if ( isWebButtonGroup ( component ) )
            {
                setMargin ( ( WebButtonGroup ) component, margin );
            }
        }
    }

    @Override
    public void setEnabled ( boolean enabled )
    {
        super.setEnabled ( enabled );
        for ( Component component : getComponents () )
        {
            component.setEnabled ( enabled );
        }
    }
}
