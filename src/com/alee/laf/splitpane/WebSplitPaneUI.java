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

import com.alee.laf.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.utils.LafUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.BorderMethods;

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
 */

public class WebSplitPaneUI extends BasicSplitPaneUI implements BorderMethods
{
    /**
     * Style settings.
     */
    private Insets margin = WebSplitPaneStyle.margin;
    private Color dragDividerColor = WebSplitPaneStyle.dragDividerColor;

    /**
     * SplitPane listeners.
     */
    private PropertyChangeListener propertyChangeListener;

    /**
     * Returns an instance of the WebSplitPaneUI for the specified component.
     * This tricky method is used by UIManager to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the WebSplitPaneUI
     */
    @SuppressWarnings ("UnusedParameters")
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

        // Default settings
        SwingUtils.setOrientation ( splitPane );
        LookAndFeel.installProperty ( splitPane, WebLookAndFeel.OPAQUE_PROPERTY, Boolean.FALSE );
        splitPane.setBorder ( LafUtils.createWebBorder ( 0 ) );
        splitPane.setDividerSize ( 6 );

        // Updating border
        updateBorder ();

        // Orientation change listener
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        splitPane.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );
    }

    /**
     * Uninstalls UI from the specified component.
     *
     * @param c component with this UI
     */
    @Override
    public void uninstallUI ( final JComponent c )
    {
        splitPane.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, propertyChangeListener );

        super.uninstallUI ( c );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateBorder ()
    {
        if ( splitPane != null )
        {
            // Preserve old borders
            if ( SwingUtils.isPreserveBorders ( splitPane ) )
            {
                return;
            }

            // Actual margin
            final boolean ltr = splitPane.getComponentOrientation ().isLeftToRight ();
            final Insets m = new Insets ( margin.top, ltr ? margin.left : margin.right, margin.bottom, ltr ? margin.right : margin.left );

            // Installing border
            splitPane.setBorder ( LafUtils.createWebBorder ( m ) );
        }
    }

    /**
     * Returns component margin.
     *
     * @return component margin
     */
    public Insets getMargin ()
    {
        return margin;
    }

    /**
     * Sets component margin.
     *
     * @param margin component margin
     */
    public void setMargin ( final Insets margin )
    {
        this.margin = margin;
        updateBorder ();
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
     * {@inheritDoc}
     */
    @Override
    public BasicSplitPaneDivider createDefaultDivider ()
    {
        return new BasicSplitPaneDivider ( this )
        {
            private final Border border = BorderFactory.createEmptyBorder ( 0, 0, 0, 0 );

            private final Color color = new Color ( 158, 158, 158 );
            private final Color[] gradient = new Color[]{ StyleConstants.transparent, color, color, StyleConstants.transparent };

            @Override
            public Border getBorder ()
            {
                return border;
            }

            @Override
            protected JButton createLeftOneTouchButton ()
            {
                final WebButton iconWebButton = WebButton.createIconWebButton ( new ImageIcon ( WebSplitPaneUI.class
                        .getResource ( orientation == JSplitPane.HORIZONTAL_SPLIT ? "icons/left.png" : "icons/up.png" ) ), 0, 0, 0, false,
                        true, false );
                iconWebButton.setBorder ( BorderFactory.createEmptyBorder ( 0, 0, 0, 0 ) );
                iconWebButton.setCursor ( Cursor.getDefaultCursor () );
                iconWebButton
                        .setPreferredSize ( orientation == JSplitPane.HORIZONTAL_SPLIT ? new Dimension ( 6, 7 ) : new Dimension ( 7, 6 ) );
                return iconWebButton;
            }

            @Override
            protected JButton createRightOneTouchButton ()
            {
                final JButton iconWebButton = WebButton.createIconWebButton ( new ImageIcon ( WebSplitPaneUI.class
                        .getResource ( orientation == JSplitPane.HORIZONTAL_SPLIT ? "icons/right.png" : "icons/down.png" ) ), 0, 0, 0,
                        false, true, false );
                iconWebButton.setBorder ( BorderFactory.createEmptyBorder ( 0, 0, 0, 0 ) );
                iconWebButton.setCursor ( Cursor.getDefaultCursor () );
                iconWebButton
                        .setPreferredSize ( orientation == JSplitPane.HORIZONTAL_SPLIT ? new Dimension ( 6, 7 ) : new Dimension ( 7, 6 ) );
                return iconWebButton;
            }

            @Override
            public void paint ( final Graphics g )
            {
                final Graphics2D g2d = ( Graphics2D ) g;
                final Object aa = LafUtils.setupAntialias ( g2d );

                if ( orientation == JSplitPane.HORIZONTAL_SPLIT )
                {
                    final int startY = getHeight () / 2 - 35;
                    final int endY = getHeight () / 2 + 35;
                    g2d.setPaint ( new LinearGradientPaint ( 0, startY, 0, endY, new float[]{ 0f, 0.25f, 0.75f, 1f }, gradient ) );
                    for ( int i = startY; i < endY; i += 5 )
                    {
                        g2d.fillRect ( getWidth () / 2 - 1, i - 1, 2, 2 );
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
                }

                super.paint ( g );

                LafUtils.restoreAntialias ( g2d, aa );
            }
        };
    }

    /**
     * {@inheritDoc}
     */
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

    /**
     * {@inheritDoc}
     */
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
}