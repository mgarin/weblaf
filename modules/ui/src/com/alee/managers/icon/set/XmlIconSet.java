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

package com.alee.managers.icon.set;

import com.alee.api.annotations.NotNull;
import com.alee.api.resource.Resource;
import com.alee.utils.TextUtils;
import com.alee.utils.XmlUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

/**
 * Simple icon set that could be created based on a custom XML data file.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-IconManager">How to use IconManager</a>
 * @see IconSet
 * @see AbstractIconSet
 * @see com.alee.managers.icon.IconManager
 */
@XStreamAlias ( "IconSet" )
@XStreamConverter ( XmlIconSetConverter.class )
public class XmlIconSet extends AbstractIconSet
{
    /**
     * Constructs new {@link XmlIconSet}.
     *
     * @param resource icon set {@link Resource}
     */
    public XmlIconSet ( @NotNull final Resource resource )
    {
        super ( TextUtils.generateId () );
        XmlUtils.fromXML ( resource, this );
    }
}