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

package com.alee.managers.drag.view;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.content.TextRasterization;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.image.BufferedImage;

/**
 * Abstract {@link DragViewHandler} implementation that displays icon and text based on dragged data.
 *
 * @param <T> dragged data type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-DragManager">How to use DragManager</a>
 * @see com.alee.managers.drag.DragManager
 */
public abstract class SimpleDragViewHandler<T> implements DragViewHandler<T>
{
    /**
     * Document description margin.
     */
    protected static final Insets margin = new Insets ( 6, 6, 6, 6 );

    /**
     * Additional spacing at text sides.
     */
    protected static final int textSideSpacing = 3;

    /**
     * Returns font metrics used to display text.
     *
     * @param object dragged object
     * @return font metrics used to display text
     */
    protected FontMetrics getFontMetrics ( final T object )
    {
        return SwingUtils.getDefaultLabelFontMetrics ();
    }

    /**
     * Returns displayed icon.
     *
     * @param object dragged object
     * @return displayed icon
     */
    protected abstract Icon getIcon ( T object );

    /**
     * Returns displayed text foreground.
     *
     * @param object dragged object
     * @return displayed text foreground
     */
    protected abstract Color getForeground ( T object );

    /**
     * Returns displayed text.
     *
     * @param object dragged object
     * @return displayed text
     */
    protected abstract String getText ( T object );

    @Override
    public BufferedImage getView ( final T object, final DragSourceDragEvent event )
    {
        final Icon icon = getIcon ( object );
        final Color foreground = getForeground ( object );
        final String title = LM.get ( getText ( object ) );

        final FontMetrics fm = getFontMetrics ( object );
        final int tm = margin.left + ( icon != null ? icon.getIconWidth () + 4 : textSideSpacing );
        final int em = margin.right + textSideSpacing;
        final int w = tm + fm.stringWidth ( title ) + em;
        final int h = margin.top + Math.max ( icon != null ? icon.getIconHeight () : 0, fm.getHeight () ) + margin.bottom;

        final BufferedImage image = ImageUtils.createCompatibleImage ( w, h, Transparency.TRANSLUCENT );
        final Graphics2D g2d = image.createGraphics ();
        GraphicsUtils.setupAlphaComposite ( g2d, 0.8f );
        GraphicsUtils.setupFont ( g2d, fm.getFont () );
        SwingUtils.setupTextAntialias ( g2d, TextRasterization.subpixel );
        g2d.setPaint ( Color.WHITE );
        g2d.fillRect ( 0, 0, w, h );
        g2d.setPaint ( Color.LIGHT_GRAY );
        g2d.drawRect ( 0, 0, w - 1, h - 1 );
        if ( icon != null )
        {
            icon.paintIcon ( null, g2d, margin.left, margin.top );
        }
        g2d.setPaint ( foreground != null ? foreground : Color.BLACK );
        g2d.drawString ( title, tm, margin.top + ( h - margin.top - margin.bottom ) / 2 + LafUtils.getTextCenterShiftY ( fm ) );
        g2d.dispose ();
        return image;
    }

    @Override
    public Point getViewRelativeLocation ( final T document, final DragSourceDragEvent event, final BufferedImage view )
    {
        return new Point ( 25, 5 );
    }

    @Override
    public void dragEnded ( final T object, final DragSourceDropEvent event )
    {
        /**
         * Don't need to do anything on drag end.
         */
    }
}