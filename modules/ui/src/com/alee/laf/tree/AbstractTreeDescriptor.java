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
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;

/**
 * Abstract descriptor for {@link JTree} component.
 * Extend this class for creating custom {@link JTree} descriptors.
 *
 * @param <C> {@link JTree} type
 * @param <U> base {@link WTreeUI} type
 * @param <P> {@link ITreePainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractTreeDescriptor<C extends JTree, U extends WTreeUI, P extends ITreePainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractTreeDescriptor}.
     *
     * @param id                  {@link JTree} identifier
     * @param componentClass      {@link JTree} {@link Class}
     * @param uiClassId           {@link WTreeUI} {@link Class} identifier
     * @param baseUIClass         base {@link WTreeUI} {@link Class} applicable to {@link JTree}
     * @param uiClass             {@link WTreeUI} {@link Class} used for {@link JTree} by default
     * @param painterInterface    {@link ITreePainter} interface {@link Class}
     * @param painterClass        {@link ITreePainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link ITreePainter}
     * @param defaultStyleId      {@link JTree} default {@link StyleId}
     */
    public AbstractTreeDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass, @NotNull final String uiClassId,
                                    @NotNull final Class<U> baseUIClass, @NotNull final Class<? extends U> uiClass,
                                    @NotNull final Class<P> painterInterface, @NotNull final Class<? extends P> painterClass,
                                    @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }

    @Override
    public void updateUI ( @NotNull final C component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Updating renderer UI
        final TreeCellRenderer renderer = component.getCellRenderer ();
        if ( renderer instanceof Component )
        {
            SwingUtilities.updateComponentTreeUI ( ( Component ) renderer );
        }

        // Updating editor UI
        final TreeCellEditor editor = component.getCellEditor ();
        if ( editor instanceof Component )
        {
            SwingUtilities.updateComponentTreeUI ( ( Component ) editor );
        }
        else if ( editor instanceof DefaultCellEditor )
        {
            final Component editorComponent = ( ( DefaultCellEditor ) editor ).getComponent ();
            if ( editorComponent != null )
            {
                SwingUtilities.updateComponentTreeUI ( editorComponent );
            }
        }
    }
}