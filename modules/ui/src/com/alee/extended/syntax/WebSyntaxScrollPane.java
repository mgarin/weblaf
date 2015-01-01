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

package com.alee.extended.syntax;

import com.alee.laf.panel.WebPanelUI;
import com.alee.laf.scroll.WebScrollBarUI;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.scroll.WebScrollPaneUI;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;

/**
 * @author Mikle Garin
 */

public class WebSyntaxScrollPane extends RTextScrollPane
{
    public WebSyntaxScrollPane ()
    {
        super ();
    }

    public WebSyntaxScrollPane ( final Component comp )
    {
        super ( comp );
    }

    public WebSyntaxScrollPane ( final Component comp, final boolean drawBorder )
    {
        super ( comp );
        setDrawBorder ( drawBorder );
    }

    public WebSyntaxScrollPane ( final Component comp, final boolean drawBorder, final boolean drawInnerBorder )
    {
        super ( comp );
        setDrawBorder ( drawBorder );
        setDrawInnerBorder ( drawInnerBorder );
    }

    public WebSyntaxScrollPane ( final Component comp, final boolean lineNumbers, final Color lineNumberColor )
    {
        super ( comp, lineNumbers, lineNumberColor );
        initialize ();
    }

    protected void initialize ()
    {
        setGutterStyleId ( "editor-gutter" );
        setVerticalScrollBarPolicy ( WebScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED );
    }

    public void setDrawBorder ( final boolean drawBorder )
    {
        if ( getUI () instanceof WebScrollPaneUI )
        {
            ( ( WebScrollPaneUI ) getUI () ).setDrawBorder ( drawBorder );
        }
    }

    public void setDrawInnerBorder ( final boolean drawInnerBorder )
    {
        final JScrollBar vsb = getVerticalScrollBar ();
        if ( vsb.getUI () instanceof WebScrollBarUI )
        {
            ( ( WebScrollBarUI ) vsb.getUI () ).setPaintTrack ( drawInnerBorder );
        }
        final JScrollBar hsb = getHorizontalScrollBar ();
        if ( hsb.getUI () instanceof WebScrollBarUI )
        {
            ( ( WebScrollBarUI ) hsb.getUI () ).setPaintTrack ( drawInnerBorder );
        }
    }

    public void setGutterStyleId ( final String id )
    {
        if ( getGutter ().getUI () instanceof WebPanelUI )
        {
            ( ( WebPanelUI ) getGutter ().getUI () ).setStyleId ( id );
        }
    }
}