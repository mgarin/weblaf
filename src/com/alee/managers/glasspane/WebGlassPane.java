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

package com.alee.managers.glasspane;

import com.alee.laf.panel.WebPanel;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * This is a specified WebLaF glass pane that is used to display custom tooltips, highlights, animations, transitions and other custom
 * stuff atop of all components visible in window/applet. It can also be used to bring your own custom features.
 *
 * @author Mikle Garin
 * @see GlassPaneManager
 */

public class WebGlassPane extends WebPanel
{
    /**
     * ID prefix.
     */
    private static final String ID_PREFIX = "WGP";

    /**
     * WebGlassPane ID.
     */
    private String id = null;

    /**
     * JRootPane to which WebGlassPane is attached.
     */
    private JRootPane rootPane = null;

    /**
     * WebGlassPane mouse hit shape.
     */
    private Shape hitShape = null;

    /**
     * Custom painted image.
     */
    private BufferedImage paintedImage = null;

    /**
     * Custom painted image location.
     */
    private Point imageLocation = null;

    /**
     * Custom painted image opacity.
     */
    private int imageOpacity = 0;

    /**
     * Highlighted components.
     */
    private List<Component> highlightedComponents = new ArrayList<Component> ();

    /**
     * Highlight painting base.
     */
    private Component highlightBase = WebGlassPane.this;

    /**
     * Highlight spacing between the component bounds and highlight gray area.
     */
    private int highlightSpacing = 3;

    /**
     * Constructs WebGlassPane for the specified JRootPane.
     *
     * @param rootPane JRootPane to process
     */
    public WebGlassPane ( JRootPane rootPane )
    {
        super ();

        this.rootPane = rootPane;

        setOpaque ( false );
        setFocusable ( false );
        setLayout ( null );
    }

    /**
     * Returns WebGlassPane ID.
     *
     * @return WebGlassPane ID
     */
    public String getId ()
    {
        if ( id == null )
        {
            id = TextUtils.generateId ( ID_PREFIX );
        }
        return id;
    }

    /**
     * Returns JRootPane to which this WebGlassPane is attached.
     *
     * @return JRootPane to which this WebGlassPane is attached
     */
    public JRootPane getRootPane ()
    {
        return rootPane;
    }

    /**
     * Displays single component on WebGlassPane
     */

    public void showComponent ( JComponent component )
    {
        // Updating added component and its childs orientation
        SwingUtils.copyOrientation ( WebGlassPane.this, component );

        // Adding with 0 index to put component on top of all existing
        WebGlassPane.this.add ( component, 0 );
        WebGlassPane.this.revalidate ();
    }

    /**
     * Returns whether WebGlassPane should absorb mouse event from the specified point or not.
     * Custom hit shape is used to allow mouse events passthrough where it is required.
     *
     * @param x mouse X coordinate
     * @param y mouse Y coordinate
     * @return true if WebGlassPane should absorb mouse event from the specified point, false otherwise
     */
    public boolean contains ( int x, int y )
    {
        return hitShape != null && hitShape.contains ( x, y );
    }

    /**
     * Returns custom hit shape that is used to allow mouse events passthrough where it is required.
     *
     * @return custom hit shape that is used to allow mouse events passthrough where it is required
     */
    public Shape getHitShape ()
    {
        return hitShape;
    }

    /**
     * Sets custom hit shape that is used to allow mouse events passthrough where it is required.
     *
     * @param hitShape custom hit shape that is used to allow mouse events passthrough where it is required
     */
    public void setHitShape ( Shape hitShape )
    {
        this.hitShape = hitShape;
    }

    /**
     * Returns painted image opacity.
     *
     * @return painted image opacity
     */
    public int getImageOpacity ()
    {
        return imageOpacity;
    }

    /**
     * Returns painted image location.
     *
     * @return painted image location
     */
    public Point getImageLocation ()
    {
        return imageLocation;
    }

    /**
     * Returns painted image.
     *
     * @return painted image
     */
    public BufferedImage getPaintedImage ()
    {
        return paintedImage;
    }

    /**
     * Sets painted image at the specified location.
     *
     * @param image    image to paint
     * @param location image location
     */
    public void setPaintedImage ( BufferedImage image, Point location )
    {
        setPaintedImage ( image, location, 100 );
    }

    /**
     * Sets painted image at the specified location with the specified opacity.
     *
     * @param image    image to paint
     * @param location image location
     * @param opacity  image opacity
     */
    public void setPaintedImage ( BufferedImage image, Point location, int opacity )
    {
        Rectangle oldRect = getPaintedImageBounds ();

        this.paintedImage = image;
        this.imageLocation = location;
        this.imageOpacity = opacity;

        Rectangle repaintRect = null;
        if ( this.imageOpacity != 0 && this.paintedImage != null && this.imageLocation != null )
        {
            repaintRect = getPaintedImageBounds ();
            if ( oldRect != null )
            {
                repaintRect = new Rectangle ( Math.max ( 0, Math.min ( oldRect.x, repaintRect.x ) ),
                        Math.max ( 0, Math.min ( oldRect.y, repaintRect.y ) ),
                        Math.max ( oldRect.x + oldRect.width, repaintRect.x + repaintRect.width ),
                        Math.max ( oldRect.y + oldRect.height, repaintRect.y + repaintRect.height ) );
            }
        }

        final Rectangle finalRepaintRect = repaintRect != null ? repaintRect : oldRect;
        SwingUtilities.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                if ( finalRepaintRect != null )
                {
                    repaint ( finalRepaintRect );
                }
                else
                {
                    repaint ();
                }
            }
        } );
    }

    /**
     * Removes painted image.
     */
    public void clearPaintedImage ()
    {
        final Rectangle oldRect = getPaintedImageBounds ();

        this.imageOpacity = 0;
        this.paintedImage = null;
        this.imageLocation = null;

        if ( oldRect != null )
        {
            SwingUtilities.invokeLater ( new Runnable ()
            {
                public void run ()
                {
                    repaint ( oldRect );
                }
            } );
        }
    }

    /**
     * Returns painted image bounds.
     *
     * @return painted image bounds
     */
    public Rectangle getPaintedImageBounds ()
    {
        Rectangle oldRect = null;
        if ( this.imageOpacity != 0 && this.paintedImage != null && this.imageLocation != null )
        {
            oldRect = new Rectangle ( this.imageLocation.x, this.imageLocation.y, this.paintedImage.getWidth (),
                    this.paintedImage.getHeight () );
        }
        return oldRect;
    }

    /**
     * Highlights specified components.
     *
     * @param components components to highlight
     */
    public void addHighlightedComponents ( Component... components )
    {
        for ( Component component : components )
        {
            if ( !highlightedComponents.contains ( component ) )
            {
                highlightedComponents.add ( component );
            }
        }
        repaint ();
    }

    /**
     * Highlights specified components.
     *
     * @param components components to highlight
     */
    public void addHighlightedComponents ( List<Component> components )
    {
        for ( Component component : components )
        {
            if ( !highlightedComponents.contains ( component ) )
            {
                highlightedComponents.add ( component );
            }
        }
        repaint ();
    }

    /**
     * Removes highlight from the specified components.
     *
     * @param components components to remove highlight from
     */
    public void removeHighlightedComponents ( Component... components )
    {
        for ( Component component : components )
        {
            highlightedComponents.remove ( component );
        }
        repaint ();
    }

    /**
     * Removes highlight from the specified components.
     *
     * @param components components to remove highlight from
     */
    public void removeHighlightedComponents ( List<Component> components )
    {
        for ( Component component : components )
        {
            highlightedComponents.remove ( component );
        }
        repaint ();
    }

    /**
     * Remove all highlights.
     */
    public void clearHighlights ()
    {
        highlightedComponents.clear ();
        repaint ();
    }

    /**
     * Returns highlight painting base.
     *
     * @return highlight painting base
     */
    public Component getHighlightBase ()
    {
        return highlightBase;
    }

    /**
     * Sets highlight painting base.
     *
     * @param highlightBase highlight painting base
     */
    public void setHighlightBase ( Component highlightBase )
    {
        this.highlightBase = highlightBase;
    }

    /**
     * Returns highlight spacing.
     *
     * @return highlight spacing
     */
    public int getHighlightSpacing ()
    {
        return highlightSpacing;
    }

    /**
     * Sets highlight spacing.
     *
     * @param spacing highlight spacing
     */
    public void setHighlightSpacing ( int spacing )
    {
        this.highlightSpacing = spacing;
    }

    // todo Add this functionality
    //    public void displayFocusChange ( Component oldComponent, Component newComponent )
    //    {
    //        Rectangle oldBounds = SwingUtils.getRelativeBounds ( oldComponent, this );
    //        Rectangle newBounds = SwingUtils.getRelativeBounds ( newComponent, this );
    //
    //    }

    /**
     * Paints WebGlassPane content.
     *
     * @param g graphics context.
     */
    protected void paintComponent ( Graphics g )
    {
        super.paintComponent ( g );

        Graphics2D g2d = ( Graphics2D ) g;
        Object aa = LafUtils.setupAntialias ( g2d );

        if ( highlightedComponents.size () > 0 )
        {
            Rectangle baseBounds = SwingUtils.getRelativeBounds ( highlightBase, WebGlassPane.this );
            Area area = new Area ( new Rectangle ( baseBounds.x - 1, baseBounds.y - 1, baseBounds.width + 1, baseBounds.height + 1 ) );
            for ( Component component : highlightedComponents )
            {
                if ( component.isShowing () )
                {
                    Rectangle bounds = SwingUtils.getRelativeBounds ( component, WebGlassPane.this );
                    RoundRectangle2D.Double shape = new RoundRectangle2D.Double ( bounds.x - highlightSpacing, bounds.y - highlightSpacing,
                            bounds.width + highlightSpacing * 2 - 1, bounds.height + highlightSpacing * 2 - 1, 8, 8 );
                    area.subtract ( new Area ( shape ) );
                }
            }

            // Fill
            g2d.setPaint ( new Color ( 128, 128, 128, 128 ) );
            g2d.fill ( area );

            // Border
            g2d.setStroke ( new BasicStroke ( 1.5f ) );
            g2d.setPaint ( Color.GRAY );
            g2d.draw ( area );
        }

        if ( imageOpacity != 0 && paintedImage != null && imageLocation != null )
        {
            Composite c = g2d.getComposite ();
            if ( imageOpacity != 100 )
            {
                g2d.setComposite ( AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, ( float ) imageOpacity / 100 ) );
            }

            g2d.drawImage ( paintedImage, imageLocation.x, imageLocation.y, null );

            if ( imageOpacity != 100 )
            {
                g2d.setComposite ( c );
            }
        }

        LafUtils.restoreAntialias ( g2d, aa );
    }
}