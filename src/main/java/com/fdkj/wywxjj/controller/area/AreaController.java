package com.fdkj.wywxjj.controller.area;

import com.fdkj.wywxjj.api.model.area.Area;
import com.fdkj.wywxjj.api.util.AreaApi;
import com.fdkj.wywxjj.base.CusResponseBody;
import com.fdkj.wywxjj.error.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 主页面
 *
 * @author wyt
 */
@Controller
@RequestMapping("area")
public class AreaController {

    private static final Logger log = LoggerFactory.getLogger(AreaController.class);

    @Autowired
    private AreaApi areaApi;

    @RequestMapping("/getData")
    @ResponseBody
    public ResponseEntity<CusResponseBody> getData(HttpServletRequest request) {
        try {
            Area area = new Area().setID("100000");
            getData(request, area);
            //构造返回数据
            CusResponseBody cusResponseBody = CusResponseBody.success("获取区域成功", area.getChildren());
            return new ResponseEntity<>(cusResponseBody, HttpStatus.OK);
        } catch (Exception e) {
            log.error("获取区域失败", e);
            throw new BusinessException("获取区域失败: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value(), e);
        }
    }

    private void getData(HttpServletRequest request, Area area) throws Exception {
        List<Area> areaDataList = areaApi.getAreaDataList(request, area.getID());
        if (areaDataList != null && !areaDataList.isEmpty()) {
            area.setChildren(areaDataList);
            for (Area areaChild : areaDataList) {
                getData(request, areaChild);
            }
        }
    }

}
