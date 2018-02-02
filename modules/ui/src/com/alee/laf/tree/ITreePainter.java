package com.alee.laf.tree;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.util.Hashtable;

/**
 * Base interface for {@link JTree} component painters.
 *
 * @param <C> component type
 * @param <U> UI type
 * @author Alexandr Zernov
 */

public interface ITreePainter<C extends JTree, U extends WTreeUI> extends SpecificPainter<C, U>
{
    /**
     * Returns whether or not row hover decoration is supported by this tree painter.
     *
     * @return {@code true} if row hover decoration is supported by this tree painter, {@code false} otherwise
     */
    public boolean isRowHoverDecorationSupported ();

    /**
     * Prepares painter to pain tree.
     *
     * @param drawingCache        vertical lines drawing cache
     * @param currentCellRenderer current cell renderer
     */
    public void prepareToPaint ( Hashtable<TreePath, Boolean> drawingCache, TreeCellRenderer currentCellRenderer );
}