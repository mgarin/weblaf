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

import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

/**
 * Custom descriptor for {@link JFileChooser} component.
 *
 * @author Mikle Garin
 */

public final class FileChooserDescriptor extends AbstractComponentDescriptor<JFileChooser>
{
    /**
     * Constructs new descriptor for {@link JFileChooser} component.
     */
    public FileChooserDescriptor ()
    {
        super ( "filechooser", JFileChooser.class, "FileChooserUI", WFileChooserUI.class, WebFileChooserUI.class, StyleId.filechooser );
    }

    @Override
    public void updateUI ( final JFileChooser component )
    {
        // Removing all files filter
        if ( component.isAcceptAllFileFilterUsed () )
        {
            component.removeChoosableFileFilter ( component.getAcceptAllFileFilter () );
        }

        // Update file view as file chooser was probably deserialized
        if ( component.getFileSystemView () == null )
        {
            component.setFileSystemView ( FileSystemView.getFileSystemView () );
        }

        // Updating component UI
        super.updateUI ( component );

        // Updating UI file view for this file chooser
        final FileView fileView = component.getUI ().getFileView ( component );
        ReflectUtils.setFieldValueSafely ( component, "uiFileView", fileView );

        // Adding all files filter
        if ( component.isAcceptAllFileFilterUsed () )
        {
            component.addChoosableFileFilter ( component.getAcceptAllFileFilter () );
        }
    }
}