package GameReader;

import java.util.ArrayList;

public class Player {

    //Hazards, die auf der eigenen Seite liegen
    private Pokemon spikesBy, tSpikesBy, rocksBy;

    private ArrayList<Pokemon> mons = new ArrayList<>();
    private String nickname;
    private boolean winner=false;





    public boolean isWinner() {
        return winner;
    }
    public void setWinner(boolean winner) {
        this.winner = winner;
    }
    public Pokemon getSpikesBy() {
        return spikesBy;
    }
    public void setSpikesBy(Pokemon spikesBy) {
        this.spikesBy = spikesBy;
    }
    public Pokemon gettSpikesBy() {
        return tSpikesBy;
    }
    public void settSpikesBy(Pokemon tSpikesBy) {
        this.tSpikesBy = tSpikesBy;
    }
    public Pokemon getRocksBy() {
        return rocksBy;
    }
    public void setRocksBy(Pokemon rocksBy) {
        this.rocksBy = rocksBy;
    }
    public ArrayList<Pokemon> getMons() {
        return mons;
    }
    public void setMons(ArrayList<Pokemon> mons) {
        this.mons = mons;
    }
    public String getNickname() {
        return nickname;
    }
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int indexOfName(String s) {
        for(Pokemon p: mons) {
            try {
                if(p.getPokemon().equals(s)) return mons.indexOf(p);
            } catch(NullPointerException ignored) {}
        }
        return -1;
    }

    public int indexOfNick(String s) {
        for(Pokemon p: mons) {
            try {
                if(p.getNickname().equals(s)) return mons.indexOf(p);
            } catch(NullPointerException ignored) {}
        }
        return -1;
    }

}
