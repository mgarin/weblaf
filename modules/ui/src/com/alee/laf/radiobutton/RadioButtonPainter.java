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
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class RadioButtonPainter<C extends JRadioButton, U extends WRadioButtonUI<C>, D extends IDecoration<C, D>>
        extends AbstractStateButtonPainter<C, U, D> implements IRadioButtonPainter<C, U>
{
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.isSelected () )
        {
            states.add ( DecorationState.checked );
        }
        return states;
    }
}