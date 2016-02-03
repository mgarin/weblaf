package com.alee.managers.style.skin.web;

import com.alee.laf.button.IButtonPainter;
import com.alee.laf.button.WebButtonUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public class WebButtonPainter<E extends JButton, U extends WebButtonUI, D extends IDecoration<E, D>> extends AbstractButtonPainter<E, U, D>
        implements IButtonPainter<E, U>
{
}