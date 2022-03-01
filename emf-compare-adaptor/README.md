# Server

This java project bundles a headless jar that triggers EMF Compare and provides the results in a way that the `comparison-extension` can display them.

## Bundle the jar

Currently the jar can only be exported from Eclipse.

1. Open this project in Eclipse as a Maven project
2. Run the `ModelCompare` class as a Java Application (this will create a launch config)
3. Right-click the project -> Export -> Runnable Jar File -> Choose the launch config and destination

## Debug the jar

You can debug the server jar by starting the jar with the following arguments:

```command
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1044 -jar server.jar
```

So for example:

```command
java -Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=1044
    -jar server.jar
    -source file:///<source-file-path>
    -model <coffee-model-jar-path>
    -package org.eclipse.emfcloud.coffee.CoffeePackage
    -target file:///<target-file-path>
    -operation comparison
    -base file:///<base-file-path>
```

The jar will then wait for a debugger to connect, before executing the code.
You can use the `model_comparison.launch` config in Eclipse to connect to the application.
