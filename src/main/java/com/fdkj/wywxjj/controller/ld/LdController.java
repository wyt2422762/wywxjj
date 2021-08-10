package com.fdkj.wywxjj.controller.ld;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.sysMgr.User;
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

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 楼栋
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/LDGL")
public class LdController {
    private static final Logger log = LoggerFactory.getLogger(LdController.class);

    @Autowired
    private Api api;

    /**
     * 获取楼栋列表(分页)
     *
     * @param request req
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList/{fk_xmxxid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                     @PathVariable("fk_xmxxid") String fk_xmxxid,
                                                     @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Page<Ld> ldPage = api.getLdList(request, fk_xmxxid, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取楼栋列表成功", ldPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取楼栋列表失败", e);
            throw new BusinessException("获取楼栋列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取楼栋列表(全部)
     *
     * @param request req
     * @return res
     */
    @RequestMapping("getListAll/{fk_xmxxid}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                     @PathVariable(value = "fk_xmxxid") String fk_xmxxid) {
        try {
            List<Ld> ldList = api.getLdAllList(request, fk_xmxxid);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取楼栋列表成功", ldList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取楼栋列表失败", e);
            throw new BusinessException("获取楼栋列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request, @PathVariable String id) {
        try {
            Ld ldDetail = api.getLdDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取楼栋列表成功", ldDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取楼栋详情失败", e);
            throw new BusinessException("获取楼栋详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取楼栋导入模板
     *
     * @param request req
     * @return e
     */
    @RequestMapping("getImportTemplate")
    public ResponseEntity<CusResponseBody> getXmImportTemplate(HttpServletRequest request) {
        ExcelUtil<Ld> util = new ExcelUtil<>(Ld.class);
        return util.importTemplateExcel("楼幢数据");
    }

    /**
     * 添加/编辑楼栋
     *
     * @param request req
     * @param ld      请求体
     * @return res
     */
    @RequestMapping("aeLd")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeYh(HttpServletRequest request,
                                                @RequestBody JSONObject ld) {
        try {
            //调用接口添加编辑数据
            api.aeLd(request, ld);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新楼栋成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新楼栋失败", e);
            throw new BusinessException("更新楼栋失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 导入楼栋
     *
     * @param request req
     * @param file    文件
     * @return res
     */
    @RequestMapping("/importData")
    @ResponseBody
    public ResponseEntity<CusResponseBody> importData(HttpServletRequest request, MultipartFile file,
                                                      @RequestParam("fk_xmxxid")String fk_xmxxid) {
        try {
            //登录用户
            User user = api.getUserFromCookie(request);
            String fk_xtglid = user.getFk_xtglid();

            ExcelUtil<Ld> util = new ExcelUtil<>(Ld.class);
            List<Ld> ldList = util.importExcel(file.getInputStream());
            if (ldList != null && !ldList.isEmpty()) {
                ldList.forEach(ld -> {
                    ld.setFk_xtglid(fk_xtglid);
                    ld.setFk_xmxxid(fk_xmxxid);
                });
                JSONArray jsonArray = JSONArray.parseArray(JSON.toJSONString(ldList));
                api.importLd(request, jsonArray);
            }
            CusResponseBody cusResponseBody = CusResponseBody.success("导入楼栋成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("导入楼栋失败", e);
            throw new BusinessException("导入楼栋失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
