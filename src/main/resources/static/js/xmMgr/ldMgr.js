//向后台发送请求(添加、修改楼栋)
function aeLd(data, index) {
  $.ajax({
    url: ctx + "/CZF/LDGL/aeLd",
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      if(!index){
        window.location.reload();
      } else {
        top.layer.close(index); //关闭弹出框
        let ifr = $("div.layui-tab div.layui-tab-content div.layui-show iframe", window.top.document)[0];
        $(ifr).attr('src', $(ifr).attr('src'));
      }
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}

//生成楼盘表
function buildDetailTable(obj, fk_xmxxid) {
  let html = "";
  //单元数
  let dys = obj.dys;
  //地上层数
  let dscs = obj.dscs;

  let index = layer.load();

  $.ajax({
    url: ctx + "/CZF/FHGL/getListAll/" + fk_xmxxid + "/" + obj.id,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    success: function (data) {
      layer.close(index);
      let fhList = data.data;

      html += "<table class='layui-table'>";
      //第一行
      html += "<thead>";
      html += "<tr>";
      html += "<th style='white-space: nowrap;width: 50px;'>楼层/单元</th>";
      for (let i = 1; i <= dys; i++) {
        html += "<th style='white-space: nowrap;'>" + i + "单元</th>";
      }
      html += "</tr>";
      html += "</thead>";

      //后续行
      html += "<tbody style='max-height: 200px;overflow-y: auto;'>";
      for (let i = dscs - 0; i > 0; i--) {
        html += "<tr>";
        html += "<td style='white-space: nowrap;'>" + i + "层</td>";

        for (let j = 1; j <= dys; j++) {
          html += "<td style='white-space: nowrap;' data-lc='" + i + "' data-dy='" + j + "'>"

          if(fhList && fhList.length > 0){
            for(let ii in fhList){
              let fh = fhList[ii];
              if(fh.szdy - 0 == j && fh.szlc - 0 == i){
                html += "<span class='lpb ";
                if(fh.fjzt == "已出售"){
                  html += "border-green ";
                }
                html += "'";
                html += " data-id='" + fh.id + "' >" + fh.fh + "</span>";
              }
            }
          }

          html += "</td>";
        }

        html += "</tr>";
      }
      html += "</tbody>";

      html += "</table>";

      $("#lpb").empty().html(html);

    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      layer.close(index);
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}