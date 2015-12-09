package com.alee.laf.desktoppane;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/**
 * Base interface for JInternalFrame component painters.
 *
 * @author Alexandr Zernov
 */

public interface IInternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI> extends SpecificPainter<E, U>
{
    /**
     * Prepares painter to paint internal frame.
     *
     * @param titlePane title pane
     */
    public void prepareToPaint ( BasicInternalFrameTitlePane titlePane );
}