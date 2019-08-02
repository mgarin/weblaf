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

package com.alee.utils.swing;

import java.awt.*;

/**
 * Small extension for {@link java.awt.GridBagConstraints} providing more convenient constructors.
 *
 * @author Mikle Garin
 */
public class GridBagConstraints extends java.awt.GridBagConstraints
{
    /**
     * Default values for additional convenient constructors.
     */
    protected static final int DEFAULT_GRID_WIDTH = 1;
    protected static final int DEFAULT_GRID_HEIGHT = 1;
    protected static final double DEFAULT_WEIGHT_X = 0;
    protected static final double DEFAULT_WEIGHT_Y = 0;
    protected static final Insets DEFAULT_INSETS = new Insets ( 0, 0, 0, 0 );
    protected static final int DEFAULT_IPAD_X = 0;
    protected static final int DEFAULT_IPAD_Y = 0;

    /**
     * Creates a {@code java.awt.GridBagConstraints} object with all of its fields set to the passed-in arguments.
     */
    public GridBagConstraints ()
    {
        super ();
    }

    /**
     * Creates a {@code java.awt.GridBagConstraints} object with all of its fields set to the passed-in arguments.
     *
     * @param gridx The initial gridx value
     * @param gridy The initial gridy value
     */
    public GridBagConstraints ( final int gridx, final int gridy )
    {
        super ( gridx, gridy, DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT, DEFAULT_WEIGHT_X, DEFAULT_WEIGHT_Y, CENTER, BOTH, DEFAULT_INSETS,
                DEFAULT_IPAD_X, DEFAULT_IPAD_Y );
    }

    /**
     * Creates a {@code java.awt.GridBagConstraints} object with all of its fields set to the passed-in arguments.
     *
     * @param gridx The initial gridx value
     * @param gridy The initial gridy value
     * @param fill  The initial fill value
     */
    public GridBagConstraints ( final int gridx, final int gridy, final int fill )
    {
        super ( gridx, gridy, DEFAULT_GRID_WIDTH, DEFAULT_GRID_HEIGHT, DEFAULT_WEIGHT_X, DEFAULT_WEIGHT_Y, CENTER, fill, DEFAULT_INSETS,
                DEFAULT_IPAD_X, DEFAULT_IPAD_Y );
    }

    /**
     * Creates a {@code java.awt.GridBagConstraints} object with all of its fields set to the passed-in arguments.
     *
     * @param gridx      The initial gridx value
     * @param gridy      The initial gridy value
     * @param gridwidth  The initial gridwidth value
     * @param gridheight The initial gridheight value
     * @param fill       The initial fill value
     */
    public GridBagConstraints ( final int gridx, final int gridy, final int gridwidth, final int gridheight, final int fill )
    {
        super ( gridx, gridy, gridwidth, gridheight, DEFAULT_WEIGHT_X, DEFAULT_WEIGHT_Y, CENTER, fill, DEFAULT_INSETS, DEFAULT_IPAD_X,
                DEFAULT_IPAD_Y );
    }

    /**
     * Creates a {@code java.awt.GridBagConstraints} object with all of its fields set to the passed-in arguments.
     *
     * @param gridx      The initial gridx value
     * @param gridy      The initial gridy value
     * @param gridwidth  The initial gridwidth value
     * @param gridheight The initial gridheight value
     * @param weightx    The initial weightx value
     * @param weighty    The initial weighty value
     * @param fill       The initial fill value
     */
    public GridBagConstraints ( final int gridx, final int gridy, final int gridwidth, final int gridheight, final double weightx,
                                final double weighty, final int fill )
    {
        super ( gridx, gridy, gridwidth, gridheight, weightx, weighty, CENTER, fill, DEFAULT_INSETS, DEFAULT_IPAD_X, DEFAULT_IPAD_Y );
    }

    /**
     * Creates a {@code java.awt.GridBagConstraints} object with all of its fields set to the passed-in arguments.
     *
     * @param gridx      The initial gridx value
     * @param gridy      The initial gridy value
     * @param gridwidth  The initial gridwidth value
     * @param gridheight The initial gridheight value
     * @param weightx    The initial weightx value
     * @param weighty    The initial weighty value
     * @param anchor     The initial anchor value
     * @param fill       The initial fill value
     * @param insets     The initial insets value
     * @param ipadx      The initial ipadx value
     * @param ipady      The initial ipady value
     */
    public GridBagConstraints ( final int gridx, final int gridy, final int gridwidth, final int gridheight, final double weightx,
                                final double weighty, final int anchor, final int fill, final Insets insets, final int ipadx,
                                final int ipady )
    {
        super ( gridx, gridy, gridwidth, gridheight, weightx, weighty, anchor, fill, insets, ipadx, ipady );
    }
}