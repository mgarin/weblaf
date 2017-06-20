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

package com.alee.extended.colorchooser;

import com.alee.api.clone.Clone;
import com.alee.managers.settings.DefaultValue;
import com.alee.utils.ColorUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Gradient color information object.
 *
 * @author Mikle Garin
 */

@XStreamAlias ( "GradientData" )
public class GradientData implements Serializable, Cloneable, DefaultValue
{
    /**
     * Comparator to sort colors data on change.
     */
    private static final transient GradientColorDataComparator locationsComparator = new GradientColorDataComparator ();

    /**
     * All available colors.
     */
    @XStreamImplicit ( itemFieldName = "Color" )
    private List<GradientColorData> gradientColorsData;

    /**
     * Constructs {@link GradientData} object with empty colors list.
     */
    public GradientData ()
    {
        super ();
        gradientColorsData = new ArrayList<GradientColorData> ();
    }

    /**
     * Returns whether there is a {@link GradientColorData} for specified location or not.
     *
     * @param location location to search for {@link GradientColorData}
     * @return {@code true} if there is a {@link GradientColorData} for specified location, {@code false} otherwise
     */
    public boolean containtsLocation ( final float location )
    {
        for ( final GradientColorData gradientColorData : gradientColorsData )
        {
            if ( gradientColorData.getLocation () == location )
            {
                return true;
            }
        }
        return false;
    }

    /**
     * Adds {@link GradientColorData} into colors list and sorts the list.
     *
     * @param gradientColorData {@link GradientColorData} to add
     */
    public void addGradientColorData ( final GradientColorData gradientColorData )
    {
        gradientColorsData.add ( gradientColorData );
        sort ();
    }

    /**
     * Adds {@link GradientColorData} with the specified location and color into colors list and sorts the list.
     *
     * @param location color location
     * @param color    color
     */
    public void addGradientColorData ( final float location, final Color color )
    {
        gradientColorsData.add ( new GradientColorData ( location, color ) );
        sort ();
    }

    /**
     * Removes {@link GradientColorData} from colors list and sorts the list.
     *
     * @param gradientColorData {@link GradientColorData} to remove
     */
    public void removeGradientColorData ( final GradientColorData gradientColorData )
    {
        gradientColorsData.remove ( gradientColorData );
        sort ();
    }

    /**
     * Returns sorted colors list.
     *
     * @return sorted colors list
     */
    public List<GradientColorData> getGradientColorsData ()
    {
        sort ();
        return gradientColorsData;
    }

    /**
     * Sets colors list and sorts it.
     *
     * @param gradientColorsData new colors list
     */
    public void setGradientColorsData ( final List<GradientColorData> gradientColorsData )
    {
        this.gradientColorsData = gradientColorsData;
        sort ();
    }

    /**
     * Returns an array of fractions for {@link LinearGradientPaint}.
     *
     * @return an array of fractions for {@link LinearGradientPaint}
     */
    public float[] getFractions ()
    {
        sort ();
        final float[] fractions = new float[ gradientColorsData.size () ];
        for ( int i = 0; i < gradientColorsData.size (); i++ )
        {
            fractions[ i ] = gradientColorsData.get ( i ).getLocation ();
        }
        return fractions;
    }

    /**
     * Returns an array of colors for {@link LinearGradientPaint}.
     *
     * @return an array of colors for {@link LinearGradientPaint}
     */
    public Color[] getColors ()
    {
        sort ();
        final Color[] colors = new Color[ gradientColorsData.size () ];
        for ( int i = 0; i < gradientColorsData.size (); i++ )
        {
            colors[ i ] = gradientColorsData.get ( i ).getColor ();
        }
        return colors;
    }

    /**
     * Returns colors list size.
     *
     * @return colors list size
     */
    public int size ()
    {
        return gradientColorsData.size ();
    }

    /**
     * Returns {@link GradientColorData} at the specified index.
     *
     * @param index {@link GradientColorData} index
     * @return {@link GradientColorData} at the specified index
     */
    public GradientColorData get ( final int index )
    {
        return gradientColorsData.get ( index );
    }

    /**
     * Returns location for {@link GradientColorData} at the specified index.
     *
     * @param index {@link GradientColorData} index
     * @return location for {@link GradientColorData} at the specified index
     */
    public float getLocation ( final int index )
    {
        return get ( index ).getLocation ();
    }

    /**
     * Returns color for {@link GradientColorData} at the specified index.
     *
     * @param index {@link GradientColorData} index
     * @return color for {@link GradientColorData} at the specified index
     */
    public Color getColor ( final int index )
    {
        return get ( index ).getColor ();
    }

    /**
     * Returns first {@link GradientColorData}.
     *
     * @return first {@link GradientColorData}
     */
    public GradientColorData getFirst ()
    {
        return size () > 0 ? gradientColorsData.get ( 0 ) : null;
    }

    /**
     * Returns last {@link GradientColorData}.
     *
     * @return last {@link GradientColorData}
     */
    public GradientColorData getLast ()
    {
        return size () > 0 ? gradientColorsData.get ( size () - 1 ) : null;
    }

    /**
     * Returns calculated color for the specified location.
     * This method will return color for any location between 0f and 1f even if there is no data at the specified location.
     *
     * @param location color location
     * @return calculated color for the specified location
     */
    public Color getColorForLocation ( final float location )
    {
        // Ignore call when data is empty
        if ( size () == 0 )
        {
            return null;
        }

        // Before first color
        if ( location <= getFirst ().getLocation () )
        {
            return get ( 0 ).getColor ();
        }

        // After last color
        if ( location >= getLast ().getLocation () )
        {
            return getLast ().getColor ();
        }

        // Middle color
        int nextIndex = -1;
        for ( int i = 0; i < size (); i++ )
        {
            if ( getLocation ( i ) > location )
            {
                nextIndex = i;
                break;
            }
        }

        final float before = getLocation ( nextIndex - 1 );
        final float current = location - before;
        final float after = getLocation ( nextIndex ) - before;

        final float progress = current / after;
        final Color c1 = getColor ( nextIndex - 1 );
        final Color c2 = getColor ( nextIndex );

        return ColorUtils.getIntermediateColor ( c1, c2, progress );
    }

    /**
     * Sorts data list.
     */
    public void sort ()
    {
        Collections.sort ( gradientColorsData, locationsComparator );
    }

    @Override
    public boolean equals ( final Object obj )
    {
        if ( obj == null || !( obj instanceof GradientData ) )
        {
            return false;
        }

        final GradientData other = ( GradientData ) obj;
        if ( size () != other.size () )
        {
            return false;
        }

        for ( int i = 0; i < size (); i++ )
        {
            if ( !get ( i ).equals ( other.get ( i ) ) )
            {
                return false;
            }
        }
        return true;
    }

    @Override
    public GradientData clone ()
    {
        return Clone.cloneByFieldsSafely ( this );
    }

    /**
     * Returns default {@link GradientData} value.
     *
     * @return default {@link GradientData} value
     */
    public static GradientData getDefaultValue ()
    {
        final GradientData gradientData = new GradientData ();
        gradientData.addGradientColorData ( 0f, Color.RED );
        gradientData.addGradientColorData ( 0.25f, Color.YELLOW );
        gradientData.addGradientColorData ( 0.5f, Color.GREEN );
        gradientData.addGradientColorData ( 0.75f, Color.CYAN );
        gradientData.addGradientColorData ( 1f, Color.BLUE );
        return gradientData;
    }
}