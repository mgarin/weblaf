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

package com.alee.demo.api.example.wiki;

import com.alee.demo.skin.DemoIcons;
import com.alee.demo.skin.DemoStyles;
import com.alee.extended.layout.HorizontalFlowLayout;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.style.StyleId;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public class NoWikiPage implements WikiPage
{
    @Override
    public String getTitle ()
    {
        return "demo.content.example.wiki.none";
    }

    @Override
    public String getAddress ()
    {
        return null;
    }

    @Override
    public JComponent createLink ()
    {
        final WebPanel link = new WebPanel ( StyleId.panelTransparent, new HorizontalFlowLayout ( 4, false ) );
        link.add ( new WebLabel ( DemoStyles.wikiLabel, getTitle (), DemoIcons.github16 ) );
        return link;
    }
}