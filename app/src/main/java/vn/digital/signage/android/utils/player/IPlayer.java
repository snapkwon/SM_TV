package vn.digital.signage.android.utils.player;

/**
 * The interface Player.
 */
public interface IPlayer {

    /**
     * Play with exo player.
     *
     * @param url      the url
     * @param fileName the file name
     * @param urlIndex the url index
     */
    void playWithExoPlayer(String url, final String fileName, int urlIndex);
}
