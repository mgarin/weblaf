package com.alee.extended.checkbox;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import java.awt.*;

/**
 * Simple {@link TristateCheckBoxPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WebTristateCheckBoxUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveTristateCheckBoxPainter<C extends WebTristateCheckBox, U extends WTristateCheckBoxUI<C>>
        extends AdaptivePainter<C, U> implements ITristateCheckBoxPainter<C, U>
{
    /**
     * Constructs new AdaptiveTristateCheckBoxPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTristateCheckBoxPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public Rectangle getIconBounds ()
    {
        return null;
    }
}