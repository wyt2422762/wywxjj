package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目(小区)接口
 * @author wyt
 */
@Component
public class XmApi extends BaseApi{
    private static final Logger logger = LoggerFactory.getLogger(XmApi.class);

    /**
     * 获取小区列表(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 项目(小区)列表
     * @throws Exception err
     */
    public Page<Xm> getXmList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
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
    public List<Xm> getXmAllList(HttpServletRequest request, Map<String, String> reqBody) throws Exception {
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

}
