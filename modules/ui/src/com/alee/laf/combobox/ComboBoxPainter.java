package com.alee.laf.combobox;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JComboBox component painters.
 *
 * @author Alexandr Zernov
 */

public interface ComboBoxPainter<E extends JComboBox, U extends WebComboBoxUI> extends SpecificPainter<E, U>
{
    /**
     * Prepares painter to pain combobox.
     *
     * @param arrowButton      arrow button
     * @param currentValuePane current value pane
     */
    public void prepareToPaint ( JButton arrowButton, CellRendererPane currentValuePane );
}