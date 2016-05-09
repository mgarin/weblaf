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
import java.util.Locale;
import java.util.zip.ZipFile;

/**
 * This class represents single JAR file structure.
 * It might be really useful to navigate through JAR
 *
 * @author Mikle Garin
 */

public class JarStructure
{
    /**
     * Comparator for {@link com.alee.utils.reflection.JarEntry}.
     */
    public static final JarEntryComparator COMPARATOR = new JarEntryComparator ();

    /**
     * Location of JAR file which structure is represented by this object.
     * Since local copy of file is always required to create this structure this field always points at existing local JAR file.
     */
    private String jarLocation;

    /**
     * Root {@link com.alee.utils.reflection.JarEntry}.
     * Represents JAR structure itself using nested {@link com.alee.utils.reflection.JarEntry}.
     */
    private JarEntry root;

    /**
     * Constructs new empty JAR structure.
     */
    public JarStructure ()
    {
        super ();
    }

    /**
     * Constructs new JAR structure with the specified root {@link com.alee.utils.reflection.JarEntry}.
     *
     * @param root root {@link com.alee.utils.reflection.JarEntry}
     */
    public JarStructure ( final JarEntry root )
    {
        super ();
        setRoot ( root );
    }

    public String getJarLocation ()
    {
        return jarLocation;
    }

    public void setJarLocation ( final String jarLocation )
    {
        this.jarLocation = jarLocation;
    }

    public JarEntry getRoot ()
    {
        return root;
    }

    public void setRoot ( final JarEntry root )
    {
        this.root = root;
    }

    public List<JarEntry> getChildEntries ( final JarEntry entry )
    {
        final List<JarEntry> children = entry != null ? entry.getChildren () : getRoot ().getChildren ();
        Collections.sort ( children, COMPARATOR );
        return children;
    }

    public JarEntry findEntryByName ( final String name )
    {
        return findEntryByName ( name, getRoot () );
    }

    private JarEntry findEntryByName ( final String name, final JarEntry entry )
    {
        for ( final JarEntry child : entry.getChildren () )
        {
            if ( FileUtils.getFileNamePart ( child.getName () ).equals ( name ) )
            {
                return child;
            }
            final JarEntry found = findEntryByName ( name, child );
            if ( found != null )
            {
                return found;
            }
        }
        return null;
    }

    public List<JarEntry> findSimilarEntries ( final String name )
    {
        return findSimilarEntries ( name.toLowerCase ( Locale.ROOT ), null );
    }

    public List<JarEntry> findSimilarEntries ( final String name, final Filter<JarEntry> filter )
    {
        return findSimilarEntries ( name.toLowerCase ( Locale.ROOT ), getRoot (), filter, new ArrayList<JarEntry> () );
    }

    private List<JarEntry> findSimilarEntries ( final String name, final JarEntry entry, final Filter<JarEntry> filter,
                                                final List<JarEntry> entries )
    {
        if ( entry.getName ().toLowerCase ( Locale.ROOT ).contains ( name ) )
        {
            if ( filter == null || filter.accept ( entry ) )
            {
                entries.add ( entry );
            }
        }
        for ( final JarEntry child : entry.getChildren () )
        {
            findSimilarEntries ( name, child, filter, entries );
        }
        return entries;
    }

    public JarEntry getClassEntry ( final Class forClass )
    {
        final String[] packages = ReflectUtils.getClassPackages ( forClass );
        final String classFileName = ReflectUtils.getJavaClassName ( forClass );

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

    public void setPackageIcon ( final Package packageType, final ImageIcon icon )
    {
        setPackageIcon ( packageType.getName (), icon );
    }

    public void setPackageIcon ( final String packageName, final ImageIcon icon )
    {
        final String[] packages = ReflectUtils.getPackages ( packageName );
        JarEntry currentLevel = getRoot ();
        for ( final String currentPackage : packages )
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

    public void setClassIcon ( final Class classType, final ImageIcon icon )
    {
        final JarEntry classEntry = getClassEntry ( classType );
        if ( classEntry != null )
        {
            classEntry.setIcon ( icon );
        }
    }

    public InputStream getEntryInputStream ( final JarEntry entry ) throws IOException
    {
        return new ZipFile ( getJarLocation () ).getInputStream ( entry.getZipEntry () );
    }
}