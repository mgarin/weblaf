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

package com.alee.painter.decoration;

import com.alee.managers.style.Boundz;
import com.alee.utils.SwingUtils;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.awt.*;

/**
 * Configurable decoration state based on 9-patch image resource.
 * It uses single 9-patch image instead of complex combination of shape, shadow, border and background.
 *
 * @param <E> component type
 * @param <I> decoration type
 * @author Mikle Garin
 */

@XStreamAlias ("ninepatch")
public class NinePatchDecoration<E extends JComponent, I extends NinePatchDecoration<E, I>> extends ContentDecoration<E, I>
{
    /**
     * 9-patch icon used for this decoration.
     */
    protected NinePatchIcon icon;

    /**
     * Returns 9-patch icon used for this decoration.
     *
     * @return 9-patch icon used for this decoration
     */
    public NinePatchIcon getIcon ()
    {
        return icon;
    }

    @Override
    public Insets getBorderInsets ( final E c )
    {
        final Insets insets = super.getBorderInsets ( c );
        if ( isVisible () )
        {
            final NinePatchIcon icon = getIcon ();
            if ( icon != null )
            {
                SwingUtils.increase ( insets, icon.getMargin () );
            }
        }
        return insets;
    }

    @Override
    public Shape provideShape ( final E component, final Rectangle bounds )
    {
        // Unfortunately there is no good way to detect actual 9-patch decoration shape
        // This is why we simply return full painting bounds
        return bounds;
    }

    @Override
    public void paint ( final Graphics2D g2d, final E c, final Boundz bounds )
    {
        // Painting only if decoration is visible
        if ( isVisible () )
        {
            // Checking icon availability
            final NinePatchIcon icon = getIcon ();
            if ( icon != null )
            {
                icon.paintIcon ( g2d, bounds.get () );
            }
        }

        // Painting contents
        paintContent ( g2d, bounds, c );
    }

    @Override
    public I merge ( final I decoration )
    {
        super.merge ( decoration );
        icon = decoration.isOverwrite () || decoration.icon != null ? decoration.icon : icon;
        return ( I ) this;
    }
}