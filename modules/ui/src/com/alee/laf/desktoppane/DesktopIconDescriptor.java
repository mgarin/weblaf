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
import java.awt.*;

/**
 * Custom descriptor for {@link JInternalFrame.JDesktopIcon} component.
 *
 * @author Mikle Garin
 */

public final class DesktopIconDescriptor extends AbstractComponentDescriptor<JInternalFrame.JDesktopIcon>
{
    /**
     * Constructs new descriptor for {@link JInternalFrame.JDesktopIcon} component.
     */
    public DesktopIconDescriptor ()
    {
        super ( "desktopicon", JInternalFrame.JDesktopIcon.class, "DesktopIconUI", WebDesktopIconUI.class, WebDesktopIconUI.class,
                StyleId.desktopicon );
    }

    @Override
    public void updateUI ( final JInternalFrame.JDesktopIcon component )
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