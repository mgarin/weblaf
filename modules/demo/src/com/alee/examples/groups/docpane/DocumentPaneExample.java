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

package com.alee.examples.groups.docpane;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.panel.GroupingType;
import com.alee.extended.tab.DocumentData;
import com.alee.extended.tab.WebDocumentPane;
import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.utils.TextUtils;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Document pane example.
 *
 * @author Mikle Garin
 */

public class DocumentPaneExample extends DefaultExample
{
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
        final WebLabel title = new WebLabel ( "You can drag, close and split tabs in this document pane", loadIcon ( "info.png" ) );

        final WebDocumentPane pane = new WebDocumentPane ();
        pane.setUndecorated ( false );
        addDocuments ( pane );

        final WebButton add = new WebButton ( loadIcon ( "add.png" ), new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                addDocuments ( pane );
            }
        } );
        final WebButton clear = new WebButton ( loadIcon ( "clear.png" ), new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                pane.closeAll ();
            }
        } );
        final WebButton restore = new WebButton ( loadIcon ( "restore.png" ), new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                pane.closeAll ();
                addDocuments ( pane );
            }
        } );

        final GroupPanel titlePanel = new GroupPanel ( GroupingType.fillFirst, 5, title, add, clear, restore );
        return new GroupPanel ( GroupingType.fillLast, 10, false, titlePanel, pane ).setMargin ( 10 );
    }

    /**
     * Adds a few documents into the document pane.
     *
     * @param pane document pane
     */
    protected void addDocuments ( final WebDocumentPane pane )
    {
        pane.openDocument ( new DocumentData ( TextUtils.generateId (), loadIcon ( "1.png" ), "Excel doc", null, new WebLabel () ) );
        pane.openDocument ( new DocumentData ( TextUtils.generateId (), loadIcon ( "2.png" ), "PDF doc", null, new WebLabel () ) );
        pane.openDocument ( new DocumentData ( TextUtils.generateId (), loadIcon ( "3.png" ), "Office doc", null, new WebLabel () ) );
        pane.openDocument ( new DocumentData ( TextUtils.generateId (), loadIcon ( "4.png" ), "Music clip", null, new WebLabel () ) );
        pane.openDocument ( new DocumentData ( TextUtils.generateId (), loadIcon ( "5.png" ), "Stamp file", null, new WebLabel () ) );
        pane.openDocument ( new DocumentData ( TextUtils.generateId (), loadIcon ( "6.png" ), "Word doc", null, new WebLabel () ) );
    }
}