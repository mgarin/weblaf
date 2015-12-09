package com.alee.managers.style.skin.web;

import com.alee.extended.layout.ToolbarLayout;
import com.alee.laf.menu.IMenuBarPainter;
import com.alee.laf.menu.WebMenuBarUI;

import javax.swing.*;

/**
 * @author Alexandr Zernov
 */

public class WebMenuBarPainter<E extends JMenuBar, U extends WebMenuBarUI> extends AbstractDecorationPainter<E, U>
        implements IMenuBarPainter<E, U>
{
    @Override
    public void install ( final E c, final U ui )
    {
        super.install ( c, ui );
        component.setLayout ( new ToolbarLayout ( 0 ) );
    }
}