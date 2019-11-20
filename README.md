# android-tracking
Android tracking application to track user behaviour while filling any type of forms.

# 1. How to Install
#### 1.1. Add following dependecies in buildscript:
```groovy
classpath 'org.aspectj:aspectjtools:1.9.4'
classpath("com.archinamon:android-gradle-aspectj:3.4.3")
```
#### 1.2. Add maven repository in allprojects:
```groovy
maven { url 'https://jitpack.io' }
```

#### 1.3. Add plugin in your app:
```groovy
apply plugin: 'com.archinamon.aspectj'
```

#### 1.4. Enbale multiDex

#### 1.5. Add following libraries
 Add library to app:
```groovy
implementation 'com.github.Algolytics:android-tracking:%current_version%'
```
 Allow aspectj from library:
```groovy
aspectj {
    includeAspectsFromJar 'com.github.Algolytics:android-tracking:%current_version%'
}
```

#### 1.6. In your main class:
Create an object of AspectConfig and enable its options:


 
 **Kotlin**
```kotlin

val aspectConfig = AspectConfig()
            .enableClicks()
            .enableInputs()
            .enableActivities()
```
**Java**
```java
AspectConfig aspectConfig = new AspectConfig()
            .enableClicks()
            .enableInputs()
            .enableActivities()
```
**Available options:**
- `enableClicks()` - _enabling click aspects_
- `enableInputs()` - _enabling text aspects_
- `enableActivities()` - _enabling activity aspects_

Create an object of Tracker and enable its options:

**Kotlin**
```kotlin
Tracker.Builder(
    this,
    "apiKey",
    "url",
    "scenarioName"
)
.enableConnectivity()
.enableLocation()
.aspectConfig(aspectConfig)
.build()
```
**Java**
```Java
new Tracker.Builder(
    this,
    "apiKey",
    "url",
    "scenarioName"
)
.enableConnectivity()
.enableLocation()
.aspectConfig(aspectConfig)
.build()
```

**Available options:**
- `enableConnectivity()` - _tracking Internet connectivity information_
- `enableAccelerometer` - _tracking information about phone rotations_
- `enableLocation()` - _tracking information about location_
- `enableBattery()` - _tracking information about battery_
- `enableWifi()` - _tracking information about nearby wifi connections_
- `enableContacts()` - _tracking amount of contacts_
- `enablePhotos()` - _tracking amount of photos_
- `enableCalendar()` - _tracking events from calendar_
- `enableMessages()` - _tracking amount of messages_
- `enableApplications()` - _tracking amount and names of installed application_
- `aspectConfig( aspectConfig )` - _tracking information about aspects (text inputs, changing activities, button clicks). It takes previously created AspectConfig object as an argument._
- `apiPoolingTimeMillis( timeInMillis )` - _changing frequency of sending information to the server (default is 1/30 000 millis). It takes time in millis as an argument._
- `connectivityPoolingTimeMillis( timeInMillis )` - _changing frequency of getting information about connectivity (default is 1/30 000 millis). It takes time in millis as an argument._
- `accelerometerPoolingTimeMillis( timeInMillis )` - _changing frequency of getting information about accelerometer (default is 1/30 000 millis). It takes time in millis as an argument._
- `locationPoolingTimeMillis( timeInMillis )` - _changing frequency of getting information about location (default is 1/30 000 millis). It takes time in millis as an argument._
- `batteryPoolingTimeMillis( timeInMillis )` - _changing frequency of getting information about battery (default is 1/30 000 millis). It takes time in millis as an argument._
- `wifiPoolingTimeMillis( timeInMillis )` - _changing frequency of getting information about nearby wifi connections (default is 1/30 000 millis). It takes time in millis as an argument._


#### 1.6. Enable following permission in your app:

```xml
<uses-permission android:name="android.permission.ACCESS_FINE_LOCATION" />
<uses-permission android:name="android.permission.ACCESS_COARSE_LOCATION" />
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.READ_CONTACTS" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.READ_CALENDAR" />
<uses-permission android:name="android.permission.READ_SMS" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.CHANGE_WIFI_STATE" />
```
#### 1.7. If you want to track inputs and spinners, you have to add listners to them:
```kotlin
inputName.addTextChangedListener(object: DefaultTextWatcher(editText){})
spinnerName.onItemSelectedListener = DefaultSpinnerListener(spinnerName, "spinner_tracking_name")

```

#### 1.8. If needed, change version of build:gradle:
Maximal version that is compatible with android-tracker is 3.4.2
```groovy
 classpath 'com.android.tools.build:gradle:3.5.2'
 ```

# 2. What Do We Track:
- amount of contacts + date of last one
- amount of messages + date of last one
- amount of photos
- list of applications
- list of events from calendar
- accelerometer registers
- location registers
- connectivity information
- battery information
- nearby wifi networks
- changed activities
- clicks events
- selected inputs

# 3. Supported Version
- minimal SDK version: 19
- target SDK version: 28
