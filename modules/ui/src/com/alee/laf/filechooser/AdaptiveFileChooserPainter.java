package com.alee.laf.filechooser;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple FileChooserPainter adapter class.
 * It is used to install simple non-specific painters into WebFileChooserUI.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveFileChooserPainter<E extends JFileChooser, U extends WebFileChooserUI> extends AdaptivePainter<E, U>
        implements IFileChooserPainter<E, U>
{
    /**
     * Constructs new AdaptiveFileChooserPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveFileChooserPainter ( final Painter painter )
    {
        super ( painter );
    }
}