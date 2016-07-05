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

package com.alee.laf.menu;

import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.layout.AbstractContentLayout;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract implementation of simple menu item layout.
 * It only paints contents placed under {@link #ICON}, {@link #TEXT}, {@link #ACCELERATOR} and {@link #ARROW} constraints.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */

@XStreamAlias ( "MenuItemLayout" )
public class MenuItemContentLayout<E extends JMenuItem, D extends IDecoration<E, D>, I extends MenuItemContentLayout<E, D, I>>
        extends AbstractContentLayout<E, D, I>
{
    /**
     * Layout constraints.
     */
    public static final String ICON = "icon";
    public static final String TEXT = "text";
    public static final String ACCELERATOR = "accelerator";
    public static final String ARROW = "arrow";

    /**
     * Whether or not menu items text should be aligned by maximum icon size.
     */
    @XStreamAsAttribute
    protected Boolean alignTextByIcons;

    /**
     * Gap between icon and text contents.
     */
    @XStreamAsAttribute
    protected Integer iconTextGap;

    /**
     * Gap between text and accelerator contents.
     */
    @XStreamAsAttribute
    protected Integer textAcceleratorGap;

    /**
     * Gap between text and arrow contents.
     */
    @XStreamAsAttribute
    protected Integer textArrowGap;

    /**
     * Returns whether or not menu items text should be aligned by maximum icon size.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if menu items text should be aligned by maximum icon size, false otherwise
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected boolean isAlignTextByIcons ( final E c, final D d )
    {
        return alignTextByIcons == null || alignTextByIcons;
    }

    /**
     * Returns gap between icon and text contents.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gap between icon and text contents
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int getIconTextGap ( final E c, final D d )
    {
        return iconTextGap != null ? iconTextGap : c.getIconTextGap ();
    }

    /**
     * Returns between text and accelerator contents.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return between text and accelerator contents
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int getTextAcceleratorGap ( final E c, final D d )
    {
        return textAcceleratorGap != null ? textAcceleratorGap : 0;
    }

    /**
     * Returns between text and arrow contents.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return between text and arrow contents
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int getTextArrowGap ( final E c, final D d )
    {
        return textArrowGap != null ? textArrowGap : 0;
    }

    /**
     * Returns maximum icon width for the specified menu item.
     * It might take into account other menu items within popup menu.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return maximum icon width for the specified menu item
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected int getMaxIconWidth ( final E c, final D d )
    {
        if ( isAlignTextByIcons ( c, d ) && c.getParent () instanceof JPopupMenu )
        {
            int max = 0;
            final JPopupMenu popupMenu = ( JPopupMenu ) c.getParent ();
            for ( int i = 0; i < popupMenu.getComponentCount (); i++ )
            {
                final Component component = popupMenu.getComponent ( i );
                if ( component instanceof JMenuItem )
                {
                    final AbstractButton otherItem = ( AbstractButton ) component;
                    if ( otherItem.getIcon () != null )
                    {
                        max = Math.max ( max, otherItem.getIcon ().getIconWidth () );
                    }
                }
            }
            return max;
        }
        else
        {
            final Icon icon = c.getIcon ();
            return icon != null ? icon.getIconWidth () : 0;
        }
    }

    /**
     * Returns whether or not the specified menu item is placed within popup menu.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if the specified menu item is placed within popup menu, false otherwise
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected boolean isInPopupMenu ( final E c, final D d )
    {
        return c.getParent () != null && c.getParent () instanceof JPopupMenu;
    }

    /**
     * Returns whether or not the specified menu item has accelerator.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if the specified menu item has accelerator, false otherwise
     */
    @SuppressWarnings ( "UnusedParameters" )
    protected boolean hasAccelerator ( final E c, final D d )
    {
        return c.getAccelerator () != null;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final Dimension available = new Dimension ( bounds.width, bounds.height );
        int x = ltr ? bounds.x : bounds.x + bounds.width;
        final boolean hasIcon = !isEmpty ( c, d, ICON );
        final boolean alignTextByIcons = isAlignTextByIcons ( c, d );
        if ( hasIcon || alignTextByIcons )
        {
            Dimension ips = getPreferredSize ( c, d, available, ICON );
            if ( alignTextByIcons )
            {
                ips = SwingUtils.max ( ips, new Dimension ( getMaxIconWidth ( c, d ), 0 ) );
            }
            x += ltr ? 0 : -ips.width;
            if ( hasIcon )
            {
                paint ( g2d, new Rectangle ( x, bounds.y, ips.width, bounds.height ), c, d, ICON );
            }
            final int iconTextGap = getIconTextGap ( c, d );
            x += ltr ? ips.width + iconTextGap : -iconTextGap;
            available.width -= ips.width + iconTextGap;
        }
        if ( isInPopupMenu ( c, d ) )
        {
            if ( !isEmpty ( c, d, ARROW ) )
            {
                final Dimension aps = getPreferredSize ( c, d, available, ARROW );
                final int ax = ltr ? x + available.width - aps.width : x - available.width;
                paint ( g2d, new Rectangle ( ax, bounds.y, aps.width, bounds.height ), c, d, ARROW );
                available.width -= aps.width + getTextArrowGap ( c, d );
            }
            if ( hasAccelerator ( c, d ) && !isEmpty ( c, d, ACCELERATOR ) )
            {
                final Dimension aps = getPreferredSize ( c, d, available, ACCELERATOR );
                final int ax = ltr ? x + available.width - aps.width : x - available.width;
                paint ( g2d, new Rectangle ( ax, bounds.y, aps.width, bounds.height ), c, d, ACCELERATOR );
                available.width -= aps.width + getTextAcceleratorGap ( c, d );
            }
        }
        if ( !isEmpty ( c, d, TEXT ) )
        {
            x += ltr ? 0 : -available.width;
            paint ( g2d, new Rectangle ( x, bounds.y, available.width, bounds.height ), c, d, TEXT );
        }
    }

    @Override
    protected Dimension getContentPreferredSize ( final E c, final D d, final Dimension available )
    {
        final Dimension ps = new Dimension ();
        if ( !isEmpty ( c, d, ICON ) || isAlignTextByIcons ( c, d ) )
        {
            Dimension ips = getPreferredSize ( c, d, available, ICON );
            if ( isAlignTextByIcons ( c, d ) )
            {
                ips = SwingUtils.max ( ips, new Dimension ( getMaxIconWidth ( c, d ), 0 ) );
            }
            ps.width += ips.width + getIconTextGap ( c, d );
            ps.height = Math.max ( ps.height, ips.height );
        }
        if ( !isEmpty ( c, d, TEXT ) )
        {
            final Dimension tps = getPreferredSize ( c, d, available, TEXT );
            ps.width += tps.width;
            ps.height = Math.max ( ps.height, tps.height );
        }
        if ( isInPopupMenu ( c, d ) )
        {
            if ( hasAccelerator ( c, d ) && !isEmpty ( c, d, ACCELERATOR ) )
            {
                final Dimension aps = getPreferredSize ( c, d, available, ACCELERATOR );
                ps.width += aps.width + getTextAcceleratorGap ( c, d );
                ps.height = Math.max ( ps.height, aps.height );
            }
            if ( !isEmpty ( c, d, ARROW ) )
            {
                final Dimension aps = getPreferredSize ( c, d, available, ARROW );
                ps.width += aps.width + getTextArrowGap ( c, d );
                ps.height = Math.max ( ps.height, aps.height );
            }
        }
        return ps;
    }

    @Override
    public I merge ( final I layout )
    {
        super.merge ( layout );
        alignTextByIcons =
                isOverwrite () ? layout.alignTextByIcons : layout.alignTextByIcons != null ? layout.alignTextByIcons : alignTextByIcons;
        iconTextGap = isOverwrite () ? layout.iconTextGap : layout.iconTextGap != null ? layout.iconTextGap : iconTextGap;
        textAcceleratorGap = isOverwrite () ? layout.textAcceleratorGap :
                layout.textAcceleratorGap != null ? layout.textAcceleratorGap : textAcceleratorGap;
        textArrowGap = isOverwrite () ? layout.textArrowGap : layout.textArrowGap != null ? layout.textArrowGap : textArrowGap;
        return ( I ) this;
    }
}