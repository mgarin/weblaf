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

import com.alee.managers.settings.SettingsManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.EncryptionUtils;
import com.alee.utils.XmlUtils;

import java.io.IOException;
import java.net.*;
import java.util.List;
import java.util.Properties;

/**
 * This manager allows you to quickly manage application's global proxy settings.
 * When activated this manager might cause a proxy chooser or an authentification popup to pop.
 * That will happen in case it will detect that your proxy settings does not match system proxy settings.
 *
 * @author Mikle Garin
 * @see <a href="https://github.com/mgarin/weblaf/wiki/How-to-use-ProxyManager">How to use ProxyManager</a>
 * @see com.alee.managers.proxy.ProxySettings
 * @see com.alee.managers.proxy.SystemProxyConfirmationSupport
 */

public class ProxyManager
{
    /**
     * Settings group key for proxy settings.
     */
    public static String SETTINGS_GROUP = "ProxyManager";

    /**
     * Settings key for proxy settings.
     */
    public static String PROXY_SETTINGS = "ProxySettings";

    /**
     * Settings key for automated proxy settings option.
     */
    public static String AUTO_SETTINGS_ON = "AutoSettingsOn";

    /**
     * Settings key for system proxy settings usage option.
     */
    public static String ALWAYS_USE_SYSTEM_SETTINGS = "AlwaysUseSystemSettings";

    /**
     * Settings key for proxy settings save option.
     */
    public static String SAVE_SETTINGS = "SaveSettings";

    /**
     * Whether automatic proxy detection is enabled or not.
     */
    protected static boolean autoDetectionEnabled = false;

    /**
     * Custom proxy authenticator.
     */
    protected static Authenticator authenticator;

    /**
     * Proxy settings install flag.
     */
    protected static boolean proxySet = false;

    /**
     * System proxy settings confirmation dialog support.
     */
    protected static SystemProxyConfirmationSupport systemProxyConfirmationSupport = null;

    /**
     * Whether manager is initialized or not.
     */
    protected static boolean initialized = false;

    /**
     * Initializes manager if it wasn't already initialized.
     */
    public static synchronized void initialize ()
    {
        if ( !initialized )
        {
            // ProxySettings class alias
            XmlUtils.processAnnotations ( ProxySettings.class );

            // Proxy auto-detection
            if ( autoDetectionEnabled )
            {
                // Loading last saved or system proxy settings
                ProxySettings proxySettings = getProxySettings ();

                // Checking if system proxy settings are not the same
                final ProxySettings systemProxySettings = getSystemProxySettings ();
                if ( proxySettings.isUseProxy () != systemProxySettings.isUseProxy () ||
                        !CompareUtils.equals ( proxySettings.getProxyHost (), systemProxySettings.getProxyHost () ) ||
                        !CompareUtils.equals ( proxySettings.getProxyPort (), systemProxySettings.getProxyPort () ) )
                {
                    // Checking whether we have proxy confirmation support or auto settings are enabled
                    if ( systemProxyConfirmationSupport == null || isAutoSettingsInitialization () )
                    {
                        // Checking whether we are allowed to use system settings
                        if ( isAlwaysUseSystemSettings () )
                        {
                            // Applying system settings automatically
                            proxySettings = systemProxySettings;
                        }
                    }
                    else
                    {
                        // Asking proxy confirmation support whether we should use system proxy or leave current settings intact
                        final boolean useSystemProxy = systemProxyConfirmationSupport.shouldUseSystemProxy ();
                        if ( useSystemProxy )
                        {
                            proxySettings = systemProxySettings;
                        }
                        // Saving the choice if confirmation shouldn't be prompted anymore
                        if ( systemProxyConfirmationSupport.alwaysDoTheSame () )
                        {
                            setAutoSettingsInitialization ( true );
                            setAlwaysUseSystemSettings ( useSystemProxy );
                        }
                    }
                }

                // Default proxy settings
                setProxySettings ( proxySettings );
            }

            initialized = true;
        }
    }

    /**
     * Returns whether automatic proxy detection is enabled or not.
     *
     * @return true if automatic proxy detection is enabled, false otherwise
     */
    public static boolean isAutoDetectionEnabled ()
    {
        return autoDetectionEnabled;
    }

    /**
     * Sets whether automatic proxy detection is enabled or not.
     *
     * @param enabled whether automatic proxy detection is enabled or not
     */
    public static void setAutoDetectionEnabled ( final boolean enabled )
    {
        ProxyManager.autoDetectionEnabled = enabled;
    }

    /**
     * Returns current proxy authenticator.
     *
     * @return current proxy authenticator
     */
    public static Authenticator getAuthenticator ()
    {
        return authenticator;
    }

    /**
     * Changes current proxy authenticator to the specified one.
     *
     * @param authenticator new proxy authenticator
     */
    public static void setAuthenticator ( final Authenticator authenticator )
    {
        ProxyManager.authenticator = authenticator;
        if ( initialized && getProxySettings ().isUseProxy () )
        {
            Authenticator.setDefault ( authenticator );
        }
    }

    /**
     * Sets whether enable auto initialization of proxy settings or not.
     *
     * @param enabled whether enable auto initialization of proxy settings or not
     */
    public static void setAutoSettingsInitialization ( final boolean enabled )
    {
        SettingsManager.set ( SETTINGS_GROUP, AUTO_SETTINGS_ON, enabled );
    }

    /**
     * Returns whether auto initialization of proxy settings is enabled or not.
     *
     * @return true if auto initialization of proxy settings is enabled, false otherwise
     */
    public static boolean isAutoSettingsInitialization ()
    {
        return SettingsManager.get ( SETTINGS_GROUP, AUTO_SETTINGS_ON, false );
    }

    /**
     * Sets whether enable system proxy settings usage or not.
     *
     * @param useSystem whether enable system proxy settings usage or not
     */
    public static void setAlwaysUseSystemSettings ( final boolean useSystem )
    {
        SettingsManager.set ( SETTINGS_GROUP, ALWAYS_USE_SYSTEM_SETTINGS, useSystem );
    }

    /**
     * Returns whether system proxy settings usage is enabled or not.
     *
     * @return true if system proxy settings usage is enabled, false otherwise
     */
    public static boolean isAlwaysUseSystemSettings ()
    {
        return SettingsManager.get ( SETTINGS_GROUP, ALWAYS_USE_SYSTEM_SETTINGS, false );
    }

    /**
     * Returns current proxy settings or default proxy settings if none installed.
     *
     * @return current proxy settings or default proxy settings if none installed
     */
    public static ProxySettings getProxySettings ()
    {
        return SettingsManager.get ( SETTINGS_GROUP, PROXY_SETTINGS, getSystemProxySettings () );
    }

    /**
     * Installs saved or system proxy settings.
     * This method will install existing proxy settings that were saved before.
     * If old proxy settings could not be found then system proxy settings will be retrieved and installed.
     */
    public static void setProxySettings ()
    {
        if ( !proxySet )
        {
            setProxySettings ( getProxySettings () );
        }
    }

    /**
     * Installs empty proxy settings.
     * This method simply disables proxy.
     */
    public static void setEmptyProxySettings ()
    {
        setProxySettings ( new ProxySettings () );
    }

    /**
     * Installs system proxy settings.
     *
     * @return single system proxy settings object
     */
    public static ProxySettings setSystemProxySettings ()
    {
        final ProxySettings proxySettings = getSystemProxySettings ();
        setProxySettings ( proxySettings );
        return proxySettings;
    }

    /**
     * Installs proxy settings with specified host and port.
     * If the specified proxy requires authentification user will get prompted.
     *
     * @param host proxy host
     * @param port proxy port
     */
    public static void setProxySettings ( final String host, final String port )
    {
        setProxySettings ( new ProxySettings ( host, port ) );
    }

    /**
     * Installs proxy settings with specified host, port and proxy login and password.
     *
     * @param host     proxy host
     * @param port     proxy port
     * @param login    proxy login
     * @param password proxy password
     */
    public static void setProxySettings ( final String host, final String port, final String login, final String password )
    {
        setProxySettings ( new ProxySettings ( host, port, login, password ) );
    }

    /**
     * Installs proxy settings with specified host, port and proxy login and password.
     *
     * @param settings single proxy settings object
     */
    public static void setProxySettings ( final ProxySettings settings )
    {
        setProxySettings ( settings, true );
    }

    /**
     * Installs proxy settings with specified host, port and proxy login and password.
     *
     * @param settings single proxy settings object
     * @param save     whether save these settings or not
     */
    public static void setProxySettings ( final ProxySettings settings, final boolean save )
    {
        proxySet = true;

        // Saving new ProxySettings
        if ( save )
        {
            SettingsManager.set ( SETTINGS_GROUP, PROXY_SETTINGS, settings );
        }

        // System properties
        final Properties systemSettings = System.getProperties ();

        // Use proxy or not
        systemSettings.setProperty ( "proxySet", "" + settings.isUseProxy () );

        // Either use proxy or not
        if ( settings.isUseProxy () )
        {
            // Proxy settings
            systemSettings.setProperty ( "proxyHost", "" + settings.getProxyHost () );
            systemSettings.setProperty ( "proxyPort", "" + settings.getProxyPort () );
            systemSettings.setProperty ( "nonProxyHosts", "" + settings.getNonProxyHosts () );

            // Proxy authentification
            Authenticator.setDefault ( authenticator );
        }
        else
        {
            // Proxy settings
            systemSettings.setProperty ( "proxyHost", "" );
            systemSettings.setProperty ( "proxyPort", "" );
            systemSettings.setProperty ( "nonProxyHosts", "" );

            // Proxy authentification
            Authenticator.setDefault ( null );
        }
    }

    /**
     * Opens URL connection with current proxy settings.
     *
     * @param url url to connect to
     * @return opened URL connection
     * @throws java.io.IOException when connection failed
     */
    public static URLConnection getURLConnection ( final URL url ) throws IOException
    {
        // Recheck that proxy is set
        setProxySettings ();

        // Open connection properly
        final ProxySettings proxySettings = getProxySettings ();
        if ( proxySettings.isUseProxy () )
        {
            final URLConnection connection = url.openConnection ( new Proxy ( Proxy.Type.HTTP,
                    new InetSocketAddress ( proxySettings.getProxyHost (), proxySettings.getProxyPortInt () ) ) );
            setupProxy ( connection );
            return connection;
        }
        else
        {
            // Simple connection without proxy
            return url.openConnection ();
        }
    }

    /**
     * Installs current proxy settings to URL connection.
     *
     * @param urlConnection URL connection to modify
     */
    public static void setupProxy ( final URLConnection urlConnection )
    {
        // Recheck that proxy is set
        setProxySettings ();

        // Proxy settings
        final ProxySettings proxySettings = getProxySettings ();
        if ( proxySettings.isUseProxy () && proxySettings.isUseProxyAuthentification () )
        {
            final String toEncode = proxySettings.getProxyLogin () + ":" + proxySettings.getProxyPassword ();
            urlConnection.setRequestProperty ( "Proxy-Authorization", "Basic " + EncryptionUtils.base64encode ( toEncode ) );
        }
    }

    /**
     * Returns single system proxy settings object.
     *
     * @return single system proxy settings object
     */
    public static ProxySettings getSystemProxySettings ()
    {
        final ProxySettings proxySettings = new ProxySettings ();
        System.setProperty ( "java.net.useSystemProxies", "true" );
        final Proxy proxy = getSystemHttpProxy ();
        if ( proxy != null )
        {
            final InetSocketAddress address = ( InetSocketAddress ) proxy.address ();
            if ( address != null && address.getHostName () != null )
            {
                proxySettings.setUseProxy ( true );
                proxySettings.setProxyHost ( address.getHostName () );
                proxySettings.setProxyPort ( "" + address.getPort () );
            }
        }
        System.setProperty ( "java.net.useSystemProxies", "false" );
        return proxySettings;
    }

    /**
     * Returns system http proxy.
     *
     * @return system http proxy
     */
    protected static Proxy getSystemHttpProxy ()
    {
        try
        {
            final ProxySelector def = ProxySelector.getDefault ();
            final List<Proxy> l = def.select ( new URI ( "http://www.google.com" ) );
            for ( final Proxy proxy : l )
            {
                if ( proxy != null && proxy.type ().equals ( Proxy.Type.HTTP ) )
                {
                    return proxy;
                }
            }
        }
        catch ( final Exception e )
        {
            //
        }
        return null;
    }

    /**
     * Returns system proxy settings confirmation dialog support.
     *
     * @return system proxy settings confirmation dialog support
     */
    public static SystemProxyConfirmationSupport getSystemProxyConfirmationSupport ()
    {
        return systemProxyConfirmationSupport;
    }

    /**
     * Sets system proxy settings confirmation dialog support.
     *
     * @param support system proxy settings confirmation dialog support
     */
    public static void setSystemProxyConfirmationSupport ( final SystemProxyConfirmationSupport support )
    {
        ProxyManager.systemProxyConfirmationSupport = support;
    }
}