package com.alee.managers.style.skin.web;

import com.alee.laf.desktoppane.IDesktopPanePainter;
import com.alee.laf.desktoppane.WebDesktopPaneUI;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Web-style painter for JDesktopPane component.
 * It is used as WebDesktopPaneUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class WebDesktopPanePainter<E extends JDesktopPane, U extends WebDesktopPaneUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IDesktopPanePainter<E, U>
{
}