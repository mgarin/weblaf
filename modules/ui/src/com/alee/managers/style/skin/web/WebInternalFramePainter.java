package com.alee.managers.style.skin.web;

import com.alee.laf.desktoppane.IInternalFramePainter;
import com.alee.laf.desktoppane.WebInternalFrameUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * Web-style painter for JInternalFrame component.
 * It is used as WebInternalFrameUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class WebInternalFramePainter<E extends JInternalFrame, U extends WebInternalFrameUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IInternalFramePainter<E, U>
{
}