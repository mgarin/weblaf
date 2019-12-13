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

import com.alee.api.annotations.NotNull;
import com.alee.managers.style.AbstractComponentDescriptor;
import com.alee.managers.style.StyleId;
import com.alee.utils.ReflectUtils;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.filechooser.FileView;

/**
 * Abstract descriptor for {@link JFileChooser} component.
 * Extend this class for creating custom {@link JFileChooser} descriptors.
 *
 * @param <C> {@link JFileChooser} type
 * @param <U> base {@link WFileChooserUI} type
 * @param <P> {@link IFileChooserPainter} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 */
public abstract class AbstractFileChooserDescriptor<C extends JFileChooser, U extends WFileChooserUI, P extends IFileChooserPainter>
        extends AbstractComponentDescriptor<C, U, P>
{
    /**
     * Constructs new {@link AbstractFileChooserDescriptor}.
     *
     * @param id                  {@link JFileChooser} identifier
     * @param componentClass      {@link JFileChooser} {@link Class}
     * @param uiClassId           {@link WFileChooserUI} {@link Class} identifier
     * @param baseUIClass         base {@link WFileChooserUI} {@link Class} applicable to {@link JFileChooser}
     * @param uiClass             {@link WFileChooserUI} {@link Class} used for {@link JFileChooser} by default
     * @param painterInterface    {@link IFileChooserPainter} interface {@link Class}
     * @param painterClass        {@link IFileChooserPainter} implementation {@link Class}
     * @param painterAdapterClass adapter for {@link IFileChooserPainter}
     * @param defaultStyleId      {@link JFileChooser} default {@link StyleId}
     */
    public AbstractFileChooserDescriptor ( @NotNull final String id, @NotNull final Class<C> componentClass,
                                           @NotNull final String uiClassId, @NotNull final Class<U> baseUIClass,
                                           @NotNull final Class<? extends U> uiClass, @NotNull final Class<P> painterInterface,
                                           @NotNull final Class<? extends P> painterClass,
                                           @NotNull final Class<? extends P> painterAdapterClass, @NotNull final StyleId defaultStyleId )
    {
        super ( id, componentClass, uiClassId, baseUIClass, uiClass, painterInterface, painterClass, painterAdapterClass, defaultStyleId );
    }

    @Override
    public void updateUI ( @NotNull final C component )
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