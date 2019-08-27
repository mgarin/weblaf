package com.alee.laf.checkbox;

import com.alee.api.annotations.NotNull;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.util.List;

/**
 * Basic painter for {@link JCheckBox} component.
 * It is used as {@link WCheckBoxUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class CheckBoxPainter<C extends JCheckBox, U extends WCheckBoxUI<C>, D extends IDecoration<C, D>>
        extends AbstractStateButtonPainter<C, U, D> implements ICheckBoxPainter<C, U>
{
    @NotNull
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