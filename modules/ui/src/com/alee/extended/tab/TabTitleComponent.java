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

package com.alee.extended.tab;

import com.alee.api.annotations.NotNull;
import com.alee.extended.label.WebStyledLabel;
import com.alee.laf.button.WebButton;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.tabbedpane.TabComponent;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.lang.ref.WeakReference;

/**
 * Default component for {@link WebDocumentPane} tab titles.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see DefaultTabTitleComponentProvider
 * @see TabTitleComponentProvider
 * @see WebDocumentPane
 */
public class TabTitleComponent<T extends DocumentData> extends WebPanel implements TabComponent, UIResource
{
    /**
     * Title {@link JComponent}.
     */
    protected JComponent title;

    /**
     * Tab close {@link AbstractButton}.
     */
    protected AbstractButton closeButton;

    /**
     * Constructs new {@link TabTitleComponent}.
     *
     * @param paneData     {@link PaneData} containing document
     * @param document     {@link DocumentData} of the document to create tab title component for
     * @param mouseAdapter {@link MouseAdapter} that forwards all mouse events to tabbed pane
     */
    public TabTitleComponent ( @NotNull final PaneData<T> paneData, @NotNull final T document,
                               @NotNull final MouseAdapter mouseAdapter )
    {
        super ( StyleId.documentpaneTabPanel.at ( paneData.getTabbedPane () ), new BorderLayout ( 2, 0 ) );

        addMouseListener ( mouseAdapter );
        addMouseMotionListener ( mouseAdapter );

        // Document title label
        title = createTitle ( paneData, document, mouseAdapter );
        add ( title, BorderLayout.CENTER );

        // Document close button
        if ( paneData.getDocumentPane ().isClosable () && document.isClosable () )
        {
            add ( createCloseButton ( paneData, document ), BorderLayout.LINE_END );
        }
    }

    /**
     * Returns newly created tab title {@link JComponent}.
     *
     * @param paneData     {@link PaneData} containing opened document
     * @param document     {@link DocumentData} to create tab title {@link JComponent} for
     * @param mouseAdapter mouse adapter that forwards all mouse events to tabbed pane
     * @return newly created tab title {@link JComponent}
     */
    @NotNull
    protected JComponent createTitle ( @NotNull final PaneData<T> paneData, @NotNull final T document,
                                       @NotNull final MouseAdapter mouseAdapter )
    {
        final StyleId titleStyleId = StyleId.documentpaneTabTitle.at ( this );
        final WebStyledLabel titleLabel = new WebStyledLabel ( titleStyleId, document.getTitle (), document.getIcon () );
        titleLabel.setForeground ( document.getForeground () );
        titleLabel.addMouseListener ( mouseAdapter );
        titleLabel.addMouseMotionListener ( mouseAdapter );
        return titleLabel;
    }

    /**
     * Returns newly created tab close {@link AbstractButton}.
     *
     * @param paneData {@link PaneData} containing opened document
     * @param document document to create tab close {@link AbstractButton} for
     * @return newly created tab close {@link AbstractButton}
     */
    @NotNull
    protected AbstractButton createCloseButton ( @NotNull final PaneData<T> paneData, @NotNull final T document )
    {
        final WeakReference<T> weakDocument = new WeakReference<T> ( document );
        final StyleId closeButtonStyleId = StyleId.documentpaneTabCloseButton.at ( this );
        final WebButton closeButton = new WebButton ( closeButtonStyleId, Icons.crossSmall, Icons.crossSmallHover );
        closeButton.addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                paneData.close ( weakDocument.get () );
            }
        } );
        return closeButton;
    }

    @Override
    public Icon getIcon ()
    {
        return title instanceof JLabel ? ( ( JLabel ) title ).getIcon () : null;
    }

    @Override
    public String getTitle ()
    {
        return title instanceof JLabel ? ( ( JLabel ) title ).getText () : null;
    }
}