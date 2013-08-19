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
    private DictionariesTree dictionariesTree;

    public static void main ( String[] args )
    {
        WebLookAndFeel.install ();
        TestFrame.show ( new WebScrollPane ( new LanguageEditor ()
        {
            {
                for ( Dictionary d : LanguageManager.getDictionaries () )
                {
                    loadDictionary ( d );
                }
                loadDictionary ( LanguageManager.loadDictionary ( WebLookAndFeel.class, "resources/language.xml" ) );

                getDictionariesTree ().expandTillRecords ();
                getDictionariesTree ().setRootVisible ( false );
            }
        }, false ) );
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

    public void loadDictionary ( Dictionary dictionary )
    {
        dictionariesTree.loadDictionary ( dictionary );
    }
}