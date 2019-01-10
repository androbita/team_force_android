package com.app.salesapp.fcm;

import android.content.Context;

import com.app.salesapp.SalesApp;
import com.app.salesapp.user.UserService;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public abstract class FCMCommand {

    private static final String comment = "COMMENT";

    public static void processMessage(Context context, Map<String, String> data) {

        Map<String, FCMCommand> commandMap = new HashMap<>();
        commandMap.put(comment, new CommentCommand());

        FCMCommand fcmCommand = commandMap.get(data.get("type"));
        fcmCommand.executeMessage(context, data);
    }

    abstract void executeMessage(Context context, Map<String, String> data);
}
