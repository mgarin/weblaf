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

import com.alee.laf.button.WebButton;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.managers.icon.Icons;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.lang.ref.WeakReference;

/**
 * Default document tab title provider.
 * It is used in all WebDocumentPanes by default but can be easily replaced.
 *
 * @param <T> {@link DocumentData} type
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebDocumentPane">How to use WebDocumentPane</a>
 * @see TabTitleComponentProvider
 * @see WebDocumentPane
 */
public class DefaultTabTitleComponentProvider<T extends DocumentData> implements TabTitleComponentProvider<T>
{
    @Override
    public JComponent createTabTitleComponent ( final PaneData<T> paneData, final T document, final MouseAdapter mouseAdapter )
    {
        // Transparent title panel
        final WebPanel tabTitleComponent = new WebPanel ( StyleId.panelTransparent, new BorderLayout ( 2, 0 ) );
        tabTitleComponent.addMouseListener ( mouseAdapter );
        tabTitleComponent.addMouseMotionListener ( mouseAdapter );

        // Document title label
        tabTitleComponent.add ( createTitleLabel ( document, mouseAdapter ), BorderLayout.CENTER );

        // Document close button
        if ( paneData.getDocumentPane ().isClosable () && document.isClosable () )
        {
            tabTitleComponent.add ( createCloseButton ( paneData, document ), BorderLayout.LINE_END );
        }

        return tabTitleComponent;
    }

    /**
     * Returns newly created tab title label.
     *
     * @param document     document to create tab title component for
     * @param mouseAdapter mouse adapter that forwards all mouse events to tabbed pane
     * @return newly created tab title label
     */
    protected WebLabel createTitleLabel ( final T document, final MouseAdapter mouseAdapter )
    {
        final WebLabel titleLabel = new WebLabel ( document.getTitle (), document.getIcon () );
        titleLabel.setForeground ( document.getForeground () );
        titleLabel.addMouseListener ( mouseAdapter );
        titleLabel.addMouseMotionListener ( mouseAdapter );
        return titleLabel;
    }

    /**
     * Returns newly created tab close button.
     *
     * @param paneData PaneData containing document
     * @param document document to create tab title component for
     * @return newly created tab close button
     */
    protected WebButton createCloseButton ( final PaneData<T> paneData, final T document )
    {
        final WeakReference<T> weakDocument = new WeakReference<T> ( document );
        final StyleId closeButtonId = StyleId.documentpaneCloseButton.at ( paneData.getTabbedPane () );
        final WebButton closeButton = new WebButton ( closeButtonId, Icons.crossSmall, Icons.crossSmallHover );
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
}