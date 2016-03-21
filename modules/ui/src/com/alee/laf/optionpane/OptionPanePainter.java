package com.alee.laf.optionpane;

import com.alee.painter.decoration.AbstractContainerPainter;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for JOptionPane component.
 * It is used as WebOptionPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class OptionPanePainter<E extends JOptionPane, U extends WebOptionPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IOptionPanePainter<E, U>
{
    /**
     * Implementation is used completely from {@link com.alee.painter.decoration.AbstractContainerPainter}.
     */
}