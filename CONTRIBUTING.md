# Contributing
Open for contribution.

## Pattern convention
Designed by [Stefanus Ayudha](https://github.com/stefanusayudha).

The `base` branch serves as the pattern convention for this application. It represents the minimum example of the application and is used as the standard pattern for contributors.

Every contributor is required to adhere to this established pattern and architecture.

Please note that this pattern and architecture are original designs created by me, [(Steve)](https://github.com/stefanusayudha), and are not intended for public use.

If you choose to use this pattern without my knowledge, then all the risk is yours, and it is out of the creator's responsibility.

## Git flow
See: [Singularity Gitflow - Miro.](https://miro.com/app/board/uXjVMS5Omk8=/?share_link_id=784438148126)

## Commit convention
- fix($scope): $message.

  Used to commit a bug fix.
- feat($scope): $message.

  Used to commit a feature.
- BREAKING CHANGE($scope): $message.

  Used to commit breaking changes (rename, replace, move, delete, etc.) in the context of "not bug fixing".

**Note: Committing changes with side effect**<br/>
If your changes somehow affecting another module that is not associated with your module / scope, and somehow you cannot separate them to separated commits, you must add `!` at the beginning of your commit message and add `ALSO:` to provides the effects.

eg:
```
!fix(LoginScreen): add error handler for MCancelationException.
ALSO:
- ($scope): $changes.
- ($scope): $changes. 
```

**Important** : You must commit load resources first separately and rebase the load resources to the bottom of commit orders.
As how the development process explain by the git flow:
1. `feat($scope): Init.`
2. `feat($scope): Load Resources.`
3. `.... and next.`

Resources category includes: Strings, Icons.
