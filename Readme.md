# Mysplash

Mysplash is unofficial [Unsplash](https://unsplash.com/) client for android,
allowing you to search for inspiration from over 3 million
carefully selected photos in high quality and
download them for free.

<a href="https://play.google.com/store/apps/details?id=education.openschools.parents"><img src="rustore_icon.png" width="188" height="63" ></a>

## Getting started

* Create a developer account at: https://unsplash.com/developers
* Copy your app access key
* Add the following to your `local.properties`:
```
unsplashAccessKey="Your access key"
storePassword=""
keyPassword=""
keyAlias=""
storeFile=""
```

To build the app, you need to get a `google-services.json` file from Firebase:

* Go to [Firebase](https://console.firebase.google.com) and create a new project
* In the Firebase console, add an Android app to the project with the package name `com.timurkhabibulin.mysplash`
* Download the `google-services.json` config file
* Then place the `google-services.json` file in the `app/` directory (at the root of the Android Studio app module)

## Technology Stack

* Kotlin
* Jetpack Compose
* Clean Architecture
* MVVM
* Coroutines
* Retrofit
* Pagination
* Hilt

## Screenshots

<p align="left">
<img src="Screenshots/Editorial.jpg" alt="drawing" width="250"/>
<img src="Screenshots/Topics.jpg" alt="drawing" width="250"/>
<img src="Screenshots/PhotoInfo1.jpg" alt="drawing" width="250"/>
</p>
<p align="left">
 <img src="Screenshots/User.jpg" alt="drawing" width="250"/>
<img src="Screenshots/Search.jpg" alt="drawing" width="250"/>
<img src="Screenshots/Collections.jpg" alt="drawing" width="250"/>
<img src="Screenshots/collectionView.jpg" alt="drawing" width="250"/>
</p>



