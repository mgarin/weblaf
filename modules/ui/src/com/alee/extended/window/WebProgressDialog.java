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

import com.alee.api.annotations.NotNull;
import com.alee.laf.label.WebLabel;
import com.alee.laf.panel.WebPanel;
import com.alee.laf.progressbar.WebProgressBar;
import com.alee.laf.window.WebDialog;
import com.alee.managers.style.StyleId;
import com.alee.utils.CoreSwingUtils;

import javax.swing.*;
import java.awt.*;

/**
 * This class provides a quick way to display a progress dialog anywhere you need it and change the progress values without any additional
 * efforts like working with Swing thread to update progress.
 *
 * @author Mikle Garin
 */
public class WebProgressDialog extends WebDialog
{
    private int preferredProgressWidth = 0;
    private boolean shownOnce = false;

    private final WebPanel container;
    private final WebLabel titleText;
    private final WebProgressBar progressBar;
    private Component middleComponent = null;

    public WebProgressDialog ( final String title )
    {
        this ( null, title );
    }

    public WebProgressDialog ( final Window owner, final String title )
    {
        super ( owner, title );
        setLayout ( new BorderLayout () );

        container = new WebPanel ( new BorderLayout ( 5, 5 ) );
        container.setMargin ( 10, 10, 10, 10 );
        container.setOpaque ( false );
        add ( container, BorderLayout.CENTER );

        // Creating label with single space to hold label height on pack
        titleText = new WebLabel ( StyleId.labelShadow, " ", WebLabel.CENTER )
        {
            @NotNull
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                ps.width = 0;
                return ps;
            }
        };
        container.add ( titleText, BorderLayout.NORTH );

        // Default progress bar
        progressBar = new WebProgressBar ( WebProgressBar.HORIZONTAL, 0, 100 )
        {
            @NotNull
            @Override
            public Dimension getPreferredSize ()
            {
                final Dimension ps = super.getPreferredSize ();
                if ( preferredProgressWidth > 0 )
                {
                    ps.width = preferredProgressWidth;
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
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                titleText.setText ( text );
            }
        } );
    }

    public void setTextKey ( final String key )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                titleText.setLanguage ( key );
            }
        } );
    }

    public String getText ()
    {
        return titleText.getText ();
    }

    public void setProgressText ( final String progressText )
    {
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
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
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
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
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
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
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
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
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
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
        CoreSwingUtils.invokeLater ( new Runnable ()
        {
            @Override
            public void run ()
            {
                progressBar.setValue ( progress );
            }
        } );
    }

    public int getPreferredProgressWidth ()
    {
        return preferredProgressWidth;
    }

    public void setPreferredProgressWidth ( final int preferredProgressWidth )
    {
        this.preferredProgressWidth = preferredProgressWidth;
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

    public void setMiddleComponent ( final Component middleComponent )
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

    public void setShowProgressBar ( final boolean showProgressBar )
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

    @Override
    public void setVisible ( final boolean b )
    {
        if ( b )
        {
            shownOnce = true;
        }
        super.setVisible ( b );
    }
}
