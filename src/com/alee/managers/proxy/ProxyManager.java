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

import com.alee.extended.optionpane.WebExtendedOptionPane;
import com.alee.laf.checkbox.WebCheckBox;
import com.alee.laf.optionpane.WebOptionPane;
import com.alee.managers.language.LanguageManager;
import com.alee.managers.settings.SettingsManager;
import com.alee.utils.CompareUtils;
import com.alee.utils.EncryptionUtils;
import com.alee.utils.SwingUtils;
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
 */

public final class ProxyManager
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
     * Custom proxy authenticator.
     */
    private static Authenticator authenticator;

    /**
     * Proxy settings install flag.
     */
    private static boolean proxySet = false;

    /**
     * Whether manager is initialized or not.
     */
    private static boolean initialized = false;

    /**
     * Initializes manager if it wasn't already initialized.
     */
    public static void initialize ()
    {
        if ( !initialized )
        {
            initialized = true;

            // ProxySettings class alias
            XmlUtils.processAnnotations ( ProxySettings.class );

            // Loading last saved or system proxy settings
            ProxySettings proxySettings = getProxySettings ();

            // Checking if system proxy settings are not the same
            ProxySettings systemProxySettings = getSystemProxySettings ();
            if ( proxySettings.isUseProxy () != systemProxySettings.isUseProxy () ||
                    !CompareUtils.equals ( proxySettings.getProxyHost (), systemProxySettings.getProxyHost () ) ||
                    !CompareUtils.equals ( proxySettings.getProxyPort (), systemProxySettings.getProxyPort () ) )
            {
                if ( isAutoSettingsInitialization () )
                {
                    if ( isAlwaysUseSystemSettings () )
                    {
                        proxySettings = systemProxySettings;
                    }
                }
                else
                {
                    WebCheckBox alwaysDoTheSame = new WebCheckBox ();
                    alwaysDoTheSame.setLanguage ( "weblaf.proxy.use.system.save" );
                    alwaysDoTheSame.setSelected ( false );
                    alwaysDoTheSame.setFocusable ( false );

                    // Ask for settings replacement with system ones
                    final String message = LanguageManager.get ( "weblaf.proxy.use.system.text" );
                    final String title = LanguageManager.get ( "weblaf.proxy.use.system.title" );
                    WebExtendedOptionPane dialog = WebExtendedOptionPane
                            .showConfirmDialog ( SwingUtils.getActiveWindow (), message, alwaysDoTheSame, title,
                                    WebExtendedOptionPane.YES_NO_OPTION, WebExtendedOptionPane.QUESTION_MESSAGE );

                    int result = dialog.getResult ();
                    if ( result == WebOptionPane.YES_OPTION )
                    {
                        proxySettings = systemProxySettings;
                    }

                    if ( alwaysDoTheSame.isSelected () )
                    {
                        setAutoSettingsInitialization ( true );
                        setAlwaysUseSystemSettings ( result == WebExtendedOptionPane.YES_OPTION );
                    }
                }
            }

            // Default authentificator
            authenticator = new WebProxyAuthenticator ();

            // Default proxy settings
            setProxySettings ( proxySettings );
        }
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
    public static void setAuthenticator ( Authenticator authenticator )
    {
        ProxyManager.authenticator = authenticator;
        if ( getProxySettings ().isUseProxy () )
        {
            Authenticator.setDefault ( authenticator );
        }
    }

    /**
     * Sets whether enable auto initialization of proxy settings or not.
     *
     * @param enabled whether enable auto initialization of proxy settings or not
     */
    public static void setAutoSettingsInitialization ( boolean enabled )
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
    public static void setAlwaysUseSystemSettings ( boolean useSystem )
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
     * If old proxy settings could not be found then system proxy settigns will be retrieved and installed.
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
        ProxySettings proxySettings = getSystemProxySettings ();
        setProxySettings ( proxySettings );
        return proxySettings;
    }

    /**
     * Installs proxy settings with specified host and port.
     * If the specified proxy requires authentification user will get prompted.
     *
     * @param proxyHost proxy host
     * @param proxyPort proxy port
     */
    public static void setProxySettings ( String proxyHost, String proxyPort )
    {
        setProxySettings ( new ProxySettings ( proxyHost, proxyPort ) );
    }

    /**
     * Installs proxy settings with specified host, port and proxy login and password.
     *
     * @param proxyHost     proxy host
     * @param proxyPort     proxy port
     * @param proxyLogin    proxy login
     * @param proxyPassword proxy password
     */
    public static void setProxySettings ( String proxyHost, String proxyPort, String proxyLogin, String proxyPassword )
    {
        setProxySettings ( new ProxySettings ( proxyHost, proxyPort, proxyLogin, proxyPassword ) );
    }

    /**
     * Installs proxy settings with specified host, port and proxy login and password.
     *
     * @param proxySettings single proxy settings object
     */
    public static void setProxySettings ( ProxySettings proxySettings )
    {
        setProxySettings ( proxySettings, true );
    }

    /**
     * Installs proxy settings with specified host, port and proxy login and password.
     *
     * @param proxySettings single proxy settings object
     * @param saveSettings  whether save these settings or not
     */
    public static void setProxySettings ( ProxySettings proxySettings, boolean saveSettings )
    {
        proxySet = true;

        // Saving new ProxySettings
        if ( saveSettings )
        {
            SettingsManager.set ( SETTINGS_GROUP, PROXY_SETTINGS, proxySettings );
        }

        // System properties
        Properties systemSettings = System.getProperties ();

        // Use proxy or not
        systemSettings.setProperty ( "proxySet", "" + proxySettings.isUseProxy () );

        // Either use proxy or not
        if ( proxySettings.isUseProxy () )
        {
            // Proxy settings
            systemSettings.setProperty ( "proxyHost", "" + proxySettings.getProxyHost () );
            systemSettings.setProperty ( "proxyPort", "" + proxySettings.getProxyPort () );
            systemSettings.setProperty ( "nonProxyHosts", "" + proxySettings.getNonProxyHosts () );

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
     * @param url url to process
     * @return opened URL connection
     * @throws IOException
     */
    public static URLConnection getURLConnection ( URL url ) throws IOException
    {
        // Recheck that proxy is set
        setProxySettings ();

        // Open connection properly
        ProxySettings proxySettings = getProxySettings ();
        if ( proxySettings.isUseProxy () )
        {
            URLConnection connection = url.openConnection ( new Proxy ( Proxy.Type.HTTP,
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
    public static void setupProxy ( URLConnection urlConnection )
    {
        // Recheck that proxy is set
        setProxySettings ();

        // Proxy settings
        ProxySettings proxySettings = getProxySettings ();
        if ( proxySettings.isUseProxy () && proxySettings.isUseProxyAuthentification () )
        {
            String encoded = EncryptionUtils.base64encode ( proxySettings.getProxyLogin () + ":" + proxySettings.getProxyPassword () );
            urlConnection.setRequestProperty ( "Proxy-Authorization", "Basic " + encoded );
        }
    }

    /**
     * Returns single system proxy settings object.
     *
     * @return single system proxy settings object
     */
    public static ProxySettings getSystemProxySettings ()
    {
        ProxySettings proxySettings = new ProxySettings ();
        System.setProperty ( "java.net.useSystemProxies", "true" );
        Proxy proxy = getSystemHttpProxy ();
        if ( proxy != null )
        {
            InetSocketAddress addr = ( InetSocketAddress ) proxy.address ();
            if ( addr != null && addr.getHostName () != null )
            {
                proxySettings.setUseProxy ( true );
                proxySettings.setProxyHost ( addr.getHostName () );
                proxySettings.setProxyPort ( "" + addr.getPort () );
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
    private static Proxy getSystemHttpProxy ()
    {
        try
        {
            ProxySelector def = ProxySelector.getDefault ();
            List<Proxy> l = def.select ( new URI ( "http://www.google.com" ) );
            for ( Proxy proxy : l )
            {
                if ( proxy != null && proxy.type ().equals ( Proxy.Type.HTTP ) )
                {
                    return proxy;
                }
            }
        }
        catch ( Exception e )
        {
            //
        }
        return null;
    }
}