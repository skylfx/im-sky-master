//焦点移动到最后
function _updateFocusLast(el){
    var range = document.createRange();
    range.selectNodeContents(el);
    range.collapse(false);
    var sel = window.getSelection();
    //判断光标位置，如不需要可删除
    if(sel.anchorOffset!=0){
        return;
    };
    sel.removeAllRanges();
    sel.addRange(range);
}
//指定焦点位置插入
function _insertimg(str,index){
    var selection= window.getSelection ? window.getSelection() : document.selection;
    if(selection.focusNode.id!='editor'+index){
        //document.getElementById('editor'+index).focus();
        //切换焦点并到最后
       var el= document.getElementById('editor'+index);
        el.focus();
        _updateFocusLast(el);

        selection= window.getSelection ? window.getSelection() : document.selection;
    }
    var range= selection.createRange ? selection.createRange() : selection.getRangeAt(0);
    if (!window.getSelection){
        document.getElementById('editor'+index).focus();
        var selection= window.getSelection ? window.getSelection() : document.selection;
        var range= selection.createRange ? selection.createRange() : selection.getRangeAt(0);
        range.pasteHTML(str);
        range.collapse(false);
        range.select();
    }
    else{
        document.getElementById('editor'+index).focus();
        range.collapse(false);
        var hasR = range.createContextualFragment(str);
        var hasR_lastChild = hasR.lastChild;
        while (hasR_lastChild && hasR_lastChild.nodeName.toLowerCase() == "br" && hasR_lastChild.previousSibling && hasR_lastChild.previousSibling.nodeName.toLowerCase() == "br") {
            var e = hasR_lastChild;
            hasR_lastChild = hasR_lastChild.previousSibling;
            hasR.removeChild(e)
        }
        range.insertNode(hasR);
        if (hasR_lastChild) {
            range.setEndAfter(hasR_lastChild);
            range.setStartAfter(hasR_lastChild)
        }
        selection.removeAllRanges();
        selection.addRange(range)
    }
}
//阻止 表情图标按钮的默认事件 防止自动切换焦点
document.addEventListener("mousedown", function(e){
    //debugger;
    //console.log(e.target);
    pageMessage.clear();
    if(e.target.id=="emoijT"){
        e.preventDefault()
    }
}, false);

// 封装的对象 控制浏览器顶部标题闪烁 或者 不闪烁
var pageMessage = {
    time: 0,
    title: document.title,
    timer: null,
    // 显示新消息提示
    show: function () {
        var title = pageMessage.title.replace("【　　　】", "").replace("【新消息】", "");
        // 定时器，设置消息切换频率闪烁效果就此产生
        pageMessage.timer = setTimeout(function () {
            pageMessage.time++;
            pageMessage.show();
            if (pageMessage.time % 2 == 0) {
                document.title = "【新消息】" + title
            }
            else {
                document.title = "【　　　】" + title
            }
            ;
        }, 600);
        return [pageMessage.timer, pageMessage.title];
    },
    // 取消新消息提示 v
    clear: function () {
        clearTimeout(pageMessage.timer);
        document.title = pageMessage.title;
    }
};
