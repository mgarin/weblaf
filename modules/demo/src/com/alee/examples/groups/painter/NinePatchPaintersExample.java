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

package com.alee.examples.groups.painter;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.painter.NinePatchStatePainter;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.label.WebLabel;
import com.alee.laf.text.WebTextField;
import com.alee.managers.style.skin.ninepatch.NPLabelPainter;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import java.awt.*;

/**
 * 9-patch painters example.
 *
 * @author Mikle Garin
 */

public class NinePatchPaintersExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Nine-patch painters";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Nine-patch based painters";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Simple 9-patch styling
        final NinePatchIcon ninePatchIcon = new NinePatchIcon ( loadIcon ( "simple.9.png" ) );
        final NPLabelPainter painter = new NPLabelPainter ( ninePatchIcon );

        final WebLabel label = new WebLabel ( "Simple nine-patch file based styling", WebLabel.CENTER );
        label.setPainter ( painter ).setMargin ( 5 ).setForeground ( Color.WHITE );

        // State-dependant 9-patch styling
        final NinePatchStatePainter statePainter = NinePatchUtils.loadNinePatchStatePainter ( getResource ( "styling.xml" ) );

        final WebTextField field1 = new WebTextField ( "State-dependant" );
        field1.setPainter ( statePainter );

        final WebTextField field2 = new WebTextField ( "nine-patch" );
        field2.setPainter ( statePainter );

        final WebTextField field3 = new WebTextField ( "styling" );
        field3.setPainter ( statePainter );

        return new GroupPanel ( 4, false, label, new GroupPanel ( 4, field1, field2, field3 ) );
    }
}