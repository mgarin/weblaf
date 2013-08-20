package com.alee.extended.tree;

import com.alee.utils.CollectionUtils;
import com.alee.utils.FileUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

/**
 * This enumeration represents possible file tree root types.
 * Also this enumeration provides root files for each of the listed root types.
 *
 * @author Mikle Garin
 */

public enum FileTreeRootType
{
    /**
     * Root offered by underlying operating system.
     */
    system
            {
                public List<File> getRoots ()
                {
                    return CollectionUtils.copy ( FileSystemView.getFileSystemView ().getRoots () );
                }
            },

    /**
     * System hard drives as roots.
     */
    drives
            {
                public List<File> getRoots ()
                {
                    return CollectionUtils.copy ( FileUtils.getDiskRoots () );
                }
            },

    /**
     * User home folder as root.
     */
    userHome
            {
                public List<File> getRoots ()
                {
                    return CollectionUtils.copy ( FileUtils.getUserHome () );
                }
            };

    /**
     * Returns root files for current tree root type.
     *
     * @return root files for current tree root type
     */
    public abstract List<File> getRoots ();
}