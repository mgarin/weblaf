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
import com.alee.demo.api.example.*;
import com.alee.extended.list.CheckBoxCellData;
import com.alee.extended.list.CheckBoxListModel;
import com.alee.extended.list.WebCheckBoxList;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebCheckBoxListExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webcheckboxlist";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "checkboxlist";
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
                new BasicList ( StyleId.checkboxlist ),
                new ScrollableList ( StyleId.checkboxlist ),
                new EditableList ( StyleId.checkboxlist )
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
            super ( WebCheckBoxListExample.this, "basic", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebCheckBoxList list = new WebCheckBoxList ( getStyleId (), createModel ( createShortData () ) );
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
            super ( WebCheckBoxListExample.this, "scrollable", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebCheckBoxList list = new WebCheckBoxList ( getStyleId (), createModel ( createLongData () ) );
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
            super ( WebCheckBoxListExample.this, "editable", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebCheckBoxList list = new WebCheckBoxList ( getStyleId (), createModel ( createLongData () ) );
            list.setVisibleRowCount ( 4 );
            list.setEditable ( true );
            return CollectionUtils.asList ( new WebScrollPane ( list ) );
        }
    }

    /**
     * Returns sample check box list model.
     *
     * @param data sample data
     * @return sample check box list model
     */
    protected static CheckBoxListModel createModel ( final List<String> data )
    {
        final CheckBoxListModel model = new CheckBoxListModel ();
        for ( final String element : data )
        {
            model.add ( new CheckBoxCellData ( element ) );
        }
        return model;
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