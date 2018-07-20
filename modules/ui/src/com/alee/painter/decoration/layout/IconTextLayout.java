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

package com.alee.painter.decoration.layout;

import com.alee.api.data.BoxOrientation;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.MathUtils;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract implementation of simple icon and text layout.
 * Main constraints are {@link #ICON} and {@link #TEXT} which are always placed in the middle of layout.
 * Side spacing constraints are also supported which place contents similar to {@link BorderLayout}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */
@XStreamAlias ( "IconTextLayout" )
public class IconTextLayout<C extends JComponent, D extends IDecoration<C, D>, I extends IconTextLayout<C, D, I>>
        extends AbstractContentLayout<C, D, I> implements SwingConstants
{
    /**
     * Layout constraints.
     */
    protected static final String ICON = "icon";
    protected static final String TEXT = "text";
    protected static final String TOP_SPACE = "top-space";
    protected static final String LEFT_SPACE = "left-space";
    protected static final String RIGHT_SPACE = "right-space";
    protected static final String BOTTOM_SPACE = "bottom-space";

    /**
     * Gap between icon and text contents.
     */
    @XStreamAsAttribute
    protected Integer gap;

    /**
     * Horizontal content alignment.
     */
    @XStreamAsAttribute
    protected BoxOrientation halign;

    /**
     * Vertical content alignment.
     */
    @XStreamAsAttribute
    protected BoxOrientation valign;

    /**
     * Horizontal text position.
     */
    @XStreamAsAttribute
    protected BoxOrientation hpos;

    /**
     * Vertical text position.
     */
    @XStreamAsAttribute
    protected BoxOrientation vpos;

    /**
     * Returns gap between icon and text contents.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gap between icon and text contents
     */
    protected int getIconTextGap ( final C c, final D d )
    {
        return gap != null ? gap : 0;
    }

    /**
     * Returns horizontal content alignment.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return horizontal content alignment
     */
    protected int getHorizontalAlignment ( final C c, final D d )
    {
        return halign != null ? halign.getValue () : CENTER;
    }

    /**
     * Returns vertical content alignment.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return vertical content alignment
     */
    protected int getVerticalAlignment ( final C c, final D d )
    {
        return valign != null ? valign.getValue () : CENTER;
    }

    /**
     * Returns horizontal text position.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return horizontal text position
     */
    protected int getHorizontalTextPosition ( final C c, final D d )
    {
        return hpos != null ? hpos.getValue () : TRAILING;
    }

    /**
     * Returns vertical text position.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return vertical text position
     */
    protected int getVerticalTextPosition ( final C c, final D d )
    {
        return vpos != null ? vpos.getValue () : CENTER;
    }

    @Override
    public ContentLayoutData layoutContent ( final C c, final D d, final Rectangle bounds )
    {
        final ContentLayoutData layoutData = new ContentLayoutData ( 2 );

        final Dimension available = bounds.getSize ();
        final boolean ltr = isLeftToRight ( c, d );
        final int halign = getHorizontalAlignment ( c, d );
        final int valign = getVerticalAlignment ( c, d );

        // Retrieving spacing preferred sizes
        final Dimension top = getPreferredSize ( c, d, TOP_SPACE, available );
        final Dimension left = getPreferredSize ( c, d, LEFT_SPACE, available );
        final Dimension right = getPreferredSize ( c, d, RIGHT_SPACE, available );
        final Dimension bottom = getPreferredSize ( c, d, BOTTOM_SPACE, available );

        // Calculating actual spacing sizes
        // We have to find maximum of two in case axis alignment for main content is CENTER
        final int lw = halign == CENTER ? Math.max ( left.width, right.width ) : left.width;
        final int rw = halign == CENTER ? Math.max ( left.width, right.width ) : right.width;
        final int th = valign == CENTER ? Math.max ( top.height, bottom.height ) : top.height;
        final int bh = valign == CENTER ? Math.max ( top.height, bottom.height ) : bottom.height;

        // Calculating icon and text preferred size
        final Dimension iconTextAvailable = new Dimension ( available.width - lw - rw, available.height - th - bh );
        final Dimension iconTextPS = getIconTextPreferredSize ( c, d, iconTextAvailable );

        // Adjusting available size
        iconTextPS.width = Math.max ( 0, Math.min ( iconTextPS.width, bounds.width - lw - rw ) );
        iconTextPS.height = Math.max ( 0, Math.min ( iconTextPS.height, bounds.height - th - bh ) );

        // Calculating smallest content bounds required for text and icon
        final Rectangle b = new Rectangle ( 0, 0, iconTextPS.width, iconTextPS.height );
        if ( halign == LEFT || halign == LEADING && ltr || halign == TRAILING && !ltr )
        {
            b.x = bounds.x + lw;
        }
        else if ( halign == CENTER )
        {
            b.x = bounds.x + bounds.width / 2 - iconTextPS.width / 2;
        }
        else
        {
            b.x = bounds.x + bounds.width - iconTextPS.width - rw;
        }
        if ( valign == TOP )
        {
            b.y = bounds.y + th;
        }
        else if ( valign == CENTER )
        {
            b.y = bounds.y + bounds.height / 2 - iconTextPS.height / 2;
        }
        else
        {
            b.y = bounds.y + bounds.height - iconTextPS.height - bh;
        }

        // Calculating spacings positioning
        if ( !isEmpty ( c, d, TOP_SPACE ) )
        {
            layoutData.put ( TOP_SPACE, new Rectangle ( bounds.x, bounds.y,
                    bounds.width, b.y - bounds.y ) );
        }
        if ( !isEmpty ( c, d, LEFT_SPACE ) )
        {
            layoutData.put ( LEFT_SPACE, new Rectangle ( bounds.x, b.y,
                    b.x - bounds.x, b.height ) );
        }
        if ( !isEmpty ( c, d, RIGHT_SPACE ) )
        {
            layoutData.put ( RIGHT_SPACE, new Rectangle ( b.x + b.width, b.y,
                    bounds.x + bounds.width - b.x - b.width, b.height ) );
        }
        if ( !isEmpty ( c, d, BOTTOM_SPACE ) )
        {
            layoutData.put ( BOTTOM_SPACE, new Rectangle ( bounds.x, b.y + b.height,
                    bounds.width, bounds.y + bounds.height - b.y - b.height ) );
        }

        // Calculating text and icon positioning
        final boolean hasIcon = !isEmpty ( c, d, ICON );
        final boolean hasText = !isEmpty ( c, d, TEXT );
        if ( hasIcon && hasText )
        {
            final int hpos = getHorizontalTextPosition ( c, d );
            final int vpos = getVerticalTextPosition ( c, d );
            if ( hpos != CENTER || vpos != CENTER )
            {
                final Dimension ips = getPreferredSize ( c, d, ICON, available );
                final int gap = getIconTextGap ( c, d );
                if ( hpos == RIGHT || hpos == TRAILING && ltr )
                {
                    layoutData.put ( ICON, new Rectangle ( b.x, b.y, ips.width, b.height ) );
                    layoutData.put ( TEXT, new Rectangle ( b.x + gap + ips.width, b.y, b.width - ips.width - gap, b.height ) );
                }
                else if ( hpos == CENTER )
                {
                    if ( vpos == TOP )
                    {
                        layoutData.put ( ICON, new Rectangle ( b.x, b.y + b.height - ips.height, b.width, ips.height ) );
                        layoutData.put ( TEXT, new Rectangle ( b.x, b.y, b.width, b.height - gap - ips.height ) );
                    }
                    else
                    {
                        layoutData.put ( ICON, new Rectangle ( b.x, b.y, b.width, ips.height ) );
                        layoutData.put ( TEXT, new Rectangle ( b.x, b.y + ips.height + gap, b.width, b.height - ips.height - gap ) );
                    }
                }
                else
                {
                    layoutData.put ( ICON, new Rectangle ( b.x + b.width - ips.width, b.y, ips.width, b.height ) );
                    layoutData.put ( TEXT, new Rectangle ( b.x, b.y, b.width - ips.width - gap, b.height ) );
                }
            }
            else
            {
                layoutData.put ( ICON, b );
                layoutData.put ( TEXT, b );
            }
        }
        else if ( hasIcon )
        {
            layoutData.put ( ICON, b );
        }
        else if ( hasText )
        {
            layoutData.put ( TEXT, b );
        }

        return layoutData;
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        final int halign = getHorizontalAlignment ( c, d );
        final int valign = getVerticalAlignment ( c, d );

        // Retrieving spacing preferred sizes
        final Dimension top = getPreferredSize ( c, d, TOP_SPACE, available );
        final Dimension left = getPreferredSize ( c, d, LEFT_SPACE, available );
        final Dimension right = getPreferredSize ( c, d, RIGHT_SPACE, available );
        final Dimension bottom = getPreferredSize ( c, d, BOTTOM_SPACE, available );

        // Calculating actual spacing sizes
        // We have to find maximum of two in case axis alignment for main content is CENTER
        final int lw = halign == CENTER ? Math.max ( left.width, right.width ) : left.width;
        final int rw = halign == CENTER ? Math.max ( left.width, right.width ) : right.width;
        final int th = valign == CENTER ? Math.max ( top.height, bottom.height ) : top.height;
        final int bh = valign == CENTER ? Math.max ( top.height, bottom.height ) : bottom.height;

        // Calculating icon and text preferred size
        final Dimension iconTextAvailable = new Dimension ( available.width - lw - rw, available.height - th - bh );
        final Dimension iconText = getIconTextPreferredSize ( c, d, iconTextAvailable );

        // Calculating resulting preferred sizes
        final int pw = MathUtils.max ( top.width, lw + iconText.width + rw, bottom.width );
        final int ph = th + MathUtils.max ( left.height, iconText.height, right.height ) + bh;
        return new Dimension ( pw, ph );
    }

    /**
     * Returns icon and text preferred size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available available space
     * @return icon and text preferred size
     */
    protected Dimension getIconTextPreferredSize ( final C c, final D d, final Dimension available )
    {
        final Dimension size;
        final boolean hasIcon = !isEmpty ( c, d, ICON );
        final boolean hasText = !isEmpty ( c, d, TEXT );
        if ( hasIcon && hasText )
        {
            final int hpos = getHorizontalTextPosition ( c, d );
            final int vpos = getVerticalTextPosition ( c, d );
            final Dimension ips = getPreferredSize ( c, d, ICON, available );
            if ( hpos != CENTER || vpos != CENTER )
            {
                final int gap = getIconTextGap ( c, d );
                if ( hpos == LEFT || hpos == LEADING || hpos == RIGHT || hpos == TRAILING )
                {
                    final Dimension havailable = new Dimension ( available.width - gap - ips.width, available.height );
                    final Dimension cps = getPreferredSize ( c, d, TEXT, havailable );
                    size = new Dimension ( ips.width + gap + cps.width, Math.max ( ips.height, cps.height ) );
                }
                else
                {
                    final Dimension vavailable = new Dimension ( available.width, available.height - gap - ips.height );
                    final Dimension cps = getPreferredSize ( c, d, TEXT, vavailable );
                    size = new Dimension ( Math.max ( ips.width, cps.width ), ips.height + gap + cps.height );
                }
            }
            else
            {
                size = SwingUtils.max ( ips, getPreferredSize ( c, d, TEXT, available ) );
            }
        }
        else if ( hasIcon )
        {
            size = getPreferredSize ( c, d, ICON, available );
        }
        else if ( hasText )
        {
            size = getPreferredSize ( c, d, TEXT, available );
        }
        else
        {
            size = new Dimension ( 0, 0 );
        }
        return size;
    }
}