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

package com.alee.painter.decoration.content;

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.managers.language.LanguageUtils;
import com.alee.painter.decoration.IDecoration;
import com.thoughtworks.xstream.annotations.XStreamAlias;

import javax.swing.*;

/**
 * {@link AbstractTextContent} implementation displaying {@link JComponent}s current {@link java.util.Locale}.
 *
 * @param <C> component type
 * @param <D> decoration type
 * @param <I> content type
 * @author Mikle Garin
 */
@XStreamAlias ( "LocaleTextContent" )
public class LocaleTextContent<C extends JComponent, D extends IDecoration<C, D>, I extends AbstractTextContent<C, D, I>>
        extends AbstractTextContent<C, D, I>
{
    @Nullable
    @Override
    public String getId ()
    {
        return id != null ? id : "locale";
    }

    @Override
    protected String getText ( final C c, final D d )
    {
        return LanguageUtils.toString ( c.getLocale () );
    }

    @Override
    protected int getMnemonicIndex ( final C c, final D d )
    {
        return -1;
    }
}