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

package com.alee.extended.breadcrumb;

/**
 * @author Mikle Garin
 */

public final class WebFileBreadcrumbStyle
{
    /**
     * Display file icon on file plate
     */
    public static boolean displayFileIcon = true;

    /**
     * Display file name on file plate
     */
    public static boolean displayFileName = true;

    /**
     * Display file type, size and modification date tip on file plate
     */
    public static boolean displayFileTip = true;

    /**
     * Shorten file name inside file plate to specified symbols amount (0 to disable)
     */
    public static int fileNameLength = 0;

    /**
     * Shorten file name inside file plate popup list to specified symbols amount (0 to disable)
     */
    public static int listFileNameLength = 30;

    /**
     * Display full file name inside tip if needed
     */
    public static boolean showFullNameInTip = true;

    /**
     * Enclose last breadcrumb element by default
     */
    public static boolean encloseLastElement = false;

    /**
     * Maximum visible rows in file list inside file plate popups
     */
    public static int maxVisibleListFiles = 8;

    /**
     * Auto expand last selected breadcrumb element menu
     */
    public static boolean autoExpandLastElement = false;
}