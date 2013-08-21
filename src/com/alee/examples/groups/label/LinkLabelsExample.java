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

package com.alee.examples.groups.label;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.label.WebLinkLabel;
import com.alee.extended.panel.GroupPanel;
import com.alee.utils.FileUtils;

import java.awt.*;

/**
 * User: mgarin Date: 24.01.12 Time: 13:50
 */

public class LinkLabelsExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Link labels";
    }

    @Override
    public String getDescription ()
    {
        return "Labels with extended link functionality";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Label with site link
        WebLinkLabel l = new WebLinkLabel ();
        l.setLink ( WebLookAndFeelDemo.WEBLAF_SITE );

        // Label with email link
        WebLinkLabel el = new WebLinkLabel ();
        el.setEmailLink ( WebLookAndFeelDemo.WEBLAF_EMAIL );

        // Label with file link
        WebLinkLabel fl = new WebLinkLabel ();
        fl.setFileLink ( FileUtils.getUserHome () );

        return new GroupPanel ( 4, false, new GroupPanel ( l ), new GroupPanel ( el ), new GroupPanel ( fl ) );
    }
}