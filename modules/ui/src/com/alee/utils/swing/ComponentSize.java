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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.api.merge.Mergeable;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

import java.awt.*;
import java.io.Serializable;

/**
 * Set of settings for defining {@link Component} size.
 * This is just an utility class that is used for convenient size configuration in XML.
 *
 * @author Mikle Garin
 */
@XStreamAlias ( "ComponentSize" )
public class ComponentSize implements Mergeable, Cloneable, Serializable
{
    /**
     * {@link Component} size calculation {@link Type}.
     */
    @Nullable
    @XStreamAsAttribute
    protected Type type;

    /**
     * {@link Component} minimum width, {@code 0} by default.
     * For {@link Type#none} it will have no effect.
     * For {@link Type#minimum} it will be used if greater than component's minimum width.
     * For {@link Type#preferred} it will be used if greater than component's preferred width.
     * For {@link Type#fixed} it will be used as preferred width.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer minimumWidth;

    /**
     * {@link Component} minimum height, {@code 0} by default.
     * For {@link Type#none} it will have no effect.
     * For {@link Type#minimum} it will be used if greater than component's minimum width.
     * For {@link Type#preferred} it will be used if greater than component's preferred width.
     * For {@link Type#fixed} it will be used as preferred width.
     */
    @Nullable
    @XStreamAsAttribute
    protected Integer minimumHeight;

    /**
     * Additional {@link Component} insets.
     */
    @Nullable
    @XStreamAsAttribute
    protected Insets insets;

    /**
     * Returns component size calculation {@link Type}.
     *
     * @return component size calculation {@link Type}
     */
    @NotNull
    public Type getType ()
    {
        return type != null ? type : Type.none;
    }

    /**
     * Returns component minimum width.
     *
     * @return component minimum width
     */
    public int getMinimumWidth ()
    {
        return minimumWidth != null ? minimumWidth : 0;
    }

    /**
     * Returns component minimum height.
     *
     * @return component minimum height
     */
    public int getMinimumHeight ()
    {
        return minimumHeight != null ? minimumHeight : 0;
    }

    /**
     * Returns additional {@link Component} insets.
     *
     * @return additional {@link Component} insets
     */
    @NotNull
    public Insets getInsets ()
    {
        return insets != null ? new Insets ( insets.top, insets.left, insets.bottom, insets.right ) : new Insets ( 0, 0, 0, 0 );
    }

    /**
     * Returns {@link Component}'s size according to these {@link ComponentSize} settings.
     *
     * @param component {@link Component}
     * @return {@link Component}'s size according to these {@link ComponentSize} settings
     */
    @NotNull
    public Dimension size ( @NotNull final Component component )
    {
        final Dimension size;
        switch ( getType () )
        {
            default:
            case none:
            {
                size = new Dimension ( 0, 0 );
            }
            break;

            case fixed:
            {
                size = new Dimension (
                        ( minimumWidth != null ? minimumWidth : 0 ) + ( insets != null ? insets.left + insets.right : 0 ),
                        ( minimumHeight != null ? minimumHeight : 0 ) + ( insets != null ? insets.top + insets.bottom : 0 )
                );
            }
            break;

            case minimum:
            case preferred:
            case maximum:
            {
                final Dimension min = getType ().size ( component );
                size = new Dimension (
                        ( minimumWidth != null ? Math.max ( minimumWidth, min.width ) : min.width ) +
                                ( insets != null ? insets.left + insets.right : 0 ),
                        ( minimumHeight != null ? Math.max ( minimumHeight, min.height ) : min.height ) +
                                ( insets != null ? insets.top + insets.bottom : 0 )
                );
            }
            break;
        }
        return size;
    }

    /**
     * {@link ComponentSize} type.
     */
    public static enum Type
    {
        /**
         * Always return [0,0] size.
         * Insets aren't taken into account either.
         */
        none,

        /**
         * Returns specified minimum size.
         */
        fixed,

        /**
         * Return either minimum {@link Component}'s size or specified minimum size if it is larger.
         * Insets are added to the acquired minimum size.
         */
        minimum,

        /**
         * Return either preferred {@link Component}'s size or specified minimum size if it is larger.
         * Insets are added to the acquired preferred size.
         */
        preferred,

        /**
         * Return either maximum {@link Component}'s size or specified minimum size if it is larger.
         * Insets are added to the acquired maximum size.
         */
        maximum;

        /**
         * Returns {@link Component} size of this {@link Type}.
         *
         * @param component {@link Component}
         * @return {@link Component} size of this {@link Type}
         */
        @NotNull
        public Dimension size ( @NotNull final Component component )
        {
            final Dimension size;
            switch ( this )
            {
                default:
                case none:
                case fixed:
                    size = new Dimension ( 0, 0 );
                    break;

                case minimum:
                    size = component.getMinimumSize ();
                    break;

                case preferred:
                    size = component.getPreferredSize ();
                    break;

                case maximum:
                    size = component.getMaximumSize ();
                    break;
            }
            return size;
        }
    }
}