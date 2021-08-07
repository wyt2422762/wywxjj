package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.area.Area;
import com.fdkj.wywxjj.api.model.sysMgr.*;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.xmMgr.Ld;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.*;

/**
 * 接口调用工具类
 *
 * @author wyt
 */
@Component
public class Api {

    private static final Logger logger = LoggerFactory.getLogger(Api.class);

    @Value("${bus.api.baseUrl}")
    private String baseUrl;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取用户token
     *
     * @param userName 账号
     * @param password 密码
     * @return token
     */
    public JSONObject getUserToken(String userName, String password) {
        //请求体参数
        JSONObject jo = new JSONObject();
        jo.put("username", userName);
        jo.put("password", password);
        jo.put("fk_userid", "");
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(jo, null);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/User/YHGetToken",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取用户token失败，请求url: " + baseUrl + "/api/User/YHGetToken");
            logger.error("获取用户token失败，请求体: " + jo.toJSONString());
            logger.error("获取用户token失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        String token = jsonObject.getString("Message");
        User user = jsonObject.getObject("Results", User.class);

        JSONObject res = new JSONObject();
        res.put("token", token);
        res.put("user", user);
        return res;
    }

    /**
     * 从cookie中获取用户
     *
     * @param request req
     * @return 用户user
     * @throws Exception err
     */
    public User getUserFromCookie(HttpServletRequest request) throws Exception {
        User user = null;
        //获取当前登陆的用户信息(从cookie中获取)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("user".equals(cookie.getName())) {
                    user = JSONObject.parseObject(URLDecoder.decode(cookie.getValue(), "utf-8"), User.class);
                }
            }
        }
        return user;
    }

    /**
     * 从cookie中获取token
     *
     * @param request req
     * @return token
     */
    public String getTokenFromCookie(HttpServletRequest request) {
        String token = null;
        //获取当前登陆的用户信息(从cookie中获取)
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }
        return token;
    }

    private HttpHeaders getHttpHeaders(HttpServletRequest request) {
        //请求头
        HttpHeaders headers = new HttpHeaders();
        headers.add("Bearer", getTokenFromCookie(request));
        return headers;
    }

    /**
     * 获取所有的系统菜单内容
     *
     * @param request req
     * @return 所有的菜单项(非树形结构)
     * @throws Exception e
     */
    private List<View_PT_XT_MK_Model> getPTXT_View_PT_XT_MK_Model(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);

        String fk_xtglid = user.getFk_xtglid();

        HttpHeaders headers = getHttpHeaders(request);

        Map<String, String> params = new HashMap<>(1);
        params.put("xtid", fk_xtglid);

        //组装请求体
        HttpEntity<MultiValueMap<String, String>> requestEntity2 = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity2 =
                restTemplate.exchange(baseUrl + "/PTXT/View_PT_XT_MK_Model?xtid={xtid}",
                        HttpMethod.POST, requestEntity2, String.class, params);
        String responseEntityBody = responseEntity2.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取系统模块失败，请求url: " + baseUrl + "/PTXT/View_PT_XT_MK_Model");
            logger.error("获取系统模块失败，请求参数: xtid=" + fk_xtglid);
            logger.error("获取系统模块失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(View_PT_XT_MK_Model.class);
    }

    /**
     * 获取所有的系统菜单(树形结构)
     *
     * @param list l
     * @return 系统所有的菜单项(多级树形结构)
     */
    private List<View_PT_XT_MK_Model> buildPTXT_View_PT_XT_MK_Model(List<View_PT_XT_MK_Model> list) {
        //创建一个集合用于保存所有根主菜单
        List<View_PT_XT_MK_Model> rootMenu = new ArrayList<>();
        for (View_PT_XT_MK_Model view_pt_xt_mk_model : list) {
            if (view_pt_xt_mk_model.getMkjb() == 1) {
                rootMenu.add(view_pt_xt_mk_model);
            }
        }
        //排序
        rootMenu.sort(Comparator.comparingInt(View_PT_XT_MK_Model::getMkpxid));
        //加子菜单
        for (View_PT_XT_MK_Model menu : rootMenu) {
            List<View_PT_XT_MK_Model> clit = getChildList(menu.getId(), list);
            if (!clit.isEmpty()) {
                menu.setChildList(clit);
            }
        }
        return rootMenu;
    }

    /**
     * 全部系统菜单处理子菜单
     *
     * @param fId  父级id
     * @param list 模块列表
     * @return res
     */
    private List<View_PT_XT_MK_Model> getChildList(String fId, List<View_PT_XT_MK_Model> list) {
        List<View_PT_XT_MK_Model> childList = new ArrayList<>();
        for (View_PT_XT_MK_Model view_pt_xt_mk_model : list) {
            if (fId.equals(view_pt_xt_mk_model.getFid())) {
                childList.add(view_pt_xt_mk_model);
            }
        }
        //排序
        childList.sort(Comparator.comparingInt(View_PT_XT_MK_Model::getMkpxid));
        //加子菜单
        for (View_PT_XT_MK_Model menu : childList) {
            List<View_PT_XT_MK_Model> clit = getChildList(menu.getId(), list);
            if (!clit.isEmpty()) {
                menu.setChildList(clit);
            }
        }
        return childList;
    }

    /**
     * 获取系统的全部菜单
     *
     * @param request req
     * @return 系统全部的菜单项(树形结构)
     * @throws Exception err
     */
    public List<View_PT_XT_MK_Model> getAllModels(HttpServletRequest request) throws Exception {
        //系统全部菜单(非树形结构)
        List<View_PT_XT_MK_Model> ptXtMkModel = getPTXT_View_PT_XT_MK_Model(request);
        return buildPTXT_View_PT_XT_MK_Model(ptXtMkModel);
    }

    /**
     * 获取角色的模块
     *
     * @param fk_jsid 角色id
     * @return 角色对应的模块
     * //@throws Exception err
     */
    public List<PT_JS_MK> getRoleMk(HttpServletRequest request, String fk_jsid) {
        HttpHeaders headers = getHttpHeaders(request);

        Map<String, String> params = new HashMap<>(1);
        params.put("id", fk_jsid);

        //组装请求体
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_JS_MK_List?id={id}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取角色的模块失败，请求url: " + baseUrl + "/PTXT/PT_JS_MK_List");
            logger.error("获取角色的模块失败，请求参数: id=" + fk_jsid);
            logger.error("获取角色的模块失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONObject("Results").getJSONArray("PT_JS_MKList").toJavaList(PT_JS_MK.class);
    }

    /**
     * 获取当前登陆用户的菜单
     *
     * @param request req
     * @return 当前登陆用户的菜单
     * @throws Exception err
     */
    public List<View_PT_XT_MK_Model> getUserMenu(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);

        //系统全部菜单(非树形结构)
        List<View_PT_XT_MK_Model> ptXtMkModel = getPTXT_View_PT_XT_MK_Model(request);
        //角色模块
        List<PT_JS_MK> roleMks = getRoleMk(request, user.getFk_jsid());

        return getView_pt_xt_mk_models(ptXtMkModel, roleMks);
    }

    private List<View_PT_XT_MK_Model> getView_pt_xt_mk_models(List<View_PT_XT_MK_Model> ptXtMkModel, List<PT_JS_MK> roleMks) {
        for (View_PT_XT_MK_Model xtMkModel : ptXtMkModel) {
            for (PT_JS_MK roleMk : roleMks) {
                if (xtMkModel.getId().equals(roleMk.getFk_XTMKid())) {
                    String opt = roleMk.getActionType();
                    if (xtMkModel.getOpts() == null) {
                        xtMkModel.setOpts(new ArrayList<>());
                    }
                    xtMkModel.getOpts().add(opt);
                    if ("show".equals(opt)) {
                        xtMkModel.setShow(true);
                    }
                }
            }
        }

        return buildPTXT_View_PT_XT_MK_Model(ptXtMkModel);
    }

    /**
     * 获取角色的模块
     *
     * @param request req
     * @return 角色对应的模块信息列表
     * @throws Exception err
     */
    public List<View_PT_XT_MK_Model> getRoleModel(HttpServletRequest request, String roleId) throws Exception {
        //系统全部菜单(非树形结构)
        List<View_PT_XT_MK_Model> ptXtMkModel = getPTXT_View_PT_XT_MK_Model(request);
        //角色模块
        List<PT_JS_MK> roleMks = getRoleMk(request, roleId);

        return getView_pt_xt_mk_models(ptXtMkModel, roleMks);
    }

    /*****************************************区域api*****************************************/

    /**
     * 获取区域信息
     *
     * @param request  req
     * @param parentId 父级id
     * @return 区域信息
     * @throws Exception err
     */
    public List<Area> getAreaDataList(HttpServletRequest request, String parentId) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);
        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("fbm", parentId);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_Areas_ParentId_List?fbm={fbm}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取区域信息失败，请求url: " + baseUrl + "/PTXT/PT_Areas_ParentId_List");
            logger.error("获取日志列表失败，请求参数: " + params);
            logger.error("获取日志列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        List<Area> dataList = jsonObject.getJSONArray("Results").toJavaList(Area.class);
        return dataList;
    }

    /*****************************************日志api*****************************************/

    /**
     * 获取日志列表
     *
     * @param request   req
     * @param pageNo    第几页
     * @param pageSize  每页显示的条数
     * @param startTime 开始事件
     * @param endTime   结束事件
     * @return 日志列表
     * @throws Exception err
     */
    public Page<Xtrz> getLogList(HttpServletRequest request, Integer pageNo, Integer pageSize, String startTime, String endTime) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("id", user.getId());
        body.put("addtime", user.getAddtime());
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);
        if (StringUtils.isNotBlank(startTime)) {
            params.put("starttime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endtime", endTime);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append("/api/CZF/PT_XTRZ_List").append("?")
                .append("page={page}&pageNum={pageNum}");
        if (StringUtils.isNotBlank(startTime)) {
            sb.append("&starttime={starttime}");
        }
        if (StringUtils.isNotBlank(endTime)) {
            sb.append("&endtime={endtime}");
        }

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(sb.toString(),
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取日志列表失败，请求url: " + baseUrl + "/api/CZF/PT_XTRZ_List");
            logger.error("获取日志列表失败，请求参数: " + params);
            logger.error("获取日志列表失败，请求体: " + body.toJSONString());
            logger.error("获取日志列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Xtrz> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Xtrz> dataList = jsonObject.getJSONArray("Results").toJavaList(Xtrz.class);
        page.setDataList(dataList);

        return page;
    }

    /*
     * 添加系统日志
    public void addLog(){}*/

    /*****************************************角色api*****************************************/

    /**
     * 获取角色列表(分页)
     *
     * @param request  req
     * @param roleName 角色名称
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 角色列表
     * @throws Exception err
     */
    public Page<Role> getRoleList(HttpServletRequest request, String roleName, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(roleName)) {
            body.put("jsmc", roleName.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/PTXT/PT_JSGL_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取角色列表失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_List");
            logger.error("获取角色列表失败，请求参数: " + params);
            logger.error("获取角色列表失败，请求体: " + body.toJSONString());
            logger.error("获取角色列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Role> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Role> dataList = jsonObject.getJSONArray("Results").toJavaList(Role.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取全部角色列表
     *
     * @param request req
     * @return 角色列表
     * @throws Exception err
     */
    public List<Role> getAllRoleList(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/PTXT/PT_JSGL_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取全部角色列表失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_List");
            logger.error("获取全部角色列表失败，请求体: " + body.toJSONString());
            logger.error("获取全部角色列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(Role.class);
    }

    /**
     * 更新添加角色
     *
     * @param request req
     * @param body    请求体
     */
    public void aeRole(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_JSGL_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加角色失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_Update");
            logger.error("更新添加角色失败，请求体: " + body.toJSONString());
            logger.error("更新添加角色失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 删除角色
     *
     * @param request req
     * @param roleId  角色id
     */
    public void delRole(HttpServletRequest request, String roleId) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);
        //参数
        Map<String, String> params = new HashMap<>(1);
        params.put("id", roleId);
        //请求
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_JSGL_Del?id={id}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("删除角色失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_Del");
            logger.error("删除角色失败，请求参数: " + params);
            logger.error("删除角色失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /*****************************************用户api*****************************************/

    /**
     * 获取用户管理列表
     *
     * @param request  req
     * @param userId   用户id
     * @param username 用户名
     * @param pageNo   第几页
     * @param pageSize 每页显示多少条
     * @return 用户管理列表
     * @throws Exception err
     */
    public Page<User> getUserList(HttpServletRequest request, String userId, String username, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(userId)) {
            body.put("id", userId.trim());
        }
        if (StringUtils.isNotBlank(username)) {
            body.put("username", username.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/CZF_YHGL_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取用户管理列表失败，请求url: " + baseUrl + "/api/CZF/CZF_YHGL_List");
            logger.error("获取用户管理列表失败，请求参数: " + params);
            logger.error("获取用户管理列表失败，请求体: " + body.toJSONString());
            logger.error("获取用户管理列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<User> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<User> dataList = jsonObject.getJSONArray("Results").toJavaList(User.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取用户详情
     *
     * @param request req
     * @param id      用户id
     * @return 用户详情
     * @throws Exception err
     */
    public User getUserDetail(HttpServletRequest request, String id) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/CZF_YHGL_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取用户详情失败，请求url: " + baseUrl + "/api/CZF/CZF_YHGL_Model");
            logger.error("获取用户详情失败，请求体: " + body.toJSONString());
            logger.error("获取用户详情失败，请求参数: " + params);
            logger.error("获取用户详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONObject("Results").toJavaObject(User.class);
    }

    /**
     * 添加用户
     *
     * @param request req
     * @param body    请求体
     */
    public void addUser(HttpServletRequest request, JSONObject body) {
        String url = baseUrl + "/api/CZF/CZF_YHGL_ZhuChe";
        aeUser(request, url, body);
    }

    /**
     * 编辑用户
     *
     * @param request req
     * @param body    请求体
     */
    public void editUser(HttpServletRequest request, JSONObject body) {
        String url = baseUrl + "/api/CZF/CZF_YHGL_Update";
        aeUser(request, url, body);
    }

    /**
     * 更新添加用户
     *
     * @param request req
     * @param body    请求体
     */
    private void aeUser(HttpServletRequest request, String url, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加用户失败，请求url: " + url);
            logger.error("更新添加用户失败，请求体: " + body.toJSONString());
            logger.error("更新添加用户失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 修改密码(当前登陆用户)
     *
     * @param request     req
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    public void editUserPassword(HttpServletRequest request, String oldPassword, String newPassword) {
        if (StringUtils.isBlank(oldPassword) || StringUtils.isBlank(newPassword)) {
            throw new BusinessException("参数不正确", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);
        //请求参数
        Map<String, String> params = new HashMap<>(2);
        params.put("ymm", oldPassword.trim());
        params.put("xmm", newPassword.trim());

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/CZF_YHGL_XGMM",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("修改密码(当前登陆用户)失败，请求url: " + baseUrl + "/api/CZF/CZF_YHGL_XGMM");
            logger.error("修改密码(当前登陆用户)失败，请求参数: " + params);
            logger.error("修改密码(当前登陆用户)失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /*****************************************物业公司api*****************************************/

    /**
     * 获取物业公司列表(分页)
     *
     * @param request  req
     * @param qymc     企业名称
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 物业公司列表
     * @throws Exception err
     */
    public Page<WyGs> getWyGsList(HttpServletRequest request, String qymc, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(qymc)) {
            body.put("qymc", qymc.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/WYWXJJ_WY_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取物业公司列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_WY_List");
            logger.error("获取物业公司列表失败，请求参数: " + params);
            logger.error("获取物业公司列表失败，请求体: " + body.toJSONString());
            logger.error("获取物业公司列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<WyGs> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<WyGs> dataList = jsonObject.getJSONArray("Results").toJavaList(WyGs.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取物业公司列表(全部)
     *
     * @param request req
     * @return 物业公司列表
     * @throws Exception err
     */
    public List<WyGs> getWyGsAllList(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/WYWXJJ_WY_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取物业公司列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_WY_List");
            logger.error("获取物业公司列表失败，请求体: " + body.toJSONString());
            logger.error("获取物业公司列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(WyGs.class);
    }

    /**
     * 获取物业公司详情
     *
     * @param request req
     * @param id      物业公司id
     * @return 物业公司详情
     * @throws Exception err
     */
    public WyGs getWyGsDetail(HttpServletRequest request, String id) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/WYWXJJ_WY_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取物业公司详情失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_WY_List");
            logger.error("获取物业公司详情失败，请求参数: " + params);
            logger.error("获取物业公司详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONObject("Results").toJavaObject(WyGs.class);
    }

    /**
     * 更新添加物业公司
     *
     * @param request req
     * @param body    请求体
     */
    public void aeWyGs(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_WY_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加物业公司失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_WY_Update");
            logger.error("更新添加物业公司失败，请求体: " + body.toJSONString());
            logger.error("更新添加物业公司失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 删除物业公司
     *
     * @param request req
     * @param id      id
     */
    public void delWyGs(HttpServletRequest request, String id) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);
        //参数
        Map<String, String> params = new HashMap<>(1);
        params.put("id", id);
        //请求
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_WY_Del?id={id}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("删除物业公司失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_Del");
            logger.error("删除物业公司失败，请求参数: " + params);
            logger.error("删除物业公司失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /*****************************************银行api*****************************************/

    /**
     * 获取银行列表(分页)
     *
     * @param request  req
     * @param yhmc     银行名称
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 银行列表
     * @throws Exception err
     */
    public Page<Yh> getYhList(HttpServletRequest request, String yhmc, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(yhmc)) {
            body.put("yxmc", yhmc.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/WYWXJJ_YH_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取银行列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_YH_List");
            logger.error("获取银行列表失败，请求参数: " + params);
            logger.error("获取银行列表失败，请求体: " + body.toJSONString());
            logger.error("获取银行列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Yh> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Yh> dataList = jsonObject.getJSONArray("Results").toJavaList(Yh.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取银行列表(全部)
     *
     * @param request req
     * @return 银行列表
     * @throws Exception err
     */
    public List<Yh> getYhAllList(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/WYWXJJ_YH_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取银行列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_YH_List");
            logger.error("获取银行列表失败，请求体: " + body.toJSONString());
            logger.error("获取银行列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(Yh.class);
    }

    /**
     * 获取银行详情
     *
     * @param request req
     * @param id      银行id
     * @return 银行详情
     * @throws Exception err
     */
    public Yh getYhDetail(HttpServletRequest request, String id) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/WYWXJJ_YH_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取银行详情失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_YH_Model");
            logger.error("获取银行详情失败，请求参数: " + params);
            logger.error("获取银行详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONObject("Results").toJavaObject(Yh.class);
    }

    /**
     * 更新添加银行
     *
     * @param request req
     * @param body    请求体
     */
    public void aeYh(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_YH_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加银行失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_YH_Update");
            logger.error("更新添加银行失败，请求体: " + body.toJSONString());
            logger.error("更新添加银行失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 删除银行
     *
     * @param request req
     * @param id      id
     */
    public void delYh(HttpServletRequest request, String id) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);
        //参数
        Map<String, String> params = new HashMap<>(1);
        params.put("id", id);
        //请求
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_YH_Del?id={id}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("删除银行失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_YH_Del");
            logger.error("删除银行失败，请求参数: " + params);
            logger.error("删除银行失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /*****************************************小区api*****************************************/

    /**
     * 获取小区列表(分页)
     *
     * @param request  req
     * @param xmmc     项目(小区)名称
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 项目(小区)列表
     * @throws Exception err
     */
    public Page<Xm> getXmList(HttpServletRequest request, String xmmc, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(xmmc)) {
            body.put("xmmc", xmmc.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/xk_xmxx_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取项目(小区)列表失败，请求url: " + baseUrl + "/api/CZF/xk_xmxx_List");
            logger.error("获取项目(小区)列表失败，请求参数: " + params);
            logger.error("获取项目(小区)列表失败，请求体: " + body.toJSONString());
            logger.error("获取项目(小区)列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Xm> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Xm> dataList = jsonObject.getJSONArray("Results").toJavaList(Xm.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取项目(小区)列表(全部)
     *
     * @param request req
     * @return 项目(小区)列表
     * @throws Exception err
     */
    public List<Xm> getXmAllList(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/xk_xmxx_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取项目(小区)列表失败，请求url: " + baseUrl + "/api/CZF/xk_xmxx_List");
            logger.error("获取项目(小区)列表失败，请求体: " + body.toJSONString());
            logger.error("获取项目(小区)列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(Xm.class);
    }

    /**
     * 获取项目(小区)详情
     *
     * @param request req
     * @param id      项目(小区)id
     * @return 银行详情
     * @throws Exception err
     */
    public Xm getXmDetail(HttpServletRequest request, String id) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/xk_xmxx_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取项目(小区)详情失败，请求url: " + baseUrl + "/api/CZF/xk_xmxx_Model");
            logger.error("获取项目(小区)详情失败，请求参数: " + params);
            logger.error("获取项目(小区)详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONObject("Results").toJavaObject(Xm.class);
    }

    /**
     * 更新添加项目(小区)
     *
     * @param request req
     * @param body    请求体
     */
    public void aeXM(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/xk_xmxx_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加项目(小区)失败，请求url: " + baseUrl + "/api/CZF/xk_xmxx_Update");
            logger.error("更新添加项目(小区)失败，请求体: " + body.toJSONString());
            logger.error("更新添加项目(小区)失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 导入项目(小区)
     *
     * @param request req
     * @param body    请求体
     */
    public void importXm(HttpServletRequest request, JSONArray body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONArray> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/xk_xmxx_Insert",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("导入项目(小区)失败，请求url: " + baseUrl + "/api/CZF/xk_xmxx_Insert");
            logger.error("导入项目(小区)失败，请求体: " + body.toJSONString());
            logger.error("导入项目(小区)失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /*****************************************楼栋api*****************************************/

    /**
     * 获取楼栋列表(分页)
     *
     * @param request  req
     * @param cmc     楼栋名称
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 楼栋列表
     * @throws Exception err
     */
    public Page<Ld> getLdList(HttpServletRequest request, String cmc, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(cmc)) {
            body.put("cmc", cmc.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/xk_ldxx_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取楼栋列表失败，请求url: " + baseUrl + "/api/CZF/xk_ldxx_List");
            logger.error("获取楼栋列表失败，请求参数: " + params);
            logger.error("获取楼栋列表失败，请求体: " + body.toJSONString());
            logger.error("获取楼栋列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Ld> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Ld> dataList = jsonObject.getJSONArray("Results").toJavaList(Ld.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取楼栋列表(全部)
     *
     * @param request req
     * @return 楼栋列表
     * @throws Exception err
     */
    public List<Ld> getLdAllList(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/xk_ldxx_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取楼栋列表失败，请求url: " + baseUrl + "/api/CZF/xk_ldxx_List");
            logger.error("获取楼栋列表失败，请求体: " + body.toJSONString());
            logger.error("获取楼栋列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(Ld.class);
    }

    /**
     * 获取楼栋详情
     *
     * @param request req
     * @param id      楼栋id
     * @return 银行详情
     * @throws Exception err
     */
    public Ld getLdDetail(HttpServletRequest request, String id) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/xk_ldxx_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取楼栋详情失败，请求url: " + baseUrl + "/api/CZF/xk_ldxx_Model");
            logger.error("获取楼栋详情失败，请求参数: " + params);
            logger.error("获取楼栋详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONObject("Results").toJavaObject(Ld.class);
    }

    /**
     * 更新添加楼栋
     *
     * @param request req
     * @param body    请求体
     */
    public void aeLd(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/xk_ldxx_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加楼栋失败，请求url: " + baseUrl + "/api/CZF/xk_ldxx_Update");
            logger.error("更新添加楼栋失败，请求体: " + body.toJSONString());
            logger.error("更新添加楼栋失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 导入楼栋
     *
     * @param request req
     * @param body    请求体
     */
    public void importLd(HttpServletRequest request, JSONArray body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONArray> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/xk_ldxx_Insert",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("导入楼栋失败，请求url: " + baseUrl + "/api/CZF/xk_ldxx_Insert");
            logger.error("导入楼栋失败，请求体: " + body.toJSONString());
            logger.error("导入楼栋失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /*****************************************房间api*****************************************/

    /**
     * 获取房号列表(分页)
     *
     * @param request  req
     * @param cmc     房号名称
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 银行列表
     * @throws Exception err
     */
    public Page<Fh> getFhList(HttpServletRequest request, String cmc, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(cmc)) {
            body.put("cmc", cmc.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/xk_fhxx_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取房号列表失败，请求url: " + baseUrl + "/api/CZF/xk_fhxx_List");
            logger.error("获取房号列表失败，请求参数: " + params);
            logger.error("获取房号列表失败，请求体: " + body.toJSONString());
            logger.error("获取房号列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Fh> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Fh> dataList = jsonObject.getJSONArray("Results").toJavaList(Fh.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取房号列表(全部)
     *
     * @param request req
     * @return 房号列表
     * @throws Exception err
     */
    public List<Fh> getFhAllList(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/xk_fhxx_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取房号列表失败，请求url: " + baseUrl + "/api/CZF/xk_fhxx_List");
            logger.error("获取房号列表失败，请求体: " + body.toJSONString());
            logger.error("获取房号列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(Fh.class);
    }

    /**
     * 获取房号详情
     *
     * @param request req
     * @param id      房号id
     * @return 银行详情
     * @throws Exception err
     */
    public Fh getFhDetail(HttpServletRequest request, String id) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/xk_fhxx_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取房号详情失败，请求url: " + baseUrl + "/api/CZF/xk_fhxx_Model");
            logger.error("获取房号详情失败，请求参数: " + params);
            logger.error("获取房号详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONObject("Results").toJavaObject(Fh.class);
    }

    /**
     * 更新添加房号
     *
     * @param request req
     * @param body    请求体
     */
    public void aeFh(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/xk_fhxx_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加房号失败，请求url: " + baseUrl + "/api/CZF/xk_fhxx_Update");
            logger.error("更新添加房号失败，请求体: " + body.toJSONString());
            logger.error("更新添加房号失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 导入房号
     *
     * @param request req
     * @param body    请求体
     */
    public void importFh(HttpServletRequest request, JSONArray body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONArray> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/xk_fhxx_Insert",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("导入房号失败，请求url: " + baseUrl + "/api/CZF/xk_fhxx_Insert");
            logger.error("导入房号失败，请求体: " + body.toJSONString());
            logger.error("导入房号失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}