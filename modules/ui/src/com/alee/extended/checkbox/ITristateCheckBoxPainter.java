package com.alee.extended.checkbox;

import com.alee.laf.radiobutton.IAbstractStateButtonPainter;

import javax.swing.*;

/**
 * Base interface for JCheckBox component painters.
 *
 * @author Alexandr Zernov
 */

public interface ITristateCheckBoxPainter<E extends JCheckBox, U extends WebTristateCheckBoxUI> extends IAbstractStateButtonPainter<E, U>
{
}