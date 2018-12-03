package ridersadvisor.com.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ridersadvisor.com.models.MySiteImageVo;

/**
 * Created by Sudhir Singh on 12,April,2018
 * ESS,
 * B-65,Sector 63,Noida.
 */
public class OfflineCaptureImagesPrefs {

    public static void storeOfflineImages(Context context,
                                          List<MySiteImageVo> favorites) {
        SharedPreferences settings;
        SharedPreferences.Editor editor;

        settings = context.getSharedPreferences("ADWISE",
                Context.MODE_PRIVATE);
        editor = settings.edit();

        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);

        editor.putString("GDLASTSEENOFFERS", jsonFavorites);

        editor.commit();
    }

    public static ArrayList<MySiteImageVo> loadOfflineImages(Context context) {
        SharedPreferences settings;
        List<MySiteImageVo> favorites;

        settings = context.getSharedPreferences("ADWISE",
                Context.MODE_PRIVATE);
        if (settings.contains("GDLASTSEENOFFERS")) {
            String jsonFavorites = settings.getString("GDLASTSEENOFFERS", null);
            Gson gson = new Gson();
            MySiteImageVo[] favoriteItems = gson.fromJson(jsonFavorites,
                    MySiteImageVo[].class);
            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<MySiteImageVo>(favorites);
        } else
            return null;

        return (ArrayList<MySiteImageVo>) favorites;
    }

    public static void addOfflineImage(Context context,
                                       MySiteImageVo OfflineImage) {
        List<MySiteImageVo> favorites = loadOfflineImages(context);
        if (favorites == null)
            favorites = new ArrayList<MySiteImageVo>();
        favorites.add(OfflineImage);
        storeOfflineImages(context, favorites);
    }

    public static ArrayList<MySiteImageVo> getOfflineImage(Context context, String id) {
        ArrayList<MySiteImageVo> favorites = loadOfflineImages(context);
        ArrayList<MySiteImageVo> offlineImageList = new ArrayList<>();
        if (favorites != null) {
            for (int counter = 0; counter < favorites.size(); counter++) {
                if (favorites.get(counter).getSiteId().equalsIgnoreCase(id)) {
                    offlineImageList.add(favorites.get(counter));
                }
            }
        }

        return offlineImageList;
    }


    public static void removeOfflineImage(Context context, String siteId) {
        ArrayList<MySiteImageVo> favorites = loadOfflineImages(context);
        if (favorites != null) {
            for (int counter = 0; counter < favorites.size(); counter++) {
                if (favorites.get(counter).getSiteId().equalsIgnoreCase(siteId)) {
                    favorites.remove(counter);
                }
            }
            storeOfflineImages(context, favorites);
        }
    }

    public static void deleteAllOfflineImage(Context context) {
        ArrayList<MySiteImageVo> favorites = loadOfflineImages(context);
        if (favorites != null) {
            favorites.clear();
            storeOfflineImages(context, favorites);
        }
    }

}
