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

package com.alee.examples.groups.image;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.GroupPanel;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;

import java.awt.*;

/**
 * User: mgarin Date: 21.02.12 Time: 13:28
 */

public class WebImageExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Images";
    }

    @Override
    public String getDescription ()
    {
        return "WebImage examples";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple image
        WebImage webImage1 = new WebImage ( loadIcon ( "preferred.png" ) );
        TooltipManager.setTooltip ( webImage1, "Simple preferred-size image", TooltipWay.up );

        // Repeated image
        WebImage webImage2 = new WebImage ( loadIcon ( "repeated.png" ) );
        webImage2.setDisplayType ( DisplayType.repeat );
        webImage2.setVerticalAlignment ( WebImage.BOTTOM );
        webImage2.setHorizontalAlignment ( WebImage.RIGHT );
        webImage2.setPreferredSize ( webImage1.getPreferredSize () );
        TooltipManager.setTooltip ( webImage2, "Repeated and bottom-right aligned image", TooltipWay.up );

        // Filling image
        WebImage webImage3 = new WebImage ( loadIcon ( "fill.png" ) );
        webImage3.setDisplayType ( DisplayType.fitComponent );
        webImage3.setPreferredSize ( webImage1.getPreferredSize () );
        TooltipManager.setTooltip ( webImage3,
                "Scaled to fit component image (" + webImage3.getImageWidth () + "x" + webImage3.getImageHeight () +
                        " px)", TooltipWay.up
        );

        return new GroupPanel ( 4, webImage1, webImage2, webImage3 );
    }
}