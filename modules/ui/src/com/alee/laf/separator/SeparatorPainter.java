package com.alee.laf.separator;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSeparator component painters.
 *
 * @author Alexandr Zernov
 */

public interface SeparatorPainter<E extends JSeparator, U extends WebSeparatorUI> extends Painter<E, U>, SpecificPainter
{
}
