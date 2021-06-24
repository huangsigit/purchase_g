/** EasyWeb iframe v3.1.8 date:2020-05-04 License By http://ostudiogame.com */
layui.define(["layer","element","admin"],function(g){var i=layui.jquery;var m=layui.layer;var j=layui.element;var p=layui.admin;var e=p.setter;var k=".layui-layout-admin>.layui-header";var f=".layui-layout-admin>.layui-side>.layui-side-scroll";var b=".layui-layout-admin>.layui-body";var l=b+">.layui-tab";var a=b+">.layui-body-header";var c="admin-pagetabs";var d="admin-side-nav";var q={};var o=false;var n={homeUrl:undefined,mTabPosition:undefined,mTabList:[]};n.loadView=function(t){if(!t.menuPath){return m.msg("url不能为空",{icon:2,anim:6})}if(e.pageTabs){var s;i(l+">.layui-tab-title>li").each(function(){if(i(this).attr("lay-id")===t.menuPath){s=true}});if(!s){if(n.mTabList.length+1>=e.maxTabNum){m.msg("最多打开"+e.maxTabNum+"个选项卡",{icon:2,anim:6});return p.activeNav(n.mTabPosition)}o=true;j.tabAdd(c,{id:t.menuPath,title:'<span class="title">'+(t.menuName||"")+"</span>",content:'<iframe class="admin-iframe" lay-id="'+t.menuPath+'" src="'+t.menuPath+'" onload="layui.index.hideLoading(this);" frameborder="0"></iframe>'});p.showLoading({elem:i('iframe[lay-id="'+t.menuPath+'"]').parent(),size:""});if(t.menuPath!==n.homeUrl){n.mTabList.push(t)}if(e.cacheTab){p.putTempData("indexTabs",n.mTabList)}}if(!t.noChange){j.tabChange(c,t.menuPath)}}else{p.activeNav(t.menuPath);var r=i(b+">div>.admin-iframe");if(r.length===0){i(b).html(['<div class="layui-body-header">','   <span class="layui-body-header-title"></span>','   <span class="layui-breadcrumb pull-right" lay-filter="admin-body-breadcrumb" style="visibility: visible;"></span>',"</div>",'<div style="-webkit-overflow-scrolling: touch;">','   <iframe class="admin-iframe" lay-id="',t.menuPath,'" src="',t.menuPath,'"','      onload="layui.index.hideLoading(this);" frameborder="0"></iframe>',"</div>"].join(""));p.showLoading({elem:i('iframe[lay-id="'+t.menuPath+'"]').parent(),size:""})}else{p.showLoading({elem:r.parent(),size:""});r.attr("lay-id",t.menuPath).attr("src",t.menuPath)}i('[lay-filter="admin-body-breadcrumb"]').html(n.getBreadcrumbHtml(t.menuPath));n.mTabList.splice(0,n.mTabList.length);if(t.menuPath===n.homeUrl){n.mTabPosition=undefined;n.setTabTitle(i(t.menuName).text()||i(f+' [lay-href="'+n.homeUrl+'"]').text()||"主页")}else{n.mTabPosition=t.menuPath;n.mTabList.push(t);n.setTabTitle(t.menuName)}if(!e.cacheTab){return}p.putTempData("indexTabs",n.mTabList);p.putTempData("tabPosition",n.mTabPosition)}if(p.getPageWidth()<=768){p.flexible(true)}};n.loadHome=function(v){var u=p.getTempData("indexTabs");var t=p.getTempData("tabPosition");var r=(v.loadSetting===undefined||v.loadSetting)&&(e.cacheTab&&u&&u.length>0);n.homeUrl=v.menuPath;v.noChange=t?r:false;if(e.pageTabs||!r){n.loadView(v)}if(r){for(var s=0;s<u.length;s++){u[s].noChange=u[s].menuPath!==t;if(!u[s].noChange||(e.pageTabs&&!v.onlyLast)){n.loadView(u[s])}}}p.removeLoading(undefined,false)};n.openTab=function(r){if(window!==top&&!p.isTop()&&top.layui&&top.layui.index){return top.layui.index.openTab(r)}if(r.end){q[r.url]=r.end}n.loadView({menuPath:r.url,menuName:r.title})};n.closeTab=function(r){if(window!==top&&!p.isTop()&&top.layui&&top.layui.index){return top.layui.index.closeTab(r)}j.tabDelete(c,r)};n.setTabCache=function(r){if(window!==top&&!p.isTop()&&top.layui&&top.layui.index){return top.layui.index.setTabCache(r)}p.putSetting("cacheTab",r);if(!r){return n.clearTabCache()}p.putTempData("indexTabs",n.mTabList);p.putTempData("tabPosition",n.mTabPosition)};n.clearTabCache=function(){p.putTempData("indexTabs",null);p.putTempData("tabPosition",null)};n.setTabTitle=function(s,r){if(window!==top&&!p.isTop()&&top.layui&&top.layui.index){return top.layui.index.setTabTitle(s,r)}if(e.pageTabs){if(!r){r=i(l+">.layui-tab-title>li.layui-this").attr("lay-id")}if(r){i(l+'>.layui-tab-title>li[lay-id="'+r+'"] .title').html(s||"")}}else{if(s){i(a+">.layui-body-header-title").html(s);i(a).addClass("show");i(k).css("box-shadow","0 1px 0 0 rgba(0, 0, 0, .03)")}else{i(a).removeClass("show");i(k).css("box-shadow","")}}};n.setTabTitleHtml=function(r){if(window!==top&&!p.isTop()&&top.layui&&top.layui.index){return top.layui.index.setTabTitleHtml(r)}if(e.pageTabs){return}if(!r){return i(a).removeClass("show")}i(a).html(r);i(a).addClass("show")};n.getBreadcrumb=function(r){if(!r){r=i(b+">div>.admin-iframe").attr("lay-id")}var t=[];var s=i(f).find('[lay-href="'+r+'"]');if(s.length>0){t.push(s.text().replace(/(^\s*)|(\s*$)/g,""))}while(true){s=s.parent("dd").parent("dl").prev("a");if(s.length===0){break}t.unshift(s.text().replace(/(^\s*)|(\s*$)/g,""))}return t};n.getBreadcrumbHtml=function(r){var u=n.getBreadcrumb(r);var t=r===n.homeUrl?"":('<a ew-href="'+n.homeUrl+'">首页</a>');for(var s=0;s<u.length-1;s++){if(t){t+='<span lay-separator="">/</span>'}t+=("<a><cite>"+u[s]+"</cite></a>")}return t};n.hideLoading=function(r){if(typeof r!=="string"){r=i(r).attr("lay-id")}p.removeLoading(i('iframe[lay-id="'+r+'"],'+b+" iframe[lay-id]").parent(),false)};var h=".layui-layout-admin .site-mobile-shade";if(i(h).length===0){i(".layui-layout-admin").append('<div class="site-mobile-shade"></div>')}i(h).click(function(){p.flexible(true)});if(e.pageTabs&&i(l).length===0){i(b).html(['<div class="layui-tab" lay-allowClose="true" lay-filter="',c,'" lay-autoRefresh="',e.tabAutoRefresh=="true",'">','   <ul class="layui-tab-title"></ul><div class="layui-tab-content"></div>',"</div>",'<div class="layui-icon admin-tabs-control layui-icon-prev" ew-event="leftPage"></div>','<div class="layui-icon admin-tabs-control layui-icon-next" ew-event="rightPage"></div>','<div class="layui-icon admin-tabs-control layui-icon-down">','   <ul class="layui-nav" lay-filter="admin-pagetabs-nav">','      <li class="layui-nav-item" lay-unselect>','         <dl class="layui-nav-child layui-anim-fadein">','            <dd ew-event="closeThisTabs" lay-unselect><a>关闭当前标签页</a></dd>','            <dd ew-event="closeOtherTabs" lay-unselect><a>关闭其它标签页</a></dd>','            <dd ew-event="closeAllTabs" lay-unselect><a>关闭全部标签页</a></dd>',"         </dl>","      </li>","   </ul>","</div>"].join(""));j.render("nav","admin-pagetabs-nav")}j.on("nav("+d+")",function(v){var t=i(v);var s=t.attr("lay-href");if(!s||s==="#"){return}if(s.indexOf("javascript:")===0){return new Function(s.substring(11))()}var u=t.attr("ew-title")||t.text().replace(/(^\s*)|(\s*$)/g,"");var r=t.attr("ew-end");try{if(r){r=new Function(r)}else{r=undefined}}catch(w){console.error(w)}n.openTab({url:s,title:u,end:r});layui.event.call(this,"admin","side({*})",{href:s})});j.on("tab("+c+")",function(){var r=i(this).attr("lay-id");n.mTabPosition=(r!==n.homeUrl?r:undefined);if(e.cacheTab){p.putTempData("tabPosition",n.mTabPosition)}p.activeNav(r);p.rollPage("auto");if(i(l).attr("lay-autoRefresh")=="true"&&!o){p.refresh(r,true)}o=false;layui.event.call(this,"admin","tab({*})",{layId:r})});j.on("tabDelete("+c+")",function(s){var r=n.mTabList[s.index-1];if(r){n.mTabList.splice(s.index-1,1);if(e.cacheTab){p.putTempData("indexTabs",n.mTabList)}q[r.menuPath]&&q[r.menuPath].call();layui.event.call(this,"admin","tabDelete({*})",{layId:r.menuPath})}if(i(l+">.layui-tab-title>li.layui-this").length===0){i(l+">.layui-tab-title>li:last").trigger("click")}});i(document).off("click.navMore").on("click.navMore","[nav-bind]",function(){var r=i(this).attr("nav-bind");i('ul[lay-filter="'+d+'"]').addClass("layui-hide");i('ul[nav-id="'+r+'"]').removeClass("layui-hide");i(k+">.layui-nav .layui-nav-item").removeClass("layui-this");i(this).parent(".layui-nav-item").addClass("layui-this");if(p.getPageWidth()<=768){p.flexible(false)}layui.event.call(this,"admin","nav({*})",{navId:r})});if(e.openTabCtxMenu&&e.pageTabs){layui.use("contextMenu",function(){if(!layui.contextMenu){return}i(l+">.layui-tab-title").off("contextmenu.tab").on("contextmenu.tab","li",function(s){var r=i(this).attr("lay-id");layui.contextMenu.show([{icon:"layui-icon layui-icon-refresh",name:"刷新当前",click:function(){j.tabChange(c,r);if("true"!=i(l).attr("lay-autoRefresh")){p.refresh(r)}}},{icon:"layui-icon layui-icon-close-fill ctx-ic-lg",name:"关闭当前",click:function(){p.closeThisTabs(r)}},{icon:"layui-icon layui-icon-unlink",name:"关闭其他",click:function(){p.closeOtherTabs(r)}},{icon:"layui-icon layui-icon-close ctx-ic-lg",name:"关闭全部",click:function(){p.closeAllTabs()}}],s.clientX,s.clientY);return false})})}g("index",n)});
