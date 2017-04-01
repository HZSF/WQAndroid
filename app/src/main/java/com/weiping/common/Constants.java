package com.weiping.common;

public class Constants {
    public final static String URLTRADEMARKNAMING = "http://www.qiming2.com/Search/";
    public final static String URL_TRADEMARK_IMAGE = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_showPdfToImageInfo.xhtml?";// + "regNum=....&intcls=."(registrationNumber, categoryNumber)
    public final static String URL_TRADEMARK_BY_REG_NUM = "http://sbcx.saic.gov.cn:9080/tmois/wsztcx_getListByRegNum.xhtml?regNum=";
    public final static String URL_TRADEMARK_APPLY_FLOW_DETAIL = "http://sbcx.saic.gov.cn:9080/tmois/wsztcx_getStatesByRegInt.xhtml?";// + "regNum=...&intcls=."(registrationNumber, categoryNumber)
    public final static String URL_TRADEMARK_INTEGRATED = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_pageZhcxMain.xhtml?";
    public final static String URL_TRADEMARK_INTEGRATED_PRECISION = "http://sbcx.saic.gov.cn:9080/tmois/wszhcx_getLikeCondition.xhtml?";
    public final static String URL_TRADEMARK = "http://120.55.96.224:8080/wService/weiwei/intellecture/trademark";
    public final static String URL_PATENT_INTEGRATED = "http://120.55.96.224/searchResult.ashx?";
    public final static String URL_PATENT_INTEGRATED_DETAIL = "http://120.55.96.224/detail.aspx?";
    public final static String URL_PATENT_STATUS_SEARCH = "http://120.55.96.224/ajaxCall/getPatentStatus.ashx?";
    public final static String URL_ANNOUNCE = "http://120.55.96.224:8080/wService/weiwei/centreservice/announcement";
    public final static String URL_CS_TRAIN_COURSES = "http://120.55.96.224:8080/wService/weiwei/centreservice/course";
    public final static String URL_GET_PROVINCE = "http://120.55.96.224:8080/wService/weiwei/province";
    public final static String URL_GET_CITY = "http://120.55.96.224:8080/wService/weiwei/city";

///*
    public final static String URL_VERSION = "http://120.55.96.224:8080/wService/weiwei/version";
    public final static String URL_REGISTER_CODE_GET = "http://120.55.96.224:8080/wService/sweiwei/registerCode";
    public final static String URL_REGISTER = "http://120.55.96.224:8080/wService/sweiwei/register";
    public final static String URL_REGISTER_VERIFY_CODE = "http://120.55.96.224:8080/wService/sweiwei/verifyRegister";
    public final static String URL_AUTHENTICATE = "http://120.55.96.224:8080/wService/sweiwei/authenticate";
    public final static String URL_TOKEN_VALIDATION_PING = "http://120.55.96.224:8080/wService/sweiwei/ping";
    public final static String URL_LOGOUT = "http://120.55.96.224:8080/wService/sweiwei/logout";
    public final static String URL_GET_USER_INFO = "http://120.55.96.224:8080/wService/sweiwei/getUserInfo";
    public final static String URL_UPD_USER_INFO = "http://120.55.96.224:8080/wService/sweiwei/setUserInfo";
    public final static String URL_UPD_USER_INFO_AREA = "http://120.55.96.224:8080/wService/sweiwei/setUserInfoArea";
    public final static String URL_UPD_USER_INFO_IMG = "http://120.55.96.224:8080/wService/sweiwei/setUserInfoImg";
    public final static String URL_GET_USER_INFO_IMG = "http://120.55.96.224:8080/wService/sweiwei/getUserInfoImg";
    public final static String URL_TRADEMARK_ADD_MONITOR = "http://120.55.96.224:8080/wService/sweiwei/trademark/add/monitor";
    public final static String URL_TRADEMARK_GET_MONITOR = "http://120.55.96.224:8080/wService/sweiwei/trademark/get/monitor";
    public final static String URL_TRADEMARK_CANCEL_MONITOR = "http://120.55.96.224:8080/wService/sweiwei/trademark/delete/monitor";
    public final static String URL_TRADEMARK_ADD_SELL = "http://120.55.96.224:8080/wService/sweiwei/trademark/add/sell";
    public final static String URL_TRADEMARK_GET_TRADE = "http://120.55.96.224:8080/wService/weiwei/trademark/trade";
    public final static String URL_PATENT_ANNUAL_FEE_MONITOR = "http://120.55.96.224:8080/wService/sweiwei/patent/AnnualFeeMonitor";
    public final static String URL_PATENT_ACHIEVE_TRANSFORM = "http://120.55.96.224:8080/wService/weiwei/patent/achieveTransform";
    public final static String URL_PATENT_ADD_ACHI_TRANS = "http://120.55.96.224:8080/wService/sweiwei/patent/add/AchieveTrans";
    public final static String URL_PATENT_ADD_ANN_FEE_MON = "http://120.55.96.224:8080/wService/sweiwei/patent/add/AnnFeeMon";
    public final static String URL_PATENT_DEL_ANN_FEE_MON = "http://120.55.96.224:8080/wService/sweiwei/patent/delete/AnnFeeMon";
    public final static String URL_PATENT_DELEGATE_ANN_FEE_MON = "http://120.55.96.224:8080/wService/sweiwei/patent/delegate/AnnFeeMon";
    public final static String URL_CS_FINANCE_LOAN_SUBMIT = "http://120.55.96.224:8080/wService/sweiwei/centreservice/finance/loan/submitform";
    public final static String URL_CS_FINANCE_LEND_SUBMIT = "http://120.55.96.224:8080/wService/sweiwei/centreservice/finance/onlend/submitform";
    public final static String URL_CS_TRAIN_COURSES_REGISTER = "http://120.55.96.224:8080/wService/sweiwei/centreservice/registerCourse";
    public final static String URL_CS_HEALTHY_APPOINT = "http://120.55.96.224:8080/wService/sweiwei/centreservice/healthcheck/book";
    public final static String URL_CS_HEALTHY_APPOINTED = "http://120.55.96.224:8080/wService/sweiwei/centreservice/healthcheck/booked";
    public final static String URL_CS_HEALTHY_CANCEL_APPOINT = "http://120.55.96.224:8080/wService/sweiwei/centreservice/healthcheck/cancelbook";
    public final static String URL_CS_INSPECTION_WOOD_APPOINT = "http://120.55.96.224:8080/wService/sweiwei/centreservice/inspection/book/wood";
    public final static String URL_CS_INSPECTION_KIDS_APPOINT = "http://120.55.96.224:8080/wService/sweiwei/centreservice/inspection/book/kids";
    public final static String URL_CS_INSPECTION_TEXTILE_APPOINT = "http://120.55.96.224:8080/wService/sweiwei/centreservice/inspection/book/textile";
    public final static String URL_MEMBER_FINANCE_APPOINTED_LOAN = "http://120.55.96.224:8080/wService/sweiwei/centreservice/finance/loan/applied";
    public final static String URL_MEMBER_FINANCE_APPOINTED_CREDIT_LOAN = "http://120.55.96.224:8080/wService/sweiwei/centreservice/finance/loan/creditLoanApplied";
    public final static String URL_MEMBER_FINANCE_CANCEL_APPOINTED_CREDIT_LOAN = "http://120.55.96.224:8080/wService/sweiwei/centreservice/finance/cancel/creditLoan";
    public final static String URL_MEMBER_FINANCE_APPOINTED_LEND = "http://120.55.96.224:8080/wService/sweiwei/centreservice/finance/loan/lendingApplied";
    public final static String URL_MEMBER_FINANCE_CANCEL_APPOINTED_LEND = "http://120.55.96.224:8080/wService/sweiwei/centreservice/finance/cancel/lend";
    public final static String URL_MEMBER_QUALITY_APPOINTED_INSPECTION = "http://120.55.96.224:8080/wService/sweiwei/centreservice/inspection/booked";
    public final static String URL_MEMBER_QUALITY_CANCEL_APPOINTED_INSPECTION_WOOD = "http://120.55.96.224:8080/wService/sweiwei/centreservice/inspection/cancel/wood";
    public final static String URL_MEMBER_QUALITY_CANCEL_APPOINTED_INSPECTION_KIDS = "http://120.55.96.224:8080/wService/sweiwei/centreservice/inspection/cancel/kids";
    public final static String URL_MEMBER_QUALITY_CANCEL_APPOINTED_INSPECTION_TEXTILE = "http://120.55.96.224:8080/wService/sweiwei/centreservice/inspection/cancel/textile";
    public final static String URL_MEMBER_TRAIN_REGISTERED_COURSE = "http://120.55.96.224:8080/wService/sweiwei/centreservice/retrieveRegisteredCourse";
    public final static String URL_MEMBER_COURSES_DEREGISTER = "http://120.55.96.224:8080/wService/sweiwei/centreservice/removeRegisteredCourse";
    public final static String URL_ANNOUNCE_COMMENT = "http://120.55.96.224:8080/wService/sweiwei/comment/announce/add";
    public final static String URL_ANNOUNCE_COMMENT_FETCH = "http://120.55.96.224:8080/wService/weiwei/announce/comment";
    public final static String URL_ANNOUNCE_COMMENT_LIKE = "http://120.55.96.224:8080/wService/sweiwei/comment/announce/likeUnlike";
    public final static String URL_PROPERTY_ADD_SELL = "http://120.55.96.224:8080/wService/sweiwei/property/add/sell";
    public final static String URL_PROPERTY_FETCH_SELL = "http://120.55.96.224:8080/wService/weiwei/property/fetch/sell";
    public final static String URL_PROPERTY_ADD_FAVORITE = "http://120.55.96.224:8080/wService/sweiwei/property/add/favorites";
    public final static String URL_PROPERTY_CANCEL_FAVORITE = "http://120.55.96.224:8080/wService/sweiwei/property/cancel/favorites";
    public final static String URL_PROPERTY_ADD_LEND = "http://120.55.96.224:8080/wService/sweiwei/property/add/lend";
    public final static String URL_PROPERTY_FETCH_LEND = "http://120.55.96.224:8080/wService/weiwei/property/fetch/lend";
    public final static String URL_PROPERTY_FETCH_FAVORITE_SELL = "http://120.55.96.224:8080/wService/sweiwei/property/fetch/favorites/sell";
    public final static String URL_PROPERTY_FETCH_FAVORITE_LEND = "http://120.55.96.224:8080/wService/sweiwei/property/fetch/favorites/lend";
//*/

   /*
    public final static String URL_VERSION = "http://10.0.2.2:8080/weiwei/version";
    public final static String URL_REGISTER_CODE_GET = "http://10.0.2.2:8080/sweiwei/registerCode";
    public final static String URL_REGISTER = "http://10.0.2.2:8080/sweiwei/register";
    public final static String URL_REGISTER_VERIFY_CODE = "http://10.0.2.2:8080/sweiwei/verifyRegister";
    public final static String URL_AUTHENTICATE = "http://10.0.2.2:8080/sweiwei/authenticate";
    public final static String URL_TOKEN_VALIDATION_PING = "http://10.0.2.2:8080/sweiwei/ping";
    public final static String URL_LOGOUT = "http://10.0.2.2:8080/sweiwei/logout";
    public final static String URL_GET_USER_INFO = "http://10.0.2.2:8080/sweiwei/getUserInfo";
    public final static String URL_UPD_USER_INFO = "http://10.0.2.2:8080/sweiwei/setUserInfo";
    public final static String URL_UPD_USER_INFO_AREA = "http://10.0.2.2:8080/sweiwei/setUserInfoArea";
    public final static String URL_UPD_USER_INFO_IMG = "http://10.0.2.2:8080/sweiwei/setUserInfoImg";
    public final static String URL_GET_USER_INFO_IMG = "http://10.0.2.2:8080/sweiwei/getUserInfoImg";
    public final static String URL_TRADEMARK_ADD_MONITOR = "http://10.0.2.2:8080/sweiwei/trademark/add/monitor";
    public final static String URL_TRADEMARK_GET_MONITOR = "http://10.0.2.2:8080/sweiwei/trademark/get/monitor";
    public final static String URL_TRADEMARK_CANCEL_MONITOR = "http://10.0.2.2:8080/sweiwei/trademark/delete/monitor";
    public final static String URL_TRADEMARK_ADD_SELL = "http://10.0.2.2:8080/sweiwei/trademark/add/sell";
    public final static String URL_TRADEMARK_GET_TRADE = "http://10.0.2.2:8080/weiwei/trademark/trade";
    public final static String URL_PATENT_ANNUAL_FEE_MONITOR = "http://10.0.2.2:8080/sweiwei/patent/AnnualFeeMonitor";
    public final static String URL_PATENT_ACHIEVE_TRANSFORM = "http://10.0.2.2:8080/weiwei/patent/achieveTransform";
    public final static String URL_PATENT_ADD_ACHI_TRANS = "http://10.0.2.2:8080/sweiwei/patent/add/AchieveTrans";
    public final static String URL_PATENT_ADD_ANN_FEE_MON = "http://10.0.2.2:8080/sweiwei/patent/add/AnnFeeMon";
    public final static String URL_PATENT_DEL_ANN_FEE_MON = "http://10.0.2.2:8080/sweiwei/patent/delete/AnnFeeMon";
    public final static String URL_PATENT_DELEGATE_ANN_FEE_MON = "http://10.0.2.2:8080/sweiwei/patent/delegate/AnnFeeMon";
    public final static String URL_CS_FINANCE_LOAN_SUBMIT = "http://10.0.2.2:8080/sweiwei/centreservice/finance/loan/submitform";
    public final static String URL_CS_FINANCE_LEND_SUBMIT = "http://10.0.2.2:8080/sweiwei/centreservice/finance/onlend/submitform";
    public final static String URL_CS_TRAIN_COURSES_REGISTER = "http://10.0.2.2:8080/sweiwei/centreservice/registerCourse";
    public final static String URL_CS_HEALTHY_APPOINT = "http://10.0.2.2:8080/sweiwei/centreservice/healthcheck/book";
    public final static String URL_CS_HEALTHY_APPOINTED = "http://10.0.2.2:8080/sweiwei/centreservice/healthcheck/booked";
    public final static String URL_CS_HEALTHY_CANCEL_APPOINT = "http://10.0.2.2:8080/sweiwei/centreservice/healthcheck/cancelbook";
    public final static String URL_CS_INSPECTION_WOOD_APPOINT = "http://10.0.2.2:8080/sweiwei/centreservice/inspection/book/wood";
    public final static String URL_CS_INSPECTION_KIDS_APPOINT = "http://10.0.2.2:8080/sweiwei/centreservice/inspection/book/kids";
    public final static String URL_CS_INSPECTION_TEXTILE_APPOINT = "http://10.0.2.2:8080/sweiwei/centreservice/inspection/book/textile";
    public final static String URL_MEMBER_FINANCE_APPOINTED_LOAN = "http://10.0.2.2:8080/sweiwei/centreservice/finance/loan/applied";
    public final static String URL_MEMBER_FINANCE_APPOINTED_CREDIT_LOAN = "http://10.0.2.2:8080/sweiwei/centreservice/finance/loan/creditLoanApplied";
    public final static String URL_MEMBER_FINANCE_CANCEL_APPOINTED_CREDIT_LOAN = "http://10.0.2.2:8080/sweiwei/centreservice/finance/cancel/creditLoan";
    public final static String URL_MEMBER_FINANCE_APPOINTED_LEND = "http://10.0.2.2:8080/sweiwei/centreservice/finance/loan/lendingApplied";
    public final static String URL_MEMBER_FINANCE_CANCEL_APPOINTED_LEND = "http://10.0.2.2:8080/sweiwei/centreservice/finance/cancel/lend";
    public final static String URL_MEMBER_QUALITY_APPOINTED_INSPECTION = "http://10.0.2.2:8080/sweiwei/centreservice/inspection/booked";
    public final static String URL_MEMBER_QUALITY_CANCEL_APPOINTED_INSPECTION_WOOD = "http://10.0.2.2:8080/sweiwei/centreservice/inspection/cancel/wood";
    public final static String URL_MEMBER_QUALITY_CANCEL_APPOINTED_INSPECTION_KIDS = "http://10.0.2.2:8080/sweiwei/centreservice/inspection/cancel/kids";
    public final static String URL_MEMBER_QUALITY_CANCEL_APPOINTED_INSPECTION_TEXTILE = "http://10.0.2.2:8080/sweiwei/centreservice/inspection/cancel/textile";
    public final static String URL_MEMBER_TRAIN_REGISTERED_COURSE = "http://10.0.2.2:8080/sweiwei/centreservice/retrieveRegisteredCourse";
    public final static String URL_MEMBER_COURSES_DEREGISTER = "http://10.0.2.2:8080/sweiwei/centreservice/removeRegisteredCourse";
    public final static String URL_ANNOUNCE_COMMENT = "http://10.0.2.2:8080/sweiwei/comment/announce/add";
    public final static String URL_ANNOUNCE_COMMENT_FETCH = "http://10.0.2.2:8080/weiwei/announce/comment";
    public final static String URL_ANNOUNCE_COMMENT_LIKE = "http://10.0.2.2:8080/sweiwei/comment/announce/likeUnlike";
    public final static String URL_PROPERTY_ADD_SELL = "http://10.0.2.2:8080/sweiwei/property/add/sell";
    public final static String URL_PROPERTY_FETCH_SELL = "http://10.0.2.2:8080/weiwei/property/fetch/sell";
    public final static String URL_PROPERTY_ADD_FAVORITE = "http://10.0.2.2:8080/sweiwei/property/add/favorites";
    public final static String URL_PROPERTY_CANCEL_FAVORITE = "http://10.0.2.2:8080/sweiwei/property/cancel/favorites";
    public final static String URL_PROPERTY_ADD_LEND = "http://10.0.2.2:8080/sweiwei/property/add/lend";
    public final static String URL_PROPERTY_FETCH_LEND = "http://10.0.2.2:8080/weiwei/property/fetch/lend";
    public final static String URL_PROPERTY_FETCH_FAVORITE_SELL = "http://10.0.2.2:8080/sweiwei/property/fetch/favorites/sell";
    public final static String URL_PROPERTY_FETCH_FAVORITE_LEND = "http://10.0.2.2:8080/sweiwei/property/fetch/favorites/lend";
    */

    public final static String Wechat_APP_ID = "wx251238d643d95b1c";
    public final static String Webo_APP_KEY = "3987740948";

    public final static int STATUS_UNAUTHORIZED = 401;
    public final static int STATUS_CONFLICT = 409;
    public final static int STATUS_CONFLICT_PHONE = 418;

    public final static String JSON_NAME_RESPONSE_OBJECT = "responseObjectList";
    public final static String IS_USER_LOGIN = "logged";
    public final static String SHARE_PREFERENCE_FILE = "com.weiping.registration";
    public final static String SHARE_PREFERENCE_USERNAME = "username";
    final public static String HEAD_AUTH_TOKEN = "X-Auth-Token";

    final public static String EVENT_FAIL = "fail";
    final public static String EVENT_SUCCESS = "success";
    final public static String EVENT_EXISTED = "existed";
    final public static String YES = "Y";
    final public static String NO = "N";
    final public static String UNAUTHORIZED = "Unauthorized";

}
