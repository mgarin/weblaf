package com.alee.laf.checkbox;

import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * @author Alexandr Zernov
 */

public class CheckBoxPainter<E extends JCheckBox, U extends WebCheckBoxUI, D extends IDecoration<E, D>>
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