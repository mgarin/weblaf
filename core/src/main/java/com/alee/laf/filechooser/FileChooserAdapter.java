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

package com.alee.laf.filechooser;

import java.io.File;
import java.util.List;

/**
 * File chooser actions listener adapter.
 *
 * @author Mikle Garin
 * @since 1.4
 */

public abstract class FileChooserAdapter implements FileChooserListener
{
    /**
     * {@inheritDoc}
     */
    public void directoryChanged ( File newDirectory )
    {
        //
    }

    /**
     * {@inheritDoc}
     */
    public void selectionChanged ( List<File> selectedFiles )
    {
        //
    }
}
