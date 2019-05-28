package cn.cdjzxy.monitoringassistant;

import android.util.Log;

import org.junit.Test;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
//        saveUrlAs("https://ss1.baidu.com/9vo3dSag_xI4khGko9WTAnF6hhy/image/h%3D300/sign=0282c44e9c45d688bc02b4a494c37dab/4b90f603738da977f86d8b56be51f8198618e309.jpg",
//                "C:/Users/10708/Desktop", "GET", "test.png");
//        textMantRound();
        // delete();
    }

    /**
     * @param filePath 文件将要保存的目录
     * @param method   请求方法，包括POST和GET
     * @param url      请求的路径
     * @return
     * @功能 下载临时素材接口
     */
    public File saveUrlAs(String url, String filePath, String method, String fileName) {
        //System.out.println("fileName---->"+filePath);
        //创建不同的文件夹目录
        File file = new File(filePath);
        //判断文件夹是否存在
        if (!file.exists()) {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try {
            // 建立链接
            URL httpUrl = new URL(url);
            conn = (HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {

                filePath += "/";

            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）
            fileOut = new FileOutputStream(filePath + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while (length != -1) {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return file;

    }

    public void delete() {
        List<Integer> list = new ArrayList<>();
        list.add(0);
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);
        list.add(6);
        list.add(7);
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + "\n");
        }
        list.remove(new Integer(8));
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + "\n");
        }
    }

    /**
     * 测试四舍六入规则
     * 验证：不保留小数是：math.round() 满足四舍五入的规则
     * 保留一位或者多位小数时  满足四舍六入无成双规则
     * <p>
     * 有效位数确定后，其后面多余的数字应该舍去，只保留有效数字最末一位，这种修约（舍入）规则是“四舍六入五成双”，
     * 也即“4舍6入5凑偶”这里“四”是指≤4 时舍去，
     * <p>
     * "六"是指≥6时进上，
     * <p>
     * "五"指的是根据5后面的数字来定，当5后有数时，舍5入1；
     * <p>
     * 当5后无数或为0时，需要分两种情况来讲：
     * <p>
     * ①5前为奇数，舍5入1；
     * <p>
     * ②5前为偶数，舍5不进。
     */

    public void textManthRound() {
        float[] floats = new float[]{0.5f, 0.51f, 0.151f, 0.251f, 0.252f};
        for (float f : floats) {
//            System.out.println(Math.round(f, 1));
        }
    }
}