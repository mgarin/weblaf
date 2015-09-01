package com.alee.laf.toolbar;

import com.alee.extended.painter.SpecificPainter;
import com.alee.laf.separator.AbstractSeparatorPainter;

import javax.swing.*;

/**
 * Base interface for JSeparator component painters.
 *
 * @author Alexandr Zernov
 */

public interface ToolBarSeparatorPainter<E extends JSeparator, U extends WebToolBarSeparatorUI>
        extends AbstractSeparatorPainter<E, U>, SpecificPainter
{
}