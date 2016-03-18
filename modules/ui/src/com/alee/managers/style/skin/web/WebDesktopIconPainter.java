package com.alee.managers.style.skin.web;

import com.alee.laf.desktoppane.IDesktopIconPainter;
import com.alee.laf.desktoppane.WebDesktopIconUI;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;

/**
 * Web-style painter for JDesktopIcon component.
 * It is used as WebDesktopIconUI default painter.
 *
 * @param <E> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Alexandr Zernov
 */

public class WebDesktopIconPainter<E extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI, D extends IDecoration<E, D>>
        extends AbstractContainerPainter<E, U, D> implements IDesktopIconPainter<E, U>
{
}