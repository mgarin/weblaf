package com.alee.laf.text;

import com.alee.api.annotations.Nullable;
import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;

/**
 * Basic painter for {@link JPasswordField} component.
 * It is used as {@link WPasswordFieldUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class PasswordFieldPainter<C extends JPasswordField, U extends WPasswordFieldUI, D extends IDecoration<C, D>>
        extends AbstractTextFieldPainter<C, U, D> implements IPasswordFieldPainter<C, U>, SwingConstants
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }

    @Nullable
    @Override
    public Component getLeadingComponent ()
    {
        return ui.getLeadingComponent ();
    }

    @Nullable
    @Override
    public Component getTrailingComponent ()
    {
        return ui.getTrailingComponent ();
    }
}