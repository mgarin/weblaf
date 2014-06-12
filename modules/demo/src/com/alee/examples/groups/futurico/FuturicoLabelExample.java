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
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.skin.ninepatch.NPLabelPainter;
import com.alee.utils.NinePatchUtils;
import com.alee.utils.ninepatch.NinePatchIcon;

import java.awt.*;

/**
 * Futurico label painter example.
 *
 * @author Mikle Garin
 */

public class FuturicoLabelExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Futurico label";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Futurico-styled label";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isFillWidth ()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        // Label styled with nine-patch icon painter
        final NinePatchIcon icon = NinePatchUtils.loadNinePatchIcon ( getResource ( "label.xml" ) );
        final WebLabel label = new WebLabel ( "Sample text within styled label", WebLabel.CENTER );
        label.setPainter ( new NPLabelPainter ( icon ) ).setForeground ( Color.WHITE );
        return label;
    }
}