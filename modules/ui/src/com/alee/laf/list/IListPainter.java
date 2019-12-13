package com.alee.laf.list;

import com.alee.painter.ParameterizedPaint;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JList} component painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public interface IListPainter<C extends JList, U extends WListUI> extends SpecificPainter<C, U>, ParameterizedPaint<ListPaintParameters>
{
    /**
     * Returns whether or not item hover decoration is supported by this list painter.
     *
     * @return {@code true} if item hover decoration is supported by this list painter, {@code false} otherwise
     */
    public boolean isItemHoverDecorationSupported ();
}