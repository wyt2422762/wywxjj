/**
 * 向后台发送请求(添加、修改账户)
 * @param data
 * @param indexout
 */
function aeZh(data, indexout) {
  let loadi = top.layer.load();

  $.ajax({
    url: ctx + "/CZF/ZHGL/aeZh",
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(data.msg);
      if (!indexout) {
        window.location.reload();
      } else {
        top.layer.close(indexout); //关闭弹出框
        TAB.refreshLayerOpen();
      }
      return false;
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}

/**
 * 缴费
 * @param id 账户id
 * @param jfje 缴费金额
 * @param indexout
 */
function jf(id, jfje, indexout) {
  let data = {"jfje": jfje};

  let loadi = top.layer.load();

  $.ajax({
    url: ctx + "/CZF/ZHGL/jf/" + id + "?jfje=" + jfje,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(data.msg);
      if (!indexout) {
        window.location.reload();
      } else {
        top.layer.close(indexout); //关闭弹出框
        TAB.refreshLayerOpen();
      }
      return false;
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}

/**
 * 销户
 * @param id 账户id
 * @param data 销户申请
 * @param indexout
 */
function xh(id, data, indexout) {
  let loadi = top.layer.load();

  $.ajax({
    url: ctx + "/CZF/ZHGL/xh/" + id,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    data: JSON.stringify(data),
    success: function (data) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(data.msg);
      if (!indexout) {
        window.location.reload();
      } else {
        top.layer.close(indexout); //关闭弹出框
        TAB.refreshLayerOpen();
      }
      return false;
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.close(loadi); //关闭弹出框
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}

//销户审批
function xhsp(wkslid, yj, action, dqjdid, indexout) {
  let loadi = top.layer.load();
  $.ajax({
    url: ctx + "/CZF/XHSH/sh/" + wkslid + "?action=" + action + "&yj=" + yj + "&dqjdid=" + dqjdid,
    type: "POST",
    dataType: "json",
    contentType: "application/json;charset=utf-8",
    success: function (data) {
      top.layer.close(loadi);
      top.layer.msg(data.msg);
      if (!indexout) {
        window.location.reload();
      } else {
        top.layer.close(indexout); //关闭弹出框
        TAB.refreshLayerOpen();
        return false;
      }
    },
    error: function (XMLHttpRequest, textStatus, errorThrown) {
      top.layer.close(loadi);
      top.layer.msg(XMLHttpRequest.responseJSON.msg ? XMLHttpRequest.responseJSON.msg : "操作失败!");
      return false;
    }
  });
}