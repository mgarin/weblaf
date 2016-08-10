package com.alee.laf.separator;

import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.SeparatorUI;
import java.util.List;

/**
 * Abstract painter for separator components.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 * @author Mikle Garin
 */

public abstract class AbstractSeparatorPainter<E extends JSeparator, U extends SeparatorUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IAbstractSeparatorPainter<E, U>
{
    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        states.add ( component.getOrientation () == SwingConstants.HORIZONTAL ? DecorationState.horizontal : DecorationState.vertical );
        return states;
    }
}