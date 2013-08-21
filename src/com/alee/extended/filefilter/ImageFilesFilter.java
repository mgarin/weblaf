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

import com.alee.laf.GlobalConstants;
import com.alee.managers.language.LanguageManager;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;

/**
 * User: mgarin Date: 11.07.11 Time: 19:48
 */

public class ImageFilesFilter extends DefaultFileFilter
{
    private static final ImageIcon ICON = new ImageIcon ( ImageFilesFilter.class.getResource ( "icons/image.png" ) );

    @Override
    public ImageIcon getIcon ()
    {
        return ICON;
    }

    @Override
    public String getDescription ()
    {
        return LanguageManager.get ( "weblaf.file.filter.images" );
    }

    @Override
    public boolean accept ( File file )
    {
        return GlobalConstants.IMAGE_FORMATS.contains ( FileUtils.getFileExtPart ( file.getName ().toLowerCase (), false ) );
    }
}
