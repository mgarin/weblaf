package com.alee.laf.desktoppane;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicInternalFrameTitlePane;

/**
 * Simple InternalFramePainter adapter class.
 * It is used to install simple non-specific painters into WebOptionPaneUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveInternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI> extends AdaptivePainter<E, U>
        implements InternalFramePainter<E, U>
{
    /**
     * Constructs new AdaptiveInternalFramePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveInternalFramePainter ( final Painter painter )
    {
        super ( painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareToPaint ( final BasicInternalFrameTitlePane titlePane )
    {
        // Ignore this method in adaptive class
    }
}
