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

package com.alee.extended.statusbar;

import com.alee.global.StyleConstants;
import com.alee.laf.label.WebLabel;

import javax.swing.*;

/**
 * User: mgarin Date: 21.10.11 Time: 18:57
 */

public class WebStatusLabel extends WebLabel
{
    public WebStatusLabel ()
    {
        super ( "", WebLabel.CENTER );
        setupSettings ();
    }

    public WebStatusLabel ( Icon image )
    {
        super ( image, WebLabel.CENTER );
        setupSettings ();
    }

    public WebStatusLabel ( String text )
    {
        super ( text, WebLabel.CENTER );
        setupSettings ();
    }

    public WebStatusLabel ( String text, Icon icon )
    {
        super ( text, icon, WebLabel.CENTER );
        setupSettings ();
    }

    private void setupSettings ()
    {
        setMargin ( StyleConstants.shadeWidth, StyleConstants.shadeWidth, StyleConstants.shadeWidth, StyleConstants.shadeWidth );
    }
}
