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

package com.alee.api.merge.behavior;

import com.alee.api.Identifiable;
import com.alee.api.merge.Merge;
import com.alee.utils.CompareUtils;

import java.util.List;

/**
 * {@link List} objects merge behavior.
 * It will attempt to find identifiable elements in list and merge them.
 * Other elements will simply be added to the end of the list in provided order.
 * This is the best way we can handle list elements merge without any additional information on the elements
 *
 * @author Mikle Garin
 */

public final class ListMergeBehavior implements MergeBehavior
{
    @Override
    public boolean supports ( final Object object, final Object merged )
    {
        return object instanceof List && merged instanceof List;
    }

    @Override
    public <T> T merge ( final Merge merge, final Object object, final Object merged )
    {
        final List el = ( List ) object;
        final List ml = ( List ) merged;
        for ( final Object mergedObject : ml )
        {
            // We only merge identifiable objects as there is no other way to ensure we really need to merge them
            // We don't really want to have two different objects of the same type with the same ID in one list
            if ( mergedObject != null && mergedObject instanceof Identifiable )
            {
                // Looking for object of the same type which is also identifiable in the existing list
                // Then we compare their IDs and merge them using the same algorithm if IDs are equal
                final String mid = ( ( Identifiable ) mergedObject ).getId ();
                boolean found = false;
                for ( int j = 0; j < el.size (); j++ )
                {
                    final Object existingObject = el.get ( j );
                    if ( existingObject != null )
                    {
                        final String eid = ( ( Identifiable ) existingObject ).getId ();
                        if ( CompareUtils.equals ( eid, mid ) )
                        {
                            el.set ( j, merge.merge ( existingObject, mergedObject ) );
                            found = true;
                            break;
                        }
                    }
                }
                if ( !found )
                {
                    // Simply adding object to the end of the list
                    el.add ( mergedObject );
                }
            }
            else
            {
                // Simply adding non-identifiable object to the end of the list
                el.add ( mergedObject );
            }
        }
        return ( T ) el;
    }
}