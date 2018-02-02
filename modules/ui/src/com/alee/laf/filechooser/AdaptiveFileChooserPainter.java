package com.alee.laf.filechooser;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link FileChooserPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WFileChooserUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public final class AdaptiveFileChooserPainter<C extends JFileChooser, U extends WFileChooserUI> extends AdaptivePainter<C, U>
        implements IFileChooserPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveFileChooserPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveFileChooserPainter ( final Painter painter )
    {
        super ( painter );
    }
}