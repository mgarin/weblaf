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

package com.alee.extended.split;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.jdk.Objects;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link WebMultiSplitPane} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public class WebMultiSplitPaneUI<C extends WebMultiSplitPane> extends WMultiSplitPaneUI<C> implements PropertyChangeListener
{
    /**
     * Returns an instance of the {@link WebMultiSplitPaneUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebMultiSplitPaneUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebMultiSplitPaneUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( multisplitpane );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( multisplitpane );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installListeners ()
    {
        // Installing default listeners
        super.installListeners ();

        // Instaling custom listeners
        multisplitpane.addPropertyChangeListener ( this );
    }

    @Override
    protected void uninstallListeners ()
    {
        // Uninstaling custom listeners
        multisplitpane.removePropertyChangeListener ( this );

        // Uninstalling default listeners
        super.uninstallListeners ();
    }

    @Override
    public void propertyChange ( @NotNull final PropertyChangeEvent evt )
    {
        final String property = evt.getPropertyName ();
        if ( Objects.equals ( property, WebMultiSplitPane.DIVIDER_SIZE_PROPERTY ) )
        {
            // Updating split pane layout
            multisplitpane.revalidate ();
            multisplitpane.repaint ();
        }
        else if ( Objects.equals ( property, WebMultiSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY ) )
        {
            // Collapsing expanded view upon changes
            if ( !multisplitpane.isOneTouchExpandable () && multisplitpane.isAnyViewExpanded () )
            {
                multisplitpane.collapseExpandedView ();
            }
        }
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
}