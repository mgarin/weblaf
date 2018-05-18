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

package com.alee.extended.checkbox;

import com.alee.laf.button.AbstractTristateCheckBoxDescriptor;
import com.alee.managers.style.StyleId;

/**
 * Basic descriptor for {@link WebTristateCheckBox} component.
 * For creating custom {@link WebTristateCheckBox} descriptor {@link AbstractTristateCheckBoxDescriptor} class can be extended.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager">How to use StyleManager</a>
 * @see com.alee.managers.style.StyleManager
 * @see com.alee.managers.style.StyleManager#registerComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#unregisterComponentDescriptor(com.alee.managers.style.ComponentDescriptor)
 * @see com.alee.managers.style.StyleManager#initializeDescriptors()
 */
public final class TristateCheckBoxDescriptor extends AbstractTristateCheckBoxDescriptor<WebTristateCheckBox, WTristateCheckBoxUI>
{
    /**
     * Constructs new descriptor for {@link WebTristateCheckBox} component.
     */
    public TristateCheckBoxDescriptor ()
    {
        super ( "tristatecheckbox", WebTristateCheckBox.class, "TristateCheckBoxUI", WTristateCheckBoxUI.class, WebTristateCheckBoxUI.class,
                StyleId.tristatecheckbox );
    }
}