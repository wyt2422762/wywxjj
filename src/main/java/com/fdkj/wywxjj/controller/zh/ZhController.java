package com.fdkj.wywxjj.controller.zh;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh_his;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.utils.DateUtils;
import com.fdkj.wywxjj.utils.math.BigDecimalUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * 账户
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/ZHGL")
public class ZhController {

    private static final Logger log = LoggerFactory.getLogger(ZhController.class);

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
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        return new ModelAndView("zhMgr/zh_index");
    }


    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toInfo/{id}")
    public ModelAndView toInfo(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_info");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toJf/{id}")
    public ModelAndView toJf(HttpServletRequest request,
                             @RequestParam(value = "opts", required = false) List<String> opts,
                             @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_jf");
    }

    /**
     * 跳转到
     *
     * @param request req
     * @param opts    操作权限信息
     * @param id      账户id
     * @return res
     * @throws Exception e
     */
    @RequestMapping("toEdit/{id}")
    public ModelAndView toEdit(HttpServletRequest request,
                               @RequestParam(value = "opts", required = false) List<String> opts,
                               @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        return new ModelAndView("zhMgr/zh_edit");
    }

    /**
     * 获取账户列表
     *
     * @param request req
     * @param fk_qybm 区域编码
     * @param fk_yhid 银行id
     * @param no      账号
     * @param page    第几页
     * @param limit   每页显示多少条
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                   @RequestParam(value = "fk_yhid", required = false) String fk_yhid,
                                                   @RequestParam(value = "no", required = false) String no,
                                                   @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {

        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_yhid)) {
                reqBody.put("fk_yhid", fk_yhid);
            }
            if (StringUtils.isNotBlank(no)) {
                reqBody.put("no", no);
            }
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            Page<Zh> zhPage = api.getZhList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取账户列表成功", zhPage);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取账户列表失败", e);
            throw new BusinessException("获取账户列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 获取账户详情
     *
     * @param request req
     * @param id      账户id
     * @return res
     */
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request,
                                                     @PathVariable String id) {
        try {
            Zh zhDetail = api.getZhDetail(request, id);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取账户详情成功", zhDetail);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取账户详情失败", e);
            throw new BusinessException("获取账户详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 添加/编辑账户
     *
     * @param request req
     * @param xf      是否是续费
     * @param zh      请求体
     * @return res
     */
    @RequestMapping("aeZh")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeZh(HttpServletRequest request,
                                                @RequestParam(value = "xf", required = false) Boolean xf,
                                                @RequestBody JSONObject zh) {
        try {
            String khrq = zh.getJSONObject("model").getString("khrq");
            if (StringUtils.isBlank(khrq)) {
                khrq = DateUtils.parseDateToStr("yyyy-MM-dd\'T\'HH:mm:ss.sss", new Date());
                zh.getJSONObject("model").remove("khrq");
                zh.getJSONObject("model").put("khrq", khrq);
            }
            JSONArray list = zh.getJSONArray("list");
            if (list != null && list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    JSONObject jsonObject = list.getJSONObject(i);
                    jsonObject.putIfAbsent("jzrq", DateUtils.parseDateToStr("yyyy-MM-dd\'T\'HH:mm:ss.sss", new Date()));
                }
            }
            //调用接口添加编辑数据
            api.aeZh(request, zh);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("更新账户成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("更新账户失败", e);
            throw new BusinessException("更新账户失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 缴费
     *
     * @param request req
     * @param id      账户id
     * @param jfje    缴费金额
     * @return res
     */
    @RequestMapping("jf/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> aeZh(HttpServletRequest request,
                                                @PathVariable String id,
                                                @RequestParam("jfje") String jfje) {
        try {
            Zh zhDetail = api.getZhDetail(request, id);
            String money = zhDetail.getMoney();
            String add = BigDecimalUtil.add(money, jfje).toPlainString();
            zhDetail.setMoney(add);

            List<Zh_his> list = zhDetail.getList();
            Zh_his zh_his = new Zh_his();
            zh_his.setCzlx(Constants.Jzlb.JF);
            zh_his.setSr(jfje);
            zh_his.setZhye(add);
            zh_his.setFk_zhid(zhDetail.getId());
            zh_his.setJzrq(DateUtils.parseDateToStr("yyyy-MM-dd\'T\'HH:mm:ss.sss", new Date()));

            List<Zh_his> list1 = zhDetail.getList();
            if(list1 == null){
                list1 = new ArrayList<>();
            }
            list1.add(zh_his);

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("model", (JSONObject)JSONObject.toJSON(zhDetail));

            JSONArray ja = (JSONArray)JSONArray.toJSON(list1);
            for (int i = 0; i < ja.size(); i++) {
                JSONObject jsonObject1 = ja.getJSONObject(i);
                jsonObject1.remove("id");
            }

            jsonObject.put("list", ja);

            api.aeZh(request, jsonObject);

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("账户缴费成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("账户缴费失败", e);
            throw new BusinessException("账户缴费失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }
}
