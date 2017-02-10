package com.alee.laf.checkbox;

import com.alee.laf.radiobutton.IAbstractStateButtonPainter;

import javax.swing.*;

/**
 * Base interface for {@link JCheckBox} component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface ICheckBoxPainter<E extends JCheckBox, U extends WCheckBoxUI> extends IAbstractStateButtonPainter<E, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}