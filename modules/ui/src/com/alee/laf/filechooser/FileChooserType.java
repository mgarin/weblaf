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

import javax.swing.*;

/**
 * This enumeration represents all available {@link javax.swing.JFileChooser} types.
 *
 * @author Mikle Garin
 */
public enum FileChooserType
{
    /**
     * Save file chooser type.
     * File chooser of this type has a special input field to enter saved file name.
     * It is also limited to single file selection since there is no point in saving single content into multiple files.
     * It also has an extension chooser combobox if allowed extensions are limited.
     */
    save,

    /**
     * Open file chooser type.
     * File chooser of this type is used to choose single or multiple files.
     */
    open,

    /**
     * Custom file chooser type.
     */
    custom;

    /**
     * Returns approve text language key.
     *
     * @return approve text language key
     */
    public String getApproveButtonText ()
    {
        return "weblaf.filechooser." + this;
    }

    /**
     * Returns {@link FileChooserType} for the specified {@link JFileChooser} type.
     *
     * @param type {@link JFileChooser} type
     * @return {@link FileChooserType} for the specified {@link JFileChooser} type
     */
    @NotNull
    public static FileChooserType forType ( final int type )
    {
        final FileChooserType fileChooserType;
        switch ( type )
        {
            case JFileChooser.SAVE_DIALOG:
                fileChooserType = save;
                break;

            case JFileChooser.OPEN_DIALOG:
                fileChooserType = open;
                break;

            default:
                fileChooserType = custom;
                break;
        }
        return fileChooserType;
    }
}