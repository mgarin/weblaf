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

package com.alee.examples.groups.toolbar;

import com.alee.examples.content.DefaultExample;
import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.toolbar.WebToolBar;

/**
 * User: mgarin Date: 07.11.12 Time: 18:28
 */

public abstract class DefaultToolbarExample extends DefaultExample
{
    public void setupToolBar ( WebToolBar toolbar )
    {
        toolbar.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/save.png" ), StyleConstants.smallRound, true ) );
        toolbar.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/saveall.png" ), StyleConstants.smallRound, true ) );
        toolbar.addSeparator ();
        toolbar.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/cut.png" ), StyleConstants.smallRound, true ) );
        toolbar.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/copy.png" ), StyleConstants.smallRound, true ) );
        toolbar.add ( WebButton.createIconWebButton ( loadIcon ( "toolbar/paste.png" ), StyleConstants.smallRound, true ) );
        toolbar.addToEnd ( WebButton.createIconWebButton ( loadIcon ( "toolbar/settings.png" ), StyleConstants.smallRound, true ) );
    }
}
