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
import com.alee.api.data.Orientation;
import com.alee.extended.behavior.VisibilityBehavior;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleManager;
import com.alee.painter.PainterSupport;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import javax.swing.plaf.ComponentUI;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * Custom UI for {@link WebMultiSplitPaneDivider} component.
 *
 * @param <C> component type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public class WebMultiSplitPaneDividerUI<C extends WebMultiSplitPaneDivider> extends WMultiSplitPaneDividerUI<C>
{
    /**
     * Button for quickly toggling the left component.
     */
    protected WebButton leftOneTouchButton;

    /**
     * Button for quickly toggling the right component.
     */
    protected WebButton rightOneTouchButton;

    /**
     * Listeners.
     */
    protected transient PropertyChangeListener multiSplitPanePropertyListener;
    protected transient MultiSplitExpansionListener multiSplitExpansionListener;
    protected transient VisibilityBehavior<WebMultiSplitPaneDivider> visibilityBehavior;
    protected transient MouseAdapter dragListener;

    /**
     * Runtime variables.
     */
    protected transient boolean dragged = false;

    /**
     * Returns an instance of the {@link WebMultiSplitPaneDividerUI} for the specified component.
     * This tricky method is used by {@link UIManager} to create component UIs when needed.
     *
     * @param c component that will use UI instance
     * @return instance of the {@link WebMultiSplitPaneDividerUI}
     */
    @NotNull
    public static ComponentUI createUI ( @NotNull final JComponent c )
    {
        return new WebMultiSplitPaneDividerUI ();
    }

    @Override
    public void installUI ( @NotNull final JComponent c )
    {
        // Installing UI
        super.installUI ( c );

        // Installing layout
        divider.setLayout ( new MultiSplitPaneDividerLayout () );

        // Applying skin
        StyleManager.installSkin ( divider );
    }

    @Override
    public void uninstallUI ( @NotNull final JComponent c )
    {
        // Uninstalling applied skin
        StyleManager.uninstallSkin ( divider );

        // Uninstalling layout
        divider.setLayout ( null );

        // Uninstalling UI
        super.uninstallUI ( c );
    }

    @Override
    protected void installDefaults ()
    {
        super.installDefaults ();
        updateCursor ();
        updateOneTouchButtons ();
    }

    @Override
    protected void installListeners ()
    {
        // Installing default listeners
        super.installListeners ();

        // Installing multi split pane property change listener
        multiSplitPanePropertyListener = new PropertyChangeListener ()
        {
            @Override
            public void propertyChange ( @NotNull final PropertyChangeEvent event )
            {
                if ( event.getPropertyName ().equals ( WebMultiSplitPane.ORIENTATION_PROPERTY ) )
                {
                    updateCursor ();
                    updateOneTouchButtons ();
                }
                else if ( event.getPropertyName ().equals ( WebMultiSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY ) )
                {
                    updateOneTouchButtons ();
                }
            }
        };
        divider.getMultiSplitPane ().addPropertyChangeListener ( multiSplitPanePropertyListener );

        // Installing expansion listener
        multiSplitExpansionListener = new MultiSplitExpansionListener ()
        {
            @Override
            public void viewExpanded ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final Component view )
            {
                updateCursor ();
            }

            @Override
            public void viewCollapsed ( @NotNull final WebMultiSplitPane multiSplitPane, @NotNull final Component view )
            {
                updateCursor ();
            }
        };
        divider.getMultiSplitPane ().addExpansionListener ( multiSplitExpansionListener );

        // Installing visibility behavior
        visibilityBehavior = new VisibilityBehavior<WebMultiSplitPaneDivider> ( divider )
        {
            @Override
            protected void displayed ( @NotNull final WebMultiSplitPaneDivider component )
            {
                updateCursor ();
            }

            @Override
            protected void hidden ( @NotNull final WebMultiSplitPaneDivider component )
            {
                updateCursor ();
            }
        };
        visibilityBehavior.install ();

        // Installing drag listeners
        dragListener = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( @NotNull final MouseEvent e )
            {
                if ( e.getClickCount () == 2 )
                {
                    final WebMultiSplitPane multiSplitPane = divider.getMultiSplitPane ();
                    if ( multiSplitPane.isOneTouchExpandable () && multiSplitPane.isAnyViewExpanded () )
                    {
                        multiSplitPane.collapseExpandedView ();
                    }
                }
            }

            @Override
            public void mousePressed ( @NotNull final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) && divider.isDragAvailable () )
                {
                    dragged = true;
                    final MultiSplitPaneModel model = divider.getMultiSplitPane ().getModel ();
                    if ( model != null )
                    {
                        model.dividerDragStarted ( divider, e );
                    }
                    e.consume ();
                }
            }

            @Override
            public void mouseDragged ( @NotNull final MouseEvent e )
            {
                if ( dragged )
                {
                    final MultiSplitPaneModel model = divider.getMultiSplitPane ().getModel ();
                    if ( model != null )
                    {
                        model.dividerDragged ( divider, e );
                    }
                    e.consume ();
                }
            }

            @Override
            public void mouseReleased ( @NotNull final MouseEvent e )
            {
                if ( dragged )
                {
                    final MultiSplitPaneModel model = divider.getMultiSplitPane ().getModel ();
                    if ( model != null )
                    {
                        model.dividerDragEnded ( divider, e );
                    }
                    dragged = false;
                    e.consume ();
                }
            }
        };
        divider.addMouseListener ( dragListener );
        divider.addMouseMotionListener ( dragListener );
    }

    @Override
    protected void uninstallListeners ()
    {
        // Unnstalling drag listeners
        divider.removeMouseMotionListener ( dragListener );
        divider.removeMouseListener ( dragListener );
        dragListener = null;

        // Uninstalling visibility behavior
        visibilityBehavior.uninstall ();
        visibilityBehavior = null;

        // Unnstalling expansion listener
        divider.getMultiSplitPane ().removeExpansionListener ( multiSplitExpansionListener );
        multiSplitExpansionListener = null;

        // Uninstalling multi split pane property change listener
        divider.getMultiSplitPane ().removePropertyChangeListener ( multiSplitPanePropertyListener );
        multiSplitPanePropertyListener = null;

        // Uninstalling default listeners
        super.uninstallListeners ();
    }

    @Override
    public WebButton getLeftOneTouchButton ()
    {
        return leftOneTouchButton;
    }

    @Override
    public WebButton getRightOneTouchButton ()
    {
        return rightOneTouchButton;
    }

    /**
     * Updates divider cursor.
     */
    protected void updateCursor ()
    {
        final Orientation orientation = divider.getOrientation ();
        final int cursorType;
        if ( divider.isDragAvailable () )
        {
            cursorType = orientation.isVertical () ? Cursor.E_RESIZE_CURSOR : Cursor.S_RESIZE_CURSOR;
        }
        else
        {
            cursorType = Cursor.HAND_CURSOR;
        }
        divider.setCursor ( Cursor.getPredefinedCursor ( cursorType ) );
    }

    /**
     * Updates divider one-touch buttons.
     */
    protected void updateOneTouchButtons ()
    {
        if ( divider.getMultiSplitPane ().isOneTouchExpandable () )
        {
            if ( leftOneTouchButton == null && rightOneTouchButton == null )
            {
                /**
                 * Create the left button and add an action listener to expand/collapse it.
                 */
                leftOneTouchButton = divider.createLeftOneTouchButton ();

                /**
                 * Create the right button and add an action listener to expand/collapse it.
                 */
                rightOneTouchButton = divider.createRightOneTouchButton ();

                /**
                 * Add left and right buttons onto the divider.
                 */
                if ( leftOneTouchButton != null && rightOneTouchButton != null )
                {
                    divider.add ( leftOneTouchButton );
                    divider.add ( rightOneTouchButton );
                }
            }

            /**
             * Updating button decoration states.
             */
            DecorationUtils.fireStatesChanged ( leftOneTouchButton );
            DecorationUtils.fireStatesChanged ( rightOneTouchButton );
        }

        /**
         * Revalidating divider.
         */
        divider.revalidate ();
        divider.repaint ();
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