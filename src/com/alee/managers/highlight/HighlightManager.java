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

package com.alee.managers.highlight;

import com.alee.managers.glasspane.GlassPaneManager;
import com.alee.managers.glasspane.WebGlassPane;
import com.alee.utils.SwingUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * User: mgarin Date: 05.10.11 Time: 19:50
 * <p/>
 * This manager allows you to quickly highlight multiply visual Swing components in any window
 */

public final class HighlightManager
{
    /**
     * HighlightManager initialization
     */

    //    public static void initialize ()
    //    {
    //        FocusManager.registerGlobalFocusListener ( new GlobalFocusListener ()
    //        {
    //            public void focusChanged ( Component oldFocus, Component newFocus )
    //            {
    //                Window oldWA = SwingUtils.getWindowAncestor ( oldFocus );
    //                Window newWA = SwingUtils.getWindowAncestor ( newFocus );
    //                if ( oldFocus != null & newFocus != null && oldWA == newWA )
    //                {
    //                    WebGlassPane wgp = GlassPaneManager.getGlassPane ( oldWA );
    //                    if (wgp != null) {
    //                        wgp
    //                    }
    //                }
    //            }
    //        } );
    //    }

    /**
     * Highlights components with specified text on window
     */

    public static List<Component> highlightComponentsWithText ( String text, Component highlightBase )
    {
        List<Component> found = SwingUtils.findComponentsWithText ( text, highlightBase );
        if ( found.size () > 0 )
        {
            setHiglightedComponents ( found, highlightBase );
        }
        else
        {
            clearHighlightedComponents ( highlightBase );
        }
        return found;
    }

    /**
     * Sets highlighted component for its ancestor window
     */

    public static void setHiglightedComponent ( Component highlight )
    {
        setHiglightedComponent ( highlight, null );
    }

    public static void setHiglightedComponent ( Component highlight, Component highlightBase )
    {
        if ( highlight != null )
        {
            WebGlassPane wgp = GlassPaneManager.getGlassPane ( SwingUtils.getWindowAncestor ( highlight ) );
            if ( wgp != null )
            {
                wgp.clearHighlights ();
                wgp.setHighlightBase ( highlightBase != null ? highlightBase : wgp );
                wgp.addHighlightedComponents ( highlight );
            }
        }
    }

    /**
     * Sets highlighted components for their ancestor windows
     */

    public static void setHiglightedComponents ( List<Component> highlights )
    {
        setHiglightedComponents ( highlights, null );
    }

    public static void setHiglightedComponents ( List<Component> highlights, Component highlightBase )
    {
        if ( highlights != null && highlights.size () > 0 )
        {
            List<String> clearedIds = new ArrayList<String> ();
            for ( Component component : highlights )
            {
                WebGlassPane wgp = GlassPaneManager.getGlassPane ( SwingUtils.getWindowAncestor ( component ) );
                if ( wgp != null )
                {
                    if ( !clearedIds.contains ( wgp.getId () ) )
                    {
                        wgp.setHighlightBase ( highlightBase != null ? highlightBase : wgp );
                        wgp.clearHighlights ();
                        clearedIds.add ( wgp.getId () );
                    }
                    wgp.addHighlightedComponents ( component );
                }
            }
        }
    }

    /**
     * Removes component highlight from its ancestor window
     */

    public static void removeHigligtedComponent ( Component highlight )
    {
        if ( highlight != null )
        {
            WebGlassPane wgp = GlassPaneManager.getGlassPane ( SwingUtils.getWindowAncestor ( highlight ) );
            if ( wgp != null )
            {
                wgp.removeHighlightedComponents ( highlight );
            }
        }
    }

    /**
     * Removes components highlight from their ancestor windows
     */

    public static void removeHiglightedComponents ( List<Component> highlights )
    {
        if ( highlights != null && highlights.size () > 0 )
        {
            for ( Component component : highlights )
            {
                WebGlassPane wgp = GlassPaneManager.getGlassPane ( SwingUtils.getWindowAncestor ( component ) );
                if ( wgp != null )
                {
                    wgp.removeHighlightedComponents ( component );
                }
            }
        }
    }

    /**
     * Removes all highlights from component ancestor window
     */

    public static void clearHighlightedComponents ( Component component )
    {
        clearHighlightedComponents ( SwingUtils.getWindowAncestor ( component ) );
    }

    /**
     * Removes all highlights from window
     */

    public static void clearHighlightedComponents ( Window window )
    {
        WebGlassPane wgp = GlassPaneManager.getGlassPane ( window );
        if ( wgp != null )
        {
            wgp.clearHighlights ();
        }
    }

    /**
     * Sets component as higlight base for its window
     */

    public static void setHighlightBase ( Component highlightBase )
    {
        setHighlightBase ( SwingUtils.getWindowAncestor ( highlightBase ), highlightBase );
    }

    /**
     * Sets highlight base for window
     */

    public static void setHighlightBase ( Window window, Component highlightBase )
    {
        WebGlassPane wgp = GlassPaneManager.getGlassPane ( window );
        if ( wgp != null )
        {
            wgp.setHighlightBase ( highlightBase );
        }
    }
}
