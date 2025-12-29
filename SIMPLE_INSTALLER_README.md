# Simple App Installer

This is a simple, error-free Appium script to install and open an iOS app.

## Prerequisites

1. **Install Appium Server**:
   ```bash
   npm install -g appium
   ```

2. **Start Appium Server**:
   ```bash
   appium
   ```
   Make sure the server is running at `http://localhost:4723`

3. **iOS Simulator**:
   - Make sure you have an iOS simulator running
   - Update the device name, platform version, and UDID in the code

## How to Run

1. **Update the app path** in `SimpleAppInstaller.java`:
   ```java
   String appPath = "/path/to/your/app/file.ipa"; // Path to your .ipa or .app file
   ```

2. **Update device configuration** in `SimpleAppInstaller.java`:
   ```java
   options.setDeviceName("iPhone 15"); // Your simulator name
   options.setPlatformVersion("17.0"); // Your iOS version
   options.setUdid("YOUR_SIMULATOR_UDID"); // Your simulator UDID
   ```

3. **Compile and run**:
   ```bash
   cd /path/to/eGalvanic-Automation
   mvn compile
   mvn exec:java -Dexec.mainClass="com.egalvanic.simple.SimpleAppInstaller" -Dexec.classpathScope="compile"
   ```

## Finding Your Simulator UDID

To get your simulator's UDID and check available devices:
```bash
xcrun simctl list devices available
```

Look for your running simulator in the list and copy the UDID. You can also check if your simulator is already running using this command.

## Expected Behavior

When run successfully, the script will:
1. Connect to the Appium server
2. Install the specified app to the simulator
3. Launch the app
4. Keep the app running for 10 seconds
5. Quit the driver

## Common Error Messages

- **App does not exist or is not accessible**: This error occurs when the app file path is incorrect or the file doesn't exist. Update the `appPath` variable in the code to point to your actual app file.
- **Could not start a new session**: This error occurs when the Appium server is not running or the device configuration is incorrect.

## Troubleshooting

- **Appium server not running**: Make sure `appium` command is running in a separate terminal
- **Wrong device configuration**: Check that your device name, version, and UDID are correct
- **App path incorrect**: Verify the app file path exists and is accessible
- **Simulator not running**: Make sure your iOS simulator is launched before running the script