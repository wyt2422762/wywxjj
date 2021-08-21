package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.wf.WorkflowLink;
import com.fdkj.wywxjj.api.model.wf.WorkflowNode;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 接口调用工具类(流程)
 *
 * @author wyt
 */
@Component
public class WfApi {
    private static final Logger logger = LoggerFactory.getLogger(WfApi.class);

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
     * 查询结点信息(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   页数
     * @param pageSize 每页显示的记录
     * @return res
     * @throws Exception err
     */
    public Page<WorkflowNode> getWorkflowNodeList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        body.putAll(reqBody);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/WYWXJJ_WORKFLOW_NODE_List?page={page}&pageNum={pageNum}";

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");

        if (!success) {
            logger.error("获取流程结点列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_WORKFLOW_NODE_List");
            logger.error("获取流程结点列表失败，请求参数: " + params);
            logger.error("获取流程结点列表失败，请求体: " + body.toJSONString());
            logger.error("获取流程结点列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<WorkflowNode> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<WorkflowNode> dataList = jsonObject.getJSONArray("Results").toJavaList(WorkflowNode.class);
        page.setDataList(dataList);
        return page;
    }

    public Page<WorkflowLink> getWorkflowLinkList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        body.putAll(reqBody);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/api/CZF/WYWXJJ_WORKFLOW_LZ_List?page={page}&pageNum={pageNum}";

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");

        if (!success) {
            logger.error("获取流程流转列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_WORKFLOW_NODE_List");
            logger.error("获取流程流转列表失败，请求参数: " + params);
            logger.error("获取流程流转列表失败，请求体: " + body.toJSONString());
            logger.error("获取流程流转列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<WorkflowLink> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<WorkflowLink> dataList = jsonObject.getJSONArray("Results").toJavaList(WorkflowLink.class);
        page.setDataList(dataList);
        return page;
    }
}
