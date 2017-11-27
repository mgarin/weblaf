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

import com.alee.utils.ninepatch.NinePatchInterval;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.awt.*;
import java.io.Serializable;
import java.util.List;

/**
 * @author Mikle Garin
 */

@XStreamAlias ( "NinePatchInfo" )
public class NinePatchInfo implements Serializable
{
    @XStreamAsAttribute
    private Dimension imageSize;

    @XStreamAsAttribute
    private Insets margin;

    @XStreamImplicit ( itemFieldName = "hor" )
    private List<NinePatchInterval> horizontalStretch;

    @XStreamImplicit ( itemFieldName = "ver" )
    private List<NinePatchInterval> verticalStretch;

    public NinePatchInfo ()
    {
        super ();
    }

    public Dimension getImageSize ()
    {
        return imageSize;
    }

    public void setImageSize ( Dimension imageSize )
    {
        this.imageSize = imageSize;
    }

    public Insets getMargin ()
    {
        return margin;
    }

    public void setMargin ( Insets margin )
    {
        this.margin = margin;
    }

    public List<NinePatchInterval> getHorizontalStretch ()
    {
        return horizontalStretch;
    }

    public void setHorizontalStretch ( List<NinePatchInterval> horizontalStretch )
    {
        this.horizontalStretch = horizontalStretch;
    }

    public List<NinePatchInterval> getVerticalStretch ()
    {
        return verticalStretch;
    }

    public void setVerticalStretch ( List<NinePatchInterval> verticalStretch )
    {
        this.verticalStretch = verticalStretch;
    }

    @Override
    public boolean equals ( Object obj )
    {
        if ( obj != null && obj instanceof NinePatchInfo )
        {
            NinePatchInfo info = ( NinePatchInfo ) obj;
            return getMargin ().equals ( info.getMargin () ) &&
                    equals ( getHorizontalStretch (), info.getHorizontalStretch () ) &&
                    equals ( getVerticalStretch (), info.getVerticalStretch () );
        }
        else
        {
            return false;
        }
    }

    public static boolean equals ( List<NinePatchInterval> npi1, List<NinePatchInterval> npi2 )
    {
        if ( npi1 == null && npi2 == null )
        {
            return true;
        }
        else if ( npi1 == null || npi2 == null )
        {
            return false;
        }
        else if ( npi1.size () != npi2.size () )
        {
            return false;
        }
        else
        {
            for ( int i = 0; i < npi1.size (); i++ )
            {
                if ( !npi1.get ( i ).equals ( npi2.get ( i ) ) )
                {
                    return false;
                }
            }
            return true;
        }
    }
}