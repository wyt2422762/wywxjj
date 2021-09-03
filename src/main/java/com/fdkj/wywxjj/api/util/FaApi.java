package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.fa.Fa_fh;
import com.fdkj.wywxjj.api.model.fa.Fa_mx;
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
 * 方案接口
 *
 * @author wyt
 */
@Component
public class FaApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(FaApi.class);

    /**
     * 获取方案列表(分页)
     *
     * @param request req
     * @param reqBody 请求体
     * @return res
     * @throws Exception err
     */
    public Page<Fa> getFaList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
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

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_List");
            logger.error("获取方案列表失败，请求参数: " + params);
            logger.error("获取方案列表失败，请求体: " + body.toJSONString());
            logger.error("获取方案列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Fa> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Fa> dataList = jsonObject.getJSONArray("Results").toJavaList(Fa.class);
        page.setDataList(dataList);
        return page;
    }

    /**
     * 获取方案列表(全部)
     *
     * @param request req
     * @param reqBody 请求体
     * @return res
     * @throws Exception err
     */
    public List<Fa> getFaList(HttpServletRequest request, Map<String, String> reqBody) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        body.putAll(reqBody);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_List");
            logger.error("获取方案列表失败，请求体: " + body.toJSONString());
            logger.error("获取方案列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONArray("Results").toJavaList(Fa.class);
    }

    /**
     * 获取方案详情
     *
     * @param request req
     * @param id      请求id
     * @return res
     * @throws Exception err
     */
    public Fa getFaDetail(HttpServletRequest request, String id) throws Exception {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/WYWXJJ_FA_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取方案详情失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_Model");
            logger.error("获取方案详情失败，请求参数: " + params);
            logger.error("获取方案详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        JSONObject results = jsonObject.getJSONObject("Results");
        Fa fa = results.getObject("model", Fa.class);
        List<Fa_fh> fHlist = results.getJSONArray("FHlist").toJavaList(Fa_fh.class);
        fa.setFHlist(fHlist);
        List<Fa_mx> mXlist = results.getJSONArray("MXlist").toJavaList(Fa_mx.class);
        fa.setMXlist(mXlist);
        return fa;
    }


    /**
     * 添加编辑方案
     *
     * @param request req
     * @param body    请求体
     */
    public void aeFa(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FA_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("提交方案失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FA_Update");
            logger.error("提交方案失败，请求体: " + body.toJSONString());
            logger.error("提交方案失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 方案审批
     *
     * @param request req
     * @param body    请求体
     */
    public void spFa(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_FASP",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("方案审核失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_FASP");
            logger.error("方案审核失败，请求体: " + body.toJSONString());
            logger.error("方案审核失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
