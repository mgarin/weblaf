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

package com.alee.examples.groups.ninepatcheditor;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;
import com.alee.examples.content.FeatureState;
import com.alee.utils.CollectionUtils;

import javax.swing.*;
import java.util.List;

/**
 * User: mgarin Date: 12.03.12 Time: 13:33
 */

public class NinePatchEditorGroup extends DefaultExampleGroup
{
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "ninepatch.png" );
    }

    @Override
    public String getGroupName ()
    {
        return "Nine-patch editor";
    }

    @Override
    public String getGroupDescription ()
    {
        return "Example of nine-patch editor usage";
    }

    @Override
    public boolean isSingleExample ()
    {
        return true;
    }

    @Override
    public FeatureState getFeatureGroupState ()
    {
        return FeatureState.beta;
    }

    @Override
    public boolean isShowWatermark ()
    {
        return false;
    }

    @Override
    public List<Example> getGroupExamples ()
    {
        return CollectionUtils.copy ( ( Example ) new NinePatchEditorExample () );
    }
}