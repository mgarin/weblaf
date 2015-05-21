package com.alee.laf.tooltip;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;
import com.alee.laf.button.WebButtonUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public interface ToolTipPainter<E extends JButton, U extends WebButtonUI> extends Painter<E, U>, SpecificPainter
{
}
