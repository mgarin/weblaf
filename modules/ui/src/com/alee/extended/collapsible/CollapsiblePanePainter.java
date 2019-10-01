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
import com.alee.painter.decoration.AbstractDecorationPainter;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.DecorationUtils;
import com.alee.painter.decoration.IDecoration;

import java.util.List;

/**
 * Basic painter for {@link WebCollapsiblePane} component.
 * It is used as {@link WCollapsiblePaneUI} default painter.
 *
 * @param <C> component type
 * @param <U> component UI type
 * @param <D> decoration type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebCollapsiblePane">How to use WebCollapsiblePane</a>
 * @see WebCollapsiblePane
 */
public class CollapsiblePanePainter<C extends WebCollapsiblePane, U extends WCollapsiblePaneUI, D extends IDecoration<C, D>>
        extends AbstractDecorationPainter<C, U, D> implements ICollapsiblePanePainter<C, U>
{
    /**
     * Listeners.
     */
    protected transient CollapsiblePaneListener collapsiblePaneListener;

    @Override
    protected void installPropertiesAndListeners ()
    {
        super.installPropertiesAndListeners ();
        installCollapsiblePaneListener ();
    }

    @Override
    protected void uninstallPropertiesAndListeners ()
    {
        uninstallCollapsiblePaneListener ();
        super.uninstallPropertiesAndListeners ();
    }

    /**
     * Installs {@link CollapsiblePaneListener}.
     */
    protected void installCollapsiblePaneListener ()
    {
        collapsiblePaneListener = new CollapsiblePaneListener ()
        {
            @Override
            public void expanding ( @NotNull final WebCollapsiblePane pane )
            {
                DecorationUtils.fireStatesChanged ( pane );
            }

            @Override
            public void expanded ( @NotNull final WebCollapsiblePane pane )
            {
                DecorationUtils.fireStatesChanged ( pane );
            }

            @Override
            public void collapsing ( @NotNull final WebCollapsiblePane pane )
            {
                DecorationUtils.fireStatesChanged ( pane );
            }

            @Override
            public void collapsed ( @NotNull final WebCollapsiblePane pane )
            {
                DecorationUtils.fireStatesChanged ( pane );
            }
        };
        component.addCollapsiblePaneListener ( collapsiblePaneListener );
    }

    /**
     * Uninstalls {@link CollapsiblePaneListener}.
     */
    protected void uninstallCollapsiblePaneListener ()
    {
        component.removeCollapsiblePaneListener ( collapsiblePaneListener );
        collapsiblePaneListener = null;
    }

    @NotNull
    @Override
    public List<String> getDecorationStates ()
    {
        final List<String> states = super.getDecorationStates ();

        // Header position state
        states.add ( component.getHeaderPosition ().name () );

        // Expansion state
        states.add ( component.isExpanded () || component.isInTransition () ? DecorationState.expanded : DecorationState.collapsed );
        if ( component.isInTransition () )
        {
            states.add ( component.isExpanded () ? DecorationState.expanding : DecorationState.collapsing );
        }

        return states;
    }
}