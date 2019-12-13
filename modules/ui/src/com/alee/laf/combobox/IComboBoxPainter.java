package com.alee.laf.combobox;

import com.alee.painter.ParameterizedPaint;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JComboBox component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IComboBoxPainter<C extends JComboBox, U extends WComboBoxUI>
        extends SpecificPainter<C, U>, ParameterizedPaint<ComboBoxPaintParameters>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}