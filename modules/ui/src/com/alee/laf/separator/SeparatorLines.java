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

package com.alee.laf.separator;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.io.Serializable;
import java.util.List;

/**
 * Multiply separator lines data.
 *
 * @author Mikle Garin
 * @see com.alee.laf.separator.AbstractSeparatorPainter
 */

@XStreamAlias ( "SeparatorLines" )
public class SeparatorLines implements Serializable
{
    /**
     * Separator lines data.
     * Must always be provided to properly render separator.
     */
    @XStreamImplicit ( itemFieldName = "line" )
    private List<SeparatorLine> lines;

    /**
     * Returns separator lines data.
     *
     * @return separator lines data
     */
    public List<SeparatorLine> getLines ()
    {
        return lines;
    }

    /**
     * Sets separator lines data.
     *
     * @param lines separator lines data
     */
    public void setLines ( final List<SeparatorLine> lines )
    {
        this.lines = lines;
    }
}