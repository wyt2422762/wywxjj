package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.fa.js.Fa_js;
import com.fdkj.wywxjj.api.model.fa.js.Fa_js_ft;
import com.fdkj.wywxjj.api.model.sysMgr.User;
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
 * 方案结算接口
 *
 * @author wyt
 */
@Component
public class FaJsApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(DictApi.class);

    /**
     * 获取方案结算list(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   第几页
     * @param pageSize 每页条数
     * @return res
     */
    public Page<Fa_js> getFajsList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
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

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_JS_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案结算列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_JS_List");
            logger.error("获取方案结算列表失败，请求参数: " + params);
            logger.error("获取方案结算列表失败，请求体: " + body.toJSONString());
            logger.error("获取方案结算列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Fa_js> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Fa_js> dataList = jsonObject.getJSONArray("Results").toJavaList(Fa_js.class);
        page.setDataList(dataList);
        return page;
    }

    /**
     * 获取方案结算list(全部)
     *
     * @param request req
     * @param reqBody 请求体
     * @return res
     */
    public List<Fa_js> getFajsList(HttpServletRequest request, Map<String, String> reqBody) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        body.putAll(reqBody);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_JS_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案结算列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_JS_List");
            logger.error("获取方案结算列表失败，请求体: " + body.toJSONString());
            logger.error("获取方案结算列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONArray("Results").toJavaList(Fa_js.class);
    }

    /**
     * 获取方案结算详情
     *
     * @param request req
     * @param id      请求id
     * @return res
     * @throws Exception err
     */
    public Fa_js getFajsDetail(HttpServletRequest request, String id) throws Exception {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_JS_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案结算详情失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_JS_Model");
            logger.error("获取方案结算详情失败，请求参数: " + params);
            logger.error("获取方案结算详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        JSONObject results = jsonObject.getJSONObject("Results");
        Fa_js fa_js = results.getObject("model", Fa_js.class);
        //分摊信息
        List<Fa_js_ft> list = results.getJSONArray("list").toJavaList(Fa_js_ft.class);

        fa_js.setFtList(list);
        return fa_js;
    }

    /**
     * 添加方案结算
     *
     * @param request req
     * @param body    请求体
     */
    public void addFa_js(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FA_JS_TJ",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("添加方案结算失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_JS_TJ");
            logger.error("添加方案结算失败，请求体: " + body.toJSONString());
            logger.error("添加方案结算失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 删除方案结算
     *
     * @param request req
     * @param body    请求体
     */
    public void delFa_js(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FA_JS_SC",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("删除方案结算失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_JS_SC");
            logger.error("删除方案结算失败，请求体: " + body.toJSONString());
            logger.error("删除方案结算失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 结算支付
     *
     * @param request req
     * @param body    请求体
     */
    public void jszf(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FA_JS_ZF",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("结算支付失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_JS_ZF");
            logger.error("结算支付失败，请求体: " + body.toJSONString());
            logger.error("结算支付失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

}
