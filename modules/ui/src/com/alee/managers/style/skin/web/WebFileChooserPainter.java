package com.alee.managers.style.skin.web;

import com.alee.laf.filechooser.IFileChooserPainter;
import com.alee.laf.filechooser.WebFileChooserUI;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebFileChooserPainter<E extends JFileChooser, U extends WebFileChooserUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IFileChooserPainter<E, U>
{
}
