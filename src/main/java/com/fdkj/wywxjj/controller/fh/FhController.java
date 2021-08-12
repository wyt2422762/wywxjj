package com.fdkj.wywxjj.controller.fh;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.xmMgr.Ld;
import com.fdkj.wywxjj.api.model.xmMgr.Xm;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目(小区)
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/FHGL")
public class FhController {
    private static final Logger log = LoggerFactory.getLogger(FhController.class);

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
    @RequestMapping("toAdd/{fk_xmxxid}/{fk_ldxxid}")
    public ModelAndView toAdd(HttpServletRequest request,
                              @PathVariable("fk_xmxxid") String fk_xmxxid,
                              @PathVariable("fk_ldxxid") String fk_ldxxid,
                              @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }

        Xm xm = api.getXmDetail(request, fk_xmxxid);
        request.setAttribute("xm", xm);
        Ld ld = api.getLdDetail(request, fk_ldxxid);
        request.setAttribute("ld", ld);

        return new ModelAndView("fhMgr/fh_add");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toEdit/{fk_xmxxid}/{fk_ldxxid}/{id}")
    public ModelAndView toAdd(HttpServletRequest request,
                              @PathVariable("fk_xmxxid") String fk_xmxxid,
                              @PathVariable("fk_ldxxid") String fk_ldxxid,
                              @PathVariable("id") String id,
                              @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("user", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }

        Xm xm = api.getXmDetail(request, fk_xmxxid);
        request.setAttribute("xm", xm);
        Ld ld = api.getLdDetail(request, fk_ldxxid);
        request.setAttribute("ld", ld);

        request.setAttribute("id", id);

        return new ModelAndView("fhMgr/fh_edit");
    }

    /**
     * 获取房号列表(分页)
     *
     * @param request req
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList/{fk_xmxxid}/{fk_ldxxid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @PathVariable("fk_xmxxid") String fk_xmxxid,
                                                   @PathVariable("fk_ldxxid") String fk_ldxxid,
                                                   @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("fk_xmxxid", fk_xmxxid);
            reqBody.put("fk_ldxxid", fk_ldxxid);

            Page<Fh> ldPage = api.getFhList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取房号列表成功", ldPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取房号列表失败", e);
            throw new BusinessException("获取房号列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取房号列表(全部)
     *
     * @param request req
     * @return res
     */
    @RequestMapping("getListAll/{fk_xmxxid}/{fk_ldxxid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @PathVariable(value = "fk_xmxxid") String fk_xmxxid,
                                                   @PathVariable(value = "fk_ldxxid") String fk_ldxxid) {
        try {
            Map<String, Object> reqBody = new HashMap<>();
            reqBody.put("fk_xmxxid", fk_xmxxid);
            reqBody.put("fk_ldxxid", fk_ldxxid);

            List<Fh> ldList = api.getFhAllList(request, reqBody);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取房号列表成功", ldList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取房号列表失败", e);
            throw new BusinessException("获取房号列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request, @PathVariable String id) {
        try {
            Fh fhDetail = api.getFhDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取房号列表成功", fhDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取房号详情失败", e);
            throw new BusinessException("获取房号详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取房号导入模板
     *
     * @param request req
     * @return e
     */
    @RequestMapping("getImportTemplate")
    public ResponseEntity<CusResponseBody> getFhImportTemplate(HttpServletRequest request) {
        ExcelUtil<Fh> util = new ExcelUtil<>(Fh.class);
        return util.importTemplateExcel("房间数据");
    }

    /**
     * 添加/编辑楼栋
     *
     * @param request req
     * @param fh      请求体
     * @return res
     */
    @RequestMapping("aeFh")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeYh(HttpServletRequest request,
                                                @RequestBody JSONObject fh) {
        try {
            //调用接口添加编辑数据
            api.aeFh(request, fh);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新房间成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新房间失败", e);
            throw new BusinessException("更新房间失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 导入房间
     *
     * @param request req
     * @param file    文件
     * @return res
     */
    @RequestMapping("/importData")
    @ResponseBody
    public ResponseEntity<CusResponseBody> importData(HttpServletRequest request, MultipartFile file,
                                                      @RequestParam("fk_xmxxid") String fk_xmxxid,
                                                      @RequestParam("fk_ldxxid") String fk_ldxxid) {
        try {
            //登录用户
            User user = api.getUserFromCookie(request);
            String fk_xtglid = user.getFk_xtglid();

            ExcelUtil<Fh> util = new ExcelUtil<>(Fh.class);
            List<Fh> fhList = util.importExcel(file.getInputStream());
            if (fhList != null && !fhList.isEmpty()) {
                fhList.forEach(fh -> {
                    fh.setFk_xtglid(fk_xtglid);
                    fh.setFk_xmxxid(fk_xmxxid);
                    fh.setFk_ldxxid(fk_ldxxid);
                });
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(fhList));
                api.importFh(request, jsonArray);
            }
            CusResponseBody cusResponseBody = CusResponseBody.success("导入房间成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("导入房间失败", e);
            throw new BusinessException("导入房间失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
