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

import com.alee.extended.filefilter.DefaultFileFilter;
import com.alee.laf.GlobalConstants;
import com.alee.laf.StyleConstants;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.proxy.ProxyManager;
import com.alee.utils.file.FileDescription;
import com.alee.utils.file.FileDownloadListener;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
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
 * @since 1.4
 */

public class FileUtils
{
    /**
     * Cached file system view.
     */
    private static final FileSystemView fsv = FileSystemView.getFileSystemView ();

    /**
     * Display date format.
     */
    private static SimpleDateFormat sdf = new SimpleDateFormat ( "dd MMM yyyy HH:mm" );

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
    private static Map<String, Boolean> isDriveCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isComputer" method result.
     */
    private static Map<String, Boolean> isComputerCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isCdDrive" method result.
     */
    private static Map<String, Boolean> isCdDriveCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isFile" method result.
     */
    private static Map<String, Boolean> isFileCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isDirectory" method result.
     */
    private static Map<String, Boolean> isDirectoryCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "isHidden" method result.
     */
    private static Map<String, Boolean> isHiddenCache = new HashMap<String, Boolean> ();

    /**
     * Cache for "getDisplayFileName" method result.
     */
    private static Map<String, String> displayFileNameCache = new HashMap<String, String> ();

    /**
     * Cache for "getFileDescription" method result.
     */
    private static Map<String, FileDescription> fileDescriptionCache = new HashMap<String, FileDescription> ();

    /**
     * Cache for "getFileTypeDescription" method result.
     */
    private static Map<String, String> fileTypeDescriptionCache = new HashMap<String, String> ();

    /**
     * Cache for "getDisplayFileModificationDate" method result.
     */
    private static Map<String, String> displayFileModificationDateCache = new HashMap<String, String> ();

    /**
     * File extension icons cache.
     */
    private static Map<String, ImageIcon> extensionIconsCache = new HashMap<String, ImageIcon> ();

    /**
     * Resource icons cache.
     */
    private static Map<String, ImageIcon> resourceIconsCache = new HashMap<String, ImageIcon> ();

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
        List<File> path = new ArrayList<File> ();
        while ( file != null )
        {
            path.add ( 0, file );
            file = file.getParentFile ();
        }
        return path;
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
    public static boolean grantExecutePermissions ( File file )
    {
        try
        {
            return file.setExecutable ( true, false );
        }
        catch ( Throwable e )
        {
            e.printStackTrace ();
            return false;
        }
    }

    /**
     * Returns normalized file without redundant parts in its path.
     *
     * @param file file to normalize
     * @return normalized file
     */
    public static File normalize ( File file )
    {
        try
        {
            return file != null ? file.getCanonicalFile () : file;
        }
        catch ( IOException e )
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
        File[] roots = getSystemRoots ();
        return roots.length > 0 ? roots[ 0 ] : null;
    }

    /**
     * Returns array of available file system roots.
     *
     * @return array of available file system roots
     */
    public static File[] getSystemRoots ()
    {
        File[] roots = fsv.getRoots ();
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
        File[] roots = File.listRoots ();
        int proper = 0;
        for ( File root : roots )
        {
            if ( root.isDirectory () )
            {
                proper++;
            }
        }
        File[] properRoots = new File[ proper ];
        int currentIndex = 0;
        for ( File root : roots )
        {
            if ( root.isDirectory () )
            {
                properRoots[ currentIndex ] = root;
                currentIndex++;
            }
        }
        return properRoots;
    }

    /**
     * Returns MD5 for specified file.
     *
     * @param file file to process
     * @return MD5
     */
    public static String computeMD5 ( File file )
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
    public static String computeMD5 ( File file, int bufferLength )
    {
        try
        {
            return computeMD5 ( new FileInputStream ( file ) );
        }
        catch ( FileNotFoundException e )
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
    public static String computeMD5 ( InputStream is )
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
    public static String computeMD5 ( InputStream is, int bufferLength )
    {
        BufferedInputStream bis = new BufferedInputStream ( is );
        try
        {
            MessageDigest digest = MessageDigest.getInstance ( "MD5" );
            byte[] buffer = new byte[ bufferLength ];
            int bytesRead;
            while ( ( bytesRead = bis.read ( buffer, 0, buffer.length ) ) > 0 )
            {
                digest.update ( buffer, 0, bytesRead );
            }
            byte[] md5sum = digest.digest ();
            BigInteger bigInt = new BigInteger ( 1, md5sum );
            return bigInt.toString ( 16 );
        }
        catch ( Throwable e )
        {
            return null;
        }
        finally
        {
            try
            {
                bis.close ();
            }
            catch ( Throwable e )
            {
                //
            }
        }
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
    public static boolean equals ( File file1, File file2 )
    {
        return file1 == null && file2 == null || file1 != null && file2 != null &&
                file1.getAbsolutePath ().equals ( file2.getAbsolutePath () );
    }

    /**
     * Returns whether both list of files have equal files in the same positions or not.
     *
     * @param files1 first files list to be compared
     * @param files2 second files list to be compared
     * @return true if both list of files have equal files in the same positions, false otherwise
     */
    public static boolean equals ( List<File> files1, List<File> files2 )
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
    public static boolean containtsFile ( List files, File file )
    {
        for ( Object f : files )
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
    public static boolean isFileAccepted ( File file, List<DefaultFileFilter> filters )
    {
        if ( filters == null || filters.size () == 0 )
        {
            return true;
        }
        else
        {
            for ( FileFilter fileFilter : filters )
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
    public static String getProperFileName ( String name )
    {
        StringBuilder newName = new StringBuilder ();
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
    public static boolean isIllegalFileNameChar ( char c )
    {
        boolean isIllegal = false;
        for ( char ILLEGAL_CHARACTER : ILLEGAL_CHARACTERS )
        {
            if ( c == ILLEGAL_CHARACTER )
            {
                isIllegal = true;
            }
        }
        return isIllegal;
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
    public static boolean isParent ( File parent, File child )
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
    public static String getFileNamePart ( File file )
    {
        return getFileNamePart ( file.getName () );
    }

    /**
     * Returns file name without extension.
     *
     * @param name file name to trim
     * @return file name without extension
     */
    public static String getFileNamePart ( String name )
    {
        int i = name.lastIndexOf ( "." );
        return i == -1 ? name : name.substring ( 0, i );
    }

    /**
     * Returns file extension either with or without dot.
     *
     * @param file    file to process
     * @param withDot whether return the extension with dot, or not
     * @return file extension
     */
    public static String getFileExtPart ( String file, boolean withDot )
    {
        int i = file.lastIndexOf ( "." );
        return i == -1 ? "" : withDot ? file.substring ( i ) : file.substring ( i + 1 );
    }

    /**
     * Returns shortened file name.
     *
     * @param name file name to shorten
     * @return shortened file name
     */
    public static String getShortFileName ( String name )
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
    public static String getShortFileName ( String name, int length )
    {
        if ( length < 2 )
        {
            return name;
        }
        else
        {
            String newName = getFileNamePart ( name );
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
    public static List sortFiles ( List<File> files )
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
    public static File[] sortFiles ( File[] files )
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
    public static String getAvailableName ( String dir, String name )
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
    public static String getAvailableName ( File dir, String name )
    {
        List<String> exist = new ArrayList<String> ();
        File[] files = dir.listFiles ();
        if ( files != null )
        {
            for ( File file : files )
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
    public static String getAvailableName ( List<String> exist, String name )
    {
        // If specified name doesn't exist return it
        if ( !exist.contains ( name ) )
        {
            return name;
        }

        // Gathering name parts
        int dot = name.lastIndexOf ( "." );
        String nameStart = dot != -1 ? name.substring ( 0, dot ).trim () : name;
        String nameExt = dot != -1 ? name.substring ( name.lastIndexOf ( "." ) ) : null;
        String nameReal = null;
        Integer index = null;
        int ob = nameStart.lastIndexOf ( "(" );
        int cb = nameStart.lastIndexOf ( ")" );
        if ( ob < cb && cb == nameStart.length () - 1 )
        {
            try
            {
                nameReal = nameStart.substring ( 0, ob );
                index = Integer.parseInt ( nameStart.substring ( ob + 1, cb ) );
                index++;
            }
            catch ( Throwable e )
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
    public static List<File> toFilesList ( Object... object )
    {
        List<File> files = new ArrayList<File> ( object != null ? object.length : 0 );
        for ( Object file : files )
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
    public static void deleteFiles ( Object... files )
    {
        for ( Object object : files )
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
    public static void deleteFile ( File file )
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
                for ( File child : file.listFiles () )
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
    public static void clearDirectory ( File dir )
    {
        if ( dir.exists () )
        {
            if ( dir.isDirectory () )
            {
                for ( File child : dir.listFiles () )
                {
                    deleteFile ( child );
                }
            }
        }
    }

    /**
     * Returns complete file description.
     *
     * @param file     file to process
     * @param fileSize file size
     * @return complete file description
     */
    public static FileDescription createFileDescription ( File file, String fileSize )
    {
        // File name
        String name = getDisplayFileName ( file );

        // File or image size
        String size = file.isFile () ? getDisplayFileSize ( file ) + ( fileSize != null ? " (" + fileSize + ")" : "" ) : null;

        // File type description
        String description = getFileTypeDescription ( file );

        // Modification date
        //        long modified = file.lastModified ();
        //        String date = modified != 0 ? /*"<br>" +*/ sdf.format ( new Date ( modified ) ) : null;

        return new FileDescription ( name, size, description, null/*date*/ );
    }

    /**
     * Returns file size to display.
     *
     * @param file file to process
     * @return file size to display
     */
    public static String getDisplayFileSize ( File file )
    {
        return getFileSizeString ( file.length () );
    }

    /**
     * Returns file size to display.
     *
     * @param file   file to process
     * @param digits number of digits after the dot
     * @return file size to display
     */
    public static String getDisplayFileSize ( File file, int digits )
    {
        return getFileSizeString ( file.length (), digits );
    }

    /**
     * Returns file size to display.
     *
     * @param size size of the file
     * @return file size to display
     */
    public static String getFileSizeString ( long size )
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
    public static String getFileSizeString ( long size, int digits )
    {
        DecimalFormat df = new DecimalFormat ( digits == 0 ? "#" : "#." + getDigits ( digits ) );
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
    private static String getDigits ( int digits )
    {
        StringBuilder stringBuilder = new StringBuilder ( digits );
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
    public static boolean copyDirectory ( String src, String dst )
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
    public static boolean copyDirectory ( String src, String dst, boolean stopOnFail )
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
    public static boolean copyDirectory ( File srcDir, File dstDir )
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
    public static boolean copyDirectory ( File srcDir, File dstDir, boolean stopOnFail )
    {
        // todo Actually ignore exceptions aswell if stopOnFail = false
        if ( srcDir.exists () && srcDir.isDirectory () )
        {
            // Ensure destination directory exists and perform copy
            if ( ensureDirectoryExists ( dstDir ) )
            {
                // Copying all subdirectories and subfiles
                boolean success = true;
                for ( File file : srcDir.listFiles () )
                {
                    String copied = dstDir.getAbsolutePath () + File.separator + file.getName ();
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
    public static boolean copyFile ( String src, String dst )
    {
        try
        {
            // Creating destination directory if needed
            File dstDir = new File ( new File ( dst ).getParent () );
            if ( ensureDirectoryExists ( dstDir ) )
            {
                FileChannel srcFC = new FileInputStream ( src ).getChannel ();
                FileChannel dstFC = new FileOutputStream ( dst ).getChannel ();
                return copyFile ( srcFC, dstFC );
            }
            else
            {
                return false;
            }

        }
        catch ( FileNotFoundException e )
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
    public static boolean copyFile ( File srcFile, File dstFile )
    {
        if ( srcFile.exists () && srcFile.isFile () )
        {
            try
            {
                // Creating destination directory if needed
                File dstDir = new File ( dstFile.getParent () );
                if ( ensureDirectoryExists ( dstDir ) )
                {
                    FileChannel srcFC = new FileInputStream ( srcFile ).getChannel ();
                    FileChannel dstFC = new FileOutputStream ( dstFile ).getChannel ();
                    return copyFile ( srcFC, dstFC );
                }
                else
                {
                    return false;
                }
            }
            catch ( FileNotFoundException e )
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
    public static boolean copyFile ( FileChannel srcFC, FileChannel dstFC )
    {
        try
        {
            dstFC.transferFrom ( srcFC, 0, srcFC.size () );
            srcFC.close ();
            dstFC.close ();
            return true;
        }
        catch ( IOException e )
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
    public static String readToString ( Class nearClass, String resource )
    {
        try
        {
            return readToString ( nearClass.getResourceAsStream ( resource ) );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns text content read from the file at the specified url.
     *
     * @param url text file url
     * @return text file content
     */
    public static String readToString ( URL url )
    {
        try
        {
            return readToString ( url.openConnection ().getInputStream () );
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns text content read from the input stream.
     *
     * @param inputStream text content input stream
     * @return text content
     */
    public static String readToString ( InputStream inputStream )
    {
        try
        {
            if ( inputStream != null )
            {
                return readToString ( new InputStreamReader ( inputStream, "UTF8" ) );
            }
            else
            {
                return null;
            }
        }
        catch ( Throwable e )
        {
            return null;
        }
        finally
        {
            try
            {
                inputStream.close ();
            }
            catch ( IOException e )
            {
                // Ignore this exception
            }
        }
    }

    /**
     * Returns text content read from the specified file.
     *
     * @param file file to read
     * @return text file content
     */
    public static String readToString ( File file )
    {
        try
        {
            if ( file != null && file.exists () && file.isFile () )
            {
                return readToString ( new FileReader ( file ) );
            }
            else
            {
                return null;
            }
        }
        catch ( Throwable e )
        {
            return null;
        }
    }

    /**
     * Returns text content read from the specified reader.
     *
     * @param reader text content reader
     * @return text content
     */
    public static String readToString ( Reader reader )
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
        catch ( Throwable e )
        {
            return "";
        }
        finally
        {
            try
            {
                reader.close ();
            }
            catch ( IOException e )
            {
                // Ignore this exception
            }
        }
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir path of directory to process
     * @return list of file paths
     */
    public static List<String> getSubpaths ( String dir )
    {
        return getSubpaths ( new File ( dir ) );
    }

    /**
     * Returns list of all file paths in this directory and all subdirectories.
     *
     * @param dir directory to process
     * @return list of file paths
     */
    public static List<String> getSubpaths ( File dir )
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
    public static List<String> getSubpaths ( File dir, String path )
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
    public static List<String> getSubpaths ( File dir, String path, List<String> paths )
    {
        for ( File file : dir.listFiles () )
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

    public static File downloadFile ( String url, File dstFile )
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
    public static File downloadFile ( String url, File dstFile, FileDownloadListener listener )
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
    public static File downloadFile ( String url, String dst, boolean encodeUrl, String contentType, int timeout )
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
    public static File downloadFile ( String url, String dst, boolean encodeUrl, String contentType, int timeout,
                                      FileDownloadListener listener )
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
    public static File downloadFile ( String url, File dstFile, boolean encodeUrl, String contentType, int timeout )
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
    public static File downloadFile ( String url, File dstFile, boolean encodeUrl, String contentType, int timeout,
                                      FileDownloadListener listener )
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
                String ct = uc.getContentType ();
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
            byte[] buf = new byte[ 50 * 1024 ];
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
        catch ( Throwable e )
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
    public static int getFileSize ( String url )
    {
        try
        {
            return getFileSize ( new URL ( url ) );
        }
        catch ( Throwable e )
        {
            e.printStackTrace ();
            return -1;
        }
    }

    /**
     * Returns file size, located at the specified url.
     *
     * @param url file location url
     * @return file size
     */
    public static int getFileSize ( URL url )
    {
        try
        {
            // Creating URLConnection
            URLConnection uc = ProxyManager.getURLConnection ( url );

            // todo Tihs size is limited to maximum of 2GB, should retrieve long instead
            // Retrieving file size
            return uc.getContentLength ();
        }
        catch ( Throwable e )
        {
            e.printStackTrace ();
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
    public static List<File> findFilesRecursively ( String dir, List<String> extension )
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
    public static List<File> findFilesRecursively ( File dir, List<String> extension )
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
    public static List<File> findFilesRecursively ( String dir, List<String> extension, boolean withDot )
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
    public static List<File> findFilesRecursively ( File dir, List<String> extension, boolean withDot )
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
    public static List<File> findFilesRecursively ( String dir, List<String> extension, boolean withDot, List<File> found )
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
    public static List<File> findFilesRecursively ( File dir, final List<String> extension, final boolean withDot, List<File> found )
    {
        return findFilesRecursively ( dir, new FileFilter ()
        {
            public boolean accept ( File file )
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
    public static List<File> findFilesRecursively ( String dir, FileFilter filter )
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
    public static List<File> findFilesRecursively ( File dir, FileFilter filter )
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
    public static List<File> findFilesRecursively ( String dir, FileFilter filter, List<File> found )
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
    public static List<File> findFilesRecursively ( File dir, FileFilter filter, List<File> found )
    {
        if ( found == null )
        {
            found = new ArrayList<File> ();
        }
        if ( dir.exists () && dir.isDirectory () )
        {
            for ( File file : dir.listFiles () )
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
    public static boolean ensureDirectoryExists ( String dir )
    {
        return ensureDirectoryExists ( new File ( dir ) );
    }

    /**
     * Returns true if directory exists or was successfully created during this check, false otherwise.
     *
     * @param dir directory to check
     * @return true if directory exists or was successfully created during this check, false otherwise
     */
    public static boolean ensureDirectoryExists ( File dir )
    {
        if ( dir.exists () )
        {
            return true;
        }
        else
        {
            return dir.mkdirs ();
        }
    }

    /**
     * Clears cache for "isDrive" method.
     */
    public static void clearIsDriveCache ()
    {
        isDriveCache.clear ();
    }

    /**
     * Returns whether the specified file points to system hard drive or not.
     *
     * @param file file to process
     * @return true if the specified file points to system hard drive, false otherwise
     */
    public static boolean isDrive ( File file )
    {
        String absolutePath = file.getAbsolutePath ();
        if ( isDriveCache.containsKey ( absolutePath ) )
        {
            return isDriveCache.get ( absolutePath );
        }
        else
        {
            boolean isDrive = fsv.isDrive ( file );
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
     * Returns whether the specified file points to system hard drive or not.
     *
     * @param file file to process
     * @return true if the specified file points to system hard drive, false otherwise
     */
    public static boolean isComputer ( File file )
    {
        String absolutePath = file.getAbsolutePath ();
        if ( isComputerCache.containsKey ( absolutePath ) )
        {
            return isComputerCache.get ( absolutePath );
        }
        else
        {
            boolean isComputer = fsv.isComputerNode ( file );
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
     * Returns whether the specified file points to system CD/DVD/Bluray drive or not.
     * This method bases on file type description and might not work on some systems which does not provide it.
     * This method caches its result to improve its performance when used in various renderers.
     *
     * @param file file to process
     * @return true if the specified file points to system CD, DVD or Bluray drive, false otherwise
     */
    public static boolean isCdDrive ( File file )
    {
        String absolutePath = file.getAbsolutePath ();
        if ( isCdDriveCache.containsKey ( absolutePath ) )
        {
            return isCdDriveCache.get ( absolutePath );
        }
        else
        {
            boolean isCdDrive;
            if ( file.getParent () == null )
            {
                String sysDes = getFileTypeDescription ( file );
                String description;
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
     * Returns whether the specified file is actually a file (and not a directory, disk or some system folder) or not.
     *
     * @param file file to process
     * @return true if the specified file is actually a file, false otherwise
     */
    public static boolean isFile ( File file )
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
            boolean isFile = file.isFile ();
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
     * Returns whether the specified file is directory or not.
     *
     * @param file file to process
     * @return true if the specified file is directory, false otherwise
     */
    public static boolean isDirectory ( File file )
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
            boolean isDirectory = file.isDirectory ();
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
            boolean isHidden = file.getParentFile () != null && file.isHidden ();
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
     * Returns complete file description.
     *
     * @param file     file to process
     * @param fileSize file size on disk
     * @return complete file description
     */
    public static FileDescription getFileDescription ( File file, String fileSize )
    {
        if ( fileDescriptionCache.containsKey ( file.getAbsolutePath () ) )
        {
            return fileDescriptionCache.get ( file.getAbsolutePath () );
        }
        else
        {
            FileDescription fileDescription = createFileDescription ( file, fileSize );
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
     * Returns file name to display.
     *
     * @param file file to process
     * @return file name to display
     */
    public static String getDisplayFileName ( File file )
    {
        String absolutePath = file.getAbsolutePath ();
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
     * Clears cache for "getDisplayFileModificationDate" method.
     */
    public static void clearDisplayFileModificationDateCache ()
    {
        displayFileModificationDateCache.clear ();
    }

    /**
     * Returns file modification date to display.
     *
     * @param file file to process
     * @return file modification date to display
     */
    public static String getDisplayFileModificationDate ( File file )
    {
        String absolutePath = file.getAbsolutePath ();
        if ( displayFileModificationDateCache.containsKey ( absolutePath ) )
        {
            return displayFileModificationDateCache.get ( absolutePath );
        }
        else
        {
            String date = sdf.format ( new Date ( file.lastModified () ) );
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
     * Returns file type description.
     *
     * @param file file to process
     * @return file type description
     */
    public static String getFileTypeDescription ( File file )
    {
        if ( file == null )
        {
            return "";
        }
        else if ( fileTypeDescriptionCache.containsKey ( file.getAbsolutePath () ) )
        {
            return fileTypeDescriptionCache.get ( file.getAbsolutePath () );
        }
        else
        {
            String description = fsv.getSystemTypeDescription ( file );
            fileTypeDescriptionCache.put ( file.getAbsolutePath (), description );
            return description;
        }
    }

    /**
     * Returns system file icon.
     *
     * @param file file to process
     * @return system file icon
     */
    public static ImageIcon getFileIcon ( File file )
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
    public static ImageIcon getFileIcon ( File file, boolean large )
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
    public static ImageIcon getStandartFileIcon ( File file, boolean large )
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
    public static ImageIcon getStandartFileIcon ( File file, boolean large, boolean enabled )
    {
        if ( file == null )
        {
            return null;
        }

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
        float transparency = isHidden ( file ) ? 0.5f : 1f;
        String key = getStandartFileIconCacheKey ( extension, large, transparency, enabled );

        // Retrieving icon
        if ( extensionIconsCache.containsKey ( key ) )
        {
            return extensionIconsCache.get ( key );
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
                extensionIconsCache.put ( key, icon );
            }
            else
            {
                // Cache enabled icon
                extensionIconsCache.put ( getStandartFileIconCacheKey ( extension, large, transparency, true ), icon );

                // Cache disabled icon
                icon = ImageUtils.createDisabledCopy ( icon );
                extensionIconsCache.put ( key, icon );
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
    private static String getStandartFileIconCacheKey ( String extension, boolean large, float transparency, boolean enabled )
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
    public static ImageIcon getStandartFileIcon ( boolean large, String extension, float transparency )
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
    public static ImageIcon getIconResource ( Class nearClass, String resource )
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
    public static ImageIcon getIconResource ( Class nearClass, String resource, float transparency )
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
}