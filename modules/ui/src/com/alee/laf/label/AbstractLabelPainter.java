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

package com.alee.laf.label;

import com.alee.global.StyleConstants;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * Abstract painter for label components.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public abstract class AbstractLabelPainter<E extends JLabel, U extends BasicLabelUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IAbstractLabelPainter<E, U>
{
    /**
     * Style settings.
     */
    protected boolean drawShade;
    protected Color shadeColor;
    protected int shadeSize;
    protected Rotation rotation;

    /**
     * Runtime variables.
     */
    protected Rectangle paintIconR = new Rectangle ();
    protected Rectangle paintTextR = new Rectangle ();
    protected Rectangle paintViewR = new Rectangle ();

    /**
     * Returns actual label rotation.
     *
     * @return actual label rotation
     */
    protected Rotation getActualRotation ()
    {
        return rotation != null ? ltr ? rotation : rotation.rightToLeft () : Rotation.none;
    }

    @Override
    protected Boolean isOpaqueUndecorated ()
    {
        return false;
    }

    @Override
    public Insets getCompleteBorder ()
    {
        final Insets border = super.getCompleteBorder ();
        if ( border != null )
        {
            switch ( getActualRotation () )
            {
                case counterClockwise:
                    return i ( border.left, border.bottom, border.right, border.top );

                case upsideDown:
                    return i ( border.bottom, border.right, border.top, border.left );

                case clockwise:
                    return i ( border.right, border.top, border.left, border.bottom );
            }
        }
        return border;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Applying graphics settings
        final Font oldFont = GraphicsUtils.setupFont ( g2d, c.getFont () );
        final Paint oldPaint = g2d.getPaint ();

        // Paint background
        paintBackground ( g2d, bounds, c, ui );

        // Retrieving icon & text
        final String text = c.getText ();
        final Icon icon = ( c.isEnabled () ) ? c.getIcon () : c.getDisabledIcon ();

        // Check icon/text existance
        if ( icon == null && text == null )
        {
            return;
        }

        // Applying label rotation
        final Rotation rotation = getActualRotation ();
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
            g2d.rotate ( angle, rX / 2, rY / 2 );
        }

        // Layouting label elements before painting them
        final FontMetrics fm = c.getFontMetrics ( c.getFont () );
        final String clippedText = layout ( c, fm, c.getWidth (), c.getHeight () );

        // Painting icon and text
        paintIcon ( g2d, c, icon );
        paintText ( g2d, c, clippedText, fm );

        // Restoring graphics settings
        g2d.setPaint ( oldPaint );
        GraphicsUtils.restoreFont ( g2d, oldFont );
    }

    /**
     * Paints background.
     * todo Should be removed upon as soon as decoration supports all kinds of borders used in extending painters so far
     *
     * @param g2d    graphics context
     * @param bounds bounds for painter visual data
     * @param label  painted component
     * @param ui     painted component UI
     */
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final E label, final U ui )
    {
        // Basic label doesn't have any background
    }

    /**
     * Paints label icon.
     *
     * @param g2d   graphics context
     * @param label painted component
     * @param icon  label icon
     */
    protected void paintIcon ( final Graphics2D g2d, final E label, final Icon icon )
    {
        if ( icon != null )
        {
            icon.paintIcon ( label, g2d, paintIconR.x, paintIconR.y );
        }
    }

    /**
     * Paints label text.
     *
     * @param g2d   graphics context
     * @param label painted component
     * @param text  label text
     * @param fm    label font metrics
     */
    protected void paintText ( final Graphics2D g2d, final E label, final String text, final FontMetrics fm )
    {
        if ( text != null )
        {
            final Map textHints = drawShade ? StyleConstants.defaultTextRenderingHints : StyleConstants.textRenderingHints;
            final Map oldHints = SwingUtils.setupTextAntialias ( g2d, textHints );
            if ( isHtmlText ( label ) )
            {
                paintHtmlText ( g2d, label );
            }
            else
            {
                paintPlainText ( g2d, label, text, fm );
            }
            SwingUtils.restoreTextAntialias ( g2d, oldHints );
        }
    }

    /**
     * Returns whether or not label contains HTML text.
     *
     * @param label painted component
     * @return true if label contains HTML text, false otherwise
     */
    protected boolean isHtmlText ( final E label )
    {
        return label.getClientProperty ( BasicHTML.propertyKey ) != null;
    }

    /**
     * Paints HTML text view.
     *
     * @param g2d   graphics context
     * @param label painted component
     */
    protected void paintHtmlText ( final Graphics2D g2d, final E label )
    {
        final View v = ( View ) label.getClientProperty ( BasicHTML.propertyKey );
        v.paint ( g2d, paintTextR );
    }

    /**
     * Paints plain text view.
     *
     * @param g2d   graphics context
     * @param label painted component
     * @param text  label text
     * @param fm    label font metrics
     */
    protected void paintPlainText ( final Graphics2D g2d, final E label, final String text, final FontMetrics fm )
    {
        final int textX = paintTextR.x;
        final int textY = paintTextR.y + fm.getAscent ();
        if ( label.isEnabled () )
        {
            paintEnabledText ( label, g2d, text, textX, textY );
        }
        else
        {
            paintDisabledText ( label, g2d, text, textX, textY );
        }
    }

    /**
     * Updates painted label layout and returns clipped or full label text.
     *
     * @param label  painted component
     * @param fm     label font metrics
     * @param width  label width
     * @param height label height
     * @return clipped or full label text
     */
    protected String layout ( final E label, final FontMetrics fm, final int width, final int height )
    {
        final Insets insets = label.getInsets ( null );
        final Icon icon = ( label.isEnabled () ) ? label.getIcon () : label.getDisabledIcon ();
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;

        if ( getActualRotation ().isVertical () )
        {
            paintViewR.width = height;
            paintViewR.height = width;
        }
        else
        {
            paintViewR.width = width;
            paintViewR.height = height;
        }

        paintViewR.width -= insets.left + insets.right;
        paintViewR.height -= insets.top + insets.bottom;

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        return layoutCL ( label, fm, label.getText (), icon, paintViewR, paintIconR, paintTextR );
    }

    /**
     * Performs label layout and returns clipped or full label text.
     *
     * @param label painted component
     * @param fm    label font metrics
     * @param text  label text
     * @param icon  label icon
     * @param viewR rectangle limited by label insets
     * @param iconR icon rectangle dummy
     * @param textR text rectangle dummy
     * @return clipped or full label text
     */
    protected String layoutCL ( final E label, final FontMetrics fm, final String text, final Icon icon, final Rectangle viewR,
                                final Rectangle iconR, final Rectangle textR )
    {
        return SwingUtilities.layoutCompoundLabel ( label, fm, text, icon, label.getVerticalAlignment (), label.getHorizontalAlignment (),
                label.getVerticalTextPosition (), label.getHorizontalTextPosition (), viewR, iconR, textR, label.getIconTextGap () );
    }

    /**
     * Performs enabled text painting.
     *
     * @param label painted component
     * @param g2d   graphics context
     * @param text  label text
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintEnabledText ( final E label, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        if ( drawShade )
        {
            g2d.setPaint ( label.getForeground () );
            paintShadowText ( g2d, text, textX, textY );
        }
        else
        {
            final int mnemIndex = label.getDisplayedMnemonicIndex ();
            g2d.setPaint ( label.getForeground () );
            SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemIndex, textX, textY );
        }
    }

    /**
     * Performs disabled text painting.
     *
     * @param label painted component
     * @param g2d   graphics context
     * @param text  label text
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintDisabledText ( final E label, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        if ( label.isEnabled () && drawShade )
        {
            g2d.setPaint ( label.getBackground ().darker () );
            paintShadowText ( g2d, text, textX, textY );
        }
        else
        {
            final int accChar = label.getDisplayedMnemonicIndex ();
            final Color background = label.getBackground ();
            g2d.setPaint ( background.brighter () );
            SwingUtils.drawStringUnderlineCharAt ( g2d, text, accChar, textX + 1, textY + 1 );
            g2d.setPaint ( background.darker () );
            SwingUtils.drawStringUnderlineCharAt ( g2d, text, accChar, textX, textY );
        }
    }

    /**
     * Paints custom text shade.
     *
     * @param g2d   graphics context
     * @param text  text
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintShadowText ( final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        g2d.translate ( textX, textY );
        GraphicsUtils.paintTextEffect ( g2d, text, shadeColor, shadeSize, -shadeSize, 1 - shadeSize, true );
        g2d.translate ( -textX, -textY );
    }

    @Override
    public Dimension getPreferredSize ()
    {
        Dimension ps = getContentSize ();

        final Insets cb = getCompleteBorder ();
        if ( cb != null )
        {
            ps.width += cb.left + cb.right;
            ps.height += cb.top + cb.bottom;
        }

        if ( getActualRotation ().isVertical () )
        {
            ps = transposeDimension ( ps );
        }

        return ps;
    }

    /**
     * Returns label content size.
     *
     * @return label content size.
     */
    protected Dimension getContentSize ()
    {
        final String text = component.getText ();
        final Icon icon = ( component.isEnabled () ) ? component.getIcon () : component.getDisabledIcon ();
        final Font font = component.getFont ();

        if ( ( icon == null ) && ( ( text == null ) || ( ( text != null ) && ( font == null ) ) ) )
        {
            return new Dimension ( 0, 0 );
        }
        else if ( ( text == null ) || ( ( icon != null ) && ( font == null ) ) )
        {
            return new Dimension ( icon.getIconWidth (), icon.getIconHeight () );
        }
        else
        {
            final FontMetrics fm = component.getFontMetrics ( font );

            final Rectangle iconR = new Rectangle ();
            final Rectangle textR = new Rectangle ();
            final Rectangle viewR = new Rectangle ();
            iconR.x = iconR.y = iconR.width = iconR.height = 0;
            textR.x = textR.y = textR.width = textR.height = 0;
            viewR.x = 0;
            viewR.y = 0;
            viewR.width = viewR.height = Short.MAX_VALUE;

            layoutCL ( component, fm, text, icon, viewR, iconR, textR );
            final int x1 = Math.min ( iconR.x, textR.x );
            final int x2 = Math.max ( iconR.x + iconR.width, textR.x + textR.width );
            final int y1 = Math.min ( iconR.y, textR.y );
            final int y2 = Math.max ( iconR.y + iconR.height, textR.y + textR.height );
            return new Dimension ( x2 - x1, y2 - y1 );
        }
    }

    /**
     * Returns transposed dimension.
     *
     * @param from dimension to transpose
     * @return transposed dimension
     */
    protected Dimension transposeDimension ( final Dimension from )
    {
        return new Dimension ( from.height, from.width );
    }
}