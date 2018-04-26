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

package com.alee.utils.reflection;

/**
 * Enumeration representing {@link Class} relation types.
 * Use {@link #of(Class, Class)} method to determine {@link ClassRelationType} of two {@link Class}es.
 * Use {@link #of(Object, Object)} method to determine {@link ClassRelationType} of two {@link Object}s.
 *
 * @author Mikle Garin
 */
public enum ClassRelationType
{
    /**
     * Classes are the same.
     */
    SAME,

    /**
     * One class is an ancestor of onother one.
     */
    ANCESTOR,

    /**
     * One class is a descendant of onother one.
     */
    DESCENDANT,

    /**
     * Classes are not related.
     */
    UNRELATED;

    /**
     * Returns whether or not classes are the same.
     *
     * @return {@code true} if classes are the same, {@code false} otherwise
     */
    public boolean isSame ()
    {
        return this == SAME;
    }

    /**
     * Returns whether or not one class is an ancestor of onother one.
     *
     * @return {@code true} if one class is an ancestor of onother one, {@code false} otherwise
     */
    public boolean isAncestor ()
    {
        return this == ANCESTOR;
    }

    /**
     * Returns whether or not one class is a descendant of onother one.
     *
     * @return {@code true} if one class is a descendant of onother one, {@code false} otherwise
     */
    public boolean isDescendant ()
    {
        return this == DESCENDANT;
    }

    /**
     * Returns whether or not classes are not related.
     *
     * @return {@code true} if classes are not related, {@code false} otherwise
     */
    public boolean isUnrelated ()
    {
        return this == UNRELATED;
    }

    /**
     * Returns whether or not classes are related.
     *
     * @return {@code true} if classes are related, {@code false} otherwise
     */
    public boolean isRelated ()
    {
        return this != UNRELATED;
    }

    /**
     * Returns one object class relation to another one.
     *
     * @param one     some object
     * @param another another object
     * @return one object class relation to another one
     */
    public static ClassRelationType of ( final Object one, final Object another )
    {
        return of ( one.getClass (), another.getClass () );
    }

    /**
     * Returns one class relation to another one.
     *
     * @param one     some class
     * @param another another class
     * @return one class relation to another one
     */
    public static ClassRelationType of ( final Class one, final Class another )
    {
        if ( one == another )
        {
            return SAME;
        }
        else if ( one.isAssignableFrom ( another ) )
        {
            return DESCENDANT;
        }
        else if ( another.isAssignableFrom ( one ) )
        {
            return ANCESTOR;
        }
        else
        {
            return UNRELATED;
        }
    }
}