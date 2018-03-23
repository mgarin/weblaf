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

import com.alee.laf.combobox.WebComboBox;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;
import com.alee.utils.collection.ImmutableList;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * {@link WebComboBox} for choosing from list of {@link Locale} supported by {@link LanguageManager}.
 * Although you should be careful as {@link LanguageManager} allows using {@link Locale} that is not actually supported.
 * {@link LanguageManager} might also use supported {@link Locale} variation as selected one in which case it will also be selected here.
 * It is generally recommended to provide your custom language limitations based on which languages you want to support.
 *
 * @author Mikle Garin
 */

public class LanguageChooser extends WebComboBox
{
    /**
     * Constructs new {@link LanguageChooser}.
     */
    public LanguageChooser ()
    {
        this ( StyleId.auto );
    }

    /**
     * Constructs new {@link LanguageChooser}.
     *
     * @param locales {@link Locale}s to limit choice to
     */
    public LanguageChooser ( final Locale... locales )
    {
        this ( StyleId.auto, locales );
    }

    /**
     * Constructs new {@link LanguageChooser}.
     *
     * @param locales {@link Collection} of {@link Locale}s to limit choice to
     */
    public LanguageChooser ( final Collection<Locale> locales )
    {
        this ( StyleId.auto, locales );
    }

    /**
     * Constructs new {@link LanguageChooser}.
     *
     * @param model {@link LanguageChooserModel}
     */
    public LanguageChooser ( final LanguageChooserModel model )
    {
        this ( StyleId.auto, model );
    }

    /**
     * Constructs new {@link LanguageChooser}.
     *
     * @param id style ID
     */
    public LanguageChooser ( final StyleId id )
    {
        this ( id, new LanguageChooserModel () );
    }

    /**
     * Constructs new {@link LanguageChooser}.
     *
     * @param id      style ID
     * @param locales {@link Locale}s to limit choice to
     */
    public LanguageChooser ( final StyleId id, final Locale... locales )
    {
        this ( id, new LanguageChooserModel ( locales ) );
    }

    /**
     * Constructs new {@link LanguageChooser}.
     *
     * @param id      style ID
     * @param locales {@link Collection} of {@link Locale}s to limit choice to
     */
    public LanguageChooser ( final StyleId id, final Collection<Locale> locales )
    {
        this ( id, new LanguageChooserModel ( locales ) );
    }

    /**
     * Constructs new {@link LanguageChooser}.
     *
     * @param id    style ID
     * @param model {@link LanguageChooserModel}
     */
    public LanguageChooser ( final StyleId id, final LanguageChooserModel model )
    {
        super ( id, model );
        setRenderer ( new LanguageChooserRenderer () );
        addActionListener ( new ActionListener ()
        {
            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                // Executing later to avoid any possible interferences
                CoreSwingUtils.invokeLater ( new Runnable ()
                {
                    @Override
                    public void run ()
                    {
                        final Locale locale = ( Locale ) getSelectedItem ();
                        if ( locale != null )
                        {
                            LanguageManager.setLocale ( locale );
                        }
                    }
                } );
            }
        } );
    }

    @Override
    public LanguageChooserModel getModel ()
    {
        return ( LanguageChooserModel ) super.getModel ();
    }

    @Override
    public void setModel ( final ComboBoxModel model )
    {
        if ( !( model instanceof LanguageChooserModel ) )
        {
            throw new IllegalArgumentException ( "Only LanguageChooserModel instances are allowed" );
        }
        if ( dataModel != null )
        {
            removeLanguageListener ( ( LanguageChooserModel ) dataModel );
            removeDictionaryListener ( ( LanguageChooserModel ) dataModel );
        }
        super.setModel ( model );
        addLanguageListener ( ( LanguageChooserModel ) model );
        addDictionaryListener ( ( LanguageChooserModel ) model );
    }

    @Override
    public StyleId getDefaultStyleId ()
    {
        return StyleId.languagechooser;
    }

    /**
     * Returns {@link List} of {@link Locale}s to choice is limited to.
     *
     * @return {@link List} of {@link Locale}s to choice is limited to
     */
    public List<Locale> getLocales ()
    {
        return getModel ().getLocales ();
    }

    /**
     * Sets {@link Locale}s to limit choice to.
     *
     * @param locales {@link Locale}s to limit choice to
     */
    public void setLocales ( final Locale... locales )
    {
        setLocales ( new ImmutableList<Locale> ( locales ) );
    }

    /**
     * Sets {@link Collection} of {@link Locale}s to limit choice to.
     *
     * @param locales {@link Collection} of {@link Locale}s to limit choice to
     */
    public void setLocales ( final Collection<Locale> locales )
    {
        getModel ().setLocales ( locales );
    }
}