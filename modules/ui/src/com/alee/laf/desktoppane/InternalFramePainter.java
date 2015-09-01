package com.alee.laf.desktoppane;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/**
 * Base interface for JInternalFrame component painters.
 *
 * @author Alexandr Zernov
 */

public interface InternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI> extends Painter<E, U>, SpecificPainter
{
    /**
     * Prepares painter to paint internal frame.
     *
     * @param titlePane title pane
     */
    public void prepareToPaint ( BasicInternalFrameTitlePane titlePane );
}