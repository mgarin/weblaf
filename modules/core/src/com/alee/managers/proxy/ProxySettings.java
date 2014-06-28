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

package com.alee.managers.proxy;

import com.alee.utils.xml.PasswordConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * User: mgarin Date: 12.10.11 Time: 17:49
 */

@XStreamAlias ("ProxySettings")
public class ProxySettings implements Serializable, Cloneable
{
    @XStreamAsAttribute
    private boolean useProxy = false;

    @XStreamAsAttribute
    private String proxyHost = null;

    @XStreamAsAttribute
    private String proxyPort = null;

    @XStreamAsAttribute
    private String nonProxyHosts = null;

    @XStreamAsAttribute
    private boolean useProxyAuthentification = false;

    @XStreamAsAttribute
    private String proxyLogin = null;

    @XStreamAsAttribute
    @XStreamConverter (PasswordConverter.class)
    private String proxyPassword = null;

    public ProxySettings ()
    {
        this ( null, null );
    }

    public ProxySettings ( final String proxyHost, final String proxyPort )
    {
        this ( proxyHost, proxyPort, null, null );
    }

    public ProxySettings ( final String proxyHost, final String proxyPort, final String proxyLogin, final String proxyPassword )
    {
        super ();

        if ( proxyHost != null && proxyPort != null )
        {
            this.useProxy = true;
            this.proxyHost = proxyHost;
            this.proxyPort = proxyPort;

            if ( proxyLogin != null && proxyPassword != null )
            {
                this.useProxyAuthentification = true;
                this.proxyLogin = proxyLogin;
                this.proxyPassword = proxyPassword;
            }
            else
            {
                this.useProxyAuthentification = false;
            }
        }
        else
        {
            this.useProxy = false;
            this.useProxyAuthentification = false;
        }
    }

    public boolean isUseProxy ()
    {
        return useProxy;
    }

    public void setUseProxy ( final boolean useProxy )
    {
        this.useProxy = useProxy;
    }

    public String getProxyHost ()
    {
        return proxyHost;
    }

    public void setProxyHost ( final String proxyHost )
    {
        this.proxyHost = proxyHost;
    }

    public String getProxyPort ()
    {
        return proxyPort;
    }

    public int getProxyPortInt ()
    {
        try
        {
            return Integer.parseInt ( proxyPort );
        }
        catch ( final Throwable e )
        {
            return 80;
        }
    }

    public void setProxyPort ( final String proxyPort )
    {
        this.proxyPort = proxyPort;
    }

    public String getNonProxyHosts ()
    {
        return nonProxyHosts;
    }

    public void setNonProxyHosts ( final String nonProxyHosts )
    {
        this.nonProxyHosts = nonProxyHosts;
    }

    public boolean isUseProxyAuthentification ()
    {
        return useProxyAuthentification;
    }

    public void setUseProxyAuthentification ( final boolean useProxyAuthentification )
    {
        this.useProxyAuthentification = useProxyAuthentification;
    }

    public String getProxyLogin ()
    {
        return proxyLogin;
    }

    public void setProxyLogin ( final String proxyLogin )
    {
        this.proxyLogin = proxyLogin;
    }

    public String getProxyPassword ()
    {
        return proxyPassword;
    }

    public void setProxyPassword ( final String proxyPassword )
    {
        this.proxyPassword = proxyPassword;
    }

    @Override
    protected ProxySettings clone ()
    {
        final ProxySettings proxySettings = new ProxySettings ();
        proxySettings.setUseProxy ( isUseProxy () );
        proxySettings.setProxyHost ( getProxyHost () );
        proxySettings.setProxyPort ( getProxyPort () );
        proxySettings.setNonProxyHosts ( getNonProxyHosts () );
        proxySettings.setUseProxyAuthentification ( isUseProxyAuthentification () );
        proxySettings.setProxyLogin ( getProxyLogin () );
        proxySettings.setProxyPassword ( getProxyPassword () );
        return proxySettings;
    }
}