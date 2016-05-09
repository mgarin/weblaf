package com.alee.laf.optionpane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JOptionPane component painters.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public interface IOptionPanePainter<E extends JOptionPane, U extends WebOptionPaneUI> extends SpecificPainter<E, U>
{
}