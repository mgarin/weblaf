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

package com.alee.extended.collapsible;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.data.BoxOrientation;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.Stateful;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link WebButton} used as default {@link WebCollapsiblePane} expansion control.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 * @see WebCollapsiblePane#createControlComponent()
 * @see WebCollapsiblePane#setControlComponent(Component)
 */
public abstract class AbstractControlButton extends WebButton implements Stateful
{
    /**
     * Constructs new {@link AbstractControlButton}.
     */
    public AbstractControlButton ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link AbstractControlButton}.
     *
     * @param id style ID
     */
    public AbstractControlButton ( @NotNull final StyleId id )
    {
        super ( id );
        addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                onControlAction ( e );
            }
        } );
    }

    /**
     * Returns header panel position.
     *
     * @return header panel position
     */
    @NotNull
    public abstract BoxOrientation getHeaderPosition ();

    /**
     * Returns whether or not {@link WebCollapsiblePane} is expanded.
     *
     * @return {@code true} if {@link WebCollapsiblePane} is expanded, {@code false} otherwise
     */
    public abstract boolean isExpanded ();

    /**
     * Returns whether or not {@link WebCollapsiblePane} is in transition to either of two expansion states.
     *
     * @return {@code true} if {@link WebCollapsiblePane} is in transition, {@code false} otherwise
     */
    public abstract boolean isInTransition ();

    /**
     * Override this method to perform something upon control action.
     *
     * @param e {@link ActionEvent}
     */
    protected abstract void onControlAction ( @NotNull ActionEvent e );

    @Nullable
    @Override
    public List<String> getStates ()
    {
        final List<String> states = new ArrayList<String> ( 3 );

        // Header position state
        states.add ( getHeaderPosition ().name () );

        // Expansion state
        states.add ( isExpanded () || isInTransition () ? DecorationState.expanded : DecorationState.collapsed );
        if ( isInTransition () )
        {
            states.add ( isExpanded () ? DecorationState.expanding : DecorationState.collapsing );
        }

        return states;
    }

    /**
     * A subclass of {@link AbstractControlButton} that implements {@link javax.swing.plaf.UIResource}.
     * This subclass is used by {@link WebCollapsiblePaneUI} to setup default style.
     * If you want to customize {@link AbstractControlButton} with your own style do not use this subclass.
     */
    public static abstract class UIResource extends AbstractControlButton implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link AbstractControlButton}.
         */
    }
}