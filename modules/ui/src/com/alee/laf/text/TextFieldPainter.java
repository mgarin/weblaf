package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.awt.*;

/**
 * Basic painter for {@link JTextField} component.
 * It is used as {@link WebTextFieldUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class TextFieldPainter<E extends JTextField, U extends WebTextFieldUI, D extends IDecoration<E, D>>
        extends AbstractTextFieldPainter<E, U, D> implements ITextFieldPainter<E, U>, SwingConstants
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }

    @Override
    public Component getTrailingComponent ()
    {
        return ui.getTrailingComponent ();
    }

    @Override
    public Component getLeadingComponent ()
    {
        return ui.getLeadingComponent ();
    }
}