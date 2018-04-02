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

package com.alee.laf.splitpane;

import com.alee.api.jdk.Objects;
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.IDecoration;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

/**
 * Basic painter for {@link WebSplitPaneDivider} component.
 * It is used as {@link WSplitPaneDividerUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 */

public class SplitPaneDividerPainter<C extends WebSplitPaneDivider, U extends WSplitPaneDividerUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements ISplitPaneDividerPainter<C, U>
{
    /**
     * {@link PropertyChangeListener} for {@link JSplitPane} divider is used for.
     */
    protected transient PropertyChangeListener splitPanePropertyChangeListener;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installSplitPaneListeners ( component.getSplitPane () );
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallSplitPaneListeners ( component.getSplitPane () );
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
            uninstallSplitPaneListeners ( ( JSplitPane ) oldValue );
            installSplitPaneListeners ( ( JSplitPane ) newValue );
        }
    }

    /**
     * Installs listeners into {@link JSplitPane} if it exists.
     *
     * @param splitPane {@link JSplitPane}, could be {@code null}
     */
    protected void installSplitPaneListeners ( final JSplitPane splitPane )
    {
        if ( splitPane != null )
        {
            splitPanePropertyChangeListener = new PropertyChangeListener ()
            {
                @Override
                public void propertyChange ( final PropertyChangeEvent event )
                {
                    if ( Objects.equals ( event.getPropertyName (),
                            JSplitPane.ORIENTATION_PROPERTY, JSplitPane.ONE_TOUCH_EXPANDABLE_PROPERTY ) )
                    {
                        updateDecorationState ();
                    }
                }
            };
            splitPane.addPropertyChangeListener ( splitPanePropertyChangeListener );
        }
    }

    /**
     * Uninstalls listeners from {@link JSplitPane} if it exists.
     *
     * @param splitPane {@link JSplitPane}, could be {@code null}
     */
    protected void uninstallSplitPaneListeners ( final JSplitPane splitPane )
    {
        if ( splitPane != null )
        {
            splitPane.removePropertyChangeListener ( splitPanePropertyChangeListener );
            splitPanePropertyChangeListener = null;
        }
    }

    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Split pane specific states
        final JSplitPane splitPane = component.getSplitPane ();
        if ( splitPane != null )
        {
            // Divider orientation
            final boolean vertical = splitPane.getOrientation () == JSplitPane.HORIZONTAL_SPLIT;
            states.add ( vertical ? DecorationState.vertical : DecorationState.horizontal );

            // One-touch
            if ( splitPane.isOneTouchExpandable () )
            {
                states.add ( DecorationState.oneTouch );
            }
        }

        return states;
    }
}