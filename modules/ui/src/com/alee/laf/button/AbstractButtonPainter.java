package com.alee.laf.button;

import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.basic.BasicButtonUI;
import java.util.List;

/**
 * Abstract painter for button components.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public abstract class AbstractButtonPainter<E extends AbstractButton, U extends BasicButtonUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IAbstractButtonPainter<E, U>, ChangeListener
{
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Adding listeners
        component.getModel ().addChangeListener ( this );
    }

    @Override
    public void uninstall ( final E c, final U ui )
    {
        // Removing listeners
        component.getModel ().removeChangeListener ( this );

        super.uninstall ( c, ui );
    }

    @Override
    public void stateChanged ( final ChangeEvent e )
    {
        // Updating state on model changes
        updateDecorationState ();
    }

    @Override
    protected void propertyChange ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChange ( property, oldValue, newValue );

        // Switching model change listener to new model
        if ( CompareUtils.equals ( property, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            ( ( ButtonModel ) oldValue ).removeChangeListener ( this );
            ( ( ButtonModel ) newValue ).addChangeListener ( this );
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        final ButtonModel model = component.getModel ();
        if ( model.isPressed () )
        {
            states.add ( DecorationState.pressed );
        }
        if ( model.isSelected () )
        {
            states.add ( DecorationState.selected );
        }
        return states;
    }
}