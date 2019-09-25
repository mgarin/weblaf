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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.demo.api.example.*;
import com.alee.laf.list.ListCellArea;
import com.alee.laf.list.ListToolTipProvider;
import com.alee.laf.list.WebList;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.language.LM;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebListExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "weblist";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "list";
    }

    @NotNull
    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.extended;
    }

    @NotNull
    @Override
    protected List<Preview> createPreviews ()
    {
        return CollectionUtils.<Preview>asList (
                new BasicList ( StyleId.list ),
                new ScrollableList ( StyleId.list ),
                new EditableList ( StyleId.list ),
                new ListTooltips ( StyleId.list )
        );
    }

    /**
     * Basic list preview.
     */
    protected class BasicList extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public BasicList ( final StyleId styleId )
        {
            super ( WebListExample.this, "basic", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebList list = new WebList ( getStyleId (), createShortData () );
            return CollectionUtils.asList ( list );
        }
    }

    /**
     * Scrollable list preview.
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
            super ( WebListExample.this, "scrollable", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebList list = new WebList ( getStyleId (), createLongData () );
            list.setVisibleRowCount ( 4 );
            return CollectionUtils.asList ( new WebScrollPane ( list ) );
        }
    }

    /**
     * Editable list preview.
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
            super ( WebListExample.this, "editable", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebList list = new WebList ( getStyleId (), createLongData () );
            list.setVisibleRowCount ( 4 );
            list.setEditable ( true );
            return CollectionUtils.asList ( new WebScrollPane ( list ) );
        }
    }

    /**
     * Custom list tooltips preview.
     */
    protected class ListTooltips extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public ListTooltips ( final StyleId styleId )
        {
            super ( WebListExample.this, "tooltips", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebList list = new WebList ( getStyleId (), createLongData () );
            list.setVisibleRowCount ( 4 );
            list.setToolTipProvider ( new ListToolTipProvider<String> ()
            {
                @Nullable
                @Override
                protected String getToolTipText ( @NotNull final JList list,
                                                  @NotNull final ListCellArea<String, JList> area )
                {
                    return LM.get ( getPreviewLanguageKey ( "cell" ), area.index (), area.getValue ( list ) );
                }
            } );
            return CollectionUtils.asList ( new WebScrollPane ( list ) );
        }
    }

    /**
     * Returns sample short list data.
     *
     * @return sample short list data
     */
    protected static List<String> createShortData ()
    {
        return TextUtils.numbered ( "List element %s", 1, 3 );
    }

    /**
     * Returns sample long list data.
     *
     * @return sample long list data
     */
    protected static List<String> createLongData ()
    {
        return TextUtils.numbered ( "List element %s", 1, 12 );
    }
}