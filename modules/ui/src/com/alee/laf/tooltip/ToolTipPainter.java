package com.alee.laf.tooltip;

import com.alee.painter.Painter;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public interface ToolTipPainter<E extends JComponent, U extends WebToolTipUI> extends Painter<E, U>, SpecificPainter
{
}