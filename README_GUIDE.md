# Base Principle
## Composition Theory.
Everything is based on composition theory. If you are not sure what todo, just obey the composition theory and  you will be alright.

For example, navigation is a Monad - a collection of screens that can be represented in notation as follows: `type Navigation = Collection<Screens>`.
```kotlin
@Composable
fun Navigation(
  navigationState: NavigationState,
  onNavigate: (NavigationRequest) -> Unit,
) {
  Navigator(
    route = navigationState.route
  ) {
    route("login") {
      LoginScreen(
        onSuccess = { token ->
          // request navigation to dashboard
          ...
        }
      )
    }
    route("dashboard) {
      Dashboard()
    }
  }
}
```
What needs to be noted in the code above is:

1. Navigation as a collection of screens.
2. Navigation determines the next route after successful login by injecting a lambda into the onSuccess parameter of the LoginScreen function.
3. The login page is unaware of the existence of the dashboard page, and vice versa. Each screen is unaware of and cannot see or depend on each other.

### Rule of Collection
Elements in a collection should not depends horizontally. For example, book1 should not depend on book2, screen1 should not depend on screen2, it just didn't makesense.
Horizontal dependencies will lead you to circular dependency problem.
Entity with same type should not depend on each other.

### No fancy pattern only monad
Each screen does not need to depend on other screens. Screens can be scaled horizontally without needing a complex dependency system.

Navigation acts as a monad that also regulates the screen cycle. This eliminates the need for dependencies between screens.

### If you going fancy, you are guilty
The entire application should be expressible in function composition notation.
For example: `App = Scaffold o Navigation o Screen o Widget o Component o ...`, don't be crazy with fancy pattern, just use composition.

## Library Based
No module can have a business context except the `composeApp` module; therefore, all modules other than `composeApp` will be referred to as "Library Modules."

Library is a collection; In this codebase, a library should only contain homogeneous collections.
For example: the `screen` module is a collection of screens.

Library Modules should not have instances within them.
Any instances such as Services should only exist within the `composeApp` module.

## Contextual programming.
A library can requires an interface but never a context. Library should not have business context.
You can use the context wrapping method to pass the interface from an instance with context to the function.

Example:
```kotlin
with(storageInterface) {
  saveToLocalStorage(file)
}
```
The `saveToLocalStorage(arg)` function is context-less; its intention is only to save a file. The function should not concern itself with context or have any context within it.

# Don't
## Local Properties & Build Variables
Learning from the mistakes of many companies, libraries should not be bound to local properties nor the Build Variables.
Only the composeApp module may access local properties.
Only the composeApp module may have a business context.

## Library Context
Libraries should not have business contexts. Library should be `Context Free`.
The reason why the composeApp module must provide MainContext and other contexts such as Api context, Storage context, and so on is because libraries should not have business contexts.
Only the MainContext class may be bound to the bussiness context.

If in case a function need context to run, you can use context receiver to deliver the context to the function.
```kotlin
with(MainContext.httpContext) {
  doHttpCall()
}

// in the code above, httpContext bound with business context such endpoints, interceptors etc.
// the `doHttpCall()` function don't need to know where is the endpoint api.
```

By doing that no function need to have context on their own.

## Context re-constructing
Libraries shouldn't ask how the context works.
Libraries should not deconstruct the context to obtain information from it.
Libraries should not re-create the context for their own.

## Transitive Dependency
Don't.

## Awesome dependency injection
Don't. You may disagree, but with composition theory, you don't need such sophisticated dependency injection. All those fancy features should be avoided.
