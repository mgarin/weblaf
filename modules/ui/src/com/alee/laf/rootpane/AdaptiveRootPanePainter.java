package com.alee.laf.rootpane;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;

/**
 * Simple {@link RootPanePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WRootPaneUI}.
 *
 * @author Alexandr Zernov
 */

public final class AdaptiveRootPanePainter<E extends JRootPane, U extends WRootPaneUI> extends AdaptivePainter<E, U>
        implements IRootPanePainter<E, U>
{
    /**
     * Constructs new {@link AdaptiveRootPanePainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveRootPanePainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public boolean isDecorated ()
    {
        return false;
    }
}