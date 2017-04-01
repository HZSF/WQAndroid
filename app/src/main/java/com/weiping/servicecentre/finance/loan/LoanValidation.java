package com.weiping.servicecentre.finance.loan;

import com.weiping.common.StringUtility;

public class LoanValidation {
    public int validateCreditFormMandatoryFields(LoanForm loanForm){
        int errorFieldIndex = -1;
        String[] fields = loanForm.getFormFields();
        for(int i = 0; i < fields.length; i++){
            if(StringUtility.isEmptyString(fields[0])){
                errorFieldIndex = i;
                break;
            }
        }
        return errorFieldIndex;
    }
}
