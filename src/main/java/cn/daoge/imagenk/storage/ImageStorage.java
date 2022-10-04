package cn.daoge.imagenk.storage;


import java.awt.*;
import java.util.Set;

/**
 * 图片提供源接口
 */
public interface ImageStorage {
    /**
     * 通过名称获取图片
     */
    Image get(String name);

    /**
     * 获取存在的所有图片名称
     */
    Set<String> getAll();
}