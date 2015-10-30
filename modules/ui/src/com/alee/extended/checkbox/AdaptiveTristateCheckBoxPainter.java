package com.alee.extended.checkbox;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple TristateCheckBoxPainter adapter class.
 * It is used to install simple non-specific painters into WebTristateCheckBoxUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveTristateCheckBoxPainter<E extends JCheckBox, U extends WebTristateCheckBoxUI> extends AdaptivePainter<E, U>
        implements TristateCheckBoxPainter<E, U>
{
    /**
     * Constructs new AdaptiveTristateCheckBoxPainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTristateCheckBoxPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public Rectangle getIconRect ()
    {
        return null;
    }
}