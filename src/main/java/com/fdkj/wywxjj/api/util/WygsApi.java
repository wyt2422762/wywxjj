package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.sysMgr.WyGs;
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
 * 物业公司接口
 * @author wyt
 */
@Component
public class WygsApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(WygsApi.class);

    /**
     * 获取物业公司列表(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 物业公司列表
     * @throws Exception err
     */
    public Page<WyGs> getWyGsList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
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
     * @param reqBody 请求体
     * @return 物业公司列表
     * @throws Exception err
     */
    public List<WyGs> getWyGsAllList(HttpServletRequest request, Map<String, String> reqBody) throws Exception {
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
}
