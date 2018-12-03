package ridersadvisor.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ridersadvisor.com.models.MySiteDetailVo;

/**
 * Created by Sudhir Singh on 16,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class AddSiteDetailPrefs {

    public static void storeOfflineImages(Context context,
                                          List<MySiteDetailVo> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences("ADWISE",
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString("SITEDETAIL", jsonFavorites);

        editor.commit();
    }

    public static ArrayList<MySiteDetailVo> loadOfflineSiteData(Context context) {
        SharedPreferences settings;
        List<MySiteDetailVo> favorites;

        settings = context.getSharedPreferences("ADWISE",
                Context.MODE_PRIVATE);
        if (settings.contains("SITEDETAIL")) {
            String jsonFavorites = settings.getString("SITEDETAIL", null);
            Gson gson = new Gson();
            MySiteDetailVo[] favoriteItems = gson.fromJson(jsonFavorites,
                    MySiteDetailVo[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<MySiteDetailVo>(favorites);
        } else
            return null;

        return (ArrayList<MySiteDetailVo>) favorites;
    }

    public static void addFormData(Context context,
                                   MySiteDetailVo OfflineImage) {
        List<MySiteDetailVo> favorites = loadOfflineSiteData(context);
        if (favorites == null)
            favorites = new ArrayList<MySiteDetailVo>();
        favorites.add(OfflineImage);
        storeOfflineImages(context, favorites);
    }

    public static void removeOfflineSiteData(Context context, int position) {
        ArrayList<MySiteDetailVo> favorites = loadOfflineSiteData(context);
        if (favorites != null) {
            favorites.remove(position);
            storeOfflineImages(context, favorites);
        }
    }

    public static boolean isSiteExist(Context context, String id) {
        ArrayList<MySiteDetailVo> favorites = loadOfflineSiteData(context);
        boolean isSiteExist=false;
        if (favorites != null) {
            for (int counter=0;counter<favorites.size();counter++)
            {
                if(favorites.get(counter).getId().equalsIgnoreCase(id)){
                    isSiteExist=true;
                    break;
                }
            }

        }

        return  isSiteExist;
    }

    public static void deleteAllOfflineImage(Context context) {
        ArrayList<MySiteDetailVo> favorites = loadOfflineSiteData(context);
        if (favorites != null) {
            favorites.clear();
            storeOfflineImages(context, favorites);
        }
    }

}
