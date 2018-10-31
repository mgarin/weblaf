package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JEditorPane} component.
 * It is used as {@link WEditorPaneUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */
public class EditorPanePainter<C extends JEditorPane, U extends WEditorPaneUI, D extends IDecoration<C, D>>
        extends AbstractTextAreaPainter<C, U, D> implements IEditorPanePainter<C, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}