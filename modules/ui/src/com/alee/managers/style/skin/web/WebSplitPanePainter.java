package com.alee.managers.style.skin.web;

import com.alee.laf.splitpane.ISplitPanePainter;
import com.alee.laf.splitpane.WebSplitPaneUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebSplitPanePainter<E extends JSplitPane, U extends WebSplitPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements ISplitPanePainter<E, U>
{
}