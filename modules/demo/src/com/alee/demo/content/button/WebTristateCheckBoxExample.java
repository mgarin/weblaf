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

package com.alee.demo.content.button;

import com.alee.demo.api.example.*;
import com.alee.extended.checkbox.WebTristateCheckBox;
import com.alee.extended.layout.VerticalFlowLayout;
import com.alee.laf.checkbox.CheckState;
import com.alee.managers.style.StyleId;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class WebTristateCheckBoxExample extends AbstractStylePreviewExample
{
    @Override
    public String getId ()
    {
        return "tristatecheckbox";
    }

    @Override
    protected String getStyleFileName ()
    {
        return "tristatecheckbox";
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
                new TextCheckBox ( "basic", StyleId.tristatecheckbox, "Simple check text" ),
                new TextCheckBox ( "styled", StyleId.tristatecheckboxStyled, "{Styled:b} {check:u} {text:c(red)}" ),
                new TextCheckBox ( "link", StyleId.tristatecheckboxLink, "Link-like {tristate check:b} box" )
        );
    }

    /**
     * Tristate check box preview.
     */
    protected class TextCheckBox extends AbstractStylePreview
    {
        /**
         * Check box text.
         */
        protected String text;

        /**
         * Constructs new style preview.
         *
         * @param id      preview ID
         * @param styleId preview style ID
         * @param text    check box text
         */
        public TextCheckBox ( final String id, final StyleId styleId, final String text )
        {
            super ( WebTristateCheckBoxExample.this, id, FeatureState.updated, styleId );
            this.text = text;
        }

        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new VerticalFlowLayout ( 0, 8, false, false );
        }

        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            final WebTristateCheckBox cb1 = createCheckBox ( "1", CheckState.checked );
            final WebTristateCheckBox cb2 = createCheckBox ( "2", CheckState.mixed );
            final WebTristateCheckBox cb3 = createCheckBox ( "3", CheckState.unchecked );
            return CollectionUtils.asList ( cb1, cb2, cb3 );
        }

        /**
         * Returns new check box instance.
         *
         * @param suffix check box text suffix
         * @param state  check box selection state
         * @return new check box instance
         */
        protected WebTristateCheckBox createCheckBox ( final String suffix, final CheckState state )
        {
            return new WebTristateCheckBox ( getStyleId (), text + " " + suffix, state );
        }
    }
}