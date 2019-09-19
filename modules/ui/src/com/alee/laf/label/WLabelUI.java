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

import com.alee.api.annotations.NotNull;
import com.alee.laf.WebUI;
import com.alee.utils.LafUtils;

import javax.swing.*;
import javax.swing.plaf.LabelUI;

/**
 * Pluggable look and feel interface for {@link JLabel} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public abstract class WLabelUI<C extends JLabel> extends LabelUI implements WebUI<C>
{
    /**
     * {@link LabelInputListener} for the {@link JLabel}.
     */
    protected LabelInputListener<C> inputListener;

    /**
     * Runtime variables.
     */
    protected C label;

    @NotNull
    @Override
    public String getPropertyPrefix ()
    {
        return "Label.";
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Saving component reference
        label = ( C ) c;

        // Installing default component settings
        installDefaults ();

        // Installing default component listeners
        installListeners ();
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling default component listeners
        uninstallListeners ();

        // Uninstalling default component settings
        uninstallDefaults ();

        // Removing component reference
        label = null;
    }

    /**
     * Installs default component settings.
     */
    protected void installDefaults ()
    {
        LafUtils.installDefaults ( label, getPropertyPrefix () );
    }

    /**
     * Uninstalls default component settings.
     */
    protected void uninstallDefaults ()
    {
        LafUtils.uninstallDefaults ( label );
    }

    /**
     * Installs default component listeners.
     */
    protected void installListeners ()
    {
        inputListener = createLabelInputListener ();
        inputListener.install ( label );
    }

    /**
     * Uninstalls default component listeners.
     */
    protected void uninstallListeners ()
    {
        inputListener.uninstall ( label );
        inputListener = null;
    }

    /**
     * Returns {@link LabelInputListener} for the {@link JLabel}.
     *
     * @return {@link LabelInputListener} for the {@link JLabel}
     */
    protected LabelInputListener<C> createLabelInputListener ()
    {
        return new WLabelInputListener<C, WLabelUI<C>> ();
    }
}