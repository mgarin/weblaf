package com.alee.laf.toolbar;

import com.alee.laf.separator.IAbstractSeparatorPainter;

import javax.swing.*;

/**
 * Base interface for JSeparator component painters.
 *
 * @author Alexandr Zernov
 */

public interface IToolBarSeparatorPainter<E extends JToolBar.Separator, U extends WebToolBarSeparatorUI>
        extends IAbstractSeparatorPainter<E, U>
{
}