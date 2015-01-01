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

package com.alee.laf.table.editors;

import com.alee.extended.date.DateSelectionListener;
import com.alee.extended.date.WebDateField;
import com.alee.laf.button.WebButton;
import com.alee.utils.swing.WebDefaultCellEditor;

import java.util.Date;

/**
 * @author Mikle Garin
 */

public class WebDateEditor extends WebDefaultCellEditor<WebDateField>
{
    public WebDateEditor ()
    {
        super ();

        editorComponent = new WebDateField ( false );
        editorComponent.setFireSelectionWithoutChanges ( true );
        editorComponent.addDateSelectionListener ( new DateSelectionListener ()
        {
            @Override
            public void dateSelected ( final Date date )
            {
                stopCellEditing ();
            }
        } );

        final WebButton popupButton = editorComponent.getPopupButton ();
        popupButton.setRound ( 0 );
        popupButton.setLeftRightSpacing ( 0 );
        popupButton.setDrawSides ( false, true, false, false );

        delegate = new EditorDelegate ()
        {
            @Override
            public void setValue ( final Object value )
            {
                editorComponent.setDate ( ( Date ) value );
            }

            @Override
            public Object getCellEditorValue ()
            {
                // Updating value from field to make sure it is up-to-date
                editorComponent.updateDateFromField ( false );
                return editorComponent.getDate ();
            }
        };
    }
}