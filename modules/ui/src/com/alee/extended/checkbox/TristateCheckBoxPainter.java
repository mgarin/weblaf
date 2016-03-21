package com.alee.extended.checkbox;

import com.alee.laf.checkbox.AbstractStateButtonPainter;
import com.alee.laf.checkbox.CheckState;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import java.util.List;

/**
 * Basic painter for WebTristateCheckBox component.
 * It is used as WebTristateCheckBoxUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class TristateCheckBoxPainter<E extends WebTristateCheckBox, U extends WebTristateCheckBoxUI, D extends IDecoration<E, D>>
        extends AbstractStateButtonPainter<E, U, D> implements ITristateCheckBoxPainter<E, U>
{
    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.getState () == CheckState.checked )
        {
            states.add ( DecorationState.checked );
        }
        if ( component.getState () == CheckState.mixed )
        {
            states.add ( DecorationState.mixed );
        }
        return states;
    }
}