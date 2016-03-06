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

import com.alee.managers.settings.DefaultValue;
import com.alee.utils.ColorUtils;
import com.alee.utils.MergeUtils;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Data object for WebGradientColorChooser component.
 *
 * @author Mikle Garin
 */

@XStreamAlias ("GradientData")
public class GradientData implements Serializable, Cloneable, DefaultValue
{
    /**
     * Comparator to sort colors data on change.
     */
    private static final transient GradientColorDataComparator locationsComparator = new GradientColorDataComparator ();

    /**
     * All available colors data.
     */
    @XStreamImplicit (itemFieldName = "Color")
    private List<GradientColorData> gradientColorsData;

    /**
     * Constructs GradientData object with empty data list.
     */
    public GradientData ()
    {
        super ();
        gradientColorsData = new ArrayList<GradientColorData> ();
    }

    /**
     * Returns whether there is a GradientColorData for specified location or not.
     *
     * @param location location to search for GradientColorData
     * @return true if there is a GradientColorData for specified location, false otherwise
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
     * Adds GradientColorData into data list and sorts the list.
     *
     * @param gradientColorData GradientColorData to add
     */
    public void addGradientColorData ( final GradientColorData gradientColorData )
    {
        gradientColorsData.add ( gradientColorData );
        sort ();
    }

    /**
     * Adds GradientColorData with the specified location and color into data list and sorts the list.
     *
     * @param location GradientColorData location
     * @param color    GradientColorData color
     */
    public void addGradientColorData ( final float location, final Color color )
    {
        gradientColorsData.add ( new GradientColorData ( location, color ) );
        sort ();
    }

    /**
     * Removes GradientColorData from data list and sorts the list.
     *
     * @param gradientColorData GradientColorData to remove
     */
    public void removeGradientColorData ( final GradientColorData gradientColorData )
    {
        gradientColorsData.remove ( gradientColorData );
        sort ();
    }

    /**
     * Returns sorted data list.
     *
     * @return sorted data list
     */
    public List<GradientColorData> getGradientColorsData ()
    {
        sort ();
        return gradientColorsData;
    }

    /**
     * Sets data list and sorts it.
     *
     * @param gradientColorsData new data list
     */
    public void setGradientColorsData ( final List<GradientColorData> gradientColorsData )
    {
        this.gradientColorsData = gradientColorsData;
        sort ();
    }

    /**
     * Returns an array of fractions for LinearGradientPaint.
     *
     * @return an array of fractions for LinearGradientPaint
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
     * Returns an array of colors for LinearGradientPaint.
     *
     * @return an array of colors for LinearGradientPaint
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
     * Returns data list size.
     *
     * @return data list size
     */
    public int size ()
    {
        return gradientColorsData.size ();
    }

    /**
     * Returns GradientColorData at the specified index.
     *
     * @param index GradientColorData index
     * @return GradientColorData at the specified index
     */
    public GradientColorData get ( final int index )
    {
        return gradientColorsData.get ( index );
    }

    /**
     * Returns location for GradientColorData at the specified index.
     *
     * @param index GradientColorData index
     * @return location for GradientColorData at the specified index
     */
    public float getLocation ( final int index )
    {
        return get ( index ).getLocation ();
    }

    /**
     * Returns color for GradientColorData at the specified index.
     *
     * @param index GradientColorData index
     * @return color for GradientColorData at the specified index
     */
    public Color getColor ( final int index )
    {
        return get ( index ).getColor ();
    }

    /**
     * Returns first GradientColorData.
     *
     * @return first GradientColorData
     */
    public GradientColorData getFirst ()
    {
        return size () > 0 ? gradientColorsData.get ( 0 ) : null;
    }

    /**
     * Returns last GradientColorData.
     *
     * @return last GradientColorData
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

    /**
     * Returns whether this GradientData is equal to the specified object or not.
     *
     * @param obj object to compare with
     * @return true if this GradientData is equal to the specified object, false otherwise
     */
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

    /**
     * Returns cloned GradientData.
     *
     * @return cloned GradientData
     */
    @Override
    public GradientData clone ()
    {
        return MergeUtils.cloneByFieldsSafely ( this );
    }

    /**
     * Returns default GradientData value.
     *
     * @return default GradientData value
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