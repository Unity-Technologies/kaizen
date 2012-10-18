# kaizen

A dependency management and continuous delivery framework for Unity extensions.

## Rationale

The underlying assumption is that by breaking system functionality into smaller units
that can be evolved and deployed independently of each other integration becomes simpler
because it can happen in small and localized steps.

The strategy is to automate to the fullest extent possible the whole delivery process
including the building and deployment of versioned binary components with explicitly
managed dependencies amongst them.

## In The Abstract

User visible functionality is grouped and delivered as coarse grained units called *bundles*. 

Bundles are versioned groupings of *components*. 

Components are versioned sets of files (such as a dotnet assembly and its xml documentation file for example) stored in *repositories*.

Repositories are directories of components, local or remote.

Components might declare dependencies on (versions of) other components.

The version number of a component is incremented every time a new version is published in accordance with [semantic versioning rules](http://semver.org/).

Components can and should be published independently of each other.

## In The Concrete

At the physical level there are only components distributed as zip
files together with version and dependency metadata.

Two types of components are defined at this point:

  1. A *bundle* is a component containing an informational text file
     (just so there's something to be zipped) and a set of dependencies on other
     components. A bundle's only purpose is to give a name to
     a set of component dependencies that together deliver a certain
     functionality (CodeEditor, for instance). Notice that bundles can
     also depend on other bundles allowing for arbitrary
     categorization of functionality.

  2. An *assembly* is a component containing a dotnet dll together with its xml
    documentation file.

These two are enough for the purpose of distributing managed
extensions to unity.

We're using [gradle] (http://www.gradle.org/) as the underlying infrastructure.

## The Publisher POV

  1. Declare dependencies.
  2. ./gradlew update (downloads and unpacks dependencies)
  3. Code something beautiful.
  4. ./gradlew publish (automatically increments version number)

## Handling UnityEngine.dll and UnityEditor.dll

Since those assemblies are strongly tied to the runtime they are
shipped with they are not subject to continous delivery and instead
have their versions pinned by the installation. They are still
considered backwards compatible so bundles developed against older
versions of them can still be installed.

## The User POV

Users install bundles into their projects.

When a bundle is installed or updated all transitive dependencies are
automatically resolved, downloaded and unpacked.

From the point of view of the user the current workflow goes like this:

 1. user imports [kaizen.unityPackage](https://github.com/downloads/Unity-Technologies/kaizen/kaizen.unityPackage) into the project
 2. user selects which bundles are to be installed (at the current stage by
    editing kaizen/kaizen.d/bundles.gradle and uncommenting
    the relevant lines)
 3. user asks kaizen to update the project with the selected bundles
    (atm by opening a shell in the kaizen directory and running './gradlew
    update' which causes all selected bundle dependencies to be
    resolved, downloaded and unpacked)

## Repositories are like channels

Even though we're starting with a single remote repository for bundles and
components the underlying infrastructure can handle any number of
repositories which could be leveraged to implement things like private
channels for paying customers, promotion workflows for components to
migrate from unstable/experimental/beta channels into stable ones, etc.

New releases of unity might ship with a private repository containing snapshots of
bundles promoted to 'builtin' status.

## Contributors

See: https://github.com/Unity-Technologies/kaizen/graphs/contributors
