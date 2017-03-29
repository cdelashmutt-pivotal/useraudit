# CF User Audit
A small app to report on all users in a CF install.

## Building
Call `./gradlew assemble` at the root of the project.  The executable jar will be placed in the `build/libs` directory in the project.

## Running
First, either build the project, or download a release from the "Releases" link just above the files listing for the project in Github.

Next, after you have the executable jar, just run it using the following syntax (replacing the bogus parameters with the actual parameters for your CF install).

```commandline
$ java -jar useraudit-0.0.1-SNAPSHOT.jar --cf.apiHost=api.local.pcfdev.io --cf.username=SomePowerfulUser --cf.password=APassword
```

If you are running against a CF install with an untrusted certificate, you can skip SSL validation as follows:
```commandline
$ java -jar useraudit-0.0.1-SNAPSHOT.jar --cf.apiHost=api.local.pcfdev.io --cf.username=SomePowerfulUser --cf.password=APassword --cf.skip-ssl-validation=true
```

You will then see some status output, and then the results of the list of users in your CF install will show up, similar to the following.
```json
[ {
  "id" : "cb729a29-9cc0-4eb1-89f4-1fbc8e348a54",
  "name" : "system",
  "users" : [ {
    "name" : "push_apps_manager",
    "roles" : [ "org_user" ]
  }, {
    "name" : "admin",
    "roles" : [ "org_user" ]
  }, {
    "name" : "system_verification",
    "roles" : [ "org_user" ]
  } ],
  "spaces" : [ {
    "id" : "fa3cd80f-362d-499e-9c02-fdf467919954",
    "name" : "system",
    "users" : [ {
      "name" : "push_apps_manager",
      "roles" : [ "space_developer", "space_manager" ]
    } ]
  }, {
    "id" : "29cf9586-2f01-4a98-b7f5-dcf4f9ac1a78",
    "name" : "notifications-with-ui",
    "users" : [ {
      "name" : "admin",
      "roles" : [ "space_developer", "space_manager" ]
    } ]
  }, {
    "id" : "3cb2bcf0-62d3-4387-9437-929a5613bfd6",
    "name" : "brokers",
    "users" : [ {
      "name" : "admin",
      "roles" : [ "space_developer", "space_manager" ]
    } ]
  }, {
    "id" : "a778c0d7-36ea-4513-8be0-5de6ee2acd12",
    "name" : "autoscaling",
    "users" : [ {
      "name" : "admin",
      "roles" : [ "space_developer", "space_manager" ]
    } ]
  }, {
    "id" : "0454271b-c2a9-4eb6-a55e-5c253075d8fa",
    "name" : "pivotal-account-space",
    "users" : [ {
      "name" : "admin",
      "roles" : [ "space_developer", "space_manager" ]
    } ]
  }, {
    "id" : "7f805fad-4a94-4905-b858-010d26702e5d",
    "name" : "p-spring-cloud-services",
    "users" : [ {
      "name" : "admin",
      "roles" : [ "space_developer", "space_manager" ]
    } ]
  } ]
} ]
```

The output is formatted as Organizations, with the users with roles for each organization, the spaces for that organization, and all the users with roles for each space. 