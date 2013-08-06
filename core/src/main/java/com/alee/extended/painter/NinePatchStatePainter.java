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

package com.alee.extended.painter;

import com.alee.laf.toolbar.WebToolBar;
import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 14.12.11 Time: 16:19
 */

public class NinePatchStatePainter<E extends JComponent> extends DefaultPainter<E>
{
    private Map<String, NinePatchIcon> stateIcons;

    public NinePatchStatePainter ()
    {
        super ();
        this.stateIcons = new HashMap<String, NinePatchIcon> ();
    }

    public NinePatchStatePainter ( Map<String, NinePatchIcon> stateIcons )
    {
        super ();
        this.stateIcons = stateIcons;
    }

    /**
     * State icons methods
     */

    public Map<String, NinePatchIcon> getStateIcons ()
    {
        return stateIcons;
    }

    public void setStateIcons ( Map<String, NinePatchIcon> stateIcons )
    {
        this.stateIcons = stateIcons;
    }

    public void addStateIcon ( String componentState, NinePatchIcon ninePatchIcon )
    {
        stateIcons.put ( componentState, ninePatchIcon );
    }

    public void removeStateIcon ( ComponentState componentState )
    {
        stateIcons.remove ( componentState );
    }

    /**
     * Returns true if there is atleast one state icon available
     */

    public boolean hasStateIcons ()
    {
        return stateIcons != null && stateIcons.size () > 0;
    }

    /**
     * Paints background according to component state
     */

    public void paint ( Graphics2D g2d, Rectangle bounds, E c )
    {
        if ( hasStateIcons () && c != null )
        {
            // Current state icon retrieval
            NinePatchIcon stateIcon;
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
                NinePatchIcon focusIcon = getExactStateIcon ( ComponentState.focused );
                if ( focusIcon != null )
                {
                    focusIcon.setComponent ( c );
                    focusIcon.paintIcon ( g2d, bounds );
                }
            }
        }
    }

    /**
     * Returns true on focused component state
     */

    private boolean isFocused ( E c )
    {
        return c.isFocusOwner ();
    }

    /**
     * State icons support for any component
     */

    private NinePatchIcon getComponentBackground ( E c )
    {
        return getStateIcon ( c.isEnabled () ? ComponentState.normal : ComponentState.disabled );
    }

    /**
     * State icons support for buttons
     */

    private NinePatchIcon getButtonBackground ( AbstractButton c )
    {
        ButtonModel bm = c.getModel ();
        if ( bm.isPressed () )
        {
            return getStateIcon ( bm.isSelected () ? ComponentState.selectedPressed : ComponentState.pressed );
        }
        else if ( bm.isSelected () )
        {
            if ( bm.isEnabled () )
            {
                return getStateIcon ( bm.isRollover () ? ComponentState.selectedRollover : ComponentState.selected );
            }
            else
            {
                return getStateIcon ( ComponentState.selectedDisabled );
            }
        }
        else
        {
            if ( bm.isEnabled () )
            {
                return getStateIcon ( bm.isRollover () ? ComponentState.rollover : ComponentState.normal );
            }
            else
            {
                return getStateIcon ( ComponentState.disabled );
            }
        }
    }

    /**
     * State icons support for toolbars
     */

    private NinePatchIcon getToolBarBackground ( JToolBar c )
    {
        if ( c instanceof WebToolBar && ( ( WebToolBar ) c ).isFloating () )
        {
            return getStateIcon ( c.isEnabled () ? ComponentState.floating : ComponentState.floatingDisabled );
        }
        else
        {
            return getStateIcon ( c.isEnabled () ? ComponentState.normal : ComponentState.disabled );
        }
    }

    /**
     * Returns exact state icon even if it is null
     */

    public NinePatchIcon getExactStateIcon ( String componentState )
    {
        return stateIcons.get ( componentState );
    }

    /**
     * Returns state icon or possible replacement for it
     */

    public NinePatchIcon getStateIcon ( String componentState )
    {
        if ( componentState.equals ( ComponentState.normal ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : null;
        }
        else if ( componentState.equals ( ComponentState.rollover ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.normal );
        }
        else if ( componentState.equals ( ComponentState.disabled ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.normal );
        }
        else if ( componentState.equals ( ComponentState.pressed ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.selected );
        }
        else if ( componentState.equals ( ComponentState.selected ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.normal );
        }
        else if ( componentState.equals ( ComponentState.selectedRollover ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.selected );
        }
        else if ( componentState.equals ( ComponentState.selectedDisabled ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.selected );
        }
        else if ( componentState.equals ( ComponentState.selectedPressed ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.pressed );
        }
        else if ( componentState.equals ( ComponentState.focused ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : null;
        }
        else if ( componentState.equals ( ComponentState.floating ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.normal );
        }
        else if ( componentState.equals ( ComponentState.floatingDisabled ) )
        {
            return stateIcons.containsKey ( componentState ) ? stateIcons.get ( componentState ) : getStateIcon ( ComponentState.floating );
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns maximum preferred size according to space needed for rach state icon
     */

    public Dimension getPreferredSize ( E c )
    {
        if ( hasStateIcons () )
        {
            Dimension maxDimension = new Dimension ( 0, 0 );
            for ( NinePatchIcon npi : stateIcons.values () )
            {
                npi.setComponent ( c );
                maxDimension = SwingUtils.max ( maxDimension, npi.getPreferredSize () );
            }
            return maxDimension;
        }
        else
        {
            return super.getPreferredSize ( c );
        }
    }

    /**
     * Returns maximum margin according to space needed for rach state icon
     */

    public Insets getMargin ( E c )
    {
        Insets margin = super.getMargin ( c );
        if ( hasStateIcons () )
        {
            Insets maxInsets = new Insets ( 0, 0, 0, 0 );
            for ( NinePatchIcon npi : stateIcons.values () )
            {
                npi.setComponent ( c );
                maxInsets = SwingUtils.max ( maxInsets, npi.getMargin () );
            }
            return SwingUtils.max ( margin, maxInsets );
        }
        else
        {
            return margin;
        }
    }
}
