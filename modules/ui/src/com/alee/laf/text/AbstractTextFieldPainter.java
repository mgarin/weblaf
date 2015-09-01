package com.alee.laf.text;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicTextFieldUI;

/**
 * Base interface for JTextField component painters.
 *
 * @author Alexandr Zernov
 */

public interface AbstractTextFieldPainter<E extends JTextField, U extends BasicTextFieldUI> extends Painter<E, U>, SpecificPainter
{
}