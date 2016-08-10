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
import com.alee.managers.style.StyleException;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * Abstract implementation of simple text content.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

@SuppressWarnings ( "UnusedParameters" )
public abstract class AbstractTextContent<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractTextContent<E, D, I>>
        extends AbstractContent<E, D, I> implements SwingConstants
{
    /**
     * Text foreground color.
     */
    @XStreamAsAttribute
    protected Color color;

    /**
     * Horizontal text alignment.
     */
    @XStreamAsAttribute
    protected Integer halign;

    /**
     * Vertical text alignment.
     */
    @XStreamAsAttribute
    protected Integer valign;

    /**
     * Whether or not text should be truncated when it gets outside of the available bounds.
     */
    @XStreamAsAttribute
    protected Boolean truncate;

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

    @Override
    public boolean isEmpty ( final E c, final D d )
    {
        return TextUtils.isEmpty ( getText ( c, d ) );
    }

    /**
     * Returns text foreground color.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text foreground color
     */
    protected Color getColor ( final E c, final D d )
    {
        // This {@link javax.swing.plaf.UIResource} check allows us to ignore such colors in favor of style ones
        // But this will not ignore any normal color set from the code as this component foreground
        return color != null && c.getForeground () instanceof UIResource ? color : c.getForeground ();
    }

    /**
     * Returns text horizontal alignment.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text horizontal alignment
     */
    protected int getHorizontalAlignment ( final E c, final D d )
    {
        return halign != null ? halign : LEADING;
    }

    /**
     * Returns text vertical alignment.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text vertical alignment
     */
    protected int getVerticalAlignment ( final E c, final D d )
    {
        return valign != null ? valign : CENTER;
    }

    /**
     * Returns whether or not text should be truncated when it gets outside of the available bounds.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if text should be truncated when it gets outside of the available bounds, false otherwise
     */
    protected boolean isTruncated ( final E c, final D d )
    {
        return truncate == null || truncate;
    }

    /**
     * Returns whether or not text shadow should be painted.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if text shadow should be painted, false otherwise
     */
    protected boolean isShadow ( final E c, final D d )
    {
        return shadow != null && shadow;
    }

    /**
     * Returns shadow color.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return shadow color
     */
    protected Color getShadowColor ( final E c, final D d )
    {
        if ( shadowColor != null )
        {
            return shadowColor;
        }
        throw new StyleException ( "shadow color must not be empty" );
    }

    /**
     * Returns shadow size.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return shadow size
     */
    protected int getShadowSize ( final E c, final D d )
    {
        if ( shadowSize != null )
        {
            return shadowSize;
        }
        throw new StyleException ( "shadow size must not be empty" );
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
    protected abstract int getMnemonicIndex ( E c, D d );

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        // Ensure that text painting is allowed
        if ( !isEmpty ( c, d ) )
        {
            // Applying graphics settings
            final Font oldFont = GraphicsUtils.setupFont ( g2d, c.getFont () );

            // Installing text antialias settings
            final Map textHints = isShadow ( c, d ) ? StyleConstants.defaultTextRenderingHints : StyleConstants.textRenderingHints;
            final Map oldHints = SwingUtils.setupTextAntialias ( g2d, textHints );

            // Painting either HTML or plain text
            if ( isHtmlText ( c, d ) )
            {
                paintHtml ( g2d, bounds, c, d );
            }
            else
            {
                final int shadowWidth = isShadow ( c, d ) ? getShadowSize ( c, d ) : 0;
                bounds.x += shadowWidth;
                bounds.width -= shadowWidth * 2;
                paintText ( g2d, bounds, c, d );
            }

            // Restoring text antialias settings
            SwingUtils.restoreTextAntialias ( g2d, oldHints );

            // Restoring graphics settings
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
    protected void paintText ( final Graphics2D g2d, final Rectangle bounds, final E c, final D d )
    {
        final Paint op = GraphicsUtils.setupPaint ( g2d, getColor ( c, d ) );
        final String text = getText ( c, d );

        final int mnemIndex = getMnemonicIndex ( c, d );
        final FontMetrics fm = c.getFontMetrics ( c.getFont () );
        final int va = getVerticalAlignment ( c, d );
        int ha = getHorizontalAlignment ( c, d );
        final boolean truncated = isTruncated ( c, d );
        final int tw = fm.stringWidth ( text );

        int textX = bounds.x;
        int textY = bounds.y;

        // Adjusting coordinates according to vertical alignment
        switch ( va )
        {
            case CENTER:
                textY += ( bounds.height + fm.getAscent () - fm.getLeading () - fm.getDescent () ) / 2;
                break;

            case BOTTOM:
                textY += bounds.height - fm.getHeight ();
                break;

            default:
                textY += fm.getAscent ();
        }

        // Adjusting coordinates according to horizontal alignment
        if ( tw < bounds.width )
        {
            final boolean ltr = c.getComponentOrientation ().isLeftToRight ();
            if ( ltr ? ha == LEADING : ha == TRAILING )
            {
                ha = SwingConstants.LEFT;
            }
            else if ( ltr ? ha == TRAILING : ha == LEADING )
            {
                ha = SwingConstants.RIGHT;
            }

            switch ( ha )
            {
                case CENTER:
                    textX += ( bounds.width - tw ) / 2;
                    break;

                case RIGHT:
                    textX += bounds.width - tw;
                    break;
            }
        }

        // Clipping text if needed
        final String paintedText = truncated && bounds.width < tw ?
                SwingUtilities.layoutCompoundLabel ( fm, text, null, 0, ha, 0, 0, bounds, new Rectangle (), new Rectangle (), 0 ) : text;

        // Painting text
        paintTextFragment ( c, d, g2d, paintedText, textX, textY, mnemIndex );

        GraphicsUtils.restorePaint ( g2d, op );
    }

    /**
     * Paints text fragment.
     *
     * @param c        painted component
     * @param d        painted decoration state
     * @param g2d      graphics context
     * @param text     text fragment
     * @param textX    text X coordinate
     * @param textY    text Y coordinate
     * @param mneIndex index of mnemonic
     */
    protected void paintTextFragment ( final E c, final D d, final Graphics2D g2d, final String text, final int textX, final int textY,
                                       final int mneIndex )
    {
        if ( isShadow ( c, d ) )
        {
            g2d.translate ( textX, textY );
            final int ss = getShadowSize ( c, d );
            paintTextEffect ( g2d, text, getShadowColor ( c, d ), ss, -ss, 1 - ss, true );
            g2d.translate ( -textX, -textY );
        }
        else
        {
            SwingUtils.drawStringUnderlineCharAt ( g2d, text, mneIndex, textX, textY );
        }
    }

    /**
     * Draw a string with a blur or shadow effect. The light angle is assumed to be 0 degrees, (i.e., window is illuminated from top).
     * The effect is intended to be subtle to be usable in as many text components as possible. The effect is generated with multiple calls
     * to draw string. This method paints the text on coordinates {@code tx}, {@code ty}. If text should be painted elsewhere, a transform
     * should be applied to the graphics before passing it.
     *
     * @param g2d      graphics context
     * @param text     text to paint
     * @param color    effect color
     * @param size     effect size
     * @param tx       shift by X
     * @param ty       shift by Y
     * @param isShadow whether should paint shadow effect or not
     */
    protected void paintTextEffect ( final Graphics2D g2d, final String text, final Color color, final int size, final double tx,
                                     final double ty, final boolean isShadow )
    {
        // Effect "darkness"
        final float opacity = 0.8f;

        final Composite oldComposite = g2d.getComposite ();
        final Color oldColor = g2d.getColor ();

        // Use a alpha blend smaller than 1 to prevent the effect from becoming too dark when multiple paints occur on top of each other.
        float preAlpha = 0.4f;
        if ( oldComposite instanceof AlphaComposite && ( ( AlphaComposite ) oldComposite ).getRule () == AlphaComposite.SRC_OVER )
        {
            preAlpha = Math.min ( ( ( AlphaComposite ) oldComposite ).getAlpha (), preAlpha );
        }
        g2d.setPaint ( ColorUtils.removeAlpha ( color ) );

        g2d.translate ( tx, ty );

        // If the effect is a shadow it looks better to stop painting a bit earlier - shadow will look softer
        final int maxSize = isShadow ? size - 1 : size;

        for ( int i = -size; i <= maxSize; i++ )
        {
            for ( int j = -size; j <= maxSize; j++ )
            {
                final double distance = i * i + j * j;
                float alpha = opacity;
                if ( distance > 0.0d )
                {
                    alpha = ( float ) ( 1.0f / ( distance * size * opacity ) );
                }
                alpha *= preAlpha;
                if ( alpha > 1.0f )
                {
                    alpha = 1.0f;
                }
                g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, alpha ) );
                g2d.drawString ( text, i + size, j + size );
            }
        }

        // Restore graphics
        g2d.translate ( -tx, -ty );
        g2d.setComposite ( oldComposite );
        g2d.setPaint ( oldColor );

        // Painting text itself
        g2d.drawString ( text, 0, 0 );
    }

    @Override
    protected Dimension getContentPreferredSize ( final E c, final D d, final Dimension available )
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
                return new Dimension ( w, h );
            }
            else
            {
                final Dimension pts = getPreferredTextSize ( c, d, available );
                pts.width += isShadow ( c, d ) ? getShadowSize ( c, d ) * 2 : 0;
                return pts;
            }
        }
        else
        {
            return new Dimension ( 0, 0 );
        }
    }

    /**
     * Returns preferred text size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available theoretically available space for this content
     * @return preferred text size
     */
    protected Dimension getPreferredTextSize ( final E c, final D d, final Dimension available )
    {
        final String text = getText ( c, d );
        final FontMetrics fm = c.getFontMetrics ( c.getFont () );
        return new Dimension ( SwingUtils.stringWidth ( fm, text ), fm.getHeight () );
    }

    @Override
    public I merge ( final I content )
    {
        super.merge ( content );
        color = content.isOverwrite () || content.color != null ? content.color : color;
        halign = content.isOverwrite () || content.halign != null ? content.halign : halign;
        valign = content.isOverwrite () || content.valign != null ? content.valign : valign;
        truncate = content.isOverwrite () || content.truncate != null ? content.truncate : truncate;
        shadow = content.isOverwrite () || content.shadow != null ? content.shadow : shadow;
        shadowColor = content.isOverwrite () || content.shadowColor != null ? content.shadowColor : shadowColor;
        shadowSize = content.isOverwrite () || content.shadowSize != null ? content.shadowSize : shadowSize;
        return ( I ) this;
    }
}