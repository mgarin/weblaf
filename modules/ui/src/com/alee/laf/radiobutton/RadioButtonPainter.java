package com.alee.laf.radiobutton;

import com.alee.laf.checkbox.AbstractStateButtonPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Basic painter for {@link JRadioButton} component.
 * It is used as {@link WRadioButtonUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class RadioButtonPainter<E extends JRadioButton, U extends WRadioButtonUI, D extends IDecoration<E, D>>
        extends AbstractStateButtonPainter<E, U, D> implements IRadioButtonPainter<E, U>
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