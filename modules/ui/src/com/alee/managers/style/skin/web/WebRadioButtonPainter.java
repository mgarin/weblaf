package com.alee.managers.style.skin.web;

import com.alee.laf.radiobutton.IRadioButtonPainter;
import com.alee.laf.radiobutton.WebRadioButtonUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebRadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI, D extends IDecoration<E, D>>
        extends AbstractStateButtonPainter<E, U, D> implements IRadioButtonPainter<E, U>
{
}