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
 * This class represents single step within WebStepProgress component.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-WebStepProgress">How to use WebStepProgress</a>
 * @see com.alee.extended.progress.WebStepProgress
 */
public class StepData
{
    /**
     * Step label component.
     * This component is displayed near step visual representation similar to JSlider.
     */
    private Component label;

    /**
     * Constructs new step with empty label component.
     */
    public StepData ()
    {
        this ( ( Component ) null );
    }

    /**
     * Constructs new step with WebLabel component using the specified text.
     *
     * @param label label text
     */
    public StepData ( final String label )
    {
        this ( new WebLabel ( label ) );
    }

    /**
     * Constructs new step with the specified label component.
     *
     * @param label label component
     */
    public StepData ( final Component label )
    {
        super ();
        setLabel ( label );
    }

    /**
     * Returns label component.
     *
     * @return label component
     */
    public Component getLabel ()
    {
        return label;
    }

    /**
     * Sets label component.
     *
     * @param label new label component
     */
    public void setLabel ( final Component label )
    {
        this.label = label;
    }
}