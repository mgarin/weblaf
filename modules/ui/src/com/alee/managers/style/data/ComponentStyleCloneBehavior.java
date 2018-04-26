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

package com.alee.managers.style.data;

import com.alee.api.clone.RecursiveClone;
import com.alee.api.clone.behavior.ReflectionCloneBehavior;
import com.alee.utils.CollectionUtils;
import com.alee.utils.reflection.ModifierType;

/**
 * Special {@link ReflectionCloneBehavior} extension for {@link ComponentStyle} objects.
 * It performs post-clone updates required to properly copy {@link ComponentStyle} with all nested styles.
 *
 * @author Mikle Garin
 */
public final class ComponentStyleCloneBehavior extends ReflectionCloneBehavior<ComponentStyle>
{
    /**
     * Constructs new {@link ComponentStyleCloneBehavior}.
     */
    public ComponentStyleCloneBehavior ()
    {
        super ( Policy.all, ModifierType.STATIC );
    }

    @Override
    public boolean supports ( final RecursiveClone clone, final Object object )
    {
        return super.supports ( clone, object ) && object instanceof ComponentStyle;
    }

    @Override
    public ComponentStyle clone ( final RecursiveClone clone, final ComponentStyle object, final int depth )
    {
        final ComponentStyle styleCopy = super.clone ( clone, object, depth );

        /**
         * Updating transient parent field for child {@link ComponentStyle}s.
         * This is important since their parent have been cloned and needs to be updated.
         * Zero depth cloned object doesn't need parent to be updated, it is preserved upon clone.
         */
        if ( CollectionUtils.notEmpty ( styleCopy.getNestedStyles () ) )
        {
            for ( final ComponentStyle style : styleCopy.getNestedStyles () )
            {
                style.setParent ( styleCopy );
            }
        }

        return styleCopy;
    }
}