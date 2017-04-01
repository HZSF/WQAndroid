package com.weiping.servicecentre.finance.loan;

import org.json.JSONException;
import org.json.JSONObject;

public class LoanForm {

    protected String reg_capital;
    protected String total_asset;
    protected String total_debt;
    protected String bank_loan;
    protected String other_debt;
    protected String owner_equality;
    protected String asset_liability_ratio;
    protected String main_product;
    protected String productivity;
    protected String production_capacity;
    protected String industrial_output;
    protected String sales_income;
    protected String total_profit;
    protected String total_tax;
    protected String added_value_tax;
    protected String loan_purpose;//loan_purpose
    protected String loan_limit;
    protected String loan_period; //loan_period;

    protected String[] formFields = new String[]{
            reg_capital,
            total_asset,
            total_debt,
            bank_loan,
            other_debt,
            owner_equality,
            asset_liability_ratio,
            main_product,
            productivity,
            production_capacity,
            industrial_output,
            sales_income,
            total_profit,
            total_tax,
            added_value_tax,
            loan_purpose,
            loan_limit,
            loan_period
    };

    public final static String[] JSON_CS_FINANCE_LOAN_FORM_FIELDS = new String[]{
            "reg_capital",
            "total_asset",
            "total_debt",
            "bank_loan",
            "other_debt",
            "owner_equality",
            "asset_liability_ratio",
            "main_product",
            "productivity",
            "production_capacity",
            "industrial_output",
            "sales_income",
            "total_profit",
            "total_tax",
            "added_value_tax",
            "loan_purpose",
            "loan_limit",
            "loan_period"
    };

    public JSONObject toJson(){
        try{
            JSONObject formJsonObject = new JSONObject();
            for(int i=0; i<formFields.length; i++){
                String fieldName = formFields[i];
                if(null != fieldName){
                    formJsonObject.put(JSON_CS_FINANCE_LOAN_FORM_FIELDS[i], fieldName);
                }

            }
            return formJsonObject;
        }catch (JSONException e){
            e.printStackTrace();
        }
        return null;
    }

    public void setFields(String[] strings){
        if(strings.length == formFields.length) {
            for (int i = 0; i < formFields.length; i++) {
                formFields[i] = strings[i];
            }
        }
    }

    public String[] getFormFields(){
        return formFields;
    }
}
