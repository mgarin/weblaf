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

package com.alee.demo.content.chooser.dialog;


import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.laf.button.WebButton;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author Michka Popoff
 */

public class JFileChooserExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "jfilechooser";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "filechooser";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    public WikiPage getWikiPage ()
    {
        return new OracleWikiPage ( "How to Use File Choosers", "filechooser" );
    }

    @Override
    protected java.util.List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList ( new OpenFileChooserDialog( StyleId.filechooser ),
                new SaveFileChooserDialog ( StyleId.filechooser ) );
    }

    /**
     * File Chooser dialog preview (open).
     */
    protected class OpenFileChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public OpenFileChooserDialog( final StyleId styleId )
        {
            super ( JFileChooserExample.this, "open", FeatureState.updated, styleId );
        }

        @Override
        protected java.util.List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebButton showChooser = new WebButton ( "Show open file chooser", new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    new JFileChooser().showOpenDialog ( ( Component ) e.getSource () );
                }
            } );
            return CollectionUtils.asList ( showChooser );
        }
    }

    /**
     * File Chooser dialog preview (save).
     */
    protected class SaveFileChooserDialog extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public SaveFileChooserDialog( final StyleId styleId )
        {
            super ( JFileChooserExample.this, "save", FeatureState.updated, styleId );
        }

        @Override
        protected java.util.List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebButton showChooser = new WebButton ( "Show save file chooser", new ActionListener ()
            {
                @Override
                public void actionPerformed ( final ActionEvent e )
                {
                    new JFileChooser().showSaveDialog ( ( Component ) e.getSource () );
                }
            } );
            return CollectionUtils.asList ( showChooser );
        }
    }
}