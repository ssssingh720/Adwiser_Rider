package ridersadvisor.com.manager;


import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {
    private volatile static SharedPrefManager instance;
    private String PREFS_NAME = "CLASS_TAB";
    private Context context;

    public SharedPrefManager() {

    }

    public static SharedPrefManager getInstance() {
        if (instance == null) {
            synchronized (SharedPrefManager.class) {
                instance = new SharedPrefManager();
            }
        }
        return instance;
    }

    public void init(Context context) {
        this.context = context;
    }

    /*
     * Method to Set the SharedPreferneces data
     *
     * @param key A String value specifying the key value
     *
     * @param value The boolean value to be stored
     */
    public SharedPrefManager setSharedData(String key, Boolean value) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putBoolean(key, value);
        editor.commit();
        return this;
    }

    /*
     * Method to Set the SharedPreferneces data
     *
     * @param key An String value specifying the key value
     *
     * @param value The integer value to be stored
     */
    public SharedPrefManager setSharedData(String key, int value) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt(key, value);
        editor.commit();
        return this;
    }

    /*
     * Method to Set the SharedPreferneces data
     *
     * @param key A String value specifying the key value
     *
     * @param value The String value to be stored
     */
    public SharedPrefManager setSharedData(String key, String value) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString(key, value);
        editor.commit();
        return this;
    }

    /*
     * Method to Set the SharedPreferneces data
     *
     * @param key A String value specifying the key value
     *
     * @param value The Long value to be stored
     */
    public SharedPrefManager setSharedData(String key, Long value) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.putLong(key, value);
        editor.commit();
        return this;
    }

    /*
     * Method to retrieve the SharedPreferneces data
     *
     * @param key The key of which the value has to be retrieved
     *
     * @return The boolean value which was saved
     */
    public Boolean getSharedDataBoolean(String key) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        return shared.getBoolean(key, false);
    }

    /*
     * Method to retrieve the SharedPreferneces data
     *
     * @param key The key of which the value has to be retrieved
     *
     * @return The string value which was saved
     */
    public String getSharedDataString(String key) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        return shared.getString(key, "");
    }

    /*
     * Method to retrieve the SharedPreferneces data
     *
     * @param key The key of which the value has to be retrieved
     *
     * @return The string value which was saved
     */
    public Long getSharedDataLong(String key) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        return shared.getLong(key, 0);
    }

    /*
     * Method to retrieve the SharedPreferneces data
     *
     * @param key The key of which the value has to be retrieved
     *
     * @return The integer value which was saved
     */
    public int getSharedDataInt(String key) {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        return shared.getInt(key, 0);
    }

//    public void storeBookMark(
//            List<Bookmark> favorites) {
//        SharedPreferences settings;
//        SharedPreferences.Editor editor;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        editor = settings.edit();
//
//        Gson gson = new Gson();
//        String jsonFavorites = gson.toJson(favorites);
//
//        editor.putString(BOOKMARK_STORY, jsonFavorites);
//
//        editor.commit();
//    }
//
//    public ArrayList<Bookmark> getBookmark() {
//        SharedPreferences settings;
//        List<Bookmark> favorites;
//
//        settings = context.getSharedPreferences(PREFS_NAME,
//                Context.MODE_PRIVATE);
//        if (settings.contains(BOOKMARK_STORY)) {
//            String jsonFavorites = settings.getString(BOOKMARK_STORY, null);
//            Gson gson = new Gson();
//            Bookmark[] favoriteItems = gson.fromJson(jsonFavorites,
//                    Bookmark[].class);
//            favorites = Arrays.asList(favoriteItems);
//            favorites = new ArrayList<Bookmark>(favorites);
//        } else
//            return null;
//
//        return (ArrayList<Bookmark>) favorites;
//    }
//
//    public void addBookmark(
//            Bookmark offersDetails) {
//        List<Bookmark> favorites = getBookmark();
//        if (favorites == null)
//            favorites = new ArrayList<Bookmark>();
//        favorites.add(offersDetails);
//        storeBookMark(favorites);
//    }
//
//    public void removeBookmark( int position) {
//        ArrayList<Bookmark> favorites = getBookmark();
//        if (favorites != null) {
//            favorites.remove(position);
//            storeBookMark(favorites);
//        }
//    }
//
//    public void deleteAllBookmark() {
//        ArrayList<Bookmark> favorites = getBookmark();
//        if (favorites != null) {
//            favorites.clear();
//            storeBookMark(favorites);
//        }
//    }


    public void clearPreference() {
        SharedPreferences shared = context.getSharedPreferences(
                PREFS_NAME, 0);
        SharedPreferences.Editor editor = shared.edit();
        editor.clear();
        editor.commit();
    }
}
