package com.alee.laf.text;

import com.alee.api.annotations.Nullable;
import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;

/**
 * Basic painter for {@link JFormattedTextField} component.
 * It is used as {@link WFormattedTextFieldUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class FormattedTextFieldPainter<C extends JFormattedTextField, U extends WFormattedTextFieldUI, D extends IDecoration<C, D>>
        extends AbstractTextFieldPainter<C, U, D> implements IFormattedTextFieldPainter<C, U>, SwingConstants
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