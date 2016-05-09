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

import com.alee.demo.api.*;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class JListExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "jlist";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "list";
    }

    @Override
    public FeatureType getFeatureType ()
    {
        return FeatureType.swing;
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final BasicList basic = new BasicList ( StyleId.list );
        final ScrollableList scrollable = new ScrollableList ( StyleId.list );
        return CollectionUtils.<Preview>asList ( basic, scrollable );
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
            super ( JListExample.this, "basic", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JList list = new JList ( createShortData () );
            list.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
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
            super ( JListExample.this, "scrollable", FeatureState.updated, styleId );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ( final StyleId containerStyleId )
        {
            final JList list = new JList ( createLongData () );
            list.putClientProperty ( StyleId.STYLE_PROPERTY, getStyleId () );
            list.setVisibleRowCount ( 4 );
            return CollectionUtils.asList ( new WebScrollPane ( list ) );
        }
    }

    /**
     * Returns sample short data.
     *
     * @return sample short data
     */
    protected static String[] createShortData ()
    {
        return TextUtils.numbered ( "List element %s", 1, 3 ).toArray ( new String[ 3 ] );
    }

    /**
     * Returns sample long data.
     *
     * @return sample long data
     */
    protected static String[] createLongData ()
    {
        return TextUtils.numbered ( "List element %s", 1, 12 ).toArray ( new String[ 12 ] );
    }
}