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

package com.alee.demo.content.data.grid;

import com.alee.demo.api.*;
import com.alee.extended.filechooser.WebFileTable;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class WebFileTableExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "webfiletable";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "filetable";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicTable basic = new BasicTable ( StyleId.filetable );
        final ScrollableTable scrollable = new ScrollableTable ( StyleId.filetable );
        final EditableTable editable = new EditableTable ( StyleId.filetable );
        return CollectionUtils.<Preview>asList ( basic, scrollable, editable );
    }

    /**
     * Basic table preview.
     */
    protected class BasicTable extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public BasicTable ( final StyleId styleId )
        {
            super ( WebFileTableExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final File[] home = FileUtils.getUserHome ().listFiles ();
            final List<File> files = CollectionUtils.asList ( home.length > 4 ? Arrays.copyOfRange ( home, 0, 4 ) : home );
            final WebFileTable table = new WebFileTable ( getStyleId (), files );
            return CollectionUtils.asList ( table );
        }
    }

    /**
     * Scrollable table preview.
     */
    protected class ScrollableTable extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public ScrollableTable ( final StyleId styleId )
        {
            super ( WebFileTableExample.this, "scrollable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebFileTable table = new WebFileTable ( getStyleId () );
            table.setDisplayedDirectory ( FileUtils.getUserHome () );
            table.setVisibleRowCount ( 6 );
            return CollectionUtils.asList ( new WebScrollPane ( table ) );
        }
    }

    /**
     * Editable table preview.
     */
    protected class EditableTable extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public EditableTable ( final StyleId styleId )
        {
            super ( WebFileTableExample.this, "editable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final WebFileTable table = new WebFileTable ( getStyleId () );
            table.setDisplayedDirectory ( FileUtils.getUserHome () );
            table.setVisibleRowCount ( 6 );
            table.setEditable ( true );
            return CollectionUtils.asList ( new WebScrollPane ( table ) );
        }
    }
}