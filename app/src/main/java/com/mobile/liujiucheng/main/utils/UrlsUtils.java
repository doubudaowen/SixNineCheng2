package com.mobile.liujiucheng.main.utils;

public class UrlsUtils {

    //熊猫合租的上传接口
    public static final String urlUploadPandaRelease="http://114.215.102.96:8288/69cheng-app/house/publishJointRentHouse";
    //添加室友的上传接口
    public static final String urlAddRoommate="http://114.215.102.96:8288/69cheng-app/tenant/publishRenant";
    //发布求租的上传接口
    public static final String urlRelease="http://114.215.102.96:8288/69cheng-app/tenant/publish";
    //修改求租信息的接口
    public static final String urlReleaseModify="http://114.215.102.96:8288/69cheng-app/tenant/updateTenant";


    //我的发布中，“二手房”、“熊猫合租”、“出租房”列表的信息请求接口
    public static final String urlMyReleaseSecond="http://114.215.102.96:8288/69cheng-app/servlet/ServerHouseInterfaceServlet";
    //获取求租信息详情的接口
    public static final String urlReleaseLoadInfo="http://114.215.102.96:8288/69cheng-app/tenant/tenantByUserId";
    public static final String getUrlReleaseLoadInfo="http://114.215.102.96:8288/69cheng-app/tenant/tenantDetailByUserId";
    //熊猫合租的室友详情
    public static final String urlReleaseRoommateInfo="http://114.215.102.96:8288/69cheng-app/tenant/getJointTenantDetail";


    //我的发布中删除合租接口
    public static final String urlMyReleasePandaDelete="http://114.215.102.96:8288/69cheng-app/house/delJointHouse";
    //我的发布中删除二手房接口
    public static final String urlMyReleaseSecondDelete="http://114.215.102.96:8288/69cheng-app/second/delSecond";
    //我的发布中删除出租房的接口
    public static final String urlMyReleaseRentDelete="http://114.215.102.96:8288/69cheng-app/house/delThouse";
    //删除求租信息的接口
    public static final String urlReleaseDelete="http://114.215.102.96:8288/69cheng-app/tenant/delTenant";

    //我的发布中二手房修改接口
    public static final String urlMyReleaseSecondModify="http://114.215.102.96:8288/69cheng-app/second/updSecond";
    //我的发布中出租房修改接口
    public static final String urlMyReleaseRentModify2="http://114.215.102.96:8288/69cheng-app/house/updThouse";
    //我的发布中熊猫合租的修改接口
    public static final String urlMyReleasePandaModify="http://114.215.102.96:8288/69cheng-app/house/updJointHouse";
    //我的发布中添加室友的修改接口
    public static final String urlReleaseRoommateModify="http://114.215.102.96:8288/69cheng-app/tenant/updJointTenant";



    public static final String urlHtml = "http://tieba.baidu.com/f?kw=%E5%85%AD%E4%B9%9D%E5%9F%8E&ie=utf-8";
	public static final String urls = "http://114.215.102.96:8288/69cheng-app/servlet/ServerHouseInterfaceServlet";  //区域选择接口


	public static final String urlUpload = "http://114.215.102.96:8288/69cheng-app/servlet/ServerUploadSecondServlet";
	public static final String urlUploadHouse = "http://114.215.102.96:8288/69cheng-app/servlet/ServerUploadHouseServlet";

	public static final String urlUploadImage = "http://114.215.102.96:8288/69cheng-app/user/updateHeadIMG";

	public static final String urlFuzzy = "http://114.215.102.96:8288/69cheng-app/servlet/ServerInterfaceServlet";

//	public static final String urlLoadingH = "http://114.215.102.96:8288/69cheng-app/servlet/ServerInterfaceServlet";
	// 模糊查询
	public static final String urlLoading = "http://114.215.102.96:8288/69cheng-app/house/searchHouse";

	public static final String urlLoadingSecond = "http://114.215.102.96:8288/69cheng-app/second/searchSecond";
	//合作申请
	public static final String urlApplication = "http://114.215.102.96:8288/69cheng-app/cooperation/saveCooperration";
	//版本升级
	public static final String Versionupdate = "http://114.215.102.96:8288/69cheng-app/servlet/ServerInterfaceServlet";
	//下载新的版本号
	public static final String upVersionCode = "http://114.215.102.96:8288/69cheng-app/app/SixNineCheng.apk";
	//public static final String urlLoading = "http://192.168.0.254:8080/69cheng-app/house/searchHouse";
	//public static final String urlLoadingSecond = "http://192.168.0.254:8080/69cheng-app/second/searchSecond";


    //DJJ
    //精准求租接口
    public static final String acquireRentList = "http://114.215.102.96:8288/69cheng-app/tenant/findTenant";
    //精准求租详情页
    public static final String acquireDetail = "http://114.215.102.96:8288/69cheng-app/tenant/tenantDetail";
    //精准求租收藏
    public static final String acquireCollection = "http://114.215.102.96:8288/69cheng-app/collect/collectTenant";
    //删除精准求租收藏
    public static final String acquireDelCollection = "http://114.215.102.96:8288/69cheng-app/collect/delTenantCollect";
    //获取用户收藏的求租信息
    public static final String getAcquireCollectionList = "http://114.215.102.96:8288/69cheng-app/collect/getCollectTenant";

    //熊猫合租列表
    public static final String pandaList = "http://114.215.102.96:8288/69cheng-app/house/getAllJoint";
    //熊猫合租详情页
    public static final String pandaDetail = "http://114.215.102.96:8288/69cheng-app/house/getJointDetail";
    //熊猫合租收藏接口
    public static final String pandaCollection = "http://114.215.102.96:8288/69cheng-app/servlet/ServerHouseInterfaceServlet";
    //熊猫合租删除收藏接口
    public static final String pandaDelCollection = "http://114.215.102.96:8288/69cheng-app/servlet/ServerHouseInterfaceServlet";
    //熊猫合租收藏列表
    public static final String pandaCollectionList = "http://114.215.102.96:8288/69cheng-app/collect/getJointCollect";

    //忘记密码验证码
    public static final String forgetPasswordCode = "http://114.215.102.96:8288/69cheng-app/user/getCodeByForget";
    //忘记密码
    public static final String forgetPassword = "http://114.215.102.96:8288/69cheng-app/user/updatePWDByForget";
    //登陆接口
    public static final String urlLoadingDeng = "http://114.215.102.96:8288/69cheng-app/servlet/ServerInterfaceServlet";

    //修改密码短信验证
    public static final String urlModificationCode = "http://114.215.102.96:8288/69cheng-app/servlet/ServerInterfaceServlet?type=getVerifiableCode";
    //修改密码
    public static final String urlModification = "http://114.215.102.96:8288/69cheng-app/servlet/ServerInterfaceServlet?type=registerAndModifyPwd";

    //修改我的用户名
    public static final String urlModifyName = "http://114.215.102.96:8288/69cheng-app/user/updateName";
    //轮播图
    public static final String urlLoadingH = "http://114.215.102.96:8288/69cheng-app/servlet/ServerInterfaceServlet";
}
