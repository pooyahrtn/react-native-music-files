declare const MusicFiles: any;
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
export declare const syncTracks: (options: MusicFileOptions, onTrackReceived: (track: MusicFile) => void) => Promise<unknown>;
export default MusicFiles;
