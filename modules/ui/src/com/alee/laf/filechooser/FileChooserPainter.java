package com.alee.laf.filechooser;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JFileChooser component painters.
 *
 * @author Alexandr Zernov
 */

public interface FileChooserPainter<E extends JFileChooser, U extends WebFileChooserUI> extends Painter<E, U>, SpecificPainter
{
}