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

package com.alee.demo.content.desktoppane;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.demo.content.SampleData;
import com.alee.managers.language.UILanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JInternalFrameExample extends AbstractPreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jinternalframe";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @NotNull
    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use Internal Frames", "internalframe" );
    }

    @NotNull
    @Override
    protected LayoutManager createPreviewLayout ()
    {
        return new BorderLayout ();
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new InternalFramePreview ()
        );
    }

    /**
     * Internal frame preview.
     */
    protected class InternalFramePreview extends AbstractPreview
    {
        /**
         * Constructs new preview.
         */
        public InternalFramePreview ()
        {
            super ( JInternalFrameExample.this, "desktop", FeatureState.updated );
        }

        @NotNull
        @Override
        protected JComponent createPreview ( @NotNull final List<Preview> previews, final int index )
        {
            // Configuring desktop pane
            final JDesktopPane desktopPane = new JDesktopPane ();
            desktopPane.putClientProperty ( StyleId.STYLE_PROPERTY, StyleId.desktoppaneTransparent );

            // Initializing desktop pane contents
            try
            {
                // Internal frames with simple content
                addInternalFrame ( desktopPane, 0 );
                addInternalFrame ( desktopPane, 1 );
                addInternalFrame ( desktopPane, 2 );
            }
            catch ( final PropertyVetoException ignored )
            {
                /**
                 * Unfortunately some JInternalFrame methods throw non-runtime exceptions.
                 * These exceptions are hidden in WebInternalFrame for convenience so you can use it instead.
                 */
            }

            return desktopPane;
        }

        /**
         * Adds another {@link JInternalFrame} into specified {@link JDesktopPane}.
         *
         * @param desktopPane {@link JDesktopPane}
         * @param index       index of {@link JInternalFrame} to add
         * @throws PropertyVetoException thrown when some changes were not allowed
         */
        protected void addInternalFrame ( final JDesktopPane desktopPane, final int index ) throws PropertyVetoException
        {
            final JInternalFrame internalFrame = new JInternalFrame ( null, true, true, true, true );
            internalFrame.setFrameIcon ( loadExampleIcon ( "frame16.png" ) );
            UILanguageManager.registerComponent ( internalFrame, getExampleLanguagePrefix () + "frame.title", index );

            final JTable table = new JTable ( SampleData.createLongTableModel ( true ) );
            final JScrollPane scrollPane = new JScrollPane ( table );
            scrollPane.setPreferredSize ( new Dimension ( 300, 150 ) );
            internalFrame.add ( scrollPane );

            final JButton internalFrameIcon = new JButton ( "", loadExampleIcon ( "frame32.png" ) );
            internalFrameIcon.setHorizontalTextPosition ( JButton.CENTER );
            internalFrameIcon.setVerticalTextPosition ( JButton.BOTTOM );
            UILanguageManager.registerComponent ( internalFrameIcon, getExampleLanguagePrefix () + "frame.button", index );
            internalFrameIcon.addActionListener ( new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    try
                    {
                        if ( internalFrame.isClosed () )
                        {
                            if ( internalFrame.getParent () == null )
                            {
                                desktopPane.add ( internalFrame );
                            }
                            internalFrame.setClosed ( false );
                            internalFrame.setVisible ( true );
                            internalFrame.setIcon ( false );
                        }
                        else
                        {
                            internalFrame.setIcon ( !internalFrame.isIcon () );
                        }
                    }
                    catch ( final PropertyVetoException ignored )
                    {
                        /**
                         * Unfortunately some JInternalFrame methods throw non-runtime exceptions.
                         * These exceptions are hidden in WebInternalFrame for convenience so you can use it instead.
                         */
                    }
                }
            } );
            internalFrameIcon.setBounds ( 25, 25 + 100 * index, 100, 75 );
            desktopPane.add ( internalFrameIcon );

            internalFrame.setClosed ( true );
            internalFrame.setLocation ( 150 + 25 * index, 25 + 25 * index );
            internalFrame.pack ();
        }

        @Override
        public void applyEnabled ( final boolean enabled )
        {
            // todo
        }
    }
}