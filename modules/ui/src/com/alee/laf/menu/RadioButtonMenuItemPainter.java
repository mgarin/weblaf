package com.alee.laf.menu;

import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JMenuItem component painters.
 *
 * @author Alexandr Zernov
 */

public interface RadioButtonMenuItemPainter<E extends JMenuItem, U extends WebRadioButtonMenuItemUI>
        extends AbstractMenuItemPainter<E, U>, SpecificPainter
{
}