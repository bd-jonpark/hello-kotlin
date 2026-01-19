# Hello Kotlin

Kotlin test project with intentional security defects for Coverity SDLC testing.

## Intentional Defects

- SQL Injection
- Command Injection
- Path Traversal
- Hardcoded Credentials
- Resource Leak
- Null Pointer Dereference
- Division by Zero
- Weak Cryptography (MD5)
- Insecure Random

## Build

```bash
./gradlew build
```

## Coverity Scan

```bash
cov-build --dir idir ./gradlew build
cov-analyze --dir idir
cov-commit-defects --dir idir --url $COV_URL --stream hello-kotlin
```
