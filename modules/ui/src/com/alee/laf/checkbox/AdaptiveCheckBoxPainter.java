package com.alee.laf.checkbox;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link CheckBoxPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WCheckBoxUI}.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveCheckBoxPainter<E extends JCheckBox, U extends WCheckBoxUI> extends AdaptivePainter<E, U>
        implements ICheckBoxPainter<E, U>
{
    /**
     * Constructs new {@link AdaptiveCheckBoxPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveCheckBoxPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public Rectangle getIconBounds ()
    {
        return null;
    }
}