package com.alee.managers.style.skin.web;

import com.alee.extended.checkbox.ITristateCheckBoxPainter;
import com.alee.extended.checkbox.TristateCheckIcon;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.checkbox.WebTristateCheckBoxUI;
import com.alee.laf.checkbox.CheckIcon;

/**
 * @author Alexandr Zernov
 */

public class WebTristateCheckBoxPainter<E extends WebTristateCheckBox, U extends WebTristateCheckBoxUI>
        extends AbstractStateButtonPainter<E, U> implements ITristateCheckBoxPainter<E, U>
{
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );

        // Initial check state
        stateIcon.setState ( component.getState () );
    }

    @Override
    protected CheckIcon createCheckStateIcon ()
    {
        return new TristateCheckIcon ( component );
    }

    @Override
    protected void performStateChanged ()
    {
        if ( isAnimationAllowed () )
        {
            stateIcon.setNextState ( component.getState () );
            checkTimer.start ();
        }
        else
        {
            checkTimer.stop ();
            stateIcon.setState ( component.getState () );
            component.repaint ();
        }
    }
}