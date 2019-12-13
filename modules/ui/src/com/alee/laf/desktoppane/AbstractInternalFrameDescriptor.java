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
import com.alee.utils.ReflectUtils;

import javax.swing.*;

/**
 * Abstract descriptor for {@link JInternalFrame} component.
 * Extend this class for creating custom {@link JInternalFrame} descriptors.
 *
 * @param <C> {@link JInternalFrame} type
 * @param <U> base {@link WInternalFrameUI} type
 * @param <P> {@link IInternalFramePainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractInternalFrameDescriptor<C extends JInternalFrame, U extends WInternalFrameUI, P extends IInternalFramePainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractInternalFrameDescriptor}.
     *
     * @param id                  {@link JInternalFrame} identifier
     * @param componentClass      {@link JInternalFrame} {@link Class}
     * @param uiClassId           {@link WInternalFrameUI} {@link Class} identifier
     * @param baseUIClass         base {@link WInternalFrameUI} {@link Class} applicable to {@link JInternalFrame}
     * @param uiClass             {@link WInternalFrameUI} {@link Class} used for {@link JInternalFrame} by default
     * @param painterInterface    {@link IInternalFramePainter} interface {@link Class}
     * @param painterClass        {@link IInternalFramePainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IInternalFramePainter}
     * @param defaultStyleId      {@link JInternalFrame} default {@link StyleId}
     */
    public AbstractInternalFrameDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
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

        // Updating frame icon UI
        final JInternalFrame.JDesktopIcon desktopIcon = component.getDesktopIcon ();
        if ( desktopIcon != null )
        {
            ReflectUtils.callMethodSafely ( desktopIcon, "updateUIWhenHidden" );
        }
    }
}