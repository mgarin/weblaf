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

package com.alee.extended.window;

import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.rootpane.WebDialog;
import com.alee.utils.SwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * User: mgarin Date: 14.02.12 Time: 12:08
 * <p/>
 * This class provides a quick way to display a progress dialog anywhere you need it and change the progress values without any additional
 * efforts like working with Swing thread to update progress
 */

public class WebProgressDialog extends WebDialog
{
    private int preferredWidth = 0;
    private boolean shownOnce = false;

    private WebPanel container;
    private WebLabel titleText;
    private Component middleComponent = null;
    private WebProgressBar progressBar;

    public WebProgressDialog ( String title )
    {
        this ( null, title );
    }

    public WebProgressDialog ( Window owner, String title )
    {
        super ( owner, title );
        setLayout ( new BorderLayout () );

        container = new WebPanel ( new BorderLayout ( 5, 5 ) );
        container.setMargin ( 10, 10, 10, 10 );
        container.setOpaque ( false );
        add ( container, BorderLayout.CENTER );

        // Creating label with single space to hold label height on pack
        titleText = new WebLabel ( " ", WebLabel.CENTER )
        {
            public Dimension getPreferredSize ()
            {
                Dimension ps = super.getPreferredSize ();
                ps.width = 0;
                return ps;
            }
        };
        titleText.setDrawShade ( true );
        container.add ( titleText, BorderLayout.NORTH );

        // Default progress bar
        progressBar = new WebProgressBar ( WebProgressBar.HORIZONTAL, 0, 100 )
        {
            public Dimension getPreferredSize ()
            {
                Dimension ps = super.getPreferredSize ();
                if ( preferredWidth > 0 )
                {
                    ps.width = preferredWidth;
                }
                return ps;
            }
        };
        progressBar.setStringPainted ( true );
        container.add ( progressBar, BorderLayout.SOUTH );

        setDefaultCloseOperation ( JDialog.DISPOSE_ON_CLOSE );
        setResizable ( false );
        setModal ( false );

        updateBounds ();
        setLocationRelativeTo ( owner );
    }

    public void setText ( final String text )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                titleText.setText ( text );
            }
        } );
    }

    public String getText ()
    {
        return titleText.getText ();
    }

    public void setProgressText ( final String progressText )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                progressBar.setString ( progressText );
            }
        } );
    }

    public String getProgressText ()
    {
        return progressBar.getString ();
    }

    public void setShowProgressText ( final boolean showProgressText )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                progressBar.setStringPainted ( showProgressText );
                updateBounds ();
            }
        } );
    }

    public boolean isShowProgressText ()
    {
        return progressBar.isStringPainted ();
    }

    public void setIndeterminate ( final boolean indeterminate )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                progressBar.setIndeterminate ( indeterminate );
            }
        } );
    }

    public boolean isIndeterminate ()
    {
        return progressBar.isIndeterminate ();
    }

    public void setMinimum ( final int minimum )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                progressBar.setMinimum ( minimum );
            }
        } );
    }

    public int getMinimum ()
    {
        return progressBar.getMinimum ();
    }

    public void setMaximum ( final int maximum )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                progressBar.setMaximum ( maximum );
            }
        } );
    }

    public int getMaximum ()
    {
        return progressBar.getMaximum ();
    }

    public void setProgress ( final int progress )
    {
        SwingUtils.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                progressBar.setValue ( progress );
            }
        } );
    }

    public int getPreferredWidth ()
    {
        return preferredWidth;
    }

    public void setPreferredWidth ( int preferredWidth )
    {
        this.preferredWidth = preferredWidth;
        updateBounds ();
    }

    public int getProgress ()
    {
        return progressBar.getValue ();
    }

    public WebLabel getTitleLabel ()
    {
        return titleText;
    }

    public Component getMiddleComponent ()
    {
        return middleComponent;
    }

    public void setMiddleComponent ( Component middleComponent )
    {
        if ( this.middleComponent != null )
        {
            container.remove ( middleComponent );
        }
        this.middleComponent = middleComponent;
        container.add ( middleComponent, BorderLayout.CENTER );
        container.revalidate ();
        updateBounds ();
    }

    public WebProgressBar getProgressBar ()
    {
        return progressBar;
    }

    public void setShowProgressBar ( boolean showProgressBar )
    {
        if ( showProgressBar )
        {
            if ( progressBar.getParent () != container )
            {
                container.add ( progressBar, BorderLayout.SOUTH );
                container.revalidate ();
                updateBounds ();
            }
        }
        else
        {
            if ( progressBar.getParent () == container )
            {
                container.remove ( progressBar );
                container.revalidate ();
                updateBounds ();
            }
        }
    }

    private void updateBounds ()
    {
        pack ();
        if ( !shownOnce )
        {
            setLocationRelativeTo ( getOwner () );
        }
    }

    public void setVisible ( boolean b )
    {
        if ( b )
        {
            shownOnce = true;
        }
        super.setVisible ( b );
    }
}