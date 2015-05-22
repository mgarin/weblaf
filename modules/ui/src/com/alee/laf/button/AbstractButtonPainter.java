package com.alee.laf.button;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;

/**
 * Base interface for JButton component painters.
 *
 * @author Mikle Garin
 */

public interface AbstractButtonPainter<E extends AbstractButton, U extends BasicButtonUI> extends Painter<E, U>, SpecificPainter
{
}