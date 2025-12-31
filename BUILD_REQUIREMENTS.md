# Build Requirements

## Java Version

This project requires **Java 21** for building and running.

### Check Java Version

```bash
java -version
# Should show: openjdk version "21.x.x" or similar
```

### Install Java 21

#### Using SDKMAN (Recommended)
```bash
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk install java 21.0.1-tem
sdk use java 21.0.1-tem
```

#### Using Package Manager

**macOS (Homebrew)**:
```bash
brew install openjdk@21
```

**Ubuntu/Debian**:
```bash
sudo apt update
sudo apt install openjdk-21-jdk
```

**Windows**:
Download from [Adoptium](https://adoptium.net/)

### Verify Installation

```bash
java -version
mvn -version
```

Both should show Java 21.

## Build Project

Once Java 21 is installed:

```bash
# Build all modules
mvn clean install

# Or use build script
./scripts/build-all.sh
```

## Note

The project was designed with Java 21 to leverage:
- Virtual Threads (Project Loom)
- Pattern Matching enhancements
- Record Patterns
- Latest performance improvements
