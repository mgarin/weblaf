package com.alee.managers.style.skin.web;

import com.alee.laf.checkbox.CheckBoxPainter;
import com.alee.laf.checkbox.CheckIcon;
import com.alee.laf.checkbox.SimpleCheckIcon;
import com.alee.laf.checkbox.WebCheckBoxUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebCheckBoxPainter<E extends JCheckBox, U extends WebCheckBoxUI> extends WebBasicStateButtonPainter<E, U>
        implements CheckBoxPainter<E, U>
{
    @Override
    protected CheckIcon createCheckStateIcon ()
    {
        return new SimpleCheckIcon ();
    }
}