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

import com.alee.api.annotations.Nullable;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleId;
import com.alee.painter.decoration.DecorationState;
import com.alee.painter.decoration.Stateful;

import java.util.ArrayList;
import java.util.List;

/**
 * Default one-touch button implementation for {@link WebMultiSplitPaneDivider}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebMultiSplitPane">How to use WebMultiSplitPane</a>
 * @see WebMultiSplitPane
 */
public class OneTouchButton extends WebButton implements Stateful
{
    /**
     * {@link WebMultiSplitPaneDivider} this button is attached to.
     */
    protected final WebMultiSplitPaneDivider divider;

    /**
     * Constructs new {@link OneTouchButton}.
     *
     * @param id      style ID
     * @param divider {@link WebMultiSplitPaneDivider} this button is attached to
     */
    public OneTouchButton ( final StyleId id, final WebMultiSplitPaneDivider divider )
    {
        super ( id );
        this.divider = divider;
    }

    @Nullable
    @Override
    public List<String> getStates ()
    {
        final List<String> states;
        if ( divider != null )
        {
            // Additional split pane states
            states = new ArrayList<String> ( 1 );

            // Buttons positioning orientation
            states.add ( divider.getOrientation ().isVertical () ? DecorationState.vertical : DecorationState.horizontal );

            // One-touch
            if ( divider.getMultiSplitPane ().isOneTouchExpandable () )
            {
                states.add ( DecorationState.oneTouch );
            }
        }
        else
        {
            // This will be the case upon initialization
            // Divider is the last one to be set so it will be null upon first run
            states = null;
        }
        return states;
    }
}