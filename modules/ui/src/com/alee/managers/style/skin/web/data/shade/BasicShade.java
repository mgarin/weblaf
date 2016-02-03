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

package com.alee.managers.style.skin.web.data.shade;

import com.alee.managers.style.skin.web.data.decoration.WebDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;

/**
 * Basic shade that can be painted on any shape.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("BasicShade")
public class BasicShade<E extends JComponent, D extends WebDecoration<E, D>, I extends BasicShade<E, D, I>> extends AbstractShade<E, D, I>
{
    /**
     * Reference keeping shade icon in memory.
     * If we do not save it to this reference it will be erased after first GC.
     * If that happens - request to {@link com.alee.utils.NinePatchUtils} will be forced to create a new one.
     */
    protected transient NinePatchIcon shade;

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d, final Shape shape )
    {
        final int width = getWidth ();
        final float transparency = getTransparency ();
        if ( width > 0 && transparency > 0f )
        {
            final ShadeType type = getType ();
            final Color color = getColor ();
            if ( width < 5 )
            {
                // todo Optimize composite usage by moving shade painting algorithm here
                final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, transparency, transparency < 1f );
                GraphicsUtils.drawShade ( g2d, shape, color, width, type == ShadeType.inner ? shape : null );
                GraphicsUtils.restoreComposite ( g2d, oc, transparency < 1f );
                shade = null;
            }
            else
            {
                // todo 1. Support colored large shade
                // todo 2. Use raw buffered image cached for the component size when shape is custom
                // todo    ImageUtils.createShadeImage ( ... ) for non-standard settings
                // todo    ImageUtils.createInnerShadeImage ( ... ) for non-standard settings
                final int round = d.getRound ();
                final Rectangle b = shape.getBounds ();
                if ( type == ShadeType.outer )
                {
                    shade = NinePatchUtils.getShadeIcon ( width, round, transparency );
                    shade.paintIcon ( g2d, b.x - width, b.y - width, b.width + 1 + width * 2, b.height + 1 + width * 2 );
                }
                else
                {
                    shade = NinePatchUtils.getInnerShadeIcon ( width, round, transparency );
                    shade.paintIcon ( g2d, b.x, b.y, b.width + 1, b.height + 1 );
                }
            }
        }
        else
        {
            shade = null;
        }
    }
}