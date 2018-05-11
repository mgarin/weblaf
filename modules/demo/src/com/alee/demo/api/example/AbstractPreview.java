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
    protected Example example;

    /**
     * Preview identifier.
     */
    protected final String id;

    /**
     * Feature state.
     */
    protected final FeatureState state;

    /**
     * Cached preview component.
     */
    protected JComponent preview;

    /**
     * Constructs new  preview.
     *
     * @param example example this preview belongs to
     * @param id      preview id
     */
    public AbstractPreview ( final Example example, final String id )
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
    public AbstractPreview ( final Example example, final String id, final FeatureState state )
    {
        super ();
        this.example = example;
        this.id = id;
        this.state = state;
    }

    @Override
    public Example getExample ()
    {
        return example;
    }

    @Override
    public String getId ()
    {
        return id;
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return state;
    }

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
    protected String getPreviewLanguageKey ( final String key )
    {
        return getPreviewLanguagePrefix () + key;
    }

    @Override
    public JComponent getPreview ( final List<Preview> previews, final int index )
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
    protected abstract JComponent createPreview ( List<Preview> previews, int index );

    @Override
    public JComponent getEqualizableWidthComponent ()
    {
        return null;
    }
}