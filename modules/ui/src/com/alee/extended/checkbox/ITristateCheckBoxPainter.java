package com.alee.extended.checkbox;

import com.alee.laf.radiobutton.IAbstractStateButtonPainter;

import javax.swing.*;

/**
 * Base interface for JCheckBox component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ITristateCheckBoxPainter<E extends JCheckBox, U extends WebTristateCheckBoxUI> extends IAbstractStateButtonPainter<E, U>
{
}