package com.alee.laf.colorchooser;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ColorChooserPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WColorChooserUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveColorChooserPainter<C extends JColorChooser, U extends WColorChooserUI> extends AdaptivePainter<C, U>
        implements IColorChooserPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveColorChooserPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveColorChooserPainter ( final Painter painter )
    {
        super ( painter );
    }
}