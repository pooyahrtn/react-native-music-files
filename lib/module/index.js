import { NativeModules, Platform, DeviceEventEmitter } from 'react-native';
const {
  MusicFiles
} = NativeModules;

const startListenTracks = options => {
  return new Promise((res, rej) => {
    if (Platform.OS === 'android') {
      MusicFiles.getAll(options, res, rej);
    } else {
      rej('not support on ios yet');
    }
  });
};

export const syncTracks = (options, onTrackReceived) => {
  DeviceEventEmitter.addListener("onSongReceived", onTrackReceived);
  return startListenTracks(options);
};
export default MusicFiles;
//# sourceMappingURL=index.js.map