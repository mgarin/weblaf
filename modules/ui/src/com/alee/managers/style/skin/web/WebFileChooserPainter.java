package com.alee.managers.style.skin.web;

import com.alee.laf.filechooser.FileChooserPainter;
import com.alee.laf.filechooser.WebFileChooserUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebFileChooserPainter<E extends JFileChooser, U extends WebFileChooserUI> extends WebDecorationPainter<E, U>
        implements FileChooserPainter<E, U>
{
}
