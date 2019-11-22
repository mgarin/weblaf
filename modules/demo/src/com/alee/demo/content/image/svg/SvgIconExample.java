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
import com.alee.api.annotations.Nullable;
import com.alee.api.resource.ClassResource;
import com.alee.demo.api.example.*;
import com.alee.extended.layout.CompactFlowLayout;
import com.alee.extended.svg.*;
import com.alee.laf.WebLookAndFeel;
import com.alee.managers.icon.data.IconAdjustment;
import com.alee.utils.CollectionUtils;
import com.kitfox.svg.SVGElement;
import com.kitfox.svg.xml.StyleAttribute;

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
                new SvgIconPreview ( "stroke", new SvgStroke ( Color.RED ) ),
                new SvgIconPreview ( "opacity", new SampleSvgAdjustment () )
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
        @NotNull
        protected List<? extends IconAdjustment<SvgIcon>> adjustments;

        /**
         * Constructs new {@link SvgIconPreview}.
         *
         * @param id          preview ID
         * @param adjustments {@link IconAdjustment}s to apply to {@link SvgIcon}s
         */
        public SvgIconPreview ( @NotNull final String id, @NotNull final IconAdjustment<SvgIcon>... adjustments )
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
            final SvgIcon icon1 = new SvgIcon ( new ClassResource ( WebLookAndFeel.class, "icons/icon.svg" ), 200, 200 );
            icon1.apply ( adjustments );

            final SvgIcon icon2 = new SvgIcon ( new ClassResource ( SvgIconExample.class, "resources/firefox.svg" ), 200, 200 );
            icon2.apply ( adjustments );

            final SvgIcon icon3 = new SvgIcon ( new ClassResource ( SvgIconExample.class, "resources/mona.svg" ), 187, 279 );
            icon3.apply ( adjustments );

            return CollectionUtils.asList ( new JLabel ( icon1 ), new JLabel ( icon2 ), new JLabel ( icon3 ) );
        }
    }

    /**
     * Custom {@link IconAdjustment} for {@link SvgIcon} that modifies root SVG element opacity.
     */
    protected static class SampleSvgAdjustment extends AbstractSvgAttributeAdjustment
    {
        /**
         * Constructs new {@link SampleSvgAdjustment} applied to root SVG element.
         */
        public SampleSvgAdjustment ()
        {
            super ( "svg" );
        }

        @NotNull
        @Override
        protected String getAttribute ( @NotNull final SvgIcon icon )
        {
            return SvgElements.OPACITY;
        }

        @Nullable
        @Override
        protected String getValue ( @NotNull final SvgIcon icon, @NotNull final SVGElement element,
                                    @Nullable final StyleAttribute attribute )
        {
            return "0.5";
        }
    }
}