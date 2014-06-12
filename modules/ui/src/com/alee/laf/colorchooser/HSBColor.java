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

package com.alee.laf.colorchooser;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;

/**
 * User: mgarin Date: 10.03.11 Time: 18:52
 */

@XStreamAlias ( "HSBColor" )
public class HSBColor
{
    // Values: 0f-1f (0-360 degrees)
    @XStreamAsAttribute
    private float hue = 0f;

    // Values: 0f-1f (0-100%)
    @XStreamAsAttribute
    private float saturation = 0f;

    // Values: 0f-1f (0-100%)
    @XStreamAsAttribute
    private float brightness = 0f;

    public HSBColor ()
    {
        super ();
    }

    public HSBColor ( float hue, float saturation, float brightness )
    {
        super ();
        this.hue = hue;
        this.saturation = saturation;
        this.brightness = brightness;
    }

    public HSBColor ( Color color )
    {
        this ( color.getRed (), color.getGreen (), color.getBlue () );
    }

    public HSBColor ( int red, int green, int blue )
    {
        super ();

        float[] values = Color.RGBtoHSB ( red, green, blue, null );
        this.hue = values[ 0 ];
        this.saturation = values[ 1 ];
        this.brightness = values[ 2 ];
    }

    public float getBrightness ()
    {
        return brightness;
    }

    public void setBrightness ( float brightness )
    {
        this.brightness = brightness;
    }

    public float getHue ()
    {
        return hue;
    }

    public void setHue ( float hue )
    {
        this.hue = hue;
    }

    public float getSaturation ()
    {
        return saturation;
    }

    public void setSaturation ( float saturation )
    {
        this.saturation = saturation;
    }

    public Color getColor ()
    {
        return new Color ( Color.HSBtoRGB ( hue, saturation, brightness ) );
    }

    public String toString ()
    {
        return getClass ().getCanonicalName () + "[h=" + getHue () + ",s=" + getSaturation () +
                ",b=" + getBrightness () + "]";
    }
}
