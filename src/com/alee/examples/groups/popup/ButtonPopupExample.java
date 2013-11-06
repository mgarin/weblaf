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

package com.alee.examples.groups.popup;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.popup.PopupWay;
import com.alee.managers.popup.WebButtonPopup;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 20.08.12 Time: 15:07
 */

public class ButtonPopupExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Button popup";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled button popup";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Grid-layouted container
        TableLayout gridLayout = new TableLayout ( new double[][]{ { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } } );
        WebPanel gridPanel = new WebPanel ( gridLayout );

        // Popup buttons
        gridPanel.add ( new GroupPanel ( createPopupButton ( PopupWay.upLeft ), createPopupButton ( PopupWay.upCenter ),
                createPopupButton ( PopupWay.upRight ) ), "1,0" );
        gridPanel.add ( new GroupPanel ( false, createPopupButton ( PopupWay.leftUp ), createPopupButton ( PopupWay.leftCenter ),
                createPopupButton ( PopupWay.leftDown ) ), "0,1" );
        gridPanel.add ( new GroupPanel ( false, createPopupButton ( PopupWay.rightUp ), createPopupButton ( PopupWay.rightCenter ),
                createPopupButton ( PopupWay.rightDown ) ), "2,1" );
        gridPanel.add ( new GroupPanel ( createPopupButton ( PopupWay.downLeft ), createPopupButton ( PopupWay.downCenter ),
                createPopupButton ( PopupWay.downRight ) ), "1,2" );

        return new GroupPanel ( gridPanel );
    }

    private WebButton createPopupButton ( PopupWay way )
    {
        // Button that calls for popup
        WebButton showPopup = new WebButton ( getButtonIcon ( way ) );

        // Popup itself
        WebButtonPopup popup = new WebButtonPopup ( showPopup, way );

        // Sample popup content
        WebLabel label = new WebLabel ( "Sample label", WebLabel.CENTER );
        WebTextField field = new WebTextField ( "Sample field", 10 );
        field.setHorizontalAlignment ( SwingConstants.CENTER );
        GroupPanel content = new GroupPanel ( 5, false, label, field );
        content.setMargin ( 15 );

        // Setup popup content
        popup.setContent ( content );

        // Component focused by default
        popup.setDefaultFocusComponent ( field );

        return showPopup;
    }

    private ImageIcon getButtonIcon ( PopupWay way )
    {
        return loadIcon ( "way/" + way + ".png" );
    }
}