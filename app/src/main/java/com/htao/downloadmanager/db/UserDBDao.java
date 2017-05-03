package com.htao.downloadmanager.db;

import com.htao.downloadmanager.MyApplication;

/**
 * Created by Administrator on 2017/5/3 0003.
 */

public class UserDBDao {
    public void insertUser(User user){
        MyApplication.getDaoInstant().getUserDao().insert(user);
        MyApplication.getDaoInstant().getUserDao().update(user);
        MyApplication.getDaoInstant().getUserDao().queryBuilder().where(UserDao.Properties.Name.eq("liming")).list();
        MyApplication.getDaoInstant().getUserDao().queryBuilder().where(UserDao.Properties.Age.eq(10)).list();
        MyApplication.getDaoInstant().getUserDao().queryBuilder().whereOr(UserDao.Properties.Name.eq("liming"),UserDao.Properties.Age.eq(10)).list();
    }
}
