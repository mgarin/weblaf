package com.alee.laf.splitpane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Basic painter for {@link JSplitPane} component.
 * It is used as {@link WSplitPaneUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class SplitPanePainter<C extends JSplitPane, U extends WSplitPaneUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements ISplitPanePainter<C, U>
{
    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating split pane decoration states
        if ( Objects.equals ( property, JSplitPane.ORIENTATION_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @NotNull
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