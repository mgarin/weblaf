/*
 * This file is part of WebLookAndFeel library.
 *
 * WebLookAndFeel library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * WebLookAndFeel library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with WebLookAndFeel library.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.alee.laf.tree;

import com.alee.api.annotations.NotNull;
import com.alee.painter.decoration.AbstractSectionDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.util.List;

/**
 * Simple tree drop location painter based on {@link AbstractSectionDecorationPainter}.
 * It is used within {@link TreePainter} to paint drop location on the tree.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */
public class TreeDropLocationPainter<C extends JTree, U extends WTreeUI, D extends IDecoration<C, D>>
        extends AbstractSectionDecorationPainter<C, U, D> implements ITreeDropLocationPainter<C, U>
{
    /**
     * Tree drop location to paint visual representation for.
     * It is passed into this painter right before painting operation call.
     */
    protected JTree.DropLocation location;

    @Override
    public String getSectionId ()
    {
        return "drop.location";
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( location != null )
        {
            states.add ( isDropBetween ( location ) ? DecorationState.dropBetween : DecorationState.dropOn );
        }
        return states;
    }

    @Override
    public void prepareToPaint ( final JTree.DropLocation location )
    {
        this.location = location;
        updateDecorationState ();
    }

    @Override
    protected boolean isDecorationAvailable ( @NotNull final D decoration )
    {
        // We don't need to paint anything when drop location is not available
        return location != null && super.isDecorationAvailable ( decoration );
    }

    @Override
    public Rectangle getDropViewBounds ( final JTree.DropLocation location )
    {
        return isDropBetween ( location ) ? getDropBetweenViewBounds ( location ) : getDropOnViewBounds ( location );
    }

    /**
     * Returns whether the specified drop location should be displayed as line or not.
     *
     * @param location drop location
     * @return true if the specified drop location should be displayed as line, false otherwise
     */
    protected boolean isDropBetween ( final JTree.DropLocation location )
    {
        return location != null && location.getPath () != null && location.getChildIndex () != -1;
    }

    /**
     * Returns drop ON view bounds.
     *
     * @param location drop location
     * @return drop ON view bounds
     */
    protected Rectangle getDropOnViewBounds ( final JTree.DropLocation location )
    {
        final TreePath dropPath = location.getPath ();
        return component.getPathBounds ( dropPath );
    }

    /**
     * Returns drop BETWEEN view bounds.
     *
     * @param location drop location
     * @return drop BETWEEN view bounds
     */
    protected Rectangle getDropBetweenViewBounds ( final JTree.DropLocation location )
    {
        final Rectangle rect;
        final TreePath path = location.getPath ();
        final int index = location.getChildIndex ();
        final Insets insets = component.getInsets ();
        final Dimension ps = getPreferredSize ();

        if ( component.getRowCount () == 0 )
        {
            rect = new Rectangle ( insets.left, insets.top, component.getWidth () - insets.left - insets.right, 0 );
        }
        else
        {
            final TreeModel model = component.getModel ();
            final Object root = model.getRoot ();
            if ( path.getLastPathComponent () == root && index >= model.getChildCount ( root ) )
            {
                rect = component.getRowBounds ( component.getRowCount () - 1 );
                rect.y = rect.y + rect.height;
                final Rectangle xRect;

                if ( !component.isRootVisible () )
                {
                    xRect = component.getRowBounds ( 0 );
                }
                else if ( model.getChildCount ( root ) == 0 )
                {
                    final int totalChildIndent = ui.getLeftChildIndent () + ui.getRightChildIndent ();
                    xRect = component.getRowBounds ( 0 );
                    xRect.x += totalChildIndent;
                    xRect.width -= totalChildIndent + totalChildIndent;
                }
                else
                {
                    final int lastIndex = model.getChildCount ( root ) - 1;
                    final Object lastObject = model.getChild ( root, lastIndex );
                    final TreePath lastChildPath = path.pathByAddingChild ( lastObject );
                    xRect = component.getPathBounds ( lastChildPath );
                }

                rect.x = xRect.x;
                rect.width = xRect.width;
            }
            else
            {
                rect = component.getPathBounds ( path.pathByAddingChild ( model.getChild ( path.getLastPathComponent (), index ) ) );
            }
        }

        if ( rect.y != 0 )
        {
            rect.y--;
        }

        if ( !ltr )
        {
            rect.x = rect.x + rect.width - ps.width;
        }

        rect.width = ps.width;
        rect.height = ps.height;

        return rect;
    }
}