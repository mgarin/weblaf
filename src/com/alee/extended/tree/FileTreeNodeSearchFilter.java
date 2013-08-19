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

package com.alee.extended.tree;

import com.alee.utils.compare.Filter;

import java.io.File;

/**
 * Custom filter for file tree nodes.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public class FileTreeNodeSearchFilter extends FileTreeNodeFilter
{
    /**
     * Additional search filter that filters out
     */
    protected Filter<FileTreeNode> searchFilter = null;

    public FileTreeNodeSearchFilter ()
    {
        super ();
    }

    public FileTreeNodeSearchFilter ( Filter<File> filter )
    {
        super ( filter );
    }

    public Filter<FileTreeNode> getSearchFilter ()
    {
        return searchFilter;
    }

    public void setSearchFilter ( Filter<FileTreeNode> searchFilter )
    {
        this.searchFilter = searchFilter;
    }

    public void setSearchPhrase ( final String phrase )
    {
        // todo Move to a separate class
        // todo Cache results for sub-nodes search till filter update called (?)
        this.searchFilter = new Filter<FileTreeNode> ()
        {
            public boolean accept ( FileTreeNode object )
            {
                return searchForText ( object, phrase );
            }

            private boolean searchForText ( FileTreeNode object, String phrase )
            {
                if ( object.getFile ().getName ().contains ( phrase ) )
                {
                    return true;
                }
                for ( int i = 0; i < object.getChildCount (); i++ )
                {
                    if ( searchForText ( ( FileTreeNode ) object.getChildAt ( i ), phrase ) )
                    {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    public boolean accept ( FileTreeNode object )
    {
        return super.accept ( object ) && ( searchFilter == null || searchFilter.accept ( object ) );
    }
}