# Contributing
Open for contribution.

## Registration
Contact me via [email: stefanus.ayudha@gmail.com](mailto:stefanus.ayudha@gmail.com) with the following format:<br/>
```markdown
Subject: 
I want to contribute to Singularity Mobile APP!

Body:
Github user name: John Doe.
Task I want to take: [link to task].
```

## Project
Brows the task you want to take here:
[Singularity Project](https://github.com/orgs/SingularityIndonesia/projects/1).
You can take any task available that is ready.

## Pattern convention
Designed by [Stefanus Ayudha](https://github.com/stefanusayudha).

The `base` branch serves as the pattern convention for this application. It represents the minimum example of the application and is used as the standard pattern for contributors.

Every contributor is required to adhere to this established pattern and architecture.

## Git flow
![Singularity GitFlow - Gitflow.jpg](Docs%2FSingularity%20GitFlow%20-%20Gitflow.jpg)
See: [Singularity Gitflow - Miro.](https://miro.com/app/board/uXjVMS5Omk8=/?share_link_id=784438148126)

## Commit convention
- `fix($scope): $message.`

  To commit a bug fix.
- `feat($scope): $message.`

  To commit a feature.
- `BREAKING CHANGE($scope): $message.`

  To commit breaking changes (rename, replace, move, delete, etc.) in the context of "not bug fixing".
- `doc($scope): $message.`

  To commit documentation.

**Note: Committing changes with side effect**<br/>
If your changes somehow affecting another module that is not associated with your module / scope, and somehow you cannot separate them to separated commits, you must add `!` at the beginning of your commit message and add `ALSO:` to provides the effects.

eg:
```
!fix(LoginScreen): add error handler for MCancelationException.
ALSO:
- ($scope): $changes.
- ($scope): $changes. 
```

Resources category includes: Strings, Icons.

## Commit Procedure
Commit the lowest module first.

1. Set the commit view into module.

   ![Screenshot 2024-03-10 at 19.14.55.png](Docs%2FScreenshot%202024-03-10%20at%2019.14.55.png)
2. Commit modules in order from the lowest to highest.

   ![Screenshot 2024-03-10 at 19.19.43.png](Docs%2FScreenshot%202024-03-10%20at%2019.19.43.png)

3. Commit only what is necessary.

   ![Screenshot 2024-03-10 at 19.21.44.png](Docs%2FScreenshot%202024-03-10%20at%2019.21.44.png)

## FAQ
- `#MissingSdk`, `#MissingLocalProperties`, `#LibraryIsNotAnAndroidModule`.

  These issues are known to occur on Windows machines. You need to create a symbolic link from your `local.properties` file to the `Library/` directory. 
  
  **Warning**: Copying the `local.properties` file to the Library project is not recommended.
- `#LaunchError`, `#NoDefaultActivity` `#LaunchingExampleActivity`. To launch the example activity you need to edit launch configuration to launch to a Specific Activity, and set the target to ExampleActivity.
  see: [Launching Specific Activity](https://developer.android.com/studio/run/rundebugconfig?hl=id#:~:text=%3C/intent%2Dfilter%3E-,Specified%20Activity,-%2D%20Meluncurkan%20aktivitas%20aplikasi).