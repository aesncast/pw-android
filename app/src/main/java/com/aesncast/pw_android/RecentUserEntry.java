package com.aesncast.pw_android;

public class RecentUserEntry {
    public final String domainName;
    public final String userName;
    public final String sequenceName;

    public RecentUserEntry(String domainName, String userName, String sequenceName) {
        this.domainName = domainName;
        this.userName = userName;
        this.sequenceName = sequenceName;
    }
}
