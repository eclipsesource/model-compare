# model-compare

A model comparison framework for EMF.cloud

> **_Disclaimer_**
> This project is a Proof of Concept for how to enable EMF Compare in a Theia application. It is not in a state to consume it and/or use it in production.
> The project is the result of a student thesis, is provided as is and will not be maintained actively for now. If you are interested in a stable version of this feature, please get in [contact with us](https://www.eclipse.org/emfcloud/contact/).

## Project structure

This project contains:

- [emf-compare-adaptor](./emf-compare-adaptor): a Java-based adaptor of [EMFCompare](https://www.eclipse.org/emf/compare/)
- [client/comparison-extension](./client/comparison-extension/): a Theia extension, that displays the results provided by the adaptor
- [client/examples](./client/examples/): a collection of example use cases (currently limited to a tree-based Coffee model comparison)

## Contributing

Like stated above, this project will not be actively maintained, as it is intended as a demo only. However, if you want to contribute a fix or raise an issue, feel free to do so.
