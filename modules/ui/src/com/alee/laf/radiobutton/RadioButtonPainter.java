package com.alee.laf.radiobutton;

import com.alee.laf.checkbox.AbstractStateButtonPainter;
import com.alee.laf.radiobutton.IRadioButtonPainter;
import com.alee.laf.radiobutton.WebRadioButtonUI;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class RadioButtonPainter<E extends JRadioButton, U extends WebRadioButtonUI, D extends IDecoration<E, D>>
        extends AbstractStateButtonPainter<E, U, D> implements IRadioButtonPainter<E, U>
{
}