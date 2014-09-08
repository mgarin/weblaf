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

package com.alee.extended.panel;

import com.alee.global.StyleConstants;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * User: mgarin Date: 03.06.11 Time: 17:46
 */

public class BorderPanel extends WebPanel
{
    public BorderPanel ( final Component component )
    {
        this ( component, 0 );
    }

    public BorderPanel ( final Component component, final int border )
    {
        this ( component, border, border, border, border );
    }

    public BorderPanel ( final Component component, final int top, final int left, final int bottom, final int right )
    {
        this ( component, new Insets ( top, left, bottom, right ) );
    }

    public BorderPanel ( final Component component, final Insets margin )
    {
        super ();
        setMargin ( margin );
        if ( component != null )
        {
            setOpaque ( component.isOpaque () );
            setBackground ( component.getBackground () );
            add ( component, BorderLayout.CENTER );
        }
        else
        {
            setOpaque ( true );
            setBackground ( StyleConstants.backgroundColor );
        }
    }
}