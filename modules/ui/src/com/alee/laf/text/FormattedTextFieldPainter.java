package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;

/**
 * Basic painter for {@link JFormattedTextField} component.
 * It is used as {@link WebFormattedTextFieldUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class FormattedTextFieldPainter<E extends JFormattedTextField, U extends WFormattedTextFieldUI, D extends IDecoration<E, D>>
        extends AbstractTextFieldPainter<E, U, D> implements IFormattedTextFieldPainter<E, U>, SwingConstants
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }

    @Override
    public Component getLeadingComponent ()
    {
        return ui.getLeadingComponent ();
    }

    @Override
    public Component getTrailingComponent ()
    {
        return ui.getTrailingComponent ();
    }
}