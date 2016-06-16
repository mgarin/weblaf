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
import com.alee.painter.decoration.content.IContent;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract implementation of simple icon and text layout.
 * It only paints contents placed under {@link #ICON} or {@link #TEXT} constraints.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> layout type
 * @author Mikle Garin
 */

@XStreamAlias ( "IconTextLayout" )
public class IconTextContentLayout<E extends JComponent, D extends IDecoration<E, D>, I extends IconTextContentLayout<E, D, I>>
        extends AbstractContentLayout<E, D, I> implements SwingConstants
{
    /**
     * Layout constraints.
     */
    public static final String ICON = "icon";
    public static final String TEXT = "text";

    @XStreamAsAttribute
    protected Integer gap;

    @XStreamAsAttribute
    protected BoxOrientation halign;

    @XStreamAsAttribute
    protected BoxOrientation valign;

    @XStreamAsAttribute
    protected BoxOrientation hpos;

    @XStreamAsAttribute
    protected BoxOrientation vpos;

    /**
     * Returns gap between icon and text contents.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return gap between icon and text contents
     */
    protected int getIconTextGap ( final E c, final D d )
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
    protected int getHorizontalAlignment ( final E c, final D d )
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
    protected int getVerticalAlignment ( final E c, final D d )
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
    protected int getHorizontalTextPosition ( final E c, final D d )
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
    protected int getVerticalTextPosition ( final E c, final D d )
    {
        return vpos != null ? vpos.getValue () : CENTER;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        // Calculating available size
        final Dimension size = getPreferredSize ( c, d );
        size.width = Math.min ( size.width, bounds.width );
        size.height = Math.min ( size.height, bounds.height );

        // Painting contents if at least some space is available
        if ( size.width > 0 && size.height > 0 )
        {
            // Proper content clipping
            final Shape oc = GraphicsUtils.setupClip ( g2d, bounds );

            // Calculating smallest content bounds
            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            final int halign = getHorizontalAlignment ( c, d );
            final int valign = getVerticalAlignment ( c, d );
            final Rectangle b = new Rectangle ( 0, 0, size.width, size.height );
            if ( halign == LEFT || halign == LEADING && ltr )
            {
                b.x = bounds.x;
            }
            else if ( halign == CENTER )
            {
                b.x = bounds.x + bounds.width / 2 - size.width / 2;
            }
            else
            {
                b.x = bounds.x + bounds.width - size.width;
            }
            if ( valign == TOP )
            {
                b.y = bounds.y;
            }
            else if ( valign == CENTER )
            {
                b.y = bounds.y + bounds.height / 2 - size.height / 2;
            }
            else
            {
                b.y = bounds.y + bounds.height - size.height;
            }

            // Painting contents
            final IContent icon = getContent ( ICON );
            final IContent text = getContent ( TEXT );
            if ( !isEmpty ( c, d, icon ) && !isEmpty ( c, d, text ) )
            {
                final int hpos = getHorizontalTextPosition ( c, d );
                final int vpos = getVerticalTextPosition ( c, d );
                if ( hpos != CENTER || vpos != CENTER )
                {
                    final Dimension ips = icon.getPreferredSize ( c, d );
                    final int gap = getIconTextGap ( c, d );
                    if ( hpos == RIGHT || hpos == TRAILING && ltr )
                    {
                        icon.paint ( g2d, new Rectangle ( b.x, b.y, ips.width, b.height ), c, d );
                        text.paint ( g2d, new Rectangle ( b.x + gap + ips.width, b.y, b.width - ips.width - gap, b.height ), c, d );
                    }
                    else if ( hpos == CENTER )
                    {
                        if ( vpos == TOP )
                        {
                            text.paint ( g2d, new Rectangle ( b.x, b.y, b.width, b.height - gap - ips.height ), c, d );
                            icon.paint ( g2d, new Rectangle ( b.x, b.y + b.height - ips.height, b.width, ips.height ), c, d );
                        }
                        else
                        {
                            icon.paint ( g2d, new Rectangle ( b.x, b.y, b.width, ips.height ), c, d );
                            text.paint ( g2d, new Rectangle ( b.x, b.y + ips.height + gap, b.width, b.height - ips.height - gap ), c, d );
                        }
                    }
                    else
                    {
                        icon.paint ( g2d, new Rectangle ( b.x + b.width - ips.width, b.y, ips.width, b.height ), c, d );
                        text.paint ( g2d, new Rectangle ( b.x, b.y, b.width - ips.width - gap, b.height ), c, d );
                    }
                }
                else
                {
                    icon.paint ( g2d, b, c, d );
                    text.paint ( g2d, b, c, d );
                }
            }
            else if ( !isEmpty ( c, d, icon ) )
            {
                icon.paint ( g2d, b, c, d );
            }
            else if ( !isEmpty ( c, d, text ) )
            {
                text.paint ( g2d, b, c, d );
            }

            // Restoring clip area
            GraphicsUtils.restoreClip ( g2d, oc );
        }
    }

    @Override
    public Dimension getPreferredSize ( final E c, final D d )
    {
        final IContent icon = getContent ( ICON );
        final IContent text = getContent ( TEXT );
        if ( !isEmpty ( c, d, icon ) && !isEmpty ( c, d, text ) )
        {
            final int hpos = getHorizontalTextPosition ( c, d );
            final int vpos = getVerticalTextPosition ( c, d );
            final Dimension ips = icon.getPreferredSize ( c, d );
            final Dimension cps = text.getPreferredSize ( c, d );
            if ( hpos != CENTER || vpos != CENTER )
            {
                final int gap = getIconTextGap ( c, d );
                if ( hpos == LEFT || hpos == LEADING || hpos == RIGHT || hpos == TRAILING )
                {
                    return new Dimension ( ips.width + gap + cps.width, Math.max ( ips.height, cps.height ) );
                }
                else
                {
                    return new Dimension ( Math.max ( ips.width, cps.width ), ips.height + gap + cps.height );
                }
            }
            else
            {
                return SwingUtils.max ( ips, cps );
            }
        }
        else if ( !isEmpty ( c, d, icon ) )
        {
            return icon.getPreferredSize ( c, d );
        }
        else if ( !isEmpty ( c, d, text ) )
        {
            return text.getPreferredSize ( c, d );
        }
        else
        {
            return new Dimension ( 0, 0 );
        }
    }

    @Override
    public I merge ( final I layout )
    {
        super.merge ( layout );
        if ( layout.gap != null )
        {
            gap = layout.gap;
        }
        if ( layout.valign != null )
        {
            valign = layout.valign;
        }
        if ( layout.halign != null )
        {
            halign = layout.halign;
        }
        if ( layout.hpos != null )
        {
            hpos = layout.hpos;
        }
        if ( layout.vpos != null )
        {
            vpos = layout.vpos;
        }
        return ( I ) this;
    }
}