package com.alee.laf.splitpane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.util.List;

/**
 * Basic painter for {@link JSplitPane} component.
 * It is used as {@link WebSplitPaneUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class SplitPanePainter<E extends JSplitPane, U extends WebSplitPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements ISplitPanePainter<E, U>
{
    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating split pane decoration states
        if ( CompareUtils.equals ( property, JSplitPane.ORIENTATION_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Split pane orientation
        final boolean horizontal = component.getOrientation () == JSplitPane.HORIZONTAL_SPLIT;
        states.add ( horizontal ? DecorationState.horizontal : DecorationState.vertical );

        // One-touch
        if ( component.isOneTouchExpandable () )
        {
            states.add ( DecorationState.oneTouch );
        }

        return states;
    }
}