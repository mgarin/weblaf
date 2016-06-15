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

package com.alee.painter.decoration.content;

import com.alee.global.StyleConstants;
import com.alee.laf.label.Rotation;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

/**
 * Abstract implementation of simple text content.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

public abstract class AbstractTextContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractTextContent<E, D, I>>
        extends AbstractContent<E, D, I>
{
    /**
     * Text rotation.
     */
    @XStreamAsAttribute
    protected Rotation rotation;

    /**
     * Whether or not should paint text shadow.
     */
    @XStreamAsAttribute
    protected Boolean shadow;

    /**
     * Text shadow color.
     */
    @XStreamAsAttribute
    protected Color shadowColor;

    /**
     * Text shadow size.
     */
    @XStreamAsAttribute
    protected Integer shadowSize;

    @Override
    public String getId ()
    {
        return id != null ? id : "text";
    }

    /**
     * Returns whether or not text shadow should be painted.
     *
     * @return true if text shadow should be painted, false otherwie
     */
    public boolean isShadow ()
    {
        return shadow != null && shadow;
    }

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return TextUtils.isEmpty ( getText ( c, d ) );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        // Ensure that text painting is allowed
        if ( !isEmpty ( c, d ) )
        {
            // Applying graphics settings
            final Font oldFont = GraphicsUtils.setupFont ( g2d, c.getFont () );
            final AffineTransform transform = g2d.getTransform ();

            // Applying rotation
            final Rotation rotation = getActualRotation ( c );
            if ( rotation != Rotation.none )
            {
                double angle = 0;
                double rX = 0;
                double rY = 0;
                switch ( rotation )
                {
                    case clockwise:
                        angle = Math.PI / 2;
                        rX = bounds.width;
                        rY = bounds.width;
                        break;

                    case upsideDown:
                        angle = Math.PI;
                        rX = bounds.width;
                        rY = bounds.height;
                        break;

                    case counterClockwise:
                        angle = -Math.PI / 2;
                        rX = bounds.height;
                        rY = bounds.height;
                        break;
                }
                g2d.rotate ( angle, bounds.x + rX / 2, bounds.y + rY / 2 );
            }
            final Rectangle rotatedBounds = rotation.transpose ( bounds );

            // Installing text antialias settings
            final Map textHints = isShadow () ? StyleConstants.defaultTextRenderingHints : StyleConstants.textRenderingHints;
            final Map oldHints = SwingUtils.setupTextAntialias ( g2d, textHints );

            // Painting either HTML or plain text
            if ( isHtmlText ( c, d ) )
            {
                paintHtml ( g2d, rotatedBounds, c, d );
            }
            else
            {
                paintPlainText ( g2d, rotatedBounds, c, d );
            }

            // Restoring text antialias settings
            SwingUtils.restoreTextAntialias ( g2d, oldHints );

            // Restoring graphics settings
            g2d.setTransform ( transform );
            GraphicsUtils.restoreFont ( g2d, oldFont );
        }
    }

    /**
     * Paints HTML text view.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     */
    protected void paintHtml ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        getHtml ( c, d ).paint ( g2d, bounds );
    }

    /**
     * Paints plain text view.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     */
    protected void paintPlainText ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final Paint op = GraphicsUtils.setupPaint ( g2d, c.getForeground () );
        final String text = getText ( c, d );

        final int mnemIndex = getMnenonicIndex ( c, d );
        final FontMetrics fm = c.getFontMetrics ( c.getFont () );
        final int textX = bounds.x + bounds.width / 2 + LafUtils.getTextCenterShiftX ( fm, text );
        final int textY = bounds.y + bounds.height / 2 + LafUtils.getTextCenterShiftY ( fm );
        SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemIndex, textX, textY );

        // todo Paint disabled text somehow
        // todo Paint shadow text effect

        GraphicsUtils.restorePaint ( g2d, op );
    }

    /**
     * Returns actual text rotation.
     *
     * @param c painted component
     * @return actual text rotation
     */
    protected Rotation getActualRotation ( final E c )
    {
        return rotation != null ? c.getComponentOrientation ().isLeftToRight () ? rotation : rotation.rightToLeft () : Rotation.none;
    }

    /**
     * Returns whether or not text contains HTML text.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if text contains HTML text, false otherwise
     */
    protected abstract boolean isHtmlText ( E c, D d );

    /**
     * Returns HTML text view to be painted.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return HTML text view to be painted
     */
    protected abstract View getHtml ( E c, D d );

    /**
     * Returns text to be painted.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text to be painted
     */
    protected abstract String getText ( E c, D d );

    /**
     * Returns mnemonic index or {@code -1} if it shouldn't be displayed.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return mnemonic index or {@code -1} if it shouldn't be displayed
     */
    protected abstract int getMnenonicIndex ( E c, D d );

    @Override
    public Dimension getPreferredSize ( final E c, final D d )
    {
        if ( !isEmpty ( c, d ) )
        {
            final int w;
            final int h;
            if ( isHtmlText ( c, d ) )
            {
                final View html = getHtml ( c, d );
                w = ( int ) html.getPreferredSpan ( View.X_AXIS );
                h = ( int ) html.getPreferredSpan ( View.Y_AXIS );
            }
            else
            {
                final String text = getText ( c, d );
                final FontMetrics fm = c.getFontMetrics ( c.getFont () );
                w = SwingUtils.stringWidth ( fm, text );
                h = fm.getHeight ();
            }
            final boolean ver = getActualRotation ( c ).isVertical ();
            return ver ? new Dimension ( h, w ) : new Dimension ( w, h );
        }
        else
        {
            return new Dimension ( 0, 0 );
        }
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        if ( content.rotation != null )
        {
            rotation = content.rotation;
        }
        if ( content.shadow != null )
        {
            shadow = content.shadow;
        }
        if ( content.shadowColor != null )
        {
            shadowColor = content.shadowColor;
        }
        if ( content.shadowSize != null )
        {
            shadowSize = content.shadowSize;
        }
        return ( I ) this;
    }
}