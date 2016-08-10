package com.alee.laf.combobox;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link ComboBoxPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WComboBoxUI}.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveComboBoxPainter<E extends JComboBox, U extends WComboBoxUI> extends AdaptivePainter<E, U>
        implements IComboBoxPainter<E, U>
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
    public void prepareToPaint ( final CellRendererPane currentValuePane )
    {
        // Ignore this method in adaptive class
    }
}