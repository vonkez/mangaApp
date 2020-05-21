package com.vonkez.utils;

import com.vonkez.ui.NavigationManager;


public class SearchboxManager {
    public static void search(String s) {
        System.out.println(s);
        NavigationManager.navigate(s);
        //NavigationManager.testNavigate();
    }

}
