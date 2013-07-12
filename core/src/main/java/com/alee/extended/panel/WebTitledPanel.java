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

import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * User: mgarin Date: 12.04.12 Time: 14:03
 */

public class WebTitledPanel extends WebPanel
{
    private WebPanel title;
    private WebPanel content;

    public WebTitledPanel ()
    {
        super ( true );
        setMargin ( 0 );
        setWebColored ( false );

        title = new WebPanel ( true );
        title.setDrawSides ( false, false, true, false );
        title.setMargin ( 0 );
        title.setShadeWidth ( 0 );
        add ( title, BorderLayout.NORTH );

        content = new WebPanel ();
        content.setMargin ( 0 );
        add ( content, BorderLayout.CENTER );
    }

    public WebPanel getTitlePanel ()
    {
        return title;
    }

    public WebPanel getContentPanel ()
    {
        return content;
    }

    public Component getTitle ()
    {
        return title.getComponentCount () > 0 ? title.getComponent ( 0 ) : null;
    }

    public void setTitle ( Component titleComponent )
    {
        title.removeAll ();
        title.add ( titleComponent );
        title.revalidate ();
        title.repaint ();
    }

    public Component getContent ()
    {
        return content.getComponentCount () > 0 ? content.getComponent ( 0 ) : null;
    }

    public void setContent ( Component contentComponent )
    {
        content.removeAll ();
        content.add ( contentComponent );
        content.revalidate ();
        content.repaint ();
    }
}