package com.alee.laf.button;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JButton component painters.
 *
 * @author Mikle Garin
 */

public interface ButtonPainter<E extends JButton, U extends WebButtonUI> extends AbstractButtonPainter<E, U>, SpecificPainter
{
}