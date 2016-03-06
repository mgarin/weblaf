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

/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

/*
Copyright 2006 Jerry Huxtable

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.alee.graphics.filters;

import java.awt.*;
import java.awt.image.ColorModel;

public final class MiscComposite implements Composite
{

    public final static int BLEND = 0;
    public final static int ADD = 1;
    public final static int SUBTRACT = 2;
    public final static int DIFFERENCE = 3;

    public final static int MULTIPLY = 4;
    public final static int DARKEN = 5;
    public final static int BURN = 6;
    public final static int COLOR_BURN = 7;

    public final static int SCREEN = 8;
    public final static int LIGHTEN = 9;
    public final static int DODGE = 10;
    public final static int COLOR_DODGE = 11;

    public final static int HUE = 12;
    public final static int SATURATION = 13;
    public final static int VALUE = 14;
    public final static int COLOR = 15;

    public final static int OVERLAY = 16;
    public final static int SOFT_LIGHT = 17;
    public final static int HARD_LIGHT = 18;
    public final static int PIN_LIGHT = 19;

    public final static int EXCLUSION = 20;
    public final static int NEGATION = 21;
    public final static int AVERAGE = 22;

    public final static int STENCIL = 23;
    public final static int SILHOUETTE = 24;

    private static final int MIN_RULE = BLEND;
    private static final int MAX_RULE = SILHOUETTE;

    public static String[] RULE_NAMES = { "Normal", "Add", "Subtract", "Difference",

            "Multiply", "Darken", "Burn", "Color Burn",

            "Screen", "Lighten", "Dodge", "Color Dodge",

            "Hue", "Saturation", "Brightness", "Color",

            "Overlay", "Soft Light", "Hard Light", "Pin Light",

            "Exclusion", "Negation", "Average",

            "Stencil", "Silhouette", };

    protected float extraAlpha;
    protected int rule;

    private MiscComposite ( final int rule )
    {
        this ( rule, 1.0f );
    }

    private MiscComposite ( final int rule, final float alpha )
    {
        if ( alpha < 0.0f || alpha > 1.0f )
        {
            throw new IllegalArgumentException ( "alpha value out of range" );
        }
        if ( rule < MIN_RULE || rule > MAX_RULE )
        {
            throw new IllegalArgumentException ( "unknown composite rule" );
        }
        this.rule = rule;
        this.extraAlpha = alpha;
    }

    public static Composite getInstance ( final int rule, final float alpha )
    {
        switch ( rule )
        {
            case MiscComposite.BLEND:
                return AlphaComposite.getInstance ( AlphaComposite.SRC_OVER, alpha );
            //		case MiscComposite.ADD:
            //			return new AddComposite( alpha );
            //		case MiscComposite.SUBTRACT:
            //			return new SubtractComposite( alpha );
            //		case MiscComposite.DIFFERENCE:
            //			return new DifferenceComposite( alpha );
            //		case MiscComposite.MULTIPLY:
            //			return new MultiplyComposite( alpha );
            //		case MiscComposite.DARKEN:
            //			return new DarkenComposite( alpha );
            //		case MiscComposite.BURN:
            //			return new BurnComposite( alpha );
            //		case MiscComposite.COLOR_BURN:
            //			return new ColorBurnComposite( alpha );
            //		case MiscComposite.SCREEN:
            //			return new ScreenComposite( alpha );
            //		case MiscComposite.LIGHTEN:
            //			return new LightenComposite( alpha );
            //		case MiscComposite.DODGE:
            //			return new DodgeComposite( alpha );
            //		case MiscComposite.COLOR_DODGE:
            //			return new ColorDodgeComposite( alpha );
            //		case MiscComposite.HUE:
            //			return new HueComposite( alpha );
            //		case MiscComposite.SATURATION:
            //			return new SaturationComposite( alpha );
            //		case MiscComposite.VALUE:
            //			return new ValueComposite( alpha );
            //		case MiscComposite.COLOR:
            //			return new ColorComposite( alpha );
            //		case MiscComposite.OVERLAY:
            //			return new OverlayComposite( alpha );
            //		case MiscComposite.SOFT_LIGHT:
            //			return new SoftLightComposite( alpha );
            //		case MiscComposite.HARD_LIGHT:
            //			return new HardLightComposite( alpha );
            //		case MiscComposite.PIN_LIGHT:
            //			return new PinLightComposite( alpha );
            //		case MiscComposite.EXCLUSION:
            //			return new ExclusionComposite( alpha );
            //		case MiscComposite.NEGATION:
            //			return new NegationComposite( alpha );
            //		case MiscComposite.AVERAGE:
            //			return new AverageComposite( alpha );
            case MiscComposite.STENCIL:
                return AlphaComposite.getInstance ( AlphaComposite.DST_IN, alpha );
            case MiscComposite.SILHOUETTE:
                return AlphaComposite.getInstance ( AlphaComposite.DST_OUT, alpha );
        }
        return new MiscComposite ( rule, alpha );
    }

    @Override
    public CompositeContext createContext ( final ColorModel srcColorModel, final ColorModel dstColorModel, final RenderingHints hints )
    {
        return new MiscCompositeContext ( rule, extraAlpha, srcColorModel, dstColorModel );
    }

    public float getAlpha ()
    {
        return extraAlpha;
    }

    public int getRule ()
    {
        return rule;
    }

    public int hashCode ()
    {
        return ( Float.floatToIntBits ( extraAlpha ) * 31 + rule );
    }

    public boolean equals ( final Object o )
    {
        if ( !( o instanceof MiscComposite ) )
        {
            return false;
        }
        final MiscComposite c = ( MiscComposite ) o;
        return rule == c.rule && extraAlpha == c.extraAlpha;
    }
}