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

package com.alee.utils;

import com.alee.global.GlobalConstants;
import com.alee.global.StyleConstants;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.log.Log;
import com.alee.managers.proxy.ProxyManager;
import com.alee.utils.compare.Filter;
import com.alee.utils.file.FileDescription;
import com.alee.utils.file.FileDownloadListener;
import com.alee.utils.file.SystemFileListener;
import com.alee.utils.filefilter.AbstractFileFilter;
import com.alee.utils.filefilter.CustomFileFilter;
import com.alee.utils.swing.WebTimer;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.JTextComponent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * This class provides a set of utilities to work with files, file names and their extensions.
 * <p/>
 * Note that methods which request information about files from the system has their own caches to improve performance.
 * If you will need to clear that cache simply call the corresponding clearCache method, for example:
 * For method "isHidden" you will need to call "clearIsHiddenCache" and all cached values will be resetted.
 *
 * @author Mikle Garin
 */
public final class FileUtils
{
    /**
     * todo 1. File.exists doesn't work in JDK7? Probably should add workaround for that?
     */

    /**
     * Icons.
     */
    private static final ImageIcon COMPUTER_ICON = new ImageIcon ( FileUtils.class.getResource ( "icons/computer.png" ) );

    /**
     * Cached file system view.
     */
    private static final FileSystemView fsv = FileSystemView.getFileSystemView ();

    /**
     * Display date format.
     */
    private static final SimpleDateFormat sdf = new SimpleDateFormat ( "dd MMM yyyy HH:mm" );

    /**
     * Default encoding used to read files.
     */
    private static final String defaultEncoding = "UTF-8";

    /**
     * Buffer size for MD5 calculations.
     */
    private static final int MD5_BUFFER_LENGTH = 102400;

    /**
     * Buffer size for text reader.
     */
    private static final int TEXT_BUFFER_SIZE = 65536;

    /**
     * Number of bytes in 1 kilobyte.
     */
    public static final long KB = 1024;

    /**
     * Number of bytes in 1 megabyte.
     */
    public static final long MB = 1024 * KB;

    /**
     * Number of bytes in 1 gigabyte.
     */
    public static final long GB = 1024 * MB;

    /**
     * Number of bytes in 1 petabyte.
     */
    public static final long PB = 1024 * GB;

    /**
     * All illegal file name characters.
     */
    private static final char[] ILLEGAL_CHARACTERS =
            { '/', '\n', '\r', '\t', '\0', '\f', '\"', '`', '!', '?', '*', '\\', '<', '>', '|', ':', ';', '.', ',', '%', '$', '@', '#', '^',
                    '{', '}', '[', ']', ']' };

    /**
     * Cache for "isDrive" method result.
     */
    private static final Map<String, Boolean> isDriveCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isComputer" method result.
     */
    private static final Map<String, Boolean> isComputerCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isCdDrive" method result.
     */
    private static final Map<String, Boolean> isCdDriveCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isFile" method result.
     */
    private static final Map<String, Boolean> isFileCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isDirectory" method result.
     */
    private static final Map<String, Boolean> isDirectoryCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isHidden" method result.
     */
    private static final Map<String, Boolean> isHiddenCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "getDisplayFileName" method result.
     */
    private static final Map<String, String> displayFileNameCache = new HashMap<String, String> ();

    /**
     * Cache for "getFileDescription" method result.
     */
    private static final Map<String, FileDescription> fileDescriptionCache = new HashMap<String, FileDescription> ();

    /**
     * Cache for "getFileTypeDescription" method result.
     */
    private static final Map<String, String> fileTypeDescriptionCache = new HashMap<String, String> ();

    /**
     * Cache for "getDisplayFileCreationDate" method result.
     */
    private static final Map<String, String> displayFileCreationDateCache = new HashMap<String, String> ();

    /**
     * Cache for "getDisplayFileModificationDate" method result.
     */
    private static final Map<String, String> displayFileModificationDateCache = new HashMap<String, String> ();

    /**
     * File extension icons cache lock.
     */
    private static final Object extensionIconsCacheLock = new Object ();

    /**
     * File extension icons cache.
     */
    private static final Map<String, ImageIcon> extensionIconsCache = new HashMap<String, ImageIcon> ();

    /**
     * Resource icons cache.
     */
    private static final Map<String, ImageIcon> resourceIconsCache = new HashMap<String, ImageIcon> ();

    /**
     * Default file tracking updates delay.
     */
    private static final int FILE_TRACKING_DELAY = 5000;

    /**
     * Clears all caches for specified files.
     *
     * @param files files to process
     */
    public static void clearFilesCaches ( final File... files )
    {
        if ( files != null && files.length > 0 )
        {
            for ( final File file : files )
            {
                clearFileCaches ( file );
            }
        }
    }

    /**
     * Clears all caches for specified file.
     *
     * @param file file to process
     */
    public static void clearFileCaches ( final File file )
    {
        if ( file != null )
        {
            clearFileCaches ( file.getAbsolutePath () );
        }
    }

    /**
     * Clears all caches for file under the specified path.
     *
     * @param path file path
     */
    public static void clearFileCaches ( final String path )
    {
        clearDisplayFileNameCache ( path );
        clearIsHiddenCache ( path );
        clearIsFileCache ( path );
        clearIsDirectoryCache ( path );
        clearIsComputerCache ( path );
        clearIsDriveCache ( path );
        clearIsCdDriveCache ( path );
        clearFileDescriptionCache ( path );
        clearFileTypeDescriptionCache ( path );
        clearDisplayFileCreationDateCache ( path );
        clearDisplayFileModificationDateCache ( path );
    }

    /**
     * Returns list of files contained in path of the specified file.
     * <p/>
     * For example if you have some file that points to some local file:
     * "C:\folder\file.txt"
     * You will get this list of files:
     * "C:\", "C:\folder\", "C:\folder\file.txt"
     *
     * @param file file to process
     * @return list of files contained in path of the specified file
     */
    public static List<File> getFilePath ( File file )
    {
        final List<File> path = new ArrayList<File> ();
        while ( file != null )
        {
            path.add ( 0, file );
            file = file.getParentFile ();
        }
        return path;
    }

    /**
     * Returns file path relative to specified folder or canonical path if file is not inside that folder.
     *
     * @param file   file to get relative path to
     * @param folder one of file's parent folders
     * @return file path relative to specified folder or canonical path if file is not inside that folder
     */
    public static String getRelativePath ( final File file, final File folder )
    {
        return folder.toURI ().relativize ( file.toURI () ).getPath ();
    }

    /**
     * Returns whether specified file's name can be edited.
     *
     * @param file file to edit
     * @return true if specified file's name can be edited, false otherwise
     */
    public static boolean isNameEditable ( final File file )
    {
        return file.getParentFile () != null && file.canWrite () && file.getParentFile ().canWrite ();
    }

    /**
     * Sets file name as text and selects its name part in any text component.
     *
     * @param editor text editor to process
     * @param file   file to process
     */
    public static void displayFileName ( final JTextComponent editor, final File file )
    {
        final String name = file.getName ();
        editor.setText ( name );
        editor.setSelectionStart ( 0 );
        editor.setSelectionEnd ( file.isDirectory () ? name.length () : FileUtils.getFileNamePart ( name ).length () );
    }

    /**
     * Returns system directory for temporary files.
     *
     * @return system directory for temporary files
     */
    public static File getSystemTemp ()
    {
        return new File ( getSystemTempPath () );
    }

    /**
     * Returns path to system directory for temporary files.
     *
     * @return path to system directory for temporary files
     */
    public static String getSystemTempPath ()
    {
        return System.getProperty ( "java.io.tmpdir" );
    }

    /**
     * Grants file execution permission for all users for specified file.
     *
     * @param file file for permissions change
     * @return true if permissions change operation succeed, false otherwise
     */
    public static boolean grantExecutePermissions ( final File file )
    {
        try
        {
            return file.setExecutable ( true, false );
        }
        catch ( final Throwable e )
        {
            Log.error ( FileUtils.class, e );
            return false;
        }
    }

    /**
     * Returns normalized file without redundant parts in its path.
     *
     * @param file file to normalize
     * @return normalized file
     */
    public static File normalize ( final File file )
    {
        try
        {
            return file != null ? file.getCanonicalFile () : file;
        }
        catch ( final IOException e )
        {
            return file;
        }
    }

    /**
     * Returns first available file system root.
     *
     * @return first available file system root
     */
    public static File getSystemRoot ()
    {
        final File[] roots = getSystemRoots ();
        return roots.length > 0 ? roots[ 0 ] : null;
    }

    /**
     * Returns array of available file system roots.
     *
     * @return array of available file system roots
     */
    public static File[] getSystemRoots ()
    {
        final File[] roots = fsv.getRoots ();
        if ( roots != null && roots.length > 0 )
        {
            return roots;
        }
        else
        {
            return getDiskRoots ();
        }
    }

    /**
     * Returns array of available system disks.
     *
     * @return array of available system disks
     */
    public static File[] getDiskRoots ()
    {
        final File[] roots = File.listRoots ();
        int proper = 0;
        for ( final File root : roots )
        {
            if ( isDirectory ( root ) )
            {
                proper++;
            }
        }
        final File[] properRoots = new File[ proper ];
        int currentIndex = 0;
        for ( final File root : roots )
        {
            if ( isDirectory ( root ) )
            {
                properRoots[ currentIndex ] = root;
                currentIndex++;
            }
        }
        return properRoots;
    }

    /**
     * Returns directory files array or empty array (instead of null) if no files present.
     *
     * @param directory  directory to look into
     * @param fileFilter file filter
     * @return directory files array or empty array (instead of null) if no files present
     */
    public static File[] listFiles ( final File directory, final Filter<File> fileFilter )
    {
        return listFiles ( directory, new FileFilter ()
        {
            @Override
            public boolean accept ( final File file )
            {
                return fileFilter.accept ( file );
            }
        } );
    }

    /**
     * Returns directory files array or empty array (instead of null) if no files present.
     *
     * @param directory  directory to look into
     * @param fileFilter file filter
     * @return directory files array or empty array (instead of null) if no files present
     */
    public static File[] listFiles ( final File directory, final FileFilter fileFilter )
    {
        final File[] files = directory.listFiles ( fileFilter );
        return files != null ? files : new File[ 0 ];
    }

    /**
     * Returns MD5 for specified file.
     *
     * @param file file to process
     * @return MD5
     */
    public static String computeMD5 ( final File file )
    {
        return computeMD5 ( file, MD5_BUFFER_LENGTH );
    }

    /**
     * Returns MD5 for specified file and using a buffer of specified length.
     *
     * @param file         file to process
     * @param bufferLength buffer length
     * @return MD5
     */
    public static String computeMD5 ( final File file, final int bufferLength )
    {
        try
        {
            return computeMD5 ( new FileInputStream ( file ), bufferLength );
        }
        catch ( final FileNotFoundException e )
        {
            return null;
        }
    }

    /**
     * Returns MD5 using the specified data stream.
     *
     * @param is data stream to process
     * @return MD5
     */
    public static String computeMD5 ( final InputStream is )
    {
        return computeMD5 ( is, MD5_BUFFER_LENGTH );
    }

    /**
     * Returns MD5 using the specified data stream and a buffer of specified length.
     *
     * @param is           data stream to process
     * @param bufferLength buffer length
     * @return MD5
     */
    public static String computeMD5 ( final InputStream is, final int bufferLength )
    {
        final BufferedInputStream bis = new BufferedInputStream ( is );
        try
        {
            final MessageDigest digest = MessageDigest.getInstance ( "MD5" );
            final byte[] buffer = new byte[ bufferLength ];
            int bytesRead;
            while ( ( bytesRead = bis.read ( buffer, 0, buffer.length ) ) > 0 )
            {
                digest.update ( buffer, 0, bytesRead );
            }
            final byte[] md5sum = digest.digest ();
            final BigInteger bigInt = new BigInteger ( 1, md5sum );
            return bigInt.toString ( 16 );
        }
        catch ( final Throwable e )
        {
            return null;
        }
        finally
        {
            try
            {
                bis.close ();
            }
            catch ( final Throwable e )
            {
                //
            }
        }
    }

    /**
     * Returns application working directory.
     *
     * @return application working directory
     */
    public static File getWorkingDirectory ()
    {
        return new File ( getWorkingDirectoryPath () );
    }

    /**
     * Returns application working directory.
     *
     * @return application working directory
     */
    public static String getWorkingDirectoryPath ()
    {
        return System.getProperty ( "user.dir" );
    }

    /**
     * Returns user home directory.
     *
     * @return user home directory
     */
    public static File getUserHome ()
    {
        return new File ( getUserHomePath () );
    }

    /**
     * Returns path to user home directory.
     *
     * @return path to user home directory
     */
    public static String getUserHomePath ()
    {
        String home = System.getProperty ( "user.home" );
        if ( !home.endsWith ( File.separator ) )
        {
            home += File.separator;
        }
        return home;
    }

    /**
     * Returns whether both files represent the same path in file system or not.
     *
     * @param file1 first file to be compared
     * @param file2 second file to be compared
     * @return true if both files represent the same path in file system, false otherwise
     */
    public static boolean equals ( final File file1, final File file2 )
    {
        if ( file1 == null && file2 == null )
        {
            return true;
        }
        else
        {
            final boolean notNull = file1 != null && file2 != null;
            try
            {
                return notNull && file1.getCanonicalPath ().equals ( file2.getCanonicalPath () );
            }
            catch ( final IOException e )
            {
                return notNull && file1.getAbsolutePath ().equals ( file2.getAbsolutePath () );
            }
        }
    }

    /**
     * Returns whether both list of files have equal files in the same positions or not.
     *
     * @param files1 first files list to be compared
     * @param files2 second files list to be compared
     * @return true if both list of files have equal files in the same positions, false otherwise
     */
    public static boolean equals ( final List<File> files1, final List<File> files2 )
    {
        if ( files1.size () != files2.size () )
        {
            return false;
        }
        else if ( files1.size () == files2.size () && files2.size () == 0 )
        {
            return true;
        }
        else
        {
            for ( int i = 0; i < files1.size (); i++ )
            {
                if ( !files1.get ( i ).getAbsolutePath ().equals ( files2.get ( i ).getAbsolutePath () ) )
                {
                    return false;
                }
            }
            return true;
        }
    }

    /**
     * Returns whether list of files or file paths contains the specified file or not.
     *
     * @param files list of files to search in
     * @param file  file to look for
     * @return true if list of files or file paths contains the specified file, false otherwise
     */
    public static boolean containtsFile ( final List files, final File file )
    {
        for ( final Object f : files )
        {
            if ( f instanceof File )
            {
                if ( ( ( File ) f ).getAbsolutePath ().equals ( file.getAbsolutePath () ) )
                {
                    return true;
                }
            }
            else if ( f instanceof String )
            {
                if ( f.equals ( file.getAbsolutePath () ) )
                {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns whether any of the specified file filters accept the file or not.
     *
     * @param file    file to process
     * @param filters file filters list
     * @return true if any of the specified file filters accept the file, false otherwise
     */
    public static boolean isFileAccepted ( final File file, final List<AbstractFileFilter> filters )
    {
        if ( filters == null || filters.size () == 0 )
        {
            return true;
        }
        else
        {
            for ( final FileFilter fileFilter : filters )
            {
                if ( fileFilter.accept ( file ) )
                {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * Returns a valid for any file system file name based on specified name.
     *
     * @param name name to process
     * @return file name valid for any file system
     */
    public static String getProperFileName ( final String name )
    {
        final StringBuilder newName = new StringBuilder ();
        for ( int i = 0; i < name.length (); ++i )
        {
            if ( !isIllegalFileNameChar ( name.charAt ( i ) ) )
            {
                newName.append ( name.charAt ( i ) );
            }
        }
        return newName.toString ().replaceAll ( " ", "_" );
    }

    /**
     * Returns whether the specified character is illegal or not.
     *
     * @param c character to process
     * @return true the specified character is illegal, false otherwise
     */
    public static boolean isIllegalFileNameChar ( final char c )
    {
        boolean isIllegal = false;
        for ( final char ILLEGAL_CHARACTER : ILLEGAL_CHARACTERS )
        {
            if ( c == ILLEGAL_CHARACTER )
            {
                isIllegal = true;
            }
        }
        return isIllegal;
    }

    /**
     * Returns file canonical path if its possible or absolute path otherwise.
     *
     * @param file file to process
     * @return file canonical path if its possible or absolute path otherwise
     */
    public static String canonicalPath ( final File file )
    {
        try
        {
            return file.getCanonicalPath ();
        }
        catch ( final IOException e )
        {
            return file.getAbsolutePath ();
        }
    }

    /**
     * Returns top not-null parent for the specified file.
     * If file has no parents at all simply returns null.
     *
     * @param file file to process
     * @return top not-null parent for the specified file or null if it has no parent files
     */
    public static File getTopParent ( File file )
    {
        file = file.getAbsoluteFile ();
        File parent = file.getParentFile ();
        if ( parent == null )
        {
            return null;
        }
        while ( parent.getParentFile () != null )
        {
            parent = parent.getParentFile ();
        }
        return parent;
    }

    /**
     * Returns whether the specified child file is one of parent file childs or not.
     *
     * @param parent parent file
     * @param child  child file
     * @return true if the specified child file is one of parent file childs, false otherwise
     */
    public static boolean isParent ( final File parent, File child )
    {
        if ( child == parent )
        {
            return false;
        }
        if ( child == null )
        {
            return false;
        }
        if ( parent == null )
        {
            return true;
        }
        if ( child.equals ( parent ) )
        {
            return false;
        }
        child = child.getAbsoluteFile ();
        File cp = child.getParentFile ();
        while ( cp != null )
        {
            if ( cp.equals ( parent ) )
            {
                return true;
            }
            cp = cp.getParentFile ();
        }
        return false;
    }

    /**
     * Returns file name without extension.
     *
     * @param file file to process
     * @return file name without extension
     */
    public static String getFileNamePart ( final File file )
    {
        return file != null ? getFileNamePart ( file.getName () ) : "";
    }

    /**
     * Returns file name without extension.
     *
     * @param name file name to trim
     * @return file name without extension
     */
    public static String getFileNamePart ( final String name )
    {
        if ( !TextUtils.isEmpty ( name ) )
        {
            final int i = name.lastIndexOf ( "." );
            return i == -1 ? name : name.substring ( 0, i );
        }
        else
        {
            return "";
        }
    }

    /**
     * Returns file extension either with or without dot.
     *
     * @param file    file to process
     * @param withDot whether return the extension with dot, or not
     * @return file extension
     */
    public static String getFileExtPart ( final File file, final boolean withDot )
    {
        return file != null ? getFileExtPart ( file.getName (), withDot ) : "";
    }

    /**
     * Returns file extension either with or without dot.
     *
     * @param name    file name to process
     * @param withDot whether return the extension with dot, or not
     * @return file extension
     */
    public static String getFileExtPart ( final String name, final boolean withDot )
    {
        if ( !TextUtils.isEmpty ( name ) )
        {
            final int i = name.lastIndexOf ( "." );
            return i == -1 ? "" : withDot ? name.substring ( i ) : name.substring ( i + 1 );
        }
        else
        {
            return "";
        }
    }

    /**
     * Returns shortened file name.
     *
     * @param name file name to shorten
     * @return shortened file name
     */
    public static String getShortFileName ( final String name )
    {
        return getShortFileName ( name, 30 );
    }

    /**
     * Returns shortened to the specified length file name.
     * Note that file extension length is not counted.
     *
     * @param name   file name to shorten
     * @param length maximum allowed file name length
     * @return shortened file name
     */
    public static String getShortFileName ( final String name, final int length )
    {
        if ( length < 2 )
        {
            return name;
        }
        else
        {
            final String newName = getFileNamePart ( name );
            if ( newName.length () > length )
            {
                // 2 symbols taken by dots
                return newName.substring ( 0, length - 2 ) + "..." + getFileExtPart ( name, false );
            }
            else
            {
                return name;
            }
        }
    }

    /**
     * Returns sorted list of files.
     *
     * @param files list of files to sort
     * @return sorted list of files
     */
    public static List sortFiles ( final List<File> files )
    {
        if ( files != null )
        {
            Collections.sort ( files, GlobalConstants.FILE_COMPARATOR );
        }
        return files;
    }

    /**
     * Returns sorted array of files.
     *
     * @param files array of files to sort
     * @return sorted array of files
     */
    public static File[] sortFiles ( final File[] files )
    {
        if ( files != null )
        {
            Arrays.sort ( files, GlobalConstants.FILE_COMPARATOR );
        }
        return files;
    }

    /**
     * Returns available file name in the specified directory and which is similar the specified name.
     *
     * @param dir  directory path to check
     * @param name file name to check
     * @return available file name
     */
    public static String getAvailableName ( final String dir, final String name )
    {
        return getAvailableName ( new File ( dir ), name );
    }

    /**
     * Returns available file name in the specified directory and which is similar the specified name.
     *
     * @param dir  directory to check
     * @param name file name to check
     * @return available file name
     */
    public static String getAvailableName ( final File dir, final String name )
    {
        final List<String> exist = new ArrayList<String> ();
        final File[] files = dir.listFiles ();
        if ( files != null )
        {
            for ( final File file : files )
            {
                exist.add ( file.getName () );
            }
        }
        return getAvailableName ( exist, name );
    }

    /**
     * Returns available file name that is not contained in the existing names list.
     *
     * @param exist list of existing file names
     * @param name  file name to check
     * @return available file name
     */
    public static String getAvailableName ( final List<String> exist, String name )
    {
        // If specified name doesn't exist return it
        if ( !exist.contains ( name ) )
        {
            return name;
        }

        // Gathering name parts
        final int dot = name.lastIndexOf ( "." );
        final String nameStart = dot != -1 ? name.substring ( 0, dot ).trim () : name;
        final String nameExt = dot != -1 ? name.substring ( name.lastIndexOf ( "." ) ) : null;
        final int ob = nameStart.lastIndexOf ( "(" );
        final int cb = nameStart.lastIndexOf ( ")" );
        String nameReal = null;
        Integer index = null;
        if ( ob < cb && cb == nameStart.length () - 1 )
        {
            try
            {
                nameReal = nameStart.substring ( 0, ob );
                index = Integer.parseInt ( nameStart.substring ( ob + 1, cb ) );
                index++;
            }
            catch ( final Throwable e )
            {
                //
            }
        }

        // Choosing new name
        int i = 1;
        while ( exist.contains ( name ) )
        {
            if ( nameReal != null && index != null )
            {
                name = nameReal + "(" + index + ")" + nameExt;
                index++;
            }
            else
            {
                name = nameStart + " (" + i + ")" + ( nameExt != null ? nameExt : "" );
                i++;
            }
        }

        return name;
    }

    /**
     * Converts objects array into list of files.
     * Non-file type objects will be simply ignored.
     *
     * @param object file object
     * @return list of files
     */
    public static List<File> toFilesList ( final Object... object )
    {
        final List<File> files = new ArrayList<File> ( object != null ? object.length : 0 );
        for ( final Object file : files )
        {
            if ( file instanceof File )
            {
                files.add ( ( File ) file );
            }
        }
        return files;
    }

    /**
     * Deletes all specified files.
     *
     * @param files files to delete
     */
    public static void deleteFiles ( final Object... files )
    {
        for ( final Object object : files )
        {
            if ( object instanceof File )
            {
                deleteFile ( ( File ) object );
            }
        }
    }

    /**
     * Deletes all specified files.
     *
     * @param files files to delete
     */
    public static void deleteFiles ( final List files )
    {
        for ( final Object object : files )
        {
            if ( object instanceof File )
            {
                deleteFile ( ( File ) object );
            }
        }
    }

    /**
     * Deletes file or directory completely.
     * All child files and directories will be removed first in case directory is deleted.
     *
     * @param file file to delete
     */
    public static void deleteFile ( final File file )
    {
        if ( file.exists () )
        {
            if ( file.isFile () )
            {
                if ( !file.delete () )
                {
                    file.deleteOnExit ();
                }
            }
            else if ( file.isDirectory () )
            {
                for ( final File child : file.listFiles () )
                {
                    deleteFile ( child );
                }
                if ( !file.delete () )
                {
                    file.deleteOnExit ();
                }
            }
        }
    }

    /**
     * Deletes all child files and directories for specified directory.
     * Directory itself will not be deleted.
     *
     * @param dir directory to clear
     */
    public static void clearDirectory ( final File dir )
    {
        if ( dir.exists () )
        {
            if ( dir.isDirectory () )
            {
                for ( final File child : dir.listFiles () )
                {
                    deleteFile ( child );
                }
            }
        }
    }

    /**
     * Returns transformed file filter.
     *
     * @param fileFilter IO file filter
     * @return transformed file filter
     */
    public static AbstractFileFilter transformFileFilter ( final FileFilter fileFilter )
    {
        final AbstractFileFilter filter;
        if ( fileFilter instanceof AbstractFileFilter )
        {
            filter = ( AbstractFileFilter ) fileFilter;
        }
        else
        {
            filter = new CustomFileFilter ( GlobalConstants.ALL_FILES_FILTER.getIcon (),
                    LanguageManager.get ( "weblaf.file.filter.custom" ) )
            {
                @Override
                public boolean accept ( final File file )
                {
                    return fileFilter == null || fileFilter.accept ( file );
                }
            };
        }
        return filter;
    }

    /**
     * Returns transformed file filter.
     *
     * @param fileFilter Swing file filter
     * @return transformed file filter.
     */
    public static AbstractFileFilter transformFileFilter ( final javax.swing.filechooser.FileFilter fileFilter )
    {
        final AbstractFileFilter filter;
        if ( fileFilter instanceof AbstractFileFilter )
        {
            filter = ( AbstractFileFilter ) fileFilter;
        }
        else
        {
            filter = new CustomFileFilter ( GlobalConstants.ALL_FILES_FILTER.getIcon (), fileFilter.getDescription () )
            {
                @Override
                public boolean accept ( final File file )
                {
                    return fileFilter == null || fileFilter.accept ( file );
                }
            };
        }
        return filter;
    }

    /**
     * Returns filtered files list.
     *
     * @param files      files collection to filter
     * @param fileFilter file filter
     * @return filtered files list
     */
    public static List<File> filterFiles ( final Collection<File> files, final AbstractFileFilter fileFilter )
    {
        final List<File> filteredFiles = new ArrayList<File> ( files.size () );
        for ( final File file : files )
        {
            if ( fileFilter.accept ( file ) )
            {
                filteredFiles.add ( file );
            }
        }
        return filteredFiles;
    }

    /**
     * Returns complete file description.
     *
     * @param file     file to process
     * @param fileSize file size
     * @return complete file description
     */
    public static FileDescription createFileDescription ( final File file, final String fileSize )
    {
        // File name
        final String name = getDisplayFileName ( file );

        // File or image size
        final String size = file.isFile () ? getDisplayFileSize ( file ) + ( fileSize != null ? " (" + fileSize + ")" : "" ) : null;

        // File type description
        final String description = getFileTypeDescription ( file );

        // Modification date
        //        long modified = file.lastModified ();
        //        String date = modified != 0 ? /*"<br>" +*/ sdf.format ( new Date ( modified ) ) : null;

        return new FileDescription ( name, size, description, null /*date*/ );
    }

    /**
     * Returns file size to display.
     *
     * @param file file to process
     * @return file size to display
     */
    public static String getDisplayFileSize ( final File file )
    {
        // todo Cache this value
        return getFileSizeString ( file.length () );
    }

    /**
     * Returns file size to display.
     *
     * @param file   file to process
     * @param digits number of digits after the dot
     * @return file size to display
     */
    public static String getDisplayFileSize ( final File file, final int digits )
    {
        // todo Cache this value
        return getFileSizeString ( file.length (), digits );
    }

    /**
     * Returns file size to display.
     *
     * @param size size of the file
     * @return file size to display
     */
    public static String getFileSizeString ( final long size )
    {
        return getFileSizeString ( size, 2 );
    }

    /**
     * Returns file size to display.
     *
     * @param size   size of the file
     * @param digits number of digits after the dot
     * @return file size to display
     */
    public static String getFileSizeString ( final long size, final int digits )
    {
        final DecimalFormat df = new DecimalFormat ( digits == 0 ? "#" : "#." + getDigits ( digits ) );
        if ( size < KB )
        {
            return df.format ( size ) + " " + LanguageManager.get ( "weblaf.file.size.b" );
        }
        else if ( size >= KB && size < MB )
        {
            return df.format ( ( float ) size / KB ) + " " + LanguageManager.get ( "weblaf.file.size.kb" );
        }
        else if ( size >= MB && size < GB )
        {
            return df.format ( ( float ) size / MB ) + " " + LanguageManager.get ( "weblaf.file.size.mb" );
        }
        else
        {
            return df.format ( ( float ) size / GB ) + " " + LanguageManager.get ( "weblaf.file.size.gb" );
        }
    }

    /**
     * Returns pattern part for decimal format with a specified number of digits.
     *
     * @param digits number of digits
     * @return pattern part for decimal format
     */
    private static String getDigits ( final int digits )
    {
        final StringBuilder stringBuilder = new StringBuilder ( digits );
        for ( int i = 0; i < digits; i++ )
        {
            stringBuilder.append ( "#" );
        }
        return stringBuilder.toString ();
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Also ignores any exceptions that might occur during the copy process.
     *
     * @param src source directory path
     * @param dst destination directory path
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( final String src, final String dst )
    {
        return copyDirectory ( src, dst, false );
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Whether to stop copy operation if any exception occurs or not is set by stopOnFail argument.
     *
     * @param src        source directory path
     * @param dst        destination directory path
     * @param stopOnFail whether to stop copy operation if any exception occurs or not
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( final String src, final String dst, final boolean stopOnFail )
    {
        return copyDirectory ( new File ( src ), new File ( dst ), stopOnFail );
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Also ignores any exceptions that might occur during the copy process.
     *
     * @param srcDir source directory
     * @param dstDir destination directory
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( final File srcDir, final File dstDir )
    {
        return copyDirectory ( srcDir, dstDir, false );
    }

    /**
     * Copies src directory content into dst directory and returns whether operation succeed or not.
     * Whether to stop copy operation if any exception occurs or not is set by stopOnFail argument.
     *
     * @param srcDir     source directory
     * @param dstDir     destination directory
     * @param stopOnFail whether to stop copy operation if any exception occurs or not
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyDirectory ( final File srcDir, final File dstDir, final boolean stopOnFail )
    {
        // todo Actually ignore exceptions aswell if stopOnFail = false
        if ( srcDir.exists () && srcDir.isDirectory () )
        {
            // Ensure destination directory exists and perform copy
            if ( ensureDirectoryExists ( dstDir ) )
            {
                // Copying all subdirectories and subfiles
                boolean success = true;
                for ( final File file : srcDir.listFiles () )
                {
                    final String copied = dstDir.getAbsolutePath () + File.separator + file.getName ();
                    if ( file.isDirectory () )
                    {
                        success = copyDirectory ( file.getAbsolutePath (), copied ) && success;
                        if ( !success && stopOnFail )
                        {
                            return false;
                        }
                    }
                    else
                    {
                        success = copyFile ( file.getAbsolutePath (), copied ) && success;
                        if ( !success && stopOnFail )
                        {
                            return false;
                        }
                    }
                }
                return success;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Copies source file content into destination file.
     * If destination directory doesn't exist it will be created in the process.
     * If destination file doesn't exist it will be also created in the process.
     *
     * @param src source file path
     * @param dst destination file path
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyFile ( final String src, final String dst )
    {
        try
        {
            // Creating destination directory if needed
            final File dstDir = new File ( new File ( dst ).getParent () );
            if ( ensureDirectoryExists ( dstDir ) )
            {
                final FileChannel srcFC = new FileInputStream ( src ).getChannel ();
                final FileChannel dstFC = new FileOutputStream ( dst ).getChannel ();
                return copyFile ( srcFC, dstFC );
            }
            else
            {
                return false;
            }

        }
        catch ( final FileNotFoundException e )
        {
            return false;
        }
    }

    /**
     * Copies source file content into destination file.
     * If destination directory doesn't exist it will be created in the process.
     * If destination file doesn't exist it will be also created in the process.
     *
     * @param srcFile source file
     * @param dstFile destination file
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyFile ( final File srcFile, final File dstFile )
    {
        if ( srcFile.exists () && srcFile.isFile () )
        {
            try
            {
                // Creating destination directory if needed
                final File dstDir = new File ( dstFile.getParent () );
                if ( ensureDirectoryExists ( dstDir ) )
                {
                    final FileChannel srcFC = new FileInputStream ( srcFile ).getChannel ();
                    final FileChannel dstFC = new FileOutputStream ( dstFile ).getChannel ();
                    return copyFile ( srcFC, dstFC );
                }
                else
                {
                    return false;
                }
            }
            catch ( final FileNotFoundException e )
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    /**
     * Copies file data from source file channel into destination file channel.
     *
     * @param srcFC source file channel
     * @param dstFC destination file channel
     * @return true if copy operation succeed, false otherwise
     */
    public static boolean copyFile ( final FileChannel srcFC, final FileChannel dstFC )
    {
        try
        {
            dstFC.transferFrom ( srcFC, 0, srcFC.size () );
            srcFC.close ();
            dstFC.close ();
            return true;
        }
        catch ( final IOException e )
        {
            return false;
        }
    }

    /**
     * Returns text content read from the file located near specified class.
     *
     * @param nearClass class near which text file is located
     * @param resource  text file location
     * @return text file content
     */
    public static String readToString ( final Class nearClass, final String resource )
    {
        return readToString ( nearClass, resource, defaultEncoding );
    }

    /**
     * Returns content read from the file located near specified class.
     *
     * @param nearClass class near which file is located
     * @param resource  file location
     * @param encoding  file encoding
     * @return file content
     */
    public static String readToString ( final Class nearClass, final String resource, final String encoding )
    {
        try
        {
            return readToString ( nearClass.getResourceAsStream ( resource ), encoding );
        }
        catch ( final Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns content read from the file at the specified url.
     *
     * @param url file url
     * @return file content
     */
    public static String readToString ( final URL url )
    {
        return readToString ( url, defaultEncoding );
    }

    /**
     * Returns content read from the file at the specified url.
     *
     * @param url      file url
     * @param encoding file encoding
     * @return file content
     */
    public static String readToString ( final URL url, final String encoding )
    {
        try
        {
            return readToString ( url.openStream (), encoding );
        }
        catch ( final Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns content read from the specified file.
     *
     * @param file file to read
     * @return file content
     */
    public static String readToString ( final File file )
    {
        return readToString ( file, defaultEncoding );
    }

    /**
     * Returns content read from the specified file.
     *
     * @param file     file to read
     * @param encoding file encoding
     * @return file content
     */
    public static String readToString ( final File file, final String encoding )
    {
        try
        {
            if ( file != null && file.exists () && file.isFile () )
            {
                return readToString ( new FileInputStream ( file ), encoding );
            }
            else
            {
                return null;
            }
        }
        catch ( final Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns content read from the input stream.
     *
     * @param inputStream text content input stream
     * @return text content
     */
    public static String readToString ( final InputStream inputStream )
    {
        return readToString ( inputStream, defaultEncoding );
    }

    /**
     * Returns content read from the input stream.
     *
     * @param inputStream text content input stream
     * @param encoding    stream data encoding
     * @return content
     */
    public static String readToString ( final InputStream inputStream, final String encoding )
    {
        try
        {
            if ( inputStream != null )
            {
                return readToString ( new InputStreamReader ( inputStream, encoding ) );
            }
            else
            {
                return null;
            }
        }
        catch ( final Throwable e )
        {
            return null;
        }
        finally
        {
            try
            {
                inputStream.close ();
            }
            catch ( final IOException e )
            {
                // Ignore this exception
            }
        }
    }

    /**
     * Returns content read from the specified reader.
     *
     * @param reader text content reader
     * @return content
     */
    public static String readToString ( final Reader reader )
    {
        try
        {
            if ( reader == null )
            {
                return "";
            }
            int charsRead;
            final char[] buffer = new char[ TEXT_BUFFER_SIZE ];
            final StringBuilder sb = new StringBuilder ();
            while ( ( charsRead = reader.read ( buffer, 0, TEXT_BUFFER_SIZE ) ) != -1 )
            {
                sb.append ( buffer, 0, charsRead );
            }
            return sb.toString ();
        }
        catch ( final Throwable e )
        {
            return "";
        }
        finally
        {
            try
            {
                reader.close ();
            }
            catch ( final IOException e )
            {
                // Ignore this exception
            }
        }
    }

    /**
     * Writes text to the specified file overwriting any content inside the file.
     * If file or even its directory doesn't exist - they will be created.
     *
     * @param text text to write
     * @param file file to write text into
     */
    public static void writeStringToFile ( final String text, final File file )
    {
        writeStringToFile ( text, file, "UTF-8" );
    }

    /**
     * Writes text to the specified file overwriting any content inside the file.
     * If file or even its directory doesn't exist - they will be created.
     *
     * @param text text to write
     * @param file file to write text into
     */
    public static void writeStringToFile ( final String text, final File file, final String encoding )
    {
        // Throw exception if file is a directory
        if ( file.exists () && file.isDirectory () )
        {
            throw new RuntimeException ( "Specified file points to existing folder!" );
        }

        // Creating directories if necessary
        file.getParentFile ().mkdirs ();

        // Writing text to file
        PrintWriter writer = null;
        try
        {
            writer = new PrintWriter ( file, encoding );
            writer.write ( text );
        }
        catch ( final FileNotFoundException e )
        {
            Log.error ( FileUtils.class, e );
        }
        catch ( final UnsupportedEncodingException e )
        {
            Log.error ( FileUtils.class, e );
        }
        finally
        {
            if ( writer != null )
            {
                writer.close ();
            }
        }
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir path of directory to process
     * @return list of file paths
     */
    public static List<String> getSubpaths ( final String dir )
    {
        return getSubpaths ( new File ( dir ) );
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir directory to process
     * @return list of file paths
     */
    public static List<String> getSubpaths ( final File dir )
    {
        return getSubpaths ( dir, "" );
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir  directory to process
     * @param path path to current position
     * @return list of file paths
     */
    public static List<String> getSubpaths ( final File dir, final String path )
    {
        return getSubpaths ( dir, path, new ArrayList<String> () );
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir   directory to process
     * @param path  path to current position
     * @param paths list of collected paths
     * @return list of file paths
     */
    public static List<String> getSubpaths ( final File dir, final String path, final List<String> paths )
    {
        for ( final File file : dir.listFiles () )
        {
            if ( file.isFile () )
            {
                paths.add ( path + file.getName () );
            }
            else if ( file.isDirectory () )
            {
                getSubpaths ( file, path + file.getName () + File.separator, paths );
            }
        }
        return paths;
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed or null if not.
     *
     * @param url     file source url
     * @param dstFile destination file
     * @return downloaded file if download succeed, null otherwise
     */
    public static File downloadFile ( final String url, final File dstFile )
    {
        return downloadFile ( url, dstFile, false, null, GlobalConstants.SHORT_TIMEOUT, null );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed or null if not.
     * You can observe and manipulate the download process by providing a file download listener.
     *
     * @param url      file source url
     * @param dstFile  destination file
     * @param listener file download process listener
     * @return downloaded file if download succeed, null otherwise
     */
    public static File downloadFile ( final String url, final File dstFile, final FileDownloadListener listener )
    {
        return downloadFile ( url, dstFile, false, null, GlobalConstants.SHORT_TIMEOUT, listener );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed or null if not.
     *
     * @param url         file source url
     * @param dst         destination file path
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @return downloaded file if download succeed, null otherwise
     */
    public static File downloadFile ( final String url, final String dst, final boolean encodeUrl, final String contentType,
                                      final int timeout )
    {
        return downloadFile ( url, new File ( dst ), encodeUrl, contentType, timeout, null );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed or null if not.
     * You can observe and manipulate the download process by providing a file download listener.
     *
     * @param url         file source url
     * @param dst         destination file path
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @param listener    file download process listener
     * @return downloaded file if download succeed, null otherwise
     */
    public static File downloadFile ( final String url, final String dst, final boolean encodeUrl, final String contentType,
                                      final int timeout, final FileDownloadListener listener )
    {
        return downloadFile ( url, new File ( dst ), encodeUrl, contentType, timeout, listener );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed or null if not.
     *
     * @param url         file source url
     * @param dstFile     destination file
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @return downloaded file if download succeed, null otherwise
     */
    public static File downloadFile ( final String url, final File dstFile, final boolean encodeUrl, final String contentType,
                                      final int timeout )
    {
        return downloadFile ( url, dstFile, encodeUrl, contentType, timeout, null );
    }

    /**
     * Downloads file from the specified url to destination file and returns it if download succeed or null if not.
     * You can observe and manipulate the download process by providing a file download listener.
     *
     * @param url         file source url
     * @param dstFile     destination file
     * @param encodeUrl   whether encode the source url or not
     * @param contentType content type limitation
     * @param timeout     connection and read timeout
     * @param listener    file download process listener
     * @return downloaded file if download succeed, null otherwise
     */
    public static File downloadFile ( final String url, final File dstFile, final boolean encodeUrl, final String contentType,
                                      final int timeout, final FileDownloadListener listener )
    {
        try
        {
            // Creating connection
            final URL encodedUrl = new URL ( encodeUrl ? WebUtils.encodeUrl ( url ) : url );
            final URLConnection uc = ProxyManager.getURLConnection ( encodedUrl );

            // Configuring timeouts
            if ( timeout != 0 )
            {
                uc.setConnectTimeout ( timeout );
                uc.setReadTimeout ( timeout );
            }

            // Checking stop flag
            if ( listener != null && listener.shouldStopDownload () )
            {
                deleteFile ( dstFile );
                return null;
            }

            // Content type limitation
            if ( contentType != null )
            {
                final String ct = uc.getContentType ();
                if ( !ct.contains ( contentType ) )
                {
                    deleteFile ( dstFile );
                    return null;
                }
            }

            // Notifying about file size
            if ( listener != null )
            {
                listener.sizeDetermined ( uc.getContentLength () );

                // Checking stop flag
                if ( listener.shouldStopDownload () )
                {
                    deleteFile ( dstFile );
                    return null;
                }
            }

            // Opening file stream
            final InputStream in = uc.getInputStream ();
            final FileOutputStream out = new FileOutputStream ( dstFile );

            // Checking stop flag
            if ( listener != null && listener.shouldStopDownload () )
            {
                out.flush ();
                out.close ();
                in.close ();
                deleteFile ( dstFile );
                return null;
            }

            // Downloading content part by part
            final byte[] buf = new byte[ 50 * 1024 ];
            int totalBytesRead = 0;
            int bytesRead;
            while ( ( bytesRead = in.read ( buf ) ) != -1 )
            {
                if ( listener != null )
                {
                    totalBytesRead += bytesRead;
                    listener.partDownloaded ( totalBytesRead );

                    // Checking stop flag
                    if ( listener.shouldStopDownload () )
                    {
                        out.flush ();
                        out.close ();
                        in.close ();
                        deleteFile ( dstFile );
                        return null;
                    }
                }
                out.write ( buf, 0, bytesRead );
            }
            out.flush ();
            out.close ();
            in.close ();

            // Informing about completed download
            if ( listener != null )
            {
                listener.fileDownloaded ( dstFile );
            }
            return dstFile;
        }
        catch ( final Throwable e )
        {
            // Informing about failed download
            if ( listener != null )
            {
                listener.fileDownloadFailed ( e );
            }
            return null;
        }
    }

    /**
     * Returns file size, located at the specified url.
     *
     * @param url file location url
     * @return file size
     */
    public static int getFileSize ( final String url )
    {
        try
        {
            return getFileSize ( new URL ( url ) );
        }
        catch ( final Throwable e )
        {
            Log.error ( FileUtils.class, e );
            return -1;
        }
    }

    /**
     * Returns file size, located at the specified url.
     *
     * @param url file location url
     * @return file size
     */
    public static int getFileSize ( final URL url )
    {
        try
        {
            // Creating URLConnection
            final URLConnection uc = ProxyManager.getURLConnection ( url );

            // todo Tihs size is limited to maximum of 2GB, should retrieve long instead
            // Retrieving file size
            return uc.getContentLength ();
        }
        catch ( final Throwable e )
        {
            Log.error ( FileUtils.class, e );
            return -1;
        }
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       path to directory to process
     * @param extension file extensions list
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final String dir, final List<String> extension )
    {
        return findFilesRecursively ( new File ( dir ), extension );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       directory to process
     * @param extension file extensions list
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final File dir, final List<String> extension )
    {
        return findFilesRecursively ( dir, extension, true );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       path to directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final String dir, final List<String> extension, final boolean withDot )
    {
        return findFilesRecursively ( new File ( dir ), extension, withDot );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final File dir, final List<String> extension, final boolean withDot )
    {
        return findFilesRecursively ( dir, extension, withDot, null );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       path to directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @param found     list in which found files should be stored
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final String dir, final List<String> extension, final boolean withDot,
                                                    final List<File> found )
    {
        return findFilesRecursively ( new File ( dir ), extension, withDot, found );
    }

    /**
     * Returns list of files with specified extensions found in the specified directory and its subdirectories.
     *
     * @param dir       directory to process
     * @param extension file extensions list
     * @param withDot   whether extensions contain dot or not
     * @param found     list in which found files should be stored
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final File dir, final List<String> extension, final boolean withDot,
                                                    final List<File> found )
    {
        return findFilesRecursively ( dir, new FileFilter ()
        {
            @Override
            public boolean accept ( final File file )
            {
                return file.isFile () && extension.contains ( getFileExtPart ( file.getName (), withDot ) );
            }
        }, found );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    path to directory to process
     * @param filter file filter
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final String dir, final FileFilter filter )
    {
        return findFilesRecursively ( new File ( dir ), filter );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    directory to process
     * @param filter file filter
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final File dir, final FileFilter filter )
    {
        return findFilesRecursively ( dir, filter, null );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    path to directory to process
     * @param filter file filter
     * @param found  list in which found files should be stored
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final String dir, final FileFilter filter, final List<File> found )
    {
        return findFilesRecursively ( new File ( dir ), filter, found );
    }

    /**
     * Returns list of files accepted by file filter found in the specified directory and its subdirectories.
     *
     * @param dir    directory to process
     * @param filter file filter
     * @param found  list in which found files should be stored
     * @return list of found files
     */
    public static List<File> findFilesRecursively ( final File dir, final FileFilter filter, List<File> found )
    {
        if ( found == null )
        {
            found = new ArrayList<File> ();
        }
        if ( dir.exists () && dir.isDirectory () )
        {
            for ( final File file : dir.listFiles () )
            {
                if ( filter.accept ( file ) )
                {
                    found.add ( file );
                }
                if ( file.isDirectory () )
                {
                    findFilesRecursively ( file, filter, found );
                }
            }
        }
        return found;
    }

    /**
     * Returns true if directory exists or was successfully created during this check, false otherwise.
     *
     * @param dir path to directory to check
     * @return true if directory exists or was successfully created during this check, false otherwise
     */
    public static boolean ensureDirectoryExists ( final String dir )
    {
        return ensureDirectoryExists ( new File ( dir ) );
    }

    /**
     * Returns true if directory exists or was successfully created during this check, false otherwise.
     *
     * @param dir directory to check
     * @return true if directory exists or was successfully created during this check, false otherwise
     */
    public static boolean ensureDirectoryExists ( final File dir )
    {
        return dir.exists () || dir.mkdirs ();
    }

    /**
     * Clears cache for "isDrive" method.
     */
    public static void clearIsDriveCache ()
    {
        isDriveCache.clear ();
    }

    /**
     * Clears cache for "isDrive" method for specified file path.
     */
    public static void clearIsDriveCache ( final String absolutePath )
    {
        isDriveCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file points to system hard drive or not.
     *
     * @param file file to process
     * @return true if the specified file points to system hard drive, false otherwise
     */
    public static boolean isDrive ( final File file )
    {
        final String absolutePath = file.getAbsolutePath ();
        if ( isDriveCache.containsKey ( absolutePath ) )
        {
            return isDriveCache.get ( absolutePath );
        }
        else
        {
            final boolean isDrive = fsv.isDrive ( file );
            isDriveCache.put ( absolutePath, isDrive );
            return isDrive;
        }
    }

    /**
     * Clears cache for "isComputer" method.
     */
    public static void clearIsComputerCache ()
    {
        isComputerCache.clear ();
    }

    /**
     * Clears cache for "isComputer" method for specified file path.
     */
    public static void clearIsComputerCache ( final String absolutePath )
    {
        isComputerCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file points to system hard drive or not.
     *
     * @param file file to process
     * @return true if the specified file points to system hard drive, false otherwise
     */
    public static boolean isComputer ( final File file )
    {
        final String absolutePath = file.getAbsolutePath ();
        if ( isComputerCache.containsKey ( absolutePath ) )
        {
            return isComputerCache.get ( absolutePath );
        }
        else
        {
            final boolean isComputer = fsv.isComputerNode ( file );
            isComputerCache.put ( absolutePath, isComputer );
            return isComputer;
        }
    }

    /**
     * Clears cache for "isCdDrive" method.
     */
    public static void clearIsCdDriveCache ()
    {
        isCdDriveCache.clear ();
    }

    /**
     * Clears cache for "isCdDrive" method for specified file path.
     */
    public static void clearIsCdDriveCache ( final String absolutePath )
    {
        isCdDriveCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file points to system CD/DVD/Bluray drive or not.
     * This method bases on file type description and might not work on some systems which does not provide it.
     * This method caches its result to improve its performance when used in various renderers.
     *
     * @param file file to process
     * @return true if the specified file points to system CD, DVD or Bluray drive, false otherwise
     */
    public static boolean isCdDrive ( final File file )
    {
        final String absolutePath = file.getAbsolutePath ();
        if ( isCdDriveCache.containsKey ( absolutePath ) )
        {
            return isCdDriveCache.get ( absolutePath );
        }
        else
        {
            final boolean isCdDrive;
            if ( file.getParent () == null )
            {
                final String sysDes = getFileTypeDescription ( file );
                final String description;
                if ( sysDes != null )
                {
                    description = sysDes.toLowerCase ();
                }
                else
                {
                    description = file.getName ();
                }
                isCdDrive = description.contains ( "cd" ) || description.contains ( "dvd" ) ||
                        description.contains ( "blu-ray" ) || description.contains ( "bluray" );
            }
            else
            {
                isCdDrive = false;
            }
            isCdDriveCache.put ( absolutePath, isCdDrive );
            return isCdDrive;
        }
    }

    /**
     * Clears cache for "isFile" method.
     */
    public static void clearIsFileCache ()
    {
        isFileCache.clear ();
    }

    /**
     * Clears cache for "isFile" method for specified file path.
     */
    public static void clearIsFileCache ( final String absolutePath )
    {
        isFileCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file is actually a file (and not a directory, disk or some system folder) or not.
     *
     * @param file file to process
     * @return true if the specified file is actually a file, false otherwise
     */
    public static boolean isFile ( final File file )
    {
        if ( file == null )
        {
            return false;
        }
        else if ( isFileCache.containsKey ( file.getAbsolutePath () ) )
        {
            return isFileCache.get ( file.getAbsolutePath () );
        }
        else
        {
            final boolean isFile = file.isFile ();
            isFileCache.put ( file.getAbsolutePath (), isFile );
            return isFile;
        }
    }

    /**
     * Clears cache for "isDirectory" method.
     */
    public static void clearIsDirectoryCache ()
    {
        isDirectoryCache.clear ();
    }

    /**
     * Clears cache for "isDirectory" method for specified file path.
     */
    public static void clearIsDirectoryCache ( final String absolutePath )
    {
        isDirectoryCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file is directory or not.
     *
     * @param file file to process
     * @return true if the specified file is directory, false otherwise
     */
    public static boolean isDirectory ( final File file )
    {
        if ( file == null )
        {
            return false;
        }
        else if ( isDirectoryCache.containsKey ( file.getAbsolutePath () ) )
        {
            return isDirectoryCache.get ( file.getAbsolutePath () );
        }
        else
        {
            final boolean isDirectory = file.isDirectory ();
            isDirectoryCache.put ( file.getAbsolutePath (), isDirectory );
            return isDirectory;
        }
    }

    /**
     * Clears cache for "isHidden" method.
     */
    public static void clearIsHiddenCache ()
    {
        isHiddenCache.clear ();
    }

    /**
     * Clears cache for "isHidden" method for specified file path.
     */
    public static void clearIsHiddenCache ( final String absolutePath )
    {
        isHiddenCache.remove ( absolutePath );
    }

    /**
     * Returns whether the specified file is hidden or not.
     *
     * @param file file to process
     * @return true if the specified file is hidden, false otherwise
     */
    public static boolean isHidden ( File file )
    {
        if ( file == null )
        {
            return false;
        }
        else if ( isHiddenCache.containsKey ( file.getAbsolutePath () ) )
        {
            return isHiddenCache.get ( file.getAbsolutePath () );
        }
        else
        {
            file = file.getAbsoluteFile ();
            final boolean isHidden = file.getParentFile () != null && file.isHidden ();
            isHiddenCache.put ( file.getAbsolutePath (), isHidden );
            return isHidden;
        }
    }

    /**
     * Clears cache for "getFileDescription" method.
     */
    public static void clearFileDescriptionCache ()
    {
        fileDescriptionCache.clear ();
    }

    /**
     * Clears cache for "getFileDescription" method for specified file path.
     */
    public static void clearFileDescriptionCache ( final String absolutePath )
    {
        fileDescriptionCache.remove ( absolutePath );
    }

    /**
     * Returns complete file description.
     *
     * @param file     file to process
     * @param fileSize file size on disk
     * @return complete file description
     */
    public static FileDescription getFileDescription ( final File file, final String fileSize )
    {
        if ( fileDescriptionCache.containsKey ( file.getAbsolutePath () ) )
        {
            return fileDescriptionCache.get ( file.getAbsolutePath () );
        }
        else
        {
            final FileDescription fileDescription = createFileDescription ( file, fileSize );
            fileDescriptionCache.put ( file.getAbsolutePath (), fileDescription );
            return fileDescription;
        }
    }

    /**
     * Clears cache for "getDisplayFileName" method.
     */
    public static void clearDisplayFileNameCache ()
    {
        displayFileNameCache.clear ();
    }

    /**
     * Clears cache for "getDisplayFileName" method for specified file path.
     */
    public static void clearDisplayFileNameCache ( final String absolutePath )
    {
        displayFileNameCache.remove ( absolutePath );
    }

    /**
     * Returns file name to display.
     *
     * @param file file to process
     * @return file name to display
     */
    public static String getDisplayFileName ( final File file )
    {
        final String absolutePath = file.getAbsolutePath ();
        if ( displayFileNameCache.containsKey ( absolutePath ) )
        {
            return displayFileNameCache.get ( absolutePath );
        }
        else
        {
            String name = fsv.getSystemDisplayName ( file );
            if ( name == null || name.trim ().equals ( "" ) )
            {
                name = getFileTypeDescription ( file );
            }
            displayFileNameCache.put ( absolutePath, name );
            return name;
        }
    }

    /**
     * Clears cache for "getDisplayFileCreationDate" method.
     */
    public static void clearDisplayFileCreationDateCache ()
    {
        displayFileCreationDateCache.clear ();
    }

    /**
     * Clears cache for "getDisplayFileCreationDate" method for specified file path.
     */
    public static void clearDisplayFileCreationDateCache ( final String absolutePath )
    {
        displayFileCreationDateCache.remove ( absolutePath );
    }

    /**
     * Returns file modification date to display.
     *
     * @param file file to process
     * @return file modification date to display
     */
    public static String getDisplayFileCreationDate ( final File file )
    {
        final String absolutePath = file.getAbsolutePath ();
        if ( displayFileCreationDateCache.containsKey ( absolutePath ) )
        {
            return displayFileCreationDateCache.get ( absolutePath );
        }
        else
        {
            final String date = sdf.format ( new Date ( file.lastModified () ) );
            displayFileCreationDateCache.put ( absolutePath, date );
            return date;
        }
    }

    /**
     * Clears cache for "getDisplayFileModificationDate" method.
     */
    public static void clearDisplayFileModificationDateCache ()
    {
        displayFileModificationDateCache.clear ();
    }

    /**
     * Clears cache for "getDisplayFileModificationDate" method for specified file path.
     */
    public static void clearDisplayFileModificationDateCache ( final String absolutePath )
    {
        displayFileModificationDateCache.remove ( absolutePath );
    }

    /**
     * Returns file modification date to display.
     *
     * @param file file to process
     * @return file modification date to display
     */
    public static String getDisplayFileModificationDate ( final File file )
    {
        final String absolutePath = file.getAbsolutePath ();
        if ( displayFileModificationDateCache.containsKey ( absolutePath ) )
        {
            return displayFileModificationDateCache.get ( absolutePath );
        }
        else
        {
            final String date = sdf.format ( new Date ( file.lastModified () ) );
            displayFileModificationDateCache.put ( absolutePath, date );
            return date;
        }
    }

    /**
     * Clears cache for "getFileTypeDescription" method.
     */
    public static void clearFileTypeDescriptionCache ()
    {
        fileTypeDescriptionCache.clear ();
    }

    /**
     * Clears cache for "getFileTypeDescription" method for specified file path.
     */
    public static void clearFileTypeDescriptionCache ( final String absolutePath )
    {
        fileTypeDescriptionCache.remove ( absolutePath );
    }

    /**
     * Returns file type description.
     *
     * @param file file to process
     * @return file type description
     */
    public static String getFileTypeDescription ( final File file )
    {
        if ( file == null )
        {
            return "";
        }
        else
        {
            final String absolutePath = file.getAbsolutePath ();
            if ( fileTypeDescriptionCache.containsKey ( absolutePath ) )
            {
                return fileTypeDescriptionCache.get ( absolutePath );
            }
            else
            {
                final String description = fsv.getSystemTypeDescription ( file );
                fileTypeDescriptionCache.put ( absolutePath, description );
                return description;
            }
        }
    }

    /**
     * Returns default icon for "My computer" file.
     *
     * @return default icon for "My computer" file
     */
    public static ImageIcon getMyComputerIcon ()
    {
        return COMPUTER_ICON;
    }

    /**
     * Returns system file icon.
     *
     * @param file file to process
     * @return system file icon
     */
    public static ImageIcon getFileIcon ( final File file )
    {
        return getFileIcon ( file, false );
    }

    /**
     * Returns either large or small system file icon.
     *
     * @param file  file to process
     * @param large whether return large icon or not
     * @return either large or small system file icon
     */
    public static ImageIcon getFileIcon ( final File file, final boolean large )
    {
        // This way of icon retrieval is disabled due to inaccurate icon sizes on Win Vista/7/8
        //        if ( SystemUtils.isWindows () )
        //        {
        //            if ( file == null )
        //            {
        //                return null;
        //            }
        //
        //            ShellFolder sf;
        //            try
        //            {
        //                sf = ShellFolder.getShellFolder ( file );
        //            }
        //            catch ( FileNotFoundException e )
        //            {
        //                return null;
        //            }
        //
        //            Image img = sf.getIcon ( large );
        //            if ( img != null )
        //            {
        //                return new ImageIcon ( img, sf.getFolderType () );
        //            }
        //            else
        //            {
        //                return null;
        //            }
        //        }
        //        else
        //        {
        return getStandartFileIcon ( file, large );
        //        }
    }

    /**
     * Returns either large or small file icon from a standard icons set.
     *
     * @param file  file to process
     * @param large whether return large icon or not
     * @return either large or small file icon
     */
    public static ImageIcon getStandartFileIcon ( final File file, final boolean large )
    {
        return getStandartFileIcon ( file, large, true );
    }

    /**
     * Returns either large or small file icon from a standard icons set.
     *
     * @param file  file to process
     * @param large whether return large icon or not
     * @return either large or small file icon
     */
    public static ImageIcon getStandartFileIcon ( final File file, final boolean large, final boolean enabled )
    {
        if ( file == null )
        {
            return null;
        }

        // todo Properly lock operations for all cached file methods

        // Retrieving required icon extension or type
        String extension;
        if ( !isDirectory ( file ) )
        {
            extension = getFileExtPart ( file.getName (), false ).trim ().toLowerCase ();
            if ( extension.trim ().equals ( "" ) )
            {
                extension = file.getAbsolutePath ();
            }
        }
        else if ( isCdDrive ( file ) )
        {
            extension = "cd_drive";
        }
        else if ( isDrive ( file ) )
        {
            extension = "drive";
        }
        else if ( isComputer ( file ) )
        {
            extension = "computer";
        }
        else
        {
            extension = "folder";
        }

        // Constructing icon cache key
        final float transparency = isHidden ( file ) ? 0.5f : 1f;
        final String key = getStandartFileIconCacheKey ( extension, large, transparency, enabled );

        // Retrieving icon
        final boolean contains;
        synchronized ( extensionIconsCacheLock )
        {
            contains = extensionIconsCache.containsKey ( key );
        }
        if ( contains )
        {
            synchronized ( extensionIconsCacheLock )
            {
                return extensionIconsCache.get ( key );
            }
        }
        else
        {
            // Retrieving file type icon
            ImageIcon icon = getStandartFileIcon ( large, extension, transparency );
            if ( icon == null )
            {
                // Simply use unknown file icon
                icon = getStandartFileIcon ( large, "file", transparency );
            }

            // Caching the resulting icon
            if ( enabled )
            {
                // Cache enabled icon
                synchronized ( extensionIconsCacheLock )
                {
                    extensionIconsCache.put ( key, icon );
                }
            }
            else
            {
                // Cache enabled icon
                synchronized ( extensionIconsCacheLock )
                {
                    extensionIconsCache.put ( getStandartFileIconCacheKey ( extension, large, transparency, true ), icon );
                }

                // Cache disabled icon
                icon = ImageUtils.createDisabledCopy ( icon );
                synchronized ( extensionIconsCacheLock )
                {
                    extensionIconsCache.put ( key, icon );
                }
            }

            return icon;
        }
    }

    /**
     * Returns standart file icon cache key.
     *
     * @param extension    file extension or identifier
     * @param large        whether large icon used or not
     * @param transparency icon transparency
     * @param enabled      whether enabled icon or not
     * @return standart file icon cache key
     */
    private static String getStandartFileIconCacheKey ( final String extension, final boolean large, final float transparency,
                                                        final boolean enabled )
    {
        return extension + StyleConstants.SEPARATOR + large + StyleConstants.SEPARATOR + transparency + StyleConstants.SEPARATOR + enabled;
    }

    /**
     * Returns either large or small icon for the specified extension from a standard icons set.
     *
     * @param large        whether return large icon or not
     * @param extension    file extension
     * @param transparency icon transparency
     * @return either large or small icon for the specified extension
     */
    public static ImageIcon getStandartFileIcon ( final boolean large, final String extension, final float transparency )
    {
        return getIconResource ( FileUtils.class, "icons/extensions/" + ( large ? "32" : "16" ) + "/file_extension_" + extension +
                ".png", transparency );
    }

    /**
     * Returns resource icon.
     * Note that returned icon will be cached using its placement.
     *
     * @param nearClass class near which the icon is located
     * @param resource  icon location
     * @return resource icon
     */
    public static ImageIcon getIconResource ( final Class nearClass, final String resource )
    {
        return getIconResource ( nearClass, resource, 1f );
    }

    /**
     * Returns resource icon with the specified transparency.
     * Note that returned icon will be cached using its placement and transparency value.
     *
     * @param nearClass    class near which the icon is located
     * @param resource     icon location
     * @param transparency custom icon transparency
     * @return resource icon
     */
    public static ImageIcon getIconResource ( final Class nearClass, final String resource, final float transparency )
    {
        final String key = nearClass.getCanonicalName () + StyleConstants.SEPARATOR + resource + StyleConstants.SEPARATOR + transparency;
        if ( resourceIconsCache.containsKey ( key ) )
        {
            return resourceIconsCache.get ( key );
        }
        else
        {
            final URL url = nearClass.getResource ( resource );
            ImageIcon icon;
            if ( url != null )
            {
                icon = new ImageIcon ( url );
                if ( transparency < 1f )
                {
                    icon = ImageUtils.createTransparentCopy ( icon, transparency );
                }
            }
            else
            {
                icon = null;
            }
            resourceIconsCache.put ( key, icon );
            return icon;
        }
    }

    /**
     * Starts tracking file for possible changes.
     *
     * @param listener system file listener
     */
    public static WebTimer trackFile ( final File file, final SystemFileListener listener )
    {
        return trackFile ( file, listener, FILE_TRACKING_DELAY );
    }

    /**
     * Starts tracking file for possible changes.
     *
     * @param listener system file listener
     * @param delay    delay between checks for changes
     */
    public static WebTimer trackFile ( final File file, final SystemFileListener listener, final long delay )
    {
        final WebTimer tracker = new WebTimer ( "File tracker - " + file.getName (), delay, 0 );
        tracker.addActionListener ( new ActionListener ()
        {
            private Long lastModified = null;

            @Override
            public void actionPerformed ( final ActionEvent e )
            {
                if ( file.exists () )
                {
                    final long lm = file.lastModified ();
                    if ( lastModified != lm )
                    {
                        listener.modified ( file );
                        lastModified = lm;
                    }
                }
                else
                {
                    listener.unbound ( file );
                    tracker.stop ();
                }
            }
        } );
        tracker.setUseDaemonThread ( true );
        return tracker;
    }
}
