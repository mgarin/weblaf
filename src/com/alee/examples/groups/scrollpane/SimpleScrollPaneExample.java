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

package com.alee.examples.groups.scrollpane;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.examples.content.ExamplesManager;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.laf.text.WebTextArea;

import java.awt.*;

/**
 * ScrollPane example.
 *
 * @author Mikle Garin
 */

public class SimpleScrollPaneExample extends DefaultExample
{
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle ()
    {
        return "Simple scroll pane";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDescription ()
    {
        return "Simple Web-styled scroll pane";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Component getPreview ( final WebLookAndFeelDemo owner )
    {
        final WebTextArea scrollableArea = new WebTextArea ( ExamplesManager.createLongString () );
        scrollableArea.setLineWrap ( true );
        scrollableArea.setWrapStyleWord ( true );
        scrollableArea.setMargin ( 5 );

        final WebScrollPane webScrollPane = new WebScrollPane ( scrollableArea, false, true );
        webScrollPane.setPreferredSize ( new Dimension ( 0, 0 ) );
        return webScrollPane;
    }
}