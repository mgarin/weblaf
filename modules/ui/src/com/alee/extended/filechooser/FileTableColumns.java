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

package com.alee.extended.filechooser;

import com.alee.utils.collection.ImmutableList;

import java.util.List;

/**
 * This interface contains a list of constants which represent all columns available in WebFileTable.
 *
 * @author Mikle Garin
 */
public interface FileTableColumns
{
    /**
     * File table column IDs prefix.
     */
    public static final String COLUMN_PREFIX = "weblaf.filechooser.table.column.";

    /**
     * File table column IDs.
     */
    public static final String NUMBER_COLUMN = COLUMN_PREFIX + "number";
    public static final String NAME_COLUMN = COLUMN_PREFIX + "name";
    public static final String SIZE_COLUMN = COLUMN_PREFIX + "size";
    public static final String EXTENSION_COLUMN = COLUMN_PREFIX + "extension";
    public static final String CREATION_DATE_COLUMN = COLUMN_PREFIX + "creation.date";
    public static final String MODIFICATION_DATE_COLUMN = COLUMN_PREFIX + "modification.date";

    /**
     * Default file table columns.
     */
    public static final List<String> DEFAULT_COLUMNS = new ImmutableList<String> (
            NAME_COLUMN,
            EXTENSION_COLUMN,
            SIZE_COLUMN,
            MODIFICATION_DATE_COLUMN
    );
}