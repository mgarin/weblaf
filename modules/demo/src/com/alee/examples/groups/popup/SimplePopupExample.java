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
import com.alee.extended.image.WebImage;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.window.ComponentMoveAdapter;
import com.alee.laf.button.WebButton;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.combobox.WebComboBoxCellRenderer;
import com.alee.managers.popup.PopupManager;
import com.alee.managers.popup.PopupStyle;
import com.alee.managers.popup.WebPopup;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * User: mgarin Date: 15.08.12 Time: 18:49
 */

public class SimplePopupExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Simple popup";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled simple popup";
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Popup
        final WebPopup popup = new WebPopup ();
        popup.setMargin ( 20 );
        popup.add ( new WebImage ( loadIcon ( "move.png" ) ) );

        // Popup move mouse adapter
        ComponentMoveAdapter.install ( popup, popup );

        // Popup invoker button
        final WebButton showPopup = new WebButton ( "Show simple popup" );

        // Popup style chooser
        final WebComboBox popupStyle = new WebComboBox ( PopupStyle.values () );
        popupStyle.setSelectedItem ( PopupManager.getDefaultPopupStyle () );
        popupStyle.setRenderer ( new WebComboBoxCellRenderer ()
        {
            @Override
            public Component getListCellRendererComponent ( final JList list, final Object value, final int index, final boolean isSelected,
                                                            final boolean cellHasFocus )
            {
                return super.getListCellRendererComponent ( list, "Style: " + value, index, isSelected, cellHasFocus );
            }
        } );
        popupStyle.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                popup.setPopupStyle ( ( PopupStyle ) popupStyle.getSelectedItem () );
                popup.packPopup ();
            }
        } );

        // Popup show/hide action
        showPopup.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( popup.isShowing () )
                {
                    popup.hidePopup ();
                }
                else
                {
                    popup.showAsPopupMenu ( showPopup );
                }
            }
        } );

        return new GroupPanel ( 4, showPopup, popupStyle );
    }
}