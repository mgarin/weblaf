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

package com.alee.laf.text;

import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.TextUI;
import javax.swing.text.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.util.Vector;

/**
 * User: mgarin Date: 16.05.11 Time: 17:17
 */

public class WebHighlighter extends LayeredHighlighter
{

    /**
     * Creates a new DefaultHighlighther object.
     */
    public WebHighlighter ()
    {
        drawsLayeredHighlights = true;
    }

    // ---- Highlighter methods ----------------------------------------------

    /**
     * Renders the highlights.
     *
     * @param g the graphics context
     */
    @Override
    public void paint ( Graphics g )
    {
        // PENDING(prinz) - should cull ranges not visible
        int len = highlights.size ();
        for ( int i = 0; i < len; i++ )
        {
            HighlightInfo info = ( HighlightInfo ) highlights.elementAt ( i );
            if ( !( info instanceof LayeredHighlightInfo ) )
            {
                // Avoid allocing unless we need it.
                Rectangle a = component.getBounds ();
                Insets insets = component.getInsets ();
                a.x = insets.left;
                a.y = insets.top;
                a.width -= insets.left + insets.right;
                a.height -= insets.top + insets.bottom;
                for (; i < len; i++ )
                {
                    info = ( HighlightInfo ) highlights.elementAt ( i );
                    if ( !( info instanceof LayeredHighlightInfo ) )
                    {
                        Highlighter.HighlightPainter p = info.getPainter ();
                        p.paint ( g, info.getStartOffset (), info.getEndOffset (), a, component );
                    }
                }
            }
        }
    }

    /**
     * Called when the UI is being installed into the interface of a JTextComponent.  Installs the editor, and removes any existing
     * highlights.
     *
     * @param c the editor component
     * @see Highlighter
     */
    @Override
    public void install ( JTextComponent c )
    {
        component = c;
        removeAllHighlights ();
    }

    /**
     * Called when the UI is being removed from the interface of a JTextComponent.
     *
     * @param c the component
     * @see Highlighter
     */
    @Override
    public void deinstall ( JTextComponent c )
    {
        component = null;
    }

    /**
     * Adds a highlight to the view.  Returns a tag that can be used to refer to the highlight.
     *
     * @param p0 the start offset of the range to highlight >= 0
     * @param p1 the end offset of the range to highlight >= p0
     * @param p  the painter to use to actually render the highlight
     * @return an object that can be used as a tag to refer to the highlight
     * @throws javax.swing.text.BadLocationException
     *          if the specified location is invalid
     */
    @Override
    public Object addHighlight ( int p0, int p1, Highlighter.HighlightPainter p ) throws BadLocationException
    {
        Document doc = component.getDocument ();
        HighlightInfo i =
                ( getDrawsLayeredHighlights () && ( p instanceof LayeredHighlighter.LayerPainter ) ) ? new LayeredHighlightInfo () :
                        new HighlightInfo ();
        i.painter = p;
        i.p0 = doc.createPosition ( p0 );
        i.p1 = doc.createPosition ( p1 );
        highlights.addElement ( i );
        safeDamageRange ( p0, p1 );
        return i;
    }

    /**
     * Removes a highlight from the view.
     *
     * @param tag the reference to the highlight
     */
    @Override
    public void removeHighlight ( Object tag )
    {
        if ( tag instanceof LayeredHighlightInfo )
        {
            LayeredHighlightInfo lhi = ( LayeredHighlightInfo ) tag;
            if ( lhi.width > 0 && lhi.height > 0 )
            {
                component.repaint ( lhi.x, lhi.y, lhi.width, lhi.height );
            }
        }
        else
        {
            HighlightInfo info = ( HighlightInfo ) tag;
            safeDamageRange ( info.p0, info.p1 );
        }
        highlights.removeElement ( tag );
    }

    /**
     * Removes all highlights.
     */
    @Override
    public void removeAllHighlights ()
    {
        TextUI mapper = component.getUI ();
        if ( getDrawsLayeredHighlights () )
        {
            int len = highlights.size ();
            if ( len != 0 )
            {
                int minX = 0;
                int minY = 0;
                int maxX = 0;
                int maxY = 0;
                int p0 = -1;
                int p1 = -1;
                for ( int i = 0; i < len; i++ )
                {
                    HighlightInfo hi = ( HighlightInfo ) highlights.elementAt ( i );
                    if ( hi instanceof LayeredHighlightInfo )
                    {
                        LayeredHighlightInfo info = ( LayeredHighlightInfo ) hi;
                        minX = Math.min ( minX, info.x );
                        minY = Math.min ( minY, info.y );
                        maxX = Math.max ( maxX, info.x + info.width );
                        maxY = Math.max ( maxY, info.y + info.height );
                    }
                    else
                    {
                        if ( p0 == -1 )
                        {
                            p0 = hi.p0.getOffset ();
                            p1 = hi.p1.getOffset ();
                        }
                        else
                        {
                            p0 = Math.min ( p0, hi.p0.getOffset () );
                            p1 = Math.max ( p1, hi.p1.getOffset () );
                        }
                    }
                }
                if ( minX != maxX && minY != maxY )
                {
                    component.repaint ( minX, minY, maxX - minX, maxY - minY );
                }
                if ( p0 != -1 )
                {
                    try
                    {
                        safeDamageRange ( p0, p1 );
                    }
                    catch ( BadLocationException e )
                    {
                        //
                    }
                }
                highlights.removeAllElements ();
            }
        }
        else if ( mapper != null )
        {
            int len = highlights.size ();
            if ( len != 0 )
            {
                int p0 = Integer.MAX_VALUE;
                int p1 = 0;
                for ( int i = 0; i < len; i++ )
                {
                    HighlightInfo info = ( HighlightInfo ) highlights.elementAt ( i );
                    p0 = Math.min ( p0, info.p0.getOffset () );
                    p1 = Math.max ( p1, info.p1.getOffset () );
                }
                try
                {
                    safeDamageRange ( p0, p1 );
                }
                catch ( BadLocationException e )
                {
                    //
                }

                highlights.removeAllElements ();
            }
        }
    }

    /**
     * Changes a highlight.
     *
     * @param tag the highlight tag
     * @param p0  the beginning of the range >= 0
     * @param p1  the end of the range >= p0
     * @throws BadLocationException if the specified location is invalid
     */
    @Override
    public void changeHighlight ( Object tag, int p0, int p1 ) throws BadLocationException
    {
        Document doc = component.getDocument ();
        if ( tag instanceof LayeredHighlightInfo )
        {
            LayeredHighlightInfo lhi = ( LayeredHighlightInfo ) tag;
            if ( lhi.width > 0 && lhi.height > 0 )
            {
                component.repaint ( lhi.x, lhi.y, lhi.width, lhi.height );
            }
            // Mark the highlights region as invalid, it will reset itself
            // next time asked to paint.
            lhi.width = lhi.height = 0;
            lhi.p0 = doc.createPosition ( p0 );
            lhi.p1 = doc.createPosition ( p1 );
            safeDamageRange ( Math.min ( p0, p1 ), Math.max ( p0, p1 ) );
        }
        else
        {
            HighlightInfo info = ( HighlightInfo ) tag;
            int oldP0 = info.p0.getOffset ();
            int oldP1 = info.p1.getOffset ();
            if ( p0 == oldP0 )
            {
                safeDamageRange ( Math.min ( oldP1, p1 ), Math.max ( oldP1, p1 ) );
            }
            else if ( p1 == oldP1 )
            {
                safeDamageRange ( Math.min ( p0, oldP0 ), Math.max ( p0, oldP0 ) );
            }
            else
            {
                safeDamageRange ( oldP0, oldP1 );
                safeDamageRange ( p0, p1 );
            }
            info.p0 = doc.createPosition ( p0 );
            info.p1 = doc.createPosition ( p1 );
        }
    }

    /**
     * Makes a copy of the highlights.  Does not actually clone each highlight, but only makes references to them.
     *
     * @return the copy
     * @see Highlighter#getHighlights
     */
    @Override
    public Highlighter.Highlight[] getHighlights ()
    {
        int size = highlights.size ();
        if ( size == 0 )
        {
            return noHighlights;
        }
        Highlighter.Highlight[] h = new Highlighter.Highlight[ size ];
        highlights.copyInto ( h );
        return h;
    }

    /**
     * When leaf Views (such as LabelView) are rendering they should call into this method. If a highlight is in the given region it will
     * be
     * drawn immediately.
     *
     * @param g          Graphics used to draw
     * @param p0         starting offset of view
     * @param p1         ending offset of view
     * @param viewBounds Bounds of View
     * @param editor     JTextComponent
     * @param view       View instance being rendered
     */
    @Override
    public void paintLayeredHighlights ( Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view )
    {
        for ( int counter = highlights.size () - 1; counter >= 0; counter-- )
        {
            Object tag = highlights.elementAt ( counter );
            if ( tag instanceof LayeredHighlightInfo )
            {
                LayeredHighlightInfo lhi = ( LayeredHighlightInfo ) tag;
                int start = lhi.getStartOffset ();
                int end = lhi.getEndOffset ();
                if ( ( p0 < start && p1 > start ) || ( p0 >= start && p0 < end ) )
                {
                    lhi.paintLayeredHighlights ( g, p0, p1, viewBounds, editor, view );
                }
            }
        }
    }

    /**
     * Queues damageRange() call into event dispatch thread to be sure that views are in consistent state.
     */
    private void safeDamageRange ( final Position p0, final Position p1 )
    {
        safeDamager.damageRange ( p0, p1 );
    }

    /**
     * Queues damageRange() call into event dispatch thread to be sure that views are in consistent state.
     */
    private void safeDamageRange ( int a0, int a1 ) throws BadLocationException
    {
        Document doc = component.getDocument ();
        safeDamageRange ( doc.createPosition ( a0 ), doc.createPosition ( a1 ) );
    }

    /**
     * If true, highlights are drawn as the Views draw the text. That is the Views will call into <code>paintLayeredHighlight</code> which
     * will result in a rectangle being drawn before the text is drawn (if the offsets are in a highlighted region that is). For this to
     * work the painter supplied must be an instance of LayeredHighlightPainter.
     */
    public void setDrawsLayeredHighlights ( boolean newValue )
    {
        drawsLayeredHighlights = newValue;
    }

    public boolean getDrawsLayeredHighlights ()
    {
        return drawsLayeredHighlights;
    }

    // ---- member variables --------------------------------------------

    private final static Highlighter.Highlight[] noHighlights = new Highlighter.Highlight[ 0 ];
    private Vector highlights = new Vector ();  // Vector<HighlightInfo>
    private JTextComponent component;
    private boolean drawsLayeredHighlights;
    private SafeDamager safeDamager = new SafeDamager ();

    class HighlightInfo implements Highlighter.Highlight
    {

        @Override
        public int getStartOffset ()
        {
            return p0.getOffset ();
        }

        @Override
        public int getEndOffset ()
        {
            return p1.getOffset ();
        }

        @Override
        public Highlighter.HighlightPainter getPainter ()
        {
            return painter;
        }

        Position p0;
        Position p1;
        Highlighter.HighlightPainter painter;
    }


    /**
     * LayeredHighlightPainter is used when a drawsLayeredHighlights is true. It maintains a rectangle of the region to paint.
     */
    class LayeredHighlightInfo extends HighlightInfo
    {
        int x;
        int y;
        int width;
        int height;

        void union ( Shape bounds )
        {
            if ( bounds == null )
            {
                return;
            }

            Rectangle alloc;
            if ( bounds instanceof Rectangle )
            {
                alloc = ( Rectangle ) bounds;
            }
            else
            {
                alloc = bounds.getBounds ();
            }
            if ( width == 0 || height == 0 )
            {
                x = alloc.x;
                y = alloc.y;
                width = alloc.width;
                height = alloc.height;
            }
            else
            {
                width = Math.max ( x + width, alloc.x + alloc.width );
                height = Math.max ( y + height, alloc.y + alloc.height );
                x = Math.min ( x, alloc.x );
                width -= x;
                y = Math.min ( y, alloc.y );
                height -= y;
            }
        }

        /**
         * Restricts the region based on the receivers offsets and messages the painter to paint the region.
         */

        void paintLayeredHighlights ( Graphics g, int p0, int p1, Shape viewBounds, JTextComponent editor, View view )
        {
            int start = getStartOffset ();
            int end = getEndOffset ();
            // Restrict the region to what we represent
            p0 = Math.max ( start, p0 );
            p1 = Math.min ( end, p1 );
            // Paint the appropriate region using the painter and union
            // the effected region with our bounds.
            //            union ( ( ( LayeredHighlighter.LayerPainter ) painter )
            //                    .paintLayer ( g, p0, p1, viewBounds, editor, view ) );


            Graphics2D g2d = ( Graphics2D ) g;
            Object aa = LafUtils.setupAntialias ( g2d );

            Shape oldClip = g2d.getClip ();

            try
            {
                // Proper clip
                Insets insets = editor.getInsets ();
                Area newClip = new Area (
                        new Rectangle ( insets.left - 2, insets.top - 2, editor.getWidth () - insets.left - insets.right + 4,
                                editor.getHeight () - insets.top - insets.bottom + 4 ) );
                newClip.intersect ( new Area ( editor.getVisibleRect () ) );
                g2d.setClip ( newClip );

                // Proper selection area
                Rectangle b = ( Rectangle ) view.modelToView ( p0, Position.Bias.Forward, p1, Position.Bias.Backward, viewBounds );
                b.x -= 1;
                b.y -= 1;
                b.height += 1;
                b.width += end == editor.getText ().length () ? 1 : 0;

                Shape hs = new RoundRectangle2D.Double ( b.x, b.y, b.width, b.height, 0, 0 );

                // Outer shade
                LafUtils.drawShade ( g2d, hs, com.alee.laf.StyleConstants.shadeColor, 2 );

                // Background
                g2d.setPaint ( new Color ( 128, 128, 128, 64 ) );
                g2d.fill ( hs );

                // Border
                g2d.setPaint ( Color.LIGHT_GRAY );
                g2d.draw ( hs );
            }
            catch ( BadLocationException e )
            {
                //
            }

            g2d.setClip ( oldClip );

            LafUtils.restoreAntialias ( g2d, aa );
        }
    }

    /**
     * This class invokes <code>mapper.damageRange</code> in EventDispatchThread. The only one instance per Highlighter is cretaed. When a
     * number of ranges should be damaged it collects them into queue and damages them in consecutive order in <code>run</code> call.
     */
    class SafeDamager implements Runnable
    {
        private Vector p0 = new Vector ( 10 );
        private Vector p1 = new Vector ( 10 );
        private Document lastDoc = null;

        /**
         * Executes range(s) damage and cleans range queue.
         */
        @Override
        public synchronized void run ()
        {
            if ( component != null )
            {
                TextUI mapper = component.getUI ();
                if ( mapper != null && lastDoc == component.getDocument () )
                {
                    // the Document should be the same to properly
                    // display highlights
                    int len = p0.size ();
                    for ( int i = 0; i < len; i++ )
                    {
                        mapper.damageRange ( component, ( ( Position ) p0.get ( i ) ).getOffset (),
                                ( ( Position ) p1.get ( i ) ).getOffset () );
                    }
                }
            }
            p0.clear ();
            p1.clear ();

            // release reference
            lastDoc = null;
        }

        /**
         * Adds the range to be damaged into the range queue. If the range queue is empty (the first call or run() was already invoked)
         * then
         * adds this class instance into EventDispatch queue.
         * <p/>
         * The method also tracks if the current document changed or component is null. In this case it removes all ranges added before
         * from
         * range queue.
         */
        public synchronized void damageRange ( Position pos0, Position pos1 )
        {
            if ( component == null )
            {
                p0.clear ();
                lastDoc = null;
                return;
            }

            boolean addToQueue = p0.isEmpty ();
            Document curDoc = component.getDocument ();
            if ( curDoc != lastDoc )
            {
                if ( !p0.isEmpty () )
                {
                    p0.clear ();
                    p1.clear ();
                }
                lastDoc = curDoc;
            }
            p0.add ( pos0 );
            p1.add ( pos1 );

            if ( addToQueue )
            {
                SwingUtilities.invokeLater ( this );
            }
        }
    }
}
