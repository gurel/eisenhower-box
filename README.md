# Eisenhower Box
This is a sample todo app.

## Prerequisites
- node
- npm
(activator should take care of rest)

# Install
Pull files from git and then call:

> npm install

To Start the server:
> activator run

This will actually trigger a **npm** build action and start the local **dynamodb** instance.

Username: admin@admin.com

Password: admin

You can also find a register link on the login page to create more users. There is nothing special about the admin user.
It's only initial user the system automatically creates.


### Resources

- https://github.com/akka/akka/tree/master/akka-samples

- https://www.lightbend.com/activator/templates#filter:java8

- playframework official site

- https://docs.angularjs.org


### Note to self
- Check how to  Dev and Prod builds separately
- Check Akka remoting and configuration
- Akka supervision
- Add Testing
- dynamoDB must manually be started as Playhooks don't work on test

### Captains Log
- New Project
- Created a webpack setup in npm with Babel ES6 support (output 3 files app.js, vendor.js, style.css), didn't actually go into less/sass yet only plain css (see: webpack.config.js)
- Attached npm scripts to SBT in order to build and watch for changes (hot-loading)
- start with directives
- scoped style and templating
- get bootstrap as external dep.
- Build matrix DOM
- play with scope
- generalization of directive creation
- Started play controllers
- Configured IntelliJ to debug
- Started to look at akka for non-blocking functionalities
- Created model class for Task
- Created task actors
- Created task dao
- checked how to do callbacks in controllers (java8 rules!)
- started to look ad DynamoDB for persistant storage
- checked how to create and use Java annotations (syntax has changed since i last used java)
- Started to implement annotations to support dynamodb
- Found that aws sdk has already implemented annotations and mapping for dynamodb, so switched to that.
- Started implementing TaskDAO to use actors so i can perform non-blocking DynamoDB actions.
- Install and attach DynamoDB to SBT
- Configured /gitignore
- Implement DynamoDb on actors
- Add service to angular to do task operations
- Add status to task (BE and FE)
- Angular routing to add a settings screen
- Implemented service and model for User
- User Authentication to Play, uses sessions.
- Play Twirl templating for login and register pages
