package com.weiping.InteProperty.trademark.core;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class TradeMarkSearcherJSON implements Serializable{
    public String applicationNumber;
    public String categoryNumber;
    public String searchMethod;
    public String searchContent;

    public String searchType;

    public JSONObject toJson(){
        try{
            //JSONObject jsonObj = new JSONObject();
            JSONObject searcherJsonObject = new JSONObject();
            if (null != applicationNumber){
                searcherJsonObject.put(Constants.JSON_NAME_APPLICATION_NUMBER, applicationNumber);
            }
            if (null != categoryNumber){
                searcherJsonObject.put(Constants.JSON_NAME_CATEGORY_NUMBER, categoryNumber);
            }
            if (null != searchMethod){
                searcherJsonObject.put(Constants.JSON_NAME_SEARCH_METHOD, searchMethod);
            }
            if (null != searchContent){
                searcherJsonObject.put(Constants.JSON_NAME_SEARCH_CONTENT, URLEncoder.encode(URLEncoder.encode(searchContent, "UTF-8"), "UTF-8"));
            }
            if (null != searchType){
                searcherJsonObject.put(Constants.JSON_NAME_SEARCH_TYPE, searchType);
            }

            //jsonObj.put(searchType, searcherJsonObject);
            return searcherJsonObject;
        }catch (JSONException e){
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void setApplicationNumber(String string){
        applicationNumber = string;
    }
    public String getApplicationNumber(){
        return applicationNumber;
    }
    public void setCategoryNumber(String string){
        categoryNumber = string;
    }
    public String getCategoryNumber(){
        return categoryNumber;
    }
    public void setSearchMethod(String string){
        searchMethod = string;
    }
    public String getSearchMethod(){
        return searchMethod;
    }
    public void setSearchContent(String string){
        searchContent = string;
    }
    public String getSearchContent(){
        return searchContent;
    }
    public void setSearchType(String string){
        searchType = string;
    }
    public String getSearchType(){
        return searchType;
    }
}
