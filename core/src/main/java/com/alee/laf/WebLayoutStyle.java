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
 * @since 1.4
 */

public class WebLayoutStyle extends DefaultLayoutStyle
{
    /**
     * Layout style instance.
     */
    public static WebLayoutStyle INSTANCE = new WebLayoutStyle ();

    /**
     * Returns the amount of space to use between two components.
     * The return value indicates the distance to place
     * <code>component2</code> relative to <code>component1</code>.
     * For example, the following returns the amount of space to place
     * between <code>component2</code> and <code>component1</code>
     * when <code>component2</code> is placed vertically above
     * <code>component1</code>:
     * <pre>
     *   int gap = getPreferredGap(component1, component2,
     *                             ComponentPlacement.RELATED,
     *                             SwingConstants.NORTH, parent);
     * </pre>
     * The <code>type</code> parameter indicates the relation between
     * the two components.  If the two components will be contained in
     * the same parent and are showing similar logically related
     * items, use <code>RELATED</code>.  If the two components will be
     * contained in the same parent but show logically unrelated items
     * use <code>UNRELATED</code>.  Some look and feels may not
     * distinguish between the <code>RELATED</code> and
     * <code>UNRELATED</code> types.
     * <p/>
     * The return value is not intended to take into account the
     * current size and position of <code>component2</code> or
     * <code>component1</code>.  The return value may take into
     * consideration various properties of the components.  For
     * example, the space may vary based on font size, or the preferred
     * size of the component.
     *
     * @param component1 the <code>JComponent</code>
     *                   <code>component2</code> is being placed relative to
     * @param component2 the <code>JComponent</code> being placed
     * @param position   the position <code>component2</code> is being placed
     *                   relative to <code>component1</code>; one of
     *                   <code>SwingConstants.NORTH</code>,
     *                   <code>SwingConstants.SOUTH</code>,
     *                   <code>SwingConstants.EAST</code> or
     *                   <code>SwingConstants.WEST</code>
     * @param type       how the two components are being placed
     * @param parent     the parent of <code>component2</code>; this may differ
     *                   from the actual parent and it may be <code>null</code>
     * @return the amount of space to place between the two components
     * @throws NullPointerException     if <code>component1</code>,
     *                                  <code>component2</code> or <code>type</code> is
     *                                  <code>null</code>
     * @throws IllegalArgumentException if <code>position</code> is not
     *                                  one of <code>SwingConstants.NORTH</code>,
     *                                  <code>SwingConstants.SOUTH</code>,
     *                                  <code>SwingConstants.EAST</code> or
     *                                  <code>SwingConstants.WEST</code>
     * @see LookAndFeel#getLayoutStyle
     * @since 1.6
     */
    public int getPreferredGap ( JComponent component1, JComponent component2, ComponentPlacement type, int position, Container parent )
    {
        super.getPreferredGap ( component1, component2, type, position, parent );

        int offset = 0;
        switch ( type )
        {
            case INDENT:
                if ( position == SwingConstants.EAST || position == SwingConstants.WEST )
                {
                    int indent = getIndent ( component1, position );
                    if ( indent > 0 )
                    {
                        return indent;
                    }
                    return 12;
                }
            case RELATED:
                if ( component1.getUIClassID ().equals ( "ToggleButtonUI" ) && component2.getUIClassID ().equals ( "ToggleButtonUI" ) )
                {
                    ButtonModel sourceModel = ( ( JToggleButton ) component1 ).
                            getModel ();
                    ButtonModel targetModel = ( ( JToggleButton ) component2 ).
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

    /**
     * Returns the amount of space to place between the component and specified
     * edge of its parent.
     *
     * @param component the <code>JComponent</code> being positioned
     * @param position  the position <code>component</code> is being placed
     *                  relative to its parent; one of
     *                  <code>SwingConstants.NORTH</code>,
     *                  <code>SwingConstants.SOUTH</code>,
     *                  <code>SwingConstants.EAST</code> or
     *                  <code>SwingConstants.WEST</code>
     * @param parent    the parent of <code>component</code>; this may differ
     *                  from the actual parent and may be <code>null</code>
     * @return the amount of space to place between the component and specified
     *         edge
     * @throws IllegalArgumentException if <code>position</code> is not
     *                                  one of <code>SwingConstants.NORTH</code>,
     *                                  <code>SwingConstants.SOUTH</code>,
     *                                  <code>SwingConstants.EAST</code> or
     *                                  <code>SwingConstants.WEST</code>
     */
    public int getContainerGap ( JComponent component, int position, Container parent )
    {
        super.getContainerGap ( component, position, parent );
        return getButtonGap ( component, position, 12 - getButtonAdjustment ( component, position ) );
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
    private int getButtonAdjustment ( JComponent source, int edge )
    {
        String classID = source.getUIClassID ();
        if ( classID.equals ( "ButtonUI" ) || classID.equals ( "ToggleButtonUI" ) )
        {
            if ( ( edge == SwingConstants.EAST || edge == SwingConstants.SOUTH ) )
            {
                if ( source.getBorder () instanceof UIResource )
                {
                    return 1;
                }
            }
        }
        else if ( edge == SwingConstants.SOUTH )
        {
            if ( ( classID.equals ( "RadioButtonUI" ) || classID.equals ( "CheckBoxUI" ) ) )
            {
                return 1;
            }
        }
        return 0;
    }
}