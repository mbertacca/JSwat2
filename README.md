 JSwat README

JSwat README
============

What is it?
-----------

JSwat is a graphical Java debugger front-end, written for the Java 2 platform, utilizing the [Java Platform Debugger Architecture](http://java.sun.com/products/jpda/) (JPDA). The source code is licensed under the [GNU General Public License](http://www.gnu.org/copyleft/gpl.html). This version of the software has entered maintenace mode. Generally only bug fixes are being made to this line of the code. See the [web site](http://www.bluemarsh.com/java/jswat/) for the latest version of the debugger.

Requirements
------------

### J2SE

JSwat requires the [Java 2 Standard Edition](http://java.sun.com/j2se/), version 1.4 or higher. Compatible software from other vendors may be used, if so desired.

### JPDA

The Java Platform Debugger Architecture (JPDA) software, which is included in most releases of the J2SE, is utilized by JSwat to carry out its debugging activities. The required classes are typically found in the `<jdk>\lib\tools.jar` file, and begin with the prefix `com.sun.jdi` (where JDI stands for Java Debug Interface).

### Other Libraries

JSwat includes additional jar files containing libraries that are utilized by the program. They are referenced by the manifest of the JSwat jar file and must be kept in the same directory as the JSwat jar file. The jar files do not have to be in your classpath, though it would not hurt if they were.

### Debug Info

JSwat, like most Java debuggers, requires that the classes to be debugged be compiled using the \-g flag. In addition, do not compile the classes with the optimization flags, as that may hinder debugging. If you are using [Ant](http://ant.apache.org/) to build your application, include `debug="on"` in the `javac` task, which will generate the debug info during compilation.

Starting JSwat
--------------

There are basically two ways to start JSwat: easy and not so easy. If you are running Microsoft Windows then you can use the **`jswat.bat`** batch file. If you are running a Unix-like system then you can use the **`jswat`** shell script. Otherwise, you can start JSwat **manually**, as shown below.

java -Djava.ext.dirs=<JAVA\_HOME>\\lib -jar <JSWAT\_HOME>\\jswat.jar

_Unix users must replace \\ with / in the line above._

_Mac OS X users can leave out the -D option entirely._

Note that `JAVA_HOME` is the path to the JDK, while `JSWAT_HOME` is the path to the `jswat.jar` file. The `<JAVA_HOME>\lib` directory must contain the `tools.jar` file in order for JSwat to start. This file contains the JPDA classes (`com.sun.jdi`) used by JSwat. If the file containing these classes is located elsewhere in your system, you must make the appropriate change to the command used to invoke JSwat.

### Passing Arguments

JSwat can accept a string of commands to be executed as soon as it has completed its startup. These commands are passed at the end of the command line used to start the program. Here are some examples:

*   java ... jswat.jar load -client MyClass arg1 arg2
*   java ... jswat.jar attach mymachine:1234
*   java ... jswat.jar "load MyClass; stop MyClass:120; resume"
*   java ... jswat.jar \\"classpath cp1;cp2;cp3\\"

Read the help section titled "Commands" to learn more about using JSwat commands. Note that the last example uses slashes to escape the quotes used on the command line. See the appropriate documentation for your command shell to learn more.

### Other Ways to Start JSwat

1.  As an alternative to using the Java Extensions Mechanism, you may append to the boot classpath, like so:
    
    java -Xbootclasspath/a:<JAVA\_HOME>\\lib\\tools.jar -jar <JSWAT\_HOME>\\jswat.jar
    
2.  For a shorter invocation, list the aforementioned `tools.jar` file and the JSwat jar files in your classpath and invoke the program with this simple command:
    
    java com.bluemarsh.jswat.Main
    
3.  Another short invocation can be accomplished by copying the `tools.jar` file to the `<JAVA_HOME>\jre\lib\ext` directory, then starting JSwat like so:
    
    java -jar <JSWAT\_HOME>\\jswat.jar
    
    Note that the \-jar option causes the classpath setting to be ignored. This includes the path following the \-cp or \-classpath options, as well as the `CLASSPATH` environment variable.
    

Documentation
-------------

The JSwat documentation is made up primarily of the built-in help. Help on JSwat commands is accessed using the "help" command. General help topics are available from the "Help" menu in JSwat. Additionally, there are several "how to" documents on the [web site](http://www.bluemarsh.com/java/jswat/docs/).

Stopping JSwat
--------------

The correct way to exit JSwat is with either the window close button, the exit command, or the Exit item in the File menu. Using Ctrl-c to exit JSwat will likely cause problems and should be avoided. It must _only_ be used in cases where the JVM hosting JSwat has frozen.

Removing JSwat
--------------

Removing JSwat from your system is generally pretty simple. Find the files, wherever it is that you originally put them, and delete them. If, on the other hand, you used one of the packaged versions such as RPM or DEB, you will need to remove the package using the appropriate system tool.

To remove all traces of JSwat from your system, you will need to look in only one place. If you are using Microsoft Windows, look in the registry under the `HKEY_CURRENT_USER\Software\JavaSoft\Prefs\com\bluemarsh\jswat` key. Users of Unix-like systems will find the JSwat settings stored in the `~/.java/.userPrefs/com/bluemarsh/jswat/` directory. You may also have created a `jswat.init` file, so be sure to look for that as well.

Platform particulars
--------------------

Below are notes concerning particular platforms and what must be done to make JSwat work correctly on them.

### Mac OS X

JSwat is known to work on Mac OS X 10.3 with JDK 1.4.1.

David Cok reports that on OS X 10.1.5 he was unable to launch the debuggee from JSwat, although he could remotely attach to a running debuggee. But as he points out, this is true for all of the Java debuggers he tested on this platform.

Marc Hadley, using OS X 10.1.5, says it was necessary for him to add the hostname of his machine to the `netinfo` file. Otherwise he got an error about `gethostbyname` when launching the debuggee from within JSwat.

### Windows

The JRE, often installed with the JDK, seems to cause more problems than it solves. If JSwat tells you that the JPDA cannot be found and you are certain you followed the instructions above correctly, then chances are the JRE is getting in the way. Be sure that you refer to the `java.exe` located in the JDK `bin` directory. Otherwise you may inadvertently invoke the JRE which is not suitable for debugging.

### Windows NT and JDK 1.4

There seems to be a problem with using JDK 1.4 on Windows NT 4.0, relating to the DirectX features. The errors occur in the method `sun.awt.windows.Win32DDRenderer.doFillRectDD`. The solution appears to be quite simple, just add the following option after java when starting JSwat: \-Dsun.java2d.noddraw=true

### Windows 2000/XP and JDK 1.4

There seems to be a problem with using JDK 1.4.1 on Windows 2000 and XP relating to certain video cards. Basically, when the Java application exits, the operating system hangs. The relevant bug report is on the [developer services](http://developer.java.sun.com/developer/bugParade/bugs/4713003.html) site, which you may want to read for further information. There is a work-around that seems to work for some users and is worth trying. Add the following option after java when starting JSwat: \-Dsun.java2d.d3d=false

A problem related to the one described above is that the JSwat application seems to hang on startup. Try installing the DirectX support in your system to avoid the issue. If that does not work, try the option mentioned above, or replace your video card per the bug report mentioned above.

Troubleshooting
---------------

Below are problems you might run into while using JSwat. Each offers a solution for either resolving or working around the issue.

### Unable to find JPDA

See instructions above and be sure to follow them carefully. Check for any relevant information in the "Platform Particulars" section.

### prefs BackingStoreException

See the "Preferences Do Not Persist" item.

### Upgrading to 2.0 message keeps appearing

See the "Preferences Do Not Persist" item.

### Preferences Do Not Persist

If the preferences in JSwat are not persisting, you may see one of the following symptoms:

*   "Updating to 2.0" dialog always displays at startup.
*   `java.util.prefs.BackingStoreException` occurs, maybe mentioning something about an XML parser, such as xerces.

The cause of the problem seems to be that an alternate XML parser is in the classpath. This XML parser does not understand how to handle the JavaSoft Preferences DTD and thus fails to read or write the Preferences data.

The solution is to run JSwat using the \-jar option to provide a clean environment in which to run JSwat.

Simon Hill pointed out that the 2.1 version of the xerces parser no longer has the problem described above. If you are using an older version of this parser, you may want to upgrade.

Getting help
------------

Write an email to the [author](mailto:nfiedler@bluemarsh.com), or join the `jswat-user` [mailing list](http://www.bluemarsh.com/java/jswat/mail-lists.html) and post your question there.

JSwat and related documentation are Copyright © 1999-2005
