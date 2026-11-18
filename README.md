# Jenkins Emotional Plugin

[![Jenkins Plugin](https://img.shields.io/jenkins/plugin/v/emotional-jenkins-plugin?label=Jenkins%20Plugin)](https://plugins.jenkins.io/emotional-jenkins-plugin/)
[![Jenkins Plugin Installs](https://img.shields.io/jenkins/plugin/i/emotional-jenkins-plugin?color=blue)](https://plugins.jenkins.io/emotional-jenkins-plugin/)
[![GitHub Issues](https://img.shields.io/github/issues/jenkinsci/emotional-jenkins-plugin)](https://github.com/jenkinsci/emotional-jenkins-plugin/issues)
[![GitHub Pull Requests](https://img.shields.io/github/issues-pr/jenkinsci/emotional-jenkins-plugin)](https://github.com/jenkinsci/emotional-jenkins-plugin/pulls)
[![License](https://img.shields.io/github/license/jenkinsci/emotional-jenkins-plugin)](https://github.com/jenkinsci/emotional-jenkins-plugin/blob/master/LICENSE)

<a href="https://jenkins.io">
    <img width="400" src="https://www.jenkins.io/images/jenkins-logo-title-dark.svg" alt="Jenkins logo"> 
</a>

## What is it

Emotional Jenkins is a fun Jenkins plugin that changes Mr. Jenkins' background expression based on the result of your builds.
When a build fails, Jenkins gets angry. When a build is unstable, he looks sad. And when everything goes well, he keeps his usual smile.

A little personality for your Jenkins dashboard!

![alt text](image.png)


## Table of Contents

- [Configuration](#configuration)
- [Expressions](#expressions)
  - [Successful Build](#successful-build)
  - [Unstable Build](#unstable-build)
  - [Failed Build](#failed-build)
- [Requirements](#requirements)
- [Contributing](#contributing)
- [License](#license)

## Configuration 

To enable Emotional Jenkins for a job:

1. Open your Jenkins job.
2. Click **Configure**.
3. Navigate to the **Post-build Actions** section.
4. Click **Add post-build action**.
5. Select **Emotional Jenkins**.
6. Save your configuration.

Once enabled, Emotional Jenkins automatically updates Mr. Jenkins' background expression based on the build result.

## Expressions

Emotional Jenkins changes Mr. Jenkins' appearance according to the outcome of your builds.

### Successful Build

When a build succeeds, Mr. Jenkins keeps his happy expression.

![Happy Jenkins](src/main/webapp/images/jenkins.png)

### Unstable Build

When a build is unstable, Mr. Jenkins looks sad.

![Sad Jenkins](src/main/webapp/images/sad-jenkins.png)

### Failed Build

When a build fails, Mr. Jenkins gets angry.

![Angry Jenkins](src/main/webapp/images/angry-jenkins.png)

## Requirements

- Java 25 or later.
- Jenkins compatible with the plugin's configured Jenkins baseline.

## Contributing

Contributions are welcome!

1. Fork the repository.
2. Create a branch for your changes.
3. Make your changes and add tests when applicable.
4. Run the test suite using `mvn clean verify`.
5. Submit a pull request with a clear description of your changes.

Please ensure that your changes follow the project's coding standards.

## License

This project is licensed under the [MIT License](LICENSE).