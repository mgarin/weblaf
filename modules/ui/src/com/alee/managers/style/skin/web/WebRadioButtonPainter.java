package com.alee.managers.style.skin.web;

import com.alee.laf.checkbox.CheckIcon;
import com.alee.laf.radiobutton.RadioButtonPainter;
import com.alee.laf.radiobutton.RadioStateIcon;
import com.alee.laf.radiobutton.WebRadioButtonUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebRadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI> extends WebBasicStateButtonPainter<E, U>
        implements RadioButtonPainter<E, U>
{
    /**
     * {@inheritDoc}
     */
    @Override
    protected CheckIcon createCheckStateIcon ()
    {
        return new RadioStateIcon ();
    }
}