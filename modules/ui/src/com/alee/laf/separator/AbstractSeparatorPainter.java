package com.alee.laf.separator;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.SeparatorUI;

/**
 * Base interface for JSeparator component painters.
 *
 * @author Alexandr Zernov
 */

public interface AbstractSeparatorPainter<E extends JSeparator, U extends SeparatorUI> extends SpecificPainter<E, U>
{
}