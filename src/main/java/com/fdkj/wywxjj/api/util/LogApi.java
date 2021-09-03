package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.ser.Serializers;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.sysMgr.Xtrz;
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
 * 系统日志接口
 *
 * @author wyt
 */
@Component
public class LogApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(DictApi.class);

    /**
     * 获取日志列表
     *
     * @param request   req
     * @param pageNo    第几页
     * @param pageSize  每页显示的条数
     * @param startTime 开始事件
     * @param endTime   结束事件
     * @return 日志列表
     * @throws Exception err
     */
    public Page<Xtrz> getLogList(HttpServletRequest request, Integer pageNo, Integer pageSize, String startTime, String endTime) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("id", user.getId());
        body.put("addtime", user.getAddtime());
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);
        if (StringUtils.isNotBlank(startTime)) {
            params.put("starttime", startTime);
        }
        if (StringUtils.isNotBlank(endTime)) {
            params.put("endtime", endTime);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl).append("/api/CZF/PT_XTRZ_List").append("?")
                .append("page={page}&pageNum={pageNum}");
        if (StringUtils.isNotBlank(startTime)) {
            sb.append("&starttime={starttime}");
        }
        if (StringUtils.isNotBlank(endTime)) {
            sb.append("&endtime={endtime}");
        }

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(sb.toString(),
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取日志列表失败，请求url: " + baseUrl + "/api/CZF/PT_XTRZ_List");
            logger.error("获取日志列表失败，请求参数: " + params);
            logger.error("获取日志列表失败，请求体: " + body.toJSONString());
            logger.error("获取日志列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Xtrz> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Xtrz> dataList = jsonObject.getJSONArray("Results").toJavaList(Xtrz.class);
        page.setDataList(dataList);

        return page;
    }
}
