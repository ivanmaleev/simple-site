package com.maleev.simple.utils;

import com.maleev.simple.model.entity.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "<none>";
    }
}