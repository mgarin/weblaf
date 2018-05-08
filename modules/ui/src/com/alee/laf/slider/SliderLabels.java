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

package com.alee.laf.slider;

import com.alee.api.jdk.Objects;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.label.WebLabel;
import com.alee.managers.style.StyleId;

import javax.swing.*;
import javax.swing.plaf.UIResource;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

/**
 * Custom {@link Dictionary} implementation for default {@link JSlider} labels generation.
 * Unlike the {@code SmartHashtable} in {@link JSlider} this implementation always uses {@link JSlider} settings.
 *
 * @author Mikle Garin
 */

public class SliderLabels extends Hashtable implements PropertyChangeListener
{
    /**
     * {@link JSlider} this {@link SliderLabel} is attached to.
     */
    protected final JSlider slider;

    /**
     * Customized first value to display label for.
     */
    private final Integer start;

    /**
     * Customized distance between displayed values.
     */
    private final Integer distance;

    /**
     * Constructs new {@link SliderLabel}.
     *
     * @param slider {@link JSlider} this {@link SliderLabel} will be attached to
     */
    public SliderLabels ( final JSlider slider )
    {
        this ( slider, slider.getMinimum (), slider.getMajorTickSpacing () );
    }

    /**
     * Constructs new {@link SliderLabel}.
     *
     * @param slider   {@link JSlider} this {@link SliderLabel} will be attached to
     * @param start    first value to display label for
     * @param distance distance between displayed values
     */
    public SliderLabels ( final JSlider slider, final int start, final int distance )
    {
        super ();
        this.slider = slider;
        this.start = start != slider.getMinimum () ? start : null;
        this.distance = distance != slider.getMajorTickSpacing () ? distance : null;
        createLabels ();
    }

    /**
     * Returns first value to display label for.
     * It will always have its own label displayed.
     *
     * @return first value to display label for
     */
    protected int getStart ()
    {
        return start != null ? start : slider.getMinimum ();
    }

    /**
     * Returns distance between displayed values.
     * Multiple labels will be generated based on this distance.
     *
     * @return distance between displayed values
     */
    protected int getDistance ()
    {
        return distance != null ? distance : slider.getMajorTickSpacing ();
    }

    /**
     * Returns last value to display label for.
     * Not that it might not receive an actual label depending on increment.
     *
     * @return last value to display label for
     */
    protected int getEnd ()
    {
        return slider.getMaximum ();
    }

    /**
     * Returns array of properties that affect displayed slider labels.
     *
     * @return array of properties that affect displayed slider labels
     */
    protected Object[] getProperties ()
    {
        final List<String> properties = new ArrayList<String> ( 3 );
        properties.add ( WebLookAndFeel.MINIMUM_PROPERTY );
        properties.add ( WebLookAndFeel.MAXIMUM_PROPERTY );
        if ( distance == null )
        {
            properties.add ( WebLookAndFeel.MAJOR_TICK_SPACING_PROPERTY );
        }
        return properties.toArray ();
    }

    @Override
    public void propertyChange ( final PropertyChangeEvent e )
    {
        if ( Objects.equals ( e.getPropertyName (), getProperties () ) )
        {
            // Save the labels that were added by the developer
            final Enumeration previousKeys = slider.getLabelTable ().keys ();
            final Hashtable hashtable = new Hashtable ();
            while ( previousKeys.hasMoreElements () )
            {
                final Object key = previousKeys.nextElement ();
                final Object value = slider.getLabelTable ().get ( key );
                if ( !( value instanceof SliderLabel ) )
                {
                    hashtable.put ( key, value );
                }
            }

            // Recreate default labels
            clear ();
            createLabels ();

            // Add the saved labels
            final Enumeration keys = hashtable.keys ();
            while ( keys.hasMoreElements () )
            {
                final Object key = keys.nextElement ();
                put ( key, hashtable.get ( key ) );
            }

            // Update slider label table
            slider.setLabelTable ( this );
        }
    }

    /**
     * Constructs all labels and saves them.
     */
    protected void createLabels ()
    {
        final int distance = getDistance ();
        if ( distance > 0 )
        {
            for ( int value = getStart (); value <= getEnd (); value += distance )
            {
                put ( value, createLabel ( value ) );
            }
        }
    }

    /**
     * Returns new label component.
     *
     * @param value label value
     * @return new label component
     */
    protected JComponent createLabel ( final int value )
    {
        return new SliderLabel ( Integer.toString ( value ) );
    }

    /**
     * Customized {@link WebLabel} for {@link JSlider} labels display.
     */
    protected class SliderLabel extends WebLabel implements UIResource
    {
        /**
         * Constructs new {@link SliderLabel}.
         *
         * @param text displayed text
         */
        public SliderLabel ( final String text )
        {
            super ( StyleId.sliderTickLabel.at ( slider ), text );
            setName ( "Slider.label" );
        }

        @Override
        public Font getFont ()
        {
            final Font font = super.getFont ();
            if ( font != null && !( font instanceof UIResource ) )
            {
                return font;
            }
            return slider.getFont ();
        }
    }
}