package com.alee.laf.separator;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.SeparatorUI;

/**
 * Base interface for JSeparator component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IAbstractSeparatorPainter<E extends JSeparator, U extends SeparatorUI> extends SpecificPainter<E, U>
{
}