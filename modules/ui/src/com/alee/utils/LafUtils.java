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

package com.alee.utils;

import com.alee.global.StyleConstants;
import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.log.Log;
import com.alee.managers.style.MarginSupport;
import com.alee.managers.style.PaddingSupport;
import com.alee.managers.style.ShapeProvider;
import com.alee.managers.style.Styleable;
import com.alee.painter.common.TexturePainter;
import com.alee.utils.laf.FocusType;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.xml.ResourceFile;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.RootPaneUI;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides a set of utilities for core WebLaF components.
 * Created mostly for internal usage within WebLaF.
 *
 * @author Mikle Garin
 */

public final class LafUtils
{
    /**
     * Returns whether window in which specified component located is decorated by L&amp;F or not.
     *
     * @param component component used to determine window decoration state
     * @return true if window in which specified component located is decorated by L&amp;F, false otherwise
     */
    public static boolean isInDecoratedWindow ( final Component component )
    {
        final JRootPane rootPane = SwingUtils.getRootPane ( component );
        if ( rootPane != null )
        {
            final RootPaneUI ui = rootPane.getUI ();
            if ( ui instanceof WebRootPaneUI )
            {
                return ( ( WebRootPaneUI ) ui ).isDecorated ();
            }
        }
        return false;
    }

    /**
     * Returns current component margin if it is supported.
     * Might return null which is basically the same as an empty [0,0,0,0] margin.
     *
     * @param component component to retrieve margin from
     * @return current component margin if it is supported
     */
    public static Insets getMargin ( final Component component )
    {
        if ( component instanceof MarginSupport )
        {
            return ( ( MarginSupport ) component ).getMargin ();
        }
        else
        {
            final ComponentUI ui = getUI ( component );
            if ( ui instanceof MarginSupport )
            {
                return ( ( MarginSupport ) ui ).getMargin ();
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Returns current component padding if it is supported.
     * Might return null which is basically the same as an empty [0,0,0,0] padding.
     *
     * @param component component to retrieve padding from
     * @return current component padding if it is supported
     */
    public static Insets getPadding ( final Component component )
    {
        if ( component instanceof PaddingSupport )
        {
            return ( ( PaddingSupport ) component ).getPadding ();
        }
        else
        {
            final ComponentUI ui = getUI ( component );
            if ( ui instanceof PaddingSupport )
            {
                return ( ( PaddingSupport ) ui ).getPadding ();
            }
            else
            {
                return null;
            }
        }
    }

    /**
     * Returns current component border insets if it is supported.
     * Might return null which is basically the same as an empty [0,0,0,0] border insets.
     *
     * @param component component to retrieve border insets from
     * @return current component border insets if it is supported
     */
    public static Insets getInsets ( final Component component )
    {
        if ( component instanceof JComponent )
        {
            return ( ( JComponent ) component ).getInsets ();
        }
        else
        {
            return null;
        }
    }

    /**
     * Returns component UI or null if UI cannot be retrieved.
     *
     * @param component component to retrieve UI from
     * @param <T>       UI class type
     * @return component UI or null if UI cannot be retrieved
     */
    public static <T extends ComponentUI> T getUI ( final Component component )
    {
        return ReflectUtils.callMethodSafely ( component, "getUI" );
    }

    /**
     * Draws alpha-background
     */

    public static void drawAlphaLayer ( final Graphics2D g2d, final int x, final int y, final int width, final int height )
    {
        drawAlphaLayer ( g2d, x, y, width, height, StyleConstants.ALPHA_RECT_SIZE );
    }

    public static void drawAlphaLayer ( final Graphics2D g2d, final int x, final int y, final int width, final int height, final int size )
    {
        drawAlphaLayer ( g2d, x, y, width, height, size, StyleConstants.LIGHT_ALPHA, StyleConstants.DARK_ALPHA );
    }

    public static void drawAlphaLayer ( final Graphics2D g2d, final int x, final int y, final int width, final int height, final int size,
                                        final Color light, final Color dark )
    {
        // todo Optimize paint by using generated texture image
        final int xAmount = width / size + 1;
        final int yAmount = height / size + 1;
        boolean lightColor;
        for ( int i = 0; i < xAmount; i++ )
        {
            for ( int j = 0; j < yAmount; j++ )
            {
                lightColor = ( i + j ) % 2 == 0;
                final Color color = lightColor ? light : dark;
                if ( color != null )
                {
                    g2d.setPaint ( color );
                    final int w = ( x + i * size + size > x + width ) ? ( width - i * size ) : size;
                    final int h = ( y + j * size + size > y + height ) ? ( height - j * size ) : size;
                    g2d.fillRect ( x + i * size, y + j * size, w, h );
                }
            }
        }
    }

    /**
     * Determines real text size
     */

    public static Rectangle getTextBounds ( final String text, final Graphics g, final Font font )
    {
        return getTextBounds ( text, ( Graphics2D ) g, font );
    }

    public static Rectangle getTextBounds ( final String text, final Graphics2D g2d, final Font font )
    {
        final FontRenderContext renderContext = g2d.getFontRenderContext ();
        final GlyphVector glyphVector = font.createGlyphVector ( renderContext, text );
        return glyphVector.getVisualBounds ().getBounds ();
    }

    /**
     * Paints web styled border within the component with shadow and background if needed
     */

    @Deprecated
    public static Shape drawWebStyle ( final Graphics2D g2d, final JComponent component, final Color shadeColor, final int shadeWidth,
                                       final int round, final boolean fillBackground, final boolean webColored )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, StyleConstants.darkBorderColor,
                StyleConstants.disabledBorderColor );
    }

    public static Shape drawWebStyle ( final Graphics2D g2d, final JComponent component, final Color shadeColor, final int shadeWidth,
                                       final int round, final boolean fillBackground, final boolean webColored, final Color border )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, border, border );
    }

    public static Shape drawWebStyle ( final Graphics2D g2d, final JComponent component, final Color shadeColor, final int shadeWidth,
                                       final int round, final boolean fillBackground, final boolean webColored, final Color border,
                                       final Color disabledBorder )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, border, disabledBorder, 1f );
    }

    public static Shape drawWebStyle ( final Graphics2D g2d, final JComponent component, final Color shadeColor, final int shadeWidth,
                                       final int round, final boolean fillBackground, final boolean webColored, final Color border,
                                       final Color disabledBorder, final float opacity )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, border, disabledBorder,
                component.getBackground (), opacity );
    }

    public static Shape drawWebStyle ( final Graphics2D g2d, final JComponent component, final Color shadeColor, final int shadeWidth,
                                       final int round, final boolean fillBackground, final boolean webColored, final Color border,
                                       final Color disabledBorder, final Color background, final float opacity )
    {
        // todo Use simple drawRoundRect e.t.c. methods
        // todo Add new class "ShapeInfo" that will contain a shape data and pass it instead of shapes

        // Ignore incorrect or zero opacity
        if ( opacity <= 0f || opacity > 1f )
        {
            return null;
        }

        // State settings
        final Object aa = GraphicsUtils.setupAntialias ( g2d );
        final Composite oc = GraphicsUtils.setupAlphaComposite ( g2d, opacity, opacity < 1f );

        // Shapes
        final Shape borderShape = getWebBorderShape ( component, shadeWidth, round );

        // Outer shadow
        if ( component.isEnabled () && shadeColor != null )
        {
            GraphicsUtils.drawShade ( g2d, borderShape, shadeColor, shadeWidth );
        }

        // Background
        if ( fillBackground )
        {
            // Setup either cached gradient paint or single color paint
            g2d.setPaint ( webColored ? getWebGradientPaint ( 0, shadeWidth, 0, component.getHeight () - shadeWidth ) : background );

            // Fill background shape
            if ( round > 0 )
            {
                g2d.fillRoundRect ( shadeWidth, shadeWidth, component.getWidth () - shadeWidth * 2, component.getHeight () - shadeWidth * 2,
                        round * 2 + 2, round * 2 + 2 );
            }
            else
            {
                g2d.fillRect ( shadeWidth, shadeWidth, component.getWidth () - shadeWidth * 2, component.getHeight () - shadeWidth * 2 );
            }
        }

        // Border
        if ( border != null )
        {
            g2d.setPaint ( component.isEnabled () ? border : disabledBorder );
            g2d.draw ( borderShape );
        }

        // Restoring old values
        GraphicsUtils.restoreComposite ( g2d, oc, opacity < 1f );
        GraphicsUtils.restoreAntialias ( g2d, aa );

        return borderShape;
    }

    private static final Map<String, GradientPaint> gradientCache = new HashMap<String, GradientPaint> ();

    public static GradientPaint getWebGradientPaint ( final Rectangle bounds )
    {
        return getWebGradientPaint ( bounds.x, bounds.y, bounds.x, bounds.y + bounds.height );
    }

    public static GradientPaint getWebGradientPaint ( final int x1, final int y1, final int x2, final int y2 )
    {
        final String key = x1 + ";" + y1 + ";" + x2 + ";" + y2;
        if ( gradientCache.containsKey ( key ) )
        {
            return gradientCache.get ( key );
        }
        else
        {
            final GradientPaint gp = new GradientPaint ( x1, y1, StyleConstants.topBgColor, x2, y2, StyleConstants.bottomBgColor );
            gradientCache.put ( key, gp );
            return gp;
        }
    }

    public static Shape getWebBorderShape ( final JComponent component, final int shadeWidth, final int round )
    {
        if ( round > 0 )
        {
            return new RoundRectangle2D.Double ( shadeWidth, shadeWidth, component.getWidth () - shadeWidth * 2 - 1,
                    component.getHeight () - shadeWidth * 2 - 1, round * 2, round * 2 );
        }
        else
        {
            return new Rectangle2D.Double ( shadeWidth, shadeWidth, component.getWidth () - shadeWidth * 2 - 1,
                    component.getHeight () - shadeWidth * 2 - 1 );
        }
    }

    /**
     * Paints custom shaped web styled border within the component with shadow and background
     */

    public static void drawCustomWebBorder ( final Graphics2D g2d, final JComponent component, final Shape borderShape,
                                             final Color shadeColor, final int shadeWidth, final boolean fillBackground,
                                             final boolean webColored )
    {
        drawCustomWebBorder ( g2d, component, borderShape, shadeColor, shadeWidth, fillBackground, webColored, Color.GRAY,
                Color.LIGHT_GRAY );
    }

    public static void drawCustomWebBorder ( final Graphics2D g2d, final JComponent component, final Shape borderShape,
                                             final Color shadeColor, final int shadeWidth, final boolean fillBackground,
                                             final boolean webColored, final Color border, final Color disabledBorder )
    {
        drawCustomWebBorder ( g2d, component, borderShape, shadeColor, shadeWidth, fillBackground, webColored, border, disabledBorder,
                component.getBackground () );
    }

    public static void drawCustomWebBorder ( final Graphics2D g2d, final JComponent component, final Shape borderShape,
                                             final Color shadeColor, final int shadeWidth, final boolean fillBackground,
                                             final boolean webColored, final Color border, final Color disabledBorder,
                                             final Color backgroundColor )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Outer shadow
        if ( component.isEnabled () )
        {
            GraphicsUtils.drawShade ( g2d, borderShape, shadeColor, shadeWidth );
        }

        // Background
        if ( fillBackground )
        {
            if ( webColored )
            {
                final Rectangle shapeBounds = borderShape.getBounds ();
                g2d.setPaint ( new GradientPaint ( 0, shapeBounds.y, StyleConstants.topBgColor, 0, shapeBounds.y + shapeBounds.height,
                        StyleConstants.bottomBgColor ) );
                g2d.fill ( borderShape );
            }
            else
            {
                g2d.setPaint ( backgroundColor );
                g2d.fill ( borderShape );
            }
        }

        // Border
        if ( border != null )
        {
            g2d.setPaint ( component.isEnabled () ? border : disabledBorder );
            g2d.draw ( borderShape );
        }

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Draws custom shaped web styled focus within the component
     */

    public static void drawCustomWebFocus ( final Graphics2D g2d, final JComponent component, final FocusType focusType, final Shape shape )
    {
        drawCustomWebFocus ( g2d, component, focusType, shape, null );
    }

    public static void drawCustomWebFocus ( final Graphics2D g2d, final JComponent component, final FocusType focusType, final Shape shape,
                                            final Boolean mouseover )
    {
        drawCustomWebFocus ( g2d, component, focusType, shape, mouseover, null );
    }

    public static void drawCustomWebFocus ( final Graphics2D g2d, final JComponent component, final FocusType focusType, final Shape shape,
                                            final Boolean mouseover, Boolean hasFocus )
    {
        hasFocus = hasFocus != null ? hasFocus : component.hasFocus () && component.isEnabled ();
        if ( hasFocus && focusType.equals ( FocusType.componentFocus ) )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            final Stroke os = GraphicsUtils.setupStroke ( g2d, StyleConstants.focusStroke );

            g2d.setPaint ( StyleConstants.focusColor );
            g2d.draw ( shape );

            GraphicsUtils.restoreStroke ( g2d, os );
            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
        else if ( focusType.equals ( FocusType.fieldFocus ) && ( hasFocus || mouseover != null && mouseover ) )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            final Stroke os = GraphicsUtils.setupStroke ( g2d, StyleConstants.fieldFocusStroke );

            g2d.setPaint ( hasFocus ? StyleConstants.fieldFocusColor : StyleConstants.transparentFieldFocusColor );
            g2d.draw ( shape );

            GraphicsUtils.restoreStroke ( g2d, os );
            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }

    /**
     * Draws web styled selection using shapes operations.
     * This method is pretty slow and should not be used for multiply selections presentation.
     */

    public static int halfButton = 4;
    public static int halfSelector = 2;
    public static int halfLine = 1;
    public static int shadeWidth = 2;

    public static void drawWebSelection ( final Graphics2D g2d, final Color color, final int x, final int y, final int width,
                                          final int height, final boolean resizableLR, final boolean resizableUD,
                                          final boolean drawConnectors )
    {
        drawWebSelection ( g2d, color, new Rectangle ( x, y, width, height ), resizableLR, resizableUD, drawConnectors );
    }

    public static void drawWebSelection ( final Graphics2D g2d, final Color color, final int x, final int y, final int width,
                                          final int height, final boolean resizableLR, final boolean resizableUD,
                                          final boolean drawConnectors, final boolean drawSideControls )
    {
        drawWebSelection ( g2d, color, new Rectangle ( x, y, width, height ), resizableLR, resizableUD, drawConnectors, drawSideControls );
    }

    public static void drawWebSelection ( final Graphics2D g2d, final Color color, final Rectangle selection, final boolean resizableLR,
                                          final boolean resizableUD, final boolean drawConnectors )
    {
        drawWebSelection ( g2d, color, selection, resizableLR, resizableUD, drawConnectors, true );
    }

    public static void drawWebSelection ( final Graphics2D g2d, final Color color, Rectangle selection, final boolean resizableLR,
                                          final boolean resizableUD, final boolean drawConnectors, final boolean drawSideControls )
    {
        selection = GeometryUtils.validateRect ( selection );

        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final Area buttonsShape = new Area ();

        // Top
        if ( resizableUD )
        {
            if ( resizableLR )
            {
                buttonsShape.add ( new Area (
                        new Ellipse2D.Double ( selection.x - halfButton, selection.y - halfButton, halfButton * 2, halfButton * 2 ) ) );
                buttonsShape.add ( new Area (
                        new Ellipse2D.Double ( selection.x + selection.width - halfButton, selection.y - halfButton, halfButton * 2,
                                halfButton * 2 ) ) );
            }
            if ( drawSideControls )
            {
                buttonsShape.add ( new Area (
                        new Ellipse2D.Double ( selection.x + selection.width / 2 - halfButton, selection.y - halfButton, halfButton * 2,
                                halfButton * 2 ) ) );
            }
        }

        // Middle
        if ( resizableLR && drawSideControls )
        {
            buttonsShape.add ( new Area (
                    new Ellipse2D.Double ( selection.x - halfButton, selection.y + selection.height / 2 - halfButton, halfButton * 2,
                            halfButton * 2 ) ) );
            buttonsShape.add ( new Area (
                    new Ellipse2D.Double ( selection.x + selection.width - halfButton, selection.y + selection.height / 2 - halfButton,
                            halfButton * 2, halfButton * 2 ) ) );
        }

        // Bottom
        if ( resizableUD )
        {
            if ( resizableLR )
            {
                buttonsShape.add ( new Area (
                        new Ellipse2D.Double ( selection.x - halfButton, selection.y + selection.height - halfButton, halfButton * 2,
                                halfButton * 2 ) ) );
                buttonsShape.add ( new Area (
                        new Ellipse2D.Double ( selection.x + selection.width - halfButton, selection.y + selection.height - halfButton,
                                halfButton * 2, halfButton * 2 ) ) );
            }
            if ( drawSideControls )
            {
                buttonsShape.add ( new Area (
                        new Ellipse2D.Double ( selection.x + selection.width / 2 - halfButton, selection.y + selection.height - halfButton,
                                halfButton * 2, halfButton * 2 ) ) );
            }
        }

        // Button connectors
        if ( drawConnectors )
        {
            final Area selectionShape = new Area (
                    new RoundRectangle2D.Double ( selection.x - halfLine, selection.y - halfLine, selection.width + halfLine * 2,
                            selection.height + halfLine * 2, 5, 5 ) );
            selectionShape.subtract ( new Area (
                    new RoundRectangle2D.Double ( selection.x + halfLine, selection.y + halfLine, selection.width - halfLine * 2,
                            selection.height - halfLine * 2, 3, 3 ) ) );
            buttonsShape.add ( selectionShape );
        }

        // Shade
        GraphicsUtils.drawShade ( g2d, buttonsShape, Color.GRAY, shadeWidth );

        // Border
        g2d.setPaint ( Color.GRAY );
        g2d.draw ( buttonsShape );

        // Background
        g2d.setPaint ( color );
        g2d.fill ( buttonsShape );

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    public static void drawWebSelector ( final Graphics2D g2d, final Color color, Rectangle selection, final int selector )
    {
        selection = GeometryUtils.validateRect ( selection );

        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        final Ellipse2D buttonsShape;
        if ( selector == SwingConstants.NORTH_WEST )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x - halfSelector, selection.y - halfSelector, halfSelector * 2, halfSelector * 2 );
        }
        else if ( selector == SwingConstants.NORTH )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x + selection.width / 2 - halfSelector, selection.y - halfSelector, halfSelector * 2,
                            halfSelector * 2 );
        }
        else if ( selector == SwingConstants.NORTH_EAST )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x + selection.width - halfSelector, selection.y - halfSelector, halfSelector * 2,
                            halfSelector * 2 );
        }
        else if ( selector == SwingConstants.WEST )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x - halfSelector, selection.y + selection.height / 2 - halfSelector, halfSelector * 2,
                            halfSelector * 2 );
        }
        else if ( selector == SwingConstants.EAST )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x + selection.width - halfSelector, selection.y + selection.height / 2 - halfSelector,
                            halfSelector * 2, halfSelector * 2 );
        }
        else if ( selector == SwingConstants.SOUTH_WEST )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x - halfSelector, selection.y + selection.height - halfSelector, halfSelector * 2,
                            halfSelector * 2 );
        }
        else if ( selector == SwingConstants.SOUTH )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x + selection.width / 2 - halfSelector, selection.y + selection.height - halfSelector,
                            halfSelector * 2, halfSelector * 2 );
        }
        else if ( selector == SwingConstants.SOUTH_EAST )
        {
            buttonsShape =
                    new Ellipse2D.Double ( selection.x + selection.width - halfSelector, selection.y + selection.height - halfSelector,
                            halfSelector * 2, halfSelector * 2 );
        }
        else
        {
            buttonsShape = null;
        }

        // Background
        g2d.setPaint ( color );
        g2d.fill ( buttonsShape );

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    /**
     * Draws web styled selection using predefined images set.
     * This method is much faster than the one before but has less settings due to the predefined graphics
     * todo Get rid of this stuff and resources for good
     */

    private static final NinePatchIcon conn = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/conn.9.png" ) );
    private static final NinePatchIcon lr_conn = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/lr_conn.9.png" ) );
    private static final NinePatchIcon ud_conn = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/ud_conn.9.png" ) );
    private static final NinePatchIcon corners_conn =
            new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/corners_conn.9.png" ) );
    private static final NinePatchIcon full_conn = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/full_conn.9.png" ) );
    private static final NinePatchIcon lr = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/lr.9.png" ) );
    private static final NinePatchIcon ud = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/ud.9.png" ) );
    private static final NinePatchIcon corners = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/corners.9.png" ) );
    private static final NinePatchIcon full = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/full.9.png" ) );

    private static final ImageIcon gripper = new ImageIcon ( LafUtils.class.getResource ( "icons/selection/gripper.png" ) );

    public static void drawWebIconedSelection ( final Graphics2D g2d, final Rectangle selection, final boolean resizableLR,
                                                final boolean resizableUD, final boolean drawConnectors )
    {
        drawWebIconedSelection ( g2d, selection, resizableLR, resizableUD, drawConnectors, true );
    }

    public static void drawWebIconedSelection ( final Graphics2D g2d, Rectangle selection, final boolean resizableLR,
                                                final boolean resizableUD, final boolean drawConnectors, final boolean drawSideControls )
    {
        selection = GeometryUtils.validateRect ( selection );

        // Calculating selection rect
        final Rectangle rect = calculateIconedRect ( selection );

        // Drawing selection
        if ( drawConnectors )
        {
            if ( !resizableLR && !resizableUD )
            {
                conn.paintIcon ( g2d, rect );
            }
            else if ( resizableLR && !resizableUD && drawSideControls )
            {
                lr_conn.paintIcon ( g2d, rect );
            }
            else if ( !resizableLR && resizableUD && drawSideControls )
            {
                ud_conn.paintIcon ( g2d, rect );
            }
            else if ( resizableLR && resizableUD )
            {
                if ( drawSideControls )
                {
                    full_conn.paintIcon ( g2d, rect );
                }
                else
                {
                    corners_conn.paintIcon ( g2d, rect );
                }
            }
        }
        else
        {
            if ( resizableLR && !resizableUD && drawSideControls )
            {
                lr.paintIcon ( g2d, rect );
            }
            else if ( !resizableLR && resizableUD && drawSideControls )
            {
                ud.paintIcon ( g2d, rect );
            }
            else if ( resizableLR && resizableUD )
            {
                if ( drawSideControls )
                {
                    full.paintIcon ( g2d, rect );
                }
                else
                {
                    corners.paintIcon ( g2d, rect );
                }
            }
        }
    }

    public static void drawWebIconedSelector ( final Graphics2D g2d, Rectangle selection, final int selector )
    {
        selection = GeometryUtils.validateRect ( selection );

        // Calculating selector rect
        final Rectangle rect = calculateIconedRect ( selection );

        // Drawing selector
        getSelectorIcon ( selector ).paintIcon ( g2d, rect );
    }

    public static void drawWebIconedGripper ( final Graphics2D g2d, final Point point )
    {
        drawWebIconedGripper ( g2d, point.x, point.y );
    }

    public static void drawWebIconedGripper ( final Graphics2D g2d, final int x, final int y )
    {
        g2d.drawImage ( gripper.getImage (), x - gripper.getIconWidth () / 2, y - gripper.getIconHeight () / 2, null );
    }

    private static Rectangle calculateIconedRect ( final Rectangle selection )
    {
        // Recalculating coordinates to iconed view
        return new Rectangle ( selection.x - halfButton - shadeWidth, selection.y - halfButton - shadeWidth,
                selection.width + halfButton * 2 + shadeWidth * 2, selection.height + halfButton * 2 + shadeWidth * 2 );
    }

    private static final Map<Integer, NinePatchIcon> selectorCache = new HashMap<Integer, NinePatchIcon> ();

    private static NinePatchIcon getSelectorIcon ( final int selector )
    {
        if ( selectorCache.containsKey ( selector ) )
        {
            return selectorCache.get ( selector );
        }
        else
        {
            final NinePatchIcon npi = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/selector" + selector + ".9.png" ) );
            selectorCache.put ( selector, npi );
            return npi;
        }
    }

    /**
     * Returns X and Y shift for the specified text and font metrics.
     * It will return values you need to add to the point relative to which you want to center text.
     *
     * @param metrics font metrics
     * @param text    text
     * @return X and Y shift for the specified text and font metrics
     */
    public static Point getTextCenterShift ( final FontMetrics metrics, final String text )
    {
        return new Point ( getTextCenterShiftX ( metrics, text ), getTextCenterShiftY ( metrics ) );
    }

    /**
     * Returns X shift for the specified text and font metrics.
     * It will return value you need to add to the point X coordinate relative to which you want to horizontally center text.
     *
     * @param metrics font metrics
     * @param text    text
     * @return X shift for the specified text and font metrics
     */
    public static int getTextCenterShiftX ( final FontMetrics metrics, final String text )
    {
        return -metrics.stringWidth ( text ) / 2;
    }

    /**
     * Returns Y shift for the specified font metrics.
     * It will return value you need to add to the point Y coordinate relative to which you want to vertically center text.
     *
     * @param metrics font metrics
     * @return Y shift for the specified font metrics
     */
    public static int getTextCenterShiftY ( final FontMetrics metrics )
    {
        return ( metrics.getAscent () - metrics.getLeading () - metrics.getDescent () ) / 2;
    }

    /**
     * Returns shape provider for the specified component or null if shape provider is not supported.
     * This might be used to provide dynamic component shape to other components.
     *
     * @param component component to process
     * @return shape provider for the specified component or null if shape provider is not supported
     */
    public static ShapeProvider getShapeProvider ( final Component component )
    {
        if ( component instanceof ShapeProvider )
        {
            return ( ShapeProvider ) component;
        }
        else
        {
            final ComponentUI ui = getUI ( component );
            if ( ui != null && ui instanceof ShapeProvider )
            {
                return ( ShapeProvider ) ui;
            }
        }
        return null;
    }

    /**
     * Returns styleable element for the specified component.
     *
     * @param c component to retrieve styleable element for
     * @return styleable element for the specified component
     */
    public static Styleable getStyleable ( final Component c )
    {
        return c != null ? c instanceof Styleable ? ( Styleable ) c : getStyleable ( LafUtils.getUI ( c ) ) : null;
    }

    /**
     * Returns styleable element for the specified ui.
     *
     * @param ui ui to retrieve styleable element for
     * @return styleable element for the specified ui
     */
    public static Styleable getStyleable ( final ComponentUI ui )
    {
        return ui != null && ui instanceof Styleable ? ( Styleable ) ui : null;
    }

    /**
     * Returns TexturePainter which is read from the source.
     *
     * @param source one of possible sources: URL, String, File, Reader, InputStream
     * @return TexturePainter
     */
    public static TexturePainter loadTexturePainter ( final Object source )
    {
        return loadTexturePainter ( XmlUtils.loadResourceFile ( source ) );
    }

    /**
     * Returns TexturePainter which is read from specified ResourceFile.
     *
     * @param resource file description
     * @return TexturePainter
     */
    public static TexturePainter loadTexturePainter ( final ResourceFile resource )
    {
        return new TexturePainter ( XmlUtils.loadImageIcon ( resource ) );
    }

    /**
     * Installs specified L&amp;F as current application's L&amp;F.
     *
     * @param clazz L&amp;F class
     * @return true if L&amp;F was installed successfully, false otherwise
     */
    public static boolean setupLookAndFeelSafely ( final Class<? extends LookAndFeel> clazz )
    {
        return setupLookAndFeelSafely ( clazz.getCanonicalName () );
    }

    /**
     * Installs specified L&amp;F as current application's L&amp;F.
     *
     * @param className L&amp;F canonical class name
     * @return true if L&amp;F was installed successfully, false otherwise
     */
    public static boolean setupLookAndFeelSafely ( final String className )
    {
        try
        {
            UIManager.setLookAndFeel ( className );
            return true;
        }
        catch ( final Throwable e )
        {
            Log.get ().error ( "Unable to initialize L&F for class name: " + className, e );
            return false;
        }
    }
}