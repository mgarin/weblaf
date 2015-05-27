package com.alee.extended.checkbox;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple TrisateCheckBoxPainter adapter class.
 * It is used to install simple non-specific painters into WebTristateCheckBoxUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveTrisateCheckBoxPainter<E extends JCheckBox, U extends WebTristateCheckBoxUI> extends AdaptivePainter<E, U>
        implements TrisateCheckBoxPainter<E, U>
{
    /**
     * Constructs new AdaptiveTrisateCheckBoxPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTrisateCheckBoxPainter ( final Painter painter )
    {
        super ( painter );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getIconRect ()
    {
        return null;
    }
}
