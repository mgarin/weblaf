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

import com.alee.api.Identifiable;
import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;
import com.alee.utils.TextUtils;
import com.alee.utils.UtilityException;

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
public class JarEntry implements Identifiable
{
    /**
     * ID prefix.
     */
    protected static final String ID_PREFIX = "JE";

    /**
     * Unique JAR entry ID.
     */
    @NotNull
    protected final String id;

    /**
     * JAR structure this entry belongs to.
     */
    @NotNull
    protected final JarStructure structure;

    /**
     * Parent JAR entry if it has one.
     */
    @Nullable
    protected final JarEntry parent;

    /**
     * ZIP entry reference for this JAR entry.
     */
    @Nullable
    protected final ZipEntry zipEntry;

    /**
     * JAR entry type.
     */
    @NotNull
    protected final JarEntryType type;

    /**
     * JAR entry name.
     */
    @NotNull
    protected String name;

    /**
     * Custom JAR entry icon.
     */
    @Nullable
    protected Icon icon;

    /**
     * Children JAR entries.
     */
    @Nullable
    protected List<JarEntry> children;

    /**
     * Constructs new {@link JarEntry}.
     *
     * @param structure {@link JarStructure}
     * @param type      {@link JarEntryType}
     * @param name      {@link JarEntry} name
     */
    public JarEntry ( @NotNull final JarStructure structure, @NotNull final JarEntryType type, @NotNull final String name )
    {
        this ( structure, null, null, type, name );
    }

    /**
     * Constructs new {@link JarEntry}.
     *
     * @param structure {@link JarStructure}
     * @param parent    parent {@link JarEntry}
     * @param zipEntry  {@link ZipEntry}
     * @param type      {@link JarEntryType}
     * @param name      {@link JarEntry} name
     */
    public JarEntry ( @NotNull final JarStructure structure, @Nullable final JarEntry parent, @Nullable final ZipEntry zipEntry,
                      @NotNull final JarEntryType type, @NotNull final String name )
    {
        this.id = TextUtils.generateId ( ID_PREFIX );
        this.structure = structure;
        this.parent = parent;
        this.zipEntry = zipEntry;
        this.children = null;
        this.type = type;
        this.name = name;
    }

    @NotNull
    @Override
    public String getId ()
    {
        return id;
    }

    /**
     * Returns parent {@link JarEntry} or {@code null} if this is a {@link JarEntryType#JAR} entry.
     *
     * @return parent {@link JarEntry} or {@code null} if this is a {@link JarEntryType#JAR} entry
     */
    @Nullable
    public JarEntry getParent ()
    {
        return parent;
    }

    /**
     * Returns {@link ZipEntry} or {@code null} if this is a {@link JarEntryType#JAR} entry.
     *
     * @return {@link ZipEntry} or {@code null} if this is a {@link JarEntryType#JAR} entry
     */
    @Nullable
    public ZipEntry getZipEntry ()
    {
        return zipEntry;
    }

    /**
     * Returns {@link JarEntryType}.
     *
     * @return {@link JarEntryType}
     */
    @NotNull
    public JarEntryType getType ()
    {
        return type;
    }

    /**
     * Returns {@link JarEntry} name.
     *
     * @return {@link JarEntry} name
     */
    @NotNull
    public String getName ()
    {
        return name;
    }

    /**
     * Sets custom {@link JarEntry} name.
     *
     * @param name custom {@link JarEntry} name
     */
    public void setName ( @NotNull final String name )
    {
        this.name = name;
    }

    /**
     * Returns {@link JarEntry} icon.
     *
     * @return {@link JarEntry} icon
     */
    @NotNull
    public Icon getIcon ()
    {
        final Icon icon;
        if ( this.icon != null )
        {
            icon = this.icon;
        }
        else if ( type != JarEntryType.FILE )
        {
            icon = type.getIcon ();
        }
        else
        {
            final Icon standard = FileUtils.getStandardFileIcon ( false, getEntryExtension (), 1f );
            icon = standard != null ? standard : JarEntryType.FILE.getIcon ();
        }
        return icon;
    }

    /**
     * Sets custom {@link JarEntry} icon.
     *
     * @param icon custom {@link JarEntry} icon
     */
    public void setIcon ( @Nullable final Icon icon )
    {
        this.icon = icon;
    }

    /**
     * Returns copy of children {@link JarEntry}s.
     *
     * @return copy of children {@link JarEntry}s
     */
    @NotNull
    public List<JarEntry> getChildren ()
    {
        return this.children != null ?
                new ArrayList<JarEntry> ( this.children ) :
                new ArrayList<JarEntry> ();
    }

    /**
     * Returns child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found.
     *
     * @param name child {@link JarEntry} name
     * @return child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found
     */
    @NotNull
    public JarEntry getChildByName ( @Nullable final String name )
    {
        return getChildByName ( name, false );
    }

    /**
     * Returns child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found.
     *
     * @param name        child {@link JarEntry} name
     * @param recursively whether should look for the child recursively in all children
     * @return child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found
     */
    @NotNull
    public JarEntry getChildByName ( @Nullable final String name, final boolean recursively )
    {
        final JarEntry child = findChildByName ( name, recursively );
        if ( child == null )
        {
            throw new UtilityException ( "Cannot find child by name: " + name );
        }
        return child;
    }

    /**
     * Returns child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found.
     *
     * @param name child {@link JarEntry} name
     * @return child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found
     */
    @Nullable
    public JarEntry findChildByName ( @Nullable final String name )
    {
        return findChildByName ( name, false );
    }

    /**
     * Returns child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found.
     *
     * @param name        child {@link JarEntry} name
     * @param recursively whether should look for the child recursively in all children
     * @return child {@link JarEntry} with the specfiied name or {@code null} if it cannot be found
     */
    @Nullable
    public JarEntry findChildByName ( @Nullable final String name, final boolean recursively )
    {
        JarEntry childByName = null;
        if ( this.children != null )
        {
            for ( final JarEntry child : this.children )
            {
                if ( child.getName ().equals ( name ) )
                {
                    childByName = child;
                    break;
                }
                else if ( recursively )
                {
                    final JarEntry otherChild = child.findChildByName ( name, true );
                    if ( otherChild != null )
                    {
                        childByName = child;
                        break;
                    }
                }
            }
        }
        return childByName;
    }

    /**
     * Adds child {@link JarEntry}.
     *
     * @param child child {@link JarEntry} to add
     */
    public void addChild ( @NotNull final JarEntry child )
    {
        if ( this.children == null )
        {
            this.children = new ArrayList<JarEntry> ();
        }
        this.children.add ( child );
        CollectionUtils.sort ( this.children, JarEntryComparator.instance () );
    }

    /**
     * Removes child {@link JarEntry}.
     *
     * @param child child {@link JarEntry} to remove
     */
    public void removeChild ( @NotNull final JarEntry child )
    {
        if ( this.children != null )
        {
            this.children.remove ( child );
        }
    }

    /**
     * Returns {@link JarEntry} file extension if it is a file, empty {@link String} othewise.
     *
     * @return {@link JarEntry} file extension if it is a file, empty {@link String} othewise
     */
    @NotNull
    public String getEntryExtension ()
    {
        return FileUtils.getFileExtPart ( name, false );
    }

    /**
     * Returns whether or not this {@link JarEntry} represents specified {@link Class}.
     *
     * @param classType {@link Class}
     * @return {@code true} if this {@link JarEntry} represents specified {@link Class}, {@code false} otherwise
     */
    public boolean isClassEntry ( @Nullable final Class classType )
    {
        return classType != null && classType.getCanonicalName ().equals ( getCanonicalEntryName () );
    }

    /**
     * Returns {@link JarEntry} canonical name.
     *
     * @return {@link JarEntry} canonical name
     */
    @NotNull
    public String getCanonicalEntryName ()
    {
        // Creating canonical name
        StringBuilder canonicalName = new StringBuilder ( getName () );
        JarEntry parent = getParent ();
        while ( parent != null && !parent.getType ().equals ( JarEntryType.JAR ) )
        {
            canonicalName.insert ( 0, parent.getName () + "." );
            parent = parent.getParent ();
        }

        // Removing extension from classes
        if ( type.equals ( JarEntryType.CLASS ) || type.equals ( JarEntryType.JAVA ) )
        {
            canonicalName = new StringBuilder ( canonicalName.substring ( 0, canonicalName.lastIndexOf ( "." ) ) );
        }

        return canonicalName.toString ();
    }

    /**
     * Returns {@link JarEntry} canonical path.
     *
     * @return {@link JarEntry} canonical path
     */
    @NotNull
    public String getCanonicalEntryPath ()
    {
        final StringBuilder canonicalName = new StringBuilder ( getName () );
        JarEntry parent = getParent ();
        while ( parent != null && !parent.getType ().equals ( JarEntryType.JAR ) &&
                parent.getParent () != null && !parent.getParent ().getType ().equals ( JarEntryType.JAR ) )
        {
            canonicalName.insert ( 0, parent.getName () + "/" );
            parent = parent.getParent ();
        }
        return canonicalName.toString ();
    }

    /**
     * Returns {@link List} of {@link JarEntry}s representing this {@link JarEntry} path to root {@link JarEntry}.
     * Root will always be at {@code 0} index, this element will always be at the last index.
     *
     * @return {@link List} of {@link JarEntry}s representing this {@link JarEntry} path to root {@link JarEntry}
     */
    @NotNull
    public List<JarEntry> getPath ()
    {
        final List<JarEntry> path = new ArrayList<JarEntry> ();
        JarEntry current = JarEntry.this;
        while ( current != null && !current.getType ().equals ( JarEntryType.JAR ) )
        {
            path.add ( 0, current );
            current = current.getParent ();
        }
        return path;
    }

    /**
     * Returns {@link JarEntry} content {@link InputStream}.
     *
     * @return {@link JarEntry} content {@link InputStream}
     */
    @NotNull
    public InputStream getInputStream ()
    {
        final ZipEntry zipEntry = getZipEntry ();
        if ( zipEntry != null )
        {
            try
            {
                return new ZipFile ( structure.getJarLocation () ).getInputStream ( zipEntry );
            }
            catch ( final IOException e )
            {
                throw new UtilityException ( "Unable to open InputStream for JarEntry: " + getName (), e );
            }
        }
        else
        {
            throw new UtilityException ( "JarEntry of JAR type cannot be read" );
        }
    }

    @Override
    public boolean equals ( @Nullable final Object other )
    {
        return other instanceof JarEntry && ( ( JarEntry ) other ).getCanonicalEntryPath ().equals ( getCanonicalEntryPath () );
    }

    @NotNull
    @Override
    public String toString ()
    {
        return getName () + " (" + getType () + ")";
    }
}