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

package com.alee.demo.skin;

import com.alee.api.resource.ClassResource;
import com.alee.managers.icon.set.XmlIconSet;

/**
 * Light icon set for {@link com.alee.demo.DemoApplication}.
 *
 * @author Mikle Garin
 */
public final class DemoLightIconSet extends XmlIconSet
{
    /**
     * Constructs new {@link DemoLightIconSet}.
     */
    public DemoLightIconSet ()
    {
        super ( new ClassResource ( DemoLightIconSet.class, "resources/demo-light-icon-set.xml" ) );
    }
}