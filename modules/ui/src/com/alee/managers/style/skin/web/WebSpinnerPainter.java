package com.alee.managers.style.skin.web;

import com.alee.laf.spinner.ISpinnerPainter;
import com.alee.laf.spinner.WebSpinnerUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebSpinnerPainter<E extends JSpinner, U extends WebSpinnerUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements ISpinnerPainter<E, U>
{
}