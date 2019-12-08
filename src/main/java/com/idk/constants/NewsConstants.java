package com.idk.constants;

public class NewsConstants {

    public static final String BROKER_URL = "vm://embedded-broker?broker.persistent=false";

    public static final String SUBSCRIBE_NEWS_EVENT = "Subscribe_News_Event";
    public static final String READ_NEWS_EVENT = "Read_News_Event";
    public static final String ADD_NEWS_EVENT = "Add_News_Event";
    public static final String DELETE_NEWS_EVENT = "Deleted_News_Event";
    public static final String UPDATE_NEWS_EVENT = "Deleted_News_Event";

    //RECEIVERS
    public static final String DESTINATION_RECEIVED_SUBSCRIPTIONS = "Destination_Received_Subscriptions";
    public static final String DESTINATION_REQUESTED_NEWS_READ = "Destination_Requested_News_Read";
    public static final String DESTINATION_REQUESTED_NEWS_DELETED = "Destination_Requested_News_Deleted";
    public static final String DESTINATION_REQUESTED_NEWS_UPDATED = "Destination_Requested_News_Deleted";

}
