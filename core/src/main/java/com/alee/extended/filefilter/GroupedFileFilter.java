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

package com.alee.extended.filefilter;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: mgarin Date: 08.07.11 Time: 16:39
 */

public class GroupedFileFilter extends DefaultFileFilter
{
    private GroupType groupType;
    private DefaultFileFilter defaultFilter;
    private List<FileFilter> filters;

    public GroupedFileFilter ( GroupType groupType, FileFilter... filters )
    {
        super ();

        // Filters grouping type
        this.groupType = groupType;

        // Default filter
        this.defaultFilter =
                filters != null && filters.length > 0 && filters[ 0 ] instanceof DefaultFileFilter ? ( DefaultFileFilter ) filters[ 0 ] :
                        null;

        // Filters to group
        this.filters = new ArrayList<FileFilter> ();
        if ( filters != null )
        {
            Collections.addAll ( this.filters, filters );
        }
    }

    public ImageIcon getIcon ()
    {
        return defaultFilter != null ? defaultFilter.getIcon () : null;
    }

    public String getDescription ()
    {
        return defaultFilter != null ? defaultFilter.getDescription () : null;
    }

    public boolean accept ( File pathname )
    {
        if ( groupType.equals ( GroupType.AND ) )
        {
            for ( FileFilter filter : filters )
            {
                if ( filter != null && !filter.accept ( pathname ) )
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            for ( FileFilter filter : filters )
            {
                if ( filter == null || filter.accept ( pathname ) )
                {
                    return true;
                }
            }
            return false;
        }
    }
}
