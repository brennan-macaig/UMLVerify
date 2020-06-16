# UMLVerify Plugin


### Building

To build the plugin, ensure you have Maven installed and on your system path.

Rename `src/main/resources/config.TEMPLATE.yml` to `src/main/resources/config.yml`, then run

```
mvn install
```

This will install the plugin and all dependencies. It will deposit two JAR files in `target/`:

- UMLVerify-1.15.2.jar - this jar should not be used on the server, and is just the plugin without any additional APIs.
- UMLVerify-1.15.2-jar-with-dependencies.jar - This is the plugin that should be run on a server.

For development reasons, I recommend that you set up a PaperSpigot server. This can be done by following [these instructions.](https://paper.readthedocs.io/en/latest/server/getting-started.html)

Of course, after any build and before the next build, it's probably a good idea to run `mvn clean` which will clean the `target/` directory.