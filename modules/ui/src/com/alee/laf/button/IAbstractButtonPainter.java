package com.alee.laf.button;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

/**
 * Base interface for various button component painters.
 * todo Should be using WButtonUI as soon as menu items are moved to fully customized base UIs
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */
public interface IAbstractButtonPainter<C extends AbstractButton, U extends ButtonUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}