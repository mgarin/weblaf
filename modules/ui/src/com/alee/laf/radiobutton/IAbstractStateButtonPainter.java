package com.alee.laf.radiobutton;

import com.alee.laf.button.IAbstractButtonPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;

/**
 * Base interface for JCheckBox and JRadioButton component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IAbstractStateButtonPainter<E extends AbstractButton, U extends BasicButtonUI> extends IAbstractButtonPainter<E, U>
{
    /**
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    public Rectangle getIconRect ();
}