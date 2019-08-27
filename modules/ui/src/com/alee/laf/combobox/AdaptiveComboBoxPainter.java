package com.alee.laf.combobox;

import com.alee.api.annotations.NotNull;
import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ComboBoxPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WComboBoxUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveComboBoxPainter<C extends JComboBox, U extends WComboBoxUI> extends AdaptivePainter<C, U>
        implements IComboBoxPainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveComboBoxPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveComboBoxPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void prepareToPaint ( @NotNull final CellRendererPane currentValuePane )
    {
        // Ignore this method in adaptive class
    }
}