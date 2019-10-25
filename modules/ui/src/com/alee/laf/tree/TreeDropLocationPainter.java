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
import com.alee.api.annotations.Nullable;
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
    @Nullable
    protected JTree.DropLocation location;

    @Override
    public String getSectionId ()
    {
        return "drop.location";
    }

    @Nullable
    @Override
    public Rectangle getDropViewBounds ( @NotNull final JTree.DropLocation location )
    {
        Rectangle dropViewBounds = null;
        if ( component.getRowCount () > 0 )
        {
            final TreeModel model = component.getModel ();
            final TreePath path = location.getPath ();
            if ( model != null && path != null )
            {
                if ( isDropBetween ( location, model, path ) )
                {
                    dropViewBounds = getDropBetweenViewBounds ( location, model, path, location.getChildIndex () );
                }
                else
                {
                    dropViewBounds = getDropOnViewBounds ( location, model, path );
                }
            }
        }
        return dropViewBounds;
    }

    /**
     * Returns whether the specified drop location should be displayed as line or not.
     *
     * @param location {@link javax.swing.JTree.DropLocation}
     * @param model    {@link TreeModel}
     * @param path     {@link TreePath}
     * @return true if the specified drop location should be displayed as line, false otherwise
     */
    protected boolean isDropBetween ( @NotNull final JTree.DropLocation location, @NotNull final TreeModel model,
                                      @NotNull final TreePath path )
    {
        return location.getChildIndex () != -1 && model.getChildCount ( path.getLastPathComponent () ) > 0;
    }

    /**
     * Returns drop ON view bounds.
     * Might return {@code null} if {@link TreePath} bounds are not available.
     *
     * @param location {@link javax.swing.JTree.DropLocation}
     * @param model    {@link TreeModel}
     * @param path     drop {@link TreePath}
     * @return drop ON view bounds
     */
    @Nullable
    protected Rectangle getDropOnViewBounds ( @NotNull final JTree.DropLocation location, @NotNull final TreeModel model,
                                              @NotNull final TreePath path )
    {
        return component.getPathBounds ( path );
    }

    /**
     * Returns drop BETWEEN view bounds.
     *
     * @param location {@link javax.swing.JTree.DropLocation}
     * @param model    {@link TreeModel}
     * @param path     parent {@link TreePath}
     * @param index    drop index at parent {@link TreePath}
     * @return drop BETWEEN view bounds
     */
    @Nullable
    protected Rectangle getDropBetweenViewBounds ( @NotNull final JTree.DropLocation location, @NotNull final TreeModel model,
                                                   @NotNull final TreePath path, final int index )
    {
        final Object parent = path.getLastPathComponent ();
        final int childCount = model.getChildCount ( parent );

        // Retrieving actual child index
        final int actualIndex;
        final boolean atStart;
        if ( index < childCount )
        {
            // Simply using child at [index] as it exists
            actualIndex = index;
            atStart = true;
        }
        else
        {
            // Using child at [childCount-1], it should be the same as [index-1] but safer
            actualIndex = childCount - 1;
            atStart = false;
        }

        // Retrieving child bounds
        final Object child = model.getChild ( parent, actualIndex );
        final TreePath childPath = path.pathByAddingChild ( child );
        final Rectangle bounds = component.getPathBounds ( childPath );

        // Adjusting drop indicator bounds
        if ( bounds != null )
        {
            // Adjusting for RTL
            final Dimension ps = getPreferredSize ();
            if ( !ltr )
            {
                bounds.x = bounds.x + bounds.width - ps.width;
            }

            // Adjusting to start or end of the node bounds
            if ( atStart )
            {
                bounds.y -= ps.height / 2;
            }
            else
            {
                bounds.y += bounds.height - ps.height / 2;
            }

            // Simply using preferred width and height
            // We want preferred sizes to avoid painting extremely long or wide location indicator
            bounds.width = ps.width;
            bounds.height = ps.height;
        }

        return bounds;
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();
        if ( location != null )
        {
            final TreeModel model = component.getModel ();
            final TreePath path = location.getPath ();
            if ( model != null && path != null )
            {
                states.add ( isDropBetween ( location, model, path ) ? DecorationState.dropBetween : DecorationState.dropOn );
            }
        }
        return states;
    }

    @Override
    public void prepareToPaint ( @NotNull final JTree.DropLocation location )
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
}