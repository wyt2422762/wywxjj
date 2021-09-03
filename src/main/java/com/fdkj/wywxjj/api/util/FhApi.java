package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 房间接口
 * @author wyt
 */
@Component
public class FhApi extends BaseApi{
    private static final Logger logger = LoggerFactory.getLogger(FhApi.class);

    /**
     * 获取房号列表(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 银行列表
     * @throws Exception err
     */
    public Page<Fh> getFhList(HttpServletRequest request, Map<String, Object> reqBody, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (reqBody != null && reqBody.size() > 0) {
            body.putAll(reqBody);
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
        List<Fh> dataList = null;
        JSONArray results = jsonObject.getJSONArray("Results");
        if (results != null && results.size() > 0) {
            dataList = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                JSONObject jsonObject1 = results.getJSONObject(i);
                Zh zh = jsonObject1.toJavaObject(Zh.class);
                zh.setId(jsonObject1.getString("zhid"));
                Fh fh = jsonObject1.getObject("m", Fh.class);
                if (StringUtils.isNotBlank(zh.getNo()) && StringUtils.isNotBlank(zh.getZt())) {
                    fh.setZh(zh);
                }
                dataList.add(fh);
            }
        }

        Page<Fh> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取房号列表(全部)
     *
     * @param request req
     * @param reqBody 请求体
     * @return 房号列表
     * @throws Exception err
     */
    public List<Fh> getFhAllList(HttpServletRequest request, Map<String, Object> reqBody) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (reqBody != null && reqBody.size() > 0) {
            body.putAll(reqBody);
        }

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

        JSONArray results = jsonObject.getJSONArray("Results");
        if (results != null && results.size() > 0) {
            List<Fh> list = new ArrayList<>();
            for (int i = 0; i < results.size(); i++) {
                JSONObject jsonObject1 = results.getJSONObject(i);
                Zh zh = jsonObject1.toJavaObject(Zh.class);
                zh.setId(jsonObject1.getString("zhid"));
                Fh fh = jsonObject1.getObject("m", Fh.class);
                if (StringUtils.isNotBlank(zh.getNo()) && StringUtils.isNotBlank(zh.getZt())) {
                    fh.setZh(zh);
                }
                list.add(fh);
            }
            return list;
        } else {
            return null;
        }
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
