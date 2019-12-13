package com.alee.laf.tree;

import com.alee.painter.ParameterizedPaint;
import com.alee.painter.SpecificPainter;

import javax.swing.*;

/**
 * Base interface for {@link JTree} component painters.
 *
 * @param <C> component type
 * @param <U> UI type
 * @author Alexandr Zernov
 */
public interface ITreePainter<C extends JTree, U extends WTreeUI> extends SpecificPainter<C, U>, ParameterizedPaint<TreePaintParameters>
{
    /**
     * Returns whether or not row hover decoration is supported by this tree painter.
     *
     * @return {@code true} if row hover decoration is supported by this tree painter, {@code false} otherwise
     */
    public boolean isRowHoverDecorationSupported ();
}