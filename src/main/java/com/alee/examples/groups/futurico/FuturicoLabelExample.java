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

package com.alee.examples.groups.futurico;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.laf.label.WebLabel;
import com.alee.utils.XmlUtils;

import java.awt.*;

/**
 * User: mgarin Date: 14.03.12 Time: 14:19
 */

public class FuturicoLabelExample extends DefaultExample
{
    public String getTitle ()
    {
        return "Futurico label";
    }

    public String getDescription ()
    {
        return "Futurico-styled label";
    }

    public boolean isFillWidth ()
    {
        return true;
    }

    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Label styled with nine-patch icon painter
        WebLabel label = new WebLabel ( "Sample text within styled label", WebLabel.CENTER );
        label.setForeground ( Color.WHITE );
        label.setPainter ( XmlUtils.loadNinePatchIconPainter ( getResource ( "label.xml" ) ) );
        return label;
    }
}