package com.alee.laf.separator;

import com.alee.api.annotations.NotNull;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.SeparatorUI;
import java.util.List;

/**
 * Abstract painter for {@link JSeparator} component.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */
public abstract class AbstractSeparatorPainter<C extends JSeparator, U extends SeparatorUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IAbstractSeparatorPainter<C, U>
{
    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        states.add ( component.getOrientation () == SwingConstants.HORIZONTAL ? DecorationState.horizontal : DecorationState.vertical );
        return states;
    }
}