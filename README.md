# android-tracking
Android tracking application to track user behaviour while filling any type of forms.

# 1. How to Install
#### 1.1. Add following dependecies in buildscript:
```groovy
classpath 'org.aspectj:aspectjtools:1.9.4'
classpath("com.archinamon:android-gradle-aspectj:3.3.12")
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

 Add library to app:
```groovy
implementation 'com.github.Algolytics:android-tracking:0.0.1'
```
 Allow aspectj from library:
```groovy
aspectj {
    includeAspectsFromJar 'com.github.Algolytics:android-tracking:0.0.1'
}
```

#### 1.5. In your main class:
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
- `enableClicks()` - enabling click aspects
- `enableInputs()` - enabling text aspects
- `enableActivities()` - enabling activity aspects

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
- `enableConnectivity()` - tracking Internet connectivity information
- `enableAccelerometer` - tracking information about phone rotations
- `enableLocation()` - tracking information about location
- `enableBattery()` - tracking information about battery
- `enableContacts()` - tracking amount of contacts
- `enablePhotos()` - tracking amount of photos
- `enableCalendar()` - tracking events from calendar
- `enableMessages()` - tracking amount of messages
- `enableApplications()` - tracking amount and names of installed applications
- `aspectConfig( acpectConfig )` - tracking information about aspects (text inputs, changing activities, button clicks). It takes previously created AspectConfig object as an argument.
- `apiPoolingTimeMillis( timeInMillis )` - changing frequency of sending information to the server (default is 1/30 000 millis). It takes time in millis as an argument.
- `connectivityPoolingTimeMillis( timeInMillis )` - changing frequency of getting information about connectivity (default is 1/30 000 millis). It takes time in millis as an argument.
- `accelerometerPoolingTimeMillis( timeInMillis )` - changing frequency of getting information about accelerometer (default is 1/30 000 millis). It takes time in millis as an argument.
- `locationPoolingTimeMillis( timeInMillis )` - changing frequency of getting information about location (default is 1/30 000 millis). It takes time in millis as an argument.
- `batteryPoolingTimeMillis( timeInMillis )` - changing frequency of getting information about battery (default is 1/30 000 millis). It takes time in millis as an argument.


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
```
#### 1.7. If you want to track the inputs, you have to add listners to them:
```kotlin
inputName.addTextChangedListener(object: DefaultTextWatcher(editText){})
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
- bettery information
- changed activities
- clicks events
- selected inputs

# 3. Supported Version
- minimal SDK version: 19
- target SDK version: 28
