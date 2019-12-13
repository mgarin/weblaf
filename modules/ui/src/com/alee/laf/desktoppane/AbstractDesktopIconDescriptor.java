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

package com.alee.laf.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;

/**
 * Abstract descriptor for {@link javax.swing.JInternalFrame.JDesktopIcon} component.
 * Extend this class for creating custom {@link javax.swing.JInternalFrame.JDesktopIcon} descriptors.
 *
 * @param <C> {@link javax.swing.JInternalFrame.JDesktopIcon} type
 * @param <U> base {@link WDesktopIconUI} type
 * @param <P> {@link IDesktopIconPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractDesktopIconDescriptor<C extends JInternalFrame.JDesktopIcon, U extends WDesktopIconUI, P extends IDesktopIconPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractDesktopIconDescriptor}.
     *
     * @param id                  {@link javax.swing.JInternalFrame.JDesktopIcon} identifier
     * @param componentClass      {@link javax.swing.JInternalFrame.JDesktopIcon} {@link Class}
     * @param uiClassId           {@link WDesktopIconUI} {@link Class} identifier
     * @param baseUIClass         base {@link WDesktopIconUI} {@link Class} applicable to {@link javax.swing.JInternalFrame.JDesktopIcon}
     * @param uiClass             {@link WDesktopIconUI} {@link Class} used for {@link javax.swing.JInternalFrame.JDesktopIcon} by default
     * @param painterInterface    {@link IDesktopIconPainter} interface {@link Class}
     * @param painterClass        {@link IDesktopIconPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IDesktopIconPainter}
     * @param defaultStyleId      {@link javax.swing.JInternalFrame.JDesktopIcon} default {@link StyleId}
     */
    public AbstractDesktopIconDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                           @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                           @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                           @NotNull final Class<? extends P> painterClass,
                                           @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }

    @Override
    public void updateUI ( @NotNull final C component )
    {
        // Updating component UI
        super.updateUI ( component );

        // Invalidating component
        component.invalidate ();

        // Updating component size
        final Dimension ps = component.getPreferredSize ();
        component.setSize ( ps.width, ps.height );

        // Updating internal frame UI
        // Don't do this if UI not created yet
        final JInternalFrame internalFrame = component.getInternalFrame ();
        if ( internalFrame != null && internalFrame.getUI () != null )
        {
            SwingUtilities.updateComponentTreeUI ( internalFrame );
        }
    }
}