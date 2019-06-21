package cn.cdjzxy.monitoringassistant.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.wonders.health.lib.base.mvp.IView;
import com.wonders.health.lib.base.mvp.Message;
import com.wonders.health.lib.base.utils.ArtUtils;

import cn.cdjzxy.monitoringassistant.mvp.model.entity.upload.PreciptationSampForm;
import cn.cdjzxy.monitoringassistant.mvp.presenter.ApiPresenter;
import cn.cdjzxy.monitoringassistant.mvp.ui.module.task.TaskDetailActivity;

/**
 * 帮助索贝传的测试类
 */
public class MyTextUtils {

    public static String TEXT_DATA = "{\n" +
            "\t\"DelFiles\": [],\n" +
            "\t\"IsAdd\": false,\n" +
            "\t\"IsSubmit\": false,\n" +
            "\t\"SampForm\": {\n" +
            "\t\t\"AddTime\": \"2019-06-21 15:26:41\",\n" +
            "\t\t\"AddressId\": \"0f4fd32d-fd61-43da-bac6-f518e5ef6bb4\",\n" +
            "\t\t\"AddressName\": \"废气机电\",\n" +
            "\t\t\"Comment\": \"bb\",\n" +
            "\t\t\"CurUserId\": \"6e2c449a-9ce1-412b-bac8-ff7c369814e0\",\n" +
            "\t\t\"CurUserName\": \"Admin\",\n" +
            "\t\t\"DeviceId\": \"0be28726-c603-4c3d-a616-23fbc0111678\",\n" +
            "\t\t\"DeviceName\": \"电导率仪DDS-307A(610612030024)\",\n" +
            "\t\t\"FormFlows\": \"[{\\\"FlowId\\\":\\\"38233438-bcbc-44a0-801c-ac185d2861f5\\\",\\\"FlowName\\\":\\\"送样\\\",\\\"NodeNumber\\\":1,\\\"CurrentStatus\\\":0,\\\"IsJoinFlow\\\":false,\\\"NodeHandleCode\\\":\\\"0\\\",\\\"AllFlowUsers\\\":null,\\\"FlowUserIds\\\":\\\"\\\",\\\"FlowUserNames\\\":\\\"\\\"}]\",\n" +
            "\t\t\"FormName\": \"空气（无组织废气）采样及样品交接记录\",\n" +
            "\t\t\"FormPath\": \"/FormTemplate/FILL_KQCY_GAS_XD\",\n" +
            "\t\t\"FormType\": \"ae5de2f9-a359-4dfa-b81d-d93a8b895780\",\n" +
            "\t\t\"FormTypeName\": \"空气\",\n" +
            "\t\t\"Id\": \"6091a5b3-28e2-434f-8eb5-f807629ce5c4\",\n" +
            "\t\t\"MethodId\": \"76c0354a-908b-4c22-9fbd-f01f13d6190e\",\n" +
            "\t\t\"MethodName\": \"大气污染物无组织排放监测技术导则(HJ/T55-2000)\",\n" +
            "\t\t\"Montype\": 2,\n" +
            "\t\t\"ParentTagId\": \"ae5de2f9-a359-4dfa-b81d-d93a8b895780\",\n" +
            "\t\t\"PrivateData\": \"{\\\"ClientName\\\":\\\"成都协力魔芋科学种植加工园有限公司\\\",\\\"DeviceText\\\":\\\"电导率仪DDS-307A(610612030024)(检定 2020-05-28)\\\",\\\"SeriesGroup\\\":[{\\\"Index\\\":0,\\\"IsChecked\\\":false,\\\"MonitemId\\\":\\\"d2d5ba7d-80e0-4221-b628-72c3224a68d6\\\",\\\"MonitemName\\\":\\\"碳\\\",\\\"SeriesCnt\\\":\\\"2\\\",\\\"SeriesComb\\\":false}],\\\"SourceDate\\\":\\\"2020-05-28\\\",\\\"SourceWay\\\":\\\"检定\\\"}\",\n" +
            "\t\t\"ProjectId\": \"c9da1936-d833-4a1a-b4e4-f50f22138e0d\",\n" +
            "\t\t\"ProjectName\": \"气态表单测试\",\n" +
            "\t\t\"SamplingDetailYQFs\": [],\n" +
            "\t\t\"SamplingDetails\": [{\n" +
            "\t\t\t\"FrequecyNo\": \"1\",\n" +
            "\t\t\t\"Id\": \"b8c07888-c90f-4581-8df6-f867cb5a4834\",\n" +
            "\t\t\t\"IsCompare\": false,\n" +
            "\t\t\t\"IsSenceAnalysis\": false,\n" +
            "\t\t\t\"MonitemId\": \"d2d5ba7d-80e0-4221-b628-72c3224a68d6\",\n" +
            "\t\t\t\"MonitemName\": \"碳\",\n" +
            "\t\t\t\"OrderIndex\": \"1\",\n" +
            "\t\t\t\"Preservative\": \"否\",\n" +
            "\t\t\t\"PrivateData\": \"{\\\"Pressure\\\":\\\"12\\\",\\\"RelativHumidity\\\":\\\"12\\\",\\\"SampTimeDuration\\\":\\\"14:19 -- 14:19\\\",\\\"SamplingFlow\\\":\\\"12\\\",\\\"SamplingLength\\\":\\\"12\\\",\\\"Temperature\\\":\\\"12\\\",\\\"WindDirection\\\":\\\"12\\\",\\\"WindSpeed\\\":\\\"12\\\"}\",\n" +
            "\t\t\t\"ProjectId\": \"c9da1936-d833-4a1a-b4e4-f50f22138e0d\",\n" +
            "\t\t\t\"SampingCode\": \"SQ190621-5403-01\",\n" +
            "\t\t\t\"SamplingCount\": \"1\",\n" +
            "\t\t\t\"SamplingId\": \"6091a5b3-28e2-434f-8eb5-f807629ce5c4\",\n" +
            "\t\t\t\"SamplingTime\": \"14:19 -- 14:19\",\n" +
            "\t\t\t\"SamplingType\": \"0\",\n" +
            "\t\t\t\"Value1\": \"12\",\n" +
            "\t\t\t\"Value2\": \"14\",\n" +
            "\t\t\t\"Value3\": \"13\"\n" +
            "\t\t}],\n" +
            "\t\t\"SamplingFormStands\": [{\n" +
            "\t\t\t\"AnalysisSite\": \"现场\",\n" +
            "\t\t\t\"Container\": \"2\",\n" +
            "\t\t\t\"Count\": 1,\n" +
            "\t\t\t\"Id\": \"6b9d0ad0-de5b-41fa-8622-e5f2152a69fd\",\n" +
            "\t\t\t\"Index\": \"1\",\n" +
            "\t\t\t\"MonItems\": [\"d2d5ba7d-80e0-4221-b628-72c3224a68d6\"],\n" +
            "\t\t\t\"MonitemIds\": \"d2d5ba7d-80e0-4221-b628-72c3224a68d6\",\n" +
            "\t\t\t\"MonitemName\": \"碳\",\n" +
            "\t\t\t\"SamplingAmount\": \"2\",\n" +
            "\t\t\t\"SamplingId\": \"6091a5b3-28e2-434f-8eb5-f807629ce5c4\",\n" +
            "\t\t\t\"SaveMehtod\": \"2\",\n" +
            "\t\t\t\"SaveTimes\": \"2\",\n" +
            "\t\t\t\"StandNo\": 3,\n" +
            "\t\t\t\"UpdateTime\": \"2019-06-21 15:20:45\"\n" +
            "\t\t}, {\n" +
            "\t\t\t\"AnalysisSite\": \"现场\",\n" +
            "\t\t\t\"Container\": \"2\",\n" +
            "\t\t\t\"Count\": 1,\n" +
            "\t\t\t\"Id\": \"5eabe185-2342-4bad-98d3-03a85ae13edf\",\n" +
            "\t\t\t\"Index\": \"2\",\n" +
            "\t\t\t\"MonItems\": [\"c251eab1-aac6-48d3-9ad2-61f49d42f0af\"],\n" +
            "\t\t\t\"MonitemIds\": \"c251eab1-aac6-48d3-9ad2-61f49d42f0af\",\n" +
            "\t\t\t\"MonitemName\": \"盐酸联苯胺\",\n" +
            "\t\t\t\"SamplingAmount\": \"2\",\n" +
            "\t\t\t\"SamplingId\": \"6091a5b3-28e2-434f-8eb5-f807629ce5c4\",\n" +
            "\t\t\t\"SaveMehtod\": \"2\",\n" +
            "\t\t\t\"SaveTimes\": \"2\",\n" +
            "\t\t\t\"StandNo\": 3,\n" +
            "\t\t\t\"UpdateTime\": \"2019-06-21 15:20:45\"\n" +
            "\t\t}],\n" +
            "\t\t\"SamplingHeight\": \"12\",\n" +
            "\t\t\"SamplingNo\": \"190621-54-03\",\n" +
            "\t\t\"SamplingTimeBegin\": \"2019-06-21 00:00:00\",\n" +
            "\t\t\"SamplingTimeEnd\": \"2019-06-21 00:00:00\",\n" +
            "\t\t\"SamplingUserId\": \"6e2c449a-9ce1-412b-bac8-ff7c369814e0\",\n" +
            "\t\t\"SamplingUserName\": \"Admin\",\n" +
            "\t\t\"Status\": 0,\n" +
            "\t\t\"StatusName\": \"已完成\",\n" +
            "\t\t\"SubmitId\": \"6e2c449a-9ce1-412b-bac8-ff7c369814e0\",\n" +
            "\t\t\"SubmitName\": \"Admin\",\n" +
            "\t\t\"TagId\": \"c0ae99a1-0606-4276-8eb5-e352bbf46ef9\",\n" +
            "\t\t\"TagName\": \"室内空气\",\n" +
            "\t\t\"TransStatus\": 0,\n" +
            "\t\t\"UpdateTime\": \"2019-06-21 15:12:36\",\n" +
            "\t\t\"Version\": 0,\n" +
            "\t\t\"Weather\": \"小雨\"\n" +
            "\t},\n" +
            "\t\"UploadFiles\": [],\n" +
            "\t\"isCompelSubmit\": true,\n" +
            "\t\"isDevceForm\": false\n" +
            "}";

    public static void uploadMySampleForData(ApiPresenter apiPresenter, Context context) {
        Gson gson = new Gson();
        try {
            PreciptationSampForm sampForm = gson.fromJson(TEXT_DATA, PreciptationSampForm.class);
            apiPresenter.createTable(Message.obtain(new IView() {
                @Override
                public void showMessage(@NonNull String message) {
                    ArtUtils.makeText(context, message);
                }

                @Override
                public void handleMessage(@NonNull Message message) {
                    switch (message.what) {
                        case Message.RESULT_FAILURE:
                        case Message.RESULT_OK:
                            showMessage(message.str);
                            return;
                    }

                }
            }), sampForm);
        }catch (Exception e){

        }

    }
}

