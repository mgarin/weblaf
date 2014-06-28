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

package com.alee.examples.groups.futurico;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.FeatureState;
import com.alee.laf.text.WebTextArea;
import com.alee.utils.NinePatchUtils;

import java.awt.*;

/**
 * User: mgarin Date: 14.03.12 Time: 14:48
 */

public class FuturicoTextAreaExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Futurico text area";
    }

    @Override
    public String getDescription ()
    {
        return "Futurico-styled text area";
    }

    @Override
    public FeatureState getFeatureState ()
    {
        return FeatureState.beta;
    }

    @Override
    public boolean isFillWidth ()
    {
        return true;
    }

    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Text area styled with nine-patch state painter
        final WebTextArea textArea = new WebTextArea ();
        textArea.setRows ( 2 );
        textArea.setText ( "Multiline\nStyled\nTextarea" );
        textArea.setPainter ( NinePatchUtils.loadNinePatchStatePainter ( getResource ( "area.xml" ) ) );
        textArea.setForeground ( Color.WHITE );
        return textArea;
    }
}