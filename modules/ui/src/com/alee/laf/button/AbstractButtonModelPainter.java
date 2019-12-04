package com.alee.laf.button;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.painter.SpecificPainter;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.util.List;

/**
 * Abstract painter for custom components that use {@link ButtonModel}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public abstract class AbstractButtonModelPainter<C extends JComponent, U extends ComponentUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements SpecificPainter<C, U>
{
    /**
     * Listeners.
     */
    protected transient ChangeListener buttonModelChangeListener;

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
            if ( oldValue != null && buttonModelChangeListener != null )
            {
                ( ( ButtonModel ) oldValue ).removeChangeListener ( buttonModelChangeListener );
            }
            if ( newValue != null && buttonModelChangeListener != null )
            {
                ( ( ButtonModel ) newValue ).addChangeListener ( buttonModelChangeListener );
            }
            updateDecorationState ();
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

    /**
     * Installs {@link ChangeListener} into {@link ButtonModel} implementation.
     */
    protected void installModelChangeListener ()
    {
        buttonModelChangeListener = new ChangeListener ()
        {
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
        };
        getButtonModel ().addChangeListener ( buttonModelChangeListener );
    }

    /**
     * Uninstalls {@link ChangeListener} from {@link ButtonModel} implementation.
     */
    protected void uninstallModelChangeListener ()
    {
        getButtonModel ().removeChangeListener ( buttonModelChangeListener );
        buttonModelChangeListener = null;
    }

    /**
     * Returns whether or not button is pressed.
     *
     * @return true if button is pressed, false otherwise
     */
    protected boolean isPressed ()
    {
        return getButtonModel ().isPressed ();
    }

    /**
     * Returns whether or not button is selected.
     *
     * @return true if button is selected, false otherwise
     */
    protected boolean isSelected ()
    {
        return getButtonModel ().isSelected ();
    }

    /**
     * Returns {@link JComponent}'s {@link ButtonModel}.
     *
     * @return {@link JComponent}'s {@link ButtonModel}
     */
    @NotNull
    protected abstract ButtonModel getButtonModel ();
}