package com.alee.managers.style.skin.web;

import com.alee.extended.checkbox.TrisateCheckBoxPainter;
import com.alee.extended.checkbox.TristateCheckIcon;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.checkbox.WebTristateCheckBoxUI;
import com.alee.laf.checkbox.CheckIcon;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebTristateCheckBoxPainter<E extends JCheckBox, U extends WebTristateCheckBoxUI> extends WebBasicStateButtonPainter<E, U>
        implements TrisateCheckBoxPainter<E, U>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );
        // Initial check state
        stateIcon.setState ( getTristateCheckbox ().getState () );
    }

    /**
     * Returns tristate checkbox which uses this UI.
     *
     * @return tristate checkbox which uses this UI
     */
    protected WebTristateCheckBox getTristateCheckbox ()
    {
        return ( WebTristateCheckBox ) component;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CheckIcon createCheckStateIcon ()
    {
        return new TristateCheckIcon ( getTristateCheckbox () );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void performStateChanged ()
    {
        final WebTristateCheckBox tcb = getTristateCheckbox ();
        if ( isAnimationAllowed () && isAnimated () && tcb.isEnabled () )
        {
            stateIcon.setNextState ( tcb.getState () );
            checkTimer.start ();
        }
        else
        {
            checkTimer.stop ();
            stateIcon.setState ( tcb.getState () );
            tcb.repaint ();
        }
    }
}