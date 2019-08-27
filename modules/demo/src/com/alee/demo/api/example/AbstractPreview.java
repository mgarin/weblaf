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

package com.alee.demo.api.example;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;

import javax.swing.*;
import java.util.List;

/**
 * @author Mikle Garin
 */
public abstract class AbstractPreview implements Preview
{
    /**
     * Example this preview belongs to.
     */
    @NotNull
    protected Example example;

    /**
     * Preview identifier.
     */
    @NotNull
    protected final String id;

    /**
     * Feature state.
     */
    @NotNull
    protected final FeatureState state;

    /**
     * Cached preview component.
     */
    @Nullable
    protected JComponent preview;

    /**
     * Constructs new  preview.
     *
     * @param example example this preview belongs to
     * @param id      preview id
     */
    public AbstractPreview ( @NotNull final Example example, @NotNull final String id )
    {
        this ( example, id, FeatureState.common );
    }

    /**
     * Constructs new  preview.
     *
     * @param example example this preview belongs to
     * @param id      preview id
     * @param state   feature state
     */
    public AbstractPreview ( @NotNull final Example example, @NotNull final String id, @NotNull final FeatureState state )
    {
        super ();
        this.example = example;
        this.id = id;
        this.state = state;
    }

    @NotNull
    @Override
    public Example getExample ()
    {
        return example;
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    @NotNull
    @Override
    public FeatureState getFeatureState ()
    {
        return state;
    }

    @NotNull
    @Override
    public String getTitle ()
    {
        return getPreviewLanguagePrefix () + "title";
    }

    /**
     * Returns language prefix for this preview.
     *
     * @return language prefix for this preview
     */
    @NotNull
    protected String getPreviewLanguagePrefix ()
    {
        return "demo.example." + getExample ().getGroupId () + "." + getExample ().getId () + "." + getId () + ".";
    }

    /**
     * Returns complete language key for this preview for the specified key part.
     *
     * @param key language key part
     * @return complete language key for this preview for the specified key part
     */
    @NotNull
    protected String getPreviewLanguageKey ( final String key )
    {
        return getPreviewLanguagePrefix () + key;
    }

    @NotNull
    @Override
    public JComponent getPreview ( @NotNull final List<Preview> previews, final int index )
    {
        if ( preview == null )
        {
            preview = createPreview ( previews, index );
        }
        return preview;
    }

    /**
     * Returns preview component.
     * This can be anything provided by the example.
     *
     * @param previews all previews within example
     * @param index    index of this preview
     * @return preview component
     */
    @NotNull
    protected abstract JComponent createPreview ( @NotNull List<Preview> previews, int index );

    @Nullable
    @Override
    public JComponent getEqualizableWidthComponent ()
    {
        return null;
    }
}