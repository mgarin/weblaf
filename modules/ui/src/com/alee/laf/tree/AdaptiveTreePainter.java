package com.alee.laf.tree;

import com.alee.extended.painter.AdaptivePainter;
import com.alee.extended.painter.Painter;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.util.Hashtable;

/**
 * Simple TreePainter adapter class.
 * It is used to install simple non-specific painters into WebTreeUI.
 *
 * @author Alexandr Zernov
 */

public class AdaptiveTreePainter<E extends JTree, U extends WebTreeUI> extends AdaptivePainter<E, U> implements TreePainter<E, U>
{
    /**
     * Constructs new AdaptiveTreePainter for the specified painter.
     *
     * @param painter painter to adapt
     */
    public AdaptiveTreePainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public void prepareToPaint ( final Hashtable<TreePath, Boolean> drawingCache, final TreeCellRenderer currentCellRenderer )
    {
        // Ignore this method in adaptive class
    }
}