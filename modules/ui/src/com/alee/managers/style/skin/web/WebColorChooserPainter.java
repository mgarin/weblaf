package com.alee.managers.style.skin.web;

import com.alee.laf.colorchooser.ColorChooserPainter;
import com.alee.laf.colorchooser.WebColorChooserUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebColorChooserPainter<E extends JColorChooser, U extends WebColorChooserUI> extends WebDecorationPainter<E, U>
        implements ColorChooserPainter<E, U>
{
}