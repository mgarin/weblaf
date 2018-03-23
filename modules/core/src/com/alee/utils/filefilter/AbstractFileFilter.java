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

import com.alee.api.ui.IconBridge;
import com.alee.api.ui.RenderingParameters;
import com.alee.api.ui.TextBridge;
import com.alee.utils.compare.Filter;

import javax.swing.*;
import java.io.File;
import java.io.FileFilter;

/**
 * Default file filter for WebLaF file chooser that provides additional filter information.
 * This class overrides IO, Swing and utils filter classes.
 *
 * @author Mikle Garin
 */

public abstract class AbstractFileFilter extends javax.swing.filechooser.FileFilter
        implements FileFilter, Filter<File>, IconBridge<RenderingParameters>, TextBridge<RenderingParameters>
{
    /**
     * Returns file filter {@link Icon}.
     *
     * @param parameters {@link RenderingParameters}
     * @return file filter {@link Icon}
     */
    @Override
    public abstract Icon getIcon ( RenderingParameters parameters );

    /**
     * Returns file filter title.
     * Uses its description but can be overridden.
     *
     * @param parameters {@link RenderingParameters}
     * @return file filter title
     */
    @Override
    public String getText ( final RenderingParameters parameters )
    {
        return getDescription ();
    }

    /**
     * Returns short file filter description.
     *
     * @return short file filter description
     */
    @Override
    public abstract String getDescription ();

    /**
     * Returns whether the given file is accepted by this filter or not.
     *
     * @param file file to process
     * @return true if the given file is accepted by this filter, false otherwise
     */
    @Override
    public abstract boolean accept ( File file );
}