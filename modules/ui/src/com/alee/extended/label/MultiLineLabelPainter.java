package com.alee.extended.label;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JLabel component painters.
 *
 * @author Alexandr Zernov
 */

public interface MultiLineLabelPainter<E extends JLabel, U extends WebMultiLineLabelUI> extends Painter<E, U>, SpecificPainter
{
}
