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

package com.alee.examples.content.themes;

import javax.swing.*;

/**
 * @author Mikle Garin
 */

public enum EditorTheme
{
    idea,
    dark,
    vs,
    eclipse;

    public String getName ()
    {
        switch ( this )
        {
            case idea:
                return "IntelliJ IDEA Theme";
            case dark:
                return "Dark Theme";
            case vs:
                return "Visual Studio Them";
            case eclipse:
                return "Eclipse Theme";
            default:
                return null;
        }
    }

    public Icon getIcon ()
    {
        return new ImageIcon ( EditorTheme.class.getResource ( "icons/" + this + ".png" ) );
    }
}