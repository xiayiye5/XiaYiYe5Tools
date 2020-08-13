package com.xiayiye5.sdktestdemo;


import androidx.core.content.FileProvider;

/**
 * @author Xiayiye5
 * 2020年8月5日11:09:51
 * 下面自定义FileProvider是为了解决游戏里面已定义了FileProvider的冲突问题
 * 如果你项目使用的最新androidX 库,FileProvider导入使用：androidx.core.content.FileProvider
 * 如果你使用的非androidX库,FileProvider导入使用：android.support.v4.content.FileProvider
 */
public class XiaYiYe5FileProvider extends FileProvider {
    public XiaYiYe5FileProvider() {
    }
}
