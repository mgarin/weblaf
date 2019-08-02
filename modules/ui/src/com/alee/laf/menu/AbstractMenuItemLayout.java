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
import com.alee.painter.decoration.layout.ContentLayoutData;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract implementation of simple menu item layout.
 * It only paints contents placed under {@link #ICON}, {@link #TEXT}, {@link #ACCELERATOR} and {@link #ARROW} constraints.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */
public abstract class AbstractMenuItemLayout<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractMenuItemLayout<C, D, I>>
        extends AbstractContentLayout<C, D, I>
{
    /**
     * Layout constraints.
     */
    protected static final String ICON = "icon";
    protected static final String TEXT = "text";
    protected static final String ACCELERATOR = "accelerator";
    protected static final String ARROW = "arrow";

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
    protected abstract boolean isAlignTextByIcons ( C c, D d );

    /**
     * Returns maximum icon width for the specified menu item.
     * It might take into account other menu items within popup menu.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return maximum icon width for the specified menu item
     */
    protected abstract int getMaxIconWidth ( C c, D d );

    /**
     * Returns gap between icon and text contents.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gap between icon and text contents
     */
    protected int getIconTextGap ( final C c, final D d )
    {
        return iconTextGap != null ? iconTextGap : 0;
    }

    /**
     * Returns between text and accelerator contents.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return between text and accelerator contents
     */
    protected int getTextAcceleratorGap ( final C c, final D d )
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
    protected int getTextArrowGap ( final C c, final D d )
    {
        return textArrowGap != null ? textArrowGap : 0;
    }

    @Override
    public ContentLayoutData layoutContent ( final C c, final D d, final Rectangle bounds )
    {
        final ContentLayoutData layoutData = new ContentLayoutData ( 4 );
        final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
        final Dimension available = new Dimension ( bounds.width, bounds.height );
        int x = ltr ? bounds.x : bounds.x + bounds.width;
        final boolean hasIcon = !isEmpty ( c, d, ICON );
        final boolean alignTextByIcons = isAlignTextByIcons ( c, d );
        if ( hasIcon || alignTextByIcons )
        {
            Dimension ips = getPreferredSize ( c, d, ICON, available );
            if ( alignTextByIcons )
            {
                ips = SwingUtils.max ( ips, new Dimension ( getMaxIconWidth ( c, d ), 0 ) );
            }
            x += ltr ? 0 : -ips.width;
            if ( hasIcon )
            {
                layoutData.put ( ICON, new Rectangle ( x, bounds.y, ips.width, bounds.height ) );
            }
            final int iconTextGap = getIconTextGap ( c, d );
            x += ltr ? ips.width + iconTextGap : -iconTextGap;
            available.width -= ips.width + iconTextGap;
        }
        if ( !isEmpty ( c, d, ARROW ) )
        {
            final Dimension aps = getPreferredSize ( c, d, ARROW, available );
            final int ax = ltr ? x + available.width - aps.width : x - available.width;
            layoutData.put ( ARROW, new Rectangle ( ax, bounds.y, aps.width, bounds.height ) );
            available.width -= aps.width + getTextArrowGap ( c, d );
        }
        if ( !isEmpty ( c, d, ACCELERATOR ) )
        {
            final Dimension aps = getPreferredSize ( c, d, ACCELERATOR, available );
            final int ax = ltr ? x + available.width - aps.width : x - available.width;
            layoutData.put ( ACCELERATOR, new Rectangle ( ax, bounds.y, aps.width, bounds.height ) );
            available.width -= aps.width + getTextAcceleratorGap ( c, d );
        }
        if ( !isEmpty ( c, d, TEXT ) )
        {
            x += ltr ? 0 : -available.width;
            layoutData.put ( TEXT, new Rectangle ( x, bounds.y, available.width, bounds.height ) );
        }
        return layoutData;
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        final Dimension ps = new Dimension ();
        if ( !isEmpty ( c, d, ICON ) || isAlignTextByIcons ( c, d ) )
        {
            Dimension ips = getPreferredSize ( c, d, ICON, available );
            if ( isAlignTextByIcons ( c, d ) )
            {
                ips = SwingUtils.max ( ips, new Dimension ( getMaxIconWidth ( c, d ), 0 ) );
            }
            ps.width += ips.width + getIconTextGap ( c, d );
            ps.height = Math.max ( ps.height, ips.height );
        }
        if ( !isEmpty ( c, d, TEXT ) )
        {
            final Dimension tps = getPreferredSize ( c, d, TEXT, available );
            ps.width += tps.width;
            ps.height = Math.max ( ps.height, tps.height );
        }
        if ( !isEmpty ( c, d, ACCELERATOR ) )
        {
            final Dimension aps = getPreferredSize ( c, d, ACCELERATOR, available );
            ps.width += aps.width + getTextAcceleratorGap ( c, d );
            ps.height = Math.max ( ps.height, aps.height );
        }
        if ( !isEmpty ( c, d, ARROW ) )
        {
            final Dimension aps = getPreferredSize ( c, d, ARROW, available );
            ps.width += aps.width + getTextArrowGap ( c, d );
            ps.height = Math.max ( ps.height, aps.height );
        }
        return ps;
    }
}