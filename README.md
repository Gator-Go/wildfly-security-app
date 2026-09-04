
# WildFly Security App

Prototype. Factory-generated Security app from wildfly-builder.

Catalog: https://sw-builder.com/appstore/wildfly/apps/wildfly-security-app.html

Builder: https://github.com/Gator-Go/wildfly-builder

Live demo: https://sw-builder.com/security/do?op=Home  
Sign in with `guest` / `guest`.

## Build (Unix)

Prerequisites: Git, Groovy, JDK, Maven, WildFly.

Expected sibling directories:

    ~/wildfly/wildfly-builder
    ~/wildfly/wildfly-security-app

```bash
cd ~/wildfly/wildfly-security-app
git pull
./wildfly-security-build-deploy.sh
```
## Layout:
```text
wildfly-security-app/
├── wildfly-security-build-deploy.sh
├── Extender/
│   ├── SecurityExtender.groovy
│   └── CheckMaxVideosObserver.java
├── options/
│   ├── APP_CODE_TYPES.xml
│   ├── APP_ENUMS.xml
│   ├── APP_EVENTS.xml
│   ├── APP_HOME.xml
│   ├── APP_NAMES.xml
│   └── APP_TABLES.xml
└── security_logo.png
```
## Note:

template/ and build/ appear after a build. They come from wildfly-builder.

WildFlyBuilder.groovy, updateAppsList.groovy, and update_and_commit.sh
are copied in from wildfly-builder at build time.

SecurityExtender.groovy performs functions unique to the security app.
CheckMaxVideosObserver.java is a security-app extension class.

The security/ dir appears after a build and is the build output where
the new app is created.