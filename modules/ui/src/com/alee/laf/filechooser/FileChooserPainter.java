package com.alee.laf.filechooser;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JFileChooser component painters.
 *
 * @author Alexandr Zernov
 */

public interface FileChooserPainter<E extends JFileChooser, U extends WebFileChooserUI> extends SpecificPainter<E, U>
{
}