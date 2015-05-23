package com.alee.managers.style.skin.web;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.global.StyleConstants;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.toolbar.*;
import com.alee.utils.GraphicsUtils;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * @author Alexandr Zernov
 */

public class WebToolBarPainter<E extends JToolBar, U extends WebToolBarUI> extends WebDecorationPainter<E, U>
        implements ToolBarPainter<E, U>
{
    public static final int gripperSpace = 5;

    /**
     * Style settings.
     */
    protected int spacing = WebToolBarStyle.spacing;
    protected Color topBgColor = WebToolBarStyle.topBgColor;
    protected Color bottomBgColor = WebToolBarStyle.bottomBgColor;
    protected ToolbarStyle toolbarStyle = WebToolBarStyle.toolbarStyle;

    /**
     * Listeners.
     */
    protected AncestorListener ancestorListener;
    protected PropertyChangeListener propertyChangeListener;
    protected PropertyChangeListener componentOrientationListener;

    /**
     * Runtime variables.
     */
    protected boolean floating;
    protected final Color middleColor = new Color ( 158, 158, 158 );
    protected final Color[] gradient = new Color[]{ StyleConstants.transparent, middleColor, middleColor, StyleConstants.transparent };
    protected final float[] fractions = { 0f, 0.33f, 0.66f, 1f };

    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        updateLayout ( c, true );

        // Border and layout update listeners
        ancestorListener = new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateBorder ();
                updateLayout ( c, false );
            }
        };
        c.addAncestorListener ( ancestorListener );
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
                updateLayout ( c, false );
            }
        };
        c.addPropertyChangeListener ( WebLookAndFeel.TOOLBAR_FLOATABLE_PROPERTY, propertyChangeListener );
        c.addPropertyChangeListener ( WebLookAndFeel.TOOLBAR_ORIENTATION_PROPERTY, propertyChangeListener );
        componentOrientationListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
            }
        };
        c.addPropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, componentOrientationListener );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        c.removeAncestorListener ( ancestorListener );
        c.removePropertyChangeListener ( WebLookAndFeel.TOOLBAR_ORIENTATION_PROPERTY, propertyChangeListener );
        c.removePropertyChangeListener ( WebLookAndFeel.TOOLBAR_FLOATABLE_PROPERTY, propertyChangeListener );
        c.removePropertyChangeListener ( WebLookAndFeel.ORIENTATION_PROPERTY, componentOrientationListener );

        super.uninstall ( c, ui );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        final Object aa = GraphicsUtils.setupAntialias ( g2d );

        // Painting toolbar
        super.paint ( g2d, bounds, c, ui );

        // Painting gripper
        paintGripper ( g2d, bounds, c, ui );

        GraphicsUtils.restoreAntialias ( g2d, aa );
    }

    @Override
    public void setFloating ( final boolean floating )
    {
        this.floating = floating;
    }

    @Override
    protected void paintBorder ( final Graphics2D g2d, final Rectangle bounds, final E c, final Shape borderShape )
    {
        super.paintBorder ( g2d, bounds, c, borderShape );
        //        if ( c != null )
        //        {
        //            // Preserve old borders
        //            if ( SwingUtils.isPreserveBorders ( c ) )
        //            {
        //                return;
        //            }
        //
        //            // Web-style insets
        //            final int gripperSpacing = c.isFloatable () ? gripperSpace : 0;
        //            final boolean horizontal = c.getOrientation () == WebToolBar.HORIZONTAL;
        //            if ( toolbarStyle.equals ( ToolbarStyle.standalone ) )
        //            {
        //                if ( floating )
        //                {
        //                    c.setBorder ( LafUtils.createWebBorder ( margin.top + ( !horizontal ? gripperSpacing : 0 ),
        //                            margin.left + ( horizontal && ltr ? gripperSpacing : 0 ), margin.bottom,
        //                            margin.right + ( horizontal && !ltr ? gripperSpacing : 0 ) ) );
        //                }
        //                else
        //                {
        //                    c.setBorder ( LafUtils.createWebBorder ( margin.top + 1 + shadeWidth + ( !horizontal ? gripperSpacing : 0 ),
        //                            margin.left + 1 + shadeWidth +
        //                                    ( horizontal && ltr ? gripperSpacing : 0 ), margin.bottom + 1 + shadeWidth,
        //                            margin.right + 1 + shadeWidth +
        //                                    ( horizontal && !ltr ? gripperSpacing : 0 ) ) );
        //                }
        //            }
        //            else
        //            {
        //                if ( floating )
        //                {
        //                    c.setBorder ( LafUtils.createWebBorder ( margin.top + ( !horizontal ? gripperSpacing : 0 ),
        //                            margin.left + ( horizontal && ltr ? gripperSpacing : 0 ), margin.bottom,
        //                            margin.right + ( horizontal && !ltr ? gripperSpacing : 0 ) ) );
        //                }
        //                else
        //                {
        //                    c.setBorder ( LafUtils.createWebBorder ( margin.top + ( !horizontal ? gripperSpacing : 0 ),
        //                            margin.left + ( horizontal && ltr ? gripperSpacing : 0 ) +
        //                                    ( !horizontal && !ltr ? 1 : 0 ), margin.bottom + ( horizontal ? 1 : 0 ),
        //                            margin.right + ( horizontal && !ltr ? gripperSpacing : 0 ) +
        //                                    ( !horizontal && ltr ? 1 : 0 ) ) );
        //                }
        //            }
        //        }
    }

    @Override
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final E c, final Shape backgroundShape )
    {
        super.paintBackground ( g2d, bounds, c, backgroundShape );
        //
        //        final boolean horizontal = c.getOrientation () == WebToolBar.HORIZONTAL;
        //
        //        if ( floating )
        //        {
        //            final GradientPaint pg;
        //            if ( horizontal )
        //            {
        //                pg = new GradientPaint ( 0, c.getHeight () / 2, topBgColor, 0, c.getHeight (), bottomBgColor );
        //            }
        //            else
        //            {
        //                pg = new GradientPaint ( c.getWidth () / 2, 0, topBgColor, c.getWidth (), 0, bottomBgColor );
        //            }
        //            g2d.setPaint ( pg );
        //            g2d.fill ( backgroundShape );
        //        }
        //        else
        //        {
        //            if ( toolbarStyle.equals ( ToolbarStyle.standalone ) )
        //            {
        //                final RoundRectangle2D rr = new RoundRectangle2D.Double ( shadeWidth, shadeWidth, c.getWidth () - shadeWidth * 2 - 1,
        //                        c.getHeight () - shadeWidth * 2 - 1, round, round );
        //
        //                if ( c.isEnabled () )
        //                {
        //                    GraphicsUtils.drawShade ( g2d, rr, StyleConstants.shadeColor, shadeWidth );
        //                }
        //
        //                if ( horizontal )
        //                {
        //                    g2d.setPaint ( new GradientPaint ( 0, c.getHeight () / 2, topBgColor, 0, c.getHeight (), bottomBgColor ) );
        //                }
        //                else
        //                {
        //                    g2d.setPaint ( new GradientPaint ( c.getWidth () / 2, 0, topBgColor, c.getWidth (), 0, bottomBgColor ) );
        //                }
        //                g2d.fill ( rr );
        //
        //                g2d.setPaint ( c.isEnabled () ? borderColor : disabledBorderColor );
        //                g2d.draw ( rr );
        //            }
        //            else
        //            {
        //                if ( horizontal )
        //                {
        //                    g2d.setPaint ( new GradientPaint ( 0, c.getHeight () / 2, topBgColor, 0, c.getHeight (), bottomBgColor ) );
        //                }
        //                else
        //                {
        //                    g2d.setPaint ( new GradientPaint ( c.getWidth () / 2, 0, topBgColor, c.getWidth (), 0, bottomBgColor ) );
        //                }
        //                g2d.fillRect ( 0, 0, c.getWidth (), c.getHeight () );
        //
        //                g2d.setPaint ( c.isEnabled () ? borderColor : disabledBorderColor );
        //                if ( horizontal )
        //                {
        //                    g2d.drawLine ( 0, c.getHeight () - 1, c.getWidth () - 1, c.getHeight () - 1 );
        //                }
        //                else
        //                {
        //                    if ( ltr )
        //                    {
        //                        g2d.drawLine ( c.getWidth () - 1, 0, c.getWidth () - 1, c.getHeight () - 1 );
        //                    }
        //                    else
        //                    {
        //                        g2d.drawLine ( 0, 0, 0, c.getHeight () - 1 );
        //                    }
        //                }
        //            }
        //        }
    }

    protected void paintGripper ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        if ( c.isFloatable () )
        {
            if ( c.getOrientation () == WebToolBar.HORIZONTAL )
            {
                final int gradY = shadeWidth + 1;
                final int gradEndY = c.getHeight () - shadeWidth - 2;
                if ( gradEndY > gradY )
                {
                    g2d.setPaint ( new LinearGradientPaint ( 0, gradY, 0, gradEndY, fractions, gradient ) );

                    // Determining gripper X coordinate
                    int x = toolbarStyle.equals ( ToolbarStyle.standalone ) ? shadeWidth + 1 + margin.left + ( floating ? -1 : 1 ) :
                            margin.left + gripperSpace / 2 - 1;
                    if ( !ltr )
                    {
                        x = c.getWidth () - x - 2;
                    }

                    // Painting gripper
                    for ( int i = c.getHeight () / 2 - 3; i >= gradY; i -= 4 )
                    {
                        g2d.fillRect ( x, i, 2, 2 );
                    }
                    for ( int i = c.getHeight () / 2 + 1; i + 2 <= gradEndY; i += 4 )
                    {
                        g2d.fillRect ( x, i, 2, 2 );
                    }
                }
            }
            else
            {
                final int gradX = shadeWidth + 1;
                final int gradEndX = c.getWidth () - shadeWidth - 2;
                if ( gradEndX > gradX )
                {
                    g2d.setPaint ( new LinearGradientPaint ( gradX, 0, gradEndX, 0, fractions, gradient ) );

                    // Determining gripper Y coordinate
                    final int y = toolbarStyle.equals ( ToolbarStyle.standalone ) ? shadeWidth + 1 + margin.top + ( floating ? -1 : 1 ) :
                            margin.top + gripperSpace / 2 - 1;

                    // Painting gripper
                    for ( int i = c.getWidth () / 2 - 3; i >= gradX; i -= 4 )
                    {
                        g2d.fillRect ( i, y, 2, 2 );
                    }
                    for ( int i = c.getWidth () / 2 + 1; i + 2 <= gradEndX; i += 4 )
                    {
                        g2d.fillRect ( i, y, 2, 2 );
                    }
                }
            }
        }
    }

    private void updateLayout ( final JComponent c, final boolean install )
    {
        final boolean installed = c.getLayout () instanceof ToolbarLayout;
        if ( !install && !installed )
        {
            return;
        }

        final ToolbarLayout layout = new ToolbarLayout ( spacing, component.getOrientation () );
        if ( installed )
        {
            final ToolbarLayout old = ( ToolbarLayout ) c.getLayout ();
            layout.setConstraints ( old.getConstraints () );
        }
        c.setLayout ( layout );
    }
}