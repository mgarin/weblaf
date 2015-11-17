package com.alee.laf.combobox;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple ComboBoxPainter adapter class.
 * It is used to install simple non-specific painters into WebComboBoxUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveComboBoxPainter<E extends JComboBox, U extends WebComboBoxUI> extends AdaptivePainter<E, U>
        implements ComboBoxPainter<E, U>
{
    /**
     * Constructs new AdaptiveComboBoxPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveComboBoxPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void prepareToPaint ( final JButton arrowButton, final CellRendererPane currentValuePane )
    {
        // Ignore this method in adaptive class
    }
}