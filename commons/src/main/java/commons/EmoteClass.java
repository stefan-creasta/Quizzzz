package commons;

public class EmoteClass {
    public enum Emote {Angry, LOL, Sweat, Clap, Win};
    public Emote emote;
    public Long gameId;

    public EmoteClass(Long id, Emote e){
        gameId = id;
        emote = e;
    }
}
