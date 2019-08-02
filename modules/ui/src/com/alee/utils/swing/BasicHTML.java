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

package com.alee.utils.swing;

import javax.swing.*;
import javax.swing.text.*;
import javax.swing.text.html.*;
import java.awt.*;
import java.io.Reader;
import java.io.StringReader;
import java.net.URL;

/**
 * Support for providing HTML views for the swing components. This translates a simple HTML string to a {@link javax.swing.text.View}
 * implementation that can render the HTML and provide the necessary layout semantics.
 *
 * This class is a copy-paste of {@link javax.swing.plaf.basic.BasicHTML} which has some unwanted settings like foreground hardcoded.
 * So this class acts as a replacement for all WebLaF components that could contain HTML in their text.
 *
 * @author Timothy Prinzing
 * @author Mikle Garin
 */
public final class BasicHTML
{
    /**
     * If this client property of a {@link JComponent} is set to {@link Boolean#TRUE} its content is never treated as HTML.
     * This is a copy of {@link javax.swing.plaf.basic.BasicHTML#htmlDisable} field which is private.
     */
    private static final String htmlDisable = "html.disable";

    /**
     * Check the given string to see if it should trigger the HTML rendering logic in a non-text component that supports HTML rendering.
     *
     * @param c    component that will be displaying HTML content
     * @param text text possibly containing HTML
     * @return {@code true} if provided string should trigger the HTML rendering logic, {@code false} otherwise
     */
    public static boolean isHTMLString ( final JComponent c, final String text )
    {
        final Boolean disabled = ( Boolean ) c.getClientProperty ( htmlDisable );
        if ( disabled != Boolean.TRUE )
        {
            if ( text != null && text.length () >= 6 && text.charAt ( 0 ) == '<' && text.charAt ( 5 ) == '>' )
            {
                final String tag = text.substring ( 1, 5 );
                return tag.equalsIgnoreCase ( javax.swing.plaf.basic.BasicHTML.propertyKey );
            }
        }
        return false;
    }

    /**
     * Returns HTML renderer for the given component and string of HTML.
     *
     * @param c           component to create HTML View for
     * @param html        HTML content
     * @param defaultFont text font
     * @param foreground  text foreground color
     * @return HTML renderer for the given component and string of HTML
     */
    public static View createHTMLView ( final JComponent c, final String html, final Font defaultFont, final Color foreground )
    {
        final BasicEditorKit kit = getFactory ();
        final Document doc = kit.createDefaultDocument ( defaultFont, foreground );
        final Object base = c.getClientProperty ( javax.swing.plaf.basic.BasicHTML.documentBaseKey );
        if ( base instanceof URL )
        {
            ( ( HTMLDocument ) doc ).setBase ( ( URL ) base );
        }
        final Reader r = new StringReader ( html );
        try
        {
            kit.read ( r, doc, 0 );
        }
        catch ( final Exception e )
        {
            // Ignored
        }
        final ViewFactory f = kit.getViewFactory ();
        final View hview = f.create ( doc.getDefaultRootElement () );
        return new Renderer ( f, hview );
    }

    /**
     * Returns the baseline for the HTML renderer.
     *
     * @param view the View to get the baseline for
     * @param w    the width to get the baseline for
     * @param h    the height to get the baseline for
     * @return baseline or a value &lt; 0 indicating there is no reasonable baseline
     * @throws IllegalArgumentException if width or height is &lt; 0
     * @see java.awt.FontMetrics
     * @see javax.swing.JComponent#getBaseline(int, int)
     * @since 1.6
     */
    public static int getHTMLBaseline ( final View view, final int w, final int h )
    {
        if ( w < 0 || h < 0 )
        {
            throw new IllegalArgumentException ( "Width and height must be >= 0" );
        }
        if ( view instanceof Renderer )
        {
            return getBaseline ( view.getView ( 0 ), w, h );
        }
        return -1;
    }

    /**
     * Gets the baseline for the specified component.
     * This digs out the View client property, and if non-null the baseline is calculated from it.
     * Otherwise the baseline is the value {@code y + ascent}.
     */
    private static int getBaseline ( final JComponent c, final int y, final int ascent,
                                     final int w, final int h )
    {
        final View view = ( View ) c.getClientProperty ( javax.swing.plaf.basic.BasicHTML.propertyKey );
        if ( view != null )
        {
            final int baseline = getHTMLBaseline ( view, w, h );
            if ( baseline < 0 )
            {
                return baseline;
            }
            return y + baseline;
        }
        return y + ascent;
    }

    /**
     * Gets the baseline for the specified View.
     */
    private static int getBaseline ( final View view, final int w, final int h )
    {
        if ( hasParagraph ( view ) )
        {
            view.setSize ( w, h );
            return getBaseline ( view, new Rectangle ( 0, 0, w, h ) );
        }
        return -1;
    }

    private static int getBaseline ( final View view, Shape bounds )
    {
        if ( view.getViewCount () == 0 )
        {
            return -1;
        }
        final AttributeSet attributes = view.getElement ().getAttributes ();
        Object name = null;
        if ( attributes != null )
        {
            name = attributes.getAttribute ( StyleConstants.NameAttribute );
        }
        int index = 0;
        if ( name == HTML.Tag.HTML && view.getViewCount () > 1 )
        {
            // For HTML on widgets the header is not visible, skip it.
            index++;
        }
        bounds = view.getChildAllocation ( index, bounds );
        if ( bounds == null )
        {
            return -1;
        }
        final View child = view.getView ( index );
        if ( view instanceof javax.swing.text.ParagraphView )
        {
            final Rectangle rect;
            if ( bounds instanceof Rectangle )
            {
                rect = ( Rectangle ) bounds;
            }
            else
            {
                rect = bounds.getBounds ();
            }
            return rect.y + ( int ) ( rect.height *
                    child.getAlignment ( View.Y_AXIS ) );
        }
        return getBaseline ( child, bounds );
    }

    private static boolean hasParagraph ( final View view )
    {
        if ( view instanceof javax.swing.text.ParagraphView )
        {
            return true;
        }
        if ( view.getViewCount () == 0 )
        {
            return false;
        }
        final AttributeSet attributes = view.getElement ().getAttributes ();
        Object name = null;
        if ( attributes != null )
        {
            name = attributes.getAttribute ( StyleConstants.NameAttribute );
        }
        int index = 0;
        if ( name == HTML.Tag.HTML && view.getViewCount () > 1 )
        {
            // For HTML on widgets the header is not visible, skip it.
            index = 1;
        }
        return hasParagraph ( view.getView ( index ) );
    }

    private static BasicEditorKit getFactory ()
    {
        if ( basicHTMLFactory == null )
        {
            basicHTMLViewFactory = new BasicHTMLViewFactory ();
            basicHTMLFactory = new BasicEditorKit ();
        }
        return basicHTMLFactory;
    }

    /**
     * The source of the HTML renderers
     */
    private static BasicEditorKit basicHTMLFactory;

    /**
     * Creates the Views that visually represent the model.
     */
    private static ViewFactory basicHTMLViewFactory;

    /**
     * Overrides to the default stylesheet.  Should consider
     * just creating a completely fresh stylesheet.
     */
    private static final String styleChanges =
            "p { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }" +
                    "body { margin-top: 0; margin-bottom: 0; margin-left: 0; margin-right: 0 }";

    /**
     * The views produced for the ComponentUI implementations aren't
     * going to be edited and don't need full HTML support.  This kit
     * alters the HTMLEditorKit to try and trim things down a bit.
     * It does the following:
     * <ul>
     * <li>It doesn't produce Views for things like comments,
     * head, title, unknown tags, etc.
     * <li>It installs a different set of css settings from the default
     * provided by HTMLEditorKit.
     * </ul>
     */
    private static class BasicEditorKit extends HTMLEditorKit
    {
        /**
         * Shared base style for all documents created by us use.
         */
        private static StyleSheet defaultStyles;

        /**
         * Overridden to return our own slimmed down style sheet.
         */
        @Override
        public StyleSheet getStyleSheet ()
        {
            if ( defaultStyles == null )
            {
                defaultStyles = new StyleSheet ();
                final StringReader r = new StringReader ( styleChanges );
                try
                {
                    defaultStyles.loadRules ( r, null );
                }
                catch ( final Exception e )
                {
                    // don't want to die in static initialization...
                    // just display things wrong.
                }
                r.close ();
                defaultStyles.addStyleSheet ( super.getStyleSheet () );
            }
            return defaultStyles;
        }

        /**
         * Sets the async policy to flush everything in one chunk, and
         * to not display unknown tags.
         */
        public Document createDefaultDocument ( final Font defaultFont, final Color foreground )
        {
            final StyleSheet styles = getStyleSheet ();
            final StyleSheet ss = new StyleSheet ();
            ss.addStyleSheet ( styles );
            final BasicDocument doc = new BasicDocument ( ss, defaultFont, foreground );
            doc.setAsynchronousLoadPriority ( Integer.MAX_VALUE );
            doc.setPreservesUnknownTags ( false );
            return doc;
        }

        /**
         * Returns the ViewFactory that is used to make sure the Views don't
         * load in the background.
         */
        @Override
        public ViewFactory getViewFactory ()
        {
            return basicHTMLViewFactory;
        }
    }

    /**
     * BasicHTMLViewFactory extends HTMLFactory to force images to be loaded
     * synchronously.
     */
    private static class BasicHTMLViewFactory extends HTMLEditorKit.HTMLFactory
    {
        @Override
        public View create ( final Element elem )
        {
            final View view = super.create ( elem );

            if ( view instanceof ImageView )
            {
                ( ( ImageView ) view ).setLoadsSynchronously ( true );
            }
            return view;
        }
    }

    /**
     * The subclass of HTMLDocument that is used as the model. getForeground
     * is overridden to return the foreground property from the Component this
     * was created for.
     */
    private static class BasicDocument extends HTMLDocument
    {
        /**
         * The host, that is where we are rendering.
         */
        // private JComponent host;
        public BasicDocument ( final StyleSheet s, final Font defaultFont, final Color foreground )
        {
            super ( s );
            setPreservesUnknownTags ( false );
            setFontAndColor ( defaultFont, foreground );
        }

        /**
         * Sets the default font and default color. These are set by
         * adding a rule for the body that specifies the font and color.
         * This allows the HTML to override these should it wish to have
         * a custom font or color.
         */
        private void setFontAndColor ( final Font font, final Color fg )
        {
            getStyleSheet ().addRule ( displayPropertiesToCSS ( font, fg ) );
        }

        private String displayPropertiesToCSS ( final Font font, final Color fg )
        {
            final StringBuilder rule = new StringBuilder ( "body {" );
            if ( font != null )
            {
                rule.append ( " font-family: " );
                rule.append ( font.getFamily () );
                rule.append ( " ; " );
                rule.append ( " font-size: " );
                rule.append ( font.getSize () );
                rule.append ( "pt ;" );
                if ( font.isBold () )
                {
                    rule.append ( " font-weight: 700 ; " );
                }
                if ( font.isItalic () )
                {
                    rule.append ( " font-style: italic ; " );
                }
            }
            if ( fg != null )
            {
                rule.append ( " color: #" );
                if ( fg.getRed () < 16 )
                {
                    rule.append ( '0' );
                }
                rule.append ( Integer.toHexString ( fg.getRed () ) );
                if ( fg.getGreen () < 16 )
                {
                    rule.append ( '0' );
                }
                rule.append ( Integer.toHexString ( fg.getGreen () ) );
                if ( fg.getBlue () < 16 )
                {
                    rule.append ( '0' );
                }
                rule.append ( Integer.toHexString ( fg.getBlue () ) );
                rule.append ( " ; " );
            }
            rule.append ( " }" );
            return rule.toString ();
        }
    }

    /**
     * Root text view that acts as an HTML renderer.
     */
    private static class Renderer extends View
    {
        private final View view;
        private final ViewFactory factory;

        private int width;

        public Renderer ( final ViewFactory f, final View v )
        {
            super ( null );
            factory = f;
            view = v;
            view.setParent ( this );
            // initially layout to the preferred size
            setSize ( view.getPreferredSpan ( X_AXIS ), view.getPreferredSpan ( Y_AXIS ) );
        }

        /**
         * Fetches the attributes to use when rendering.  At the root
         * level there are no attributes.  If an attribute is resolved
         * up the view hierarchy this is the end of the line.
         */
        @Override
        public AttributeSet getAttributes ()
        {
            return null;
        }

        /**
         * Determines the preferred span for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the span the view would like to be rendered into.
         * Typically the view is told to render into the span
         * that is returned, although there is no guarantee.
         * The parent may choose to resize or break the view.
         */
        @Override
        public float getPreferredSpan ( final int axis )
        {
            if ( axis == X_AXIS )
            {
                // width currently laid out to
                return width;
            }
            return view.getPreferredSpan ( axis );
        }

        /**
         * Determines the minimum span for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the span the view would like to be rendered into.
         * Typically the view is told to render into the span
         * that is returned, although there is no guarantee.
         * The parent may choose to resize or break the view.
         */
        @Override
        public float getMinimumSpan ( final int axis )
        {
            return view.getMinimumSpan ( axis );
        }

        /**
         * Determines the maximum span for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the span the view would like to be rendered into.
         * Typically the view is told to render into the span
         * that is returned, although there is no guarantee.
         * The parent may choose to resize or break the view.
         */
        @Override
        public float getMaximumSpan ( final int axis )
        {
            return Integer.MAX_VALUE;
        }

        /**
         * Specifies that a preference has changed.
         * Child views can call this on the parent to indicate that
         * the preference has changed.  The root view routes this to
         * invalidate on the hosting component.
         * <p>
         * This can be called on a different thread from the
         * event dispatching thread and is basically unsafe to
         * propagate into the component.  To make this safe,
         * the operation is transferred over to the event dispatching
         * thread for completion.  It is a design goal that all view
         * methods be safe to call without concern for concurrency,
         * and this behavior helps make that true.
         *
         * @param child  the child view
         * @param width  true if the width preference has changed
         * @param height true if the height preference has changed
         */
        @Override
        public void preferenceChanged ( final View child, final boolean width, final boolean height )
        {
            //
        }

        /**
         * Determines the desired alignment for this view along an axis.
         *
         * @param axis may be either X_AXIS or Y_AXIS
         * @return the desired alignment, where 0.0 indicates the origin
         * and 1.0 the full span away from the origin
         */
        @Override
        public float getAlignment ( final int axis )
        {
            return view.getAlignment ( axis );
        }

        /**
         * Renders the view.
         *
         * @param g          the graphics context
         * @param allocation the region to render into
         */
        @Override
        public void paint ( final Graphics g, final Shape allocation )
        {
            final Rectangle alloc = allocation.getBounds ();
            view.setSize ( alloc.width, alloc.height );
            view.paint ( g, allocation );
        }

        /**
         * Sets the view parent.
         *
         * @param parent the parent view
         */
        @Override
        public void setParent ( final View parent )
        {
            throw new Error ( "Can't set parent on root view" );
        }

        /**
         * Returns the number of views in this view.  Since
         * this view simply wraps the root of the view hierarchy
         * it has exactly one child.
         *
         * @return the number of views
         * @see #getView
         */
        @Override
        public int getViewCount ()
        {
            return 1;
        }

        /**
         * Gets the n-th view in this container.
         *
         * @param n the number of the view to get
         * @return the view
         */
        @Override
        public View getView ( final int n )
        {
            return view;
        }

        /**
         * Provides a mapping from the document model coordinate space
         * to the coordinate space of the view mapped to it.
         *
         * @param pos the position to convert
         * @param a   the allocated region to render into
         * @return the bounding box of the given position
         */
        @Override
        public Shape modelToView ( final int pos, final Shape a, final Position.Bias b ) throws BadLocationException
        {
            return view.modelToView ( pos, a, b );
        }

        /**
         * Provides a mapping from the document model coordinate space
         * to the coordinate space of the view mapped to it.
         *
         * @param p0 the position to convert >= 0
         * @param b0 the bias toward the previous character or the
         *           next character represented by p0, in case the
         *           position is a boundary of two views.
         * @param p1 the position to convert >= 0
         * @param b1 the bias toward the previous character or the
         *           next character represented by p1, in case the
         *           position is a boundary of two views.
         * @param a  the allocated region to render into
         * @return the bounding box of the given position is returned
         * @throws BadLocationException     if the given position does
         *                                  not represent a valid location in the associated document
         * @throws IllegalArgumentException for an invalid bias argument
         * @see View#viewToModel
         */
        @Override
        public Shape modelToView ( final int p0, final Position.Bias b0, final int p1,
                                   final Position.Bias b1, final Shape a ) throws BadLocationException
        {
            return view.modelToView ( p0, b0, p1, b1, a );
        }

        /**
         * Provides a mapping from the view coordinate space to the logical
         * coordinate space of the model.
         *
         * @param x x coordinate of the view location to convert
         * @param y y coordinate of the view location to convert
         * @param a the allocated region to render into
         * @return the location within the model that best represents the
         * given point in the view
         */
        @Override
        public int viewToModel ( final float x, final float y, final Shape a, final Position.Bias[] bias )
        {
            return view.viewToModel ( x, y, a, bias );
        }

        /**
         * Returns the document model underlying the view.
         *
         * @return the model
         */
        @Override
        public Document getDocument ()
        {
            return view.getDocument ();
        }

        /**
         * Returns the starting offset into the model for this view.
         *
         * @return the starting offset
         */
        @Override
        public int getStartOffset ()
        {
            return view.getStartOffset ();
        }

        /**
         * Returns the ending offset into the model for this view.
         *
         * @return the ending offset
         */
        @Override
        public int getEndOffset ()
        {
            return view.getEndOffset ();
        }

        /**
         * Gets the element that this view is mapped to.
         *
         * @return the view
         */
        @Override
        public Element getElement ()
        {
            return view.getElement ();
        }

        /**
         * Sets the view size.
         *
         * @param width  the width
         * @param height the height
         */
        @Override
        public void setSize ( final float width, final float height )
        {
            this.width = ( int ) width;
            view.setSize ( width, height );
        }

        /**
         * Fetches the container hosting the view.  This is useful for
         * things like scheduling a repaint, finding out the host
         * components font, etc.  The default implementation
         * of this is to forward the query to the parent view.
         *
         * @return the container
         */
        @Override
        public Container getContainer ()
        {
            return null;
        }

        /**
         * Fetches the factory to be used for building the
         * various view fragments that make up the view that
         * represents the model.  This is what determines
         * how the model will be represented.  This is implemented
         * to fetch the factory provided by the associated
         * EditorKit.
         *
         * @return the factory
         */
        @Override
        public ViewFactory getViewFactory ()
        {
            return factory;
        }
    }
}