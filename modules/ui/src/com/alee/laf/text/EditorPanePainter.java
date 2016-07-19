package com.alee.laf.text;

import com.alee.managers.language.LM;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Basic painter for {@link JEditorPane} component.
 * It is used as {@link WebEditorPaneUI} default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class EditorPanePainter<E extends JEditorPane, U extends WebEditorPaneUI, D extends IDecoration<E, D>>
        extends AbstractTextAreaPainter<E, U, D> implements IEditorPanePainter<E, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}