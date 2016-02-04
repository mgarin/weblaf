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

package com.alee.painter.common;

import com.alee.painter.AbstractPainter;
import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicToolBarUI;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This painter class allows you to specify multiply 9-patch images for different component states.
 * By default there is support for some standard Swing component states like buttons.
 *
 * @param <E> component type
 * @author Mikle Garin
 * @see NinePatchState
 * @see com.alee.utils.ninepatch.NinePatchIcon
 * @see NinePatchIconPainter
 * @see com.alee.painter.AbstractPainter
 * @see com.alee.painter.Painter
 */

public class NinePatchStatePainter<E extends JComponent, U extends ComponentUI> extends AbstractPainter<E, U>
{
    /**
     * todo 1. Move each component support to separate classes?
     */

    /**
     * Map containing separate 9-patch icons for different component states.
     */
    protected Map<String, NinePatchIcon> stateIcons;

    /**
     * Constructs new 9-patch state painter with empty states.
     */
    public NinePatchStatePainter ()
    {
        super ();
        this.stateIcons = new HashMap<String, NinePatchIcon> ();
    }

    /**
     * Constructs new 9-patch state painter with specified state icons.
     *
     * @param stateIcons state icons map
     */
    public NinePatchStatePainter ( final Map<String, NinePatchIcon> stateIcons )
    {
        super ();
        this.stateIcons = stateIcons;
    }

    /**
     * Returns states map.
     *
     * @return states map
     */
    public Map<String, NinePatchIcon> getStateIcons ()
    {
        return stateIcons;
    }

    /**
     * Sets states map.
     *
     * @param stateIcons states map
     */
    public void setStateIcons ( final Map<String, NinePatchIcon> stateIcons )
    {
        this.stateIcons = stateIcons;
        updateAll ();
    }

    /**
     * Adds painter state.
     *
     * @param state         state to add
     * @param ninePatchIcon 9-patch icon
     */
    public void addStateIcon ( final String state, final NinePatchIcon ninePatchIcon )
    {
        stateIcons.put ( state, ninePatchIcon );
        updateAll ();
    }

    /**
     * Removes painter state.
     *
     * @param state state to remove
     */
    public void removeStateIcon ( final String state )
    {
        stateIcons.remove ( state );
        updateAll ();
    }

    /**
     * Returns whether at least one state icon is available or not.
     *
     * @return true if at least one state icon is available, false otherwise
     */
    public boolean hasStateIcons ()
    {
        return stateIcons != null && stateIcons.size () > 0;
    }

    /**
     * Returns whether component is in focused state or not.
     *
     * @param component component to process
     * @return true if component is in focused state, false otherwise
     */
    protected boolean isFocused ( final E component )
    {
        return component.isFocusOwner ();
    }

    /**
     * Returns current state icon for the specified component.
     *
     * @param component component to process
     * @return current state icon
     */
    protected NinePatchIcon getComponentBackground ( final E component )
    {
        return getStateIcon ( component.isEnabled () ? NinePatchState.normal : NinePatchState.disabled );
    }

    /**
     * Returns current state icon for the specified button.
     *
     * @param button button to process
     * @return current state icon
     */
    protected NinePatchIcon getButtonBackground ( final AbstractButton button )
    {
        final ButtonModel bm = button.getModel ();
        if ( bm.isPressed () )
        {
            return getStateIcon ( bm.isSelected () ? NinePatchState.selectedPressed : NinePatchState.pressed );
        }
        else if ( bm.isSelected () )
        {
            if ( bm.isEnabled () )
            {
                return getStateIcon ( bm.isRollover () ? NinePatchState.selectedRollover : NinePatchState.selected );
            }
            else
            {
                return getStateIcon ( NinePatchState.selectedDisabled );
            }
        }
        else
        {
            if ( bm.isEnabled () )
            {
                return getStateIcon ( bm.isRollover () ? NinePatchState.rollover : NinePatchState.normal );
            }
            else
            {
                return getStateIcon ( NinePatchState.disabled );
            }
        }
    }

    /**
     * Returns current state icon for the specified toolbar.
     *
     * @param toolbar toolbar to process
     * @return current state icon
     */
    protected NinePatchIcon getToolBarBackground ( final JToolBar toolbar )
    {
        if ( toolbar.getUI () instanceof BasicToolBarUI && ( ( BasicToolBarUI ) toolbar.getUI () ).isFloating () )
        {
            return getStateIcon ( toolbar.isEnabled () ? NinePatchState.floating : NinePatchState.floatingDisabled );
        }
        else
        {
            return getStateIcon ( toolbar.isEnabled () ? NinePatchState.normal : NinePatchState.disabled );
        }
    }

    /**
     * Returns exact state icon or null if it is not specified.
     *
     * @param state component state
     * @return exact state icon or null if it is not specified
     */
    public NinePatchIcon getExactStateIcon ( final String state )
    {
        return stateIcons.get ( state );
    }

    /**
     * Returns state icon or possible replacement for it.
     *
     * @param state component state
     * @return state icon or possible replacement for it
     */
    public NinePatchIcon getStateIcon ( final String state )
    {
        if ( state.equals ( NinePatchState.normal ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : null;
        }
        else if ( state.equals ( NinePatchState.rollover ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.normal );
        }
        else if ( state.equals ( NinePatchState.disabled ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.normal );
        }
        else if ( state.equals ( NinePatchState.pressed ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.selected );
        }
        else if ( state.equals ( NinePatchState.selected ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.normal );
        }
        else if ( state.equals ( NinePatchState.selectedRollover ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.selected );
        }
        else if ( state.equals ( NinePatchState.selectedDisabled ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.selected );
        }
        else if ( state.equals ( NinePatchState.selectedPressed ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.pressed );
        }
        else if ( state.equals ( NinePatchState.focused ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : null;
        }
        else if ( state.equals ( NinePatchState.floating ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.normal );
        }
        else if ( state.equals ( NinePatchState.floatingDisabled ) )
        {
            return stateIcons.containsKey ( state ) ? stateIcons.get ( state ) : getStateIcon ( NinePatchState.floating );
        }
        else
        {
            return null;
        }
    }

    @Override
    public Insets getBorders ()
    {
        final Insets margin = super.getBorders ();
        if ( hasStateIcons () )
        {
            Insets maxInsets = i ( 0, 0, 0, 0 );
            for ( final NinePatchIcon npi : stateIcons.values () )
            {
                npi.setComponent ( component );
                maxInsets = SwingUtils.max ( maxInsets, npi.getMargin () );
            }
            return SwingUtils.max ( margin, maxInsets );
        }
        else
        {
            return margin;
        }
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        if ( hasStateIcons () && c != null )
        {
            // Current state icon retrieval
            final NinePatchIcon stateIcon;
            if ( c instanceof AbstractButton )
            {
                // Button component
                stateIcon = getButtonBackground ( ( AbstractButton ) c );
            }
            else if ( c instanceof JToolBar )
            {
                // Toolbar component
                stateIcon = getToolBarBackground ( ( JToolBar ) c );
            }
            else
            {
                // Any component
                stateIcon = getComponentBackground ( c );
            }

            // Draw background
            if ( stateIcon != null )
            {
                stateIcon.setComponent ( c );
                stateIcon.paintIcon ( g2d, bounds );
            }

            // Draw focus
            if ( isFocused ( c ) )
            {
                final NinePatchIcon focusIcon = getExactStateIcon ( NinePatchState.focused );
                if ( focusIcon != null )
                {
                    focusIcon.setComponent ( c );
                    focusIcon.paintIcon ( g2d, bounds );
                }
            }
        }
    }

    @Override
    public Dimension getPreferredSize ()
    {
        if ( hasStateIcons () )
        {
            Dimension maxDimension = new Dimension ( 0, 0 );
            for ( final NinePatchIcon npi : stateIcons.values () )
            {
                npi.setComponent ( component );
                maxDimension = SwingUtils.max ( maxDimension, npi.getPreferredSize () );
            }
            return maxDimension;
        }
        else
        {
            return super.getPreferredSize ();
        }
    }
}