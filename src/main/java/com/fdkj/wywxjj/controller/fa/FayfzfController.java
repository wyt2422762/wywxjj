package com.fdkj.wywxjj.controller.fa;

import com.alibaba.fastjson.JSONObject;
import com.fdkj.wywxjj.api.model.fa.Fa;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf;
import com.fdkj.wywxjj.api.model.fa.yf.Fa_yf_ft;
import com.fdkj.wywxjj.api.model.fa.yf.zf.Fa_zf;
import com.fdkj.wywxjj.api.model.sysMgr.User;
import com.fdkj.wywxjj.api.model.xmMgr.Fh;
import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.util.Api;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.constant.Constants;
import com.fdkj.wywxjj.error.BusinessException;
import com.fdkj.wywxjj.model.base.Page;
import com.fdkj.wywxjj.utils.DateUtils;
import com.fdkj.wywxjj.utils.uuid.IdUtils;
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
import java.util.*;

/**
 * 方案预付支付
 *
 * @author wyt
 */
@Controller
@RequestMapping("CZF/FAYFZF")
public class FayfzfController {

    private static final Logger log = LoggerFactory.getLogger(FayfzfController.class);

    @Autowired
    private Api api;

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @return res
     * @throws Exception err
     */
    @RequestMapping("Index")
    public ModelAndView index(HttpServletRequest request,
                              @RequestParam(value = "opts", required = false) List<String> opts) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        return new ModelAndView("faMgr/fayf/zf/zf_index");
    }

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @param id      方案预付id
     * @return res
     * @throws Exception err
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
        return new ModelAndView("faMgr/fayf/zf/yfzf_info");
    }

    /**
     * 跳转
     *
     * @param request req
     * @param opts    操作权限
     * @param id      方案预付id
     * @return res
     * @throws Exception err
     */
    @RequestMapping("toZf/{id}")
    public ModelAndView toZf(HttpServletRequest request,
                             @RequestParam(value = "opts", required = false) List<String> opts,
                             @PathVariable("id") String id) throws Exception {
        request.setAttribute("cuser", api.getUserFromCookie(request));
        request.setAttribute("opts", opts);
        if (opts != null && !opts.isEmpty()) {
            String s = StringUtils.join(opts, ",");
            request.setAttribute("optsStr", s);
        }
        request.setAttribute("id", id);
        Fa_yf fayfDetail = api.getFayfDetail(request, id);
        request.setAttribute("zfbh", fayfDetail.getYfkbh());

        return new ModelAndView("faMgr/fayf/zf/yfzf_zf");
    }

    /**
     * 获取预付list(分页)
     *
     * @param request req
     * @param yfkbh   预付编号
     * @param zt      状态
     * @param fk_qybm 区域编码
     * @param page    第几页
     * @param limit   每页显示的条数
     * @return res
     */
    @RequestMapping("getList")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getList(HttpServletRequest request,
                                                   @RequestParam(value = "yfkbh", required = false) String yfkbh,
                                                   @RequestParam(value = "zt", required = false) String zt,
                                                   @RequestParam(value = "fk_qybm", required = false) String fk_qybm,
                                                   @RequestParam("page") Integer page, @RequestParam("limit") Integer limit) {
        try {
            Map<String, String> reqBody = new HashMap<>();
            if (StringUtils.isNotBlank(fk_qybm)) {
                reqBody.put("fk_qybm", fk_qybm);
            }
            if (StringUtils.isNotBlank(yfkbh)) {
                reqBody.put("yfkbh", yfkbh);
            }
            if (StringUtils.isNotBlank(zt)) {
                reqBody.put("zt", zt);
            }
            Page<Fa_yf> fayfList = api.getFayfList(request, reqBody, page, limit);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取方案列表成功", fayfList);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取方案预付列表失败", e);
            throw new BusinessException("获取方案预付列表失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    /**
     * 预付支付详情
     *
     * @param request req
     * @param id      预付id
     * @return res
     */
    @RequestMapping("getDetail/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getDetail(HttpServletRequest request,
                                                     @PathVariable("id") String id) {
        try {
            //登录用户
            User cuser = api.getUserFromCookie(request);
            //预付详情
            Fa_yf fayfDetail = api.getFayfDetail(request, id);
            //分摊列表
            List<Fa_yf_ft> ftList = fayfDetail.getFtList();
            List<Fh> fhList = new ArrayList<>();
            for (Fa_yf_ft fa_yf_ft : ftList) {
                String fk_fhid = fa_yf_ft.getFk_fhid();
                String fk_zhid = fa_yf_ft.getFk_zhid();

                Fh fhDetail = api.getFhDetail(request, fk_fhid);
                fhDetail.setFtje(fa_yf_ft.getFtje());

                Zh zhDetail = api.getZhDetail(request, fk_zhid);
                fhDetail.setZh(zhDetail);

                fhList.add(fhDetail);
            }
            fayfDetail.setFtList2(fhList);

            //获取方案信息
            String fk_faid = fayfDetail.getFk_faid();
            Fa faDetail = api.getFaDetail(request, fk_faid);

            //获取预付支付信息
            Map<String, String> body = new HashMap<>();
            body.put("zflx", Constants.Zflx.YF);
            body.put("fk_yfkid", fayfDetail.getId());

            Page<Fa_zf> fazfList = api.getFazfList(request, body, 1, 1);
            List<Fa_zf> dataList = fazfList.getDataList();

            Map<String, Object> res = new HashMap<>();
            res.put("yfInfo", fayfDetail);
            res.put("faInfo", faDetail);

            if(dataList != null && !dataList.isEmpty()) {
                Fa_zf fa_zf = dataList.get(0);
                res.put("yfzfInfo", fa_zf);
            }

            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取预付详情成功", res);
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取预付详情失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("获取预付详情失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }

    /**
     * 预付支付
     *
     * @param request req
     * @param id      预付id
     * @return res
     */
    @RequestMapping("zf/{id}")
    @ResponseBody
    public ResponseEntity<CusResponseBody> zf(HttpServletRequest request,
                                              @PathVariable("id") String id,
                                              @RequestBody JSONObject jsonObject) {
        try {
            //登录用户
            User cuser = api.getUserFromCookie(request);
            String dateToStr = DateUtils.parseDateToStr("yyyy-MM-dd'T'HH:mm:ss.sss", new Date());

            //预付详情
            Fa_yf fayfDetail = api.getFayfDetail(request, id);
            //1. 判断预付状态
            if(Constants.Zfzt.YZF.equals(fayfDetail.getZt())) {
                throw new BusinessException("该预付已支付，支付失败", HttpStatus.BAD_REQUEST.value());
            }
            //2. 修改支付状态
            fayfDetail.setZt(Constants.Zfzt.YZF);
            //3. 获取支付数据
            Fa_zf fa_zf = jsonObject.toJavaObject(Fa_zf.class);
            fa_zf.setAddtime(dateToStr).setFk_yhid(cuser.getId()).setCzr(cuser.getUsername())
                    .setZfrq(dateToStr).setId(IdUtils.randomUUID()).setZflx(Constants.Zflx.YF);
            //4. 构造请求数据
            JSONObject json = new JSONObject();
            //方案预付model
            json.put("wywxjJ_FA_YFKmodel", JSONObject.parseObject(JSONObject.toJSONString(fayfDetail)));
            //支付model
            json.put("wywxjJ_FA_ZFmodel", JSONObject.parseObject(JSONObject.toJSONString(fa_zf)));
            //5. 请求接口
            api.yfzf(request, json);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("预付支付成功");
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("预付支付失败", e);
            if (e instanceof BusinessException) {
                throw (BusinessException) e;
            } else {
                throw new BusinessException("预付支付失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
            }
        }
    }
}
