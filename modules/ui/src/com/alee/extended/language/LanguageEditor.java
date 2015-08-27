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

import com.alee.extended.window.TestFrame;
import com.alee.managers.style.StyleId;
import com.alee.laf.WebLookAndFeel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.scroll.WebScrollPane;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.language.data.Dictionary;

/**
 * User: mgarin Date: 16.05.12 Time: 14:33
 */

public class LanguageEditor extends WebPanel
{
    private final DictionariesTree dictionariesTree;

    public static void main ( final String[] args )
    {
        WebLookAndFeel.install ();

        final LanguageEditor languageEditor = new LanguageEditor ();

        for ( final Dictionary d : LanguageManager.getDictionaries () )
        {
            languageEditor.loadDictionary ( d );
        }
        languageEditor.loadDictionary ( LanguageManager.loadDictionary ( WebLookAndFeel.class, "resources/language.xml" ) );

        languageEditor.getDictionariesTree ().expandTillRecords ();
        languageEditor.getDictionariesTree ().setRootVisible ( false );

        TestFrame.show ( new WebScrollPane ( StyleId.scrollpaneUndecorated, languageEditor ) );
    }

    public LanguageEditor ()
    {
        super ();

        dictionariesTree = new DictionariesTree ();
        add ( dictionariesTree );
    }

    public DictionariesTree getDictionariesTree ()
    {
        return dictionariesTree;
    }

    public void loadDictionary ( final Dictionary dictionary )
    {
        dictionariesTree.loadDictionary ( dictionary );
    }
}