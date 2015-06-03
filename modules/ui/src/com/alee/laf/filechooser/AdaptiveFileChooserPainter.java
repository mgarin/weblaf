package com.alee.laf.filechooser;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;

/**
 * Simple FileChooserPainter adapter class.
 * It is used to install simple non-specific painters into WebFileChooserUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveFileChooserPainter<E extends JFileChooser, U extends WebFileChooserUI> extends AdaptivePainter<E, U>
        implements FileChooserPainter<E, U>
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
