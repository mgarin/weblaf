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

import com.alee.api.clone.Clone;
import com.alee.utils.TextUtils;
import com.alee.utils.xml.PasswordConverter;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import java.io.Serializable;

/**
 * Proxy settings object.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-ProxyManager">How to use ProxyManager</a>
 * @see ProxyManager
 */
@XStreamAlias ( "ProxySettings" )
public class ProxySettings implements Cloneable, Serializable
{
    /**
     * Whether should use proxy settings or not.
     */
    @XStreamAsAttribute
    private boolean useProxy = false;

    /**
     * Proxy host.
     */
    @XStreamAsAttribute
    private String proxyHost = null;

    /**
     * Proxy port.
     */
    @XStreamAsAttribute
    private String proxyPort = null;

    /**
     * Non-proxy hosts.
     */
    @XStreamAsAttribute
    private String nonProxyHosts = null;

    /**
     * Whether proxy requires authentification or not.
     */
    @XStreamAsAttribute
    private boolean useProxyAuthentification = false;

    /**
     * Proxy login.
     */
    @XStreamAsAttribute
    private String proxyLogin = null;

    /**
     * Proxy password.
     */
    @XStreamAsAttribute
    @XStreamConverter ( PasswordConverter.class )
    private String proxyPassword = null;

    /**
     * Constructs disabled proxy settings.
     */
    public ProxySettings ()
    {
        this ( null, null );
    }

    /**
     * Constructs proxy settings for host without authentification.
     *
     * @param proxyHost proxy host
     * @param proxyPort proxy port
     */
    public ProxySettings ( final String proxyHost, final String proxyPort )
    {
        this ( proxyHost, proxyPort, null, null );
    }

    /**
     * Constructs proxy settings for host that requires authentification.
     *
     * @param proxyHost     proxy host
     * @param proxyPort     proxy port
     * @param proxyLogin    proxy login
     * @param proxyPassword proxy password
     */
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

    /**
     * Returns whether should use proxy settings or not.
     *
     * @return true if should use proxy settings, false otherwise
     */
    public boolean isUseProxy ()
    {
        return useProxy;
    }

    /**
     * Sets whether should use proxy settings or not.
     *
     * @param useProxy whether should use proxy settings or not
     */
    public void setUseProxy ( final boolean useProxy )
    {
        this.useProxy = useProxy;
    }

    /**
     * Returns proxy host.
     *
     * @return proxy host
     */
    public String getProxyHost ()
    {
        return proxyHost;
    }

    /**
     * Sets proxy host.
     *
     * @param proxyHost new proxy host
     */
    public void setProxyHost ( final String proxyHost )
    {
        this.proxyHost = proxyHost;
    }

    /**
     * Returns proxy port.
     *
     * @return proxy port
     */
    public String getProxyPort ()
    {
        return proxyPort;
    }

    /**
     * Returns integer proxy port.
     *
     * @return integer proxy port
     */
    public int getProxyPortInt ()
    {
        try
        {
            return Integer.parseInt ( proxyPort );
        }
        catch ( final Exception e )
        {
            return 80;
        }
    }

    /**
     * Sets proxy port.
     *
     * @param proxyPort new proxy port
     */
    public void setProxyPort ( final String proxyPort )
    {
        this.proxyPort = proxyPort;
    }

    /**
     * Returns non-proxy hosts.
     *
     * @return non-proxy hosts
     */
    public String getNonProxyHosts ()
    {
        return nonProxyHosts;
    }

    /**
     * Sets non-proxy hosts.
     *
     * @param nonProxyHosts new non-proxy hosts
     */
    public void setNonProxyHosts ( final String nonProxyHosts )
    {
        this.nonProxyHosts = nonProxyHosts;
    }

    /**
     * Returns whether proxy requires authentification or not.
     *
     * @return true if proxy requires authentification, false otherwise
     */
    public boolean isUseProxyAuthentification ()
    {
        return useProxyAuthentification;
    }

    /**
     * Sets whether proxy requires authentification or not.
     *
     * @param useProxyAuthentification whether proxy requires authentification or not
     */
    public void setUseProxyAuthentification ( final boolean useProxyAuthentification )
    {
        this.useProxyAuthentification = useProxyAuthentification;
    }

    /**
     * Returns proxy login.
     *
     * @return proxy login
     */
    public String getProxyLogin ()
    {
        return proxyLogin;
    }

    /**
     * Sets proxy login.
     *
     * @param proxyLogin new proxy login
     */
    public void setProxyLogin ( final String proxyLogin )
    {
        this.proxyLogin = proxyLogin;
    }

    /**
     * Returns proxy password.
     *
     * @return proxy password
     */
    public String getProxyPassword ()
    {
        return proxyPassword;
    }

    /**
     * Sets proxy password.
     *
     * @param proxyPassword new proxy password
     */
    public void setProxyPassword ( final String proxyPassword )
    {
        this.proxyPassword = proxyPassword;
    }

    @Override
    public ProxySettings clone ()
    {
        return Clone.deep ().clone ( this );
    }

    @Override
    public String toString ()
    {
        if ( isUseProxy () )
        {
            final boolean useAuth = isUseProxyAuthentification ();
            final String auth = useAuth ? getProxyLogin () + ":" + TextUtils.createString ( "*", getProxyPassword ().length () ) + "@" : "";
            return "ProxySettings [ " + auth + getProxyHost () + ":" + getProxyPort () + " ]";
        }
        else
        {
            return "ProxySettings [ no proxy ]";
        }
    }
}