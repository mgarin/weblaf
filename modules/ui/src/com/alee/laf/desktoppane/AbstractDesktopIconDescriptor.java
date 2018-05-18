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

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;

/**
 * Abstract descriptor for {@link JInternalFrame.JDesktopIcon} component.
 * Extend this class for creating custom {@link JInternalFrame.JDesktopIcon} descriptors.
 *
 * @param <C> {@link JComponent} type
 * @param <U> base {@link ComponentUI} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#initializeDescriptors()
 */
public abstract class AbstractDesktopIconDescriptor<C extends JInternalFrame.JDesktopIcon, U extends WebDesktopIconUI>
        extends AbstractComponentDescriptor<C, U>
{
    /**
     * Constructs new {@link AbstractDesktopIconDescriptor}.
     *
     * @param id             component identifier
     * @param componentClass component class
     * @param uiClassId      component UI class ID
     * @param baseUIClass    base UI class applicable to this component
     * @param uiClass        UI class applied to the component by default
     * @param defaultStyleId component default style ID
     */
    public AbstractDesktopIconDescriptor ( final String id, final Class<C> componentClass, final String uiClassId,
                                           final Class<U> baseUIClass, final Class<? extends U> uiClass, final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, defaultStyleId );
    }

    @Override
    public void updateUI ( final C component )
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