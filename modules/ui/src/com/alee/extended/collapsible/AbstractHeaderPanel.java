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
import com.alee.laf.panel.WebPanel;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.Stateful;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Custom {@link WebPanel} used as default {@link WebCollapsiblePane} header component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 * @see WebCollapsiblePane#createHeaderComponent()
 * @see WebCollapsiblePane#setHeaderComponent(Component)
 */
public abstract class AbstractHeaderPanel extends WebPanel implements Stateful
{
    /**
     * Constructs new {@link AbstractHeaderPanel}.
     */
    public AbstractHeaderPanel ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link AbstractHeaderPanel}.
     *
     * @param id style ID
     */
    public AbstractHeaderPanel ( @NotNull final StyleId id )
    {
        super ( id, new HeaderLayout () );

        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mouseReleased ( @NotNull final MouseEvent e )
            {
                if ( SwingUtilities.isLeftMouseButton ( e ) )
                {
                    final Point point = e.getPoint ();
                    if ( 0 <= point.x && point.x < AbstractHeaderPanel.this.getWidth () &&
                            0 <= point.y && point.y < AbstractHeaderPanel.this.getHeight () )
                    {
                        onHeaderAction ( e );
                    }
                }
            }
        } );
        addKeyListener ( new KeyAdapter ()
        {
            @Override
            public void keyReleased ( @NotNull final KeyEvent e )
            {
                if ( Hotkey.ENTER.isTriggered ( e ) || Hotkey.SPACE.isTriggered ( e ) )
                {
                    onHeaderAction ( e );
                }
            }
        } );
    }

    @Override
    public void setLayout ( @Nullable final LayoutManager layout )
    {
        if ( layout == null || layout instanceof HeaderLayout )
        {
            super.setLayout ( layout );
        }
        else
        {
            throw new IllegalArgumentException ( "Only HeaderLayout instances are supported" );
        }
    }

    @Nullable
    @Override
    public HeaderLayout getLayout ()
    {
        return ( HeaderLayout ) super.getLayout ();
    }

    /**
     * Returns title {@link Component}.
     *
     * @return title {@link Component}
     */
    @Nullable
    public Component getTitle ()
    {
        final HeaderLayout layout = getLayout ();
        return layout != null ? layout.getTitle () : null;
    }

    /**
     * Returns control {@link Component}.
     *
     * @return control {@link Component}
     */
    @Nullable
    public Component getControl ()
    {
        final HeaderLayout layout = getLayout ();
        return layout != null ? layout.getControl () : null;
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
     * Override this method to perform something upon header action.
     * This can either be {@link MouseEvent} or {@link KeyEvent} from according listeners.
     *
     * @param e {@link InputEvent}
     */
    protected abstract void onHeaderAction ( @NotNull InputEvent e );

    @Nullable
    @Override
    public List<String> getStates ()
    {
        final List<String> states = new ArrayList<String> ( 2 );

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
     * A subclass of {@link AbstractHeaderPanel} that implements {@link javax.swing.plaf.UIResource}.
     * This subclass is used by {@link WebCollapsiblePaneUI} to setup default style.
     * If you want to customize {@link AbstractHeaderPanel} with your own style do not use this subclass.
     */
    public static abstract class UIResource extends AbstractHeaderPanel implements javax.swing.plaf.UIResource
    {
        /**
         * Implementation is used completely from {@link AbstractHeaderPanel}.
         */
    }
}