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
export declare const getTracks: (options: MusicFileOptions) => Promise<MusicFile[]>;
export default MusicFiles;
