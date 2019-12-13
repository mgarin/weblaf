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

package com.alee.laf.list;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract descriptor for {@link JList} component.
 * Extend this class for creating custom {@link JList} descriptors.
 *
 * @param <C> {@link JList} type
 * @param <U> base {@link WListUI} type
 * @param <P> {@link IListPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractListDescriptor<C extends JList, U extends WListUI, P extends IListPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractListDescriptor}.
     *
     * @param id                  {@link JList} identifier
     * @param componentClass      {@link JList} {@link Class}
     * @param uiClassId           {@link WListUI} {@link Class} identifier
     * @param baseUIClass         base {@link WListUI} {@link Class} applicable to {@link JList}
     * @param uiClass             {@link WListUI} {@link Class} used for {@link JList} by default
     * @param painterInterface    {@link IListPainter} interface {@link Class}
     * @param painterClass        {@link IListPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IListPainter}
     * @param defaultStyleId      {@link JList} default {@link StyleId}
     */
    public AbstractListDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass, @NotNull final String uiClassId,
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
        final ListCellRenderer renderer = component.getCellRenderer ();
        if ( renderer instanceof Component )
        {
            SwingUtilities.updateComponentTreeUI ( ( Component ) renderer );
        }
    }
}