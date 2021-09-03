package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.area.Area;
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
 * 区域接口
 *
 * @author wyt
 */
@Component
public class AreaApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(AreaApi.class);

    /**
     * 获取区域信息
     *
     * @param request  req
     * @param parentId 父级id
     * @return 区域信息
     * @throws Exception err
     */
    public List<Area> getAreaDataList(HttpServletRequest request, String parentId) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);
        //请求参数
        Map<String, Object> params = new HashMap<>(1);
        params.put("fbm", parentId);

        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_Areas_ParentId_List?fbm={fbm}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取区域信息失败，请求url: " + baseUrl + "/PTXT/PT_Areas_ParentId_List");
            logger.error("获取日志列表失败，请求参数: " + params);
            logger.error("获取日志列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        List<Area> dataList = jsonObject.getJSONArray("Results").toJavaList(Area.class);
        return dataList;
    }

}
