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

package com.alee.examples.content;

import com.alee.global.StyleConstants;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.WebTabbedPane;
import com.alee.utils.CollectionUtils;
import com.alee.utils.SwingUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.reflection.JarEntry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 06.03.12 Time: 13:32
 */

public class ViewTabbedPane extends WebTabbedPane
{
    public static final ImageIcon REMOVE_ICON = new ImageIcon ( ViewTabbedPane.class.getResource ( "icons/remove.png" ) );

    public static final String ID_PREFIX = "VTP";

    private List<ViewListener> viewListeners = new ArrayList<ViewListener> ( 1 );

    private List<String> ids = new ArrayList<String> ();
    private List<JarEntry> data = new ArrayList<JarEntry> ();

    public ViewTabbedPane ()
    {
        super ( WebTabbedPane.TOP );
        setTabInsets ( new Insets ( 2, 3, 1, 1 ) );

        addMouseListener ( new MouseAdapter ()
        {
            @Override
            public void mousePressed ( MouseEvent e )
            {
                if ( SwingUtilities.isMiddleMouseButton ( e ) )
                {
                    int index = getUI ().tabForCoordinate ( ViewTabbedPane.this, e.getX (), e.getY () );
                    if ( index != -1 )
                    {
                        removeTabAt ( index );
                    }
                }
            }
        } );
    }

    public boolean isOpenedEntry ( JarEntry entry )
    {
        return data.contains ( entry );
    }

    public void selectEntry ( JarEntry entry )
    {
        if ( isOpenedEntry ( entry ) )
        {
            setSelectedIndex ( data.indexOf ( entry ) );
        }
    }

    public void viewEntry ( JarEntry entry, Component viewer )
    {
        if ( isOpenedEntry ( entry ) )
        {
            closeEntry ( entry );
        }

        // Creating special tab content
        WebPanel content = new WebPanel ();
        content.add ( new TabAreaSeparator (), BorderLayout.NORTH );
        content.add ( viewer, BorderLayout.CENTER );

        // Inserting tab
        int index = getSelectedIndex () + 1;
        super.insertTab ( entry.getName (), entry.getIcon (), content, null, index );

        // Creating closable tab title
        String id = TextUtils.generateId ( ID_PREFIX );
        setTabComponentAt ( index, createRemovableTitle ( entry.getName (), entry.getIcon (), id ) );
        ids.add ( index, id );
        data.add ( index, entry );

        // Selecting new tab
        setSelectedIndex ( index );

        // Informing about new tab
        fireViewOpened ( data.get ( index ) );
    }

    private class TabAreaSeparator extends JComponent
    {
        @Override
        protected void paintComponent ( Graphics g )
        {
            g.setColor ( new Color ( 237, 237, 237 ) );
            g.fillRect ( 0, 0, getWidth (), getHeight () - 1 );
            g.setColor ( StyleConstants.darkBorderColor );
            g.drawLine ( 0, getHeight () - 1, getWidth () - 1, getHeight () - 1 );
        }

        @Override
        public Dimension getPreferredSize ()
        {
            return new Dimension ( 0, 4 );
        }
    }

    public void closeEntry ( JarEntry entry )
    {
        if ( data.contains ( entry ) )
        {
            removeTabAt ( data.indexOf ( entry ) );
        }
    }

    public JarEntry getSelectedEntry ()
    {
        int index = getSelectedIndex ();
        return index != -1 ? data.get ( index ) : null;
    }

    //    public void insertTab ( String title, Icon icon, Component component, String tip, int index )
    //    {
    //        super.insertTab ( title, icon, component, tip, index );
    //
    //        String id = TextUtils.generateId ( ID_PREFIX );
    //        setTabComponentAt ( index, createRemovableTitle ( title, icon, id ) );
    //        ids.add ( index, id );
    //    }

    private Component createRemovableTitle ( String title, Icon icon, final String id )
    {
        WebPanel removableTitle = new WebPanel ();
        removableTitle.setOpaque ( false );

        WebLabel titleLabel = new WebLabel ( title, icon );
        titleLabel.setMargin ( 0, 2, 0, 4 );
        removableTitle.add ( titleLabel, BorderLayout.CENTER );

        WebButton remove = WebButton.createIconWebButton ( REMOVE_ICON, StyleConstants.smallRound, 2, 2, true );
        remove.setFocusable ( false );
        remove.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( ActionEvent e )
            {
                removeTabAt ( ids.indexOf ( id ) );
            }
        } );
        removableTitle.add ( remove, BorderLayout.LINE_END );

        // Copying component orientation from tabbed pane
        SwingUtils.copyOrientation ( ViewTabbedPane.this, removableTitle );

        return removableTitle;
    }

    @Override
    public void removeTabAt ( int index )
    {
        super.removeTabAt ( index );

        JarEntry removed = data.get ( index );
        ids.remove ( index );
        data.remove ( index );
        fireViewClosed ( removed );
    }

    public List<ViewListener> getViewListeners ()
    {
        return viewListeners;
    }

    public void setViewListeners ( List<ViewListener> viewListeners )
    {
        this.viewListeners = viewListeners;
    }

    public void addViewListener ( ViewListener listener )
    {
        this.viewListeners.add ( listener );
    }

    public void removeViewListener ( ViewListener listener )
    {
        this.viewListeners.remove ( listener );
    }

    private void fireViewOpened ( JarEntry entry )
    {
        for ( ViewListener listener : CollectionUtils.copy ( viewListeners ) )
        {
            listener.viewOpened ( entry );
        }
    }

    private void fireViewClosed ( JarEntry entry )
    {
        for ( ViewListener listener : CollectionUtils.copy ( viewListeners ) )
        {
            listener.viewClosed ( entry );
        }
    }
}