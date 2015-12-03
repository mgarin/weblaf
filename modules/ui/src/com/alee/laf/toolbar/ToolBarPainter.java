package com.alee.laf.toolbar;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JToolBar component painters.
 *
 * @author Alexandr Zernov
 */

public interface ToolBarPainter<E extends JToolBar, U extends WebToolBarUI> extends SpecificPainter<E, U>
{
}