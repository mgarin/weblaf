package com.alee.managers.style.skin.web;

import com.alee.extended.painter.AbstractPainter;
import com.alee.laf.colorchooser.ColorChooserPainter;
import com.alee.laf.colorchooser.WebColorChooserUI;

import javax.swing.*;
import java.awt.*;

/**
 * @author Alexandr Zernov
 */

public class WebColorChooserPainter<E extends JColorChooser, U extends WebColorChooserUI> extends AbstractPainter<E, U>
        implements ColorChooserPainter<E, U>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void paint ( final Graphics2D g2d, final Rectangle bounds, final E c, final U ui )
    {
    }
}
