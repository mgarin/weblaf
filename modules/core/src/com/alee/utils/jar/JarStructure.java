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

import com.alee.api.annotations.NotNull;
import com.alee.api.annotations.Nullable;
import com.alee.utils.FileUtils;
import com.alee.utils.ReflectUtils;
import com.alee.utils.UtilityException;
import com.alee.utils.file.FileDownloadListener;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.CodeSource;
import java.util.List;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * This class represents single JAR file structure.
 * It might be really useful to navigate through JAR
 *
 * @author Mikle Garin
 */
public class JarStructure
{
    /**
     * Location of JAR file which structure is represented by this object.
     * Since local copy of file is always required to create this structure this field always points at existing local JAR file.
     */
    @NotNull
    protected final String jarLocation;

    /**
     * Root {@link JarEntry}.
     * Represents JAR structure itself using nested {@link JarEntry}.
     */
    @NotNull
    protected final JarEntry root;

    /**
     * Constructs new {@link JarStructure}.
     *
     * @param jarClass any class within the JAR
     */
    public JarStructure ( @NotNull final Class jarClass )
    {
        this ( jarClass, null, null, null );
    }

    /**
     * Constructs new {@link JarStructure}.
     *
     * @param jarClass          any class within the JAR
     * @param allowedExtensions list of extension filters
     * @param allowedPackages   list of allowed packages
     */
    public JarStructure ( @NotNull final Class jarClass, @Nullable final List<String> allowedExtensions,
                          @Nullable final List<String> allowedPackages )
    {
        this ( jarClass, allowedExtensions, allowedPackages, null );
    }

    /**
     * Constructs new {@link JarStructure}.
     *
     * @param jarClass          any class within the JAR
     * @param allowedExtensions list of extension filters
     * @param allowedPackages   list of allowed packages
     * @param listener          {@link FileDownloadListener} for JAR file
     */
    public JarStructure ( @NotNull final Class jarClass, @Nullable final List<String> allowedExtensions,
                          @Nullable final List<String> allowedPackages, @Nullable final FileDownloadListener listener )
    {
        try
        {
            final CodeSource src = jarClass.getProtectionDomain ().getCodeSource ();
            if ( src != null )
            {
                // Source url
                final URL jarUrl = src.getLocation ();
                final URI uri = jarUrl.toURI ();

                // Source file
                final File jarFile;
                final String scheme = uri.getScheme ();
                if ( scheme != null && scheme.equalsIgnoreCase ( "file" ) )
                {
                    // Local jar-file
                    jarFile = new File ( uri );
                }
                else
                {
                    // Remote jar-file
                    jarFile = FileUtils.downloadFile (
                            jarUrl.toString (),
                            File.createTempFile ( jarUrl.getFile (), ".tmp" ),
                            listener
                    );
                }

                // Creating JAR structure
                this.jarLocation = jarFile.getAbsolutePath ();

                // Updating root element
                this.root = new JarEntry ( this, JarEntryType.JAR, jarFile.getName () );

                // Reading all entries and parsing them into structure
                final ZipInputStream zip = new ZipInputStream ( jarUrl.openStream () );
                ZipEntry zipEntry;
                while ( ( zipEntry = zip.getNextEntry () ) != null )
                {
                    final String entryName = zipEntry.getName ();
                    if ( isAllowedPackage ( entryName, allowedPackages ) &&
                            ( zipEntry.isDirectory () || isAllowedExtension ( entryName, allowedExtensions ) ) )
                    {
                        final String[] path = entryName.split ( "/" );
                        JarEntry currentLevel = this.root;
                        for ( int i = 0; i < path.length; i++ )
                        {
                            if ( i < path.length - 1 )
                            {
                                JarEntry child = currentLevel.findChildByName ( path[ i ] );
                                if ( child == null )
                                {
                                    child = new JarEntry ( this, currentLevel, zipEntry, JarEntryType.PACKAGE, path[ i ] );
                                    currentLevel.addChild ( child );
                                }
                                currentLevel = child;
                            }
                            else
                            {
                                final JarEntryType type;
                                final String ext = FileUtils.getFileExtPart ( path[ i ], false );
                                if ( ext.equals ( "java" ) )
                                {
                                    type = JarEntryType.JAVA;
                                }
                                else if ( ext.equals ( "class" ) )
                                {
                                    type = JarEntryType.CLASS;
                                }
                                else if ( !ext.isEmpty () )
                                {
                                    type = JarEntryType.FILE;
                                }
                                else
                                {
                                    type = JarEntryType.PACKAGE;
                                }
                                currentLevel.addChild ( new JarEntry ( this, currentLevel, zipEntry, type, path[ i ] ) );
                            }
                        }
                    }
                }
                zip.close ();
            }
            else
            {
                throw new UtilityException ( "Unable to retrieve JAR file location" );
            }
        }
        catch ( final URISyntaxException e )
        {
            throw new UtilityException ( "Unable to retrieve JAR URI", e );
        }
        catch ( final IOException e )
        {
            throw new UtilityException ( "Unable to retrieve JAR file", e );
        }
    }

    /**
     * Returns whether JAR entry with the specified name is allowed by the packages list or not.
     *
     * @param entryName       JAR entry name
     * @param allowedPackages list of allowed packages
     * @return true if JAR entry with the specified name is allowed by the packages list, false otherwise
     */
    private boolean isAllowedPackage ( @NotNull final String entryName, @Nullable final List<String> allowedPackages )
    {
        boolean allowed;
        if ( allowedPackages != null && allowedPackages.size () != 0 )
        {
            allowed = false;
            for ( final String packageStart : allowedPackages )
            {
                if ( entryName.startsWith ( packageStart ) )
                {
                    allowed = true;
                    break;
                }
            }
        }
        else
        {
            allowed = true;
        }
        return allowed;
    }

    /**
     * Returns whether JAR entry with the specified name is allowed by the extensions list or not.
     *
     * @param entryName         JAR entry name
     * @param allowedExtensions list of allowed extensions
     * @return true if JAR entry with the specified name is allowed by the extensions list, false otherwise
     */
    private boolean isAllowedExtension ( @NotNull final String entryName, @Nullable final List<String> allowedExtensions )
    {
        return allowedExtensions == null || allowedExtensions.size () == 0 || allowedExtensions.contains (
                FileUtils.getFileExtPart ( entryName, true ).toLowerCase ( Locale.ROOT )
        );
    }

    /**
     * Returns JAR file location.
     *
     * @return JAR file location
     */
    @NotNull
    public String getJarLocation ()
    {
        return jarLocation;
    }

    /**
     * Returns root {@link JarEntry} of {@link JarEntryType#JAR} representing JAR itself.
     *
     * @return root {@link JarEntry} of {@link JarEntryType#JAR} representing JAR itself
     */
    @NotNull
    public JarEntry getRoot ()
    {
        return root;
    }

    /**
     * Returns copy of children {@link JarEntry}s of the root {@link JarEntry}.
     *
     * @return copy of children {@link JarEntry}s of the root {@link JarEntry}
     */
    @NotNull
    public List<JarEntry> getChildEntries ()
    {
        return root.getChildren ();
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
        return root.getChildByName ( name );
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
        return root.getChildByName ( name, recursively );
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
        return root.findChildByName ( name );
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
        return root.findChildByName ( name, recursively );
    }

    /**
     * Returns {@link JarEntry} for the specified {@link Class} or {@code null} if it cannot be found.
     *
     * @param forClass {@link Class} to find {@link JarEntry} for
     * @return {@link JarEntry} for the specified {@link Class} or {@code null} if it cannot be found
     */
    @Nullable
    public JarEntry getClassEntry ( @NotNull final Class<?> forClass )
    {
        final String[] packages = ReflectUtils.getClassPackages ( forClass );
        final String classFileName = ReflectUtils.getJavaClassName ( forClass );

        int currentPackage = 0;
        JarEntry classEntry = getRoot ();
        while ( classEntry != null )
        {
            if ( currentPackage < packages.length )
            {
                classEntry = classEntry.findChildByName ( packages[ currentPackage ] );
            }
            else
            {
                classEntry = classEntry.findChildByName ( classFileName );
                break;
            }
            currentPackage++;
        }

        return classEntry;
    }

    /**
     * Returns {@link JarEntry} for the specified {@link Package} or {@code null} if it cannot be found.
     *
     * @param forPackage {@link Package} to find {@link JarEntry} for
     * @return {@link JarEntry} for the specified {@link Package} or {@code null} if it cannot be found
     */
    @Nullable
    public JarEntry getPackageEntry ( @NotNull final Package forPackage )
    {
        return getPackageEntry ( forPackage.getName () );
    }

    /**
     * Returns {@link JarEntry} for the specified {@link Package} name or {@code null} if it cannot be found.
     *
     * @param forPackage {@link Package} name to find {@link JarEntry} for
     * @return {@link JarEntry} for the specified {@link Package} name or {@code null} if it cannot be found
     */
    @Nullable
    public JarEntry getPackageEntry ( @NotNull final String forPackage )
    {
        final String[] packages = ReflectUtils.getPackages ( forPackage );
        JarEntry packageEntry = getRoot ();
        for ( final String currentPackage : packages )
        {
            if ( packageEntry != null )
            {
                packageEntry = packageEntry.findChildByName ( currentPackage );
            }
            else
            {
                break;
            }
        }
        return packageEntry;
    }

    /**
     * Sets {@link Icon} for the {@link JarEntry} representing specified {@link Class}.
     *
     * @param forClass {@link Class} to find {@link JarEntry} for
     * @param icon     {@link Icon}
     */
    public void setClassIcon ( @NotNull final Class<?> forClass, @NotNull final Icon icon )
    {
        final JarEntry classEntry = getClassEntry ( forClass );
        if ( classEntry != null )
        {
            classEntry.setIcon ( icon );
        }
    }

    /**
     * Sets {@link Icon} for the {@link JarEntry} representing specified {@link Package}.
     *
     * @param forPackage {@link Package} to find {@link JarEntry} for
     * @param icon       {@link Icon}
     */
    public void setPackageIcon ( @NotNull final Package forPackage, @NotNull final Icon icon )
    {
        setPackageIcon ( forPackage.getName (), icon );
    }

    /**
     * Sets {@link Icon} for the {@link JarEntry} representing specified {@link Package} name.
     *
     * @param forPackage {@link Package} name to find {@link JarEntry} for
     * @param icon       {@link Icon}
     */
    public void setPackageIcon ( @NotNull final String forPackage, @NotNull final Icon icon )
    {
        final JarEntry packageEntry = getPackageEntry ( forPackage );
        if ( packageEntry != null )
        {
            packageEntry.setIcon ( icon );
        }
    }
}