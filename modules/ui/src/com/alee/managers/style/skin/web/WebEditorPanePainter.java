package com.alee.managers.style.skin.web;

import com.alee.laf.text.IEditorPanePainter;
import com.alee.laf.text.WebEditorPaneUI;
import com.alee.managers.language.LM;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebEditorPanePainter<E extends JEditorPane, U extends WebEditorPaneUI, D extends IDecoration<E, D>>
        extends AbstractTextAreaPainter<E, U, D> implements IEditorPanePainter<E, U>
{
    @Override
    public String getInputPrompt ()
    {
        return LM.get ( ui.getInputPrompt () );
    }
}