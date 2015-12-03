package com.alee.laf.colorchooser;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JColorChooser component painters.
 *
 * @author Alexandr Zernov
 */

public interface ColorChooserPainter<E extends JColorChooser, U extends WebColorChooserUI> extends SpecificPainter<E, U>
{
}