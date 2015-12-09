package com.alee.laf.spinner;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSpinner component painters.
 *
 * @author Alexandr Zernov
 */

public interface ISpinnerPainter<E extends JSpinner, U extends WebSpinnerUI> extends SpecificPainter<E, U>
{
}