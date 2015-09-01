package com.alee.laf.spinner;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JSpinner component painters.
 *
 * @author Alexandr Zernov
 */

public interface SpinnerPainter<E extends JSpinner, U extends WebSpinnerUI> extends Painter<E, U>, SpecificPainter
{
}