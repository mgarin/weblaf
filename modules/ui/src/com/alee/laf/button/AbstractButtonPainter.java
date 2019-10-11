package com.alee.laf.button;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ButtonUI;
import java.util.List;

/**
 * Abstract painter for button components.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public abstract class AbstractButtonPainter<C extends AbstractButton, U extends ButtonUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IAbstractButtonPainter<C, U>, ChangeListener
{
    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installModelChangeListener ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallModelChangeListener ();
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Switching model change listener on button model change
        if ( Objects.equals ( property, WebLookAndFeel.MODEL_PROPERTY ) )
        {
            if ( oldValue != null )
            {
                ( ( ButtonModel ) oldValue ).removeChangeListener ( this );
            }
            if ( newValue != null )
            {
                ( ( ButtonModel ) newValue ).addChangeListener ( this );
            }
            updateDecorationState ();
        }

        // Updating hover listener
        if ( Objects.equals ( property, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY ) )
        {
            updateHoverListeners ();
        }
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( isPressed () )
        {
            states.add ( DecorationState.pressed );
        }
        states.add ( isSelected () ? DecorationState.selected : DecorationState.unselected );
        return states;
    }

    @Override
    protected boolean usesHoverView ()
    {
        // Additional case of hover state usage for buttons exclusively
        return component.isRolloverEnabled () || super.usesHoverView ();
    }

    /**
     * Installs {@link ChangeListener} into {@link ButtonModel} implementation.
     */
    protected void installModelChangeListener ()
    {
        component.getModel ().addChangeListener ( this );
    }

    @Override
    public void stateChanged ( @NotNull final ChangeEvent e )
    {
        // Ensure component is still available
        // This might happen if painter is replaced from another ChangeListener
        if ( component != null )
        {
            // Updating state on model changes
            updateDecorationState ();
        }
    }

    /**
     * Uninstalls {@link ChangeListener} from {@link ButtonModel} implementation.
     */
    protected void uninstallModelChangeListener ()
    {
        component.getModel ().removeChangeListener ( this );
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
}