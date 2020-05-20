package com.vonkez.utils;

import com.vonkez.ui.NavigationManager;

import java.util.ArrayList;

public class SearchboxManager {
    public static void search(String s) {
        System.out.println(s);
        NavigationManager.navigate(s);
        //NavigationManager.testNavigate();
    }
//
//    public ArrayList parse(String s){
//        if (!s.startsWith(">"))
//            return new ArrayList(new ArrayList());
//                //searchCatalog(s);
//        else {
//            var parsedCommands=  new ArrayList<>();
//            String[] commands = s.split(">");
//            for(String command: commands){
//                // first parameter is command
//                String[] parameters = command.split(";");
//                var combos = new ArrayList<String[]>();
//                for(String parameter: parameters){
//                    String[] combo = parameter.split(":");
//                    combos.add(combo);
//                }
//                parsedCommands.add(combos);
//            }
//            return parsedCommands;
//        }
//    }

}
