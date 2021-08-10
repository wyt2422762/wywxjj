package com.fdkj.wywxjj.controller.xm;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.utils.DateUtils;
import com.fdkj.wywxjj.utils.poi.ExcelUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 项目(小区)
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/XMGL")
public class XmController {

    private static final Logger log = LoggerFactory.getLogger(XmController.class);

    @Autowired
    private Api api;

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @return res
     * @throws Exception e
     */
    @RequestMapping("Index")
    public ModelAndView index(HttpServletRequest request, @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("xmMgr/xm_index");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toAdd")
    public ModelAndView toAdd(HttpServletRequest request, @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        return new ModelAndView("xmMgr/xm_add");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toEdit/{id}")
    public ModelAndView toEdit(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        request.setAttribute("id", id);
        return new ModelAndView("xmMgr/xm_edit");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toInfo/{id}")
    public ModelAndView toInfo(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        request.setAttribute("fk_xmxxid", id);
        return new ModelAndView("xmMgr/xm_info");
    }

    /**
     * 获取项目(小区)列表(分页)
     *
     * @param request req
     * @param xmmc    项目(小区)名称
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getXmList(HttpServletRequest request,
                                                     @RequestParam(value = "xmmc", required = false) String xmmc,
                                                     @RequestParam(value = "fk_wyid", required = false) String fk_wyid,
                                                     @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Page<Xm> xmPage = api.getXmList(request, fk_wyid, xmmc, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取小区列表成功", xmPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取小区列表失败", e);
            throw new BusinessException("获取小区列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取项目(小区)列表(全部)
     *
     * @param request req
     * @return res
     */
    @RequestMapping("getListAll")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getXmList(HttpServletRequest request,
                                                     @RequestParam(value = "fk_wyid", required = false) String fk_wyid) {
        try {
            List<Xm> xmList = api.getXmAllList(request, fk_wyid);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取小区列表成功", xmList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取小区列表失败", e);
            throw new BusinessException("获取小区列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request, @PathVariable String id) {
        try {
            Xm xmDetail = api.getXmDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取小区列表成功", xmDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取小区详情失败", e);
            throw new BusinessException("获取小区详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取项目(小区)导入模板
     *
     * @param request req
     * @return e
     */
    @RequestMapping("getImportTemplate")
    public ResponseEntity<CusResponseBody> getXmImportTemplate(HttpServletRequest request) {
        ExcelUtil<Xm> util = new ExcelUtil<>(Xm.class);
        return util.importTemplateExcel("小区数据");
    }

    /**
     * 导入项目(小区)
     *
     * @param request req
     * @param file    文件
     * @return res
     */
    @RequestMapping("/importData")
    @ResponseBody
    public ResponseEntity<CusResponseBody> importData(HttpServletRequest request, MultipartFile file) {
        try {
            //登录用户
            User user = api.getUserFromCookie(request);
            //物业公司id
            String fk_id = user.getFk_id();
            if (StringUtils.isBlank(fk_id)) {
                throw new BusinessException("物业公司id为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            String fk_xtglid = user.getFk_xtglid();
            if (StringUtils.isBlank(fk_xtglid)) {
                throw new BusinessException("系统管理id为空", HttpStatus.INTERNAL_SERVER_ERROR.value());
            }
            ExcelUtil<Xm> util = new ExcelUtil<>(Xm.class);
            List<Xm> xmList = util.importExcel(file.getInputStream());
            if (xmList != null && !xmList.isEmpty()) {
                //更新 fk_id fk_xtglid
                xmList.forEach(xm -> {
                    xm.setFk_xtglid(fk_xtglid);
                    xm.setFk_wyid(fk_id);
                });
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(xmList));
                api.importXm(request, jsonArray);
            }
            CusResponseBody cusResponseBody = CusResponseBody.success("导入小区成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("导入小区失败", e);
            throw new BusinessException("导入小区失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 添加/编辑项目(小区)
     *
     * @param request req
     * @param xm      请求体
     * @return res
     */
    @RequestMapping("aeXm")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeYh(HttpServletRequest request,
                                                @RequestBody JSONObject xm) {
        try {
            if (xm != null) {
                //计划竣工日期
                String jhjgrq = xm.getString("jhjgrq");
                //计划开工日期
                String jhkgrq = xm.getString("jhkgrq");
                //处理日期格式
                if (StringUtils.isNotBlank(jhjgrq)) {
                    xm.put("jhkgrq", DateUtils.parseDateToStr("yyyy-MM-dd\'T\'HH:mm:ss.sss", DateUtils.dateTime("yyyy-MM-dd\'T\'HH:mm:ss.sss", jhjgrq)));
                } else {
                    xm.remove("jhkgrq");
                }
                if (StringUtils.isNotBlank(jhkgrq)) {
                    xm.put("jhkgrq", DateUtils.parseDateToStr("yyyy-MM-dd\'T\'HH:mm:ss.sss", DateUtils.dateTime("yyyy-MM-dd\'T\'HH:mm:ss.sss", jhkgrq)));
                } else {
                    xm.remove("jhkgrq");
                }
                //调用接口添加编辑数据
                api.aeXM(request, xm);
            }
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新小区成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新小区失败", e);
            throw new BusinessException("更新小区失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

}
