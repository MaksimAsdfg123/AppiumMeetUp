package org.wikiqa.infra;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobileCapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class Platform {
  private static final String PLATFORM_IOS = "ios";
  private static final String PLATFORM_ANDROID = "android";

  private static final Platform instance = new Platform();
  private final String name;

  public static Platform getInstance() {
    return instance;
  }

  private Platform() {
    name = System.getenv("PLATFORM");
  }

  AppiumDriver getDriver() throws MalformedURLException {
    URL URL = new URL("http://127.0.0.1:4723/wd/hub");
    if (isAndroid()) {
      return new AndroidDriver(URL, getAndroidDesiredCapabilities());
    }
    else if (isIOS()) {
      return new IOSDriver(URL, getIOSDesiredCapabilities());
    }
    else {
      throw new IllegalArgumentException("Cannot detect type of the Driver. Platform value: " + name);
    }
  }

  public boolean isAndroid() {
    return PLATFORM_ANDROID.equals(name);
  }

  public boolean isIOS() {
    return PLATFORM_IOS.equals(name);
  }

  private DesiredCapabilities getAndroidDesiredCapabilities() {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("deviceName", "emulator-5554");
    capabilities.setCapability("platformName", "Android");
    capabilities.setCapability("platformVersion", "10");
    capabilities.setCapability("automationName", "Appium");
    capabilities.setCapability("appPackage", "org.wikipedia");
    capabilities.setCapability("appActivity", ".main.MainActivity");
    File file = new File("src/test/resources/", "apks/org.wikipedia.apk");
    capabilities.setCapability("app", file.getAbsolutePath());
    capabilities.setCapability("newCommandTimeout", 11);
    return capabilities;
  }

  private DesiredCapabilities getIOSDesiredCapabilities() {
    DesiredCapabilities capabilities = new DesiredCapabilities();
    capabilities.setCapability("platformName", "iOS");
    // Hint: run `xcodebuild -showsdks` to see the list of available SDKs
    capabilities.setCapability("deviceName", "iPhone Simulator");
    // Hint: run `xcrun simctl list runtimes` to get available runtimes
    capabilities.setCapability("platformVersion", "14.4");
    //capabilities.setCapability("app", resourcePath("/apks/Wikipedia.app"));
    capabilities.setCapability("app", "/Users/andrei/temp/BMI-Calculator-iOS13/build/Release-iphonesimulator/BMI Calculator.app");
    capabilities.setCapability("automationName", "XCUITest");
    capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 20);
    capabilities.setCapability("autoAcceptAlerts", true);
    return capabilities;
  }

  private String resourcePath(String file) {
    URL resource = Thread.currentThread().getContextClassLoader().getResource(file);
    assertNotNull(resource, "Resource not found in classpath: " + file);
    return resource.getFile();
  }
}
