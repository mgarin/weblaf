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

import com.alee.extended.image.WebImage;
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.StyleId;
import com.alee.utils.FileUtils;
import com.alee.utils.swing.WebDefaultCellEditor;

import javax.swing.*;
import java.awt.*;
import java.io.File;

/**
 * File table cell editor.
 *
 * @author Mikle Garin
 */
public class WebFileTableCellEditor extends WebDefaultCellEditor<WebTextField>
{
    /**
     * Constructs new WebFileTableCellEditor.
     */
    public WebFileTableCellEditor ()
    {
        super ();
        clickCountToStart = 3;
        editorComponent = new WebTextField ();
        delegate = new FileNameEditorDelegate ();
        editorComponent.addActionListener ( delegate );
    }

    @Override
    public Component getTableCellEditorComponent ( final JTable table, final Object value, final boolean isSelected, final int row,
                                                   final int column )
    {
        editorComponent.setStyleId ( StyleId.tableCellEditor.at ( table ) );
        return super.getTableCellEditorComponent ( table, value, isSelected, row, column );
    }

    /**
     * Custom editor delegate for WebFileTableCellEditor.
     */
    protected class FileNameEditorDelegate extends EditorDelegate<File>
    {
        @Override
        public void setValue ( final File file )
        {
            editorComponent.setLeadingComponent ( new WebImage ( FileUtils.getFileIcon ( file ) ) );
            FileUtils.displayFileName ( editorComponent, file );
            super.setValue ( file );
        }

        @Override
        public boolean stopCellEditing ()
        {
            final String newName = editorComponent.getText ();
            if ( !newName.equals ( value.getName () ) )
            {
                final File renamed = new File ( value.getParent (), newName );
                if ( value.renameTo ( renamed ) )
                {
                    value = renamed;
                }
                return super.stopCellEditing ();
            }
            else
            {
                super.cancelCellEditing ();
                return true;
            }
        }
    }
}