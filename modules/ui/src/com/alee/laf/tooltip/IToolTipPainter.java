package com.alee.laf.tooltip;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JToolTip component painters.
 *
 * @author Alexandr Zernov
 */

public interface IToolTipPainter<E extends JToolTip, U extends WebToolTipUI> extends SpecificPainter<E, U>
{
}