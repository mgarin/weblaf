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

package com.alee.laf.splitpane;

import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.*;
import com.alee.painter.DefaultPainter;
import com.alee.painter.Painter;
import com.alee.painter.PainterSupport;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.ImageUtils;
import com.alee.utils.swing.DataRunnable;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicSplitPaneDivider;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for JSplitPane component.
 *
 * @author Mikle Garin
 * @author Alexandr Zernov
 */

public class WebSplitPaneUI extends BasicSplitPaneUI implements Styleable, ShapeProvider, MarginSupport, PaddingSupport
{
    /**
     * Component painter.
     */
    @DefaultPainter ( SplitPanePainter.class )
    protected ISplitPanePainter painter;

    /**
     * Style settings.
     */
    protected Color dragDividerColor;
    protected Color dividerBorderColor;
    protected boolean drawDividerBorder;

    /**
     * Runtime variables.
     */
    protected Insets margin = null;
    protected Insets padding = null;

    /**
     * SplitPane listeners.
     */
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the WebSplitPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebSplitPaneUI
     */
    @SuppressWarnings ( "UnusedParameters" )
    public static ComponentUI createUI ( final JComponent c )
    {
        return new WebSplitPaneUI ();
    }

    /**
     * Installs UI in the specified component.
     *
     * @param c component for this UI
     */
    @Override
    public void installUI ( final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( splitPane );

        // Default settings
        LookAndFeel.installProperty ( splitPane, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        splitPane.setDividerSize ( 6 );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( splitPane );

        super.uninstallUI ( c );
    }

    @Override
    public StyleId getStyleId ()
    {
        return StyleManager.getStyleId ( splitPane );
    }

    @Override
    public StyleId setStyleId ( final StyleId id )
    {
        return StyleManager.setStyleId ( splitPane, id );
    }

    @Override
    public Shape provideShape ()
    {
        return PainterSupport.getShape ( splitPane, painter );
    }

    @Override
    public Insets getMargin ()
    {
        return margin;
    }

    @Override
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        PainterSupport.updateBorder ( getPainter () );
    }

    @Override
    public Insets getPadding ()
    {
        return padding;
    }

    @Override
    public void setPadding ( final Insets padding )
    {
        this.padding = padding;
        PainterSupport.updateBorder ( getPainter () );
    }

    /**
     * Returns split pane painter.
     *
     * @return split pane painter
     */
    public Painter getPainter ()
    {
        return PainterSupport.getAdaptedPainter ( painter );
    }

    /**
     * Sets split pane painter.
     * Pass null to remove split pane painter.
     *
     * @param painter new split pane painter
     */
    public void setPainter ( final Painter painter )
    {
        PainterSupport.setPainter ( splitPane, new DataRunnable<ISplitPanePainter> ()
        {
            @Override
            public void run ( final ISplitPanePainter newPainter )
            {
                WebSplitPaneUI.this.painter = newPainter;
            }
        }, this.painter, painter, ISplitPanePainter.class, AdaptiveSplitPanePainter.class );
    }

    /**
     * Returns dragged divider color.
     *
     * @return dragged divider color
     */
    public Color getDragDividerColor ()
    {
        return dragDividerColor;
    }

    /**
     * Sets dragged divider color.
     *
     * @param dragDividerColor dragged divider color
     */
    public void setDragDividerColor ( final Color dragDividerColor )
    {
        this.dragDividerColor = dragDividerColor;
    }

    /**
     * Returns whether divider border is painted or not.
     *
     * @return true if divider border is painted, false otherwise
     */
    public boolean isDrawDividerBorder ()
    {
        return drawDividerBorder;
    }

    /**
     * Sets whether divider border is painted or not.
     *
     * @param draw whether divider border is painted or not
     */
    public void setDrawDividerBorder ( final boolean draw )
    {
        this.drawDividerBorder = draw;
    }

    /**
     * Returns divider border color.
     *
     * @return divider border color
     */
    public Color getDividerBorderColor ()
    {
        return dividerBorderColor;
    }

    /**
     * Sets divider border color.
     *
     * @param color new divider border color
     */
    public void setDividerBorderColor ( final Color color )
    {
        this.dividerBorderColor = color;
    }

    @Override
    public BasicSplitPaneDivider createDefaultDivider ()
    {
        return new BasicSplitPaneDivider ( this )
        {
            protected final Border border = BorderFactory.createEmptyBorder ( 0, 0, 0, 0 );
            protected final Color color = new Color ( 158, 158, 158 );
            protected final Color[] gradient = new Color[]{ StyleConstants.transparent, color, color, StyleConstants.transparent };

            @Override
            public Border getBorder ()
            {
                return border;
            }

            @Override
            protected JButton createLeftOneTouchButton ()
            {
                final boolean hor = orientation == JSplitPane.HORIZONTAL_SPLIT;
                final ImageIcon icon = getOneTouchIcon ( true, hor );
                final WebButton iconWebButton = new WebButton ( StyleId.splitpaneOneTouchLeftButton.at ( splitPane ), icon );
                iconWebButton.setCursor ( Cursor.getDefaultCursor () );
                iconWebButton.setPreferredSize ( getOneTouchButtonSize ( hor ) );
                return iconWebButton;
            }

            @Override
            protected JButton createRightOneTouchButton ()
            {
                final boolean hor = orientation == JSplitPane.HORIZONTAL_SPLIT;
                final ImageIcon icon = getOneTouchIcon ( false, hor );
                final WebButton iconWebButton = new WebButton ( StyleId.splitpaneOneTouchRightButton.at ( splitPane ), icon );
                iconWebButton.setCursor ( Cursor.getDefaultCursor () );
                iconWebButton.setPreferredSize ( getOneTouchButtonSize ( hor ) );
                return iconWebButton;
            }

            /**
             * todo Replace with paintComponent?
             */
            @Override
            public void paint ( final Graphics g )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = GraphicsUtils.setupAntialias ( g2d );

                if ( orientation == JSplitPane.HORIZONTAL_SPLIT )
                {
                    final int startY = getHeight () / 2 - 35;
                    final int endY = getHeight () / 2 + 35;
                    g2d.setPaint ( new LinearGradientPaint ( 0, startY, 0, endY, new float[]{ 0f, 0.25f, 0.75f, 1f }, gradient ) );
                    for ( int i = startY; i < endY; i += 5 )
                    {
                        g2d.fillRect ( getWidth () / 2 - 1, i - 1, 2, 2 );
                    }

                    if ( drawDividerBorder )
                    {
                        g2d.setPaint ( dividerBorderColor );
                        g2d.drawLine ( 0, 0, 0, getHeight () - 1 );
                        g2d.drawLine ( getWidth () - 1, 0, getWidth () - 1, getHeight () - 1 );
                    }
                }
                else
                {
                    final int startX = getWidth () / 2 - 35;
                    final int endX = getWidth () / 2 + 35;
                    g2d.setPaint ( new LinearGradientPaint ( startX, 0, endX, 0, new float[]{ 0f, 0.25f, 0.75f, 1f }, gradient ) );
                    for ( int i = startX; i < endX; i += 5 )
                    {
                        g2d.fillRect ( i - 1, getHeight () / 2 - 1, 2, 2 );
                    }

                    if ( drawDividerBorder )
                    {
                        g2d.setPaint ( dividerBorderColor );
                        g2d.drawLine ( 0, 0, getWidth () - 1, 0 );
                        g2d.drawLine ( 0, getHeight () - 1, getWidth () - 1, getHeight () - 1 );
                    }
                }

                super.paint ( g );

                GraphicsUtils.restoreAntialias ( g2d, aa );
            }

            /**
             * Property change event, presumably from the JSplitPane, will message
             * updateOrientation if necessary.
             */
            @Override
            public void propertyChange ( final PropertyChangeEvent e )
            {
                super.propertyChange ( e );

                // Listening to split orientation changes
                if ( e.getSource () == splitPane && e.getPropertyName ().equals ( JSplitPane.ORIENTATION_PROPERTY ) )
                {
                    // Updating one-touch-button icons according to new orientation
                    final boolean hor = orientation == JSplitPane.HORIZONTAL_SPLIT;
                    if ( leftButton != null )
                    {
                        leftButton.setIcon ( getOneTouchIcon ( true, hor ) );
                        leftButton.setPreferredSize ( getOneTouchButtonSize ( hor ) );
                    }
                    if ( rightButton != null )
                    {
                        rightButton.setIcon ( getOneTouchIcon ( false, hor ) );
                        rightButton.setPreferredSize ( getOneTouchButtonSize ( hor ) );
                    }
                }
            }
        };
    }

    /**
     * Returns cached one-touch-button icon.
     *
     * @param leading    whether it should be leading button icon or not
     * @param horizontal whether split is horizontal or not
     * @return cached one-touch-button icon
     */
    protected ImageIcon getOneTouchIcon ( final boolean leading, final boolean horizontal )
    {
        final String name = horizontal ? leading ? "left" : "right" : leading ? "up" : "down";
        return ImageUtils.getImageIcon ( WebSplitPaneUI.class.getResource ( "icons/" + name + ".png" ), true );
    }

    /**
     * Returns one-touch-button size.
     *
     * @param horizontal whether split is horizontal or not
     * @return one-touch-button size
     */
    protected Dimension getOneTouchButtonSize ( final boolean horizontal )
    {
        return new Dimension ( horizontal ? 6 : 7, horizontal ? 7 : 6 );
    }

    @Override
    protected Component createDefaultNonContinuousLayoutDivider ()
    {
        return new Canvas ()
        {
            @Override
            public void paint ( final Graphics g )
            {
                if ( !isContinuousLayout () && getLastDragLocation () != -1 )
                {
                    final Dimension size = splitPane.getSize ();
                    g.setColor ( dragDividerColor );
                    if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
                    {
                        g.fillRect ( 0, 0, dividerSize - 1, size.height - 1 );
                    }
                    else
                    {
                        g.fillRect ( 0, 0, size.width - 1, dividerSize - 1 );
                    }
                }
            }
        };
    }

    @Override
    public void finishedPaintingChildren ( final JSplitPane jc, final Graphics g )
    {
        if ( jc == splitPane && getLastDragLocation () != -1 && !isContinuousLayout () &&
                !draggingHW )
        {
            final Dimension size = splitPane.getSize ();

            g.setColor ( dragDividerColor );
            if ( getOrientation () == JSplitPane.HORIZONTAL_SPLIT )
            {
                g.fillRect ( getLastDragLocation (), 0, dividerSize - 1, size.height - 1 );
            }
            else
            {
                g.fillRect ( 0, getLastDragLocation (), size.width - 1, dividerSize - 1 );
            }
        }
    }

    @Override
    public void paint ( final Graphics g, final JComponent c )
    {
        if ( painter != null )
        {
            // Call superclass to set internal flags
            // It doesn't paint anything so we don't need to worry about that
            super.paint ( g, c );

            // Painting split pane
            painter.paint ( ( Graphics2D ) g, Bounds.component.of ( c ), c, this );
        }
    }

    @Override
    public Dimension getPreferredSize ( final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c, super.getPreferredSize ( c ), painter );
    }
}