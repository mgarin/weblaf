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

package com.alee.examples.groups.list;

import com.alee.examples.WebLookAndFeelDemo;
import com.alee.examples.content.DefaultExample;
import com.alee.extended.list.FileListModel;
import com.alee.extended.list.FileListViewType;
import com.alee.extended.list.WebFileList;
import com.alee.extended.panel.GroupPanel;
import com.alee.utils.FileUtils;
import com.alee.utils.file.FileComparator;

import java.awt.*;
import java.io.File;
import java.util.Arrays;

/**
 * Files list example.
 *
 * @author Mikle Garin
 */

public class FilesListExample extends DefaultExample
{
    /**
     * Returns example title.
     *
     * @return example title
     */
    @Override
    public String getTitle ()
    {
        return "Files list";
    }

    /**
     * Returns short example description.
     *
     * @return short example description
     */
    @Override
    public String getDescription ()
    {
        return "Web-styled files list";
    }

    /**
     * Returns preview component for this example.
     *
     * @param owner demo application main frame
     * @return preview component
     */
    @Override
    public Component getPreview ( WebLookAndFeelDemo owner )
    {
        // Files list
        WebFileList webFileList = new WebFileList ();
        webFileList.setFileListViewType ( FileListViewType.icons );
        webFileList.setPreferredColumnCount ( 4 );
        webFileList.setPreferredRowCount ( 5 );

        // Custome files list model
        File[] files = FileUtils.getDiskRoots ()[ 0 ].listFiles ();
        Arrays.sort ( files, new FileComparator () );
        webFileList.setModel ( new FileListModel ( files ) );

        return new GroupPanel ( webFileList.getScrollView () );
    }
}