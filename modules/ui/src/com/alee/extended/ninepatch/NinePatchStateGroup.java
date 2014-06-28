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

package com.alee.extended.ninepatch;

import com.alee.extended.painter.NinePatchStatePainter;
import com.alee.utils.ninepatch.NinePatchIcon;

import java.util.HashMap;
import java.util.Map;

/**
 * User: mgarin Date: 06.02.12 Time: 17:43
 */

public class NinePatchStateGroup
{
    private Map<String, NinePatchIcon> stateIcons = new HashMap<String, NinePatchIcon> ();

    public NinePatchStateGroup ()
    {
        super ();
    }

    public NinePatchIcon getStateIcon ( String state )
    {
        return stateIcons.get ( state );
    }

    public void setStateIcon ( String state, NinePatchIcon icon )
    {
        stateIcons.put ( state, icon );
    }

    public Map<String, NinePatchIcon> getStateIcons ()
    {
        return stateIcons;
    }

    public void setStateIcons ( Map<String, NinePatchIcon> stateIcons )
    {
        this.stateIcons = stateIcons;
    }

    public NinePatchStatePainter getStateBackgroundPainter ()
    {
        return new NinePatchStatePainter ( stateIcons );
    }
}