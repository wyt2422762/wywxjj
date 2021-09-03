package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Jnsz;
import com.fdkj.wywxjj.api.model.sysMgr.Jnsz_ls;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.error.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 缴纳设置接口
 *
 * @author wyt
 */
@Component
public class JnszApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(JnszApi.class);

    /**
     * 获取缴纳设置列表(分页)
     *
     * @param request  req
     * @param reqBody  请求体
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 缴纳设置列表
     * @throws Exception err
     */
    public Jnsz getJnszList(HttpServletRequest request, Map<String, String> reqBody, Integer pageNo, Integer pageSize) throws Exception {
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

        String url = baseUrl + "/api/CZF/WYWXJJ_JNSZ_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取缴纳设置列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_JNSZ_List");
            logger.error("获取缴纳设置列表失败，请求参数: " + params);
            logger.error("获取缴纳设置列表失败，请求体: " + body.toJSONString());
            logger.error("获取缴纳设置列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        return jsonObject.getJSONArray("Results").getJSONObject(0).getObject("m", Jnsz.class);
    }

    /**
     * 获取缴纳设置列表(全部)
     *
     * @param request req
     * @param reqBody 请求体
     * @return 房号列表
     * @throws Exception err
     */
    public List<Jnsz> getJnszAllList(HttpServletRequest request, Map<String, Object> reqBody) throws Exception {
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

        String url = baseUrl + "/api/CZF/WYWXJJ_JNSZ_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取缴纳设置列表失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_JNSZ_List");
            logger.error("获取缴纳设置列表失败，请求体: " + body.toJSONString());
            logger.error("获取缴纳设置列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(Jnsz.class);
    }

    /**
     * 获取缴纳设置详情
     *
     * @param request req
     * @param id      房号id
     * @return 缴纳设置详情
     * @throws Exception err
     */
    public Jnsz getJnszDetail(HttpServletRequest request, String id) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("id", id);

        String url = baseUrl + "/api/CZF/WYWXJJ_JNSZ_Model?id={id}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取缴纳设置详情失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_JNSZ_Model");
            logger.error("获取缴纳设置详情失败，请求参数: " + params);
            logger.error("获取缴纳设置详情失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Jnsz jnsz = jsonObject.getJSONObject("Results").getObject("model", Jnsz.class);
        List<Jnsz_ls> jnsz_ls = jsonObject.getJSONObject("Results").getJSONArray("list").toJavaList(Jnsz_ls.class);
        if (jnsz_ls != null && !jnsz_ls.isEmpty()) {
            jnsz.setList(jnsz_ls);
        }
        return jnsz;
    }

    /**
     * 更新添加缴纳设置
     *
     * @param request req
     * @param body    请求体
     */
    public void aeJnsz(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/api/CZF/WYWXJJ_JNSZ_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加缴纳设置失败，请求url: " + baseUrl + "/api/CZF/WYWXJJ_JNSZ_Update");
            logger.error("更新添加缴纳设置失败，请求体: " + body.toJSONString());
            logger.error("更新添加缴纳设置失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
