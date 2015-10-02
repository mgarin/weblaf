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

package com.alee.demo.content.labels;

import com.alee.demo.api.AbstractExample;
import com.alee.demo.api.AbstractStylePreview;
import com.alee.demo.api.FeatureState;
import com.alee.demo.api.Preview;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */

public class LabelExample extends AbstractExample
{
    @Override
    public String getId ()
    {
        return "label";
    }

    @Override
    protected List<Preview> createPreviews ()
    {
        final SimpleLabel e1 = new SimpleLabel ( StyleId.label );
        return CollectionUtils.<Preview>asList ( e1 );
    }

    /**
     * Button preview.
     */
    protected class SimpleLabel extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param id preview style ID
         */
        public SimpleLabel ( final StyleId id )
        {
            super ( LabelExample.this, "simple", FeatureState.updated, id );
        }

        @Override
        protected JComponent createPreviewContent ( final StyleId id )
        {
            final WebLabel label = new WebLabel ( getStyleId (), "Simple text" );
            final WebLabel icon = new WebLabel ( getStyleId (), "With icon", WebLookAndFeel.getIcon ( 16 ) );
            return new GroupPanel ( id, 8, label, icon );
        }
    }
}