# Cropify
![JitPack](https://jitpack.io/v/MoyuruAizawa/cropify.svg)
![Vital](https://github.com/MoyuruAizawa/Cropify/actions/workflows/vital_check.yml/badge.svg)  
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
