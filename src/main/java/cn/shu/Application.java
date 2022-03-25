package cn.shu;

import cn.shu.dto.Status;
import cn.shu.dto.cell.CellList;
import cn.shu.dto.cell.CellRoot;
import cn.shu.dto.cell.ViewDirectoryRoot;
import cn.shu.dto.course.CourseList;
import cn.shu.dto.course.CourseRoot;
import cn.shu.dto.module.ModuleList;
import cn.shu.dto.module.ModuleRoot;
import cn.shu.dto.topic.TopicList;
import cn.shu.dto.topic.TopicRoot;
import com.alibaba.fastjson.JSON;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.*;

/**
 * 课程-> 模块 ->主题 ->单元
 */
public class Application {
    /**
     * 是否完成任务后提交评论
     */
    private final boolean isSubmitComment = false;

    /**
     * 是否完成任务后提交笔记
     */
    private final boolean isSubmitNote = false;

    /**
     * 课件上报间隔(毫秒)，太短会导致学习异常记录 (可能导致30分钟封禁)
     */
    private final int taskInterval = 2000;

    /**
     * 发布note的间隔 (毫秒)
     */
    private final int noteInterval = 2000;

    /**
     * 视频课件上报间隔(秒)，太短会导致学习异常记录 (可能导致30分钟封禁)
     */
    private final int videoIncrementInterval = 5000;

    /**
     * 视频上报进度的基本值 大于20极有可能导致学习异常记录 (可能导致30分钟封禁)
     */
    private final int videoIncrementBase = 14;

    /**
     * 视频上报进度的插值 随机数最大值 videoIncrementBase + videoIncrementX 不宜大于20 (可能导致30分钟封禁)
     */
    private final int videoIncrementX = 4;

    /**
     * 每个线程（用户） 一个 HttpClient
     */
    private final ThreadLocal<HttpClient> clientThreadLocal = new ThreadLocal<>();

    public static void main(String[] args) throws IOException, TesseractException, InterruptedException {

        CloseableHttpClient httpClient = HttpClients.createDefault();
        Application application = new Application();
        application.clientThreadLocal.set(httpClient);
        String verifyCode = application.getVerifyCode();
        application.login("2021001167", "Sxs411051", verifyCode);
        application.beginLearn();

    }

    /**
     * 登录
     *
     * @param userName 用户名
     * @param password 密码
     * @throws IOException 网络异常
     */
    public void login(String userName, String password, String verifyCode) throws IOException {

        String loginUrl = "https://www.icve.com.cn/portal/Register/Login_New";
        HttpPost httpPost = new HttpPost(loginUrl);
        httpPost.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML,like Gecko) " +
                " Chrome/86.0.4240.75 Safari/537.36");
        httpPost.addHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");
        httpPost.addHeader("Accept", "application/json");

        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("userName", new String(Base64.getEncoder().encode(userName.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)));
        params.add(new BasicNameValuePair("pwd", new String(Base64.getEncoder().encode(password.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8)));
        params.add(new BasicNameValuePair("verifycode", verifyCode));

        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, StandardCharsets.UTF_8);
        httpPost.setEntity(formEntity);
        HttpResponse httpResponse = clientThreadLocal.get().execute(httpPost);
        String s = EntityUtils.toString(httpResponse.getEntity());
    }

    /**
     * 获取验证码
     *
     * @return 验证码
     * @throws IOException 网络异常
     */
    public String getVerifyCode() throws IOException {
        //获取登录验证码
        String url = "https://www.icve.com.cn/portal/VerifyCode/index?t=" + Math.random();
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML,like Gecko) " +
                " Chrome/86.0.4240.75 Safari/537.36");
        HttpResponse httpResponse = clientThreadLocal.get().execute(httpGet);
        String filePath = "D://verifyCode" + Math.random() + ".GIF";
        Files.copy(
                httpResponse.getEntity().getContent(), Paths.get(filePath));
        System.out.println("请输入验证码：");
        Scanner scanner = new Scanner(System.in);
        String verifycode = scanner.nextLine();

        return verifycode;
    }


    /**
     * 开始学习
     *
     * @throws IOException 网络异常
     */
    public void beginLearn() throws IOException {
        //获取课程
        CourseRoot courseRoot = getAllCourse();
        for (CourseList courseList : courseRoot.getCourseList()) {
            if (courseList.getProcess() == 100) {
                System.out.println("【" + courseList.getCourseName() + "】已学习完成，跳过！");
                continue;
            }
            System.out.println("【" + courseList.getCourseName() + "】开始学习：");
            ModuleRoot module = this.getModule(courseList.getCourseOpenId(), courseList.getOpenClassId());
            for (ModuleList moduleList : module.getProgress().getModuleList()) {
                if (moduleList.getPercent() == 100) {
                    System.out.println("---【" + courseList.getCourseName() + "--" + moduleList.getName() + "】已学习完成，跳过！");
                    continue;
                }
                TopicRoot topicByModuleId = this.getTopicByModuleId(courseList.getCourseOpenId(), moduleList.getId());
                for (TopicList topicList : topicByModuleId.getTopicList()) {
                    CellRoot cellByTopicId = this.getCellByTopicId(courseList.getCourseOpenId(), courseList.getOpenClassId(), topicList.getId());
                    for (CellList cellList : cellByTopicId.getCellList()) {
                        try {
                            this.doneCellTask(cellList, courseList.getOpenClassId(), moduleList.getId());
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println(e.getMessage());
                        }
                    }
                }
            }
        }
    }

    /**
     * 获取课程
     *
     * @return 所有课程相关信息
     * @throws IOException 网络异常
     */
    public CourseRoot getAllCourse() throws IOException {
        String courseUrl = "https://zjy2.icve.com.cn/api/student/learning/getLearnningCourseList";
        HttpResponse response = clientThreadLocal.get().execute(new HttpGet(courseUrl));
        String s1 = EntityUtils.toString(response.getEntity());

        CourseRoot courseRoot = JSON.parseObject(s1, CourseRoot.class);
        return courseRoot;

    }

    /**
     * 获取课程的各个"模块"
     *
     * @param courseOpenId 课程ID
     * @param openClassId  班级ID
     * @return 所有"模块"信息
     * @throws IOException 网络异常
     */
    public ModuleRoot getModule(String courseOpenId, String openClassId) throws IOException {
        String processUrl = "https://zjy2.icve.com.cn/api/study/process/getProcessList";
        HttpPost httpPost = new HttpPost(processUrl);
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("courseOpenId", courseOpenId));
        params.add(new BasicNameValuePair("openClassId", openClassId));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        HttpResponse httpResponse = clientThreadLocal.get().execute(httpPost);
        String responseJson = EntityUtils.toString(httpResponse.getEntity());
        ModuleRoot moduleRoot = JSON.parseObject(responseJson, ModuleRoot.class);
        return moduleRoot;
    }

    /**
     * 获取模块下的"主题"
     *
     * @param courseOpenId 课程ID
     * @param moduleId     模块ID
     * @return 该模块下所有"主题"信息
     * @throws IOException 网络异常
     */
    public TopicRoot getTopicByModuleId(String courseOpenId, String moduleId) throws IOException {
        String url = "https://zjy2.icve.com.cn/api/study/process/getTopicByModuleId";
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("courseOpenId", courseOpenId));
        params.add(new BasicNameValuePair("moduleId", moduleId));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        HttpResponse httpResponse = clientThreadLocal.get().execute(httpPost);
        String responseJson = EntityUtils.toString(httpResponse.getEntity());
        TopicRoot root = JSON.parseObject(responseJson, TopicRoot.class);
        return root;
    }

    /**
     * 获取主题下"单元"信息
     *
     * @param courseOpenId 课程ID
     * @param openClassId  班级ID
     * @param topicId      主题ID
     * @return 该主题下所有"单元"信息
     * @throws IOException 网络异常
     */
    public CellRoot getCellByTopicId(String courseOpenId, String openClassId, String topicId) throws IOException {
        String url = "https://zjy2.icve.com.cn/api/study/process/getCellByTopicId";
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("courseOpenId", courseOpenId));
        params.add(new BasicNameValuePair("openClassId", openClassId));
        params.add(new BasicNameValuePair("topicId", topicId));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        HttpResponse httpResponse = clientThreadLocal.get().execute(httpPost);
        String responseJson = EntityUtils.toString(httpResponse.getEntity());
        CellRoot root = JSON.parseObject(responseJson, CellRoot.class);
        return root;
    }

    /**
     * 完成 "单元" 任务
     *
     * @param cell        单元
     * @param openClassId 班级ID
     * @param moduleId    模块ID
     * @throws InterruptedException 线程中止
     * @throws IOException          网络异常
     */
    public void doneCellTask(CellList cell, String openClassId, String moduleId) throws InterruptedException, IOException {


        String cate = cell.getCategoryName();
        // 任务进度
        int percent = 0;
        if (cell.getStuCellPercent() != null) {
            percent = cell.getStuCellPercent();
        } else if (cell.getStuCellFourPercent() != null) {
            percent = cell.getStuCellFourPercent();
        }

        if (percent == 100) {
            System.out.printf("\n试图完成 %s 类型任务: 【%s】 ...100%%%n",
                    cate, cell.getCellName());
        } else {
            System.out.printf("\n试图完成 %s 类型任务: 【%s】 ...%n",
                    cate, cell.getCellName());
        }

        if (percent != 100) {
            switch (cate) {
                case "视频":
                    doneCellVideo(cell, openClassId, moduleId);
                    break;
                case "图片":
                    doneCellImage(cell, openClassId, moduleId);
                    break;
                case "ppt":
                    doneCellPPT(cell, openClassId, moduleId);
                    break;
                case "文档":
                    doneCellDoc(cell, openClassId, moduleId);
                    break;
                case "压缩包":
                    doneCellZip(cell, openClassId, moduleId);
                    break;
                case "子节点":
                    System.out.printf("试图完成 %s 的多个子任务...%n", cell.getCellName());
                    for (CellList childCell : cell.getChildNodeList()) {
                        doneCellTask(childCell, openClassId, moduleId);
                    }
                    break;
            }
            if (!cate.equals("子节点")) {
                //追加间隔
                if (isSubmitComment || isSubmitNote)
                    Thread.sleep(5000);
                /*if (isSubmitComment &&submitComment(cell.getCourseOpenId(), openClassId, cell.getId())) {
                    System.out.println("课件评论已发布");
                }*/
                else if (isSubmitComment)
                    System.out.println("课件笔记发布失败");
                    //和评论同时发布 触发连续发布 会失败
           /*     if (isSubmitNote && submitNote(cell.getCourseOpenId(), openClassId, cell.getId())){
               System.out.println("课件笔记已发布");
           }*/
                else if (isSubmitNote) {

                    System.out.println("课件笔记发布失败");
                }
            }

        }
        System.out.printf("任务类型：【%s】 结果: 【%s】 任务名称: 【%s】 等待冷却时间%n",
                cate, cell.getCellName(), "状态");
        if (percent != 100) {
            //等待5s 免得被ban了
            Thread.sleep(videoIncrementInterval);
        }
    }

    /**
     * 完成 "ZIP" 类型任务
     *
     * @param cell        "单元"
     * @param openClassId 班级ID
     * @param moduleId    模块ID
     * @return 是否成功
     * @throws IOException 网络异常
     */
    public boolean doneCellZip(CellList cell, String openClassId, String moduleId) throws IOException {

        ViewDirectoryRoot viewDirectoryRoot = viewDirectory(cell.getCourseOpenId(), openClassId,
                cell.getId(), "s", moduleId);
        return stuProcessCellLog(
                cell.getCourseOpenId(),
                openClassId,
                cell.getId(),
                viewDirectoryRoot.getGuIdToken(),
                0,
                viewDirectoryRoot.getCellLogId()
        );

    }


    /**
     * 完成 "图片" 类型任务
     *
     * @param cell        "单元"
     * @param openClassId 班级ID
     * @param moduleId    模块ID
     * @return 是否成功
     * @throws IOException 网络异常
     */
    public boolean doneCellImage(CellList cell, String openClassId, String moduleId) throws IOException {

        return doneCellPPT(cell, openClassId, moduleId);
    }

    /**
     * 完成 "PPT" 类型任务
     *
     * @param cell        "单元"
     * @param openClassId 班级ID
     * @param moduleId    模块ID
     * @return 是否成功
     * @throws IOException 网络异常
     */
    public boolean doneCellPPT(CellList cell, String openClassId, String moduleId) throws IOException {


        ViewDirectoryRoot viewDirectoryRoot = viewDirectory(cell.getCourseOpenId(), openClassId,
                cell.getId(), "s", moduleId);
        return stuProcessCellLog(cell.getCourseOpenId()
                , openClassId
                , cell.getId()
                , viewDirectoryRoot.getGuIdToken()
                , viewDirectoryRoot.getStuStudyNewlyTime()
                , viewDirectoryRoot.getPageCount()
                , viewDirectoryRoot.getCellLogId()
                , viewDirectoryRoot.getPageCount());

    }

    /**
     * 完成 "文档" 类型任务
     *
     * @param cell        "单元"
     * @param openClassId 班级ID
     * @param moduleId    模块ID
     * @return 是否成功
     * @throws IOException 网络异常
     */
    public boolean doneCellDoc(CellList cell, String openClassId, String moduleId) throws IOException {

        return doneCellPPT(cell, openClassId, moduleId);
    }

    /**
     * 完成 "视频" 类型任务
     *
     * @param cell        "单元"
     * @param openClassId 班级ID
     * @param moduleId    模块ID
     * @return 是否成功
     * @throws IOException 网络异常
     */
    public boolean doneCellVideo(CellList cell, String openClassId, String moduleId) throws InterruptedException, IOException {

        ViewDirectoryRoot viewDirectoryRoot = viewDirectory(cell.getCourseOpenId(), openClassId,
                cell.getId(), "s", moduleId);
        //目标观看位置
        int audioVideoLong = viewDirectoryRoot.getAudioVideoLong();
        String guIdToken = viewDirectoryRoot.getGuIdToken();
        //上次观看位置
        int studyNewlyTime = viewDirectoryRoot.getStuStudyNewlyTime();
        int inc = studyNewlyTime;  //inc下次上报的进度 初始化为已有进度
        int linc = inc; //上次上报的进度
        //视频总时长 分秒
        int m = audioVideoLong / 60;
        int s = audioVideoLong % 60;
        while (inc < audioVideoLong) {
            //设置下次上报的进度
            inc += videoIncrementBase + Math.random() * videoIncrementX;

            if (inc >= audioVideoLong) {
                inc = audioVideoLong;
            }
            //等待5s 免得被ban了
            Thread.sleep(videoIncrementInterval);

            //当前已学时长 分秒
            int m2 = inc / 60;
            int s2 = inc % 60;
            boolean status = stuProcessCellLog(cell.getCourseOpenId(),
                    openClassId, cell.getId(),
                    guIdToken, inc/*"{:.6f}".format(inc)todo*/,
                    viewDirectoryRoot.getCellLogId());
            System.out.printf(" 【%s】 进度上报成功，当前完成度: %d%% 视频总时长: %s 当前进度时长: %s 跳过%d秒%n",
                    viewDirectoryRoot.getCellName(),
                    (int) Math.ceil((inc * 1.0 / audioVideoLong) * 100),
                    m + "分" + s + "秒",
                    m2 + "分" + s2 + "秒",
                    inc - linc
            );
            linc = inc;
        }
        return true;
    }

    /**
     * 调用接口查询课件进度，完成课件进度主要通过该接口
     *
     * @param courseOpenId     课程ID
     * @param openClassId      班级ID
     * @param cellId           单元ID
     * @param guIdToken
     * @param studyNewlyTime
     * @param studyNewlyPicNum
     * @param cellLogId
     * @param picNum
     * @return
     */
    private boolean stuProcessCellLog(String courseOpenId, String openClassId, String cellId, String guIdToken,
                                      int studyNewlyTime, int studyNewlyPicNum, String cellLogId, int picNum) throws IOException {

        String url = "https://zjy2.icve.com.cn/api/common/Directory/stuProcessCellLog";
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("courseOpenId", courseOpenId));
        params.add(new BasicNameValuePair("openClassId", openClassId));
        params.add(new BasicNameValuePair("cellId", cellId));
        params.add(new BasicNameValuePair("picNum", picNum + ""));
        params.add(new BasicNameValuePair("cellLogId", cellLogId));
        params.add(new BasicNameValuePair("studyNewlyTime", studyNewlyTime + ""));
        params.add(new BasicNameValuePair("studyNewlyPicNum", studyNewlyPicNum + ""));
        params.add(new BasicNameValuePair("token", guIdToken));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        HttpResponse httpResponse = clientThreadLocal.get().execute(httpPost);
        String responseJson = EntityUtils.toString(httpResponse.getEntity());
        System.out.println(responseJson);
        Status root = JSON.parseObject(responseJson, Status.class);
        if (root.getCode() != 1) {
            throw new RuntimeException(root.getMsg());
        }
        return true;
    }

    /**
     * 调用接口查询课件进度，完成课件进度主要通过该接口
     *
     * @param courseOpenId   课程ID
     * @param openClassId    班级ID
     * @param cellId         单元ID
     * @param guIdToken
     * @param studyNewlyTime
     * @param cellLogId
     * @return
     */
    private boolean stuProcessCellLog(String courseOpenId, String openClassId, String cellId, String guIdToken, int studyNewlyTime, String cellLogId) throws IOException {
        return stuProcessCellLog(courseOpenId, openClassId, cellId, guIdToken, studyNewlyTime, 0, "", 0);
    }

    /**
     * 获取课件信息，主要用与获取guIdToken和视频的总时长、ppt的总页数
     *
     * @param courseOpenId 课程ID
     * @param openClassId  班级ID
     * @param cellId       单元ID
     * @param moduleId     模块ID
     * @param flag
     * @return 课件信息
     * @throws IOException 网络异常
     */
    private ViewDirectoryRoot viewDirectory(String courseOpenId, String openClassId, String cellId, String flag, String moduleId) throws IOException {


        String url = "https://zjy2.icve.com.cn/api/common/Directory/viewDirectory";
        HttpPost httpPost = new HttpPost(url);
        List<BasicNameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("courseOpenId", courseOpenId));
        params.add(new BasicNameValuePair("openClassId", openClassId));
        params.add(new BasicNameValuePair("cellId", cellId));
        params.add(new BasicNameValuePair("flag", flag));
        params.add(new BasicNameValuePair("moduleId", moduleId));
        UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(params, "utf8");
        httpPost.setEntity(formEntity);
        HttpResponse httpResponse = clientThreadLocal.get().execute(httpPost);
        String responseJson = EntityUtils.toString(httpResponse.getEntity());
        ViewDirectoryRoot root = JSON.parseObject(responseJson, ViewDirectoryRoot.class);


        if (root.getCode() == -100) {
            /*changeStuStudyProcessCellData(
                    json['currCourseOpenId'],
                    json['currOpenClassId'],
                    json['currModuleId'],
                    json['curCellId'],
                    json['currCellName']);

        return viewDirectory(
                json['currCourseOpenId'],
                json['currOpenClassId'],
                json['curCellId'],
                flag,
                json['currModuleId']*/
        } else if (responseJson.contains("异常学习行为")) {
            throw new RemoteException(responseJson);
        }
        return root;
    }



/*
    boolean submitNote(String courseOpenId, String  openClassId, String cellId){
              return submitComment(courseOpenId, openClassId, cellId, activityType = 2)
          }


          boolean submitComment(String courseOpenId, String openClassId, String cellId, String content="老师讲的很好！" + str(random.randint(0, 10000)), activityType=1):
          """
          提交评论/笔记/问答等
          Params:
              activityType: 1评价 2笔记
          """
          r = sess.post("https://zjy2.icve.com.cn/api/common/Directory/addCellActivity", data={
                  "courseOpenId": courseOpenId,
                  "openClassId": openClassId,
                  "cellId": cellId,
                  "content": content,
                  "docJson": "",
                  "star": 5,
                  "activityType": activityType
    }).json()
          print(r)
          if (r["code"] != 1):
        # 发布异常
          return False
          return True
*/


}
