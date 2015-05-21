package com.alee.laf.button;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.PartialDecoration;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import java.awt.*;

/**
 * Base interface for JButton component painters.
 *
 * @author Mikle Garin
 */

public interface ButtonPainter<E extends JButton, U extends WebButtonUI> extends Painter<E, U>, PartialDecoration, SpecificPainter
{
    /**
     * Sets button view bounds.
     *
     * @param rect button view bounds
     */
    public void setViewRect ( Rectangle rect );

    /**
     * Sets button text bounds.
     *
     * @param rect button text bounds
     */
    public void setTextRect ( Rectangle rect );

    /**
     * Sets button icon bounds.
     *
     * @param rect button icon bounds
     */
    public void setIconRect ( Rectangle rect );
}