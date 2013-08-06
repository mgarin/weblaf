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

package com.alee.extended.progress;

import com.alee.laf.label.WebLabel;

import java.awt.*;

/**
 * User: mgarin Date: 15.01.13 Time: 14:36
 */

public class StepData
{
    private Component label;

    public StepData ()
    {
        this ( ( Component ) null );
    }

    public StepData ( String label )
    {
        this ( new WebLabel ( label ) );
    }

    public StepData ( Component label )
    {
        super ();
        setLabel ( label );
    }

    public Component getLabel ()
    {
        return label;
    }

    public void setLabel ( Component label )
    {
        this.label = label;
    }
}
