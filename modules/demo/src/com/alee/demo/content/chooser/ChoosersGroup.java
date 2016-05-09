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

package com.alee.demo.content.chooser;

import com.alee.demo.api.AbstractExampleGroup;
import com.alee.demo.content.chooser.dialog.DialogChoosersGroup;
import com.alee.demo.content.chooser.field.FieldChoosersGroup;
import com.alee.utils.CollectionUtils;

import java.util.List;

/**
 * @author Mikle Garin
 */

public class ChoosersGroup extends AbstractExampleGroup
{
    @Override
    public String getId ()
    {
        return "choosers";
    }

    @Override
    protected List<Class> getExampleGroupClasses ()
    {
        return CollectionUtils.<Class>asList ( DialogChoosersGroup.class, FieldChoosersGroup.class );
    }
}