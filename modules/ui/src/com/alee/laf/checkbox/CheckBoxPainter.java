package com.alee.laf.checkbox;

import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Basic painter for {@link JCheckBox} component.
 * It is used as {@link WCheckBoxUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class CheckBoxPainter<E extends JCheckBox, U extends WCheckBoxUI, D extends IDecoration<E, D>>
        extends AbstractStateButtonPainter<E, U, D> implements ICheckBoxPainter<E, U>
{
    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isSelected () )
        {
            states.add ( DecorationState.checked );
        }
        return states;
    }
}