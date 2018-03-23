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

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * Almost the same as the MetalLookAndFeel LayoutStyle class as it fits WebLaF perfectly.
 *
 * @author Mikle Garin
 */

public class WebLayoutStyle extends DefaultLayoutStyle
{
    /**
     * Layout style instance.
     */
    private static WebLayoutStyle instance;

    /**
     * Returns instance of this layout style.
     *
     * @return instance of this layout style
     */
    public static WebLayoutStyle getInstance ()
    {
        if ( instance == null )
        {
            instance = new WebLayoutStyle ();
        }
        return instance;
    }

    @Override
    public int getPreferredGap ( final JComponent component1, final JComponent component2, final ComponentPlacement type,
                                 final int position, final Container container )
    {
        super.getPreferredGap ( component1, component2, type, position, container );

        int offset = 0;
        switch ( type )
        {
            case INDENT:
                if ( position == SwingConstants.EAST || position == SwingConstants.WEST )
                {
                    final int indent = getIndent ( component1, position );
                    if ( indent > 0 )
                    {
                        return indent;
                    }
                    return 12;
                }
            case RELATED:
                if ( component1.getUIClassID ().equals ( "ToggleButtonUI" ) && component2.getUIClassID ().equals ( "ToggleButtonUI" ) )
                {
                    final ButtonModel sourceModel = ( ( JToggleButton ) component1 ).
                            getModel ();
                    final ButtonModel targetModel = ( ( JToggleButton ) component2 ).
                            getModel ();
                    if ( ( sourceModel instanceof DefaultButtonModel ) &&
                            ( targetModel instanceof DefaultButtonModel ) &&
                            ( ( ( DefaultButtonModel ) sourceModel ).getGroup () == ( ( DefaultButtonModel ) targetModel ).getGroup () ) &&
                            ( ( DefaultButtonModel ) sourceModel ).getGroup () != null )
                    {
                        return 2;
                    }
                    return 5;
                }
                offset = 6;
                break;
            case UNRELATED:
                offset = 12;
                break;
        }
        if ( isLabelAndNonlabel ( component1, component2, position ) )
        {
            return getButtonGap ( component1, component2, position, offset + 6 );
        }
        return getButtonGap ( component1, component2, position, offset );
    }

    @Override
    public int getContainerGap ( final JComponent component, final int position, final Container container )
    {
        super.getContainerGap ( component, position, container );
        return getButtonGap ( component, position, 12 - getButtonAdjustment ( component, position ) );
    }

    @Override
    protected int getButtonGap ( final JComponent source, final JComponent target, final int position, int offset )
    {
        offset = super.getButtonGap ( source, target, position, offset );
        if ( offset > 0 )
        {
            int buttonAdjustment = getButtonAdjustment ( source, position );
            if ( buttonAdjustment == 0 )
            {
                buttonAdjustment = getButtonAdjustment ( target, flipDirection ( position ) );
            }
            offset -= buttonAdjustment;
        }
        if ( offset < 0 )
        {
            return 0;
        }
        return offset;
    }

    /**
     * Returns button adjustment.
     *
     * @param source First component
     * @param edge   button edge
     * @return button adjustment
     */
    private int getButtonAdjustment ( final JComponent source, final int edge )
    {
        final String classID = source.getUIClassID ();
        if ( classID.equals ( "ButtonUI" ) || classID.equals ( "ToggleButtonUI" ) )
        {
            if ( edge == SwingConstants.EAST || edge == SwingConstants.SOUTH )
            {
                if ( source.getBorder () instanceof UIResource )
                {
                    return 1;
                }
            }
        }
        else if ( edge == SwingConstants.SOUTH )
        {
            if ( classID.equals ( "RadioButtonUI" ) || classID.equals ( "CheckBoxUI" ) )
            {
                return 1;
            }
        }
        return 0;
    }
}