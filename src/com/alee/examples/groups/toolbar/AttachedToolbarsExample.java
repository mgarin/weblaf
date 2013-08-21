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

package com.alee.examples.groups.toolbar;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.extended.panel.BorderPanel;
import com.alee.extended.panel.GroupPanel;
import com.alee.laf.toolbar.ToolbarStyle;
import com.alee.laf.toolbar.WebToolBar;

import java.awt.*;

/**
 * User: mgarin Date: 24.01.12 Time: 14:30
 */

public class AttachedToolbarsExample extends DefaultToolbarExample
{
    @Override
    public String getTitle ()
    {
        return "Attached toolbars";
    }

    @Override
    public String getDescription ()
    {
        return "Attach-styled toolbars";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        WebToolBar aft = new WebToolBar ( WebToolBar.HORIZONTAL );
        aft.setToolbarStyle ( ToolbarStyle.attached );
        setupToolBar ( aft );

        WebToolBar aut = new WebToolBar ( WebToolBar.HORIZONTAL );
        aut.setFloatable ( false );
        aut.setToolbarStyle ( ToolbarStyle.attached );
        aut.setComponentOrientation ( ComponentOrientation.RIGHT_TO_LEFT );
        setupToolBar ( aut );

        return new GroupPanel ( 3, false, new BorderPanel ( aft ), new BorderPanel ( aut ) );
    }
}