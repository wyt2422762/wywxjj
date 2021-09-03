package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.Role;
import com.fdkj.wywxjj.api.model.sysMgr.User;
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
 * 角色接口
 * @author wyt
 */
@Component
public class RoleApi extends BaseApi {
    private static final Logger logger = LoggerFactory.getLogger(RoleApi.class);

    /**
     * 获取角色列表(分页)
     *
     * @param request  req
     * @param roleName 角色名称
     * @param pageNo   第几页
     * @param pageSize 每页显示的条数
     * @return 角色列表
     * @throws Exception err
     */
    public Page<Role> getRoleList(HttpServletRequest request, String roleName, Integer pageNo, Integer pageSize) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());
        if (StringUtils.isNotBlank(roleName)) {
            body.put("jsmc", roleName.trim());
        }

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        //请求参数
        Map<String, Object> params = new HashMap<>(4);
        params.put("page", pageNo == null ? 1 : pageNo);
        params.put("pageNum", pageSize == null ? 10 : pageSize);

        String url = baseUrl + "/PTXT/PT_JSGL_List?page={page}&pageNum={pageNum}";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取角色列表失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_List");
            logger.error("获取角色列表失败，请求参数: " + params);
            logger.error("获取角色列表失败，请求体: " + body.toJSONString());
            logger.error("获取角色列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        //构造返回信息
        Page<Role> page = new Page<>(pageNo == null ? 1 : pageNo, pageSize == null ? 10 : pageSize);
        Integer totalRecord = jsonObject.getInteger("TotalCount");
        page.setTotalRecord(totalRecord);
        List<Role> dataList = jsonObject.getJSONArray("Results").toJavaList(Role.class);
        page.setDataList(dataList);

        return page;
    }

    /**
     * 获取全部角色列表
     *
     * @param request req
     * @return 角色列表
     * @throws Exception err
     */
    public List<Role> getAllRoleList(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //请求体
        JSONObject body = new JSONObject();
        body.put("fk_xtglid", user.getFk_xtglid());

        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);

        String url = baseUrl + "/PTXT/PT_JSGL_List";
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(url,
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取全部角色列表失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_List");
            logger.error("获取全部角色列表失败，请求体: " + body.toJSONString());
            logger.error("获取全部角色列表失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(Role.class);
    }

    /**
     * 更新添加角色
     *
     * @param request req
     * @param body    请求体
     */
    public void aeRole(HttpServletRequest request, JSONObject body) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(body, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_JSGL_Update",
                        HttpMethod.POST, requestEntity, String.class);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("更新添加角色失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_Update");
            logger.error("更新添加角色失败，请求体: " + body.toJSONString());
            logger.error("更新添加角色失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    /**
     * 删除角色
     *
     * @param request req
     * @param roleId  角色id
     */
    public void delRole(HttpServletRequest request, String roleId) {
        //请求头
        HttpHeaders headers = getHttpHeaders(request);
        //组装请求体
        HttpEntity<JSONObject> requestEntity = new HttpEntity<>(null, headers);
        //参数
        Map<String, String> params = new HashMap<>(1);
        params.put("id", roleId);
        //请求
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_JSGL_Del?id={id}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);
        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("删除角色失败，请求url: " + baseUrl + "/PTXT/PT_JSGL_Del");
            logger.error("删除角色失败，请求参数: " + params);
            logger.error("删除角色失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
