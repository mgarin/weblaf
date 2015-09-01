package com.alee.laf.tooltip;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public interface ToolTipPainter<E extends JComponent, U extends WebToolTipUI> extends Painter<E, U>, SpecificPainter
{
}