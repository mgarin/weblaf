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

import java.io.File;

/**
 * Adapter for {@link DirectoryChooserListener}.
 *
 * @author Mikle Garin
 */
public abstract class DirectoryChooserAdapter implements DirectoryChooserListener
{
    @Override
    public void selectionChanged ( final File file )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void accepted ( final File file )
    {
        /**
         * Do nothing by default.
         */
    }

    @Override
    public void cancelled ()
    {
        /**
         * Do nothing by default.
         */
    }
}