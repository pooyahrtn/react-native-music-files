import { NativeModules, Platform } from 'react-native';
const {
  MusicFiles
} = NativeModules;
export const getTracks = options => {
  return new Promise((res, rej) => {
    if (Platform.OS === 'android') {
      MusicFiles.getAll(options, res, rej);
    } else {
      rej('not support on ios yet');
    }
  });
}; // export const syncTracks = (options: MusicFileOptions, onTrackReceived: (track: MusicFile)=> void) =>{
//     DeviceEventEmitter.addListener("onSongReceived", onTrackReceived);
//     return getTracks(options);
// }

MusicFiles.getTracks = getTracks;
export default MusicFiles;
//# sourceMappingURL=index.js.map