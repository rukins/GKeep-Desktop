# GKeep Desktop. Unofficial desktop client for Google Keep Notes

![Logo](./src/jvmMain/resources/logo/logo.png)

## Table of content
- [General info](#general-info)
- [Build](#build)
- [Report Issues](#report-issues)
- [Contributing](#contributing)
- [License](#license)

## General info
Multiplatform desktop client application for Google Keep Notes that is built on [Compose Multiplatform](https://github.com/JetBrains/compose-multiplatform)
and has beautiful Material3 Design by Google from Android. \
The app uses [this library](https://github.com/rukins/gkeepapi-java) to get access data from the Android Google Keep API and [this one](https://github.com/rukins/gpsoauth-java) to log in to Google account,
so you need receive an **authentication token** to get started with your Google account. \
The guide available on [this page](https://github.com/rukins/gpsoauth-java) will help you.

## Build
You can build the application for Linux, Windows or macOS just by one command, 
but there is no cross-compilation support available at the moment, so the formats can only be built using the specific OS.

***Also, to package you need to install the required software:***

Linux: deb, rpm:
- For Red Hat Linux, the rpm-build package is required.
- For Ubuntu Linux, the fakeroot package is required.

macOS: pkg, app in a dmg
- Xcode command line tools are required when the --mac-sign option is used to request that the package be signed, and when the --icon option is used to customize the DMG image.

Windows: exe, msi
- WiX 3.0 or later is required.

**Build for Linux**
```
./gradlew packageRpm
./gradlew packageDeb
```

**Build for Windows**
```
./gradlew.bat packageMsi
```

**Build for macOS**
```
./gradlew packageDmg
```

The installation files will be available in `build/compose/binaries/main` folder.

**For more information see [Packaging Overview From Oracle Docs](https://docs.oracle.com/en/java/javase/17/jpackage/packaging-overview.html)
and [Building a native distribution Guide](https://github.com/JetBrains/compose-multiplatform/tree/master/tutorials/Native_distributions_and_local_execution)**

## Report Issues
In the [Issues](https://github.com/rukins/GKeep-Desktop/issues) section you can suggest any improvements and report any bugs you find

## Contributing
This is an open-source project and all contributions are highly welcomed

## License
Released under [MIT License](LICENSE)