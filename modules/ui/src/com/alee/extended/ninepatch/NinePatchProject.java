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

import java.util.HashMap;
import java.util.Map;

/**
 * @author Mikle Garin
 */

public class NinePatchProject
{
    private Map<String, NinePatchStateGroup> stateGroups = new HashMap<String, NinePatchStateGroup> ();

    public NinePatchProject ()
    {
        super ();
    }

    public NinePatchStateGroup getStateGroup ( final String groupName )
    {
        return stateGroups.get ( groupName );
    }

    public void setStateGroup ( final String groupName, final NinePatchStateGroup stateGroup )
    {
        stateGroups.put ( groupName, stateGroup );
    }

    public Map<String, NinePatchStateGroup> getStateGroups ()
    {
        return stateGroups;
    }

    public void setStateGroups ( final Map<String, NinePatchStateGroup> stateGroups )
    {
        this.stateGroups = stateGroups;
    }
}