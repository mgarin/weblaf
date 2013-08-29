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

/*
 * The MIT License
 *
 * Copyright (c) 2009 Samuel Sjoberg
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.alee.extended.label;

import com.alee.laf.label.WebLabelStyle;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Label UI delegate that supports multiple lines and line wrapping. Hard line breaks (<code>\n</code>) are preserved. If the dimensions of
 * the label is too small to fit all content, the string will be clipped and "..." appended to the end of the visible text (similar to the
 * default behavior of <code>JLabel</code>).
 *
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 */

public class WebMultiLineLabelUI extends BasicLabelUI implements ComponentListener
{
    /**
     * Client property key used to store the calculated wrapped lines on the JLabel.
     */
    public static final String PROPERTY_KEY = "WrappedText";

    // Static references to avoid heap allocations.
    protected static Rectangle paintIconR = new Rectangle ();
    protected static Rectangle paintTextR = new Rectangle ();
    protected static Rectangle paintViewR = new Rectangle ();
    protected static Insets paintViewInsets = new Insets ( 0, 0, 0, 0 );

    // Variables
    private static int defaultSize = 4;
    private FontMetrics metrics;

    // View settings
    private boolean drawShade = WebLabelStyle.drawShade;
    private Color shadeColor = WebLabelStyle.shadeColor;

    /**
     * UI instance creation.
     *
     * @param c the component about to be installed
     * @return the shared UI delegate instance
     */
    @SuppressWarnings ("UnusedParameters")
    public static ComponentUI createUI ( JComponent c )
    {
        return new WebMultiLineLabelUI ();
    }

    @Override
    public void installUI ( JComponent c )
    {
        super.installUI ( c );

        // Default settings
        SwingUtils.setOrientation ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void uninstallDefaults ( JLabel c )
    {
        super.uninstallDefaults ( c );
        clearCache ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void installListeners ( JLabel c )
    {
        super.installListeners ( c );
        c.addComponentListener ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void uninstallListeners ( JLabel c )
    {
        super.uninstallListeners ( c );
        c.removeComponentListener ( this );
    }

    /**
     * View settings
     */

    public boolean isDrawShade ()
    {
        return drawShade;
    }

    public void setDrawShade ( boolean drawShade )
    {
        this.drawShade = drawShade;
    }

    public Color getShadeColor ()
    {
        return shadeColor;
    }

    public void setShadeColor ( Color shadeColor )
    {
        this.shadeColor = shadeColor;
    }

    /**
     * Clear the wrapped line cache.
     *
     * @param l the label containing a cached value
     */
    protected void clearCache ( JLabel l )
    {
        l.putClientProperty ( PROPERTY_KEY, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange ( PropertyChangeEvent e )
    {
        super.propertyChange ( e );
        final String name = e.getPropertyName ();
        if ( name.equals ( "text" ) || "font".equals ( name ) )
        {
            clearCache ( ( JLabel ) e.getSource () );
        }
    }

    /**
     * Calculate the paint rectangles for the icon and text for the passed label.
     *
     * @param l      a label
     * @param fm     the font metrics to use, or <code>null</code> to get the font metrics from the label
     * @param width  label width
     * @param height label height
     */
    protected void updateLayout ( JLabel l, FontMetrics fm, int width, int height )
    {
        if ( fm == null )
        {
            fm = l.getFontMetrics ( l.getFont () );
        }
        metrics = fm;

        String text = l.getText ();
        Icon icon = l.getIcon ();
        Insets insets = l.getInsets ( paintViewInsets );

        paintViewR.x = insets.left;
        paintViewR.y = insets.top;
        paintViewR.width = width - ( insets.left + insets.right );
        paintViewR.height = height - ( insets.top + insets.bottom );

        paintIconR.x = paintIconR.y = paintIconR.width = paintIconR.height = 0;
        paintTextR.x = paintTextR.y = paintTextR.width = paintTextR.height = 0;

        layoutCL ( l, fm, text, icon, paintViewR, paintIconR, paintTextR );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( Graphics g, JComponent c )
    {
        Map hints = SwingUtils.setupTextAntialias ( g );

        JLabel label = ( JLabel ) c;
        String text = label.getText ();
        Icon icon = ( label.isEnabled () ) ? label.getIcon () : label.getDisabledIcon ();

        if ( ( icon == null ) && ( text == null ) )
        {
            return;
        }

        FontMetrics fm = g.getFontMetrics ();

        updateLayout ( label, fm, c.getWidth (), c.getHeight () );

        if ( icon != null )
        {
            icon.paintIcon ( c, g, paintIconR.x, paintIconR.y );
        }

        if ( text != null )
        {
            View v = ( View ) c.getClientProperty ( "html" );
            if ( v != null )
            {
                // HTML view disables multi-line painting.
                v.paint ( g, paintTextR );
            }
            else
            {
                // Paint the multi line text
                paintTextLines ( g, label, fm );
            }
        }

        SwingUtils.restoreTextAntialias ( g, hints );
    }

    /**
     * Paint the wrapped text lines.
     *
     * @param g     graphics component to paint on
     * @param label the label being painted
     * @param fm    font metrics for current font
     */
    protected void paintTextLines ( Graphics g, JLabel label, FontMetrics fm )
    {
        List<String> lines = getTextLines ( label );

        // Available component height to paint on.
        int height = getAvailableHeight ( label );

        int textHeight = lines.size () * fm.getHeight ();
        while ( textHeight > height )
        {
            // Remove one line until no. of visible lines is found.
            textHeight -= fm.getHeight ();
        }
        paintTextR.height = Math.min ( textHeight, height );
        paintTextR.y = alignmentY ( label, fm, paintTextR );

        int textX = paintTextR.x;
        int textY = paintTextR.y;

        for ( Iterator<String> it = lines.iterator (); it.hasNext () && paintTextR.contains ( textX, textY + getAscent ( fm ) );
              textY += fm.getHeight () )
        {

            String text = it.next ().trim ();

            if ( it.hasNext () && !paintTextR.contains ( textX, textY + fm.getHeight () + getAscent ( fm ) ) )
            {
                // The last visible row, add a clip indication.
                text = clip ( text, fm );
            }

            int x = alignmentX ( label, fm, text );

            if ( label.isEnabled () )
            {
                paintEnabledText ( label, g, text, x, textY );
            }
            else
            {
                paintDisabledText ( label, g, text, x, textY );
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintEnabledText ( JLabel l, Graphics g, String s, int textX, int textY )
    {
        if ( drawShade )
        {
            g.setColor ( l.getForeground () );
            paintShadowText ( g, s, textX, textY );
        }
        else
        {
            int mnemIndex = l.getDisplayedMnemonicIndex ();
            g.setColor ( l.getForeground () );
            SwingUtils.drawStringUnderlineCharAt ( g, s, mnemIndex, textX, textY );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void paintDisabledText ( JLabel l, Graphics g, String s, int textX, int textY )
    {
        if ( drawShade )
        {
            g.setColor ( l.getBackground ().darker () );
            paintShadowText ( g, s, textX, textY );
        }
        else
        {
            int mnemIndex = l.getDisplayedMnemonicIndex ();
            g.setColor ( l.getForeground () );
            SwingUtils.drawStringUnderlineCharAt ( g, s, mnemIndex, textX, textY );
        }
    }

    /**
     * Paint the text with a text effect.
     *
     * @param g     graphics component used to paint on
     * @param s     the string to paint
     * @param textX the x coordinate
     * @param textY the y coordinate
     */
    private void paintShadowText ( Graphics g, String s, int textX, int textY )
    {
        g.translate ( textX, textY );
        LafUtils.paintTextShadow ( ( Graphics2D ) g, s, shadeColor );
        g.translate ( -textX, -textY );
    }

    /**
     * Returns the available height to paint text on. This is the height of the passed component with insets subtracted.
     *
     * @param l a component
     * @return the available height
     */
    protected int getAvailableHeight ( JLabel l )
    {
        l.getInsets ( paintViewInsets );
        return l.getHeight () - paintViewInsets.top - paintViewInsets.bottom;
    }

    /**
     * Add a clip indication to the string. It is important that the string length does not exceed the length or the original string.
     *
     * @param text the to be painted
     * @param fm   font metrics
     * @return the clipped string
     */
    protected String clip ( String text, FontMetrics fm )
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
     * Establish the vertical text alignment. The default alignment is to center the text in the label.
     *
     * @param label  the label to paint
     * @param fm     font metrics
     * @param bounds the text bounds rectangle
     * @return the vertical text alignment, defaults to CENTER.
     */
    protected int alignmentY ( JLabel label, FontMetrics fm, Rectangle bounds )
    {
        final int height = getAvailableHeight ( label );
        int textHeight = bounds.height;

        int align = label.getVerticalAlignment ();
        switch ( align )
        {
            case JLabel.TOP:
                return getAscent ( fm ) + paintViewInsets.top;
            case JLabel.BOTTOM:
                return getAscent ( fm ) + height - paintViewInsets.top + paintViewInsets.bottom - textHeight;
            default:
        }

        // Center alignment
        int textY = paintViewInsets.top + ( height - textHeight ) / 2 + getAscent ( fm );
        return Math.max ( textY, getAscent ( fm ) + paintViewInsets.top );
    }

    private static int getAscent ( FontMetrics fm )
    {
        return fm.getAscent () + fm.getLeading ();
    }

    /**
     * Establish the horizontal text alignment. The default alignment is left aligned text.
     *
     * @param label the label to paint
     * @param fm    font metrics
     * @param s     the string to paint
     * @return the x-coordinate to use when painting for proper alignment
     */
    protected int alignmentX ( JLabel label, FontMetrics fm, String s )
    {
        boolean ltr = label.getComponentOrientation ().isLeftToRight ();
        int align = label.getHorizontalAlignment ();
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
     * Check the given string to see if it should be rendered as HTML. Code based on implementation found in
     * <code>BasicHTML.isHTMLString(String)</code> in future JDKs.
     *
     * @param s the string
     * @return <code>true</code> if string is HTML, otherwise <code>false</code>
     */
    private static boolean isHTMLString ( String s )
    {
        if ( s != null )
        {
            if ( ( s.length () >= 6 ) && ( s.charAt ( 0 ) == '<' ) && ( s.charAt ( 5 ) == '>' ) )
            {
                String tag = s.substring ( 1, 5 );
                return tag.equalsIgnoreCase ( "html" );
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( JComponent c )
    {
        Dimension d = super.getPreferredSize ( c );
        JLabel label = ( JLabel ) c;

        if ( isHTMLString ( label.getText () ) )
        {
            return d; // HTML overrides everything and we don't need to process
        }

        // Width calculated by super is OK. The preferred width is the width of
        // the unwrapped content as long as it does not exceed the width of the
        // parent container.

        if ( c.getParent () != null )
        {
            // Ensure that preferred width never exceeds the available width
            // (including its border insets) of the parent container.
            Insets insets = c.getParent ().getInsets ();
            Dimension size = c.getParent ().getSize ();
            if ( size.width > 0 )
            {
                // If width isn't set component shouldn't adjust.
                d.width = size.width - insets.left - insets.right;
            }
        }

        updateLayout ( label, null, d.width, d.height );

        // The preferred height is either the preferred height of the text
        // lines, or the height of the icon.
        d.height = Math.max ( d.height, getPreferredHeight ( label ) );

        return d;
    }

    /**
     * The preferred height of the label is the height of the lines with added top and bottom insets.
     *
     * @param label the label
     * @return the preferred height of the wrapped lines.
     */
    protected int getPreferredHeight ( JLabel label )
    {
        int numOfLines = getTextLines ( label ).size ();
        Insets insets = label.getInsets ( paintViewInsets );
        return numOfLines * metrics.getHeight () + insets.top + insets.bottom;
    }

    /**
     * Get the lines of text contained in the text label. The prepared lines is cached as a client property, accessible via {@link
     * #PROPERTY_KEY}.
     *
     * @param l the label
     * @return the text lines of the label.
     */
    @SuppressWarnings ("unchecked")
    protected List<String> getTextLines ( JLabel l )
    {
        List<String> lines = ( List<String> ) l.getClientProperty ( PROPERTY_KEY );
        if ( lines == null )
        {
            lines = prepareLines ( l );
            l.putClientProperty ( PROPERTY_KEY, lines );
        }
        return lines;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentHidden ( ComponentEvent e )
    {
        // Don't care
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentMoved ( ComponentEvent e )
    {
        // Don't care
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentResized ( ComponentEvent e )
    {
        clearCache ( ( JLabel ) e.getSource () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentShown ( ComponentEvent e )
    {
        // Don't care
    }

    /**
     * Prepare the text lines for rendering. The lines are wrapped to fit in the current available space for text. Explicit line breaks are
     * preserved.
     *
     * @param l the label to render
     * @return a list of text lines to render
     */
    protected List<String> prepareLines ( JLabel l )
    {
        List<String> lines = new ArrayList<String> ( defaultSize );
        String text = l.getText ();
        if ( text == null )
        {
            return null; // Null guard
        }
        PlainDocument doc = new PlainDocument ();
        try
        {
            doc.insertString ( 0, text, null );
        }
        catch ( BadLocationException e )
        {
            return null;
        }
        Element root = doc.getDefaultRootElement ();
        for ( int i = 0, j = root.getElementCount (); i < j; i++ )
        {
            wrap ( lines, root.getElement ( i ) );
        }
        return lines;
    }

    /**
     * If necessary, wrap the text into multiple lines.
     *
     * @param lines line array in which to store the wrapped lines
     * @param elem  the document element containing the text content
     */
    protected void wrap ( List<String> lines, Element elem )
    {
        int p1 = elem.getEndOffset ();
        Document doc = elem.getDocument ();
        for ( int p0 = elem.getStartOffset (); p0 < p1; )
        {
            int p = calculateBreakPosition ( doc, p0, p1 );
            try
            {
                lines.add ( doc.getText ( p0, p - p0 ) );
            }
            catch ( BadLocationException e )
            {
                throw new Error ( "Can't get line text. p0=" + p0 + " p=" + p );
            }
            p0 = ( p == p0 ) ? p1 : p;
        }
    }

    /**
     * Calculate the position on which to break (wrap) the line.
     *
     * @param doc the document
     * @param p0  start position
     * @param p1  end position
     * @return the actual end position, will be <code>p1</code> if content does not need to wrap, otherwise it will be less than
     *         <code>p1</code>.
     */
    protected int calculateBreakPosition ( Document doc, int p0, int p1 )
    {
        Segment segment = SegmentCache.getSegment ();
        try
        {
            doc.getText ( p0, p1 - p0, segment );
        }
        catch ( BadLocationException e )
        {
            throw new Error ( "Can't get line text" );
        }

        int width = paintTextR.width;
        int p = p0 + Utilities.getBreakLocation ( segment, metrics, 0, width, null, p0 );
        SegmentCache.releaseSegment ( segment );
        return p;
    }

    /**
     * Static singleton {@link Segment} cache.
     *
     * @author Samuel Sjoberg
     * @see javax.swing.text.SegmentCache
     */
    protected static final class SegmentCache
    {
        /**
         * Reused segments.
         */
        private ArrayList<Segment> segments = new ArrayList<Segment> ( 2 );

        /**
         * Singleton instance.
         */
        private static SegmentCache cache = new SegmentCache ();

        /**
         * Private constructor.
         */
        private SegmentCache ()
        {
            //
        }

        /**
         * Returns a <code>Segment</code>. When done, the <code>Segment</code> should be recycled by invoking {@link
         * #releaseSegment(Segment)}.
         *
         * @return a <code>Segment</code>.
         */
        public static Segment getSegment ()
        {
            int size = cache.segments.size ();
            if ( size > 0 )
            {
                return cache.segments.remove ( size - 1 );
            }
            return new Segment ();
        }

        /**
         * Releases a <code>Segment</code>. A segment should not be used after it is released, and a segment should never be released more
         * than once.
         */
        public static void releaseSegment ( Segment segment )
        {
            segment.array = null;
            segment.count = 0;
            cache.segments.add ( segment );
        }
    }
}