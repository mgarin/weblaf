package com.alee.laf.text;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JTextField component painters.
 *
 * @author Alexandr Zernov
 */

public interface TextFieldPainter<E extends JTextField, U extends WebTextFieldUI> extends AbstractTextFieldPainter<E, U>, SpecificPainter
{
}