# Quarkus - JGraphT

[![Version](https://img.shields.io/maven-central/v/io.quarkiverse.jgrapht/quarkus-jgrapht?logo=apache-maven&style=for-the-badge)](https://search.maven.org/artifact/io.quarkiverse.jgrapht/quarkus-jgrapht)

<!-- ALL-CONTRIBUTORS-BADGE:START - Do not remove or modify this section -->
[![All Contributors](https://img.shields.io/badge/all_contributors-3-orange.svg?style=flat-square)](#contributors-)
<!-- ALL-CONTRIBUTORS-BADGE:END -->

## Welcome to Quarkus - JGraphT extension!

This is the Quarkus extension for [JGraphT](https://jgrapht.org/).

Vanilla JGraphT works well in the JVM mode and reasonably well in the native mode.

Quarkus JGraphT extension ensures proper behavior in native mode for:
- jgrapht-io
  - CSV import / export
  - DIMACS import / export
  - DOT import / export
  - GEXF import / export
  - GraphML import / export
  - JSON import / export
  - Matrix export
- jgrapht-opt
  - Fastutil graphs
  - Sparse graphs
- jgrapht-unimi-dsi
  - Succinct graphs
  - WebGraph graphs

## Coordinates

```xml
<dependency>
    <groupId>io.quarkiverse.jgrapht</groupId>
    <artifactId>quarkus-jgrapht</artifactId>
    <version>LATEST</version>
</dependency>
```
## Quarkus ecosystem CI
 - Overall status:
   - https://status.quarkus.io/
 - Issue to track Quarkus JGraphT:
   - https://github.com/quarkiverse/quarkiverse/issues/38
 - CI definition:
   - `quarkiverse-jgrapht` directory in https://github.com/quarkusio/quarkus-ecosystem-ci
   - [.github/workflows/quarkus-snapshot.yaml](.github/workflows/quarkus-snapshot.yaml) in this repository

[comment]: <> (## Documentation)

[comment]: <> (The documentation for this extension should be maintained as part of this repository and it is stored in the `docs/` directory. )

[comment]: <> (The layout should follow the [Antora's Standard File and Directory Set]&#40;https://docs.antora.org/antora/2.3/standard-directories/&#41;.)

[comment]: <> (Once the docs are ready to be published, please open a PR including this repository in the [Quarkiverse Docs Antora playbook]&#40;https://github.com/quarkiverse/quarkiverse-docs/blob/main/antora-playbook.yml#L7&#41;. See an example [here]&#40;https://github.com/quarkiverse/quarkiverse-docs/pull/1&#41;.)
## Contributors ✨

Thanks goes to these wonderful people ([emoji key](https://allcontributors.org/docs/en/emoji-key)):

<!-- ALL-CONTRIBUTORS-LIST:START - Do not remove or modify this section -->
<!-- prettier-ignore-start -->
<!-- markdownlint-disable -->
<table>
  <tr>
    <td align="center"><a href="https://twitter.com/r_svoboda"><img src="https://avatars.githubusercontent.com/u/925259?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Rostislav Svoboda</b></sub></a><br /><a href="https://github.com/quarkiverse/quarkus-jgrapht/commits?author=rsvoboda" title="Code">💻</a> <a href="#maintenance-rsvoboda" title="Maintenance">🚧</a></td>
    <td align="center"><a href="http://gastaldi.wordpress.com"><img src="https://avatars.githubusercontent.com/u/54133?v=4?s=100" width="100px;" alt=""/><br /><sub><b>George Gastaldi</b></sub></a><br /><a href="#infra-gastaldi" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a></td>
    <td align="center"><a href="https://github.com/gsmet"><img src="https://avatars.githubusercontent.com/u/1279749?v=4?s=100" width="100px;" alt=""/><br /><sub><b>Guillaume Smet</b></sub></a><br /><a href="#infra-gsmet" title="Infrastructure (Hosting, Build-Tools, etc)">🚇</a></td>
  </tr>
</table>

<!-- markdownlint-restore -->
<!-- prettier-ignore-end -->

<!-- ALL-CONTRIBUTORS-LIST:END -->

This project follows the [all-contributors](https://github.com/all-contributors/all-contributors) specification. Contributions of any kind welcome!