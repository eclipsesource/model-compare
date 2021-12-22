# Server

This java project bundles a headless jar, that triggers EMF Compare and provides the results in a way that the `comparison-extension` can display them.

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
