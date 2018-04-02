package com.alee.laf.toolbar;

import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.swing.AncestorAdapter;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;
import java.util.List;

/**
 * Basic painter for {@link JToolBar} component.
 * It is used as {@link WebToolBarUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class ToolBarPainter<C extends JToolBar, U extends WebToolBarUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IToolBarPainter<C, U>
{
    /**
     * Listener used for various updates upon entering floating mode.
     */
    protected transient AncestorListener ancestorListener;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installFloatingModeListeners ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallFloatingModeListeners ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Toolbar properties change listener for border and layout updates
        if ( Objects.equals ( property, WebLookAndFeel.FLOATABLE_PROPERTY, WebLookAndFeel.ORIENTATION_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        states.add ( component.getOrientation () == JToolBar.HORIZONTAL ? DecorationState.horizontal : DecorationState.vertical );
        states.add ( ui.isFloating () ? DecorationState.floating : DecorationState.attached );
        return states;
    }

    /**
     * Installs listeners used for various updates upon entering floating mode.
     */
    protected void installFloatingModeListeners ()
    {
        ancestorListener = new AncestorAdapter ()
        {
            @Override
            public void ancestorAdded ( final AncestorEvent event )
            {
                updateDecorationState ();
            }
        };
        component.addAncestorListener ( ancestorListener );
    }

    /**
     * Uninstalls listeners used for various updates upon entering floating mode.
     */
    protected void uninstallFloatingModeListeners ()
    {
        component.removeAncestorListener ( ancestorListener );
        ancestorListener = null;
    }

    @Override
    protected void paintContent ( final Graphics2D g2d, final Rectangle bounds, final C c, final U ui )
    {
        //        // Painting gripper
        //        paintGripper ( g2d, c );
    }

    //    public static final int gripperSpace = 5;
    //
    //    /**
    //     * Paints toolbar gripper.
    //     *
    //     * @param g2d graphics context
    //     * @param c   toolbar component
    //     */
    //    protected void paintGripper ( final Graphics2D g2d, final E c )
    //    {
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
    //    }
}