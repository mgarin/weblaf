package com.alee.laf.colorchooser;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JColorChooser component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IColorChooserPainter<C extends JColorChooser, U extends WColorChooserUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}