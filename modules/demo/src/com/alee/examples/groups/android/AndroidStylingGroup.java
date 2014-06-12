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

package com.alee.examples.groups.android;

import com.alee.examples.content.DefaultExampleGroup;
import com.alee.examples.content.Example;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 12.03.12 Time: 15:32
 */

public class AndroidStylingGroup extends DefaultExampleGroup
{
    @Override
    public Icon getGroupIcon ()
    {
        return loadGroupIcon ( "android.png" );
    }

    @Override
    public String getGroupName ()
    {
        return "Android styling";
    }

    @Override
    public String getGroupDescription ()
    {
        return "Various examples of Android components styling";
    }

    @Override
    public List<Example> getGroupExamples ()
    {
        // todo Add styled list
        //        // List with nine-patch styled renderer
        //        WebList styledList = new WebList ( createSampleData () );
        //        styledList.setVisibleRowCount ( 4 );
        //        styledList.setSelectedIndex ( 0 );
        //        styledList.setEditable ( true );
        //        styledList.setRendererPainter ( XmlUtils.loadNinePatchStatePainter (
        //                ListsExample.class.getResource ( "resources/renderer.xml" ) ) );

        List<Example> examples = new ArrayList<Example> ();
        examples.add ( new AndroidLabelExample () );
        examples.add ( new AndroidPanelExample () );
        examples.add ( new AndroidButtonsExample () );
        examples.add ( new AndroidFieldsExample () );
        examples.add ( new AndroidTextAreaExample () );
        return examples;
    }
}