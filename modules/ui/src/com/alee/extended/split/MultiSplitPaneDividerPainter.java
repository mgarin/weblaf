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

import com.alee.api.jdk.Objects;
import com.alee.laf.splitpane.WebSplitPaneDivider;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Simple {@link WebMultiSplitPane} component divider painters.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */

public class MultiSplitPaneDividerPainter<C extends WebMultiSplitPaneDivider, U extends WMultiSplitPaneDividerUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements IMultiSplitPaneDividerPainter<C, U>
{
    /**
     * {@link PropertyChangeListener} for {@link WebMultiSplitPane} divider is used for.
     */
    protected transient PropertyChangeListener splitPanePropertyChangeListener;

    /**
     * {@link MultiSplitExpansionListener} for {@link WebMultiSplitPane} divider is used for.
     */
    protected transient MultiSplitExpansionListener multiSplitExpansionListener;

    /**
     * {@link MultiSplitResizeListener} for {@link WebMultiSplitPane} divider is used for.
     */
    protected transient MultiSplitResizeListener multiSplitResizeListener;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installMultiSplitPaneListeners ( component.getMultiSplitPane () );
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallMultiSplitPaneListeners ( component.getMultiSplitPane () );
        super.uninstallPropertiesAndListeners ();
    }

    @Override
    protected void propertyChanged ( final String property, final Object oldValue, final Object newValue )
    {
        // Perform basic actions on property changes
        super.propertyChanged ( property, oldValue, newValue );

        // Updating split pane listeners
        if ( Objects.equals ( property, WebSplitPaneDivider.SPLIT_PANE_PROPERTY ) )
        {
            uninstallMultiSplitPaneListeners ( ( WebMultiSplitPane ) oldValue );
            installMultiSplitPaneListeners ( ( WebMultiSplitPane ) newValue );
        }
    }

    /**
     * Installs listeners into {@link WebMultiSplitPane} if it exists.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}, could be {@code null}
     */
    protected void installMultiSplitPaneListeners ( final WebMultiSplitPane multiSplitPane )
    {
        if ( multiSplitPane != null )
        {
            splitPanePropertyChangeListener = new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( final PropertyChangeEvent event )
                {
                    if ( Objects.equals ( event.getPropertyName (), WebMultiSplitPane.ORIENTATION_PROPERTY,
                            WebMultiSplitPane.CONTINUOUS_LAYOUT_PROPERTY, WebMultiSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY ) )
                    {
                        updateDecorationState ();
                    }
                }
            };
            multiSplitPane.addPropertyChangeListener ( splitPanePropertyChangeListener );

            multiSplitExpansionListener = new MultiSplitExpansionListener ()
            {
                @Override
                public void viewExpanded ( final WebMultiSplitPane multiSplitPane, final Component view )
                {
                    updateDecorationState ();
                }

                @Override
                public void viewCollapsed ( final WebMultiSplitPane multiSplitPane, final Component view )
                {
                    updateDecorationState ();
                }
            };
            multiSplitPane.addExpansionListener ( multiSplitExpansionListener );

            multiSplitResizeListener = new MultiSplitResizeAdapter ()
            {
                @Override
                public void viewResizeStarted ( final WebMultiSplitPane multiSplitPane, final WebMultiSplitPaneDivider divider )
                {
                    updateDecorationState ();
                }

                @Override
                public void viewResizeEnded ( final WebMultiSplitPane multiSplitPane, final WebMultiSplitPaneDivider divider )
                {
                    updateDecorationState ();
                }
            };
            multiSplitPane.addResizeListener ( multiSplitResizeListener );
        }
    }

    /**
     * Uninstalls listeners from {@link WebMultiSplitPane} if it exists.
     *
     * @param multiSplitPane {@link WebMultiSplitPane}, could be {@code null}
     */
    protected void uninstallMultiSplitPaneListeners ( final WebMultiSplitPane multiSplitPane )
    {
        if ( multiSplitPane != null )
        {
            multiSplitPane.removeResizeListener ( multiSplitResizeListener );
            multiSplitResizeListener = null;
            multiSplitPane.removeExpansionListener ( multiSplitExpansionListener );
            multiSplitExpansionListener = null;
            multiSplitPane.removePropertyChangeListener ( splitPanePropertyChangeListener );
            splitPanePropertyChangeListener = null;
        }
    }

    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        final WebMultiSplitPane multiSplitPane = component.getMultiSplitPane ();

        // Divider orientation
        states.add ( component.getOrientation ().isVertical () ? DecorationState.vertical : DecorationState.horizontal );

        // Continuous layout
        states.add ( multiSplitPane.isContinuousLayout () ? DecorationState.continuous : DecorationState.nonContinuous );

        // One-touch
        if ( multiSplitPane.isOneTouchExpandable () )
        {
            states.add ( DecorationState.oneTouch );
        }

        // Dragged state
        if ( multiSplitPane.getModel ().getDraggedDivider () == component )
        {
            states.add ( DecorationState.dragged );
        }

        // Start or end position
        if ( multiSplitPane.isAnyViewExpanded () )
        {
            final int expanded = multiSplitPane.getExpandedViewIndex ();
            final int index = multiSplitPane.getModel ().getDividerIndex ( component );
            if ( index < expanded )
            {
                // Divider is at the very start of the multi split pane
                states.add ( DecorationState.start );
            }
            else
            {
                // Divider is at the very end of the multi split pane
                states.add ( DecorationState.end );
            }
        }

        return states;
    }
}