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

import com.alee.managers.language.LanguageManager;

import javax.swing.*;
import java.io.File;

/**
 * Custom file filter that accepts only non-hidden files.
 *
 * @author Mikle Garin
 */

public class NonHiddenFilter extends AbstractFileFilter
{
    /**
     * Filter icon.
     */
    public static final ImageIcon ICON = new ImageIcon ( DirectoriesFilter.class.getResource ( "icons/nonhidden.png" ) );

    @Override
    public ImageIcon getIcon ()
    {
        return ICON;
    }

    @Override
    public String getDescription ()
    {
        return LanguageManager.get ( "weblaf.file.filter.nonhidden" );
    }

    @Override
    public boolean accept ( final File file )
    {
        return !file.isHidden ();
    }
}