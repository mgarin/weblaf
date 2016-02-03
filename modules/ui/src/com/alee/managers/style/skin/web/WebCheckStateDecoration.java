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

package com.alee.managers.style.skin.web;

import com.alee.managers.style.skin.web.data.check.ICheckStateIcon;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;
import com.alee.managers.style.skin.web.data.decoration.WebDecoration;
import com.alee.utils.CollectionUtils;
import com.alee.utils.MergeUtils;
import com.alee.utils.SwingUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

@XStreamAlias ("checkDecoration")
public class WebCheckStateDecoration<E extends AbstractButton, I extends WebCheckStateDecoration<E, I>> extends WebDecoration<E, I>
        implements IDecoration<E, I>
{
    /**
     * Check state icon.
     * Implicit list is used to provide convenient XML descriptor for this field.
     */
    @XStreamImplicit
    protected List<ICheckStateIcon> icons;

    /**
     * Returns check state icon.
     *
     * @return check state icon
     */
    protected ICheckStateIcon getIcon ()
    {
        return !CollectionUtils.isEmpty ( icons ) ? icons.get ( 0 ) : null;
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        // Paint base decoration
        super.paint ( g2d, bounds, c );

        // Paint check state decoration
        paintCheckState ( g2d, bounds, c );
    }

    /**
     * Paints check state.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     */
    protected void paintCheckState ( final Graphics2D g2d, final Rectangle bounds, final E c )
    {
        final ICheckStateIcon icon = getIcon ();
        if ( icon != null )
        {
            icon.paint ( g2d, bounds, c, WebCheckStateDecoration.this, SwingUtils.shrink ( bounds, getBorderInsets () ) );
        }
    }

    @Override
    public I merge ( final I decoration )
    {
        super.merge ( decoration );
        icons = MergeUtils.merge ( icons, decoration.icons );
        return ( I ) this;
    }
}