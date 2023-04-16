# Cropify

[![JitPack](https://jitpack.io/v/MoyuruAizawa/cropify.svg)](https://jitpack.io/#MoyuruAizawa/cropify)
[![Vital](https://github.com/MoyuruAizawa/Cropify/actions/workflows/vital_check.yml/badge.svg)](https://github.com/MoyuruAizawa/Cropify/actions/workflows/vital_check.yml)  
Lightweight image cropper for Android Jetpack Compose.

https://user-images.githubusercontent.com/9051623/231838736-8bff221e-8515-4dd4-8314-64c3166e700d.mov

# Installation

1. Add the JitPack repository to your root `build.gradle`.

```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```

2. Add the dependency.

```
dependencies {
  implementation 'com.github.moyuruaizawa:cropify:${cropifyVersion}'
}
```

# Usage

Bitmap

```kotlin
val state = rememberCropifyState()

Cropify(
  bitmap = imageResource(R.drawable.bitmap),
  state = state,
  onImageCropped = {},
)
```

Uri

```kotlin
val state = rememberCropifyState()

Cropify(
  uri = imageUri,
  state = state,
  onImageCropped = {},
  onFailedToLoadImage = {}
)
```

Invoke `CropifyState#crop`, the cropped image will be passed to `onImageCropped`.
If loading an image from Uri fails, `onFailedToLoadImage` will be called.

# Features

- Cropping.
- set Bitmap or load android.net.Uri.
- Bitmap memory optimization.
- API Level 21+.

# Customizations

- Cropping frame color.
- Cropping frame alpha.
- Cropping frame stroke width.
- Cropping frame aspect ratio.
- Grid line color.
- Grid line alpha.
- Grid line stroke width.
- Mask (outside of cropping frame) color.
- Mask alpha.
- Background color.

# Acknowledgement

I would like to thank [ArthurHub/Android-Image-Cropper](https://github.com/ArthurHub/Android-Image-Cropper).    
Cropify aims to a Jetpack Compose implementation of ArthurHub/Android-Image-Cropper.  
