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

import com.alee.extended.painter.AbstractPainter;
import com.alee.extended.painter.Painter;
import com.alee.global.StyleConstants;
import com.alee.laf.label.WebLabelStyle;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.View;
import java.awt.*;
import java.util.Map;

/**
 * Web-style painter for JLabel component.
 * It is used as WebLabelUI default painter.
 *
 * @author Mikle Garin
 */

public class WebBasicLabelPainter<E extends JLabel, U extends BasicLabelUI> extends AbstractPainter<E, U>
{
    /**
     * Style settings.
     */
    protected boolean drawShade = WebLabelStyle.drawShade;
    protected Color shadeColor = WebLabelStyle.shadeColor;
    protected Float transparency = WebLabelStyle.transparency;
    protected Painter backgroundPainter = WebLabelStyle.backgroundPainter;

    /**
     * Runtime variables.
     */
    protected Rectangle paintIconR = new Rectangle ();
    protected Rectangle paintTextR = new Rectangle ();
    protected Rectangle paintViewR = new Rectangle ();

    /**
     * Constructs new WebLabelPainter with default settings.
     */
    public WebBasicLabelPainter ()
    {
        super ();
    }

    /**
     * Constructs new WebLabelPainter with the specified background painter.
     *
     * @param backgroundPainter background painter
     */
    public WebBasicLabelPainter ( final Painter backgroundPainter )
    {
        super ();
        setBackgroundPainter ( backgroundPainter );
    }

    /**
     * Returns whether text shade is displayed or not.
     *
     * @return true if text shade is displayed, false otherwise
     */
    public boolean isDrawShade ()
    {
        return drawShade;
    }

    /**
     * {@inheritDoc}
     */
    public void setDrawShade ( final boolean drawShade )
    {
        this.drawShade = drawShade;
    }

    /**
     * Returns text shade color.
     *
     * @return text shade color
     */
    public Color getShadeColor ()
    {
        return shadeColor;
    }

    /**
     * Sets text shade color.
     *
     * @param shadeColor text shade color
     */
    public void setShadeColor ( final Color shadeColor )
    {
        this.shadeColor = shadeColor;
    }

    /**
     * Returns label transparency.
     *
     * @return label transparency
     */
    public Float getTransparency ()
    {
        return transparency;
    }

    /**
     * Sets label transparency.
     *
     * @param transparency label transparency
     */
    public void setTransparency ( final Float transparency )
    {
        this.transparency = transparency;
    }

    /**
     * Returns label background painter.
     *
     * @return label background painter
     */
    public Painter getBackgroundPainter ()
    {
        return backgroundPainter;
    }

    /**
     * Sets label background painter.
     *
     * @param painter label background painter
     */
    public void setBackgroundPainter ( final Painter painter )
    {
        this.backgroundPainter = painter;
        updateAll ();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Boolean isOpaque ()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getBorders ()
    {
        return backgroundPainter != null ? backgroundPainter.getBorders () : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E label, final U ui )
    {
        // Applying graphics settings
        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, transparency, transparency != null );
        final Map textHints = drawShade ? StyleConstants.defaultTextRenderingHints : StyleConstants.textRenderingHints;
        final Font oldFont = GraphicsUtils.setupFont ( g2d, label.getFont () );
        final Map oldHints = SwingUtils.setupTextAntialias ( g2d, textHints );
        final Paint oldPaint = g2d.getPaint ();

        // Retrieving icon & text
        final String text = label.getText ();
        final Icon icon = ( label.isEnabled () ) ? label.getIcon () : label.getDisabledIcon ();

        // Painting background
        if ( backgroundPainter != null )
        {
            backgroundPainter.paint ( g2d, bounds, label, ui );
        }

        // We don't need to go futher if there is not icon/text
        if ( icon == null && text == null )
        {
            return;
        }

        final FontMetrics fm = label.getFontMetrics ( label.getFont () );
        final String clippedText = layout ( label, fm, label.getWidth (), label.getHeight () );

        if ( icon != null )
        {
            icon.paintIcon ( label, g2d, paintIconR.x, paintIconR.y );
        }

        if ( text != null )
        {
            final View v = ( View ) label.getClientProperty ( BasicHTML.propertyKey );
            if ( v != null )
            {
                // Painting HTML label view
                v.paint ( g2d, paintTextR );
            }
            else
            {
                paintText ( g2d, label, fm, clippedText );

            }
        }

        g2d.setPaint ( oldPaint );
        SwingUtils.restoreTextAntialias ( g2d, oldHints );
        GraphicsUtils.restoreFont ( g2d, oldFont );
        GraphicsUtils.restoreComposite ( g2d, oc, transparency != null );
    }

    /**
     * Paint the text.
     *
     * @param g2d         graphics context
     * @param label       label to process
     * @param fm          label font metrics
     * @param clippedText clipped label text
     */
    protected void paintText ( final Graphics2D g2d, final E label, final FontMetrics fm, final String clippedText )
    {
        // Painting plain label view
        final int textX = paintTextR.x;
        final int textY = paintTextR.y + fm.getAscent ();
        if ( label.isEnabled () )
        {
            paintEnabledText ( label, g2d, clippedText, textX, textY );
        }
        else
        {
            paintDisabledText ( label, g2d, clippedText, textX, textY );
        }
    }

    /**
     * Updates painted label layout and returns clipped or full label text.
     *
     * @param label  label to process
     * @param fm     label font metrics
     * @param width  label width
     * @param height label height
     * @return clipped or full label text
     */
    protected String layout ( final E label, final FontMetrics fm, final int width, final int height )
    {
        final Insets insets = label.getInsets ( null );
        final String text = label.getText ();
        final Icon icon = ( label.isEnabled () ) ? label.getIcon () : label.getDisabledIcon ();
        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = width - ( insets.left + insets.right );
        paintViewR.height = height - ( insets.top + insets.bottom );
        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;
        return layoutCL ( label, fm, text, icon, paintViewR, paintIconR, paintTextR );
    }

    /**
     * Performs label layout and returns clipped or full label text.
     *
     * @param label label to process
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
     * @param label label to process
     * @param g2d   graphics context
     * @param text  label text
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintEnabledText ( final E label, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        if ( drawShade )
        {
            g2d.setColor ( label.getForeground () );
            paintShadowText ( g2d, text, textX, textY );
        }
        else
        {
            final int mnemIndex = label.getDisplayedMnemonicIndex ();
            g2d.setColor ( label.getForeground () );
            SwingUtils.drawStringUnderlineCharAt ( g2d, text, mnemIndex, textX, textY );
        }
    }

    /**
     * Performs disabled text painting.
     *
     * @param label label to process
     * @param g2d   graphics context
     * @param text  label text
     * @param textX text X coordinate
     * @param textY text Y coordinate
     */
    protected void paintDisabledText ( final E label, final Graphics2D g2d, final String text, final int textX, final int textY )
    {
        if ( label.isEnabled () && drawShade )
        {
            g2d.setColor ( label.getBackground ().darker () );
            paintShadowText ( g2d, text, textX, textY );
        }
        else
        {
            final int accChar = label.getDisplayedMnemonicIndex ();
            final Color background = label.getBackground ();
            g2d.setColor ( background.brighter () );
            SwingUtils.drawStringUnderlineCharAt ( g2d, text, accChar, textX + 1, textY + 1 );
            g2d.setColor ( background.darker () );
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
        LafUtils.paintTextShadow ( g2d, text, shadeColor );
        g2d.translate ( -textX, -textY );
    }
}