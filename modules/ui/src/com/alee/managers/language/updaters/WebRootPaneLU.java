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

package com.alee.managers.language.updaters;

import com.alee.laf.rootpane.WebRootPaneUI;
import com.alee.managers.language.data.Value;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebRootPaneLU extends DefaultLanguageUpdater<JRootPane>
{
    @Override
    public void update ( final JRootPane c, final String key, final Value value, final Object... data )
    {
        final Window window = CoreSwingUtils.getWindowAncestor ( c );

        // Updating title
        if ( window instanceof Frame )
        {
            ( ( Frame ) window ).setTitle ( getDefaultText ( value, data ) );
        }
        else if ( window instanceof Dialog )
        {
            ( ( Dialog ) window ).setTitle ( getDefaultText ( value, data ) );
        }

        // todo This should be replaced by proper UI inner updates
        // Updating title pane
        if ( c.getUI () instanceof WebRootPaneUI )
        {
            final JComponent titleComponent = ( ( WebRootPaneUI ) c.getUI () ).getTitleComponent ();
            if ( titleComponent != null )
            {
                titleComponent.repaint ();
            }
        }
    }
}