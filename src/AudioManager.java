import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.embed.swing.JFXPanel;
import java.util.Stack;

public class AudioManager {
    private static MediaPlayer mainMenuAudio;
    private static MediaPlayer pausePlayerAudio;
    private static MediaPlayer winPlayerAudio;
    private static MediaPlayer losePlayerAudio;
    private static MediaPlayer drawPlayerAudio;
    private static MediaPlayer gameAudio;
    private static MediaPlayer cheatsONAudio;
    private static Stack<MediaPlayer> audioStack = new Stack<>();
    
    public static void initializeJavaFXToolkit() {
        new JFXPanel(); // Initialize the JavaFX toolkit
    }
    
    public static void playMainMenuSound() {
        String soundPath = getSoundPath("Start_Menu.mp3");
        Media sound = new Media(soundPath);

        stopAllSounds();

        mainMenuAudio = new MediaPlayer(sound);
        mainMenuAudio.setCycleCount(MediaPlayer.INDEFINITE);
        mainMenuAudio.play();
        audioStack.push(mainMenuAudio);
    }
    public static void playGameSound() {
        String soundPath = getSoundPath("Ghost_Fight.mp3");
        Media sound = new Media(soundPath);

        stopAllSounds();

        gameAudio = new MediaPlayer(sound);
        gameAudio.setCycleCount(MediaPlayer.INDEFINITE);
        gameAudio.play();
        audioStack.push(gameAudio);
    }
    public static void playCheatsOnSound() {
        String soundPath = getSoundPath("Dogsong.mp3");
        Media sound = new Media(soundPath);

        stopAllSounds();

        cheatsONAudio = new MediaPlayer(sound);
        cheatsONAudio.setCycleCount(MediaPlayer.INDEFINITE);
        cheatsONAudio.play();
        audioStack.push(cheatsONAudio);
    }

    public static void playPauseSound() {
        String soundPath = getSoundPath("CORE.mp3");
        Media sound = new Media(soundPath);

        stopAllSounds();

        pausePlayerAudio = new MediaPlayer(sound);
        pausePlayerAudio.setCycleCount(MediaPlayer.INDEFINITE);
        pausePlayerAudio.play();
        audioStack.push(pausePlayerAudio);
    }

    public static void playWinSound() {
        String soundPath = getSoundPath("It's_Showtime.mp3");
        Media sound = new Media(soundPath);

        stopAllSounds();

        winPlayerAudio = new MediaPlayer(sound);
        winPlayerAudio.setCycleCount(MediaPlayer.INDEFINITE);
        winPlayerAudio.play();
        audioStack.push(winPlayerAudio);
    }

    public static void playLoseSound() {
        String soundPath = getSoundPath("Determination.mp3");
        Media sound = new Media(soundPath);

        stopAllSounds();

        losePlayerAudio = new MediaPlayer(sound);
        losePlayerAudio.setCycleCount(MediaPlayer.INDEFINITE);
        losePlayerAudio.play();
        audioStack.push(losePlayerAudio);
    }

    public static void playDrawSound() {
        String soundPath = getSoundPath("Dont_Give_Up.mp3");
        Media sound = new Media(soundPath);

        stopAllSounds();

        drawPlayerAudio = new MediaPlayer(sound);
        drawPlayerAudio.setCycleCount(MediaPlayer.INDEFINITE);
        drawPlayerAudio.play();
        audioStack.push(drawPlayerAudio);
    }

    public static void stopAllSounds() {
        if (mainMenuAudio != null) {
            mainMenuAudio.stop();
        }
        if (pausePlayerAudio != null) {
            pausePlayerAudio.stop();
        }
        if (winPlayerAudio != null) {
            winPlayerAudio.stop();
        }
        if (losePlayerAudio != null) {
            losePlayerAudio.stop();
        }
        if (drawPlayerAudio != null) {
            drawPlayerAudio.stop();
        }
        if (gameAudio != null) {
        	gameAudio.stop();
        }
        if (cheatsONAudio != null) {
        	cheatsONAudio.stop();
        }
    }

    public static void resumePreviousSound() {
        if (!audioStack.isEmpty()) {
        	audioStack.pop();
            MediaPlayer previousAudio = audioStack.peek();
            stopAllSounds();
            previousAudio.play();
        }
    }
    

    private static String getSoundPath(String fileName) {
        return AudioManager.class.getResource("/audio/" + fileName).toString();
    }
}
