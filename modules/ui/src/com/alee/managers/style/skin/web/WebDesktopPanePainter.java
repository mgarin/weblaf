package com.alee.managers.style.skin.web;

import com.alee.laf.desktoppane.IDesktopPanePainter;
import com.alee.laf.desktoppane.WebDesktopPaneUI;
import com.alee.managers.style.skin.web.data.decoration.IDecoration;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebDesktopPanePainter<E extends JDesktopPane, U extends WebDesktopPaneUI, D extends IDecoration<E, D>>
        extends AbstractDecorationPainter<E, U, D> implements IDesktopPanePainter<E, U>
{
}