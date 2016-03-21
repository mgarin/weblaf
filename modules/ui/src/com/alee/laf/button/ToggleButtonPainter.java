package com.alee.laf.button;

import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public class ToggleButtonPainter<E extends JToggleButton, U extends WebToggleButtonUI, D extends IDecoration<E, D>>
        extends AbstractButtonPainter<E, U, D> implements IToggleButtonPainter<E, U>
{
}