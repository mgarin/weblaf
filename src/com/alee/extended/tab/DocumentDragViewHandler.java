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

package com.alee.extended.tab;

import com.alee.managers.drag.DragViewHandler;
import com.alee.utils.ImageUtils;
import com.alee.utils.LafUtils;
import sun.swing.SwingUtilities2;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.image.BufferedImage;

/**
 * Custom DragViewHandler for DocumentData object.
 *
 * @author Mikle Garin
 */

public class DocumentDragViewHandler<T extends DocumentData> implements DragViewHandler<T>
{
    /**
     * Document description margin.
     */
    protected static final Insets margin = new Insets ( 6, 6, 6, 6 );

    /**
     * Document pane which provides this DragViewHandler.
     */
    protected final WebDocumentPane documentPane;

    /**
     * Constructs custom DragViewHandler for DocumentData object.
     *
     * @param documentPane document pane which provides this DragViewHandler
     */
    public DocumentDragViewHandler ( final WebDocumentPane documentPane )
    {
        super ();
        this.documentPane = documentPane;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataFlavor getObjectFlavor ()
    {
        return DocumentTransferable.flavor;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BufferedImage getView ( final T document )
    {
        final Icon icon = document.getIcon ();
        final String title = document.getActualTitle ();

        final FontMetrics fm = SwingUtilities2.getFontMetrics ( documentPane, documentPane.getFont () );
        final int tm = margin.left + ( icon != null ? icon.getIconWidth () + 4 : 5 );
        final int em = margin.right + 5;
        final int w = tm + fm.stringWidth ( title ) + em;
        final int h = margin.top + Math.max ( ( icon != null ? icon.getIconHeight () : 0 ), fm.getHeight () ) + margin.bottom;

        final BufferedImage image = ImageUtils.createCompatibleImage ( w, h, Transparency.TRANSLUCENT );
        final Graphics2D g2d = image.createGraphics ();
        LafUtils.setupSystemTextHints ( g2d );
        LafUtils.setupAlphaComposite ( g2d, 0.8f );
        g2d.setPaint ( Color.WHITE );
        g2d.fillRect ( 0, 0, w, h );
        g2d.setPaint ( Color.LIGHT_GRAY );
        g2d.drawRect ( 0, 0, w - 1, h - 1 );
        if ( icon != null )
        {
            icon.paintIcon ( null, g2d, margin.left, margin.top );
        }
        g2d.setPaint ( Color.BLACK );
        g2d.drawString ( title, tm, margin.top + ( h - margin.top - margin.bottom ) / 2 + LafUtils.getTextCenterShearY ( fm ) );
        g2d.dispose ();
        return image;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point getViewRelativeLocation ( final T document )
    {
        return new Point ( 25, 5 );
    }
}