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

    /**
     * Listeners.
     */
    protected AncestorListener ancestorListener;
    protected PropertyChangeListener propertyChangeListener;

    /**
     * Runtime variables.
     */
    protected final Color middleColor = new Color ( 158, 158, 158 );
    protected final Color[] gradient = new Color[]{ StyleConstants.transparent, middleColor, middleColor, StyleConstants.transparent };
    protected final float[] fractions = { 0f, 0.33f, 0.66f, 1f };

    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Updating initial layout
        updateLayout ( true );

        // Ancestor listener for border and layout updates when entering floating mode
        ancestorListener = new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateBorder ();
                updateLayout ( false );
            }
        };
        component.addAncestorListener ( ancestorListener );

        // Toolbar properties change listener for border and layout updates
        propertyChangeListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( final PropertyChangeEvent evt )
            {
                updateBorder ();
                updateLayout ( false );
            }
        };
        component.addPropertyChangeListener ( WebLookAndFeel.TOOLBAR_FLOATABLE_PROPERTY, propertyChangeListener );
        component.addPropertyChangeListener ( WebLookAndFeel.TOOLBAR_ORIENTATION_PROPERTY, propertyChangeListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removePropertyChangeListener ( WebLookAndFeel.TOOLBAR_ORIENTATION_PROPERTY, propertyChangeListener );
        component.removePropertyChangeListener ( WebLookAndFeel.TOOLBAR_FLOATABLE_PROPERTY, propertyChangeListener );
        component.removeAncestorListener ( ancestorListener );

        super.uninstall ( c, ui );
    }

    /**
     * Updates toolbar layout settings.
     * todo Something shady is going on here, should check this
     *
     * @param install whether or not should install layout if not installed
     */
    protected void updateLayout ( final boolean install )
    {
        final boolean installed = component.getLayout () instanceof ToolbarLayout;
        if ( !install && !installed )
        {
            return;
        }

        final ToolbarLayout layout = new ToolbarLayout ( spacing, component.getOrientation () );
        if ( installed )
        {
            final ToolbarLayout old = ( ToolbarLayout ) component.getLayout ();
            layout.setConstraints ( old.getConstraints () );
        }
        component.setLayout ( layout );
    }

    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Painting toolbar
        super.paint ( g2d, bounds, c, ui );

        // Painting gripper
        paintGripper ( g2d, c );
    }

    @Override
    protected void paintBorder ( final Graphics2D g2d, final Rectangle bounds, final Shape borderShape )
    {
        super.paintBorder ( g2d, bounds, borderShape );

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
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final Shape backgroundShape )
    {
        super.paintBackground ( g2d, bounds, backgroundShape );
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

    /**
     * Paints toolbar gripper.
     *
     * @param g2d graphics context
     * @param c   toolbar component
     */
    protected void paintGripper ( final Graphics2D g2d, final E c )
    {
        if ( c.isFloatable () )
        {
            final Object aa = GraphicsUtils.setupAntialias ( g2d );
            if ( c.getOrientation () == WebToolBar.HORIZONTAL )
            {
                final int gradY = shadeWidth + 1;
                final int gradEndY = c.getHeight () - shadeWidth - 2;
                if ( gradEndY > gradY )
                {
                    g2d.setPaint ( new LinearGradientPaint ( 0, gradY, 0, gradEndY, fractions, gradient ) );

                    // todo Properly paint gripper
                    // Determining gripper X coordinate
                    //                    int x = toolbarStyle.equals ( ToolbarStyle.standalone ) ? shadeWidth + 1 + ( ui.isFloating () ? -1 : 1 ) :
                    //                            gripperSpace / 2 - 1;
                    int x = shadeWidth + 1 + ( ui.isFloating () ? -1 : 1 );
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

                    // todo Properly paint gripper
                    // Determining gripper Y coordinate
                    //                    final int y = toolbarStyle.equals ( ToolbarStyle.standalone ) ? shadeWidth + 1 +
                    //                            ( ui.isFloating () ? -1 : 1 ) : gripperSpace / 2 - 1;
                    final int y = shadeWidth + 1 + ( ui.isFloating () ? -1 : 1 );

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
            GraphicsUtils.restoreAntialias ( g2d, aa );
        }
    }
}