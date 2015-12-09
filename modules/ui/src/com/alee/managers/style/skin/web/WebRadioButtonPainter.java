package com.alee.managers.style.skin.web;

import com.alee.laf.checkbox.CheckIcon;
import com.alee.laf.radiobutton.IRadioButtonPainter;
import com.alee.laf.radiobutton.RadioStateIcon;
import com.alee.laf.radiobutton.WebRadioButtonUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebRadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI> extends AbstractStateButtonPainter<E, U>
        implements IRadioButtonPainter<E, U>
{
    @Override
    protected CheckIcon createCheckStateIcon ()
    {
        return new RadioStateIcon ();
    }
}