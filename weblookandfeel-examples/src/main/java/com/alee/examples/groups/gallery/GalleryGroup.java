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

package com.alee.examples.groups.gallery;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;
import com.alee.examples.content.FeatureState;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * User: mgarin Date: 17.02.12 Time: 14:18
 */

public class GalleryGroup extends DefaultExampleGroup
{
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "gallery.png" );
    }

    public String getGroupName ()
    {
        return "Image galery";
    }

    public String getGroupDescription ()
    {
        return "Image gallery usage example";
    }

    public boolean isSingleExample ()
    {
        return true;
    }

    public FeatureState getFeatureGroupState ()
    {
        return FeatureState.beta;
    }

    public List<Example> getGroupExamples ()
    {
        return CollectionUtils.copy ( ( Example ) new GalleryExample () );
    }
}