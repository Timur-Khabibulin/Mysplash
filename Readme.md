<div align="center">
<h1 align="center">Mysplash</h1>
<img src="design/logo.png" width="200" />
</div>

<h4 align="center">Mysplash is unofficial Unsplash client for android,
allowing you to search for inspiration from over 3 million
carefully selected photos in high quality and
download them for free
</h4>

<p align="center">
  <br>
  <a href="https://apps.rustore.ru/app/com.timurkhabibulin.mysplash">
    <img src="rustore_icon.png" width="188" height="63" >
  </a>
  <br>
  <br>
  <a href="https://github.com/Timur-Khabibulin/Mysplash/releases/latest">
    <img alt="Release" src="https://img.shields.io/github/v/release/Timur-Khabibulin/Mysplash?style=for-the-badge">
  </a>
  <img alt="API" src="https://img.shields.io/badge/Api%2026+-50f270?logo=android&logoColor=black&style=for-the-badge"/></a>
  <img alt="Kotlin" src="https://img.shields.io/badge/Kotlin-a503fc?logo=kotlin&logoColor=white&style=for-the-badge"/></a>
  <img alt="Jetpack Compose" src="https://img.shields.io/static/v1?style=for-the-badge&message=Jetpack+Compose&color=4285F4&logo=Jetpack+Compose&logoColor=FFFFFF&label="/></a> 
</p>

<p align="center">
<img src="design/screenshots/en/en_1.png" alt="drawing" width="18%"/>
<img src="design/screenshots/en/en_2.png" alt="drawing" width="18%"/>
<img src="design/screenshots/en/en_3.png" alt="drawing" width="18%"/>
<img src="design/screenshots/en/en_4.png" alt="drawing" width="18%"/>
<img src="design/screenshots/en/en_5.png" alt="drawing" width="18%"/>
</p>

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
* In the Firebase console, add an Android app to the project with the package
  name `com.timurkhabibulin.mysplash`
* Download the `google-services.json` config file
* Then place the `google-services.json` file in the `app/` directory (at the root of the Android
  Studio app module)

## Technology Stack

* Kotlin
* Jetpack Compose
* Clean Architecture
* MVVM
* Coroutines
* Retrofit
* Room
* Pagination
* Hilt



