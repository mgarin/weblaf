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

package com.alee.managers.icon.data;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.net.URL;

/**
 * {@link AbstractIconData} implementation for {@link ImageIcon} icon type.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see ImageIcon
 * @see com.alee.managers.icon.IconManager
 */
@XStreamAlias ( "ImageIcon" )
public class ImageIconData extends AbstractIconData<ImageIcon>
{
    @Override
    public ImageIcon loadIcon ()
    {
        if ( getNearClass () != null )
        {
            final URL url = getNearClass ().getResource ( getPath () );
            return new ImageIcon ( url );
        }
        else
        {
            final String filen = getPath ();
            return new ImageIcon ( filen );
        }
    }
}