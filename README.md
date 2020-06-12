# react-native-music-files

## Getting started

`$ npm install react-native-music-files --save`

### Mostly automatic installation

`$ react-native link react-native-music-files`

## Usage
```javascript
import {syncTracks, MusicFile} from 'react-native-music-files';

syncTracks({minimumSongDuration: 30000}, (file: MusicFile) => {
    ...
});

```
