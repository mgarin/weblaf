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

package com.alee.utils.xml;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mikle Garin
 */

@XStreamAlias ("ResourceList")
public class ResourceList implements Serializable
{
    @XStreamImplicit
    private List<ResourceFile> resources = new ArrayList<ResourceFile> ();

    public ResourceList ()
    {
        super ();
    }

    public void addResource ( final ResourceFile resource )
    {
        resources.add ( resource );
    }

    public void removeResource ( final ResourceFile resource )
    {
        resources.remove ( resource );
    }

    public ResourceFile getResource ( final int index )
    {
        return resources.get ( index );
    }

    public List<ResourceFile> getResources ()
    {
        return resources;
    }

    public void setResources ( final List<ResourceFile> resources )
    {
        this.resources = resources;
    }
}