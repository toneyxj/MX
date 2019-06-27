package com.moxi.bookstore.modle;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by xiajun on 2019/6/26.
 */

public class LogoDataDb extends DataSupport {
    @Column(unique = true)
    public long id;
    public long time;
    public String permanent_id;

    public String user_id;

    public String event_id  ;

    public String from_platform ;

    public String banben=null;

}
