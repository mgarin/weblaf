Web Look and Feel
==========
**WebLaf** is a Java Look and Feel library for cross-platform Swing applications.<br>

Its main advantages are:

1. Simple and yet stylish default theme applied to all components
2. Stylable through painters and nine-patch resources UIs
3. Various extended components - collapsible panes, checkbox lists and others
4. Various managers to simply control application settings, language, tooltips, hotkeys and other aspects
5. Various utility classes which simplify Swing components usage
6. Various Swing extensions that allows animated transitions, effects and other features

You can find more information about the library on official site:<br>
http://weblookandfeel.com


Artifacts
----------
You can always find all WebLaF releases in the "releases" section:<br>
https://github.com/mgarin/weblaf/releases

Here are the direct links for the latest release (v1.17):

1. [**weblaf-1.17.jar**](https://github.com/mgarin/weblaf/releases/download/v1.17/weblaf-1.17.jar) - library jar
2. [**weblaf-src-1.17.zip**](https://github.com/mgarin/weblaf/releases/download/v1.17/weblaf-src-1.17.zip) - project sources zip
3. [**weblaf-demo-1.17.jar**](https://github.com/mgarin/weblaf/releases/download/v1.17/weblaf-demo-1.17.jar) - executable demo jar
4. [**weblaf-javadoc-1.17.zip**](https://github.com/mgarin/weblaf/releases/download/v1.17/weblaf-javadoc-1.17.zip) - JavaDoc zip
5. [**ninepatch-editor-1.17.jar**](https://github.com/mgarin/weblaf/releases/download/v1.17/ninepatch-editor-1.17.jar) - executable 9-patch editor jar


Roadmap
----------
You can always check what fixes, features and improvements are coming by checking the milestones page:<br>
https://github.com/mgarin/weblaf/issues/milestones


Building
----------
To build various WebLaF artifacts you will need [Java 1.6 update 30 (or later)](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Apache ANT] (http://ant.apache.org/).<br>
Simply run `ant` command within the "build" library folder to build all artifacts at once.

Here is a full list of usable ANT targets in WebLaF build script:

1. **build.artifacts** - default target, builds all artifacts at once
2. **build.sources.zip** - builds Sources.zip and saves it into artifacts folder
3. **build.weblaf.jar** - builds WebLookAndFeel.jar and saves it into artifacts folder
4. **build.npe.jar** - builds NinePatchEditor.jar and saves it into artifacts folder
5. **build.weblaf.demo.jar** - builds WebLookAndFeel_demo.jar and saves it into artifacts folder
6. **build.javadoc** - creates library zipped and unzipped JavaDoc versions inside artifacts folder
7. **run.npe** - build and run NinePatchEditor.jar (Nine-Patch Editor application)
8. **run.weblaf** - build and run WebLookAndFeel.jar (library information dialog)
9. **run.weblaf.demo** - build and run WebLookAndFeel_demo.jar (library demo application)


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
