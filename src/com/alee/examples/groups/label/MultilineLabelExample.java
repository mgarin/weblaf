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

package com.alee.examples.groups.label;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.label.WebMultiLineLabel;

import java.awt.*;

/**
 * User: mgarin Date: 28.09.12 Time: 15:58
 */

public class MultilineLabelExample extends DefaultExample
{
    @Override
    public String getTitle ()
    {
        return "Multiline label";
    }

    @Override
    public String getDescription ()
    {
        return "Web-styled multiline label";
    }

    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Text with line breaks
        String text = "Simple label\n" + "with hard line breaks\n" + "across the text";

        // Simple multiline label
        return new WebMultiLineLabel ( text );
    }
}