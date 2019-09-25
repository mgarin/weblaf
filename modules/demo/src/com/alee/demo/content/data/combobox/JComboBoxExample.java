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

package com.alee.demo.content.data.combobox;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.demo.api.example.wiki.OracleWikiPage;
import com.alee.demo.api.example.wiki.WikiPage;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class JComboBoxExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "jcombobox";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "combobox";
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
        return new OracleWikiPage ( "How to Use Comboboxes", "combobox" );
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicCombobox ( StyleId.combobox, "basic" ),
                new BasicCombobox ( StyleId.comboboxHover, "hover" ),
                new BasicCombobox ( StyleId.comboboxUndecorated, "undecorated" )
        );
    }

    /**
     * Basic combobox preview.
     */
    protected class BasicCombobox extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         * @param id      preview ID
         */
        public BasicCombobox ( final StyleId styleId, final String id )
        {
            super ( JComboBoxExample.this, id, FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final JComboBox simple = new JComboBox ( createData () );
            simple.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );

            final JComboBox editable = new JComboBox ( createData () );
            editable.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            editable.setEditable ( true );

            return CollectionUtils.asList ( simple, editable );
        }
    }

    /**
     * Returns sample data.
     *
     * @return sample data
     */
    protected static String[] createData ()
    {
        return TextUtils.numbered ( "Combobox element %s", 1, 25 ).toArray ( new String[ 25 ] );
    }
}