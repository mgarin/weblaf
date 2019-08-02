/*
 * Copyright 2006 Jerry Huxtable
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alee.graphics.filters;

/**
 * A filter to change the brightness and contrast of an image.
 *
 * @author Jerry Huxtable
 */
public class ContrastFilter extends TransferFilter
{
    private float brightness;
    private float contrast;

    /**
     * Constructs new contrast filter with default settings.
     */
    public ContrastFilter ()
    {
        this ( 1f, 1f );
    }

    /**
     * Constructs new contrast filter.
     *
     * @param brightness the brightness in the range 0 to 1
     * @param contrast   the contrast in the range 0 to 1
     */
    public ContrastFilter ( final float brightness, final float contrast )
    {
        super ();
        this.brightness = brightness;
        this.contrast = contrast;
    }

    @Override
    protected float transferFunction ( float f )
    {
        f = f * brightness;
        f = ( f - 0.5f ) * contrast + 0.5f;
        return f;
    }

    /**
     * Set the filter brightness.
     *
     * @param brightness the brightness in the range 0 to 1
     * @see #getBrightness
     */
    public void setBrightness ( final float brightness )
    {
        this.brightness = brightness;
        initialized = false;
    }

    /**
     * Get the filter brightness.
     *
     * @return the brightness in the range 0 to 1
     * @see #setBrightness
     */
    public float getBrightness ()
    {
        return brightness;
    }

    /**
     * Set the filter contrast.
     *
     * @param contrast the contrast in the range 0 to 1
     * @see #getContrast
     */
    public void setContrast ( final float contrast )
    {
        this.contrast = contrast;
        initialized = false;
    }

    /**
     * Get the filter contrast.
     *
     * @return the contrast in the range 0 to 1
     * @see #setContrast
     */
    public float getContrast ()
    {
        return contrast;
    }

    @Override
    public String toString ()
    {
        return "Colors/Contrast...";
    }
}