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

package com.alee.extended.drag;

import javax.swing.*;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 12.04.12 Time: 18:36
 */

public class TextDragHandler extends TransferHandler
{
    private String text;
    private boolean defaultBehavior = true;

    public TextDragHandler ( final JComponent component )
    {
        this ( component, null );
    }

    public TextDragHandler ( final JComponent component, String text )
    {
        this ( component, text, true );
    }

    public TextDragHandler ( final JComponent component, String text, boolean defaultBehavior )
    {
        super ();
        setText ( text );
        setDefaultBehavior ( defaultBehavior );

        component.addMouseMotionListener ( new MouseAdapter ()
        {
            @Override
            public void mouseDragged ( MouseEvent e )
            {
                if ( isDefaultBehavior () && SwingUtilities.isLeftMouseButton ( e ) &&
                        component.isEnabled () )
                {
                    exportAsDrag ( component, e, getSourceActions ( component ) );
                }
            }
        } );
    }

    public String getText ()
    {
        return text;
    }

    public void setText ( String text )
    {
        this.text = text;
    }

    public boolean isDefaultBehavior ()
    {
        return defaultBehavior;
    }

    public void setDefaultBehavior ( boolean defaultBehavior )
    {
        this.defaultBehavior = defaultBehavior;
    }

    @Override
    public int getSourceActions ( JComponent c )
    {
        return COPY;
    }

    @Override
    protected Transferable createTransferable ( JComponent c )
    {
        return new StringSelection ( getText () );
    }
}