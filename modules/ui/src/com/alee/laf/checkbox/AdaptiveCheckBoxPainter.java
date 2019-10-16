package com.alee.laf.checkbox;

import com.alee.api.annotations.Nullable;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link CheckBoxPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WCheckBoxUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveCheckBoxPainter<C extends JCheckBox, U extends WCheckBoxUI<C>>
        extends AdaptivePainter<C, U> implements ICheckBoxPainter<C, U>
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

    @Nullable
    @Override
    public Rectangle getIconBounds ()
    {
        return null;
    }
}