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

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PainterSupport;
import com.alee.managers.style.StyleManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.laf.MarginSupport;
import com.alee.utils.laf.PaddingSupport;
import com.alee.utils.laf.ShapeProvider;
import com.alee.utils.laf.Styleable;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicLabelUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Label UI delegate that supports multiple lines and line wrapping. Hard line breaks (<code>\n</code>) are preserved. If the dimensions of
 * the label is too small to fit all content, the string will be clipped and "..." appended to the end of the visible text (similar to the
 * default behavior of <code>JLabel</code>).
 *
 * @author Samuel Sjoberg, http://samuelsjoberg.com
 */

public class WebMultiLineLabelUI extends BasicLabelUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport, ComponentListener
{
    /**
     * Client property key used to store the calculated wrapped lines on the JLabel.
     */
    public static final String PROPERTY_KEY = "WrappedText";

    // Static references to avoid heap allocations.
    protected static Insets paintViewInsets = new Insets ( 0, 0, 0, 0 );

    // Variables
    private static final int defaultSize = 4;

    /**
     * Component painter.
     */
    protected MultiLineLabelPainter painter;

    /**
     * Runtime variables.
     */
    protected String styleId = null;
    protected JLabel label;
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * UI instance creation.
     *
     * @param c the component about to be installed
     * @return the shared UI delegate instance
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebMultiLineLabelUI ();
    }

    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Saving label reference
        label = ( JLabel ) c;

        // Applying skin
        StyleManager.applySkin ( label );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void uninstallDefaults ( final JLabel c )
    {
        // Uninstalling applied skin
        StyleManager.removeSkin ( label );

        // Removing label reference
        label = null;

        // Uninstalling UI
        super.uninstallDefaults ( c );
        clearCache ( c );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getStyleId ()
    {
        return styleId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setStyleId ( final String id )
    {
        if ( !CompareUtils.equals ( this.styleId, id ) )
        {
            this.styleId = id;
            StyleManager.applySkin ( label );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( label, painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns label painter.
     *
     * @return label painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets label painter.
     * Pass null to remove label painter.
     *
     * @param painter new label painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( label, new DataRunnable<MultiLineLabelPainter> ()
        {
            @Override
            public void run ( final MultiLineLabelPainter newPainter )
            {
                WebMultiLineLabelUI.this.painter = newPainter;
            }
        }, this.painter, painter, MultiLineLabelPainter.class, AdaptiveMultiLineLabelPainter.class );
    }

    /**
     * Paints label.
     *
     * @param g graphics
     * @param c component
     */
    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            painter.paint ( ( Graphics2D ) g, SwingUtils.size ( c ), c, this );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void installListeners ( final JLabel c )
    {
        super.installListeners ( c );
        c.addComponentListener ( this );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void uninstallListeners ( final JLabel c )
    {
        super.uninstallListeners ( c );
        c.removeComponentListener ( this );
    }

    /**
     * View settings
     */

    /**
     * Clear the wrapped line cache.
     *
     * @param l the label containing a cached value
     */
    protected void clearCache ( final JLabel l )
    {
        l.putClientProperty ( PROPERTY_KEY, null );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        super.propertyChange ( e );
        final String name = e.getPropertyName ();
        if ( name.equals ( "text" ) || "font".equals ( name ) )
        {
            clearCache ( ( JLabel ) e.getSource () );
        }
    }

    /**
     * Check the given string to see if it should be rendered as HTML. Code based on implementation found in
     * <code>BasicHTML.isHTMLString(String)</code> in future JDKs.
     *
     * @param s the string
     * @return <code>true</code> if string is HTML, otherwise <code>false</code>
     */
    private static boolean isHTMLString ( final String s )
    {
        if ( s != null )
        {
            if ( ( s.length () >= 6 ) && ( s.charAt ( 0 ) == '<' ) && ( s.charAt ( 5 ) == '>' ) )
            {
                final String tag = s.substring ( 1, 5 );
                return tag.equalsIgnoreCase ( "html" );
            }
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        final Dimension d = super.getPreferredSize ( c );

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
            final Insets insets = c.getParent ().getInsets ();
            final Dimension size = c.getParent ().getSize ();
            if ( size.width > 0 )
            {
                // If width isn't set component shouldn't adjust.
                d.width = size.width - insets.left - insets.right;
            }
        }

        // The preferred height is either the preferred height of the text
        // lines, or the height of the icon.
        d.height = Math.max ( d.height, getPreferredHeight ( label ) );

        return PainterSupport.getPreferredSize ( c, d, painter );
    }

    /**
     * The preferred height of the label is the height of the lines with added top and bottom insets.
     *
     * @param label the label
     * @return the preferred height of the wrapped lines.
     */
    protected int getPreferredHeight ( final JLabel label )
    {
        final int numOfLines = getTextLines ( label ).size ();
        final Insets insets = label.getInsets ( paintViewInsets );
        final FontMetrics fm = label.getFontMetrics ( label.getFont () );
        return numOfLines * fm.getHeight () + insets.top + insets.bottom;
    }

    /**
     * Get the lines of text contained in the text label. The prepared lines is cached as a client property, accessible via {@link
     * #PROPERTY_KEY}.
     *
     * @param l the label
     * @return the text lines of the label.
     */
    @SuppressWarnings ( "unchecked" )
    public List<String> getTextLines ( final JLabel l )
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
    public void componentHidden ( final ComponentEvent e )
    {
        // Don't care
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentMoved ( final ComponentEvent e )
    {
        // Don't care
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentResized ( final ComponentEvent e )
    {
        clearCache ( ( JLabel ) e.getSource () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void componentShown ( final ComponentEvent e )
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
    protected List<String> prepareLines ( final JLabel l )
    {
        final List<String> lines = new ArrayList<String> ( defaultSize );
        final String text = l.getText ();
        if ( text == null )
        {
            return null; // Null guard
        }
        final PlainDocument doc = new PlainDocument ();
        try
        {
            doc.insertString ( 0, text, null );
        }
        catch ( final BadLocationException e )
        {
            return null;
        }
        final Element root = doc.getDefaultRootElement ();
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
    protected void wrap ( final List<String> lines, final Element elem )
    {
        final int p1 = elem.getEndOffset ();
        final Document doc = elem.getDocument ();
        for ( int p0 = elem.getStartOffset (); p0 < p1; )
        {
            final int p = calculateBreakPosition ( doc, p0, p1 );
            try
            {
                lines.add ( doc.getText ( p0, p - p0 ) );
            }
            catch ( final BadLocationException e )
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
     * <code>p1</code>.
     */
    protected int calculateBreakPosition ( final Document doc, final int p0, final int p1 )
    {
        final Segment segment = SegmentCache.getSegment ();
        try
        {
            doc.getText ( p0, p1 - p0, segment );
        }
        catch ( final BadLocationException e )
        {
            throw new Error ( "Can't get line text" );
        }

        final int width = label.getWidth ();//paintTextR.width; // todo Provide text view rect width
        final FontMetrics fm = label.getFontMetrics ( label.getFont () );
        final int p = p0 + Utilities.getBreakLocation ( segment, fm, 0, width, null, p0 );
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
        private final ArrayList<Segment> segments = new ArrayList<Segment> ( 2 );

        /**
         * Singleton instance.
         */
        private static final SegmentCache cache = new SegmentCache ();

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
            final int size = cache.segments.size ();
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
        public static void releaseSegment ( final Segment segment )
        {
            segment.array = null;
            segment.count = 0;
            cache.segments.add ( segment );
        }
    }
}