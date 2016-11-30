package com.alee.laf.button;

import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;
import com.alee.utils.CompareUtils;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import java.util.List;

/**
 * Abstract painter for button components.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public abstract class AbstractButtonPainter<E extends AbstractButton, U extends ButtonUI, D extends IDecoration<E, D>>
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
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Switching model change listener to new model
        if ( CompareUtils.equals ( property, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            ( ( ButtonModel ) oldValue ).removeChangeListener ( this );
            ( ( ButtonModel ) newValue ).addChangeListener ( this );
            updateDecorationState ();
        }

        // Updating hover listener
        if ( CompareUtils.equals ( property, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY ) )
        {
            updateHoverListener ();
        }
    }

    @Override
    protected List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( isPressed () )
        {
            states.add ( DecorationState.pressed );
        }
        if ( isSelected () )
        {
            states.add ( DecorationState.selected );
        }
        return states;
    }

    /**
     * Returns whether or not button is pressed.
     *
     * @return true if button is pressed, false otherwise
     */
    protected boolean isPressed ()
    {
        return component.getModel ().isPressed ();
    }

    /**
     * Returns whether or not button is selected.
     *
     * @return true if button is selected, false otherwise
     */
    protected boolean isSelected ()
    {
        return component.getModel ().isSelected ();
    }

    @Override
    protected boolean usesHover ()
    {
        // Additional case of hover state usage for buttons exclusively
        return component.isRolloverEnabled () || super.usesHover ();
    }
}