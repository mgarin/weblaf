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

package com.alee.laf.progressbar;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link JProgressBar} component.
 *
 * Basic UI usage have been removed due to:
 * 1. Multiple private things which do not work properly (like progress update on state change)
 * 2. Pointless animator which is not useful as we use our own ones
 * 3. Unnecessary settings initialization
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebProgressBarUI<C extends JProgressBar> extends WProgressBarUI<C>
{
    /**
     * Runtime variables.
     */
    protected transient EventsHandler eventsHandler;

    /**
     * Returns an instance of the {@link WebProgressBarUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebProgressBarUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebProgressBarUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( progressBar );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( progressBar );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installListeners ()
    {
        // Installing default listeners
        super.installListeners ();

        // Installing custom listeners
        eventsHandler = new EventsHandler ();
        progressBar.addChangeListener ( eventsHandler );
        progressBar.addPropertyChangeListener ( eventsHandler );
    }

    @Override
    protected void uninstallListeners ()
    {
        // Uninstalling custom listeners
        progressBar.removeChangeListener ( eventsHandler );
        progressBar.removePropertyChangeListener ( eventsHandler );
        eventsHandler = null;

        // Uninstalling default listeners
        super.uninstallListeners ();
    }

    @Override
    public boolean contains ( @NotNull final JComponent c, final int x, final int y )
    {
        return PainterSupport.contains ( c, this, x, y );
    }

    @Override
    public int getBaseline ( @NotNull final JComponent c, final int width, final int height )
    {
        return PainterSupport.getBaseline ( c, this, width, height );
    }

    @NotNull
    @Override
    public Component.BaselineResizeBehavior getBaselineResizeBehavior ( @NotNull final JComponent c )
    {
        return PainterSupport.getBaselineResizeBehavior ( c, this );
    }

    @Override
    public void paint ( @NotNull final Graphics g, @NotNull final JComponent c )
    {
        PainterSupport.paint ( g, c, this );
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return PainterSupport.getPreferredSize ( c );
    }

    /**
     * Events handler replacing {@code javax.swing.plaf.basic.BasicProgressBarUI.Handler} one.
     */
    protected class EventsHandler implements ChangeListener, PropertyChangeListener
    {
        @Override
        public void stateChanged ( @NotNull final ChangeEvent e )
        {
            progressBar.repaint ();
        }

        @Override
        public void propertyChange ( @NotNull final PropertyChangeEvent e )
        {
            final String propertyName = e.getPropertyName ();
            if ( Objects.equals ( propertyName, WebProgressBar.INDETERMINATE_PROPERTY ) )
            {
                progressBar.repaint ();
            }
        }
    }
}