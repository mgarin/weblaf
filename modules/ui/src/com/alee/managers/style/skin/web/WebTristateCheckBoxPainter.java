package com.alee.managers.style.skin.web;

import com.alee.extended.checkbox.ITristateCheckBoxPainter;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.checkbox.WebTristateCheckBoxUI;
import com.alee.laf.checkbox.CheckState;
import com.alee.managers.style.skin.web.data.DecorationState;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import java.util.List;

/**
 * @author Alexandr Zernov
 */

public class WebTristateCheckBoxPainter<E extends WebTristateCheckBox, U extends WebTristateCheckBoxUI, D extends IDecoration<E, D>>
        extends AbstractStateButtonPainter<E, U, D> implements ITristateCheckBoxPainter<E, U>
{
    @Override
    protected List<String> getDecorationStates ()
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