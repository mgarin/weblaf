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

package com.alee.managers.icon.set;

import com.alee.managers.icon.data.AbstractIconData;
import com.alee.utils.CollectionUtils;
import com.alee.utils.NetUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.XmlUtils;

import java.io.File;

/**
 * Simple icon set that could be created based on a custom XML data file.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see com.alee.managers.icon.IconManager
 */
public class XmlIconSet extends AbstractIconSet
{
    /**
     * Constructs new xml-based icon set.
     *
     * @param location icon set data XML file location
     */
    public XmlIconSet ( final String location )
    {
        this ( new File ( location ) );
    }

    /**
     * Constructs new xml-based icon set.
     *
     * @param location icon set data XML file
     */
    public XmlIconSet ( final File location )
    {
        this ( ( IconSetData ) XmlUtils.fromXML ( location ) );
    }

    /**
     * Constructs new xml-based icon set.
     *
     * @param nearClass class to find icon set data XML near
     * @param location  icon set data XML location relative to the specified class
     */
    public XmlIconSet ( final Class nearClass, final String location )
    {
        this ( ( IconSetData ) XmlUtils.fromXML ( nearClass.getResource ( location ) ) );
    }

    /**
     * Constructs new xml-based icon set.
     *
     * @param iconSetData icon set data
     */
    public XmlIconSet ( final IconSetData iconSetData )
    {
        super ( iconSetData.getId () );

        // Updating and caching icons information
        if ( CollectionUtils.notEmpty ( iconSetData.getIcons () ) )
        {
            for ( final AbstractIconData iconData : iconSetData.getIcons () )
            {
                // Changing relative class to IconSet relative class if one exists
                // This will only be performed if IconData relative class is not specified and global one is specified
                if ( iconData.getNearClass () == null && iconSetData.getNearClass () != null )
                {
                    iconData.setNearClass ( iconSetData.getNearClass () );
                }

                // Combining base path with icon path
                if ( !TextUtils.isEmpty ( iconSetData.getBase () ) )
                {
                    if ( iconData.getNearClass () != null )
                    {
                        iconData.setPath ( NetUtils.joinUrlPaths ( iconSetData.getBase (), iconData.getPath () ) );
                    }
                    else
                    {
                        iconData.setPath ( new File ( iconSetData.getBase (), iconData.getPath () ).getAbsolutePath () );
                    }
                }

                // Adding icon
                addIcon ( iconData );
            }
        }
    }
}