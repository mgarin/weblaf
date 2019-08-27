package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

import static javax.swing.JInternalFrame.*;

/**
 * Basic painter for {@link JInternalFrame} component.
 * It is used as {@link WebInternalFrameUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public class InternalFramePainter<C extends JInternalFrame, U extends WInternalFrameUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IInternalFramePainter<C, U>
{
    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Decoration update on internal frame state changes
        if ( Objects.equals ( property, IS_SELECTED_PROPERTY, IS_CLOSED_PROPERTY, IS_ICON_PROPERTY, IS_MAXIMUM_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isMaximum () )
        {
            states.add ( DecorationState.maximized );
        }
        return states;
    }
}