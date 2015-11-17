package com.alee.laf.colorchooser;

import com.alee.painter.Painter;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JColorChooser component painters.
 *
 * @author Alexandr Zernov
 */

public interface ColorChooserPainter<E extends JColorChooser, U extends WebColorChooserUI> extends Painter<E, U>, SpecificPainter
{
}