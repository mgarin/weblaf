package com.alee.laf.button;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

/**
 * Abstract painter for components based on {@link AbstractButton}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public abstract class AbstractButtonPainter<C extends AbstractButton, U extends ButtonUI, D extends IDecoration<C, D>>
        extends AbstractButtonModelPainter<C, U, D> implements IAbstractButtonPainter<C, U>
{
    @Override
    protected void propertyChanged ( @NotNull final String property, @Nullable final Object oldValue, @Nullable final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating hover listener
        if ( Objects.equals ( property, AbstractButton.ROLLOVER_ENABLED_CHANGED_PROPERTY ) )
        {
            updateHoverListeners ();
        }
    }

    @Override
    protected boolean usesHoverView ()
    {
        // Additional case of hover state usage for buttons exclusively
        return component.isRolloverEnabled () || super.usesHoverView ();
    }

    @NotNull
    @Override
    protected ButtonModel getButtonModel ()
    {
        return component.getModel ();
    }
}