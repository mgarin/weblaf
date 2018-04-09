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
import com.alee.managers.language.Language;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Custom {@link LanguageUpdater} for {@link JRootPane}.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-LanguageManager">How to use LanguageManager</a>
 * @see com.alee.managers.language.LanguageManager
 */
public class JRootPaneLU extends ToolTipLU<JRootPane>
{
    @Override
    public Class getComponentClass ()
    {
        return JRootPane.class;
    }

    @Override
    public void update ( final JRootPane component, final Language language, final String key, final Object... data )
    {
        super.update ( component, language, key, data );

        if ( language.containsText ( key ) )
        {
            final Window window = CoreSwingUtils.getWindowAncestor ( component );
            if ( window instanceof Frame )
            {
                ( ( Frame ) window ).setTitle ( language.get ( key, data ) );
            }
            else if ( window instanceof Dialog )
            {
                ( ( Dialog ) window ).setTitle ( language.get ( key, data ) );
            }

            // Updating custom window title upon language changes
            // todo This should be done within WebRootPaneUI instead
            if ( component.getUI () instanceof WebRootPaneUI )
            {
                final JComponent titleComponent = ( ( WebRootPaneUI ) component.getUI () ).getTitleComponent ();
                if ( titleComponent != null )
                {
                    titleComponent.repaint ();
                }
            }
        }
    }
}