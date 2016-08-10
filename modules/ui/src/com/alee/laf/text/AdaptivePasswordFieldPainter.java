package com.alee.laf.text;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import java.awt.*;

/**
 * Simple {@link PasswordFieldPainter} adapter class.
 * It is used to install simple non-specific painters into {@link WPasswordFieldUI}.
 *
 * @author Alexandr Zernov
 */

public final class AdaptivePasswordFieldPainter<E extends JPasswordField, U extends WPasswordFieldUI> extends AdaptivePainter<E, U>
        implements IPasswordFieldPainter<E, U>
{
    /**
     * Constructs new {@link AdaptivePasswordFieldPainter} for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptivePasswordFieldPainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public String getInputPrompt ()
    {
        return null;
    }

    @Override
    public boolean isInputPromptVisible ()
    {
        return false;
    }

    @Override
    public Component getLeadingComponent ()
    {
        return null;
    }

    @Override
    public Component getTrailingComponent ()
    {
        return null;
    }
}