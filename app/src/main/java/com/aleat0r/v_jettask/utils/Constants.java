package com.aleat0r.v_jettask.utils;

/**
 * Created by Aleksandr Kovalenko on 11.08.2016.
 */
public class Constants {

    //    Facebook
    public static final String[] FACEBOOK_PERMISSIONS_FOR_LOGIN = {"email", "public_profile", "user_birthday"};
    public static final String FACEBOOK_PERMISSION_FOR_POST = "publish_actions";
    public static final String FACEBOOK_FIELDS = "name, email, gender, birthday";

    //    Twitter
    public static final String TWITTER_KEY = "gZLWHIv76SHxpCuUqxzyVqAg3";
    public static final String TWITTER_SECRET = "RHsvQbGdwse01f8d7afuv3hpp3bFovsVaanvvvAvQ4tQxXkdfv";

    //    Vkontakte
    public static final String VKONTAKTE_FIElDS = "first_name, last_name, bdate, photo_max";

    //    Social Networks
    public static final String SOC_NETWORK_FACEBOOK = "facebook";
    public static final String SOC_NETWORK_GOOGLE_PLUS = "google_plus";
    public static final String SOC_NETWORK_TWITTER = "twitter";
    public static final String SOC_NETWORK_VKONTAKTE = "vkontakte";

    //    Intent extra data
    public static final String SOC_NETWORK_INTENT_EXTRA = "social_network";
}
