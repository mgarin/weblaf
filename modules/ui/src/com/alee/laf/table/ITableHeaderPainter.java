package com.alee.laf.table;

import com.alee.painter.SpecificPainter;

import javax.swing.table.JTableHeader;

/**
 * Base interface for {@link JTableHeader} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface ITableHeaderPainter<C extends JTableHeader, U extends WTableHeaderUI> extends SpecificPainter<C, U>
{
    /**
     * This interface doesn't offer any additional methods to implement.
     */
}