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

package com.alee.extended.list;

import com.alee.global.GlobalConstants;
import com.alee.utils.filefilter.AbstractFileFilter;

/**
 * WebFileList style class.
 *
 * @author Mikle Garin
 */

public final class WebFileListStyle
{
    /**
     * Whether to generate image file thumbnails or not.
     * Thumbnails generation might slow down list rendering in some cases.
     */
    public static boolean generateThumbnails = true;

    /**
     * Preferred visible column count.
     */
    public static int preferredColumnCount = 3;

    /**
     * Preferred visible row count.
     */
    public static int preferredRowCount = 3;

    /**
     * File view mode.
     */
    public static FileListViewType fileListViewType = FileListViewType.tiles;

    /**
     * File filter.
     */
    public static AbstractFileFilter fileFilter = GlobalConstants.NON_HIDDEN_ONLY_FILTER;
}