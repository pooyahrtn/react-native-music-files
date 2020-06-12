"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.default = exports.syncTracks = void 0;

var _reactNative = require("react-native");

const {
  MusicFiles
} = _reactNative.NativeModules;

const startListenTracks = options => {
  return new Promise((res, rej) => {
    if (_reactNative.Platform.OS === 'android') {
      MusicFiles.getAll(options, res, rej);
    } else {
      rej('not support on ios yet');
    }
  });
};

const syncTracks = (options, onTrackReceived) => {
  _reactNative.DeviceEventEmitter.addListener("onSongReceived", onTrackReceived);

  return startListenTracks(options);
};

exports.syncTracks = syncTracks;
var _default = MusicFiles;
exports.default = _default;
//# sourceMappingURL=index.js.map