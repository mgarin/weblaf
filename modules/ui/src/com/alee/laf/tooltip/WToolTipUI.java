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

package com.alee.laf.tooltip;

import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ToolTipUI;

/**
 * Pluggable look and feel interface for {@link com.alee.laf.tooltip.WebToolTip} component.
 *
 * @author Mikle Garin
 */

public abstract class WToolTipUI extends ToolTipUI
{
    /**
     * Runtime variables.
     */
    protected JComponent tooltip = null;

    @Override
    public void installUI ( final JComponent c )
    {
        // Saving tooltip to local variable
        tooltip = c;

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

        // Cleaning up reference
        this.tooltip = null;
    }

    /**
     * Returns component default font key.
     *
     * @return component default font key
     */
    protected String getFontKey ()
    {
        return "ToolTip.font";
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        if ( SwingUtils.isUIResource ( tooltip.getFont () ) )
        {
            tooltip.setFont ( UIManager.getFont ( getFontKey () ) );
        }
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LookAndFeel.uninstallBorder ( tooltip );
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