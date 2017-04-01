package com.weiping.InteProperty.trademark.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class NamingAreaCompleteAdapter extends BaseAdapter implements Filterable{

    private Activity mActivity;
    private List<String> resultList = new ArrayList<>();
    private String[] areaNumArray;

    public NamingAreaCompleteAdapter(Activity activity){
        super();
        mActivity = activity;
    }

    public List getAllItems(){
        return resultList;
    }
    public String getAreaNum(int position){
        if(areaNumArray.length > position) {
            return areaNumArray[position];
        }else{
            return null;
        }
    }
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int position) {
        return resultList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
        }
        ((TextView)convertView.findViewById(android.R.id.text1)).setText(getItem(position));
        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null){
                    List<String> strings = null;
                    try {
                        String submitData = constraint.toString();
                        if(submitData.contains("（群组")) {
                            submitData = submitData.substring(0, submitData.lastIndexOf("（群组"));
                        }
                        strings = findArea(submitData);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    filterResults.values = strings;
                    filterResults.count = strings.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if(results != null && results.count>0){
                    resultList = (List<String>) results.values;
                    notifyDataSetChanged();
                }else{
                    notifyDataSetInvalidated();
                }
            }
        };
        return filter;
    }

    private List<String> findArea(String string){
        try {
            String unicodeValue = escape(string);
            String Url = "http://www.qiming2.com/GetSers.asp?s=" + unicodeValue;
            runThread(Url);
        } catch (Exception e){
            return null;
        }
        return null;
    }

    private String escape(String src){
        int i;
        char j;
        StringBuffer tmp = new StringBuffer();
        tmp.ensureCapacity(src.length() * 6);
        for(i=0; i<src.length(); i++){
            j = src.charAt(i);
            if(Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j)){
                tmp.append(j);
            }else if(j < 256){
                tmp.append("%");
                if(j < 16){
                    tmp.append("0");
                }
                tmp.append(Integer.toString(j, 16));
            }else {
                tmp.append("%u");
                tmp.append(Integer.toString(j, 16));
            }
        }
        return tmp.toString();
    }

    class simJavaScriptInterface {
        public Context ctx;
        public simJavaScriptInterface(Context c){
            ctx = c;
        }
        @JavascriptInterface
        public void showHTML(String html){
            String result = html;
            List<String> list = new ArrayList<>();
            if (result != null) {
                StringBuilder b = new StringBuilder(result);
                b.replace(result.lastIndexOf("]"), result.lastIndexOf("]")+1, "");
                b.replace(result.indexOf("["), result.indexOf("[")+1, "");
                result = b.toString();
                result = result.replace("'", "");
                areaNumArray = result.split(",");
                for (int i = 0; i < areaNumArray.length; i++) {
                    String item_str = areaNumArray[i];
                    if(item_str.contains("@@")) {
                        item_str = item_str.substring(0, item_str.indexOf("@@"));
                        if (item_str.contains("::")) {
                            item_str = item_str.replace("::", "（群组");
                            item_str += "）";
                        }
                        list.add(item_str);
                    }
                }
            }
            resultList = list;
            notifyDataSetChanged();
        }
    }

    public void runThread(final String Url){
        mActivity.runOnUiThread(new Thread(new Runnable() {
            @Override
            public void run() {
                final WebView webView = new WebView(mActivity);
                webView.getSettings().setJavaScriptEnabled(true);
                simJavaScriptInterface sim = new simJavaScriptInterface(mActivity);
                webView.addJavascriptInterface(sim, "HtmlViewer");
                webView.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageFinished(WebView view, String url) {
                        webView.loadUrl("javascript:window.HtmlViewer.showHTML(document.getElementsByTagName('body')[0].innerHTML);");
                    }
                });
                webView.loadUrl(Url);
            }
        }));
    }
}
