# Git Best Practices for Java Monorepo

This document outlines best practices for working with this Java monorepo using Git.

## Commits

- Make atomic commits (one logical change per commit)
- Write meaningful commit messages:
  - Use the imperative mood ("Add feature" not "Added feature")
  - First line should be 50 characters or less
  - Include a more detailed description after a blank line if necessary

Example:
```
Add user authentication feature

- Implement JWT token generation
- Create login endpoint
- Add password hashing utility
```

## Branches

- Use feature branches for new development
- Name branches with a prefix indicating the type of change:
  - `feature/` for new features
  - `bugfix/` for bug fixes
  - `hotfix/` for critical fixes
  - `release/` for release preparation
  
Example: `feature/user-authentication`

## Pull Requests

- Create Pull Requests for code reviews
- Reference issues in PR descriptions
- Ensure all CI checks pass before merging
- Use squash merging to keep the history clean

## Working with the Monorepo

- Only commit source code, not build artifacts
- Run `mvn clean` before committing to ensure no build artifacts are included
- When making changes to shared modules (common-lib, service-api), ensure all dependent modules still build
- Use `git add -p` to selectively stage changes for more precise commits

## Git Hooks

Consider adding git hooks to:
- Run tests before pushing
- Validate code formatting
- Check for common issues

You can add hooks by creating scripts in the `.git/hooks` directory.

## Continuous Integration

This repository uses GitHub Actions workflows located in `.github/workflows/` to:
- Build the project
- Run tests
- Validate code quality
- Create releases

Ensure your changes pass all CI checks before merging.
