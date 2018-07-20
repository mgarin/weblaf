package com.alee.laf.checkbox;

import com.alee.laf.radiobutton.IAbstractStateButtonPainter;

import javax.swing.*;

/**
 * Base interface for {@link JCheckBox} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ICheckBoxPainter<C extends JCheckBox, U extends WCheckBoxUI<C>> extends IAbstractStateButtonPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}