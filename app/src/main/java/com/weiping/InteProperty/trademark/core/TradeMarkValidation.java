package com.weiping.InteProperty.trademark.core;


import com.weiping.common.StringUtility;

import java.util.Map;

public class TradeMarkValidation {
    public static boolean validateSimilarSearchInput(Map dataMap){
        boolean error = false;
        String category = (String)dataMap.get(Constants.SUBMIT_DATA_CATEGORYNUM);
        String content = (String)dataMap.get(Constants.SUBMIT_DATA_SEARCHCONTENT);
        String searchMethod = (String)dataMap.get(Constants.SUBMIT_DATA_SEARCHMETHOD);
        if (Integer.valueOf(category) == 0)
            return true;
        if (StringUtility.isEmptyString(content)){
            return true;
        }
        if("NUM".equalsIgnoreCase(searchMethod)){
            if(!content.matches("[0-9]+")){
                return true;
            }
        }
        return error;
    }
}
