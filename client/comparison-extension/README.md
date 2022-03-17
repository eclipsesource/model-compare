# Comparison Extension

A Theia extension that retrieves the comparison result from the `emf-compare-adaptor` and displays the result in a tree or optionally delegates the differences to a graphical view provided by the user.

## Using this extension

To configure this extension a custom [`ComparisonExtensionConfiguration`](./src/browser/comparison-extension-configuration.ts) must be bound.
At a minimum a `fileExtension`, a `ServerJar` (path to server jar), a `ModelJarPath` (path to the EMF model jar) and a `ModelPackageName` (the package name of the specified EMF model) must be bound.
To enable the graphical comparison:

- The `supportGraphicalComparison` needs to be set to `true` in the custom `ComparisonExtensionConfiguration`
- And a [`GraphicalComparisonOpener`](./src/browser/graphical/graphical-comparison-opener.ts) needs to be bound.
