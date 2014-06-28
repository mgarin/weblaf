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

package com.alee.examples.groups.popover;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.window.WebPopOver;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Customized pop-over dialog example.
 *
 * @author Mikle Garin
 */

public class CustomizedPopOverExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Customized pop-over dialog";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Customized web-styled pop-over dialog";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Button to display pop-over dialog below
        final WebButton showDialog = new WebButton ( "Show pop-over dialog", loadIcon ( "customized.png" ), new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                final WebPopOver popOver = new WebPopOver ( owner );
                popOver.setCloseOnFocusLoss ( true );
                popOver.setMargin ( 10 );
                popOver.setLayout ( new VerticalFlowLayout () );
                final WebImage icon = new WebImage ( WebLookAndFeel.getIcon ( 16 ) );
                final WebLabel titleLabel = new WebLabel ( "Pop-over dialog", WebLabel.CENTER );
                final WebButton closeButton = new WebButton ( loadIcon ( "cross2.png" ), new ActionListener ()
                {
                    @Override
                    public void actionPerformed ( final ActionEvent e )
                    {
                        popOver.dispose ();
                    }
                } ).setUndecorated ( true );
                popOver.add ( new GroupPanel ( GroupingType.fillMiddle, 4, icon, titleLabel, closeButton ).setMargin ( 0, 0, 10, 0 ) );
                popOver.add ( new WebLabel ( "1. This is a custom detached pop-over dialog" ) );
                popOver.add ( new WebLabel ( "2. You can move pop-over by dragging it" ) );
                popOver.add ( new WebLabel ( "3. Pop-over will get closed if loses focus" ) );
                popOver.add ( new WebLabel ( "4. Custom title added using standard components" ) );
                popOver.show ( owner );
            }
        } );
        return new GroupPanel ( showDialog );
    }
}