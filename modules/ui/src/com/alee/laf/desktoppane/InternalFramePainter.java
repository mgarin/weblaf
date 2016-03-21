package com.alee.laf.desktoppane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import java.util.List;

import static javax.swing.JInternalFrame.*;

/**
 * Basic painter for JInternalFrame component.
 * It is used as WebInternalFrameUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public class InternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IInternalFramePainter<E, U>
{
    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Decoration update on internal frame state changes
        if ( CompareUtils.equals ( property, IS_SELECTED_PROPERTY, IS_CLOSED_PROPERTY, IS_ICON_PROPERTY, IS_MAXIMUM_PROPERTY ) )
        {
            updateDecorationState ();
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isMaximum () )
        {
            states.add ( DecorationState.maximized );
        }
        return states;
    }
}