package GameReader;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class Spielauswertung {

    public static Player[] werteAus(String link) {

        if(link.indexOf("http:")==0) {
            link="https:"+link.split("http:")[1];
        }
        link = link +".log";

        @SuppressWarnings("unchecked")
        ArrayList<String> game = (ArrayList<String>) getGameArrayList(link).clone();

        Player p1 = new Player();
        Player p2 = new Player();

        for(String s : game) {
            //Nickname des Spielers
            if((s.contains("|player|p1"))&&(s.length()>11)) {
                p1.setNickname(s.split("\\|")[3]);
            }
            if((s.contains("|player|p2"))&&(s.length()>11)) {
                p2.setNickname(s.split("\\|")[3]);
            }

            //Welche Pokemon sind dabei?
            if(s.contains("|poke|p1")) {
                p1.getMons().add(new Pokemon(s.split("\\|")[3].split(",")[0]));
            }
            if(s.contains("|poke|p2")) {
                p2.getMons().add(new Pokemon(s.split("\\|")[3].split(",")[0]));
            }

            //Detailschange
            if(s.contains("|switch|p") || s.contains("|drag|p")){
                Player p;
                if(s.contains("|switch|p1") || s.contains("|drag|p1")) {
                    p = p1;
                }
                else {
                    p = p2;
                }

                if(s.split("\\|")[3].split(",")[0].contains("Silvally") && p.indexOfName("Silvally-*")!=-1) {//Silvally-Problem
                    p.getMons().get(p.indexOfName("Silvally-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].contains("Arceus") && p.indexOfName("Arceus-*")!=-1) {//Arceus-Problem
                    p.getMons().get(p.indexOfName("Arceus-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].contains("Genesect") && p.indexOfName("Genesect-*")!=-1) {//Genesect-Problem
                    p.getMons().get(p.indexOfName("Genesect-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].contains("Gourgeist") && p.indexOfName("Gourgeist-*")!=-1) {//Gourgeist-Problem
                    p.getMons().get(p.indexOfName("Gourgeist-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].contains("Urshifu") && p.indexOfName("Urshifu-*")!=-1) {//Urshifu-Problem
                    p.getMons().get(p.indexOfName("Urshifu-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].contains("Zamazenta") && p.indexOfName("Zamazenta-*")!=-1) {//Zamazenta-Problem
                    p.getMons().get(p.indexOfName("Zamazenta-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].contains("Zacian") && p.indexOfName("Zacian-*")!=-1) {//Zacian-Problem
                    p.getMons().get(p.indexOfName("Zacian-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].contains("Dudunsparce") && p.indexOfName("Dudunsparce-*")!=-1) {//Dudunsparce-Problem
                    p.getMons().get(p.indexOfName("Dudunsparce-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
            }

            //Win
            if(s.contains("|win|")) {
                if(p1.getNickname().equals(s.split("\\|")[2])) {
                    p1.setWinner(true);
                }
                if(p2.getNickname().equals(s.split("\\|")[2])) {
                    p2.setWinner(true);
                }
            }
        }



        Pokemon lastMove=null;
        Pokemon activeP1=null;
        Pokemon activeP2=null;
        Pokemon weatherBy=null;
        // Doom Desire & Future Sight
        Pokemon P1FutureSightBy=null, P2FutureSightBy=null, P1DoomDesireBy=null, P2DoomDesireBy=null;
        boolean P1FutureSight=false, P2FutureSight=false, P1DoomDesire=false, P2DoomDesire=false;

        //Schaden
        for(String s : game) {
            /*
             * LastMove abspeichern
             */
            if(s.contains("|move|p1")) {
                lastMove=activeP1;
            }
            if(s.contains("|move|p2")) {
                lastMove=activeP2;
            }
            //Synchronize muss als LastMove abgespeichert werden, um den Status zu tracken. Bei Protect gilt selbiges, wenn es sich um einen Baneful Bunker handelt
            if (s.contains("|-activate|p1") && (s.contains("|ability: Synchronize") || s.contains("|move: Protect"))){
                lastMove=activeP1;
            }
            if (s.contains("|-activate|p2") && (s.contains("|ability: Synchronize") || s.contains("|move: Protect"))){
                lastMove=activeP2;
            }

            /*
             * LastMove am Ende der Runde löschen
             */
            if(s.contains("|turn|")) {
                lastMove=null;
                P1FutureSight=false;
                P2FutureSight=false;
                P1DoomDesire=false;
                P2DoomDesire=false;
            }

            /*
             * aktive Pokemon abspeichern und LastMove zuruecksetzen
             */
            if(s.contains("|switch|p1") || s.contains("|drag|p1")) {
                activeP1=p1.getMons().get(p1.indexOfName(s.split("\\|")[3].split(",")[0]));
                lastMove=null;

                //Nickname
                activeP1.setNickname(s.split("\\|p1a: ")[1].split("\\|")[0]);
            }
            if(s.contains("|switch|p2") || s.contains("|drag|p2")) {
                activeP2=p2.getMons().get(p2.indexOfName(s.split("\\|")[3].split(",")[0]));
                lastMove=null;

                //Nickname
                activeP2.setNickname(s.split("\\|p2a: ")[1].split("\\|")[0]);
            }

            //Detailschange
            if(s.contains("|detailschange|p1")) {
                activeP1.setPokemon(s.split("\\|")[3].split(",")[0]);
            }
            if(s.contains("|detailschange|p2")) {
                activeP2.setPokemon(s.split("\\|")[3].split(",")[0]);
            }

            /*
             * Direkte Hits, ausgenommen von CurseSD
             */
            if((s.contains("|-damage|p1"))&&(s.split("\\|").length==4)) {
                //Wenn FutureSight oder DoomDesire
                if (P2FutureSight){
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        P2FutureSightBy.killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(P2FutureSightBy);
                    }
                } else if (P2DoomDesire) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        P2DoomDesireBy.killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(P2DoomDesireBy);
                    }
                } else {
                    if(s.contains("0 fnt")) {
                        //Wenn CurseSD
                        if (lastMove == activeP1) {
                            if (lastMove.getLastDmgBy() != null) {
                                lastMove.getLastDmgBy().killsPlus1();
                                lastMove.setDead(true);
                            } else {
                                activeP1.setDead(true);
                                activeP2.killsPlus1();
                            }
                            //Wenn nicht CurseSD, also normaler Hit
                        } else {
                            activeP1.setDead(true);
                            activeP2.killsPlus1();
                        }
                    } else {
                        activeP1.setLastDmgBy(activeP2);
                    }
                }
            }
            if((s.contains("|-damage|p2"))&&(s.split("\\|").length==4)) {
                //Wenn FutureSight oder DoomDesire
                if (P1FutureSight){
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        P1FutureSightBy.killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(P1FutureSightBy);
                    }
                } else if(P1DoomDesire) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        P1DoomDesireBy.killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(P1DoomDesireBy);
                    }
                } else {
                    if(s.contains("0 fnt")) {
                        //Wenn CurseSD
                        if(lastMove==activeP2) {
                            if(lastMove.getLastDmgBy()!=null) {
                                lastMove.getLastDmgBy().killsPlus1();
                                lastMove.setDead(true);
                            } else {
                                activeP2.setDead(true);
                                activeP1.killsPlus1();
                            }
                            //Wenn nicht CurseSD, also normaler Hit
                        } else {
                            activeP2.setDead(true);
                            activeP1.killsPlus1();
                        }
                    } else {
                        activeP2.setLastDmgBy(activeP1);
                    }
                }
            }

            /*
             * Klammerattacken
             */
            //Eingesetzt
            if((s.contains("|-activate|p1"))&&(s.split("\\|").length==5)) {
                activeP1.setBindedBy(activeP2);
            }
            if((s.contains("|-activate|p2"))&&(s.split("\\|").length==5)) {
                activeP2.setBindedBy(activeP1);
            }
            //Kill
            if(s.contains("partiallytrapped")) {
                if((s.contains("|-damage|p1"))&&(s.split("\\|").length==6)) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        activeP1.getBindedBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getBindedBy());
                    }
                }
                if((s.contains("|-damage|p2"))&&(s.split("\\|").length==6)) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        activeP2.getBindedBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(activeP2.getBindedBy());
                    }
                }
            }

            /*
             * Destiny Bond
             */
            if((s.contains("|-activate|p1"))&&(s.contains("move: Destiny Bond"))) {
                activeP1.killsPlus1();
                activeP2.setDead(true);
            }
            if((s.contains("|-activate|p2"))&&(s.contains("move: Destiny Bond"))) {
                activeP2.killsPlus1();
                activeP1.setDead(true);
            }

            /*
             * Curse
             */
            //Eingesetzt
            if((s.contains("|-start|p1"))&&(s.contains("|Curse|"))) {
                activeP1.setCursedBy(activeP2);
            }
            if((s.contains("|-start|p2"))&&(s.contains("|Curse|"))) {
                activeP2.setCursedBy(activeP1);
            }
            //Kill
            if(s.contains("|[from] Curse")) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        activeP1.getCursedBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getCursedBy());
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        activeP2.getCursedBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(activeP2.getCursedBy());
                    }
                }
            }

            /*
             * Perish Song
             */
            //eingesetzt
            if((s.contains("|move|p1"))&&(s.contains("|Perish Song|"))) {
                activeP1.setPerishedBy(activeP1);
                activeP2.setPerishedBy(activeP1);
            }
            if((s.contains("|move|p2"))&&(s.contains("|Perish Song|"))) {
                activeP1.setPerishedBy(activeP2);
                activeP2.setPerishedBy(activeP2);
            }
            //Kill
            if((s.contains("|-start|p1"))&&(s.contains("|perish0"))) {
                activeP1.setDead(true);
                if(activeP1==activeP1.getPerishedBy()) activeP1.getLastDmgBy().killsPlus1();
                else activeP1.getPerishedBy().killsPlus1();
            }
            if((s.contains("|-start|p2"))&&(s.contains("|perish0"))) {
                activeP2.setDead(true);
                if(activeP2==activeP2.getPerishedBy()) activeP2.getLastDmgBy().killsPlus1();
                else activeP2.getPerishedBy().killsPlus1();
            }

            /*
             * Seeds
             */
            //Eingesetzt
            if((s.contains("|-start|p1"))&&(s.contains("|move: Leech Seed"))) {
                activeP1.setSeededBy(activeP2);
            }
            if((s.contains("|-start|p2"))&&(s.contains("|move: Leech Seed"))) {
                activeP2.setSeededBy(activeP1);
            }
            //Kill
            if(s.contains("|[from] Leech Seed")) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        activeP1.getSeededBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getSeededBy());
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        activeP2.getSeededBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(activeP2.getSeededBy());
                    }
                }
            }

            /*
             * Nightmare
             */
            //Eingesetzt
            if((s.contains("|-start|p1"))&&(s.contains("|Nightmare"))) {
                activeP1.setNightmaredBy(activeP2);
            }
            if((s.contains("|-start|p2"))&&(s.contains("|Nightmare"))) {
                activeP2.setNightmaredBy(activeP1);
            }
            //Kill
            if(s.contains("|[from] Nightmare")) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        activeP1.getNightmaredBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getNightmaredBy());
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        activeP2.getNightmaredBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(activeP2.getNightmaredBy());
                    }
                }
            }

            /*
             * Confusion
             */
            //Eingesetzt
            if((s.contains("|-start|p1"))&&(s.contains("|confusion"))) {
                activeP1.setConfusedBy(activeP2);
            }
            if((s.contains("|-start|p2"))&&(s.contains("|confusion"))) {
                activeP2.setConfusedBy(activeP1);
            }
            //Kill
            if(s.contains("|[from] confusion")) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        activeP1.getConfusedBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getConfusedBy());
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        activeP2.getConfusedBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(activeP2.getConfusedBy());
                    }
                }
            }

            /*
             * Doom Desire & Future Sight start
             */
            // Start
            if (s.contains("|-start|p1") && s.contains("Future Sight")){
                P1FutureSightBy = activeP1;
            }
            if (s.contains("|-start|p2") && s.contains("Future Sight")){
                P2FutureSightBy = activeP2;
            }
            if (s.contains("|-start|p1") && s.contains("Doom Desire")){
                P1DoomDesireBy = activeP1;
            }
            if (s.contains("|-start|p2") && s.contains("Doom Desire")){
                P2DoomDesireBy = activeP2;
            }

            // End
            if (s.contains("|-end|p2") && s.contains("Future Sight")){
                lastMove = P1FutureSightBy;
                P1FutureSight = true;
            }
            if (s.contains("|-end|p1") && s.contains("Future Sight")){
                lastMove = P2FutureSightBy;
                P2FutureSight = true;
            }
            if (s.contains("|-end|p2") && s.contains("Doom Desire")){
                lastMove = P1DoomDesireBy;
                P1DoomDesire = true;
            }
            if (s.contains("|-end|p1") && s.contains("Doom Desire")){
                lastMove = P2DoomDesireBy;
                P2DoomDesire = true;
            }

            /*
             * Salt Cure
             */
            //Eingesetzt
            if((s.contains("|-start|p1"))&&(s.contains("|Salt Cure"))) {
                activeP1.setSaltCuredBy(activeP2);
            }
            if((s.contains("|-start|p2"))&&(s.contains("|Salt Cure"))) {
                activeP2.setSaltCuredBy(activeP1);
            }

            //Kill
            if(s.contains("|[from] Salt Cure")) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        activeP1.getSaltCuredBy().killsPlus1();
                    } else {
                        activeP1.setSaltCuredBy(activeP1.getSaltCuredBy());
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        activeP2.getSaltCuredBy().killsPlus1();
                    } else {
                        activeP2.setSaltCuredBy(activeP2.getSaltCuredBy());
                    }
                }
            }

            /*
             * PSN BRN
             */
            //Eingesetzt
            if((s.contains("|-status|p1"))) {
                if(s.contains("|[of] p")) {
                    activeP1.setStatusedBy(activeP2);
                } else if(s.contains("|[from] item:")) {
                    activeP1.setStatusedBy(null);
                } else if(lastMove!=null) {
                    activeP1.setStatusedBy(lastMove);
                } else {
                    activeP1.setStatusedBy(p1.gettSpikesBy());
                }
            }
            if((s.contains("|-status|p2"))) {
                if(s.contains("|[of] p")) {
                    activeP2.setStatusedBy(activeP1);
                } else if(s.contains("|[from] item:")) {
                    activeP2.setStatusedBy(null);
                } else if(lastMove!=null) {
                    activeP2.setStatusedBy(lastMove);
                } else {
                    activeP2.setStatusedBy(p2.gettSpikesBy());
                }
            }
            //Kill
            if((s.contains("|[from] psn")) || (s.contains("|[from] brn"))) {
                if((s.contains("|-damage|p1"))) {
                    if(activeP1.getStatusedBy()!=null) {
                        if(s.contains("0 fnt")) {
                            activeP1.setDead(true);
                            activeP1.getStatusedBy().killsPlus1();
                        } else {
                            activeP1.setLastDmgBy(activeP1.getStatusedBy());
                        }
                    } else {
                        if(s.contains("0 fnt")) {
                            activeP1.setDead(true);
                            if(activeP1.getLastDmgBy()!=null) {
                                activeP1.getLastDmgBy().killsPlus1();
                            }else {
                                activeP2.killsPlus1();
                            }
                        }
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(activeP2.getStatusedBy()!=null) {
                        if(s.contains("0 fnt")) {
                            activeP2.setDead(true);
                            activeP2.getStatusedBy().killsPlus1();
                        } else {
                            activeP2.setLastDmgBy(activeP2.getStatusedBy());
                        }
                    } else {
                        if(s.contains("0 fnt")) {
                            activeP2.setDead(true);
                            if(activeP2.getLastDmgBy()!=null) {
                                activeP2.getLastDmgBy().killsPlus1();
                            }else {
                                activeP1.killsPlus1();
                            }
                        }
                    }
                }
            }


            /*
             * HJK, JK, Life Orb, Recoil, Black Sludge, Sticky Barb, Solar Power, Dry Skin, Mindblown, Steel Beam
             */
            if((s.contains("|[from] High Jump Kick")) ||
                    (s.contains("|[from] Jump Kick")) ||
                    (s.contains("|[from] item: Life Orb")) ||
                    (s.contains("|[from] Recoil")) ||
                    (s.contains("|[from] recoil")) ||
                    (s.contains("|[from] item: Black Sludge")) ||
                    (s.contains("|[from] item: Sticky Barb")) ||
                    (s.contains("|[from] ability: Solar Power")) ||
                    (s.contains("|[from] ability: Dry Skin")) ||
                    (s.contains("|[from] mindblown")) ||
                    (s.contains("|[from] steelbeam"))
            ) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        if(activeP1.getLastDmgBy()!=null) {
                            activeP1.getLastDmgBy().killsPlus1();
                        }else {
                            activeP2.killsPlus1();
                        }
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        if(activeP2.getLastDmgBy()!=null) {
                            activeP2.getLastDmgBy().killsPlus1();
                        }else {
                            activeP1.killsPlus1();
                        }
                    }
                }
            }

            /*
             * Liquid Ooze, Aftermath, Rough Skin, Iron Barbs, Bad Dreams, Rocky Helmet, Spiky Shield, Rowap Berry, Jaboca Berry
             */
            if((s.contains("|[from] jumpkick"))||(s.contains("|[from] highjumpkick"))||(s.contains("|[from] ability: Liquid Ooze"))||(s.contains("|[from] ability: Aftermath"))||(s.contains("|[from] ability: Rough Skin"))||(s.contains("|[from] ability: Iron Barbs"))
                    ||(s.contains("|[from] ability: Bad Dreams"))||(s.contains("|[from] item: Rocky Helmet"))||(s.contains("|[from] Spiky Shield"))||(s.contains("|[from] item: Rowap Berry"))
                    ||(s.contains("|[from] item: Jaboca Berry"))) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        activeP2.killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP2);
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        activeP1.killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(activeP1);
                    }
                }
            }

            /*
             * Powder
             */
            if((s.contains("|-damage|p1"))&&(s.contains("[silent]"))) {
                if(s.contains("0 fnt")) {
                    activeP1.setDead(true);
                    activeP2.killsPlus1();
                } else {
                    activeP1.setLastDmgBy(activeP2);
                }
            }
            if((s.contains("|-damage|p2"))&&(s.contains("[silent]"))) {
                if(s.contains("0 fnt")) {
                    activeP2.setDead(true);
                    activeP1.killsPlus1();
                } else {
                    activeP2.setLastDmgBy(activeP1);
                }
            }

            /*
             * Weather
             */
            //Eingesetzt
            if((s.contains("|-weather|"))) {
                if((s.contains("|[of] p1"))) {
                    weatherBy=activeP1;
                } else if((s.contains("|[of] p2"))) {
                    weatherBy=activeP2;
                } else if (!s.contains("|[upkeep]") && !s.contains("|none")){
                    weatherBy=lastMove;
                }
            }
            //Kill
            if((s.contains("|[from] Sandstorm"))||(s.contains("|[from] Hail"))) {
                if((s.contains("|-damage|p1"))) {
                    if (p2.getMons().contains(weatherBy)){ //Abfrage, ob das Weather von einem gegnerischem Mon kommt
                        if(s.contains("0 fnt")) {
                            activeP1.setDead(true);
                            weatherBy.killsPlus1();
                        } else {
                            activeP1.setLastDmgBy(weatherBy);
                        }
                    } else {
                        if(s.contains("0 fnt")) {
                            activeP1.setDead(true);
                            activeP1.getLastDmgBy().killsPlus1();
                        }
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if (p1.getMons().contains(weatherBy)){ //Abfrage, ob das Weather von einem gegnerischem Mon kommt
                        if(s.contains("0 fnt")) {
                            activeP2.setDead(true);
                            weatherBy.killsPlus1();
                        } else {
                            activeP2.setLastDmgBy(weatherBy);
                        }
                    } else {
                        if(s.contains("0 fnt")) {
                            activeP2.setDead(true);
                            activeP2.getLastDmgBy().killsPlus1();
                        }
                    }

                }
            }

            /*
             * Rocks
             */
            //Eingesetzt
            if((s.contains("|-sidestart|p1"))&&(s.contains("|move: Stealth Rock"))) {
                p1.setRocksBy(activeP2);
            }
            if((s.contains("|-sidestart|p2"))&&(s.contains("|move: Stealth Rock"))) {
                p2.setRocksBy(activeP1);
            }
            //Kill
            if((s.contains("|[from] Stealth Rock"))) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        p1.getRocksBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(p1.getRocksBy());
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        p2.getRocksBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(p2.getRocksBy());
                    }
                }
            }

            /*
             * Spikes
             */
            //Eingesetzt
            if((s.contains("|-sidestart|p1"))&&(s.contains("|Spikes"))) {
                p1.setSpikesBy(activeP2);
            }
            if((s.contains("|-sidestart|p2"))&&(s.contains("|Spikes"))) {
                p2.setSpikesBy(activeP1);
            }
            //Kill
            if((s.contains("|[from] Spikes"))) {
                if((s.contains("|-damage|p1"))) {
                    if(s.contains("0 fnt")) {
                        activeP1.setDead(true);
                        p1.getSpikesBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(p1.getSpikesBy());
                    }
                }
                if((s.contains("|-damage|p2"))) {
                    if(s.contains("0 fnt")) {
                        activeP2.setDead(true);
                        p2.getSpikesBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(p2.getSpikesBy());
                    }
                }
            }

            /*
             * TSpikes, Toxic Debris
             */
            //Eingesetzt
            if((s.contains("|-sidestart|p1"))&&(s.contains("|move: Toxic Spikes"))) {
                p1.settSpikesBy(activeP2);
            }
            if((s.contains("|-sidestart|p2"))&&(s.contains("|move: Toxic Spikes"))) {
                p2.settSpikesBy(activeP1);
            }

            /*
             * Lunar Dance, Healing Wish
             */
            if((s.contains("|Lunar Dance|"))||(s.contains("|Healing Wish|"))) {
                if((s.contains("|move|p1"))) {
                    if((!s.contains("|[still]"))) {
                        activeP1.setDead(true);
                        if(activeP1.getLastDmgBy()!=null) {
                            activeP1.getLastDmgBy().killsPlus1();
                        }else {
                            activeP2.killsPlus1();
                        }
                    }
                }
                if((s.contains("|move|p2"))) {
                    if((!s.contains("|[still]"))) {
                        activeP2.setDead(true);
                        if(activeP2.getLastDmgBy()!=null) {
                            activeP2.getLastDmgBy().killsPlus1();
                        }else {
                            activeP1.killsPlus1();
                        }
                    }
                }
            }

            /*
             * Final Gambit, Memento
             */
            if((s.contains("|Final Gambit|"))||(s.contains("|Memento|"))) {
                if((s.contains("|move|p1"))) {
                    if(!s.contains("|[notarget]") && !s.contains("|[still]")) {
                        activeP1.setDead(true);
                        if(activeP1.getLastDmgBy()!=null) {
                            activeP1.getLastDmgBy().killsPlus1();
                        }else {
                            activeP2.killsPlus1();
                        }
                    }
                }
                if((s.contains("|move|p2"))) {
                    if(!s.contains("|[notarget]") && !s.contains("|[still]")) {
                        activeP2.setDead(true);
                        if(activeP2.getLastDmgBy()!=null) {
                            activeP2.getLastDmgBy().killsPlus1();
                        }else {
                            activeP1.killsPlus1();
                        }
                    }
                }
            }

            /*
             * Explosion, Self Destruct
             */
            if((s.contains("|Explosion|"))||(s.contains("|Self-Destruct|"))) {
                if((s.contains("|move|p1"))) {
                    activeP1.setDead(true);
                    if(activeP1.getLastDmgBy()!=null) {
                        activeP1.getLastDmgBy().killsPlus1();
                    }else {
                        activeP2.killsPlus1();
                    }
                }
                if((s.contains("|move|p2"))) {
                    activeP2.setDead(true);
                    if(activeP2.getLastDmgBy()!=null) {
                        activeP2.getLastDmgBy().killsPlus1();
                    }else {
                        activeP1.killsPlus1();
                    }
                }
            }

            /*
             * Revival Blessing
             */
            if(s.contains("|[from] move: Revival Blessing")) {
                if (s.contains("|-heal|p1:")) {
                    p1.getMons().get(p1.indexOfNickname(s.split("\\|-heal\\|p1: ")[1].split("\\|")[0])).setDead(false);
                }
                if (s.contains("|-heal|p2:")) {
                    p2.getMons().get(p2.indexOfNickname(s.split("\\|-heal\\|p2: ")[1].split("\\|")[0])).setDead(false);
                }
            }

        }
        return new Player[] {p1, p2};

    }



    private static ArrayList<String> getGameArrayList (String link){
        URL url;
        try {
            url = new URL(link);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));

            String inputLine;
            ArrayList<String> h = new ArrayList<>();
            boolean isBull = true;

            while ((inputLine = in.readLine()) != null) {
                if(!isBull)
                    h.add(inputLine);

                if(inputLine.contains("|j|")) {
                    isBull = false;
                }

                if(inputLine.contains("|win|")) {
                    isBull = true;
                }
            }
            in.close();

            return h;
        } catch (IOException e) {
            throw new ArithmeticException("Fehler");
        }


    }

    /**
     * Just to test a local file, when offline
     */
    private static ArrayList<String> getGameArrayListLocalFile (){
        String link="C:\\";
        try {
            File f = new File(link);
            Scanner in = new Scanner(f);

            String inputLine;
            ArrayList<String> h = new ArrayList<>();
            boolean isBull = true;

            while (in.hasNextLine()) {
                inputLine = in.nextLine();
                if(!isBull)
                    h.add(inputLine);

                if(inputLine.contains("|j|")) {
                    isBull = false;
                }

                if(inputLine.contains("|win|")) {
                    isBull = true;
                }
            }
            in.close();

            return h;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new ArithmeticException("Fehler");
        }


    }
}
