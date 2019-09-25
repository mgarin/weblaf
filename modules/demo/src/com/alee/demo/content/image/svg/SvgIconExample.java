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

package com.alee.demo.content.image.svg;

import com.alee.api.annotations.NotNull;
import com.alee.demo.api.example.*;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.extended.svg.AbstractSvgAttributeAdjustment;
import com.alee.extended.svg.SvgElements;
import com.alee.extended.svg.SvgGrayscale;
import com.alee.extended.svg.SvgIcon;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.icon.data.IconAdjustment;
import com.alee.utils.CollectionUtils;
import com.alee.utils.ColorUtils;
import com.kitfox.svg.SVGUniverse;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public class SvgIconExample extends AbstractPreviewExample
{
    @NotNull
    @Override
    public String getId ()
    {
        return "svgicon";
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
                new SvgIconPreview ( "basic" ),
                new SvgIconPreview ( "grayscale", new SvgGrayscale () ),
                new SvgIconPreview ( "modified", new SvgRedStroke () )
        );
    }

    /**
     * {@link SvgIcon} preview.
     */
    protected class SvgIconPreview extends AbstractTitledPreview
    {
        /**
         * {@link IconAdjustment}s to apply to {@link SvgIcon}s.
         */
        protected List<? extends IconAdjustment<SvgIcon>> adjustments;

        /**
         * Constructs new {@link SvgIconPreview}.
         *
         * @param id          preview ID
         * @param adjustments {@link IconAdjustment}s to apply to {@link SvgIcon}s
         */
        public SvgIconPreview ( final String id, final IconAdjustment<SvgIcon>... adjustments )
        {
            super ( SvgIconExample.this, id, FeatureState.release );
            this.adjustments = CollectionUtils.asList ( adjustments );
        }

        @NotNull
        @Override
        protected LayoutManager createPreviewLayout ()
        {
            return new CompactFlowLayout ( FlowLayout.LEADING, 8, 0 );
        }

        @NotNull
        @Override
        protected List<? extends JComponent> createPreviewElements ()
        {
            /**
             * Separate universe to avoid icon adjustments spreading across same icons within different previews.
             */
            final SVGUniverse universe = new SVGUniverse ();

            final SvgIcon icon1 = new SvgIcon ( universe, WebLookAndFeel.class, "icons/icon.svg", 200, 200 );
            icon1.apply ( adjustments );

            final SvgIcon icon2 = new SvgIcon ( universe, SvgIconExample.class, "resources/firefox.svg", 200, 200 );
            icon2.apply ( adjustments );

            final SvgIcon icon3 = new SvgIcon ( universe, SvgIconExample.class, "resources/mona.svg", 187, 279 );
            icon3.apply ( adjustments );

            return CollectionUtils.asList ( new JLabel ( icon1 ), new JLabel ( icon2 ), new JLabel ( icon3 ) );
        }
    }

    /**
     * Custom {@link IconAdjustment} for {@link SvgIcon} that modifies stroke color to red.
     */
    protected static class SvgRedStroke extends AbstractSvgAttributeAdjustment
    {
        /**
         * Constructs new {@link SvgRedStroke}.
         */
        public SvgRedStroke ()
        {
            super ();
            this.selector = "*";
        }

        @Override
        protected String getAttribute ( final SvgIcon icon )
        {
            return SvgElements.STROKE;
        }

        @Override
        protected String getValue ( final SvgIcon icon )
        {
            return ColorUtils.toHex ( Color.RED );
        }
    }
}