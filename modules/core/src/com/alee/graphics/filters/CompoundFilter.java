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

import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;

/**
 * A BufferedImageOp which combines two other BufferedImageOps, one after the other.
 *
 * @author Jerry Huxtable
 */

public class CompoundFilter extends AbstractBufferedImageOp
{
    private final BufferedImageOp filter1;
    private final BufferedImageOp filter2;

    /**
     * Construct a CompoundFilter.
     *
     * @param filter1 the first filter
     * @param filter2 the second filter
     */
    public CompoundFilter ( final BufferedImageOp filter1, final BufferedImageOp filter2 )
    {
        this.filter1 = filter1;
        this.filter2 = filter2;
    }

    @Override
    public BufferedImage filter ( final BufferedImage src, final BufferedImage dst )
    {
        BufferedImage image = filter1.filter ( src, dst );
        image = filter2.filter ( image, dst );
        return image;
    }
}