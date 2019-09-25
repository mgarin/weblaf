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

package com.alee.demo.content.chooser.field;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.extended.pathfield.WebPathField;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.io.File;
import java.util.List;

/**
 * @author Michka Popoff
 */
public class WebPathFieldExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "webpathfield";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "pathfield";
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
                new PathField ( StyleId.pathfield )
        );
    }

    /**
     * Path field preview.
     */
    protected class PathField extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         */
        public PathField ( final StyleId styleId )
        {
            super ( WebPathFieldExample.this, "basic", FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebPathField pathField = new WebPathField ( getStyleId () );
            pathField.setSelectedPath ( new File ( System.getProperty ( "user.home" ) ) );
            pathField.setPreferredWidth ( 300 );
            return CollectionUtils.asList ( pathField );
        }
    }
}