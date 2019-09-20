# android-tracking
Android tracking application to track user behaviour while filling any type of forms.

# How to Install
- add following dependecies in buildscript:
```groovy
classpath 'org.aspectj:aspectjtools:1.9.4'
classpath("com.archinamon:android-gradle-aspectj:3.3.12")
```
- add maven repository in allprojects:
```groovy
maven { url 'https://jitpack.io' }
```

- add plugin in your app:
```groovy
apply plugin: 'com.archinamon.aspectj'
```

- enbale multiDex

- add library to app:
```groovy
implementation 'com.github.Algolytics:android-tracking:0.0.1'
```
- allow aspect from library:
```groovy
aspectj {
    includeAspectsFromJar 'com.github.Algolytics:android-tracking:0.0.1'
}
```

- start tracking in your main application class:
```kotlin
Tracker.Builder(
    this,
    #apiKey#,
    #url#,
    #scenario_name"
).build();
```

- enable fallowing permission in your app
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
- if you want to track an input you have to add listners to it as fallowing
```kotlin
editText.addTextChangedListener(object: DefaultTextWatcher(editText){})
```

# What Do We Track:
- amount of conntacts + date of last one
- amount of messages + date of last one
- amount of photos
- list of applications
- list of events from callendar
- accelerometer registers
- location registers
- connectivity information
- bettery informations
- changed activities
- clicks evetns
- selected inputs

# Supported Version
- minimal SDK version: 19
- target SDK version: 28
