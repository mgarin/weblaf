package com.alee.laf.combobox;

import com.alee.api.annotations.NotNull;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JComboBox component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IComboBoxPainter<C extends JComboBox, U extends WComboBoxUI> extends SpecificPainter<C, U>
{
    /**
     * Prepares painter to pain combobox.
     *
     * @param currentValuePane current value pane
     */
    public void prepareToPaint ( @NotNull CellRendererPane currentValuePane );
}