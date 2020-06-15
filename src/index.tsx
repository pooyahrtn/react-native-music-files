import { NativeModules, Platform } from 'react-native';

const { MusicFiles } = NativeModules;

export interface MusicFileOptions {
    minimumSongDuration: number;
}

export interface MusicFile {
    id: string;
    path: string;
    artist: string;
    title: string;
    displayName: string;
    album: string;
    duration: number;
    cover: string;
}


export const getTracks = (options: MusicFileOptions) => {
    return new Promise<MusicFile[]>((res, rej)=>{
        if(Platform.OS === 'android'){
            MusicFiles.getAll(options, res, rej);
        }else{
            rej('not support on ios yet');
        }
    })
}

// export const syncTracks = (options: MusicFileOptions, onTrackReceived: (track: MusicFile)=> void) =>{
//     DeviceEventEmitter.addListener("onSongReceived", onTrackReceived);
//     return getTracks(options);
// }

MusicFiles.getTracks = getTracks;

export default MusicFiles;
