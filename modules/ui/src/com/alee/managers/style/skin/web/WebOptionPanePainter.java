package com.alee.managers.style.skin.web;

import com.alee.laf.optionpane.IOptionPanePainter;
import com.alee.laf.optionpane.WebOptionPaneUI;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Web-style painter for JOptionPane component.
 * It is used as WebOptionPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */

public class WebOptionPanePainter<E extends JOptionPane, U extends WebOptionPaneUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IOptionPanePainter<E, U>
{
}