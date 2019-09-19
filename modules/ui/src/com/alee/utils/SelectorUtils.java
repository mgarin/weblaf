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

import com.alee.utils.ninepatch.NinePatchIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikle Garin
 * @deprecated This class will be reworked into a seaprate feature
 */
@Deprecated
public final class SelectorUtils
{
    /**
     * Draws web styled selection using shapes operations.
     * This method is pretty slow and should not be used for multiple selections presentation.
     */

    /**
     * Painting constants.
     */
    public static int halfButton = 4;
    public static int halfSelector = 2;
    public static int halfLine = 1;
    public static int shadeWidth = 2;

    /**
     * Private constructor to avoid instantiation.
     */
    private SelectorUtils ()
    {
        throw new UtilityException ( "Utility classes are not meant to be instantiated" );
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
        selection = validateRect ( selection );

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
        selection = validateRect ( selection );

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

    private static final Map<String, Icon> selectionIconsCache = new HashMap<String, Icon> ( 10 );

    private static NinePatchIcon getSelectionIcon ( final String name )
    {
        final NinePatchIcon icon;
        if ( selectionIconsCache.containsKey ( name ) )
        {
            icon = ( NinePatchIcon ) selectionIconsCache.get ( name );
        }
        else
        {
            icon = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/" + name + ".9.png" ) );
            selectionIconsCache.put ( name, icon );
        }
        return icon;
    }

    private static ImageIcon getGripperIcon ()
    {
        final ImageIcon icon;
        final String key = "gripper";
        if ( selectionIconsCache.containsKey ( key ) )
        {
            icon = ( ImageIcon ) selectionIconsCache.get ( key );
        }
        else
        {
            icon = new ImageIcon ( LafUtils.class.getResource ( "icons/selection/" + key + ".png" ) );
            selectionIconsCache.put ( key, icon );
        }
        return icon;
    }

    private static NinePatchIcon getSelectorIcon ( final int selector )
    {
        final NinePatchIcon icon;
        final String key = Integer.toString ( selector );
        if ( selectionIconsCache.containsKey ( key ) )
        {
            icon = ( NinePatchIcon ) selectionIconsCache.get ( key );
        }
        else
        {
            icon = new NinePatchIcon ( LafUtils.class.getResource ( "icons/selection/selector" + key + ".9.png" ) );
            selectionIconsCache.put ( key, icon );
        }
        return icon;
    }

    public static void drawWebIconedSelection ( final Graphics2D g2d, final Rectangle selection, final boolean resizableLR,
                                                final boolean resizableUD, final boolean drawConnectors )
    {
        drawWebIconedSelection ( g2d, selection, resizableLR, resizableUD, drawConnectors, true );
    }

    public static void drawWebIconedSelection ( final Graphics2D g2d, Rectangle selection, final boolean resizableLR,
                                                final boolean resizableUD, final boolean drawConnectors, final boolean drawSideControls )
    {
        selection = validateRect ( selection );

        // Calculating selection rect
        final Rectangle rect = calculateIconedRect ( selection );

        // Drawing selection
        if ( drawConnectors )
        {
            if ( !resizableLR && !resizableUD )
            {
                getSelectionIcon ( "conn" ).paintIcon ( g2d, rect );
            }
            else if ( resizableLR && !resizableUD && drawSideControls )
            {
                getSelectionIcon ( "lr_conn" ).paintIcon ( g2d, rect );
            }
            else if ( !resizableLR && resizableUD && drawSideControls )
            {
                getSelectionIcon ( "ud_conn" ).paintIcon ( g2d, rect );
            }
            else if ( resizableLR && resizableUD )
            {
                if ( drawSideControls )
                {
                    getSelectionIcon ( "full_conn" ).paintIcon ( g2d, rect );
                }
                else
                {
                    getSelectionIcon ( "corners_conn" ).paintIcon ( g2d, rect );
                }
            }
        }
        else
        {
            if ( resizableLR && !resizableUD && drawSideControls )
            {
                getSelectionIcon ( "lr" ).paintIcon ( g2d, rect );
            }
            else if ( !resizableLR && resizableUD && drawSideControls )
            {
                getSelectionIcon ( "ud" ).paintIcon ( g2d, rect );
            }
            else if ( resizableLR && resizableUD )
            {
                if ( drawSideControls )
                {
                    getSelectionIcon ( "full" ).paintIcon ( g2d, rect );
                }
                else
                {
                    getSelectionIcon ( "corners" ).paintIcon ( g2d, rect );
                }
            }
        }
    }

    public static void drawWebIconedSelector ( final Graphics2D g2d, Rectangle selection, final int selector )
    {
        selection = validateRect ( selection );

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
        final ImageIcon gripper = getGripperIcon ();
        g2d.drawImage ( gripper.getImage (), x - gripper.getIconWidth () / 2, y - gripper.getIconHeight () / 2, null );
    }

    private static Rectangle calculateIconedRect ( final Rectangle selection )
    {
        // Recalculating coordinates to iconed view
        return new Rectangle ( selection.x - halfButton - shadeWidth, selection.y - halfButton - shadeWidth,
                selection.width + halfButton * 2 + shadeWidth * 2, selection.height + halfButton * 2 + shadeWidth * 2 );
    }

    /**
     * Returns valid rectangle with non-negative width and height.
     *
     * @param rect rectangle to validate
     * @return valid rectangle with non-negative width and height
     */
    private static Rectangle validateRect ( final Rectangle rect )
    {
        final Rectangle result;
        if ( rect.width >= 0 && rect.height >= 0 )
        {
            result = rect;
        }
        else
        {
            int x = rect.x;
            final int width = Math.abs ( rect.width );
            if ( rect.width < 0 )
            {
                x = x - width;
            }
            int y = rect.y;
            final int height = Math.abs ( rect.height );
            if ( rect.height < 0 )
            {
                y = y - height;
            }
            result = new Rectangle ( x, y, width, height );
        }
        return result;
    }
}