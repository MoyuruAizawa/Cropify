# Cropify

[![JitPack](https://jitpack.io/v/MoyuruAizawa/cropify.svg)](https://jitpack.io/#MoyuruAizawa/cropify)
[![Vital](https://github.com/MoyuruAizawa/Cropify/actions/workflows/vital_check.yml/badge.svg)](https://github.com/MoyuruAizawa/Cropify/actions/workflows/vital_check.yml)
[![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/MoyuruAizawa/Cropify)  
Lightweight image cropper for Android Jetpack Compose.

<img src="https://github.com/MoyuruAizawa/Images/raw/master/Cropify/sample_01.gif?raw=true" height="640" width="305" />

# Installation

1. Add the JitPack repository to `settings.gradle.kts`.

```
dependencyResolutionManagement {
    ...
    repositories {        
        ...
        maven("https://jitpack.io")
    }
}
```

2. Add the dependency.

```
dependencies {
  implementation("com.github.moyuruaizawa:cropify:${cropifyVersion}")
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
  option = CropifyOption(
    frameSize = FullSize
  ),
)
```

Uri

```kotlin
val state = rememberCropifyState()

Cropify(
  uri = imageUri,
  state = state,
  onImageCropped = {},
  onFailedToLoadImage = {},
  option = CropifyOption(
    frameSize = FullSize
  ),
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
