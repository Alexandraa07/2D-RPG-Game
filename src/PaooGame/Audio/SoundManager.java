package PaooGame.Audio;

import javax.sound.sampled.*;
import java.io.BufferedInputStream;
import java.io.InputStream;

/*! \class SoundManager
    \brief Gestioneaza redarea muzicii de fundal.
*/
public class SoundManager {

    private Clip music; /// Music

    /*! \fn public void playMusic(String path)
        \brief Incarca si reda un fisier audio in bucla continua.
        \param path Calea catre fisierul .wav din resurse.
    */
    public void playMusic(String path) {
        try {
            InputStream is = getClass().getResourceAsStream(path);
            AudioInputStream audio = AudioSystem.getAudioInputStream(new BufferedInputStream(is));
            music = AudioSystem.getClip();
            music.open(audio);
            music.loop(Clip.LOOP_CONTINUOUSLY); ///repeta la infinit
            music.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}