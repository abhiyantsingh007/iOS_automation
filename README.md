# eGalvanic Mobile Automation

iOS automation framework for eGalvanic app using Appium + Java + TestNG

## 📋 Prerequisites

- ✅ macOS with Xcode installed
- ✅ Java 17
- ✅ Maven
- ✅ Node.js
- ✅ Appium 3.x
- ✅ XCUITest driver

## 🚀 Quick Start

### Step 1: Start Appium Server
```bash
appium
```

### Step 2: Boot iOS Simulator
```bash
xcrun simctl boot "iPhone 17 Pro"
open -a Simulator
```

### Step 3: Place your eGalvanic.app file
Copy your `eGalvanic.app` or `eGalvanic.ipa` to your Desktop:
```
/Users/abhiyantsingh/Desktop/eGalvanic.app
```

### Step 4: Update App Path in LaunchAppTest.java
Edit `src/test/java/com/egalvanic/tests/LaunchAppTest.java`:
```java
private static final String APP_PATH = "/Users/abhiyantsingh/Desktop/eGalvanic.app";
```

### Step 5: Run the Test
```bash
cd eGalvanic-Automation
mvn clean test -Dtest=LaunchAppTest
```

## 📁 Project Structure

```
eGalvanic-Automation/
├── pom.xml                           # Maven dependencies
├── src/
│   ├── main/java/com/egalvanic/
│   │   ├── pages/                    # Page Object classes
│   │   ├── utils/                    # Utility classes
│   │   └── constants/                # Constants
│   └── test/java/com/egalvanic/
│       └── tests/
│           └── LaunchAppTest.java    # First test - launch app
├── config/                           # Configuration files
└── apps/                             # Place .app/.ipa files here
```

## 🧪 Running Tests

### Run single test:
```bash
mvn test -Dtest=LaunchAppTest
```

### Run all tests:
```bash
mvn clean test
```

## 📱 Simulator Commands

### List simulators:
```bash
xcrun simctl list devices
```

### Boot simulator:
```bash
xcrun simctl boot "iPhone 17 Pro"
```

### Shutdown simulator:
```bash
xcrun simctl shutdown "iPhone 17 Pro"
```

## 🔧 Troubleshooting

### Appium not connecting
- Make sure Appium server is running: `appium`
- Check port 4723 is available

### App not installing
- Verify .app/.ipa file path is correct
- For .ipa files, may need to be signed for simulator

### Element not found
- Use Appium Inspector to find correct locators
- Wait for element to be visible before interacting
