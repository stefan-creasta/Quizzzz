package commons;

public class Emote {
    public enum Type {Angry, LOL, Sweat, Clap, Win};
    public Type type;
    public String username;
    public long gameId;

    public Emote() {

    }

    public Emote(Type t, String username, long gameId){
        this.type = t;
        this.username = username;
        this.gameId = gameId;
    }

    @Override
    public String toString() {
        return "Emote{" +
                "type=" + type +
                ", username='" + username + '\'' +
                ", gameId=" + gameId +
                '}';
    }
}
