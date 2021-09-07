package com.fdkj.wywxjj.task;


import com.fdkj.wywxjj.api.model.zhMgr.Zh;
import com.fdkj.wywxjj.api.util.JxszApi;
import com.fdkj.wywxjj.api.util.ZhApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 计息计算定时任务
 * @author wyt
 */
@Component
@EnableScheduling
public class InterestTask {
    private static final Logger log = LoggerFactory.getLogger(InterestTask.class);

    @Autowired
    private ZhApi zhApi;
    @Autowired
    private JxszApi jxszApi;

    /**
     * 计算账户利息
     */
    private void calcZhInterest() {
        Map<String, String> reqBody = new HashMap<>();
    }

    private void calc(List<Zh> zhList) {
        for (Zh zh : zhList) {
            //银行id
            String fk_yhid = zh.getFk_yhid();
            //账户金额
            String money = zh.getMoney();

            //获取生效的利率

            //计算计息

            //更新账户信息，账户历史信息

        }
    }
}
