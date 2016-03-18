package com.alee.managers.style.skin.web;

import com.alee.laf.button.IToggleButtonPainter;
import com.alee.laf.button.WebToggleButtonUI;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public class WebToggleButtonPainter<E extends JToggleButton, U extends WebToggleButtonUI, D extends IDecoration<E, D>>
        extends AbstractButtonPainter<E, U, D> implements IToggleButtonPainter<E, U>
{
}