package com.alee.laf.toolbar;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.toolbar.IToolBarPainter;
import com.alee.laf.toolbar.WebToolBarUI;
import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.states.Orientation;
import com.alee.utils.CompareUtils;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.List;

/**
 * @author Alexandr Zernov
 */

public class ToolBarPainter<E extends JToolBar, U extends WebToolBarUI, D extends IDecoration<E, D>> extends AbstractContainerPainter<E, U, D>
        implements IToolBarPainter<E, U>
{
    public static final int gripperSpace = 5;

    /**
     * Style settings.
     */
    protected int spacing;

    /**
     * Listeners.
     */
    protected AncestorListener ancestorListener;

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
                updateLayout ( false );
                updateDecorationState ();
            }
        };
        component.addAncestorListener ( ancestorListener );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.removeAncestorListener ( ancestorListener );

        super.uninstall ( c, ui );
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Toolbar properties change listener for border and layout updates
        if ( CompareUtils.equals ( property, WebLookAndFeel.TOOLBAR_FLOATABLE_PROPERTY, WebLookAndFeel.TOOLBAR_ORIENTATION_PROPERTY ) )
        {
            updateLayout ( false );
            updateDecorationState ();
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        states.add ( Orientation.get ( component.getOrientation () ).name () );
        if ( ui.isFloating () )
        {
            states.add ( DecorationState.floating );
        }
        return states;
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
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
        // Painting gripper
        paintGripper ( g2d, c );
    }

    /**
     * Paints toolbar gripper.
     *
     * @param g2d graphics context
     * @param c   toolbar component
     */
    protected void paintGripper ( final Graphics2D g2d, final E c )
    {
        //        if ( c.isFloatable () )
        //        {
        //            final Object aa = GraphicsUtils.setupAntialias ( g2d );
        //            if ( c.getOrientation () == WebToolBar.HORIZONTAL )
        //            {
        //                final int gradY = shadeWidth + 1;
        //                final int gradEndY = c.getHeight () - shadeWidth - 2;
        //                if ( gradEndY > gradY )
        //                {
        //                    g2d.setPaint ( new LinearGradientPaint ( 0, gradY, 0, gradEndY, fractions, gradient ) );
        //
        //                    // todo Properly paint gripper
        //                    // Determining gripper X coordinate
        //                    //                    int x = toolbarStyle.equals ( ToolbarStyle.standalone ) ? shadeWidth + 1 + ( ui.isFloating () ? -1 : 1 ) :
        //                    //                            gripperSpace / 2 - 1;
        //                    int x = shadeWidth + 1 + ( ui.isFloating () ? -1 : 1 );
        //                    if ( !ltr )
        //                    {
        //                        x = c.getWidth () - x - 2;
        //                    }
        //
        //                    // Painting gripper
        //                    for ( int i = c.getHeight () / 2 - 3; i >= gradY; i -= 4 )
        //                    {
        //                        g2d.fillRect ( x, i, 2, 2 );
        //                    }
        //                    for ( int i = c.getHeight () / 2 + 1; i + 2 <= gradEndY; i += 4 )
        //                    {
        //                        g2d.fillRect ( x, i, 2, 2 );
        //                    }
        //                }
        //            }
        //            else
        //            {
        //                final int gradX = shadeWidth + 1;
        //                final int gradEndX = c.getWidth () - shadeWidth - 2;
        //                if ( gradEndX > gradX )
        //                {
        //                    g2d.setPaint ( new LinearGradientPaint ( gradX, 0, gradEndX, 0, fractions, gradient ) );
        //
        //                    // todo Properly paint gripper
        //                    // Determining gripper Y coordinate
        //                    //                    final int y = toolbarStyle.equals ( ToolbarStyle.standalone ) ? shadeWidth + 1 +
        //                    //                            ( ui.isFloating () ? -1 : 1 ) : gripperSpace / 2 - 1;
        //                    final int y = shadeWidth + 1 + ( ui.isFloating () ? -1 : 1 );
        //
        //                    // Painting gripper
        //                    for ( int i = c.getWidth () / 2 - 3; i >= gradX; i -= 4 )
        //                    {
        //                        g2d.fillRect ( i, y, 2, 2 );
        //                    }
        //                    for ( int i = c.getWidth () / 2 + 1; i + 2 <= gradEndX; i += 4 )
        //                    {
        //                        g2d.fillRect ( i, y, 2, 2 );
        //                    }
        //                }
        //            }
        //            GraphicsUtils.restoreAntialias ( g2d, aa );
        //        }
    }
}