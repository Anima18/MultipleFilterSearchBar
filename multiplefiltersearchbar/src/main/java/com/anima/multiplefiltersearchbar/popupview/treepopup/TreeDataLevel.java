package com.anima.multiplefiltersearchbar.popupview.treepopup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianjianhong on 19-4-8
 */
public class TreeDataLevel {
    private String code;
    private String name;
    private int level;
    private List<TreeDataLevel> childs;

    public TreeDataLevel(String code, String name, int level) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.childs = new ArrayList<>();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<TreeDataLevel> getChilds() {
        return childs;
    }

    public void setChilds(List<TreeDataLevel> childs) {
        this.childs = childs;
    }


    public void addChild(TreeDataLevel level) {
        this.childs.add(level);
    }
}
