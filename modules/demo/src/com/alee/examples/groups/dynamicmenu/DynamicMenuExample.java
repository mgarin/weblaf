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

package com.alee.examples.groups.dynamicmenu;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.menu.DynamicMenuType;
import com.alee.extended.menu.WebDynamicMenu;
import com.alee.extended.menu.WebDynamicMenuItem;
import com.alee.extended.panel.AlignPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.combobox.WebComboBox;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.notification.NotificationManager;
import com.alee.utils.SwingUtils;
import com.alee.utils.swing.IntTextDocument;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Dynamic menu example.
 *
 * @author Mikle Garin
 */

public class DynamicMenuExample extends DefaultExample
{
    /**
     * Example UI elements.
     */
    private WebComboBox type;
    private WebComboBox hidingType;
    private WebTextField radius;
    private WebTextField itemsAmount;
    private WebCheckBox drawBorder;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Document pane";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled document pane";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        type = new WebComboBox ( DynamicMenuType.values (), DynamicMenuType.shutter );
        final GroupPanel tg = new GroupPanel ( 5, new WebLabel ( "Display animation:" ), type );

        hidingType = new WebComboBox ( DynamicMenuType.values (), DynamicMenuType.star );
        final GroupPanel htg = new GroupPanel ( 5, new WebLabel ( "Hide animation:" ), hidingType );

        radius = new WebTextField ( new IntTextDocument (), "70", 4 );
        final GroupPanel rg = new GroupPanel ( 5, new WebLabel ( "Menu radius:" ), radius );

        itemsAmount = new WebTextField ( new IntTextDocument (), "5", 4 );
        final GroupPanel iag = new GroupPanel ( 5, new WebLabel ( "Items amount:" ), itemsAmount );

        drawBorder = new WebCheckBox ( "Show custom border", true );

        final WebPanel clickPanel = new WebPanel ( true );
        clickPanel.setWebColoredBackground ( false );
        clickPanel.setShadeWidth ( 20 );
        clickPanel.setBackground ( Color.WHITE );
        clickPanel.add ( new WebLabel ( "Click with left mouse button here to show menu", WebLabel.CENTER ) );
        clickPanel.addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( final MouseEvent e )
            {
                if ( SwingUtils.isLeftMouseButton ( e ) )
                {
                    createMenu ().showMenu ( e.getComponent (), e.getPoint () );
                }
            }
        } );

        final GroupPanel controls = new GroupPanel ( 15, tg, htg, rg, iag, drawBorder );
        final AlignPanel alignPanel = new AlignPanel ( controls, SwingConstants.CENTER, SwingConstants.CENTER );
        return new GroupPanel ( GroupingType.fillLast, 10, false, alignPanel, clickPanel ).setMargin ( 10 );
    }

    /**
     * Returns new dynamic menu.
     *
     * @return new dynamic menu
     */
    protected WebDynamicMenu createMenu ()
    {
        final WebDynamicMenu menu = new WebDynamicMenu ();
        menu.setType ( ( DynamicMenuType ) type.getSelectedItem () );
        menu.setHideType ( ( DynamicMenuType ) hidingType.getSelectedItem () );
        menu.setRadius ( Integer.parseInt ( radius.getText () ) );
        menu.setStepProgress ( 0.06f );

        final int amount = Integer.parseInt ( itemsAmount.getText () );
        for ( int i = 0; i < amount; i++ )
        {
            final int number = i;
            final ImageIcon icon = loadIcon ( ( i % 10 + 1 ) + ".png" );
            final WebDynamicMenuItem item = new WebDynamicMenuItem ( icon, new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    final WebLookAndFeelDemo p = WebLookAndFeelDemo.getInstance ();
                    NotificationManager.showInnerNotification ( p, "Menu #" + number + " clicked", icon ).setDisplayTime ( 3000 );
                }
            } );
            item.setMargin ( new Insets ( 8, 8, 8, 8 ) );
            item.setPaintBorder ( drawBorder.isSelected () );
            menu.addItem ( item );
        }

        return menu;
    }
}