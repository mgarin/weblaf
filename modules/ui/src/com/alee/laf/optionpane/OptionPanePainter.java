package com.alee.laf.optionpane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JOptionPane} component.
 * It is used as {@link WebOptionPaneUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class OptionPanePainter<C extends JOptionPane, U extends WebOptionPaneUI, D extends IDecoration<C, D>>
        extends AbstractContainerPainter<C, U, D> implements IOptionPanePainter<C, U>
{
    /**
     * Implementation is used completely from {@link AbstractContainerPainter}.
     */
}