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

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.ninepatch.NinePatchEditorPanel;

import java.awt.*;

/**
 * User: mgarin Date: 14.03.12 Time: 15:06
 */

public class NinePatchEditorExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Nine-patch editor";
    }

    @Override
    public String getDescription ()
    {
        return "Nine-patch editor example";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Simple nine-patch editor panel
        NinePatchEditorPanel npep = new NinePatchEditorPanel ();
        npep.setNinePatchImage ( loadIcon ( "example.png" ) );
        return npep;
    }
}