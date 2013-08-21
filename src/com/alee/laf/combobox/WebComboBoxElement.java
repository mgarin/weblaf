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

package com.alee.laf.combobox;

import com.alee.laf.label.WebLabel;

/**
 * User: mgarin Date: 27.07.12 Time: 19:17
 */

public class WebComboBoxElement extends WebLabel
{
    private WebComboBoxElementPainter painter;

    private int totalElements;
    private int index;
    private boolean selected;

    public WebComboBoxElement ()
    {
        super ();
        setPainter ( new WebComboBoxElementPainter () );
    }

    public void updatePainter ()
    {
        super.setPainter ( painter );
    }

    @Override
    public WebComboBoxElementPainter getPainter ()
    {
        return painter;
    }

    public void setPainter ( WebComboBoxElementPainter painter )
    {
        this.painter = painter;
        updatePainter ();
    }

    public int getTotalElements ()
    {
        return totalElements;
    }

    public void setTotalElements ( int totalElements )
    {
        this.totalElements = totalElements;
    }

    public int getIndex ()
    {
        return index;
    }

    public void setIndex ( int index )
    {
        this.index = index;
    }

    public boolean isSelected ()
    {
        return selected;
    }

    public void setSelected ( boolean selected )
    {
        this.selected = selected;
    }

    /**
     * Overridden for performance reasons.
     */

    // Doesn't work well on OpenJDK

    //    public void repaint ()
    //    {
    //    }
    //
    //    public void repaint ( long tm, int x, int y, int width, int height )
    //    {
    //    }
    //
    //    public void repaint ( Rectangle r )
    //    {
    //    }
    //
    //    public void validate ()
    //    {
    //    }
    //
    //    public void invalidate ()
    //    {
    //    }
    //
    //    public void revalidate ()
    //    {
    //    }
}