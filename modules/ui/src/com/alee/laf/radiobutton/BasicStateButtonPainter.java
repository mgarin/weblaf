package com.alee.laf.radiobutton;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import java.awt.*;

/**
 * Base interface for JCheckBox and JRadioButton component painters.
 *
 * @author Alexandr Zernov
 */

public interface BasicStateButtonPainter<E extends AbstractButton, U extends BasicRadioButtonUI> extends SpecificPainter<E, U>
{
    /**
     * Returns icon bounds.
     *
     * @return icon bounds
     */
    public Rectangle getIconRect ();
}