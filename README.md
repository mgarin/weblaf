WebLaF
==========
**WebLaf** is a Java Swing Look and Feel and extended components library for cross-platform applications.<br>
![Preview](http://s5.hostingkartinok.com/uploads/images/2013/10/92f65b6b3262493a5f386dc6808efbba.png)

Main advantages of WebLaF library:

1. Simple and stylish cross-platform default theme
2. Lots of additional custom Swing components
3. UIs and components stylable through painters and 9-patch images
4. Settings, language, hotkey, tooltip and other custom managers
5. Various Swing and Java utilities

You can find more information about the library on official site:<br>
http://weblookandfeel.com


Artifacts
----------
You can always find all WebLaF artifacts in the "releases" section:<br>
https://github.com/mgarin/weblaf/releases

Here are the direct links for the latest release:

1. [**weblaf-1.23.jar**](https://github.com/mgarin/weblaf/releases/download/v1.23/weblaf-1.23.jar) - library jar
2. [**weblaf-simple-1.23.jar**](https://github.com/mgarin/weblaf/releases/download/v1.23/weblaf-simple-1.23.jar) - library jar without dependencies
3. [**weblaf-src-1.23.zip**](https://github.com/mgarin/weblaf/releases/download/v1.23/weblaf-src-1.23.zip) - project sources zip
4. [**weblaf-demo-1.23.jar**](https://github.com/mgarin/weblaf/releases/download/v1.23/weblaf-demo-1.23.jar) - executable demo jar
5. [**weblaf-javadoc-1.23.zip**](https://github.com/mgarin/weblaf/releases/download/v1.23/weblaf-javadoc-1.23.zip) - JavaDoc zip
6. [**ninepatch-editor-1.23.jar**](https://github.com/mgarin/weblaf/releases/download/v1.23/ninepatch-editor-1.23.jar) - executable 9-patch editor jar


Roadmap
----------
You can always check what fixes, features and improvements are coming by checking the milestones page:<br>
https://github.com/mgarin/weblaf/issues/milestones


Building
----------
To build various WebLaF artifacts you will need [Java 1.6 (update 30 or later) or Java 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Apache ANT] (http://ant.apache.org/).<br>
Simply run `ant` command within the "build" library folder to build all artifacts at once.

Here is a full list of usable ANT targets in WebLaF build script:

1. `build.artifacts` - default target, build all artifacts at once
2. `build.weblaf.jar` -> **weblaf-x.xx.jar** - build WebLaF binary
3. `build.weblaf.simple.jar` -> **weblaf-simple-x.xx.jar** - build WebLaF binary without dependencies
4. `build.sources.zip` -> **weblaf-src-x.xx.zip** - build WebLaF sources zip archive
5. `build.npe.jar` -> **ninepatch-editor-x.xx.jar** - build 9-patch editor application
6. `build.weblaf.demo.jar` -> **weblaf-demo-x.xx.jar** - build WebLaF demo application
7. `build.javadoc` -> **weblaf-javadoc-x.xx.zip** & **javadoc** - create zipped and unzipped library JavaDoc
8. `run.npe` -> build and run ninepatch-editor-x.xx.jar - 9-patch editor application
9. `run.weblaf` -> build and run weblaf-x.xx.jar - library information dialog
10. `run.weblaf.demo` -> build and run weblaf-demo-x.xx.jar - demo application


Example Usage
----------
To install WebLaF you can simply call `install()` or use one of standard Swing L&F set methods:
```java
public class WebLafUsage
{
    public static void main ( String[] args )
    {
        // You should work with UI (including installing L&F) inside EDT
        SwingUtilities.invokeLater ( new Runnable ()
        {
            public void run ()
            {
                // Install WebLaF as application L&F
                WebLookAndFeel.install ();

                // You can also do that using standard Swing methods:
                // UIManager.setLookAndFeel ( new WebLookAndFeel () );
                // UIManager.setLookAndFeel ( "com.alee.laf.WebLookAndFeel" );
                // UIManager.setLookAndFeel ( WebLookAndFeel.class.getCanonicalName () );

                // Create you Swing application here
                // JFrame frame = ...
            }
        } );
    }
}
```


Feedback
----------
I would really appreciate if you will post any found bugs in [issues section](https://github.com/mgarin/weblaf/issues) here, on GitHub.<br>
You can also post them on the library [official site forum](http://weblookandfeel.com/forum/), but that would require registration.<br> 
And, as always, you can send any feedback directly to my email: [mgarin@alee.com](mailto:mgarin@alee.com)
