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
import com.alee.utils.filefilter.NonHiddenFilter;

import java.io.File;

/**
 * Custom filter for {@link FileTreeNode}s.
 *
 * @author Mikle Garin
 */

public class FileTreeNodeFilter implements Filter<FileTreeNode>
{
    /**
     * {@link Filter} used for filtering {@link File}s.
     */
    protected Filter<File> filter;

    /**
     * Constructs new {@link FileTreeNodeFilter} with default file filter.
     */
    public FileTreeNodeFilter ()
    {
        this ( new NonHiddenFilter () );
    }

    /**
     * Constructs new {@link FileTreeNodeFilter} using the specified file filter.
     *
     * @param filter file filter
     */
    public FileTreeNodeFilter ( final Filter<File> filter )
    {
        super ();
        this.filter = filter;
    }

    /**
     * Returns {@link Filter} used for filtering {@link File}s.
     *
     * @return {@link Filter} used for filtering {@link File}s
     */
    public Filter<File> getFilter ()
    {
        return filter;
    }

    /**
     * Sets {@link Filter} used for filtering {@link File}s.
     *
     * @param filter {@link Filter} used for filtering {@link File}s
     */
    public void setFilter ( final Filter<File> filter )
    {
        this.filter = filter;
    }

    @Override
    public boolean accept ( final FileTreeNode object )
    {
        return filter.accept ( object.getFile () );
    }
}