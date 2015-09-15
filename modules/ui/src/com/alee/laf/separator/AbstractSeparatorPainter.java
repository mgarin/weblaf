package com.alee.laf.separator;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.SeparatorUI;

/**
 * Base interface for JSeparator component painters.
 *
 * @author Alexandr Zernov
 */

public interface AbstractSeparatorPainter<E extends JSeparator, U extends SeparatorUI> extends Painter<E, U>, SpecificPainter
{
}