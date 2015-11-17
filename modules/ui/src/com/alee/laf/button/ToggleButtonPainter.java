package com.alee.laf.button;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JButton component painters.
 *
 * @author Mikle Garin
 */

public interface ToggleButtonPainter<E extends JToggleButton, U extends WebToggleButtonUI>
        extends AbstractButtonPainter<E, U>, SpecificPainter
{
}