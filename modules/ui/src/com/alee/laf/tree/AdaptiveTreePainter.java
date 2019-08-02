package com.alee.laf.tree;

import com.alee.painter.AdaptivePainter;
import com.alee.painter.Painter;

import javax.swing.*;
import javax.swing.tree.TreeCellRenderer;
import javax.swing.tree.TreePath;
import java.util.Hashtable;

/**
 * Simple {@link TreePainter} adapter class.
 * It is used to install simple non-specific painters into {@link WTreeUI}.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @author Alexandr Zernov
 */
public final class AdaptiveTreePainter<C extends JTree, U extends WTreeUI> extends AdaptivePainter<C, U> implements ITreePainter<C, U>
{
    /**
     * Constructs new {@link AdaptiveTreePainter} for the specified painter.
     *
     * @param painter {@link Painter} to adapt
     */
    public AdaptiveTreePainter ( final Painter painter )
    {
        super ( painter );
    }

    @Override
    public boolean isRowHoverDecorationSupported ()
    {
        return false;
    }

    @Override
    public void prepareToPaint ( final Hashtable<TreePath, Boolean> drawingCache, final TreeCellRenderer currentCellRenderer )
    {
        // Ignore this method in adaptive class
    }
}