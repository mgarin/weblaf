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

package com.alee.extended.language;

import com.alee.managers.language.LanguageUtils;
import com.alee.painter.decoration.IDecoration;
import com.alee.painter.decoration.content.AbstractTextContent;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;
import java.util.Locale;

/**
 * Custom content for {@link LanguageChooserRenderer} items locale.
 *
 * @param <E> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */

@XStreamAlias ( "LanguageItemLocale" )
public class LanguageItemLocale<E extends JComponent, D extends IDecoration<E, D>, I extends AbstractTextContent<E, D, I>>
        extends AbstractTextContent<E, D, I>
{
    /**
     * Client property key locale is stored under in {@link JComponent}.
     */
    public static final String LOCALE_VALUE_KEY = "locale.value";

    @Override
    public String getId ()
    {
        return id != null ? id : "locale";
    }

    @Override
    protected String getText ( final E c, final D d )
    {
        final Locale locale = ( Locale ) c.getClientProperty ( LOCALE_VALUE_KEY );
        return locale != null ? LanguageUtils.toString ( locale ) : null;
    }

    @Override
    protected int getMnemonicIndex ( final E c, final D d )
    {
        return -1;
    }
}