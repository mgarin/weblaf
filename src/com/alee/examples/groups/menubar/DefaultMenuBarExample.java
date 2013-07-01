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

package com.alee.examples.groups.menubar;

import com.alee.examples.content.DefaultExample;
import com.alee.laf.menu.WebMenu;
import com.alee.laf.menu.WebMenuBar;
import com.alee.laf.menu.WebMenuItem;
import com.alee.managers.hotkey.Hotkey;
import com.alee.managers.tooltip.TooltipManager;
import com.alee.managers.tooltip.TooltipWay;

/**
 * User: mgarin Date: 07.11.12 Time: 18:21
 */

public abstract class DefaultMenuBarExample extends DefaultExample
{
    public void setupMenuBar ( WebMenuBar menuBar )
    {
        menuBar.add ( new WebMenu ( "File", loadIcon ( "menubar/file.png" ) )
        {
            {
                add ( new WebMenu ( "New media file", loadIcon ( "menubar/media.png" ) )
                {
                    {
                        add ( new WebMenuItem ( "New image", loadIcon ( "menubar/file_image.png" ) )
                        {
                            {
                                setHotkey ( Hotkey.CTRL_N );
                            }
                        } );
                        add ( new WebMenuItem ( "New music", loadIcon ( "menubar/file_music.png" ) ) );
                        add ( new WebMenuItem ( "New video", loadIcon ( "menubar/file_video.png" ) ) );
                        add ( new WebMenuItem ( "New archive", loadIcon ( "menubar/file_archive.png" ) ) );
                    }
                } );
                addSeparator ();
                add ( new WebMenuItem ( "New text document", loadIcon ( "menubar/file_doc.png" ) ) );
                add ( new WebMenuItem ( "New excel table", loadIcon ( "menubar/file_xls.png" ) ) );
                add ( new WebMenuItem ( "New presentation", loadIcon ( "menubar/file_ppt.png" ) ) );
                addSeparator ();
                add ( new WebMenuItem ( "Exit", loadIcon ( "menubar/file_exit.png" ) )
                {
                    {
                        setHotkey ( Hotkey.ALT_F4 );
                    }
                } );
            }
        } );
        menuBar.add ( new WebMenu ( "Edit", loadIcon ( "menubar/edit.png" ) )
        {
            {
                add ( new WebMenuItem ( "Cut", loadIcon ( "menubar/edit_cut.png" ) )
                {
                    {
                        setHotkey ( Hotkey.CTRL_X );
                    }
                } );
                add ( new WebMenuItem ( "Copy", loadIcon ( "menubar/edit_copy.png" ) )
                {
                    {
                        setHotkey ( Hotkey.CTRL_C );
                    }
                } );
                add ( new WebMenuItem ( "Paste", loadIcon ( "menubar/edit_paste.png" ) )
                {
                    {
                        setHotkey ( Hotkey.CTRL_V );
                        setEnabled ( false );
                    }
                } );
            }
        } );
        menuBar.add ( new WebMenu ( "Menu tooltip", loadIcon ( "menubar/tooltip.png" ) )
        {
            {
                TooltipManager.setTooltip ( this, "Menu tooltip" );
                add ( new WebMenuItem ( "Menu item tooltip", loadIcon ( "menubar/tooltip.png" ) )
                {
                    {
                        TooltipManager.setTooltip ( this, "Menu item tooltip", TooltipWay.trailing );
                    }
                } );
            }
        } );
    }
}
