package com.alee.laf.button;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.ButtonUI;

/**
 * Base interface for various button component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Mikle Garin
 */

public interface IAbstractButtonPainter<E extends AbstractButton, U extends ButtonUI> extends SpecificPainter<E, U>
{
}