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
import javax.swing.border.Border;
import javax.swing.plaf.UIResource;
import java.awt.*;

/**
 * An implementation of LayoutStyle that returns 6 for related components, otherwise 12.
 * This class also provides helper methods for subclasses.
 */
public class DefaultLayoutStyle extends LayoutStyle
{
    private static final DefaultLayoutStyle INSTANCE = new DefaultLayoutStyle ();

    public static LayoutStyle getInstance ()
    {
        return INSTANCE;
    }

    public int getPreferredGap ( JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent )
    {

        if ( component1 == null || component2 == null || type == null )
        {
            throw new NullPointerException ();
        }
        if ( type == ComponentPlacement.INDENT && ( position == SwingConstants.EAST || position == SwingConstants.WEST ) )
        {
            int indent = getIndent ( component1, position );
            if ( indent > 0 )
            {
                return indent;
            }
        }
        return ( type == ComponentPlacement.UNRELATED ) ? 12 : 6;
    }

    public int getContainerGap ( JComponent component, int position, Container parent )
    {
        if ( component == null )
        {
            throw new NullPointerException ();
        }
        checkPosition ( position );
        return 6;
    }

    /**
     * Returns true if the classes identify a JLabel and a non-JLabel
     * along the horizontal axis.
     */
    protected boolean isLabelAndNonlabel ( JComponent c1, JComponent c2, int position )
    {
        if ( position == SwingConstants.EAST || position == SwingConstants.WEST )
        {
            boolean c1Label = ( c1 instanceof JLabel );
            boolean c2Label = ( c2 instanceof JLabel );
            return ( ( c1Label || c2Label ) && ( c1Label != c2Label ) );
        }
        return false;
    }

    /**
     * For some look and feels check boxs and radio buttons typically
     * don't paint the border, yet they have padding for a border.  Look
     * and feel guidelines generally don't include this space.  Use
     * this method to subtract this space from the specified
     * components.
     *
     * @param source   First component
     * @param target   Second component
     * @param position Position doing layout along.
     * @param offset   Ideal offset, not including border/margin
     * @return offset - border/margin around the component.
     */
    protected int getButtonGap ( JComponent source, JComponent target, int position, int offset )
    {
        offset -= getButtonGap ( source, position );
        if ( offset > 0 )
        {
            offset -= getButtonGap ( target, flipDirection ( position ) );
        }
        if ( offset < 0 )
        {
            return 0;
        }
        return offset;
    }

    /**
     * For some look and feels check boxs and radio buttons typically
     * don't paint the border, yet they have padding for a border.  Look
     * and feel guidelines generally don't include this space.  Use
     * this method to subtract this space from the specified
     * components.
     *
     * @param source   Component
     * @param position Position doing layout along.
     * @param offset   Ideal offset, not including border/margin
     * @return offset - border/margin around the component.
     */
    protected int getButtonGap ( JComponent source, int position, int offset )
    {
        offset -= getButtonGap ( source, position );
        return Math.max ( offset, 0 );
    }

    /**
     * If <code>c</code> is a check box or radio button, and the border is
     * not painted this returns the inset along the specified axis.
     */
    public int getButtonGap ( JComponent c, int position )
    {
        String classID = c.getUIClassID ();
        if ( ( classID.equals ( "CheckBoxUI" ) || classID.equals ( "RadioButtonUI" ) ) && !( ( AbstractButton ) c ).isBorderPainted () )
        {
            Border border = c.getBorder ();
            if ( border instanceof UIResource )
            {
                return getInset ( c, position );
            }
        }
        return 0;
    }

    private void checkPosition ( int position )
    {
        if ( position != SwingConstants.NORTH &&
                position != SwingConstants.SOUTH &&
                position != SwingConstants.WEST &&
                position != SwingConstants.EAST )
        {
            throw new IllegalArgumentException ();
        }
    }

    protected int flipDirection ( int position )
    {
        switch ( position )
        {
            case SwingConstants.NORTH:
                return SwingConstants.SOUTH;
            case SwingConstants.SOUTH:
                return SwingConstants.NORTH;
            case SwingConstants.EAST:
                return SwingConstants.WEST;
            case SwingConstants.WEST:
                return SwingConstants.EAST;
        }
        assert false;
        return 0;
    }

    /**
     * Returns the amount to indent the specified component if it's
     * a JCheckBox or JRadioButton.  If the component is not a JCheckBox or
     * JRadioButton, 0 will be returned.
     */
    protected int getIndent ( JComponent c, int position )
    {
        String classID = c.getUIClassID ();
        if ( classID.equals ( "CheckBoxUI" ) || classID.equals ( "RadioButtonUI" ) )
        {
            AbstractButton button = ( AbstractButton ) c;
            Insets insets = c.getInsets ();
            Icon icon = getIcon ( button );
            int gap = button.getIconTextGap ();
            if ( isLeftAligned ( button, position ) )
            {
                return insets.left + icon.getIconWidth () + gap;
            }
            else if ( isRightAligned ( button, position ) )
            {
                return insets.right + icon.getIconWidth () + gap;
            }
        }
        return 0;
    }

    private Icon getIcon ( AbstractButton button )
    {
        Icon icon = button.getIcon ();
        if ( icon != null )
        {
            return icon;
        }
        String key = null;
        if ( button instanceof JCheckBox )
        {
            key = "CheckBox.icon";
        }
        else if ( button instanceof JRadioButton )
        {
            key = "RadioButton.icon";
        }
        if ( key != null )
        {
            Object oIcon = UIManager.get ( key );
            if ( oIcon instanceof Icon )
            {
                return ( Icon ) oIcon;
            }
        }
        return null;
    }

    private boolean isLeftAligned ( AbstractButton button, int position )
    {
        if ( position == SwingConstants.WEST )
        {
            boolean ltr = button.getComponentOrientation ().isLeftToRight ();
            int hAlign = button.getHorizontalAlignment ();
            return ( ( ltr && ( hAlign == SwingConstants.LEFT || hAlign == SwingConstants.LEADING ) ) ||
                    ( !ltr && ( hAlign == SwingConstants.TRAILING ) ) );
        }
        return false;
    }

    private boolean isRightAligned ( AbstractButton button, int position )
    {
        if ( position == SwingConstants.EAST )
        {
            boolean ltr = button.getComponentOrientation ().isLeftToRight ();
            int hAlign = button.getHorizontalAlignment ();
            return ( ( ltr && ( hAlign == SwingConstants.RIGHT || hAlign == SwingConstants.TRAILING ) ) ||
                    ( !ltr && ( hAlign == SwingConstants.LEADING ) ) );
        }
        return false;
    }

    private int getInset ( JComponent c, int position )
    {
        return getInset ( c.getInsets (), position );
    }

    private int getInset ( Insets insets, int position )
    {
        if ( insets == null )
        {
            return 0;
        }
        switch ( position )
        {
            case SwingConstants.NORTH:
                return insets.top;
            case SwingConstants.SOUTH:
                return insets.bottom;
            case SwingConstants.EAST:
                return insets.right;
            case SwingConstants.WEST:
                return insets.left;
        }
        assert false;
        return 0;
    }
}