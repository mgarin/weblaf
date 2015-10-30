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

package com.alee.utils.filefilter;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This file filter groups specified file filters in a certain way defined by filter group type.
 *
 * @author Mikle Garin
 */

public class GroupedFileFilter extends AbstractFileFilter
{
    /**
     * The way file filters will be grouped.
     */
    protected FilterGroupType filterGroupType;

    /**
     * Default file filter used to display icon and description.
     */
    protected AbstractFileFilter defaultFilter;

    /**
     * List of grouped file filters.
     */
    protected List<FileFilter> filters;

    /**
     * Constructs grouped file filter with the specified parameters.
     *
     * @param filters list of file filters to be grouped
     */
    public GroupedFileFilter ( final FileFilter... filters )
    {
        this ( FilterGroupType.AND, filters );
    }

    /**
     * Constructs grouped file filter with the specified parameters.
     *
     * @param filterGroupType the way file filters will be grouped
     * @param filters         list of file filters to be grouped
     */
    public GroupedFileFilter ( final FilterGroupType filterGroupType, final FileFilter... filters )
    {
        this ( filters != null && filters.length > 0 && filters[ 0 ] instanceof AbstractFileFilter ? ( AbstractFileFilter ) filters[ 0 ] :
                null, filterGroupType, filters );
    }

    /**
     * Constructs grouped file filter with the specified parameters.
     *
     * @param defaultFilter   default file filter used to display icon and description
     * @param filterGroupType the way file filters will be grouped
     * @param filters         list of file filters to be grouped
     */
    public GroupedFileFilter ( final AbstractFileFilter defaultFilter, final FilterGroupType filterGroupType, final FileFilter... filters )
    {
        super ();

        // Filters grouping type
        this.filterGroupType = filterGroupType;

        // Default filter
        this.defaultFilter = defaultFilter;

        // Filters to group
        this.filters = new ArrayList<FileFilter> ();
        if ( filters != null )
        {
            Collections.addAll ( this.filters, filters );
        }
    }

    @Override
    public ImageIcon getIcon ()
    {
        return defaultFilter != null ? defaultFilter.getIcon () : null;
    }

    @Override
    public String getDescription ()
    {
        return defaultFilter != null ? defaultFilter.getDescription () : null;
    }

    @Override
    public boolean accept ( final File file )
    {
        if ( filterGroupType.equals ( FilterGroupType.AND ) )
        {
            for ( final FileFilter filter : filters )
            {
                if ( filter != null && !filter.accept ( file ) )
                {
                    return false;
                }
            }
            return true;
        }
        else
        {
            for ( final FileFilter filter : filters )
            {
                if ( filter == null || filter.accept ( file ) )
                {
                    return true;
                }
            }
            return false;
        }
    }
}