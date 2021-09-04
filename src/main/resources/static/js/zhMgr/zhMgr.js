/**
 * 向后台发送请求(添加、修改账户)
 * @param data
 * @param index
 */
function aeZh(data, index) {
  $.ajax({
    url: ctx + "/CZF/ZHGL/aeZh",
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      if (!index) {
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

/**
 * 缴费
 * @param id 账户id
 * @param jfje 缴费金额
 * @param index
 */
function jf(id, jfje, index) {
  let data = {"jfje": jfje};
  $.ajax({
    url: ctx + "/CZF/ZHGL/jf/" + id + "?jfje=" + jfje,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      if (!index) {
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

/**
 * 销户
 * @param id 账户id
 * @param data 销户申请
 * @param index
 */
function xh(id, data, index) {
  $.ajax({
    url: ctx + "/CZF/ZHGL/xh/" + id,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.msg(data.msg);
      if (!index) {
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

//销户审批
function xhsp(wkslid, yj, action, dqjdid, index) {
  let ii = top.layer.load();
  $.ajax({
    url: ctx + "/CZF/XHSH/sh/" + wkslid + "?action=" + action + "&yj=" + yj + "&dqjdid=" + dqjdid,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    success: function (data) {
      top.layer.close(ii);
      top.layer.msg(data.msg);
      if (!index) {
        window.location.reload();
      } else {
        top.layer.close(index); //关闭弹出框
        let ifr = $("div.layui-tab div.layui-tab-content div.layui-show iframe", window.top.document)[0];
        $(ifr).attr('src', $(ifr).attr('src'));
      }
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.close(ii);
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}