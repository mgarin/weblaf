package com.alee.managers.style.skin.web;

import com.alee.laf.desktoppane.IInternalFramePainter;
import com.alee.laf.desktoppane.WebInternalFrameUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebInternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IInternalFramePainter<E, U>
{
}