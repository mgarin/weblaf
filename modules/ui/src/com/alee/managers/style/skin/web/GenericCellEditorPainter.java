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

package com.alee.managers.style.skin.web;

import com.alee.laf.table.editors.GenericCellEditor;
import com.alee.laf.text.WebTextFieldUI;

import java.awt.*;

/**
 * Custom painter to provide visual feedback for invalid editor cells.
 *
 * @author Mikle Garin
 * @see com.alee.painter.AbstractPainter
 * @see com.alee.painter.Painter
 */

public class GenericCellEditorPainter extends WebTextFieldPainter<GenericCellEditor, WebTextFieldUI>
{
    protected Color invalidBackground = new Color ( 255, 190, 190 );

    @Override
    protected void paintBackground ( final Graphics2D g2d, final Rectangle bounds, final Shape backgroundShape )
    {
        if ( component.isInvalidValue () )
        {
            g2d.setPaint ( invalidBackground );
            g2d.fill ( backgroundShape );
        }
        else
        {
            super.paintBackground ( g2d, bounds, backgroundShape );
        }
    }
}