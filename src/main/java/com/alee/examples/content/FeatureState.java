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

package com.alee.examples.content;

import javax.swing.*;

/**
 * User: mgarin Date: 07.03.12 Time: 15:59
 */

public enum FeatureState
{
    alpha,
    beta,
    release;

    public ImageIcon getIcon ()
    {
        return new ImageIcon ( FeatureState.class.getResource ( "icons/state/" + this + ".png" ) );
    }

    public String getDescription ()
    {
        if ( this.equals ( FeatureState.alpha ) )
        {
            return "Alpha-state feature";
        }
        else if ( this.equals ( FeatureState.beta ) )
        {
            return "Beta-state feature";
        }
        else if ( this.equals ( FeatureState.release ) )
        {
            return "Completed feature";
        }
        else
        {
            return "";
        }
    }

    public String getFullDescription ()
    {
        if ( this.equals ( FeatureState.alpha ) )
        {
            return "<html><center>This is new and either untested or unfinished feature,<br>" +
                    "so it might contain some ciritcal bugs and errors.<br>" +
                    "It is released to show some of upcoming features.</center></html>";
        }
        else if ( this.equals ( FeatureState.beta ) )
        {
            return "<html><center>This is already tested and almost finished feature.<br>" +
                    "It still might contain some known non-critical bugs,<br>" +
                    "which will be fixed/changed in next version of the library.</center></html>";
        }
        else if ( this.equals ( FeatureState.release ) )
        {
            return "<html><center>This is tested feature that shouldn't have any bugs.<br>" +
                    "Such features still might be imroved in future.</center></html>";
        }
        else
        {
            return "";
        }
    }
}
