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
import com.alee.extended.drag.ImageDragHandler;
import com.alee.extended.image.WebImage;
import com.alee.extended.image.WebImageDrop;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.managers.language.data.TooltipWay;
import com.alee.managers.tooltip.TooltipManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * User: mgarin Date: 21.02.12 Time: 15:37
 */

public class WebImageDropExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Images drop";
    }

    @Override
    public String getDescription ()
    {
        return "WebImageDrop example";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        ImageIcon i1 = loadIcon ( "1.jpg" );
        ImageIcon i2 = loadIcon ( "2.jpg" );
        ImageIcon i3 = loadIcon ( "3.jpg" );

        WebImage image1 = new WebImage ( i1 );
        image1.setTransferHandler ( new ImageDragHandler ( image1, i1 ) );

        WebImage image2 = new WebImage ( i2 );
        image2.setTransferHandler ( new ImageDragHandler ( image2, i2 ) );

        WebImage image3 = new WebImage ( i3 );
        image3.setTransferHandler ( new ImageDragHandler ( image3, i3 ) );

        GroupPanel imageGroup = new GroupPanel ( 4, image1, image2, image3 );


        WebLabel dragLabel = new WebLabel ( "Try dragging images into fields below", loadIcon ( "drag.png" ), WebLabel.CENTER );
        dragLabel.setMargin ( 10, 0, 10, 0 );


        MouseAdapter ma = new MouseAdapter ()
        {
            @Override
            public void mouseClicked ( MouseEvent e )
            {
                if ( e.getClickCount () == 2 )
                {
                    ( ( WebImageDrop ) e.getComponent () ).setImage ( null );
                }
            }
        };

        WebImageDrop x64 = new WebImageDrop ( 64, 64 );
        TooltipManager.setTooltip ( x64, "Double-click to clear image", TooltipWay.trailing );
        x64.addMouseListener ( ma );

        WebImageDrop x48 = new WebImageDrop ( 48, 48 );
        TooltipManager.setTooltip ( x48, "Double-click to clear image", TooltipWay.trailing );
        x48.addMouseListener ( ma );

        WebImageDrop x32 = new WebImageDrop ( 32, 32 );
        TooltipManager.setTooltip ( x32, "Double-click to clear image", TooltipWay.trailing );
        x32.addMouseListener ( ma );

        WebImageDrop x24 = new WebImageDrop ( 24, 24 );
        TooltipManager.setTooltip ( x24, "Double-click to clear image", TooltipWay.trailing );
        x24.addMouseListener ( ma );

        WebImageDrop x16 = new WebImageDrop ( 16, 16 );
        TooltipManager.setTooltip ( x16, "Double-click to clear image", TooltipWay.trailing );
        x16.addMouseListener ( ma );

        GroupPanel dropGroup = new GroupPanel ( 4, x64, x48, x32, x24, x16 );


        return new GroupPanel ( 4, false, imageGroup, dragLabel, dropGroup );
    }
}