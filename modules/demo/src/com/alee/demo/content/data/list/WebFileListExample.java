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

package com.alee.demo.content.data.list;

import com.alee.demo.api.example.*;
import com.alee.extended.list.WebFileList;
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
public class WebFileListExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "webfilelist";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "filelist";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new IconsFileList ( StyleId.filelistIcons ),
                new TilesFileList ( StyleId.filelistTiles ),
                new ScrollableList ( StyleId.filelistIcons ),
                new EditableList ( StyleId.filelistTiles )
        );
    }

    /**
     * Basic icons file list preview.
     */
    protected class IconsFileList extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public IconsFileList ( final StyleId styleId )
        {
            super ( WebFileListExample.this, "icons", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final File[] files = FileUtils.getUserHome ().listFiles ();
            final File[] model = files.length > 28 ? Arrays.copyOfRange ( files, 0, 28 ) : files;
            final WebFileList list = new WebFileList ( getStyleId (), model );
            return CollectionUtils.asList ( list );
        }
    }

    /**
     * Basic tiles file list preview.
     */
    protected class TilesFileList extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public TilesFileList ( final StyleId styleId )
        {
            super ( WebFileListExample.this, "tiles", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final File[] files = FileUtils.getUserHome ().listFiles ();
            final File[] model = files.length > 15 ? Arrays.copyOfRange ( files, 0, 15 ) : files;
            final WebFileList list = new WebFileList ( getStyleId (), model );
            return CollectionUtils.asList ( list );
        }
    }

    /**
     * Scrollable file list preview.
     */
    protected class ScrollableList extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public ScrollableList ( final StyleId styleId )
        {
            super ( WebFileListExample.this, "scrollable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebFileList list = new WebFileList ( getStyleId (), FileUtils.getUserHome () );
            return CollectionUtils.asList ( new WebScrollPane ( list ) );
        }
    }

    /**
     * Editable file list preview.
     */
    protected class EditableList extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public EditableList ( final StyleId styleId )
        {
            super ( WebFileListExample.this, "editable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebFileList list = new WebFileList ( getStyleId (), FileUtils.getUserHome () );
            list.setEditable ( true );
            return CollectionUtils.asList ( new WebScrollPane ( list ) );
        }
    }
}