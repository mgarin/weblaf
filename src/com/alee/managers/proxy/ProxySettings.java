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

    public ProxySettings ( String proxyHost, String proxyPort )
    {
        this ( proxyHost, proxyPort, null, null );
    }

    public ProxySettings ( String proxyHost, String proxyPort, String proxyLogin, String proxyPassword )
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

    public void setUseProxy ( boolean useProxy )
    {
        this.useProxy = useProxy;
    }

    public String getProxyHost ()
    {
        return proxyHost;
    }

    public void setProxyHost ( String proxyHost )
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
        catch ( Throwable e )
        {
            return 80;
        }
    }

    public void setProxyPort ( String proxyPort )
    {
        this.proxyPort = proxyPort;
    }

    public String getNonProxyHosts ()
    {
        return nonProxyHosts;
    }

    public void setNonProxyHosts ( String nonProxyHosts )
    {
        this.nonProxyHosts = nonProxyHosts;
    }

    public boolean isUseProxyAuthentification ()
    {
        return useProxyAuthentification;
    }

    public void setUseProxyAuthentification ( boolean useProxyAuthentification )
    {
        this.useProxyAuthentification = useProxyAuthentification;
    }

    public String getProxyLogin ()
    {
        return proxyLogin;
    }

    public void setProxyLogin ( String proxyLogin )
    {
        this.proxyLogin = proxyLogin;
    }

    public String getProxyPassword ()
    {
        return proxyPassword;
    }

    public void setProxyPassword ( String proxyPassword )
    {
        this.proxyPassword = proxyPassword;
    }

    @Override
    protected ProxySettings clone ()
    {
        ProxySettings proxySettings = new ProxySettings ();
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