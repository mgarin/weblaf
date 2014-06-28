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

package com.alee.examples.groups.progress;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;
import com.alee.examples.content.FeatureState;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 14.02.12 Time: 12:43
 */

public class ProgressGroup extends DefaultExampleGroup
{
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "progress.png" );
    }

    @Override
    public String getGroupName ()
    {
        return "Custom progress";
    }

    @Override
    public String getGroupDescription ()
    {
        return "Various examples of progress components usage";
    }

    @Override
    public FeatureState getFeatureGroupState ()
    {
        return FeatureState.beta;
    }

    @Override
    public List<Example> getGroupExamples ()
    {
        List<Example> examples = new ArrayList<Example> ();
        examples.add ( new ProgressDialogExample () );
        examples.add ( new ButtonProgressOverlayExample () );
        examples.add ( new TextProgressOverlayExample () );
        examples.add ( new ImageProgressOverlayExample () );
        examples.add ( new StepProgressExample () );
        return examples;
    }
}