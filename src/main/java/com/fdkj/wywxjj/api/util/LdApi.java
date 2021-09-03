package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Ld;
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
 * 楼栋接口
 *
 * @author wyt
 */
@Component
public class LdApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(LdApi.class);

    /**
     * 获取楼栋列表(分页)
     *
     * @param request  req
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 楼栋列表
     * @throws Exception err
     */
    public Page<Ld> getLdList(HttpServletRequest request, String fk_xmxxid, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(fk_xmxxid)) {
            body.put("fk_xmxxid", fk_xmxxid.trim());
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
    public List<Ld> getLdAllList(HttpServletRequest request, String fk_xmxxid) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(fk_xmxxid)) {
            body.put("fk_xmxxid", fk_xmxxid.trim());
        }

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

}
