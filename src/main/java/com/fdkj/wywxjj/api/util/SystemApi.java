package com.fdkj.wywxjj.api.util;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.PT_JS_MK;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.sysMgr.View_PT_XT_MK_Model;
import com.fdkj.wywxjj.error.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 系统接口
 * @author wyt
 */
@Component
public class SystemApi extends BaseApi{
    private static final Logger logger = LoggerFactory.getLogger(SystemApi.class);

    /**
     * 获取所有的系统菜单内容
     *
     * @param request req
     * @return 所有的菜单项(非树形结构)
     * @throws Exception e
     */
    private List<View_PT_XT_MK_Model> getPTXT_View_PT_XT_MK_Model(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);

        String fk_xtglid = user.getFk_xtglid();

        HttpHeaders headers = getHttpHeaders(request);

        Map<String, String> params = new HashMap<>(1);
        params.put("xtid", fk_xtglid);

        //组装请求体
        HttpEntity<MultiValueMap<String, String>> requestEntity2 = new HttpEntity<>(null, headers);

        ResponseEntity<String> responseEntity2 =
                restTemplate.exchange(baseUrl + "/PTXT/View_PT_XT_MK_Model?xtid={xtid}",
                        HttpMethod.POST, requestEntity2, String.class, params);
        String responseEntityBody = responseEntity2.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取系统模块失败，请求url: " + baseUrl + "/PTXT/View_PT_XT_MK_Model");
            logger.error("获取系统模块失败，请求参数: xtid=" + fk_xtglid);
            logger.error("获取系统模块失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONArray("Results").toJavaList(View_PT_XT_MK_Model.class);
    }

    /**
     * 获取所有的系统菜单(树形结构)
     *
     * @param list l
     * @return 系统所有的菜单项(多级树形结构)
     */
    private List<View_PT_XT_MK_Model> buildPTXT_View_PT_XT_MK_Model(List<View_PT_XT_MK_Model> list) {
        //创建一个集合用于保存所有根主菜单
        List<View_PT_XT_MK_Model> rootMenu = new ArrayList<>();
        for (View_PT_XT_MK_Model view_pt_xt_mk_model : list) {
            if (view_pt_xt_mk_model.getMkjb() == 1) {
                rootMenu.add(view_pt_xt_mk_model);
            }
        }
        //排序
        rootMenu.sort(Comparator.comparingInt(View_PT_XT_MK_Model::getMkpxid));
        //加子菜单
        for (View_PT_XT_MK_Model menu : rootMenu) {
            List<View_PT_XT_MK_Model> clit = getChildList(menu.getId(), list);
            if (!clit.isEmpty()) {
                menu.setChildList(clit);
            }
        }
        return rootMenu;
    }

    /**
     * 全部系统菜单处理子菜单
     *
     * @param fId  父级id
     * @param list 模块列表
     * @return res
     */
    private List<View_PT_XT_MK_Model> getChildList(String fId, List<View_PT_XT_MK_Model> list) {
        List<View_PT_XT_MK_Model> childList = new ArrayList<>();
        for (View_PT_XT_MK_Model view_pt_xt_mk_model : list) {
            if (fId.equals(view_pt_xt_mk_model.getFid())) {
                childList.add(view_pt_xt_mk_model);
            }
        }
        //排序
        childList.sort(Comparator.comparingInt(View_PT_XT_MK_Model::getMkpxid));
        //加子菜单
        for (View_PT_XT_MK_Model menu : childList) {
            List<View_PT_XT_MK_Model> clit = getChildList(menu.getId(), list);
            if (!clit.isEmpty()) {
                menu.setChildList(clit);
            }
        }
        return childList;
    }

    /**
     * 获取系统的全部菜单
     *
     * @param request req
     * @return 系统全部的菜单项(树形结构)
     * @throws Exception err
     */
    public List<View_PT_XT_MK_Model> getAllModels(HttpServletRequest request) throws Exception {
        //系统全部菜单(非树形结构)
        List<View_PT_XT_MK_Model> ptXtMkModel = getPTXT_View_PT_XT_MK_Model(request);
        return buildPTXT_View_PT_XT_MK_Model(ptXtMkModel);
    }

    /**
     * 获取角色的模块
     *
     * @param fk_jsid 角色id
     * @return 角色对应的模块
     * //@throws Exception err
     */
    public List<PT_JS_MK> getRoleMk(HttpServletRequest request, String fk_jsid) {
        HttpHeaders headers = getHttpHeaders(request);

        Map<String, String> params = new HashMap<>(1);
        params.put("id", fk_jsid);

        //组装请求体
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(null, headers);
        ResponseEntity<String> responseEntity =
                restTemplate.exchange(baseUrl + "/PTXT/PT_JS_MK_List?id={id}",
                        HttpMethod.POST, requestEntity, String.class, params);
        String responseEntityBody = responseEntity.getBody();
        JSONObject jsonObject = JSONObject.parseObject(responseEntityBody);

        boolean success = jsonObject.getBooleanValue("Success");
        if (!success) {
            logger.error("获取角色的模块失败，请求url: " + baseUrl + "/PTXT/PT_JS_MK_List");
            logger.error("获取角色的模块失败，请求参数: id=" + fk_jsid);
            logger.error("获取角色的模块失败，返回内容: " + responseEntityBody);
            throw new BusinessException(jsonObject.getString("Message"), HttpStatus.INTERNAL_SERVER_ERROR.value());
        }

        return jsonObject.getJSONObject("Results").getJSONArray("PT_JS_MKList").toJavaList(PT_JS_MK.class);
    }

    /**
     * 获取当前登陆用户的菜单
     *
     * @param request req
     * @return 当前登陆用户的菜单
     * @throws Exception err
     */
    public List<View_PT_XT_MK_Model> getUserMenu(HttpServletRequest request) throws Exception {
        User user = getUserFromCookie(request);

        //系统全部菜单(非树形结构)
        List<View_PT_XT_MK_Model> ptXtMkModel = getPTXT_View_PT_XT_MK_Model(request);
        //角色模块
        List<PT_JS_MK> roleMks = getRoleMk(request, user.getFk_jsid());

        return getView_pt_xt_mk_models(ptXtMkModel, roleMks);
    }

    private List<View_PT_XT_MK_Model> getView_pt_xt_mk_models(List<View_PT_XT_MK_Model> ptXtMkModel, List<PT_JS_MK> roleMks) {
        for (View_PT_XT_MK_Model xtMkModel : ptXtMkModel) {
            for (PT_JS_MK roleMk : roleMks) {
                if (xtMkModel.getId().equals(roleMk.getFk_XTMKid())) {
                    String opt = roleMk.getActionType();
                    if (xtMkModel.getOpts() == null) {
                        xtMkModel.setOpts(new ArrayList<>());
                    }
                    xtMkModel.getOpts().add(opt);
                    if ("show".equals(opt)) {
                        xtMkModel.setShow(true);
                    }
                }
            }
        }

        return buildPTXT_View_PT_XT_MK_Model(ptXtMkModel);
    }

    /**
     * 获取角色的模块
     *
     * @param request req
     * @return 角色对应的模块信息列表
     * @throws Exception err
     */
    public List<View_PT_XT_MK_Model> getRoleModel(HttpServletRequest request, String roleId) throws Exception {
        //系统全部菜单(非树形结构)
        List<View_PT_XT_MK_Model> ptXtMkModel = getPTXT_View_PT_XT_MK_Model(request);
        //角色模块
        List<PT_JS_MK> roleMks = getRoleMk(request, roleId);

        return getView_pt_xt_mk_models(ptXtMkModel, roleMks);
    }
}
