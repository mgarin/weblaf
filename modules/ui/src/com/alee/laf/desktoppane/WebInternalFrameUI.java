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
import com.alee.api.annotations.Nullable;
import com.alee.managers.style.*;
import com.alee.painter.PainterSupport;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link JInternalFrame} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 */
public class WebInternalFrameUI<C extends JInternalFrame> extends WInternalFrameUI<C>
{
    /**
     * Listeners.
     */
    protected transient PropertyChangeListener rootPaneTracker;

    /**
     * Returns an instance of the {@link WebInternalFrameUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebInternalFrameUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebInternalFrameUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        super.installUI ( c );

        // Applying skin
        StyleManager.installSkin ( internalFrame );

        // Installing title pane
        if ( northPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) northPane ).install ();
        }

        // Root pane style updates
        updateRootPaneStyle ();
        rootPaneTracker = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( @NotNull final PropertyChangeEvent evt )
            {
                updateRootPaneStyle ();
            }
        };
        internalFrame.addPropertyChangeListener ( JInternalFrame.ROOT_PANE_PROPERTY, rootPaneTracker );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling listeners
        internalFrame.removePropertyChangeListener ( JInternalFrame.ROOT_PANE_PROPERTY, rootPaneTracker );

        // Uninstalling title pane
        if ( northPane instanceof WebInternalFrameTitlePane )
        {
            ( ( WebInternalFrameTitlePane ) northPane ).uninstall ();
        }

        // Uninstalling applied skin
        StyleManager.uninstallSkin ( internalFrame );

        super.uninstallUI ( c );
    }

    /**
     * Performs root pane style update.
     */
    protected void updateRootPaneStyle ()
    {
        StyleId.internalframeRootpane.at ( internalFrame ).set ( internalFrame.getRootPane () );
    }

    @NotNull
    @Override
    protected LayoutManager createLayoutManager ()
    {
        return new InternalFrameLayout ();
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
    public Dimension getMinimumSize ( @NotNull final JComponent c )
    {
        return null;
    }

    @Nullable
    @Override
    public Dimension getPreferredSize ( @NotNull final JComponent c )
    {
        return null;
    }
}