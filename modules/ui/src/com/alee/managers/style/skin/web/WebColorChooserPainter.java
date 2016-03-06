package com.alee.managers.style.skin.web;

import com.alee.laf.colorchooser.IColorChooserPainter;
import com.alee.laf.colorchooser.WebColorChooserUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebColorChooserPainter<E extends JColorChooser, U extends WebColorChooserUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IColorChooserPainter<E, U>
{
}