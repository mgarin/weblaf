package com.alee.laf.combobox;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JComboBox component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IComboBoxPainter<E extends JComboBox, U extends WebComboBoxUI> extends SpecificPainter<E, U>
{
    /**
     * Prepares painter to pain combobox.
     *
     * @param currentValuePane current value pane
     */
    public void prepareToPaint ( CellRendererPane currentValuePane );
}