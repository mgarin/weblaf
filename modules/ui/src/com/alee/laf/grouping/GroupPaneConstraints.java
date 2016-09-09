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

package com.alee.laf.grouping;

/**
 * Custom constraints data for {@link com.alee.laf.grouping.GroupPaneLayout}.
 *
 * @author Mikle Garin
 */

public final class GroupPaneConstraints
{
    /**
     * Preferred component size constraints.
     */
    public static final GroupPaneConstraints PREFERRED = of ( -1, -1 );

    /**
     * Fill component size constraints.
     */
    public static final GroupPaneConstraints FILL = of ( 1, 1 );

    /**
     * Horizontal fill component size constraints.
     */
    public static final GroupPaneConstraints HORIZONTAL_FILL = of ( 1, -1 );

    /**
     * Vertical fill component size constraints.
     */
    public static final GroupPaneConstraints VERTICAL_FILL = of ( -1, 1 );

    /**
     * Component width.
     * Provide null value for preferred component width.
     * Provide [0-1] value for percentage component width.
     * Provide [2+] value for specific component width.
     */
    public double width;

    /**
     * Component height.
     * Provide null value for preferred component height.
     * Provide [0-1] value for percentage component height.
     * Provide [2+] value for specific component height.
     */
    public double height;

    /**
     * Constructs new constraints for {@link com.alee.laf.grouping.GroupPaneLayout}.
     *
     * @param width  component width
     * @param height component height
     */
    public GroupPaneConstraints ( final double width, final double height )
    {
        super ();
        this.width = width;
        this.height = height;
    }

    /**
     * Returns new constraints for {@link com.alee.laf.grouping.GroupPaneLayout}
     *
     * @param width  component width
     * @param height component height
     * @return new constraints for {@link com.alee.laf.grouping.GroupPaneLayout}
     */
    public static GroupPaneConstraints of ( final double width, final double height )
    {
        return new GroupPaneConstraints ( width, height );
    }

    /**
     * Returns new constraints for {@link com.alee.laf.grouping.GroupPaneLayout}
     *
     * @param width component width
     * @return new constraints for {@link com.alee.laf.grouping.GroupPaneLayout}
     */
    public static GroupPaneConstraints width ( final double width )
    {
        return of ( width, -1 );
    }

    /**
     * Returns new constraints for {@link com.alee.laf.grouping.GroupPaneLayout}
     *
     * @param height component height
     * @return new constraints for {@link com.alee.laf.grouping.GroupPaneLayout}
     */
    public static GroupPaneConstraints height ( final double height )
    {
        return of ( -1, height );
    }
}