<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <!--<meta http-equiv="Content-Style-Type" content="text/css" />-->
    <title></title>
    <style>
      * {
        margin: 0;
        padding: 0;

        font-size: 10pt;
        vertical-align: center;
      }

      .search {
        display: inline-block;
        min-width: 180pt;
      }

      td {
        border: 0.75pt solid #000000;
        text-align: center;
        padding: 2pt;
      }

    </style>
</head>
<body>
<div>
    <p style="text-align: center;">
        <span style="font-size:18pt; font-weight:bold">维修资金结算明细信息汇总统计表</span>
    </p>

    <#if tjqj??>
        <!-- 统计区间 -->
        <p style="text-indent:11pt;margin-top: 5pt;">统计区间: ${tjqj!}</p>
    </#if>

    <table style="border-collapse:collapse;margin-top: 5pt;">
        <#if fk_xmxxid?? || fk_ldxxid?? || dyh??>
            <!-- 筛选条件 -->
            <tr style="height:30pt;line-height: 30pt;">
                <td colspan="5" style="width:552pt;text-align: left;">
                    <span class="search">楼盘名称: ${fk_xmxxid!}</span>
                    <span class="search">幢名称: ${fk_ldxxid!}</span>
                    <span class="search" style="min-width: 120pt;">单元: ${dyh!}</span>
                </td>
            </tr>
        </#if>

        <!-- 结算标题 -->
        <tr style="height:21pt;line-height: 21pt;">
            <td style="width:171.9pt">预付编号</td>
            <td style="width:146.25pt;">预付金额(元)</td>
            <td style="width:74.25pt">预付日期</td>
            <td style="width:84.75pt">预付签署</td>
            <td style="width:71.85pt">状态</td>
        </tr>

        <#if faYfList?? && (faYfList?size > 0) >
            <#list faYfList as yf>
                <!-- 结算数据 -->
                <tr style="height:21pt;line-height: 21pt;">
                    <td style="width:111.9pt;text-align: left;">${yf.yfkbh!}</td>
                    <td style="width:63pt;text-align: right;">${yf.yfkje!}</td>
                    <td style="width:81pt;">${yf.yfkrq!?date('yyyy-MM-dd')}</td>
                    <td style="width:88.5pt;">${yf.yfkqs!}</td>
                    <td style="width:92.25pt;">${yf.zt!}</td>
                </tr>

                <#if yf.ftList2?? && (yf.ftList2?size > 0) >
                    <!-- 结算分摊数据 -->
                    <tr>
                        <td colspan="5" style="width:552pt">
                            <table style="border-collapse:collapse; margin:10pt 50pt">
                                <tr style="height:21pt;line-height: 21pt;background-color:#e8fdf6;">
                                    <td colspan="4" style="width:425.9pt;text-align: left;">分摊信息</td>
                                </tr>
                                <!-- 分摊数据标题 -->
                                <tr style="height:21pt;line-height: 21pt;">
                                    <td style="width:117.65pt">银行账号</td>
                                    <td style="width:115.55pt">账户姓名</td>
                                    <td style="width:78pt">证件号码</td>
                                    <td style="width:111.7pt">分摊金额</td>
                                </tr>
                                <#list yf.ftList2 as ft>
                                    <!-- 分摊数据列表 -->
                                    <tr style="height:21pt;line-height: 21pt;">
                                        <td style="width:117.65pt">${ft.zh.no!}</td>
                                        <td style="width:115.55pt">${ft.zh.yzmc!}</td>
                                        <td style="width:78pt">${ft.zh.yzzjh!}</td>
                                        <td style="width:111.7pt;text-align: right;">${ft.ftje!}</td>
                                    </tr>
                                </#list>
                                <!-- 结算合计数据 -->
                                <tr style="background-color:#e8fdf6;height:21pt;line-height: 21pt;">
                                    <td style="width:117.65pt">${yf.ftHj!}</td>
                                    <td style="width:115.55pt"></td>
                                    <td style="width:78pt"></td>
                                    <td style="width:111.7pt;text-align: right;">${yf.ftTotalMoney!}</td>
                                </tr>
                            </table>
                        </td>
                    </tr>
                </#if>

            </#list>
        </#if>

        <!-- 结算数据合计 -->
        <tr style="height:21pt;line-height: 21pt;background-color:#fff6ff;">
            <td style=" width:171.9pt">${hj!}</td>
            <td style="width:146.25pt;text-align: right;">${totalMoney!}</td>
            <td colspan="3" style="width:232.35pt"></td>
        </tr>
    </table>
    <p style="margin-top: 5pt;padding-left: 380pt;">打印日期: ${dyrq!}</p>
</div>
</body>

</html>