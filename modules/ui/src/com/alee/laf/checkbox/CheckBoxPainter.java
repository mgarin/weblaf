package com.alee.laf.checkbox;

import com.alee.extended.painter.SpecificPainter;
import com.alee.laf.radiobutton.BasicStateButtonPainter;

import javax.swing.*;

/**
 * Base interface for JCheckBox component painters.
 *
 * @author Alexandr Zernov
 */

public interface CheckBoxPainter<E extends JCheckBox, U extends WebCheckBoxUI> extends BasicStateButtonPainter<E, U>, SpecificPainter
{
}