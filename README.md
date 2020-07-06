# react-native-music-files
A light native module based on [react-native-get-music-files](https://github.com/cinder92/react-native-get-music-files), to get Android local tracks.

## Getting started

`$ npm install react-native-music-files --save`

### Mostly automatic installation

`$ react-native link react-native-music-files`

### Permission
Ensure to add 
```XML
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"/>
```
in your Android manifest file and check for permissions.

## Usage
```javascript
import {getTracks, MusicFile} from 'react-native-music-files';

getTracks().then(tracks: MusicFile[] => {});

```
on Android greater than 19 `getTracks()` runs on a seperate thread and doesn't block your UI thread.

