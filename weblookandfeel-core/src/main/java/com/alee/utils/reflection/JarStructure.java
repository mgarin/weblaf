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

import com.alee.utils.FileUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.compare.Filter;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipFile;

/**
 * User: mgarin Date: 02.03.12 Time: 15:58
 */

public class JarStructure
{
    public static final JarEntryComparator COMPARATOR = new JarEntryComparator ();

    private String jarLocation;
    private JarEntry root;

    public JarStructure ( JarEntry root )
    {
        super ();
        setRoot ( root );
    }

    public String getJarLocation ()
    {
        return jarLocation;
    }

    public void setJarLocation ( String jarLocation )
    {
        this.jarLocation = jarLocation;
    }

    public JarEntry getRoot ()
    {
        return root;
    }

    public void setRoot ( JarEntry root )
    {
        this.root = root;
    }

    public List<JarEntry> getChildEntries ( JarEntry entry )
    {
        List<JarEntry> childs = entry != null ? entry.getChilds () : getRoot ().getChilds ();
        Collections.sort ( childs, COMPARATOR );
        return childs;
    }

    public JarEntry findEntryByName ( String name )
    {
        return findEntryByName ( name, getRoot () );
    }

    private JarEntry findEntryByName ( String name, JarEntry entry )
    {
        for ( JarEntry child : entry.getChilds () )
        {
            if ( FileUtils.getFileNamePart ( child.getName () ).equals ( name ) )
            {
                return child;
            }
            JarEntry found = findEntryByName ( name, child );
            if ( found != null )
            {
                return found;
            }
        }
        return null;
    }

    public List<JarEntry> findSimilarEntries ( String name )
    {
        return findSimilarEntries ( name.toLowerCase (), null );
    }

    public List<JarEntry> findSimilarEntries ( String name, Filter<JarEntry> filter )
    {
        return findSimilarEntries ( name.toLowerCase (), getRoot (), filter, new ArrayList<JarEntry> () );
    }

    private List<JarEntry> findSimilarEntries ( String name, JarEntry entry, Filter<JarEntry> filter, List<JarEntry> entries )
    {
        if ( entry.getName ().toLowerCase ().contains ( name ) )
        {
            if ( filter == null || filter.accept ( entry ) )
            {
                entries.add ( entry );
            }
        }
        for ( JarEntry child : entry.getChilds () )
        {
            findSimilarEntries ( name, child, filter, entries );
        }
        return entries;
    }

    public JarEntry getClassEntry ( Class forClass )
    {
        String[] packages = ReflectUtils.getClassPackages ( forClass );
        String classFileName = ReflectUtils.getJavaClassName ( forClass );

        int currentPackage = 0;
        JarEntry currentLevel = getRoot ();
        while ( currentLevel != null )
        {
            if ( currentPackage < packages.length )
            {
                currentLevel = currentLevel.getChildByName ( packages[ currentPackage ] );
            }
            else
            {
                currentLevel = currentLevel.getChildByName ( classFileName );
                break;
            }
            currentPackage++;
        }

        return currentLevel;
    }

    public void setPackageIcon ( Package packageType, ImageIcon icon )
    {
        setPackageIcon ( packageType.getName (), icon );
    }

    public void setPackageIcon ( String packageName, ImageIcon icon )
    {
        String[] packages = ReflectUtils.getPackages ( packageName );
        JarEntry currentLevel = getRoot ();
        for ( String currentPackage : packages )
        {
            if ( currentLevel != null )
            {
                currentLevel = currentLevel.getChildByName ( currentPackage );
            }
            else
            {
                return;
            }
        }
        currentLevel.setIcon ( icon );
    }

    public void setClassIcon ( Class classType, ImageIcon icon )
    {
        JarEntry classEntry = getClassEntry ( classType );
        if ( classEntry != null )
        {
            classEntry.setIcon ( icon );
        }
    }

    public InputStream getEntryInputStream ( JarEntry entry ) throws IOException
    {
        return new ZipFile ( getJarLocation () ).getInputStream ( entry.getZipEntry () );
    }
}