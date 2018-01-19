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

package com.alee.utils.jar;

import com.alee.utils.FileUtils;
import com.alee.utils.TextUtils;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * This class represents single JAR file structure element.
 * It might contain other nested {@link JarEntry}.
 *
 * @author Mikle Garin
 */

public class JarEntry
{
    /**
     * ID prefix.
     */
    public static final String ID_PREFIX = "JE";

    /**
     * Unique JAR entry ID.
     */
    private String id;

    /**
     * JAR entry type.
     */
    private JarEntryType type;

    /**
     * JAR entry name.
     */
    private String name;

    /**
     * Custom JAR entry icon.
     */
    private ImageIcon icon;

    /**
     * ZIP entry reference for this JAR entry.
     */
    private ZipEntry zipEntry;

    /**
     * JAR structure this entry belongs to.
     */
    private JarStructure structure;

    /**
     * Parent JAR entry if it has one.
     */
    private JarEntry parent;

    /**
     * Children JAR entries.
     */
    private List<JarEntry> children = new ArrayList<JarEntry> ();

    public JarEntry ( final JarStructure structure )
    {
        super ();
        setStructure ( structure );
        setParent ( null );
    }

    public JarEntry ( final JarStructure structure, final JarEntryType type, final String name )
    {
        super ();
        setType ( type );
        setName ( name );
        setStructure ( structure );
        setParent ( null );
    }

    public JarEntry ( final JarStructure structure, final JarEntryType type, final String name, final JarEntry parent )
    {
        super ();
        setType ( type );
        setName ( name );
        setStructure ( structure );
        setParent ( parent );
    }

    public JarEntry ( final JarStructure structure, final JarEntryType type, final String name, final JarEntry parent,
                      final List<JarEntry> children )
    {
        super ();
        setType ( type );
        setName ( name );
        setStructure ( structure );
        setParent ( parent );
        setChildren ( children );
    }

    public String getId ()
    {
        if ( id == null )
        {
            setId ();
        }
        return id;
    }

    private void setId ()
    {
        setId ( TextUtils.generateId ( ID_PREFIX ) );
    }

    public void setId ( final String id )
    {
        this.id = id;
    }

    public JarEntryType getType ()
    {
        return type;
    }

    public void setType ( final JarEntryType type )
    {
        this.type = type;
    }

    public String getName ()
    {
        return name;
    }

    public void setName ( final String name )
    {
        this.name = name;
    }

    public ZipEntry getZipEntry ()
    {
        return zipEntry;
    }

    public void setZipEntry ( final ZipEntry zipEntry )
    {
        this.zipEntry = zipEntry;
    }

    public JarStructure getStructure ()
    {
        return structure;
    }

    public void setStructure ( final JarStructure structure )
    {
        this.structure = structure;
    }

    public JarEntry getParent ()
    {
        return parent;
    }

    public void setParent ( final JarEntry parent )
    {
        this.parent = parent;
    }

    public List<JarEntry> getChildren ()
    {
        return children;
    }

    public JarEntry getChild ( final int index )
    {
        return children.get ( index );
    }

    public JarEntry getChildByName ( final String name )
    {
        for ( final JarEntry child : children )
        {
            if ( child.getName ().equals ( name ) )
            {
                return child;
            }
        }
        return null;
    }

    public void setChildren ( final List<JarEntry> children )
    {
        this.children = children;
    }

    public void addChild ( final JarEntry child )
    {
        this.children.add ( child );
    }

    public void addChild ( final int index, final JarEntry child )
    {
        this.children.add ( index, child );
    }

    public void removeChild ( final JarEntry child )
    {
        this.children.remove ( child );
    }

    public ImageIcon getIcon ()
    {
        if ( icon != null )
        {
            return icon;
        }
        else if ( type != JarEntryType.fileEntry )
        {
            return type.getIcon ();
        }
        else
        {
            final ImageIcon icon = FileUtils.getStandardFileIcon ( false, getEntryExtension (), 1f );
            if ( icon != null )
            {
                return icon;
            }
            else
            {
                return JarEntryType.fileEntry.getIcon ();
            }
        }
    }

    public void setIcon ( final ImageIcon icon )
    {
        this.icon = icon;
    }

    public String getEntryExtension ()
    {
        return FileUtils.getFileExtPart ( name, false );
    }

    public boolean isClassEntry ( final Class classType )
    {
        return classType != null && classType.getCanonicalName ().equals ( getCanonicalEntryName () );
    }

    public String getCanonicalEntryName ()
    {
        // Creating canonical name
        StringBuilder canonicalName = new StringBuilder ( getName () );
        JarEntry parent = getParent ();
        while ( parent != null && !parent.getType ().equals ( JarEntryType.jarEntry ) )
        {
            canonicalName.insert ( 0, parent.getName () + "." );
            parent = parent.getParent ();
        }

        // Removing extension from classes
        if ( type.equals ( JarEntryType.classEntry ) || type.equals ( JarEntryType.javaEntry ) )
        {
            canonicalName = new StringBuilder ( canonicalName.substring ( 0, canonicalName.lastIndexOf ( "." ) ) );
        }

        return canonicalName.toString ();
    }

    public String getCanonicalEntryPath ()
    {
        // Creating canonical path
        final StringBuilder canonicalName = new StringBuilder ( getName () );
        JarEntry parent = getParent ();
        while ( parent != null && !parent.getType ().equals ( JarEntryType.jarEntry ) &&
                !parent.getParent ().getType ().equals ( JarEntryType.jarEntry ) )
        {
            canonicalName.insert ( 0, parent.getName () + "/" );
            parent = parent.getParent ();
        }
        return canonicalName.toString ();
    }

    public List<JarEntry> getPath ()
    {
        final List<JarEntry> path = new ArrayList<JarEntry> ();
        JarEntry current = JarEntry.this;
        while ( current != null && !current.getType ().equals ( JarEntryType.jarEntry ) )
        {
            path.add ( 0, current );
            current = current.getParent ();
        }
        return path;
    }

    public InputStream getInputStream ()
    {
        try
        {
            return new ZipFile ( structure.getJarLocation () ).getInputStream ( getZipEntry () );
        }
        catch ( final IOException e )
        {
            LoggerFactory.getLogger ( JarEntry.class ).error ( e.toString (), e );
            return null;
        }
    }

    @Override
    public boolean equals ( final Object obj )
    {
        return obj != null && obj instanceof JarEntry && ( ( JarEntry ) obj ).getCanonicalEntryPath ().equals ( getCanonicalEntryPath () );
    }

    @Override
    public String toString ()
    {
        return getName () + " (" + getType () + ")";
    }
}