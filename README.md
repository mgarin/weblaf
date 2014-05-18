WebLaF
==========
**WebLaf** is a Java Swing Look and Feel and extended components library for cross-platform applications.<br>
![Preview](./screenshots/weblaf-preview.png)


Advantages
----------

- Simple and stylish cross-platform default theme
- Lots of useful custom Swing components
- Fully stylable through settings, painters and custom skins
- Language, settings, hotkey, tooltip and other custom managers
- Various Swing and general utilities for many possible cases
- Full support for RTL components orientation

You can find more information about the library on official site:<br>
http://weblookandfeel.com


Custom components
---------
Here are **some** screenshots of the custom WebLaF components:

`WebTristateCheckBox`<br>
![Tristate checkbox](./screenshots/tristate-checkbox.png)

`WebLinkLabel`<br>
![Link label](./screenshots/link-label.png)

`WebCollapsiblePane`<br>
![Collapsible pane](./screenshots/collapsible-pane.png)

`WebAccordion`<br>
![Accordion](./screenshots/accordion.png)

`WebDateField` and `WebCalendar`<br>
![Date field and calendar](./screenshots/date-calendar.png)

`WebMemoryBar`<br>
![Memory bar](./screenshots/memory-bar.png)

`WebBreadcrumb`<br>
![Breadcrumb with custom content](./screenshots/breadcrumb-custom.png)<br>
![Breadcrumb with toggle buttons](./screenshots/breadcrumb-toggle.png)

`WebFileTree`<br>
![Asynchronous file tree](./screenshots/file-tree.png)

`WebColorChooserField`<br>
![Color chooser field](./screenshots/color-chooser-field.png)

`WebGradientColorChooser`<br>
![Gradient color chooser](./screenshots/gradient-color-chooser.png)

`WebStepProgress`<br>
![Step progress](./screenshots/step-progress.png)

You can find a lot more live examples in the demo application!


Important
----------
With [v1.27](https://github.com/mgarin/weblaf/releases/tag/v1.27) update core `StyleManager` functionality was added and implemented in a few WebLaF components so the way they act might differ from what you got used to. You might want to [read here](https://github.com/mgarin/weblaf/wiki/How-to-use-StyleManager) about features that `StyleManager` brings.

I will also be adding more articles into GitHub WIKI from now on, so you can find information about various WebLaF features there.


Artifacts
----------
You can always find all WebLaF artifacts in the "releases" section:<br>
https://github.com/mgarin/weblaf/releases

Here are the direct links for the latest release:

1. [**weblaf-1.27.jar**](https://github.com/mgarin/weblaf/releases/download/v1.27/weblaf-1.27.jar) - library jar
2. [**weblaf-simple-1.27.jar**](https://github.com/mgarin/weblaf/releases/download/v1.27/weblaf-simple-1.27.jar) - library jar without dependencies
3. [**weblaf-src-1.27.zip**](https://github.com/mgarin/weblaf/releases/download/v1.27/weblaf-src-1.27.zip) - project sources zip
4. [**weblaf-demo-1.27.jar**](https://github.com/mgarin/weblaf/releases/download/v1.27/weblaf-demo-1.27.jar) - executable demo jar
5. [**weblaf-javadoc-1.27.zip**](https://github.com/mgarin/weblaf/releases/download/v1.27/weblaf-javadoc-1.27.zip) - JavaDoc zip
6. [**ninepatch-editor-1.27.jar**](https://github.com/mgarin/weblaf/releases/download/v1.27/ninepatch-editor-1.27.jar) - executable 9-patch editor jar


Roadmap
----------
You can always check what fixes, features and improvements are coming by checking the milestones page:<br>
https://github.com/mgarin/weblaf/issues/milestones
I am not updating them very frequently, but they actually represent features I want to focus on.


Updates
---------
New WebLaF versions appear approximately every month.

Sometimes it might take less time if there are some small but critical issue fixes, sometimes it might take more time if I am going to release some large feature (like it was with `StyleManager`) as I have to modify/add a lot of code and consider a lot of stuff.

In any case WebLaF is not going to disappear anytime soon. Hopefully Swing won't disappear or become deprecated soon as well.


Building
----------
To build various WebLaF artifacts you will need [Java 1.6 (update 30 or later) or Java 1.7](http://www.oracle.com/technetwork/java/javase/downloads/index.html) and [Apache ANT] (http://ant.apache.org/).<br>
Simply run `ant` command within the "build" library folder to build all artifacts at once.

Here is a full list of usable ANT targets in WebLaF build script:

1. `build.artifacts` default target, **build all artifacts** at once
2. `build.weblaf.jar` **weblaf-x.xx.jar** - build WebLaF binary
3. `build.weblaf.simple.jar` **weblaf-simple-x.xx.jar** - build WebLaF binary without dependencies
4. `build.sources.zip` **weblaf-src-x.xx.zip** - build WebLaF sources zip archive
5. `build.npe.jar` **ninepatch-editor-x.xx.jar** - build 9-patch editor application
6. `build.weblaf.demo.jar` **weblaf-demo-x.xx.jar** - build WebLaF demo application
7. `build.javadoc` **weblaf-javadoc-x.xx.zip** & **javadoc** - create zipped and unzipped library JavaDoc
8. `run.npe` build and run ninepatch-editor-x.xx.jar - 9-patch editor application
9. `run.weblaf` build and run weblaf-x.xx.jar - library information dialog
10. `run.weblaf.demo` build and run weblaf-demo-x.xx.jar - demo application


Example Usage
----------
To install WebLaF you can simply call `install()` or use one of standard Swing L&F set methods:
```java
public class UsageExample
{
    public static void main ( String[] args )
    {
        // You should work with UI (including installing L&F) inside Event Dispatch Thread (EDT)
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

                // Create you application here using Swing components
                // JFrame frame = ...

                // Or use similar WebLaF components to use extended features
                // WebFrame frame = ...
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
