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
import com.alee.extended.language.LanguageChooser;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class LanguageChooserExample extends AbstractStylePreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "languagechooser";
    }

    @NotNull
    @Override
    protected String getStyleFileName ()
    {
        return "languagechooser";
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
                new BasicLanguageChooser ( StyleId.languagechooser, "basic" ),
                new BasicLanguageChooser ( StyleId.languagechooserHover, "hover" ),
                new BasicLanguageChooser ( StyleId.languagechooserUndecorated, "undecorated" )
        );
    }

    /**
     * Basic language chooser preview.
     */
    protected class BasicLanguageChooser extends AbstractStylePreview
    {
        /**
         * Constructs new style preview.
         *
         * @param styleId preview style ID
         * @param id      preview ID
         */
        public BasicLanguageChooser ( final StyleId styleId, final String id )
        {
            super ( LanguageChooserExample.this, id, FeatureState.updated, styleId );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final LanguageChooser chooser = new LanguageChooser ( getStyleId () );
            return CollectionUtils.asList ( chooser );
        }
    }
}