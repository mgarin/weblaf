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
import com.alee.utils.TextUtils;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;

/**
 * User: mgarin Date: 02.03.12 Time: 15:59
 */

public class JarEntry
{
    public static final String ID_PREFIX = "JE";

    public static ImageIcon jarIcon = new ImageIcon ( JarEntry.class.getResource ( "icons/jar.png" ) );
    public static ImageIcon packageIcon = new ImageIcon ( JarEntry.class.getResource ( "icons/package.png" ) );
    public static ImageIcon classIcon = new ImageIcon ( JarEntry.class.getResource ( "icons/class.png" ) );
    public static ImageIcon javaIcon = new ImageIcon ( JarEntry.class.getResource ( "icons/java.png" ) );
    public static ImageIcon fileIcon = new ImageIcon ( JarEntry.class.getResource ( "icons/file.png" ) );

    private String id;
    private JarEntryType type;
    private String name;
    private ImageIcon icon;
    private JarEntry parent;
    private ZipEntry zipEntry;
    private List<JarEntry> childs = new ArrayList<JarEntry> ();

    public JarEntry ()
    {
        super ();
        setParent ( null );
    }

    public JarEntry ( JarEntryType type, String name )
    {
        super ();
        setType ( type );
        setName ( name );
        setParent ( null );
    }

    public JarEntry ( JarEntryType type, String name, JarEntry parent )
    {
        super ();
        setType ( type );
        setName ( name );
        setParent ( parent );
    }

    public JarEntry ( JarEntryType type, String name, JarEntry parent, List<JarEntry> childs )
    {
        super ();
        setType ( type );
        setName ( name );
        setParent ( parent );
        setChilds ( childs );
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

    public void setId ( String id )
    {
        this.id = id;
    }

    public JarEntryType getType ()
    {
        return type;
    }

    public void setType ( JarEntryType type )
    {
        this.type = type;
    }

    public String getName ()
    {
        return name;
    }

    public void setName ( String name )
    {
        this.name = name;
    }

    public JarEntry getParent ()
    {
        return parent;
    }

    public void setParent ( JarEntry parent )
    {
        this.parent = parent;
    }

    public ZipEntry getZipEntry ()
    {
        return zipEntry;
    }

    public void setZipEntry ( ZipEntry zipEntry )
    {
        this.zipEntry = zipEntry;
    }

    public List<JarEntry> getChilds ()
    {
        return childs;
    }

    public JarEntry getChild ( int index )
    {
        return childs.get ( index );
    }

    public JarEntry getChildByName ( String name )
    {
        for ( JarEntry child : childs )
        {
            if ( child.getName ().equals ( name ) )
            {
                return child;
            }
        }
        return null;
    }

    public void setChilds ( List<JarEntry> childs )
    {
        this.childs = childs;
    }

    public void addChild ( JarEntry child )
    {
        this.childs.add ( child );
    }

    public void addChild ( int index, JarEntry child )
    {
        this.childs.add ( index, child );
    }

    public void removeChild ( JarEntry child )
    {
        this.childs.remove ( child );
    }

    public ImageIcon getIcon ()
    {
        if ( icon != null )
        {
            return icon;
        }
        else if ( type.equals ( JarEntryType.jarEntry ) )
        {
            return jarIcon;
        }
        else if ( type.equals ( JarEntryType.packageEntry ) )
        {
            return packageIcon;
        }
        else if ( type.equals ( JarEntryType.javaEntry ) )
        {
            return javaIcon;
        }
        else if ( type.equals ( JarEntryType.classEntry ) )
        {
            return classIcon;
        }
        else
        {
            ImageIcon icon = FileUtils.getStandartFileIcon ( false, getEntryExtension (), 1f );
            if ( icon != null )
            {
                return icon;
            }
            else
            {
                return fileIcon;
            }
        }
    }

    public void setIcon ( ImageIcon icon )
    {
        this.icon = icon;
    }

    public String getEntryExtension ()
    {
        return FileUtils.getFileExtPart ( name, false );
    }

    public boolean isClassEntry ( Class classType )
    {
        return classType != null && classType.getCanonicalName ().equals ( getCanonicalEntryName () );
    }

    public String getCanonicalEntryName ()
    {
        // Creating canonical name
        String canonicalName = getName ();
        JarEntry parent = getParent ();
        while ( parent != null && !parent.getType ().equals ( JarEntryType.jarEntry ) )
        {
            canonicalName = parent.getName () + "." + canonicalName;
            parent = parent.getParent ();
        }

        // Removing extension from classes
        if ( type.equals ( JarEntryType.classEntry ) || type.equals ( JarEntryType.javaEntry ) )
        {
            canonicalName = canonicalName.substring ( 0, canonicalName.lastIndexOf ( "." ) );
        }

        return canonicalName;
    }

    public String getCanonicalEntryPath ()
    {
        // Creating canonical path
        String canonicalName = getName ();
        JarEntry parent = getParent ();
        while ( parent != null && !parent.getType ().equals ( JarEntryType.jarEntry ) &&
                !parent.getParent ().getType ().equals ( JarEntryType.jarEntry ) )
        {
            canonicalName = parent.getName () + "/" + canonicalName;
            parent = parent.getParent ();
        }
        return canonicalName;
    }

    public List<JarEntry> getPath ()
    {
        List<JarEntry> path = new ArrayList<JarEntry> ();
        JarEntry current = JarEntry.this;
        while ( current != null && !current.getType ().equals ( JarEntryType.jarEntry ) )
        {
            path.add ( 0, current );
            current = current.getParent ();
        }
        return path;
    }

    public boolean equals ( Object obj )
    {
        return obj != null && obj instanceof JarEntry && ( ( JarEntry ) obj ).getCanonicalEntryPath ().equals ( getCanonicalEntryPath () );
    }

    public String toString ()
    {
        return getName () + " (" + getType () + ")";
    }
}