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

import com.alee.extended.label.MultiLineLabelPainter;
import com.alee.extended.label.WebMultiLineLabelUI;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
import java.util.List;

/**
 * Web-style painter for JLabel component.
 * It is used as WebMultiLineLabelUI default painter.
 *
 * @author Mikle Garin
 */

public class WebMultiLineLabelPainter<E extends JLabel, U extends WebMultiLineLabelUI> extends WebBasicLabelPainter<E, U>
        implements MultiLineLabelPainter<E, U>
{
    /**
     * Runtime variables.
     */
    protected Insets paintViewInsets = new Insets ( 0, 0, 0, 0 );

    /**
     * Paint the wrapped text lines.
     *
     * @param g2d         graphics component to paint on
     * @param label       the label being painted
     * @param fm          font metrics for current font
     * @param clippedText clipped label text
     */
    @Override
    protected void paintText ( final Graphics2D g2d, final E label, final FontMetrics fm, final String clippedText )
    {
        final List<String> lines = ui.getTextLines ( label );

        // Available component height to paint on.
        final int height = getAvailableHeight ( label );

        int textHeight = lines.size () * fm.getHeight ();
        while ( textHeight > height )
        {
            // Remove one line until no. of visible lines is found.
            textHeight -= fm.getHeight ();
        }
        paintTextR.height = Math.min ( textHeight, height );
        paintTextR.y = alignmentY ( label, fm, paintTextR );

        final int textX = paintTextR.x;
        int textY = paintTextR.y;

        for ( final Iterator<String> it = lines.iterator (); it.hasNext () && paintTextR.contains ( textX, textY + getAscent ( fm ) );
              textY += fm.getHeight () )
        {

            String text = it.next ().trim ();

            if ( it.hasNext () && !paintTextR.contains ( textX, textY + fm.getHeight () + getAscent ( fm ) ) )
            {
                // The last visible row, add a clip indication.
                text = clip ( text );
            }

            final int x = alignmentX ( label, fm, text );

            if ( label.isEnabled () )
            {
                paintEnabledText ( label, g2d, text, x, textY );
            }
            else
            {
                paintDisabledText ( label, g2d, text, x, textY );
            }
        }
    }

    /**
     * Add a clip indication to the string. It is important that the string length does not exceed the length or the original string.
     *
     * @param text the to be painted
     * @return the clipped string
     */
    protected String clip ( final String text )
    {
        // Fast and lazy way to insert a clip indication is to simply replace
        // the last characters in the string with the clip indication.
        // A better way would be to use metrics and calculate how many (if any)
        // characters that need to be replaced.
        if ( text.length () < 3 )
        {
            return "...";
        }
        return text.substring ( 0, text.length () - 3 ) + "...";
    }

    /**
     * Establish the horizontal text alignment. The default alignment is left aligned text.
     *
     * @param label the label to paint
     * @param fm    font metrics
     * @param s     the string to paint
     * @return the x-coordinate to use when painting for proper alignment
     */
    protected int alignmentX ( final JLabel label, final FontMetrics fm, final String s )
    {
        final boolean ltr = label.getComponentOrientation ().isLeftToRight ();
        final int align = label.getHorizontalAlignment ();
        if ( align == JLabel.RIGHT || align == JLabel.TRAILING && ltr ||
                align == JLabel.LEADING && !ltr )
        {
            return paintViewR.width - fm.stringWidth ( s );
        }
        else if ( align == JLabel.CENTER )
        {
            return paintViewR.width / 2 - fm.stringWidth ( s ) / 2;
        }
        else
        {
            return paintViewR.x;
        }
    }

    /**
     * Establish the vertical text alignment. The default alignment is to center the text in the label.
     *
     * @param label  the label to paint
     * @param fm     font metrics
     * @param bounds the text bounds rectangle
     * @return the vertical text alignment, defaults to CENTER.
     */
    protected int alignmentY ( final JLabel label, final FontMetrics fm, final Rectangle bounds )
    {
        final int height = getAvailableHeight ( label );
        final int textHeight = bounds.height;

        final int align = label.getVerticalAlignment ();
        switch ( align )
        {
            case JLabel.TOP:
                return getAscent ( fm ) + paintViewInsets.top;
            case JLabel.BOTTOM:
                return getAscent ( fm ) + height - paintViewInsets.top + paintViewInsets.bottom - textHeight;
            default:
        }

        // Center alignment
        final int textY = paintViewInsets.top + ( height - textHeight ) / 2 + getAscent ( fm );
        return Math.max ( textY, getAscent ( fm ) + paintViewInsets.top );
    }

    /**
     * Returns the available height to paint text on. This is the height of the passed component with insets subtracted.
     *
     * @param l a component
     * @return the available height
     */
    protected int getAvailableHeight ( final JLabel l )
    {
        l.getInsets ( paintViewInsets );
        return l.getHeight () - paintViewInsets.top - paintViewInsets.bottom;
    }

    protected int getAscent ( final FontMetrics fm )
    {
        return fm.getAscent () + fm.getLeading ();
    }
}