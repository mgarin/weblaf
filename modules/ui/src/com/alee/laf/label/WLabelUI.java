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

package com.alee.laf.label;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.LabelUI;

/**
 * Pluggable look and feel interface for {@link javax.swing.JLabel} component.
 *
 * @author Mikle Garin
 */

public abstract class WLabelUI extends LabelUI
{
    /**
     * Runtime variables.
     */
    protected JLabel label;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving label reference
        label = ( JLabel ) c;

        // Installing default component settings
        installDefaults ();

        // Installing default component listeners
        installListeners ();
    }

    @Override
    public void uninstallUI ( final JComponent c )
    {
        // Uninstalling default component listeners
        uninstallListeners ();

        // Uninstalling default component settings
        uninstallDefaults ();

        // Removing label reference
        label = null;
    }

    /**
     * Returns component default font key.
     *
     * @return component default font key
     */
    protected String getFontKey ()
    {
        return "Label.font";
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        if ( SwingUtils.isUIResource ( label.getFont () ) )
        {
            label.setFont ( UIManager.getFont ( getFontKey () ) );
        }
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LookAndFeel.uninstallBorder ( label );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        // Do nothing by default
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        // Do nothing by default
    }
}