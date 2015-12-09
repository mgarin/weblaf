package com.alee.managers.style.skin.web;

import com.alee.painter.AbstractPainter;
import com.alee.laf.desktoppane.IDesktopIconPainter;
import com.alee.laf.desktoppane.WebDesktopIconUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebDesktopIconPainter<E extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI> extends AbstractPainter<E, U>
        implements IDesktopIconPainter<E, U>
{
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
    }
}