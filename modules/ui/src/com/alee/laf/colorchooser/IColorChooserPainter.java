package com.alee.laf.colorchooser;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JColorChooser component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IColorChooserPainter<E extends JColorChooser, U extends WebColorChooserUI> extends SpecificPainter<E, U>
{
}