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

package com.alee.managers.notification;

import com.alee.managers.language.LM;

/**
 * This enumeration represents available predefined question notification option types.
 * All these options have attached translations for each supported language to simplify their usage.
 *
 * @author Mikle Garin
 */
public enum NotificationOption
{
    /**
     * todo Add in future:
     * todo play
     * todo pause
     * todo stop
     * todo download
     * todo upload
     * todo delete
     * todo find
     * todo replace
     */

    /**
     * Yes option.
     */
    yes,

    /**
     * No option.
     */
    no,

    /**
     * Cancel option.
     */
    cancel,

    /**
     * Choose option.
     */
    choose,

    /**
     * Accept option.
     */
    accept,

    /**
     * Decline option.
     */
    decline,

    /**
     * Decline option.
     */
    apply,

    /**
     * Decline option.
     */
    commit,

    /**
     * Discard option.
     */
    discard,

    /**
     * Reset option.
     */
    reset,

    /**
     * Retry option.
     */
    retry,

    /**
     * Save option.
     */
    save,

    /**
     * Open option.
     */
    open,

    /**
     * Setup option.
     */
    setup,

    /**
     * Configure option.
     */
    configure,

    /**
     * Install option.
     */
    install,

    /**
     * Uninstall option.
     */
    uninstall;

    /**
     * Returns language key for this option.
     *
     * @return language key for this option
     */
    public String getLanguageKey ()
    {
        return "weblaf.ex.notification." + this;
    }

    /**
     * Returns translation text for this option.
     *
     * @return translation text for this option
     */
    public String getText ()
    {
        return LM.get ( getLanguageKey () );
    }
}