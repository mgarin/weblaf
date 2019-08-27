package com.alee.extended.checkbox;

import com.alee.api.annotations.NotNull;
import com.alee.laf.checkbox.AbstractStateButtonPainter;
import com.alee.laf.checkbox.CheckState;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import java.util.List;

/**
 * Basic painter for {@link WebTristateCheckBox} component.
 * It is used as {@link WTristateCheckBoxUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class TristateCheckBoxPainter<C extends WebTristateCheckBox, U extends WTristateCheckBoxUI<C>, D extends IDecoration<C, D>>
        extends AbstractStateButtonPainter<C, U, D> implements ITristateCheckBoxPainter<C, U>
{
    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( component.getState () == CheckState.checked )
        {
            states.add ( DecorationState.checked );
        }
        if ( component.getState () == CheckState.mixed )
        {
            states.add ( DecorationState.mixed );
        }
        return states;
    }
}