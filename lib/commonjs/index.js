"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.getTracks = void 0;

var _reactNative = require("react-native");

const {
  MusicFiles
} = _reactNative.NativeModules;

const getTracks = options => {
  return new Promise((res, rej) => {
    if (_reactNative.Platform.OS === 'android') {
      MusicFiles.getAll(options, res, rej);
    } else {
      rej('not support on ios yet');
    }
  });
}; // export const syncTracks = (options: MusicFileOptions, onTrackReceived: (track: MusicFile)=> void) =>{
//     DeviceEventEmitter.addListener("onSongReceived", onTrackReceived);
//     return getTracks(options);
// }


exports.getTracks = getTracks;
MusicFiles.getTracks = getTracks;
var _default = MusicFiles;
exports.default = _default;
//# sourceMappingURL=index.js.map