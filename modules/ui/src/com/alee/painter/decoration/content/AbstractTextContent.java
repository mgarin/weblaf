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

import com.alee.api.annotations.Nullable;
import com.alee.api.clone.behavior.OmitOnClone;
import com.alee.api.jdk.Objects;
import com.alee.api.merge.behavior.OmitOnMerge;
import com.alee.managers.style.StyleException;
import com.alee.painter.decoration.DecorationException;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.ColorUtils;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.swing.BasicHTML;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import javax.swing.*;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * Abstract implementation of simple text content.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 * @author Alexandr Zernov
 */
public abstract class AbstractTextContent<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractTextContent<C, D, I>>
        extends AbstractContent<C, D, I> implements SwingConstants
{
    /**
     * todo 1. Move shadow settings into separate serializable object
     */

    /**
     * Preferred text antialias option.
     */
    @XStreamAsAttribute
    protected TextRasterization rasterization;

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

    /**
     * Text shadow opacity.
     */
    @XStreamAsAttribute
    protected Float shadowOpacity;

    /**
     * Cached HTML {@link View} settings.
     * This is runtime-only field that should not be serialized.
     * It is also generated automatically on demand if missing.
     *
     * @see #getHtml(javax.swing.JComponent, com.alee.painter.decoration.IDecoration)
     * @see #cleanupHtml(javax.swing.JComponent, com.alee.painter.decoration.IDecoration)
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient String htmlSettings;

    /**
     * Cached HTML {@link View}.
     * This is runtime-only field that should not be serialized.
     * It is also generated automatically on demand if missing.
     *
     * @see #getHtml(javax.swing.JComponent, com.alee.painter.decoration.IDecoration)
     * @see #cleanupHtml(javax.swing.JComponent, com.alee.painter.decoration.IDecoration)
     */
    @OmitOnClone
    @OmitOnMerge
    protected transient View htmlView;

    @Nullable
    @Override
    public String getId ()
    {
        return id != null ? id : "text";
    }

    @Override
    public boolean isEmpty ( final C c, final D d )
    {
        return TextUtils.isEmpty ( getText ( c, d ) );
    }

    @Override
    public void deactivate ( final C c, final D d )
    {
        // Performing default actions
        super.deactivate ( c, d );

        // Cleaning up HTML caches
        // todo This might lower HTML performance for components with multiple states like buttons
        // todo Perfectly HTML should be cleaned up only when decoration is removed from possible decorations and not just deactivated
        cleanupHtml ( c, d );
    }

    /**
     * Returns preferred rasterization option.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return preferred rasterization option
     */
    public TextRasterization getRasterization ( final C c, final D d )
    {
        return rasterization != null ? rasterization : TextRasterization.subpixel;
    }

    /**
     * Returns text font.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text font
     */
    protected Font getFont ( final C c, final D d )
    {
        return c.getFont ();
    }

    /**
     * Returns text font metrics.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text font metrics
     */
    protected FontMetrics getFontMetrics ( final C c, final D d )
    {
        return c.getFontMetrics ( getFont ( c, d ) );
    }

    /**
     * Returns text foreground color.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text foreground color
     */
    protected Color getColor ( final C c, final D d )
    {
        // This {@link javax.swing.plaf.UIResource} check allows us to ignore such colors in favor of style ones
        // But this will not ignore any normal color set from the code as this component foreground
        return color != null && SwingUtils.isUIResource ( c.getForeground () ) ? color : c.getForeground ();
    }

    /**
     * Returns text horizontal alignment.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text horizontal alignment
     */
    protected int getHorizontalAlignment ( final C c, final D d )
    {
        return halign != null ? halign : LEADING;
    }

    /**
     * Returns text horizontal alignment adjusted according to the component orientation.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text horizontal alignment
     */
    protected int getAdjustedHorizontalAlignment ( final C c, final D d )
    {
        final int adjusted;
        final int alignment = getHorizontalAlignment ( c, d );
        switch ( alignment )
        {
            case LEADING:
                adjusted = isLeftToRight ( c, d ) ? LEFT : RIGHT;
                break;

            case TRAILING:
                adjusted = isLeftToRight ( c, d ) ? RIGHT : LEFT;
                break;

            default:
                adjusted = alignment;
                break;
        }
        return adjusted;
    }

    /**
     * Returns text vertical alignment.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text vertical alignment
     */
    protected int getVerticalAlignment ( final C c, final D d )
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
    protected boolean isTruncate ( final C c, final D d )
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
    protected boolean isShadow ( final C c, final D d )
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
    protected Color getShadowColor ( final C c, final D d )
    {
        if ( shadowColor != null )
        {
            return shadowColor;
        }
        throw new StyleException ( "Shadow color must be specified" );
    }

    /**
     * Returns shadow size.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return shadow size
     */
    protected int getShadowSize ( final C c, final D d )
    {
        return shadowSize != null ? shadowSize : 2;
    }

    /**
     * Returns shadow opacity.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return shadow opacity
     */
    public float getShadowOpacity ( final C c, final D d )
    {
        return shadowOpacity != null ? shadowOpacity : 0.8f;
    }

    /**
     * Returns whether or not text contains HTML.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return true if text contains HTML, false otherwise
     */
    protected boolean isHtmlText ( final C c, final D d )
    {
        // Determining whether or not text contains HTML
        final String text = getText ( c, d );
        final boolean html = BasicHTML.isHTMLString ( c, text );

        // Cleaning up HTML caches
        if ( !html )
        {
            cleanupHtml ( c, d );
        }

        return html;
    }

    /**
     * Returns HTML text view to be painted.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return HTML text view to be painted
     */
    protected View getHtml ( final C c, final D d )
    {
        // HTML content settings
        final String text = getText ( c, d );
        final Font font = getFont ( c, d );
        final Color foreground = getColor ( c, d );

        // HTML view settings
        final String settings = text + ";" + font + ";" + foreground;

        // Updating HTML view if needed
        if ( htmlView == null || Objects.notEquals ( htmlSettings, settings ) )
        {
            htmlSettings = settings;
            htmlView = BasicHTML.createHTMLView ( c, text, font, foreground );
        }

        // Return cached HTML view
        return htmlView;
    }

    /**
     * Cleans up HTML text view caches.
     *
     * @param c painted component
     * @param d painted decoration state
     */
    protected void cleanupHtml ( final C c, final D d )
    {
        htmlSettings = null;
        htmlView = null;
    }

    /**
     * Returns text to be painted.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return text to be painted
     */
    protected abstract String getText ( C c, D d );

    /**
     * Returns mnemonic index or {@code -1} if it shouldn't be displayed.
     *
     * @param c painted component
     * @param d painted decoration state
     * @return mnemonic index or {@code -1} if it shouldn't be displayed
     */
    protected abstract int getMnemonicIndex ( C c, D d );

    @Override
    public boolean hasContentBaseline ( final C c, final D d )
    {
        return !isHtmlText ( c, d );
    }

    @Override
    public int getContentBaseline ( final C c, final D d, final Rectangle bounds )
    {
        final int baseline;
        if ( !isHtmlText ( c, d ) )
        {
            final FontMetrics fm = getFontMetrics ( c, d );
            baseline = getTextY ( c, d, bounds, fm );
        }
        else
        {
            baseline = -1;
        }
        return baseline;
    }

    @Override
    public Component.BaselineResizeBehavior getContentBaselineResizeBehavior ( final C c, final D d )
    {
        final Component.BaselineResizeBehavior result;
        if ( !isHtmlText ( c, d ) )
        {
            switch ( getVerticalAlignment ( c, d ) )
            {
                case TOP:
                    result = Component.BaselineResizeBehavior.CONSTANT_ASCENT;
                    break;

                case BOTTOM:
                    result = Component.BaselineResizeBehavior.CONSTANT_DESCENT;
                    break;

                case CENTER:
                    result = Component.BaselineResizeBehavior.CENTER_OFFSET;
                    break;

                default:
                    result = Component.BaselineResizeBehavior.OTHER;
                    break;
            }
        }
        else
        {
            result = Component.BaselineResizeBehavior.OTHER;
        }
        return result;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final C c, final D d, final Rectangle bounds )
    {
        // Ensure that text painting is allowed
        if ( !isEmpty ( c, d ) )
        {
            // Applying graphics settings
            final Font oldFont = GraphicsUtils.setupFont ( g2d, getFont ( c, d ) );

            // Installing text antialias settings
            final TextRasterization rasterization = getRasterization ( c, d );
            final Map oldHints = SwingUtils.setupTextAntialias ( g2d, rasterization );

            // Paint color
            final Paint op = GraphicsUtils.setupPaint ( g2d, getColor ( c, d ) );

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

            // Restoring paint color
            GraphicsUtils.restorePaint ( g2d, op );

            // Restoring text antialias settings
            SwingUtils.restoreTextAntialias ( g2d, oldHints );

            // Restoring graphics settings
            GraphicsUtils.restoreFont ( g2d, oldFont );
        }
    }

    /**
     * Paints HTML text view.
     * Note that HTML text is usually not affected by the graphics paint settings as it defines its own one inside.
     * This is why custom {@link com.alee.utils.swing.BasicHTML} class exists to provide proper body text color upon view creation.
     *
     * @param g2d    graphics context
     * @param bounds painting bounds
     * @param c      painted component
     * @param d      painted decoration state
     */
    protected void paintHtml ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d )
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
    protected void paintText ( final Graphics2D g2d, final Rectangle bounds, final C c, final D d )
    {
        // Painting settings
        final String text = getText ( c, d );
        final FontMetrics fontMetrics = getFontMetrics ( c, d );
        final int textWidth = fontMetrics.stringWidth ( text );
        final int horizontalAlignment = getAdjustedHorizontalAlignment ( c, d );
        final int mnemonicIndex = getMnemonicIndex ( c, d );

        // Calculating text coordinates
        final int textX = getTextX ( c, d, bounds, textWidth, horizontalAlignment );
        final int textY = getTextY ( c, d, bounds, fontMetrics );

        // Truncating text if needed
        final String paintedText = getPaintedText ( c, d, bounds, text, fontMetrics, textWidth, horizontalAlignment );

        // Painting text
        paintTextFragment ( c, d, g2d, paintedText, textX, textY, mnemonicIndex );
    }

    /**
     * Returns text X coordinate within component bounds.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param bounds    painting bounds
     * @param width     text width
     * @param alignment horizontal text alignment
     * @return text X coordinate within component bounds
     */
    protected int getTextX ( final C c, final D d, final Rectangle bounds, final int width, final int alignment )
    {
        int textX = bounds.x;
        if ( width < bounds.width )
        {
            switch ( alignment )
            {
                case LEFT:
                    break;

                case CENTER:
                    textX += Math.floor ( ( bounds.width - width ) / 2.0 );
                    break;

                case RIGHT:
                    textX += bounds.width - width;
                    break;

                default:
                    throw new DecorationException ( "Incorrect horizontal alignment provided: " + alignment );
            }
        }
        return textX;
    }

    /**
     * Returns text Y coordinate within component bounds.
     *
     * @param c      painted component
     * @param d      painted decoration state
     * @param bounds painting bounds
     * @param fm     font metrics
     * @return text Y coordinate within component bounds
     */
    protected int getTextY ( final C c, final D d, final Rectangle bounds, final FontMetrics fm )
    {
        int textY = bounds.y;

        // Adjusting coordinates according to vertical alignment
        final int va = getVerticalAlignment ( c, d );
        switch ( va )
        {
            case TOP:
                textY += fm.getAscent ();
                break;

            case CENTER:
                textY += Math.ceil ( ( bounds.height + fm.getAscent () - fm.getLeading () - fm.getDescent () ) / 2.0 );
                break;

            case BOTTOM:
                textY += bounds.height - fm.getHeight ();
                break;

            default:
                throw new DecorationException ( "Incorrect vertical alignment provided: " + va );
        }

        return textY;
    }

    /**
     * Returns text that will actually be painted.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param bounds    painting bounds
     * @param text      text to paint
     * @param fm        font metrics
     * @param width     text width
     * @param alignment horizontal text alignment
     * @return text that will actually be painted
     */
    protected String getPaintedText ( final C c, final D d, final Rectangle bounds, final String text, final FontMetrics fm,
                                      final int width, final int alignment )
    {
        final String paintedText;
        if ( isTruncate ( c, d ) && bounds.width < width )
        {
            paintedText = SwingUtilities.layoutCompoundLabel (
                    fm, text, null, 0, alignment, 0, 0,
                    bounds, new Rectangle (), new Rectangle (), 0
            );
        }
        else
        {
            paintedText = text;
        }
        return paintedText;
    }

    /**
     * Paints text fragment.
     *
     * @param c             painted component
     * @param d             painted decoration state
     * @param g2d           graphics context
     * @param text          text fragment
     * @param textX         text X coordinate
     * @param textY         text Y coordinate
     * @param mnemonicIndex index of mnemonic
     */
    protected void paintTextFragment ( final C c, final D d, final Graphics2D g2d, final String text, final int textX, final int textY,
                                       final int mnemonicIndex )
    {
        // Painting text shadow
        paintTextShadow ( c, d, g2d, text, textX, textY );

        // Painting text itself
        paintTextString ( c, d, g2d, text, textX, textY );

        // Painting mnemonic
        paintMnemonic ( c, d, g2d, text, mnemonicIndex, textX, textY );
    }

    /**
     * Draw a string with a blur or shadow effect. The light angle is assumed to be 0 degrees, (i.e., window is illuminated from top).
     * The effect is intended to be subtle to be usable in as many text components as possible. The effect is generated with multiple calls
     * to draw string. This method paints the text on coordinates {@code tx}, {@code ty}. If text should be painted elsewhere, a transform
     * should be applied to the graphics before passing it.
     *
     * @param c     painted component
     * @param d     painted decoration state
     * @param g2d   graphics context
     * @param text  text to paint
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintTextShadow ( final C c, final D d, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        if ( isShadow ( c, d ) )
        {
            // This is required to properly render sub-pixel text antialias
            final RenderingHints rh = g2d.getRenderingHints ();

            // Shadow settings
            final float opacity = getShadowOpacity ( c, d );
            final int size = getShadowSize ( c, d );
            final Color color = getShadowColor ( c, d );
            final double tx = -size;
            final double ty = 1 - size;
            /* todo final boolean isShadow = true; - replace with shadow type? #557 */

            // Configuring graphics
            final Composite oldComposite = g2d.getComposite ();
            final Color oldColor = g2d.getColor ();
            g2d.translate ( textX + tx, textY + ty );

            // Use a alpha blend smaller than 1 to prevent the effect from becoming too dark when multiple paints occur on top of each other
            float preAlpha = 0.4f;
            if ( oldComposite instanceof AlphaComposite )
            {
                final AlphaComposite alphaComposite = ( AlphaComposite ) oldComposite;
                if ( alphaComposite.getRule () == AlphaComposite.SRC_OVER )
                {
                    // Make sure alpha blend is adjusted by composite passed from above
                    preAlpha = alphaComposite.getAlpha () * preAlpha;
                }
            }
            g2d.setPaint ( ColorUtils.opaque ( color ) );

            // If the effect is a shadow it looks better to stop painting a bit earlier - shadow will look softer
            final int maxSize = /*isShadow ?*/ size - 1 /*: size*/;
            for ( int i = -size; i <= maxSize; i++ )
            {
                for ( int j = -size; j <= maxSize; j++ )
                {
                    final double distance = i * i + j * j;
                    float alpha;
                    if ( distance > 0.0d )
                    {
                        alpha = ( float ) ( 1.0f / ( distance * size * opacity ) );
                    }
                    else
                    {
                        alpha = opacity;
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
            g2d.translate ( -textX - tx, -textY - ty );
            g2d.setComposite ( oldComposite );
            g2d.setPaint ( oldColor );

            // This is required to properly render sub-pixel text antialias
            g2d.setRenderingHints ( rh );
        }
    }

    /**
     * Paints text string.
     *
     * @param c     painted component
     * @param d     painted decoration state
     * @param g2d   graphics context
     * @param text  text to paint
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintTextString ( final C c, final D d, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        g2d.drawString ( text, textX, textY );
    }

    /**
     * Paints underlined at the specified character index.
     *
     * @param c             painted component
     * @param d             painted decoration state
     * @param g2d           graphics context
     * @param text          text to paint
     * @param mnemonicIndex index of mnemonic
     * @param textX         text X coordinate
     * @param textY         text Y coordinate
     */
    protected void paintMnemonic ( final C c, final D d, final Graphics2D g2d, final String text, final int mnemonicIndex, final int textX,
                                   final int textY )
    {
        if ( mnemonicIndex >= 0 && mnemonicIndex < text.length () )
        {
            final FontMetrics fm = getFontMetrics ( c, d );
            g2d.fillRect ( textX + fm.stringWidth ( text.substring ( 0, mnemonicIndex ) ), textY + fm.getDescent () - 1,
                    fm.charWidth ( text.charAt ( mnemonicIndex ) ), 1 );
        }
    }

    @Override
    protected Dimension getContentPreferredSize ( final C c, final D d, final Dimension available )
    {
        final Dimension ps;
        if ( !isEmpty ( c, d ) )
        {
            final int w;
            final int h;
            if ( isHtmlText ( c, d ) )
            {
                final View html = getHtml ( c, d );
                w = ( int ) html.getPreferredSpan ( View.X_AXIS );
                h = ( int ) html.getPreferredSpan ( View.Y_AXIS );
                ps = new Dimension ( w, h );
            }
            else
            {
                final Dimension pts = getPreferredTextSize ( c, d, available );
                pts.width += isShadow ( c, d ) ? getShadowSize ( c, d ) * 2 : 0;
                ps = pts;
            }
        }
        else
        {
            ps = new Dimension ( 0, 0 );
        }
        return ps;
    }

    /**
     * Returns preferred text size.
     *
     * @param c         painted component
     * @param d         painted decoration state
     * @param available theoretically available space for this content
     * @return preferred text size
     */
    protected Dimension getPreferredTextSize ( final C c, final D d, final Dimension available )
    {
        final String text = getText ( c, d );
        final FontMetrics fm = getFontMetrics ( c, d );
        final int w = SwingUtils.stringWidth ( fm, text );
        final int h = fm.getHeight ();
        return new Dimension ( w, h );
    }
}