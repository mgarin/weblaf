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

import com.alee.laf.StyleConstants;
import com.alee.laf.label.WebLabel;
import com.alee.laf.text.WebTextField;
import com.alee.utils.laf.FocusType;
import com.alee.utils.laf.ShadeType;
import com.alee.utils.ninepatch.NinePatchIcon;
import com.alee.utils.swing.BorderMethods;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 27.04.11 Time: 17:41
 */

public final class LafUtils
{
    /**
     * Creates border for web components.
     */

    public static Border createWebBorder ( Insets insets )
    {
        return new EmptyBorder ( insets.top, insets.left, insets.bottom, insets.right );
    }

    public static Border createWebBorder ( int top, int left, int bottom, int right )
    {
        return new EmptyBorder ( top, left, bottom, right );
    }

    public static Border createWebBorder ( int margin )
    {
        return new EmptyBorder ( margin, margin, margin, margin );
    }

    /**
     * Fills either clipped or visible rect with component background color if its opaque
     */

    public static void fillVisibleBackground ( Graphics g, JComponent c )
    {
        if ( c.isOpaque () )
        {
            g.setColor ( c.getBackground () );
            fillVisible ( g, c );
        }
    }

    /**
     * Fills either clipped or visible rect
     */

    public static void fillVisible ( Graphics g, JComponent c )
    {
        Shape clip = g.getClip ();
        Rectangle rect = clip != null ? clip.getBounds () : c.getVisibleRect ();
        g.fillRect ( rect.x, rect.y, rect.width, rect.height );
    }

    /**
     * Nullifies button styles
     */

    public static void nullifyButtonUI ( JButton button )
    {
        button.setUI ( new BasicButtonUI () );
        button.setMargin ( new Insets ( 0, 0, 0, 0 ) );
        button.setBorder ( null );
        button.setBorderPainted ( false );
        button.setContentAreaFilled ( false );
        button.setFocusable ( false );
        button.setOpaque ( false );
    }

    /**
     * Creates rounded shape based on its corner points
     */

    public static Shape createRoundedShape ( int round, int... points )
    {
        if ( points == null || points.length % 2 != 0 )
        {
            throw new RuntimeException ( "Incorrect x,y combinations amount" );
        }
        Point[] fp = new Point[ points.length / 2 ];
        for ( int i = 0; i < points.length; i += 2 )
        {
            fp[ i / 2 ] = new Point ( points[ i ], points[ i + 1 ] );
        }
        return createRoundedShape ( round, fp );
    }

    public static Shape createRoundedShape ( int round, Point... points )
    {
        return createRoundedShape ( round, points, null );
    }

    public static Shape createRoundedShape ( int round, Point[] points, boolean[] rounded )
    {
        if ( points == null || points.length < 3 )
        {
            throw new RuntimeException ( "There should be atleast three points presented" );
        }
        if ( rounded != null && rounded.length != points.length )
        {
            throw new RuntimeException ( "Rouned marks array size should fit points array size" );
        }

        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        for ( int i = 0; i < points.length; i++ )
        {
            Point p = points[ i ];
            if ( i == 0 )
            {
                // Start part
                Point beforePoint = points[ points.length - 1 ];
                if ( round == 0 || rounded != null && !rounded[ points.length - 1 ] )
                {
                    gp.moveTo ( beforePoint.x, beforePoint.y );
                }
                else
                {
                    Point actualBeforePoint = getRoundSidePoint ( round, beforePoint, p );
                    gp.moveTo ( actualBeforePoint.x, actualBeforePoint.y );
                }
                if ( round == 0 || rounded != null && !rounded[ i ] )
                {
                    gp.lineTo ( p.x, p.y );
                }
                else
                {
                    Point before = getRoundSidePoint ( round, p, beforePoint );
                    Point after = getRoundSidePoint ( round, p, points[ i + 1 ] );
                    gp.lineTo ( before.x, before.y );
                    gp.quadTo ( p.x, p.y, after.x, after.y );
                }
            }
            else
            {
                // Proceeding to next point     
                if ( round == 0 || rounded != null && !rounded[ i ] )
                {
                    gp.lineTo ( p.x, p.y );
                }
                else
                {
                    Point before = getRoundSidePoint ( round, p, points[ i - 1 ] );
                    Point after = getRoundSidePoint ( round, p, points[ i < points.length - 1 ? i + 1 : 0 ] );
                    gp.lineTo ( before.x, before.y );
                    gp.quadTo ( p.x, p.y, after.x, after.y );
                }
            }
        }
        return gp;
    }

    private static Point getRoundSidePoint ( int round, Point from, Point to )
    {
        if ( from.y == to.y )
        {
            if ( from.x < to.x )
            {
                return new Point ( from.x + Math.min ( round, ( to.x - from.x ) / 2 ), from.y );
            }
            else
            {
                return new Point ( from.x - Math.min ( round, ( from.x - to.x ) / 2 ), from.y );
            }
        }
        else if ( from.x == to.x )
        {
            if ( from.y < to.y )
            {
                return new Point ( from.x, from.y + Math.min ( round, ( to.y - from.y ) / 2 ) );
            }
            else
            {
                return new Point ( from.x, from.y - Math.min ( round, ( from.y - to.y ) / 2 ) );
            }
        }
        else
        {
            // todo do for non-90-degree angles
            return null;
        }
    }

    /**
     * Draws alpha-background
     */

    public static void drawAlphaLayer ( Graphics2D g2d, Rectangle rectangle )
    {
        drawAlphaLayer ( g2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height );
    }

    public static void drawAlphaLayer ( Graphics2D g2d, int x, int y, int width, int height )
    {
        drawAlphaLayer ( g2d, x, y, width, height, StyleConstants.ALPHA_RECT_SIZE );
    }

    public static void drawAlphaLayer ( Graphics2D g2d, Rectangle rectangle, int size )
    {
        drawAlphaLayer ( g2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height, size );
    }

    public static void drawAlphaLayer ( Graphics2D g2d, int x, int y, int width, int height, int size )
    {
        drawAlphaLayer ( g2d, x, y, width, height, size, StyleConstants.LIGHT_ALPHA, StyleConstants.DARK_ALPHA );
    }

    public static void drawAlphaLayer ( Graphics2D g2d, Rectangle rectangle, int size, Color light, Color dark )
    {
        drawAlphaLayer ( g2d, rectangle.x, rectangle.y, rectangle.width, rectangle.height, size, light, dark );
    }

    public static void drawAlphaLayer ( Graphics2D g2d, int x, int y, int width, int height, int size, Color light, Color dark )
    {
        // todo Optimize paint by using generated texture image
        int xAmount = width / size + 1;
        int yAmount = height / size + 1;
        boolean lightColor;
        for ( int i = 0; i < xAmount; i++ )
        {
            for ( int j = 0; j < yAmount; j++ )
            {
                lightColor = ( i + j ) % 2 == 0;
                Color color = lightColor ? light : dark;
                if ( color != null )
                {
                    g2d.setPaint ( color );
                    int w = ( x + i * size + size > x + width ) ? ( width - i * size ) : size;
                    int h = ( y + j * size + size > y + height ) ? ( height - j * size ) : size;
                    g2d.fillRect ( x + i * size, y + j * size, w, h );
                }
            }
        }
    }

    /**
     * Setting clip Shape by taking old clip Shape into account
     */

    public static Shape intersectClip ( Graphics2D g2d, Shape clip )
    {
        return intersectClip ( g2d, clip, true );
    }

    public static Shape intersectClip ( Graphics2D g2d, Shape clip, boolean shouldSetup )
    {
        if ( shouldSetup && clip != null )
        {
            Shape oldClip = g2d.getClip ();

            // Optimized by Graphics2D clip intersection
            g2d.clip ( clip );

            return oldClip;
        }
        else
        {
            return null;
        }
    }

    public static Shape subtractClip ( Graphics g, Shape clip )
    {
        return subtractClip ( g, clip, true );
    }

    public static Shape subtractClip ( Graphics g, Shape clip, boolean shouldSetup )
    {
        if ( shouldSetup && clip != null )
        {
            Shape oldClip = g.getClip ();
            if ( oldClip != null )
            {
                // Area-based substraction
                Area finalClip = new Area ( oldClip );
                finalClip.subtract ( new Area ( clip ) );
                g.setClip ( finalClip );
            }
            return oldClip;
        }
        else
        {
            return null;
        }
    }

    public static void restoreClip ( Graphics g, Shape clip )
    {
        restoreClip ( g, clip, true );
    }

    public static void restoreClip ( Graphics g, Shape clip, boolean shouldRestore )
    {
        if ( shouldRestore && clip != null )
        {
            g.setClip ( clip );
        }
    }

    /**
     * Setting new stroke
     */

    public static Stroke setupStroke ( Graphics2D g2d, Stroke stroke )
    {
        return setupStroke ( g2d, stroke, true );
    }

    public static Stroke setupStroke ( Graphics2D g2d, Stroke stroke, boolean shouldSetup )
    {
        if ( shouldSetup && stroke != null )
        {
            Stroke old = g2d.getStroke ();
            g2d.setStroke ( stroke );
            return old;
        }
        else
        {
            return null;
        }
    }

    public static void restoreStroke ( Graphics2D g2d, Stroke stroke )
    {
        restoreStroke ( g2d, stroke, true );
    }

    public static void restoreStroke ( Graphics2D g2d, Stroke stroke, boolean shouldRestore )
    {
        if ( shouldRestore && stroke != null )
        {
            g2d.setStroke ( stroke );
        }
    }

    /**
     * Setting image quality on
     */

    public static Object setupImageQuality ( Graphics g )
    {
        return setupImageQuality ( ( Graphics2D ) g );
    }

    public static Object setupImageQuality ( Graphics2D g2d )
    {
        Object old = g2d.getRenderingHint ( RenderingHints.KEY_INTERPOLATION );
        g2d.setRenderingHint ( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
        return old;
    }

    public static void restoreImageQuality ( Graphics g, Object old )
    {
        restoreImageQuality ( ( Graphics2D ) g, old );
    }

    public static void restoreImageQuality ( Graphics2D g2d, Object old )
    {
        g2d.setRenderingHint ( RenderingHints.KEY_INTERPOLATION, old != null ? old : RenderingHints.VALUE_INTERPOLATION_BILINEAR );
    }

    /**
     * Setting font
     */

    public static Font setupFont ( Graphics g, Font font )
    {
        if ( font != null )
        {
            final Font oldFont = g.getFont ();
            g.setFont ( font );
            return oldFont;
        }
        else
        {
            return null;
        }
    }

    public static void restoreFont ( Graphics g, Font font )
    {
        if ( font != null )
        {
            g.setFont ( font );
        }
    }

    /**
     * Setting antialias on
     */

    public static Object setupAntialias ( Graphics g )
    {
        return setupAntialias ( ( Graphics2D ) g, RenderingHints.VALUE_ANTIALIAS_ON );
    }

    public static Object setupAntialias ( Graphics2D g2d )
    {
        return setupAntialias ( g2d, RenderingHints.VALUE_ANTIALIAS_ON );
    }

    public static Object disableAntialias ( Graphics g )
    {
        return setupAntialias ( ( Graphics2D ) g, RenderingHints.VALUE_ANTIALIAS_OFF );
    }

    public static Object disableAntialias ( Graphics2D g2d )
    {
        return setupAntialias ( g2d, RenderingHints.VALUE_ANTIALIAS_OFF );
    }

    private static Object setupAntialias ( Graphics2D g2d, Object aa )
    {
        Object old = g2d.getRenderingHint ( RenderingHints.KEY_ANTIALIASING );
        g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, aa );
        return old;
    }

    public static void restoreAntialias ( Graphics g, Object old )
    {
        restoreAntialias ( ( Graphics2D ) g, old );
    }

    public static void restoreAntialias ( Graphics2D g2d, Object old )
    {
        g2d.setRenderingHint ( RenderingHints.KEY_ANTIALIASING, old );
    }

    /**
     * Installing system text settings
     */

    private static boolean systemTextHintsInitialized = false;
    private static Map systemTextHints = null;

    public static Map getSystemTextHints ()
    {
        if ( !systemTextHintsInitialized )
        {
            systemTextHints = ( Map ) Toolkit.getDefaultToolkit ().getDesktopProperty ( "awt.font.desktophints" );
            systemTextHintsInitialized = true;
        }
        return systemTextHints;
    }

    public static void setupSystemTextHints ( Graphics g )
    {
        setupSystemTextHints ( ( Graphics2D ) g );
    }

    public static void setupSystemTextHints ( Graphics2D g2d )
    {
        Map systemTextHints = getSystemTextHints ();
        if ( systemTextHints != null )
        {
            g2d.addRenderingHints ( systemTextHints );
        }
    }

    /**
     * Setting AlphaComposite by taking old AlphaComposite settings into account
     */

    public static Composite setupAlphaComposite ( Graphics2D g2d, Float alpha )
    {
        return setupAlphaComposite ( g2d, alpha, true );
    }

    public static Composite setupAlphaComposite ( Graphics2D g2d, Float alpha, boolean shouldSetup )
    {
        return setupAlphaComposite ( g2d, g2d.getComposite (), alpha, shouldSetup );
    }

    public static Composite setupAlphaComposite ( Graphics2D g2d, Composite composeWith, Float alpha )
    {
        return setupAlphaComposite ( g2d, composeWith, alpha, true );
    }

    public static Composite setupAlphaComposite ( Graphics2D g2d, Composite composeWith, Float alpha, boolean shouldSetup )
    {
        Composite comp = g2d.getComposite ();
        if ( !shouldSetup || alpha == null )
        {
            return comp;
        }

        // Determining old composite alpha
        float currentComposite = 1f;
        if ( composeWith != null && composeWith instanceof AlphaComposite )
        {
            currentComposite = ( ( AlphaComposite ) composeWith ).getAlpha ();
        }

        // Creating new composite
        AlphaComposite newComposite = AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, currentComposite * alpha );
        g2d.setComposite ( newComposite );

        return comp;
    }

    public static void restoreComposite ( Graphics2D g2d, Composite composite )
    {
        g2d.setComposite ( composite );
    }

    public static void restoreComposite ( Graphics2D g2d, Composite composite, boolean shouldRestore )
    {
        if ( shouldRestore )
        {
            g2d.setComposite ( composite );
        }
    }

    /**
     * Determines real text size
     */

    public static Rectangle getTextBounds ( String text, Graphics g, Font font )
    {
        return getTextBounds ( text, ( Graphics2D ) g, font );
    }

    public static Rectangle getTextBounds ( String text, Graphics2D g2d, Font font )
    {
        FontRenderContext renderContext = g2d.getFontRenderContext ();
        GlyphVector glyphVector = font.createGlyphVector ( renderContext, text );
        return glyphVector.getVisualBounds ().getBounds ();
    }

    /**
     * Paints web styled border within the component with shadow and background if needed
     */

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component )
    {
        return drawWebStyle ( g2d, component, StyleConstants.shadeColor, StyleConstants.shadeWidth, StyleConstants.smallRound );
    }

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component, Color shadeColor, int shadeWidth, int round )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, true );
    }

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component, Color shadeColor, int shadeWidth, int round,
                                       boolean fillBackground )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, false );
    }

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component, Color shadeColor, int shadeWidth, int round,
                                       boolean fillBackground, boolean webColored )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, StyleConstants.darkBorderColor,
                StyleConstants.disabledBorderColor );
    }

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component, Color shadeColor, int shadeWidth, int round,
                                       boolean fillBackground, boolean webColored, float opacity )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, StyleConstants.darkBorderColor,
                StyleConstants.disabledBorderColor, opacity );
    }

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component, Color shadeColor, int shadeWidth, int round,
                                       boolean fillBackground, boolean webColored, Color border )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, border, border );
    }

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component, Color shadeColor, int shadeWidth, int round,
                                       boolean fillBackground, boolean webColored, Color border, Color disabledBorder )
    {
        return drawWebStyle ( g2d, component, shadeColor, shadeWidth, round, fillBackground, webColored, border, disabledBorder, 1f );
    }

    public static Shape drawWebStyle ( Graphics2D g2d, JComponent component, Color shadeColor, int shadeWidth, int round,
                                       boolean fillBackground, boolean webColored, Color border, Color disabledBorder, float opacity )
    {
        // todo Use simple drawRoundRect e.t.c. methods
        // todo Add new class "ShapeInfo" that will contain a shape data and pass it instead of shapes

        // Ignore incorrect or zero opacity
        if ( opacity <= 0f || opacity > 1f )
        {
            return null;
        }

        // State settings
        Object aa = setupAntialias ( g2d );
        Composite oc = setupAlphaComposite ( g2d, opacity, opacity < 1f );

        // Shapes
        Shape borderShape = getWebBorderShape ( component, shadeWidth, round );

        // Outer shadow
        if ( component.isEnabled () && shadeColor != null )
        {
            drawShade ( g2d, borderShape, shadeColor, shadeWidth );
        }

        // Background
        if ( fillBackground )
        {
            // Setup either cached gradient paint or single color paint
            g2d.setPaint ( webColored ? getWebGradientPaint ( 0, shadeWidth, 0, component.getHeight () - shadeWidth ) :
                    component.getBackground () );

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
        restoreComposite ( g2d, oc, opacity < 1f );
        restoreAntialias ( g2d, aa );

        return borderShape;
    }

    private static Map<String, GradientPaint> gradientCache = new HashMap<String, GradientPaint> ();

    public static GradientPaint getWebGradientPaint ( Rectangle bounds )
    {
        return getWebGradientPaint ( bounds.x, bounds.y, bounds.x, bounds.y + bounds.height );
    }

    public static GradientPaint getWebGradientPaint ( int x1, int y1, int x2, int y2 )
    {
        String key = x1 + ";" + y1 + ";" + x2 + ";" + y2;
        if ( gradientCache.containsKey ( key ) )
        {
            return gradientCache.get ( key );
        }
        else
        {
            GradientPaint gp = new GradientPaint ( x1, y1, StyleConstants.topBgColor, x2, y2, StyleConstants.bottomBgColor );
            gradientCache.put ( key, gp );
            return gp;
        }
    }

    public static Shape getWebBorderShape ( JComponent component, int shadeWidth, int round )
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

    public static void drawCustomWebBorder ( Graphics2D g2d, JComponent component, Shape borderShape, Color shadeColor, int shadeWidth,
                                             boolean fillBackground, boolean webColored )
    {
        drawCustomWebBorder ( g2d, component, borderShape, shadeColor, shadeWidth, fillBackground, webColored, Color.GRAY,
                Color.LIGHT_GRAY );
    }

    public static void drawCustomWebBorder ( Graphics2D g2d, JComponent component, Shape borderShape, Color shadeColor, int shadeWidth,
                                             boolean fillBackground, boolean webColored, Color border, Color disabledBorder )
    {
        Object aa = setupAntialias ( g2d );

        // Outer shadow
        if ( component.isEnabled () )
        {
            drawShade ( g2d, borderShape, shadeColor, shadeWidth );
        }

        // Background
        if ( fillBackground )
        {
            if ( webColored )
            {
                Rectangle shapeBounds = borderShape.getBounds ();
                g2d.setPaint ( new GradientPaint ( 0, shapeBounds.y, StyleConstants.topBgColor, 0, shapeBounds.y + shapeBounds.height,
                        StyleConstants.bottomBgColor ) );
                g2d.fill ( borderShape );
            }
            else
            {
                g2d.setPaint ( component.getBackground () );
                g2d.fill ( borderShape );
            }
        }

        // Border
        if ( border != null )
        {
            g2d.setPaint ( component.isEnabled () ? border : disabledBorder );
            g2d.draw ( borderShape );
        }

        restoreAntialias ( g2d, aa );
    }

    /**
     * Paints web styled focus within the component
     */

    public static boolean drawWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, int shadeWidth, int round )
    {
        return drawWebFocus ( g2d, component, focusType, shadeWidth, round, null );
    }

    public static boolean drawWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, int shadeWidth, int round,
                                         Boolean mouseover )
    {
        return drawWebFocus ( g2d, component, focusType, shadeWidth, round, mouseover, null );
    }

    public static boolean drawWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, int shadeWidth, int round,
                                         Boolean mouseover, Boolean hasFocus )
    {
        return drawWebFocus ( g2d, component, focusType, shadeWidth, round, mouseover, hasFocus,
                focusType.equals ( FocusType.componentFocus ) ? StyleConstants.focusColor : StyleConstants.fieldFocusColor );
    }

    public static boolean drawWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, int shadeWidth, int round,
                                         Boolean mouseover, Boolean hasFocus, Color color )
    {
        return drawWebFocus ( g2d, component, focusType, shadeWidth, round, mouseover, hasFocus, color,
                focusType.equals ( FocusType.componentFocus ) ? StyleConstants.focusStroke : StyleConstants.fieldFocusStroke );
    }

    public static boolean drawWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, int shadeWidth, int round,
                                         Boolean mouseover, Boolean hasFocus, Color color, Stroke stroke )
    {
        hasFocus = hasFocus != null ? hasFocus : component.hasFocus () && component.isEnabled ();
        if ( hasFocus && focusType.equals ( FocusType.componentFocus ) )
        {
            Object aa = setupAntialias ( g2d );
            Stroke os = setupStroke ( g2d, stroke );

            g2d.setPaint ( color );
            g2d.draw ( getWebFocusShape ( component, focusType, shadeWidth, round ) );

            restoreStroke ( g2d, os );
            restoreAntialias ( g2d, aa );

            return true;
        }
        else if ( focusType.equals ( FocusType.fieldFocus ) && ( hasFocus || mouseover != null && mouseover ) )
        {
            Object aa = setupAntialias ( g2d );
            Stroke os = setupStroke ( g2d, stroke );

            //            g2d.setPaint ( hasFocus ? StyleConstants.fieldFocusColor :
            //                    StyleConstants.transparentFieldFocusColor );
            g2d.setPaint ( color );
            g2d.draw ( getWebFocusShape ( component, focusType, shadeWidth, round ) );

            restoreStroke ( g2d, os );
            restoreAntialias ( g2d, aa );

            return true;
        }
        else
        {
            return false;
        }
    }

    public static Shape getWebFocusShape ( JComponent component, FocusType focusType, int shadeWidth, int round )
    {
        // Focus side spacing
        int spacing = focusType.equals ( FocusType.componentFocus ) ? 2 : 0;

        // Corners rounding
        round = focusType.equals ( FocusType.componentFocus ) ? Math.max ( 0, round - 2 ) : round;

        // Final focus shape
        if ( round > 0 )
        {
            return new RoundRectangle2D.Double ( shadeWidth + spacing, shadeWidth + spacing,
                    component.getWidth () - shadeWidth * 2 - spacing * 2 - 1, component.getHeight () - shadeWidth * 2 - spacing * 2 - 1,
                    round * 2, round * 2 );
        }
        else
        {
            return new Rectangle2D.Double ( shadeWidth + spacing, shadeWidth + spacing,
                    component.getWidth () - shadeWidth * 2 - spacing * 2 - 1, component.getHeight () - shadeWidth * 2 - spacing * 2 - 1 );
        }
    }

    /**
     * Draws custom shaped web styled focus within the component
     */

    public static void drawCustomWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, Shape shape )
    {
        drawCustomWebFocus ( g2d, component, focusType, shape, null );
    }

    public static void drawCustomWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, Shape shape, Boolean mouseover )
    {
        drawCustomWebFocus ( g2d, component, focusType, shape, mouseover, null );
    }

    public static void drawCustomWebFocus ( Graphics2D g2d, JComponent component, FocusType focusType, Shape shape, Boolean mouseover,
                                            Boolean hasFocus )
    {
        hasFocus = hasFocus != null ? hasFocus : component.hasFocus () && component.isEnabled ();
        if ( hasFocus && focusType.equals ( FocusType.componentFocus ) )
        {
            Object aa = setupAntialias ( g2d );
            Stroke os = setupStroke ( g2d, StyleConstants.focusStroke );

            g2d.setPaint ( StyleConstants.focusColor );
            g2d.draw ( shape );

            restoreStroke ( g2d, os );
            restoreAntialias ( g2d, aa );
        }
        else if ( focusType.equals ( FocusType.fieldFocus ) && ( hasFocus || mouseover != null && mouseover ) )
        {
            Object aa = setupAntialias ( g2d );
            Stroke os = setupStroke ( g2d, StyleConstants.fieldFocusStroke );

            g2d.setPaint ( hasFocus ? StyleConstants.fieldFocusColor : StyleConstants.transparentFieldFocusColor );
            g2d.draw ( shape );

            restoreStroke ( g2d, os );
            restoreAntialias ( g2d, aa );
        }
    }

    /**
     * Draws web styled shade using specified shape
     */

    public static void drawShade ( Graphics2D g2d, Shape shape, Color shadeColor, int width )
    {
        drawShade ( g2d, shape, StyleConstants.shadeType, shadeColor, width );
    }

    public static void drawShade ( Graphics2D g2d, Shape shape, ShadeType shadeType, Color shadeColor, int width )
    {
        drawShade ( g2d, shape, shadeType, shadeColor, width, null, true );
    }

    public static void drawShade ( Graphics2D g2d, Shape shape, Color shadeColor, int width, Shape clip )
    {
        drawShade ( g2d, shape, StyleConstants.shadeType, shadeColor, width, clip, true );
    }

    public static void drawShade ( Graphics2D g2d, Shape shape, ShadeType shadeType, Color shadeColor, int width, Shape clip )
    {
        drawShade ( g2d, shape, shadeType, shadeColor, width, clip, true );
    }

    public static void drawShade ( Graphics2D g2d, Shape shape, Color shadeColor, int width, boolean round )
    {
        drawShade ( g2d, shape, StyleConstants.shadeType, shadeColor, width, null, round );
    }

    public static void drawShade ( Graphics2D g2d, Shape shape, ShadeType shadeType, Color shadeColor, int width, boolean round )
    {
        drawShade ( g2d, shape, shadeType, shadeColor, width, null, round );
    }

    public static void drawShade ( Graphics2D g2d, Shape shape, Color shadeColor, int width, Shape clip, boolean round )
    {
        drawShade ( g2d, shape, StyleConstants.shadeType, shadeColor, width, clip, round );
    }

    public static void drawShade ( Graphics2D g2d, Shape shape, ShadeType shadeType, Color shadeColor, int width, Shape clip,
                                   boolean round )
    {
        // Ignoring shade with width less than 2 
        if ( width <= 1 )
        {
            return;
        }

        // Applying clip
        Shape oldClip = clip != null ? intersectClip ( g2d, clip ) : subtractClip ( g2d, shape );

        // Saving composite
        Composite oldComposite = g2d.getComposite ();
        float currentComposite = 1f;
        if ( oldComposite instanceof AlphaComposite )
        {
            currentComposite = ( ( AlphaComposite ) oldComposite ).getAlpha ();
        }

        // Saving stroke
        Stroke oldStroke = g2d.getStroke ();

        // Drawing shade
        if ( shadeColor != null )
        {
            g2d.setPaint ( shadeColor );
        }
        if ( shadeType.equals ( ShadeType.simple ) )
        {
            // Drawing simple shade
            if ( StyleConstants.simpleShadeTransparency < 1f )
            {
                g2d.setComposite (
                        AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, StyleConstants.simpleShadeTransparency * currentComposite ) );
            }
            g2d.setStroke ( getStroke ( width * 2, round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT ) );
            g2d.draw ( shape );
        }
        else
        {
            // Drawing comples gradient shade
            width = width * 2;
            for ( int i = width; i >= 2; i -= 2 )
            {
                // float minTransp = 0.2f;
                // float maxTransp = 0.6f;
                // float opacity = minTransp + ( maxTransp - minTransp ) * ( 1 - ( i - 2 ) / ( width - 2 ) );
                float opacity = ( float ) ( width - i ) / ( width - 1 );
                g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, opacity * currentComposite ) );
                g2d.setStroke ( getStroke ( i, round ? BasicStroke.CAP_ROUND : BasicStroke.CAP_BUTT ) );
                g2d.draw ( shape );
            }
        }

        // Restoring initial grphics settings
        restoreStroke ( g2d, oldStroke );
        restoreComposite ( g2d, oldComposite );
        restoreClip ( g2d, oldClip );
    }

    /**
     * Strokes caching
     */

    private static Map<String, Stroke> cachedStrokes = new HashMap<String, Stroke> ();

    public static Stroke getStroke ( int width )
    {
        return getStroke ( width, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND );
    }

    public static Stroke getStroke ( int width, int cap )
    {
        return getStroke ( width, cap, BasicStroke.JOIN_ROUND );
    }

    public static Stroke getStroke ( int width, int cap, int join )
    {
        String key = width + "," + cap + "," + join;
        Stroke stroke = cachedStrokes.get ( key );
        if ( stroke == null )
        {
            stroke = new BasicStroke ( width, cap, join );
            cachedStrokes.put ( key, stroke );
        }
        return stroke;
    }

    /**
     * Draws web styled selection using shapes operations. This method is pretty slow and should not be used for multiply selections
     * presentantion
     */

    public static int halfButton = 4;
    public static int halfSelector = 2;
    public static int halfLine = 1;
    public static int shadeWidth = 2;

    public static void drawWebSelection ( Graphics2D g2d, Color color, int x, int y, int width, int height, boolean resizableLR,
                                          boolean resizableUD, boolean drawConnectors )
    {
        drawWebSelection ( g2d, color, new Rectangle ( x, y, width, height ), resizableLR, resizableUD, drawConnectors );
    }

    public static void drawWebSelection ( Graphics2D g2d, Color color, int x, int y, int width, int height, boolean resizableLR,
                                          boolean resizableUD, boolean drawConnectors, boolean drawSideControls )
    {
        drawWebSelection ( g2d, color, new Rectangle ( x, y, width, height ), resizableLR, resizableUD, drawConnectors, drawSideControls );
    }

    public static void drawWebSelection ( Graphics2D g2d, Color color, Rectangle selection, boolean resizableLR, boolean resizableUD,
                                          boolean drawConnectors )
    {
        drawWebSelection ( g2d, color, selection, resizableLR, resizableUD, drawConnectors, true );
    }

    public static void drawWebSelection ( Graphics2D g2d, Color color, Rectangle selection, boolean resizableLR, boolean resizableUD,
                                          boolean drawConnectors, boolean drawSideControls )
    {
        selection = GeometryUtils.validateRect ( selection );

        Object aa = setupAntialias ( g2d );

        Area buttonsShape = new Area ();

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
            Area selectionShape = new Area (
                    new RoundRectangle2D.Double ( selection.x - halfLine, selection.y - halfLine, selection.width + halfLine * 2,
                            selection.height + halfLine * 2, 5, 5 ) );
            selectionShape.subtract ( new Area (
                    new RoundRectangle2D.Double ( selection.x + halfLine, selection.y + halfLine, selection.width - halfLine * 2,
                            selection.height - halfLine * 2, 3, 3 ) ) );
            buttonsShape.add ( selectionShape );
        }

        // Shade
        drawShade ( g2d, buttonsShape, Color.GRAY, shadeWidth );

        // Border
        g2d.setPaint ( Color.GRAY );
        g2d.draw ( buttonsShape );

        // Background
        g2d.setPaint ( color );
        g2d.fill ( buttonsShape );

        restoreAntialias ( g2d, aa );
    }

    public static void drawWebSelector ( Graphics2D g2d, Color color, Rectangle selection, int selector )
    {
        selection = GeometryUtils.validateRect ( selection );

        Object aa = setupAntialias ( g2d );

        Ellipse2D buttonsShape;
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

        restoreAntialias ( g2d, aa );
    }

    /**
     * Draws web styled selection using predefined images set. This method is much faster than the one before but has less settings due to
     * the predefined graphics
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

    public static void drawWebIconedSelection ( Graphics2D g2d, Rectangle selection, boolean resizableLR, boolean resizableUD,
                                                boolean drawConnectors )
    {
        drawWebIconedSelection ( g2d, selection, resizableLR, resizableUD, drawConnectors, true );
    }

    public static void drawWebIconedSelection ( Graphics2D g2d, Rectangle selection, boolean resizableLR, boolean resizableUD,
                                                boolean drawConnectors, boolean drawSideControls )
    {
        selection = GeometryUtils.validateRect ( selection );

        // Calculating selection rect
        Rectangle rect = calculateIconedRect ( selection );

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

    public static void drawWebIconedSelector ( Graphics2D g2d, Rectangle selection, int selector )
    {
        selection = GeometryUtils.validateRect ( selection );

        // Calculating selector rect
        Rectangle rect = calculateIconedRect ( selection );

        // Drawing selector
        getSelectorIcon ( selector ).paintIcon ( g2d, rect );
    }

    public static void drawWebIconedGripper ( Graphics2D g2d, Point point )
    {
        drawWebIconedGripper ( g2d, point.x, point.y );
    }

    public static void drawWebIconedGripper ( Graphics2D g2d, int x, int y )
    {
        g2d.drawImage ( gripper.getImage (), x - gripper.getIconWidth () / 2, y - gripper.getIconHeight () / 2, null );
    }

    private static Rectangle calculateIconedRect ( Rectangle selection )
    {
        // Recalculating coordinates to iconed view
        return new Rectangle ( selection.x - halfButton - shadeWidth, selection.y - halfButton - shadeWidth,
                selection.width + halfButton * 2 + shadeWidth * 2, selection.height + halfButton * 2 + shadeWidth * 2 );
    }

    private static Map<Integer, NinePatchIcon> selectorCache = new HashMap<Integer, NinePatchIcon> ();

    private static NinePatchIcon getSelectorIcon ( int selector )
    {
        if ( selectorCache.containsKey ( selector ) )
        {
            return selectorCache.get ( selector );
        }
        else
        {
            NinePatchIcon npi = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/selector" + selector + ".9.png" ) );
            selectorCache.put ( selector, npi );
            return npi;
        }
    }

    /**
     * Draws etched shape with specified background colors
     */

    public static void drawEtchedShape ( Graphics2D g2d, BufferedImage topBg, BufferedImage bottomBg, Shape fullShape, Shape bevelShape )
    {
        Object aa = setupAntialias ( g2d );

        Rectangle bounds = fullShape.getBounds ();

        g2d.setPaint ( new TexturePaint ( topBg,
                new Rectangle ( bounds.getLocation (), new Dimension ( topBg.getWidth (), topBg.getHeight () ) ) ) );
        g2d.fill ( fullShape );

        Shape oldClip = g2d.getClip ();
        Area newClip = new Area ( oldClip );
        newClip.intersect ( new Area ( bevelShape ) );

        g2d.setClip ( newClip );
        g2d.setPaint ( new TexturePaint ( bottomBg,
                new Rectangle ( bounds.getLocation (), new Dimension ( bottomBg.getWidth (), bottomBg.getHeight () ) ) ) );
        g2d.fill ( bevelShape );

        drawShade ( g2d, bevelShape, Color.BLACK, 4 );

        g2d.setClip ( oldClip );

        g2d.setPaint ( Color.DARK_GRAY );
        g2d.draw ( bevelShape );

        restoreAntialias ( g2d, aa );
    }

    /**
     * Draw a string with a drop shadow. The light angle is assumed to be 0 degrees, (i.e., window is illuminated from top) and the shadow
     * size is 2, with a 1 pixel vertical displacement. The shadow is intended to be subtle to be usable in as many text components as
     * possible. The shadow is generated with multiple calls to draw string. This method paints the text on coordinates 0, 1. If text
     * should
     * be painted elsewhere, a transform should be applied to the graphics before passing it.
     */

    public static void paintTextShadow ( Graphics2D g2d, String s )
    {
        paintTextShadow ( g2d, s, Color.LIGHT_GRAY );
    }

    /**
     * Draw a string with a drop shadow. The light angle is assumed to be 0 degrees, (i.e., window is illuminated from top) and the shadow
     * size is 2, with a 1 pixel vertical displacement. The shadow is intended to be subtle to be usable in as many text components as
     * possible. The shadow is generated with multiple calls to draw string. This method paints the text on coordinates 0, 1. If text
     * should
     * be painted elsewhere, a transform should be applied to the graphics before passing it.
     */

    public static void paintTextShadow ( Graphics2D g2d, String s, Color c )
    {
        paintTextEffect ( g2d, s, ColorUtils.removeAlpha ( c ), TEXT_SHADOW_SIZE, -TEXT_SHADOW_SIZE, 1 - TEXT_SHADOW_SIZE, true );
    }

    /**
     * Draw a string with a glow effect. Glow differs from a drop shadow in that it isn't offset in any direction (i.e., not affected by
     * "lighting conditions").
     */

    public static void paintTextGlow ( Graphics2D g2d, String s, Color glow )
    {
        paintTextEffect ( g2d, s, ColorUtils.removeAlpha ( glow ), TEXT_SHADOW_SIZE, -TEXT_SHADOW_SIZE, -TEXT_SHADOW_SIZE, false );
    }

    /**
     * Draw a string with a blur or shadow effect. The light angle is assumed to be 0 degrees, (i.e., window is illuminated from top). The
     * effect is intended to be subtle to be usable in as many text components as possible. The effect is generated with multiple calls to
     * draw string. This method paints the text on coordinates <code>tx</code>, <code>ty</code>. If text should be painted elsewhere, a
     * transform should be applied to the graphics before passing it.
     */

    private static final int TEXT_SHADOW_SIZE = 2;

    public static void paintTextEffect ( Graphics2D g2d, String s, Color c, int size, double tx, double ty, boolean isShadow )
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
        g2d.setColor ( c );

        g2d.translate ( tx, ty );

        // If the effect is a shadow it looks better to stop painting a bit earlier - shadow will look softer
        int maxSize = isShadow ? size - 1 : size;

        for ( int i = -size; i <= maxSize; i++ )
        {
            for ( int j = -size; j <= maxSize; j++ )
            {
                double distance = i * i + j * j;
                float alpha = opacity;
                if ( distance > 0.0d )
                {
                    alpha = ( float ) ( 1.0f / ( ( distance * size ) * opacity ) );
                }
                alpha *= preAlpha;
                if ( alpha > 1.0f )
                {
                    alpha = 1.0f;
                }
                g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, alpha ) );
                g2d.drawString ( s, i + size, j + size );
            }
        }

        // Restore graphics
        g2d.translate ( -tx, -ty );
        g2d.setComposite ( oldComposite );
        g2d.setColor ( oldColor );

        g2d.drawString ( s, 0, 0 );

        //        final Color oldColor = g2d.getColor ();
        //        g2d.setColor ( c );
        //        g2d.drawString ( s, 1, 1 );
        //
        //        g2d.setColor ( oldColor );
        //        g2d.drawString ( s, 0, 0 );
    }

    /**
     * Draws dashed rectangle
     */

    public static void drawDashedRect ( Graphics2D g2d, int x1, int y1, int x2, int y2, int stripeLength, int spaceLength )
    {
        drawDashedRect ( g2d, x1, y1, x2, y2, stripeLength, spaceLength, 0.0f );
    }

    public static void drawDashedRect ( Graphics2D g2d, int x1, int y1, int x2, int y2, int stripeLength, int spaceLength,
                                        float stripeStart )
    {
        if ( x2 < x1 || y2 < y1 )
        {
            return;
        }

        final float[] dash = { stripeLength, spaceLength };
        final BasicStroke stroke = new BasicStroke ( 1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, dash, stripeStart );


        Stroke oldStroke = setupStroke ( g2d, stroke );
        g2d.drawRect ( x1, y1, x2 - x1, y2 - y1 );
        restoreStroke ( g2d, oldStroke );
    }

    /**
     * Returns custom rounded rectangle shape
     */

    public static GeneralPath createRoundedRectShape ( int x, int y, int w, int h, int arcW, int arcH )
    {
        GeneralPath gp = new GeneralPath ( GeneralPath.WIND_EVEN_ODD );
        gp.moveTo ( x, y + arcH );
        gp.quadTo ( x, y, x + arcW, y );
        gp.lineTo ( x + w - arcW, y );
        gp.quadTo ( x + w, y, x + w, y + arcH );
        gp.lineTo ( x + w, y + h - arcH );
        gp.quadTo ( x + w, y + h, x + w - arcW, y + h );
        gp.lineTo ( x + arcW, y + h );
        gp.quadTo ( x, y + h, x, y + h - arcH );
        gp.closePath ();
        return gp;
    }

    /**
     * Returns shear to center text
     */

    public static Point getTextCenterShear ( FontMetrics fm, String text )
    {
        return new Point ( getTextCenterShearX ( fm, text ), getTextCenterShearY ( fm ) );
    }

    public static int getTextCenterShearX ( FontMetrics fm, String text )
    {
        return -fm.stringWidth ( text ) / 2;
    }

    public static int getTextCenterShearY ( FontMetrics fm )
    {
        return ( fm.getAscent () - fm.getLeading () - fm.getDescent () ) / 2;
    }

    /**
     * Returns border methods for the specified component or null if custom WebLaF border is not supported.
     *
     * @param component component to process
     * @return border methods
     */
    public static BorderMethods getBorderMethods ( final Component component )
    {
        if ( component instanceof BorderMethods )
        {
            return ( BorderMethods ) component;
        }
        else
        {
            final Object ui = ReflectUtils.callMethodSafely ( component, "getUI" );
            if ( ui instanceof BorderMethods )
            {
                return ( BorderMethods ) ui;
            }
        }
        return null;
    }

    /**
     * Returns bounds for editor display atop of the label.
     *
     * @param label  edited label
     * @param editor label editor field
     * @return bounds for editor display atop of the label
     */
    public static Rectangle getLabelEditorBounds ( final WebLabel label, final WebTextField editor )
    {
        editor.setFieldMargin ( 0, label.getIconTextGap (), 0, 0 );

        // Bounds
        final Rectangle bounds = new Rectangle ( 0, 0, label.getWidth (), label.getHeight () );

        // Label settings
        final Insets lm = label.getInsets ();
        bounds.x += lm.left;
        bounds.y += lm.top;
        bounds.width -= lm.left + lm.right;
        bounds.height -= lm.top + lm.bottom;

        // Field settings
        final Insets fm = editor.getMargin ();
        final int dm = 1 + editor.getShadeWidth ();
        bounds.x -= fm.left + dm;
        bounds.y -= fm.top + dm;
        bounds.width += fm.left + fm.right + dm * 2;
        bounds.height += fm.top + fm.bottom + dm * 2;

        // Additional pixel for field size
        bounds.width += 1;

        return bounds;
    }
}