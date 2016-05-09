package com.alee.laf.button;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Base interface for various button component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IAbstractButtonPainter<E extends AbstractButton, U extends BasicButtonUI> extends SpecificPainter<E, U>
{
}