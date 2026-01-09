package Lab8;

import java.util.ArrayList;
import java.util.List;

class Song {
    String title;
    String artist;

    public Song(String title, String artist) {
        this.title = title;
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "Song{title=" + title + ", artist=" + artist + "}";
    }
}

class MP3PlayerContext {
    private MP3PlayerState state;

    public MP3PlayerContext(MP3PlayerState state) {
        this.state = state;
    }

    public void request() {
        state.handleRequest(0);
    }
}

interface MP3PlayerState {
    void handleRequest(int index);
}

class PlayState implements MP3PlayerState {
    @Override
    public void handleRequest(int index) {
        System.out.println("Song " + index + " is playing");
    }
}

class StopState implements MP3PlayerState {
    @Override
    public void handleRequest(int index) {
        System.out.println("Song " + index + " is paused");
    }
}



class MP3Player {

    private List<Song> songs;
    private MP3PlayerState currentState;
    private int index;
    private boolean paused;

    public MP3Player(List<Song> songs) {
        this.songs = songs;
        this.index = 0;
        this.paused = true;
        this.currentState = new StopState();
    }

    // ▶ PLAY
    public void pressPlay() {
        if (currentState instanceof PlayState && !paused) {
            System.out.println("Song is already playing");
            return;
        }

        currentState = new PlayState();
        currentState.handleRequest(index);
        paused = false;
    }

    // ⏹ STOP
    public void pressStop() {
        if (paused) {
            index = 0;
            System.out.println("Songs are stopped");
        } else {
            currentState = new StopState();
            currentState.handleRequest(index);
            paused = true;
        }
    }

    // ⏭ FORWARD
    public void pressFWD() {
        pauseIfPlaying();
        index = (index + 1) % songs.size();
        playAfterMove();
    }

    // ⏮ REWIND
    public void pressREW() {
        pauseIfPlaying();
        index = (index - 1 + songs.size()) % songs.size();
        playAfterMove();
    }

    private void pauseIfPlaying() {
        if (!paused) {
            currentState = new StopState();
            currentState.handleRequest(index);
        }
    }

    private void playAfterMove() {
        currentState = new PlayState();
        currentState.handleRequest(index);
        paused = false;
    }

    public void printCurrentSong() {
        System.out.println(songs.get(index));
    }

    @Override
    public String toString() {
        return "MP3Player{currentSongIndex=" + index + ", songList=" + songs + "}";
    }
}

public class PatternTest {
    public static void main(String args[]) {
        List<Song> listSongs = new ArrayList<Song>();
        listSongs.add(new Song("first-title", "first-artist"));
        listSongs.add(new Song("second-title", "second-artist"));
        listSongs.add(new Song("third-title", "third-artist"));
        listSongs.add(new Song("fourth-title", "fourth-artist"));
        listSongs.add(new Song("fifth-title", "fifth-artist"));
        MP3Player player = new MP3Player(listSongs);


        System.out.println(player.toString());
        System.out.println("First test");


        player.pressPlay();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressPlay();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Second test");


        player.pressStop();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressStop();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
        System.out.println("Third test");


        player.pressFWD();
        player.printCurrentSong();
        player.pressFWD();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressPlay();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressStop();
        player.printCurrentSong();

        player.pressFWD();
        player.printCurrentSong();
        player.pressREW();
        player.printCurrentSong();


        System.out.println(player.toString());
    }
}

//Vasiot kod ovde