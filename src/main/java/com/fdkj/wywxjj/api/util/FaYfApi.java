package com.fdkj.wywxjj.api.util;


import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf_ft;
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
 * 方案预付接口
 *
 * @author wyt
 */
@Component
public class FaYfApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(FaYfApi.class);

    /**
     * 获取方案预付款list(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   第几页
     * @param pageSize 每页条数
     * @return res
     */
    public Page<Fa_yf> getFayfList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
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

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_YFK_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案预付列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_YFK_List");
            logger.error("获取方案预付列表失败，请求参数: " + params);
            logger.error("获取方案预付列表失败，请求体: " + body.toJSONString());
            logger.error("获取方案预付列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Fa_yf> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Fa_yf> dataList = jsonObject.getJSONArray("Results").toJavaList(Fa_yf.class);
        page.setDataList(dataList);
        return page;
    }

    /**
     * 获取方案预付款list(全部)
     *
     * @param request req
     * @param reqBody 请求体
     * @return res
     */
    public List<Fa_yf> getFayfList(HttpServletRequest request, Map<String, String> reqBody) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        body.putAll(reqBody);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_YFK_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案预付列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_YFK_List");
            logger.error("获取方案预付列表失败，请求体: " + body.toJSONString());
            logger.error("获取方案预付列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONArray("Results").toJavaList(Fa_yf.class);
    }

    /**
     * 获取方案预付详情
     *
     * @param request req
     * @param id      请求id
     * @return res
     * @throws Exception err
     */
    public Fa_yf getFayfDetail(HttpServletRequest request, String id) throws Exception {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_YFK_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案预付详情失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_YFK_Model");
            logger.error("获取方案预付详情失败，请求参数: " + params);
            logger.error("获取方案预付详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        JSONObject results = jsonObject.getJSONObject("Results");
        Fa_yf fa_yf = results.getObject("model", Fa_yf.class);
        //分摊信息
        List<Fa_yf_ft> list = results.getJSONArray("list").toJavaList(Fa_yf_ft.class);

        fa_yf.setFtList(list);
        return fa_yf;
    }

    /**
     * 添加方案预付
     *
     * @param request req
     * @param body    请求体
     */
    public void addFa_yf(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FA_YFK_TJ",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("添加方案预付失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_YFK_TJ");
            logger.error("添加方案预付失败，请求体: " + body.toJSONString());
            logger.error("添加方案预付失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 删除方案预付
     *
     * @param request req
     * @param body    请求体
     */
    public void delFa_yf(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FA_YFK_SC",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("删除方案预付失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_YFK_SC");
            logger.error("删除方案预付失败，请求体: " + body.toJSONString());
            logger.error("删除方案预付失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 预付支付
     *
     * @param request req
     * @param body    请求体
     */
    public void yfzf(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FA_ZF_YFZF",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("预付支付失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_ZF_YFZF");
            logger.error("预付支付失败，请求体: " + body.toJSONString());
            logger.error("预付支付失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
