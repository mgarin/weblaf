package com.alee.laf.radiobutton;

import com.alee.laf.button.IAbstractButtonPainter;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import java.awt.*;

/**
 * Base interface for {@link JCheckBox}, {@link JRadioButton} and {@link com.alee.extended.checkbox.WebTristateCheckBox} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IAbstractStateButtonPainter<C extends AbstractButton, U extends ButtonUI> extends IAbstractButtonPainter<C, U>
{
    /**
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    public Rectangle getIconBounds ();
}