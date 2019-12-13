package com.alee.laf.table;

import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JTable} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ITablePainter<C extends JTable, U extends WTableUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}