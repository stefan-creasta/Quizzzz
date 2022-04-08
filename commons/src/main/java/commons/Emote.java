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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Emote emote = (Emote) o;

        if (gameId != emote.gameId) return false;
        if (type != emote.type) return false;
        return username.equals(emote.username);

    }
}
