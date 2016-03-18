package com.alee.laf.tree;

import com.alee.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.util.Hashtable;

/**
 * Base interface for JTree component painters.
 *
 * @param <E> component type
 * @param <U> UI type
 * @author Alexandr Zernov
 */

public interface ITreePainter<E extends JTree, U extends WebTreeUI> extends SpecificPainter<E, U>
{
    /**
     * Returns whether or not hover node decoration is supported by this tree painter.
     *
     * @return true if hover node decoration is supported by this tree painter, false otherwise
     */
    public boolean isHoverDecorationSupported ();

    /**
     * Prepares painter to pain tree.
     *
     * @param drawingCache        vertical lines drawing cache
     * @param currentCellRenderer current cell renderer
     */
    public void prepareToPaint ( Hashtable<TreePath, Boolean> drawingCache, TreeCellRenderer currentCellRenderer );
}