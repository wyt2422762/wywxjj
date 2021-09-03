package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用户接口
 * @author wyt
 */
@Component
public class UserApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(UserApi.class);

    /**
     * 获取用户管理列表
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   第几页
     * @param pageSize 每页显示多少条
     * @return 用户管理列表
     * @throws Exception err
     */
    public Page<User> getUserList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (reqBody != null && !reqBody.isEmpty()) {
            body.putAll(reqBody);
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
}
