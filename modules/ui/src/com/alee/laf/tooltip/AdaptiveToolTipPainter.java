package com.alee.laf.tooltip;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.laf.separator.SeparatorPainter;

import javax.swing.*;

/**
 * Simple ToolTipPainter adapter class.
 * It is used to install simple non-specific painters into WebToolTipUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveToolTipPainter<E extends JSeparator, U extends WebSeparatorUI, P extends Painter & SpecificPainter>
        extends AdaptivePainter<E, U, P> implements SeparatorPainter<E, U>
{
}
