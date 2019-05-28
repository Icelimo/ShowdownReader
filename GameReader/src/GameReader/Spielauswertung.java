package GameReader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class Spielauswertung {

    public static Player[] werteAus(String link) {

        if(link.indexOf("http:")==0) {
            link="https:"+link.split("http:")[1];
        }

        @SuppressWarnings("unchecked")
        ArrayList<String> game = (ArrayList<String>) getGameArrayList(link).clone();

        Player p1 = new Player();
        Player p2 = new Player();

        for(String s : game) {
            //Nickname des Spielers
            if((s.indexOf("|player|p1")!=-1)&&(s.length()>11)) {
                p1.setNickname(s.split("\\|")[3]);
            }
            if((s.indexOf("|player|p2")!=-1)&&(s.length()>11)) {
                p2.setNickname(s.split("\\|")[3]);
            }

            //Welche Pokemon sind dabei?
            if(s.indexOf("|poke|p1")!=-1) {
                p1.getMons().add(new Pokemon(s.split("\\|")[3].split(",")[0]));
            }
            if(s.indexOf("|poke|p2")!=-1) {
                p2.getMons().add(new Pokemon(s.split("\\|")[3].split(",")[0]));
            }

            //Nicks & Detailschange
            if(s.indexOf("|switch|p1")!=-1||s.indexOf("|drag|p1")!=-1) {
                if(s.split("\\|")[3].split(",")[0].indexOf("Silvally")!=-1 && p1.indexOfName("Silvally-*")!=-1) {//Silvally-Problem
                    p1.getMons().get(p1.indexOfName("Silvally-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].indexOf("Arceus")!=-1 && p1.indexOfName("Arceus-*")!=-1) {//Arceus-Problem
                    p1.getMons().get(p1.indexOfName("Arceus-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].indexOf("Genesect")!=-1 && p1.indexOfName("Genesect-*")!=-1) {//Genesect-Problem
                    p1.getMons().get(p1.indexOfName("Genesect-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].indexOf("Gourgeist")!=-1 && p1.indexOfName("Gourgeist-*")!=-1) {//Genesect-Problem
                    p1.getMons().get(p1.indexOfName("Gourgeist-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }//hier
                try {
                    p1.getMons().get(p1.indexOfName(s.split("\\|")[3].split(",")[0])).setNickname(s.split("\\|")[2].substring(5));
                }
                catch(ArrayIndexOutOfBoundsException e) {}
            }
            if(s.indexOf("|switch|p2")!=-1||s.indexOf("|drag|p2")!=-1) {
                if(s.split("\\|")[3].split(",")[0].indexOf("Silvally")!=-1 && p2.indexOfName("Silvally-*")!=-1) {//Silvally-Problem
                    p2.getMons().get(p2.indexOfName("Silvally-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].indexOf("Arceus")!=-1 && p2.indexOfName("Arceus-*")!=-1) {//Arceus-Problem
                    p2.getMons().get(p2.indexOfName("Arceus-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].indexOf("Genesect")!=-1 && p2.indexOfName("Genesect-*")!=-1) {//Genesect-Problem
                    p2.getMons().get(p2.indexOfName("Genesect-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }
                if(s.split("\\|")[3].split(",")[0].indexOf("Gourgeist")!=-1 && p1.indexOfName("Gourgeist-*")!=-1) {//Genesect-Problem
                    p1.getMons().get(p1.indexOfName("Gourgeist-*")).setPokemon(s.split("\\|")[3].split(",")[0]);
                }//Hier
                try {
                    p2.getMons().get(p2.indexOfName(s.split("\\|")[3].split(",")[0])).setNickname(s.split("\\|")[2].substring(5));
                }
                catch(ArrayIndexOutOfBoundsException e) {}
            }

            //Win
            if(s.indexOf("|win|")!=-1) {
                if(p1.getNickname().equals(s.split("\\|")[2])) {
                    p1.setWinner(true);
                }
                if(p2.getNickname().equals(s.split("\\|")[2])) {
                    p2.setWinner(true);
                }
            }

            //Datailschange
            if(s.indexOf("|detailschange|p1")!=-1) {
                p1.getMons().get(p1.indexOfNick(s.split("\\|")[2].substring(5))).setPokemon(s.split("\\|")[3].split(",")[0]);
            }
            if(s.indexOf("|detailschange|p2")!=-1) {
                p2.getMons().get(p2.indexOfNick(s.split("\\|")[2].substring(5))).setPokemon(s.split("\\|")[3].split(",")[0]);
            }
        }



        Pokemon lastMove=null;
        Pokemon activeP1=null;
        Pokemon activeP2=null;
        Pokemon weatherBy=null;
        //Schaden
        for(String s : game) {
            /*
             * LastMove abspeichern
             */
            if(s.indexOf("|move|p1")!=-1) {
                lastMove=p1.getMons().get(p1.indexOfNick(s.split("\\|")[2].substring(5, s.split("\\|")[2].length())));
            }
            if(s.indexOf("|move|p2")!=-1) {
                lastMove=p2.getMons().get(p2.indexOfNick(s.split("\\|")[2].substring(5, s.split("\\|")[2].length())));
            }

            /*
             * LastMove am Ende der Runde löschen
             */
            if(s.indexOf("|turn|")!=-1) {
                lastMove=null;
            }

            /*
             * aktive Pokemon abspeichern und LastMove zuruecksetzen
             */
            System.out.println(s);
            if(s.indexOf("|switch|p1")!=-1||s.indexOf("|drag|p1")!=-1) {
                activeP1=p1.getMons().get(p1.indexOfNick(s.split("\\|")[2].substring(5, s.split("\\|")[2].length())));
                lastMove=null;
            }
            if(s.indexOf("|switch|p2")!=-1||s.indexOf("|drag|p2")!=-1) {
                activeP2=p2.getMons().get(p2.indexOfNick(s.split("\\|")[2].substring(5, s.split("\\|")[2].length())));
                lastMove=null;
            }

            /*
             * Direkte Hits, ausgenommen von CurseSD
             */
            if((s.indexOf("|-damage|p1")!=-1)&&(s.split("\\|").length==4)) {
                if(s.indexOf("0 fnt")!=-1) {
                    //Wenn CurseSD
                    if(lastMove==p1.getMons().get(p1.indexOfNick(s.split("\\|")[2].substring(5, s.split("\\|")[2].length())))) {
                        if(lastMove.getLastDmgBy()!=null) {
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
            if((s.indexOf("|-damage|p2")!=-1)&&(s.split("\\|").length==4)) {
                if(s.indexOf("0 fnt")!=-1) {
                    //Wenn CurseSD
                    if(lastMove==p2.getMons().get(p2.indexOfNick(s.split("\\|")[2].substring(5, s.split("\\|")[2].length())))) {
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

            /*
             * Klammerattacken
             */
            //Eingesetzt
            if((s.indexOf("|-activate|p1")!=-1)&&(s.split("\\|").length==5)) {

                activeP1.setBindedBy(activeP2);
            }
            if((s.indexOf("|-activate|p2")!=-1)&&(s.split("\\|").length==5)) {
                activeP2.setBindedBy(activeP1);
            }
            //Kill
            if(s.indexOf("partiallytrapped")!=-1) {
                if((s.indexOf("|-damage|p1")!=-1)&&(s.split("\\|").length==6)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        activeP1.getBindedBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getBindedBy());
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)&&(s.split("\\|").length==4)) {
                    if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|-activate|p1")!=-1)&&(s.indexOf("move: Destiny Bond")!=-1)) {
                activeP1.killsPlus1();
                activeP2.setDead(true);
            }
            if((s.indexOf("|-activate|p2")!=-1)&&(s.indexOf("move: Destiny Bond")!=-1)) {
                activeP2.killsPlus1();
                activeP1.setDead(true);
            }

            /*
             * Curse
             */
            //Eingesetzt
            if((s.indexOf("|-start|p1")!=-1)&&(s.indexOf("|Curse|")!=-1)) {
                activeP1.setCursedBy(activeP2);
            }
            if((s.indexOf("|-start|p2")!=-1)&&(s.indexOf("|Curse|")!=-1)) {
                activeP2.setCursedBy(activeP1);
            }
            //Kill
            if(s.indexOf("|[from] Curse")!=-1) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        activeP1.getCursedBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getCursedBy());
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|move|p1")!=-1)&&(s.indexOf("|Perish Song|")!=-1)) {
                activeP1.setPerishedBy(activeP1);
                activeP2.setPerishedBy(activeP1);
            }
            if((s.indexOf("|move|p2")!=-1)&&(s.indexOf("|Perish Song|")!=-1)) {
                activeP1.setPerishedBy(activeP2);
                activeP2.setPerishedBy(activeP2);
            }
            //Kill
            if((s.indexOf("|-start|p1")!=-1)&&(s.indexOf("|perish0")!=-1)) {
                activeP1.setDead(true);
                if(activeP1==activeP1.getPerishedBy()) activeP1.getLastDmgBy().killsPlus1();
                else activeP1.getPerishedBy().killsPlus1();
            }
            if((s.indexOf("|-start|p2")!=-1)&&(s.indexOf("|perish0")!=-1)) {
                activeP2.setDead(true);
                if(activeP2==activeP2.getPerishedBy()) activeP2.getLastDmgBy().killsPlus1();
                else activeP2.getPerishedBy().killsPlus1();
            }

            /*
             * Seeds
             */
            //Eingesetzt
            if((s.indexOf("|-start|p1")!=-1)&&(s.indexOf("|move: Leech Seed")!=-1)) {
                activeP1.setSeededBy(activeP2);
            }
            if((s.indexOf("|-start|p2")!=-1)&&(s.indexOf("|move: Leech Seed")!=-1)) {
                activeP2.setSeededBy(activeP1);
            }
            //Kill
            if(s.indexOf("|[from] Leech Seed")!=-1) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        activeP1.getSeededBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getSeededBy());
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|-start|p1")!=-1)&&(s.indexOf("|Nightmare")!=-1)) {
                activeP1.setNightmaredBy(activeP2);
            }
            if((s.indexOf("|-start|p2")!=-1)&&(s.indexOf("|Nightmare")!=-1)) {
                activeP2.setNightmaredBy(activeP1);
            }
            //Kill
            if(s.indexOf("|[from] Nightmare")!=-1) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        activeP1.getNightmaredBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getNightmaredBy());
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|-start|p1")!=-1)&&(s.indexOf("|confusion")!=-1)) {
                activeP1.setConfusedBy(activeP2);
            }
            if((s.indexOf("|-start|p2")!=-1)&&(s.indexOf("|confusion")!=-1)) {
                activeP2.setConfusedBy(activeP1);
            }
            //Kill
            if(s.indexOf("|[from] confusion")!=-1) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        activeP1.getConfusedBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP1.getConfusedBy());
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP2.setDead(true);
                        activeP2.getConfusedBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(activeP2.getConfusedBy());
                    }
                }
            }

            /*
             * PSN BRN
             */
            //Eingesetzt
            if((s.indexOf("|-status|p1")!=-1)) {
                if(s.indexOf("|[of] p")!=-1) {
                    activeP1.setStatusedBy(activeP2);
                } else if(lastMove!=null) {
                    activeP1.setStatusedBy(lastMove);
                } else {
                    activeP1.setStatusedBy(p1.gettSpikesBy());
                }
            }
            if((s.indexOf("|-status|p2")!=-1)) {
                if(s.indexOf("|[of] p")!=-1) {
                    activeP2.setStatusedBy(activeP1);
                } else if(lastMove!=null) {
                    activeP2.setStatusedBy(lastMove);
                } else {
                    activeP2.setStatusedBy(p2.gettSpikesBy());
                }
            }
            //Kill
            if((s.indexOf("|[from] psn")!=-1) || (s.indexOf("|[from] brn")!=-1)) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(activeP1.getStatusedBy()!=null) {
                        if(s.indexOf("0 fnt")!=-1) {
                            activeP1.setDead(true);
                            activeP1.getStatusedBy().killsPlus1();
                        } else {
                            activeP1.setLastDmgBy(activeP1.getStatusedBy());
                        }
                    } else {
                        if(s.indexOf("0 fnt")!=-1) {
                            activeP1.setDead(true);
                            if(activeP1.getLastDmgBy()!=null) {
                                activeP1.getLastDmgBy().killsPlus1();
                            }else {
                                activeP2.killsPlus1();
                            }
                        }
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(activeP2.getStatusedBy()!=null) {
                        if(s.indexOf("0 fnt")!=-1) {
                            activeP2.setDead(true);
                            activeP2.getStatusedBy().killsPlus1();
                        } else {
                            activeP2.setLastDmgBy(activeP2.getStatusedBy());
                        }
                    } else {
                        if(s.indexOf("0 fnt")!=-1) {
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
             * HJK, JK, Life Orb, Recoil, Black Sludge, Sticky Barb, Solar Power, Dry Skin
             */
            if((s.indexOf("|[from] High Jump Kick")!=-1)||(s.indexOf("|[from] Jump Kick")!=-1)||(s.indexOf("|[from] item: Life Orb")!=-1)||(s.indexOf("|[from] Recoil")!=-1) ||(s.indexOf("|[from] recoil")!=-1)
                    ||(s.indexOf("|[from] item: Black Sludge")!=-1)||(s.indexOf("|[from] item: Sticky Barb")!=-1)||(s.indexOf("|[from] ability: Solar Power")!=-1)||(s.indexOf("|[from] ability: Dry Skin")!=-1)) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        if(activeP1.getLastDmgBy()!=null) {
                            activeP1.getLastDmgBy().killsPlus1();
                        }else {
                            activeP2.killsPlus1();
                        }
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|[from] jumpkick")!=-1)||(s.indexOf("|[from] highjumpkick")!=-1)||(s.indexOf("|[from] ability: Liquid Ooze")!=-1)||(s.indexOf("|[from] ability: Aftermath")!=-1)||(s.indexOf("|[from] ability: Rough Skin")!=-1)||(s.indexOf("|[from] ability: Iron Barbs")!=-1)
                    ||(s.indexOf("|[from] ability: Bad Dreams")!=-1)||(s.indexOf("|[from] item: Rocky Helmet")!=-1)||(s.indexOf("|[from] Spiky Shield")!=-1)||(s.indexOf("|[from] item: Rowap Berry")!=-1)
                    ||(s.indexOf("|[from] item: Jaboca Berry")!=-1)) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        activeP2.killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(activeP2);
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|-damage|p1")!=-1)&&(s.indexOf("[silent]")!=-1)) {
                if(s.indexOf("0 fnt")!=-1) {
                    activeP1.setDead(true);
                    activeP2.killsPlus1();
                } else {
                    activeP1.setLastDmgBy(activeP2);
                }
            }
            if((s.indexOf("|-damage|p2")!=-1)&&(s.indexOf("[silent]")!=-1)) {
                if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|-weather|")!=-1)) {
                if((s.indexOf("|[of] p1")!=-1)) {
                    weatherBy=activeP1;
                } else if((s.indexOf("|[of] p2")!=-1)) {
                    weatherBy=activeP2;
                } else if (s.indexOf("|[upkeep]")==-1&&s.indexOf("|none")==-1){
                    weatherBy=lastMove;
                }
            }
            //Kill
            if((s.indexOf("|[from] Sandstorm")!=-1)||(s.indexOf("|[from] Hail")!=-1)) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        weatherBy.killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(weatherBy);
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP2.setDead(true);
                        weatherBy.killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(weatherBy);
                    }
                }
            }

            /*
             * Rocks
             */
            //Eingesetzt
            if((s.indexOf("|-sidestart|p1")!=-1)&&(s.indexOf("|move: Stealth Rock")!=-1)) {
                p1.setRocksBy(activeP2);
            }
            if((s.indexOf("|-sidestart|p2")!=-1)&&(s.indexOf("|move: Stealth Rock")!=-1)) {
                p2.setRocksBy(activeP1);
            }
            //Kill
            if((s.indexOf("|[from] Stealth Rock")!=-1)) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        p1.getRocksBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(p1.getRocksBy());
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
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
            if((s.indexOf("|-sidestart|p1")!=-1)&&(s.indexOf("|Spikes")!=-1)) {
                p1.setSpikesBy(activeP2);
            }
            if((s.indexOf("|-sidestart|p2")!=-1)&&(s.indexOf("|Spikes")!=-1)) {
                p2.setSpikesBy(activeP1);
            }
            //Kill
            if((s.indexOf("|[from] Spikes")!=-1)) {
                if((s.indexOf("|-damage|p1")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP1.setDead(true);
                        p1.getSpikesBy().killsPlus1();
                    } else {
                        activeP1.setLastDmgBy(p1.getSpikesBy());
                    }
                }
                if((s.indexOf("|-damage|p2")!=-1)) {
                    if(s.indexOf("0 fnt")!=-1) {
                        activeP2.setDead(true);
                        p2.getSpikesBy().killsPlus1();
                    } else {
                        activeP2.setLastDmgBy(p2.getSpikesBy());
                    }
                }
            }

            /*
             * TSpikes
             */
            //Eingesetzt
            if((s.indexOf("|-sidestart|p1")!=-1)&&(s.indexOf("|move: Toxic Spikes")!=-1)) {
                p1.settSpikesBy(activeP2);
            }
            if((s.indexOf("|-sidestart|p2")!=-1)&&(s.indexOf("|move: Toxic Spikes")!=-1)) {
                p2.settSpikesBy(activeP1);
            }

            /*
             * Lunar Dance, Healing Wish
             */
            if((s.indexOf("|Lunar Dance|")!=-1)||(s.indexOf("|Healing Wish|")!=-1)) {
                if((s.indexOf("|move|p1")!=-1)) {
                    if((s.indexOf("|[still]")==-1)) {
                        activeP1.setDead(true);
                        if(activeP1.getLastDmgBy()!=null) {
                            activeP1.getLastDmgBy().killsPlus1();
                        }else {
                            activeP2.killsPlus1();
                        }
                    }
                }
                if((s.indexOf("|move|p2")!=-1)) {
                    if((s.indexOf("|[still]")==-1)) {
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
            if((s.indexOf("|Final Gambit|")!=-1)||(s.indexOf("|Memento|")!=-1)) {
                if((s.indexOf("|move|p1")!=-1)) {
                    if((s.indexOf("|[notarget]")==-1)) {
                        activeP1.setDead(true);
                        if(activeP1.getLastDmgBy()!=null) {
                            activeP1.getLastDmgBy().killsPlus1();
                        }else {
                            activeP2.killsPlus1();
                        }
                    }
                }
                if((s.indexOf("|move|p2")!=-1)) {
                    if((s.indexOf("|[notarget]")==-1)) {
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
            if((s.indexOf("|Explosion|")!=-1)||(s.indexOf("|Self-Destruct|")!=-1)) {
                if((s.indexOf("|move|p1")!=-1)) {
                    activeP1.setDead(true);
                    if(activeP1.getLastDmgBy()!=null) {
                        activeP1.getLastDmgBy().killsPlus1();
                    }else {
                        activeP2.killsPlus1();
                    }
                }
                if((s.indexOf("|move|p2")!=-1)) {
                    activeP2.setDead(true);
                    if(activeP2.getLastDmgBy()!=null) {
                        activeP2.getLastDmgBy().killsPlus1();
                    }else {
                        activeP1.killsPlus1();
                    }
                }
            }
        }
        System.out.println(p1.getMons().get(0).getPokemon()+" "+p1.getMons().get(0).isDead()+" Kills:"+p1.getMons().get(0).getKills());
        System.out.println(p1.getMons().get(1).getPokemon()+" "+p1.getMons().get(1).isDead()+" Kills:"+p1.getMons().get(1).getKills());
        System.out.println(p1.getMons().get(2).getPokemon()+" "+p1.getMons().get(2).isDead()+" Kills:"+p1.getMons().get(2).getKills());
        System.out.println(p1.getMons().get(3).getPokemon()+" "+p1.getMons().get(3).isDead()+" Kills:"+p1.getMons().get(3).getKills());
        System.out.println(p1.getMons().get(4).getPokemon()+" "+p1.getMons().get(4).isDead()+" Kills:"+p1.getMons().get(4).getKills());
        System.out.println(p1.getMons().get(5).getPokemon()+" "+p1.getMons().get(5).isDead()+" Kills:"+p1.getMons().get(5).getKills());
        System.out.println(p2.getMons().get(0).getPokemon()+" "+p2.getMons().get(0).isDead()+" Kills:"+p2.getMons().get(0).getKills());
        System.out.println(p2.getMons().get(1).getPokemon()+" "+p2.getMons().get(1).isDead()+" Kills:"+p2.getMons().get(1).getKills());
        System.out.println(p2.getMons().get(2).getPokemon()+" "+p2.getMons().get(2).isDead()+" Kills:"+p2.getMons().get(2).getKills());
        System.out.println(p2.getMons().get(3).getPokemon()+" "+p2.getMons().get(3).isDead()+" Kills:"+p2.getMons().get(3).getKills());
        System.out.println(p2.getMons().get(4).getPokemon()+" "+p2.getMons().get(4).isDead()+" Kills:"+p2.getMons().get(4).getKills());
        System.out.println(p2.getMons().get(5).getPokemon()+" "+p2.getMons().get(5).isDead()+" Kills:"+p2.getMons().get(5).getKills());
        System.out.println("Player 1 wins(" + p1.getNickname() + "): " + p1.isWinner());
        System.out.println("Player 2 wins(" + p2.getNickname() + "): " + p2.isWinner());

        return new Player[] {p1, p2};


    }



    public static ArrayList<String> getGameArrayList (String link){
        URL url;
        try {
            url = new URL(link);
            HttpURLConnection httpcon = (HttpURLConnection) url.openConnection();
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0");

            BufferedReader in = new BufferedReader(new InputStreamReader(httpcon.getInputStream()));

            String inputLine;
            ArrayList<String> h = new ArrayList<>();
            ArrayList<String> bull = new ArrayList<>();
            boolean isBull = true;

            while ((inputLine = in.readLine()) != null) {
                if(isBull) bull.add(inputLine);
                else {
                    h.add(inputLine);
                }

                if(inputLine.indexOf("|j|")!=-1) {
                    isBull = false;
                }

                if(inputLine.indexOf("|win|")!=-1) {
                    isBull = true;
                }
            }
            in.close();

            return h;
        } catch (IOException e) {
            throw new ArithmeticException("Fehler");
        }


    }
}
