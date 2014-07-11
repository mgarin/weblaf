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

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.extended.image.DisplayType;
import com.alee.extended.image.WebImage;
import com.alee.extended.layout.TableLayout;
import com.alee.extended.panel.GroupPanel;
import com.alee.extended.progress.StepSelectionMode;
import com.alee.extended.progress.WebStepProgress;
import com.alee.laf.panel.WebPanel;

import java.awt.*;

/**
 * User: mgarin Date: 17.01.13 Time: 16:56
 */

public class StepProgressExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Step progress";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled step progress";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        final WebStepProgress wsp1 = new WebStepProgress ( 6 );

        final WebStepProgress wsp2 = new WebStepProgress ( "Step 1", "Step 2", "Step 3" );
        wsp2.setSelectedStepIndex ( 0 );
        wsp2.setLabelsPosition ( WebStepProgress.LEADING );

        final WebStepProgress wsp3 = new WebStepProgress ( 3 );
        wsp3.setSelectedStepIndex ( 1 );
        wsp3.setSelectionMode ( StepSelectionMode.progress );

        final WebStepProgress wsp4 = new WebStepProgress ( "Step 1", "Step 2", "Step 3" );
        wsp4.setSelectedStepIndex ( 2 );
        wsp4.setLabelsPosition ( WebStepProgress.TRAILING );

        final WebStepProgress wsp5 = new WebStepProgress ( "Step 1", "Long step 2", "Step 3" );
        wsp5.setSelectedStepIndex ( 0 );
        wsp5.setLabelsPosition ( WebStepProgress.LEADING );
        wsp5.setOrientation ( WebStepProgress.VERTICAL );
        wsp5.setStepControlRound ( 2 );
        wsp5.setStepControlFillRound ( 0 );

        final WebStepProgress wsp6 = new WebStepProgress ( "Step 1", "Long step 2", "Step 3" );
        wsp6.setSelectedStepIndex ( 1 );
        wsp6.setLabelsPosition ( WebStepProgress.TRAILING );
        wsp6.setOrientation ( WebStepProgress.VERTICAL );
        wsp6.setStepControlRound ( 2 );
        wsp6.setStepControlFillRound ( 0 );
        wsp6.setSelectionMode ( StepSelectionMode.progress );

        final WebStepProgress wsp7 = new WebStepProgress ( createProgressIcon ( "ava1.jpg" ), createProgressIcon ( "ava2.jpg" ),
                createProgressIcon ( "ava3.png" ), createProgressIcon ( "ava4.jpg" ) );
        wsp7.setLabelsPosition ( WebStepProgress.TRAILING );
        wsp7.setSelectionMode ( StepSelectionMode.progress );

        final WebPanel container = new WebPanel ( new TableLayout ( new double[][]{ { TableLayout.PREFERRED, 150, TableLayout.PREFERRED },
                { TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED, TableLayout.PREFERRED } } ) );
        container.add ( wsp1, "0,0,2,0" );
        container.add ( wsp5, "0,1,0,3" );
        container.add ( wsp2, "1,1" );
        container.add ( wsp3, "1,2" );
        container.add ( wsp4, "1,3" );
        container.add ( wsp6, "2,1,2,3" );
        container.add ( wsp7, "0,4,2,4" );

        return new GroupPanel ( container );
    }

    private WebImage createProgressIcon ( final String path )
    {
        final WebImage image = new WebImage ( loadIcon ( path ) );
        image.setPreferredSize ( new Dimension ( 24, 24 ) );
        image.setDisplayType ( DisplayType.fitComponent );
        return image;
    }
}