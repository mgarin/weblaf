package com.alee.managers.style.skin.web;

import com.alee.painter.AbstractPainter;
import com.alee.laf.optionpane.IOptionPanePainter;
import com.alee.laf.optionpane.WebOptionPaneUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebOptionPanePainter<E extends JOptionPane, U extends WebOptionPaneUI> extends AbstractPainter<E, U>
        implements IOptionPanePainter<E, U>
{
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
    }
}