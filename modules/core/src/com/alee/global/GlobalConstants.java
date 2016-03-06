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

package com.alee.global;

import com.alee.utils.file.FileComparator;
import com.alee.utils.filefilter.*;

import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.List;

/**
 * This class provides various global constants.
 *
 * @author Mikle Garin
 */

public final class GlobalConstants
{
    /**
     * Sides.
     */
    public static final int NONE = -1;
    public static final int TOP = SwingConstants.TOP;
    public static final int LEFT = SwingConstants.LEFT;
    public static final int BOTTOM = SwingConstants.BOTTOM;
    public static final int RIGHT = SwingConstants.RIGHT;

    /**
     * Timeout delays.
     */
    public static final int SHORT_TIMEOUT = 3000;
    public static final int MEDIUM_TIMEOUT = 10000;
    public static final int LONG_TIMEOUT = 30000;

    /**
     * Viewable image formats.
     */
    @SuppressWarnings ( "SpellCheckingInspection" )
    public static final List<String> IMAGE_FORMATS = Arrays.asList ( "png", "apng", "gif", "agif", "jpg", "jpeg", "jpeg2000", "bmp" );

    /**
     * File filters.
     */
    public static final AllFilesFilter ALL_FILES_FILTER = new AllFilesFilter ();
    public static final NonHiddenFilter NON_HIDDEN_ONLY_FILTER = new NonHiddenFilter ();
    public static final DirectoriesFilter DIRECTORIES_FILTER = new DirectoriesFilter ();
    public static final GroupedFileFilter NON_HIDDEN_DIRECTORIES_FILTER =
            new GroupedFileFilter ( FilterGroupType.AND, GlobalConstants.DIRECTORIES_FILTER, GlobalConstants.NON_HIDDEN_ONLY_FILTER );
    public static final FilesFilter FILES_FILTER = new FilesFilter ();
    public static final ImageFilesFilter IMAGES_FILTER = new ImageFilesFilter ();
    public static final GroupedFileFilter IMAGES_AND_FOLDERS_FILTER =
            new GroupedFileFilter ( FilterGroupType.OR, GlobalConstants.IMAGES_FILTER, GlobalConstants.DIRECTORIES_FILTER );

    /**
     * Default file filters.
     */
    public static final List<AbstractFileFilter> DEFAULT_FILTERS =
            Arrays.asList ( ALL_FILES_FILTER, IMAGES_AND_FOLDERS_FILTER, DIRECTORIES_FILTER );

    /**
     * Comparators.
     */
    public static final FileComparator FILE_COMPARATOR = new FileComparator ();

    /**
     * Drawing constants.
     */
    public static final AffineTransform moveX = new AffineTransform ();
    public static final AffineTransform moveY = new AffineTransform ();
    public static final AffineTransform moveXY = new AffineTransform ();

    static
    {
        moveX.translate ( 1, 0 );
        moveY.translate ( 0, 1 );
        moveXY.translate ( 1, 1 );
    }

    /**
     * Debugging mode mark.
     */
    public static boolean DEBUG = false;
}