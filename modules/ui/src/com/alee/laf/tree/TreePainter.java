package com.alee.laf.tree;

import com.alee.extended.painter.Painter;
import com.alee.extended.painter.SpecificPainter;

import javax.swing.*;
import javax.swing.tree.AbstractLayoutCache;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.util.Hashtable;

/**
 * Base interface for JTree component painters.
 *
 * @author Alexandr Zernov
 */

public interface TreePainter<E extends JTree, U extends WebTreeUI> extends Painter<E, U>, SpecificPainter
{
    /**
     * Prepares painter to pain tree.
     *
     * @param treeState           object represents state of tree
     * @param drawingCache        vertical lines drawing cache
     * @param currentCellRenderer current cell renderer
     */
    public void prepareToPaint ( AbstractLayoutCache treeState, Hashtable<TreePath, Boolean> drawingCache,
                                 TreeCellRenderer currentCellRenderer );
}
