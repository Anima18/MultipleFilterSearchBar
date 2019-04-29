package com.anima.multiplefiltersearchbar.popupview;

import java.util.List;
import java.util.Map;

/**
 * Created by jianjianhong on 19-4-8
 */
public interface OnDataChangeListener {
    void dataRequest();
    void dataCompletion(List<Map<String, String>> dataList);
    void dataError(String message);
}
