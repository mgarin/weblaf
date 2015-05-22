package com.alee.laf.toolbar;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for JToolBar component painters.
 *
 * @author Alexandr Zernov
 */

public interface ToolBarPainter<E extends JToolBar, U extends WebToolBarUI> extends Painter<E, U>, SpecificPainter
{
    /**
     *
     * @param floating
     */
    public void setFloating ( boolean floating );
}
