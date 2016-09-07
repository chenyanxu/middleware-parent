Ext.define('kalix.workflow.mainWork.view.dashboard.WorkflowCategory', {
  requires: [
    'Ext.view.View',
    'Ext.XTemplate',
    'Ext.scroll.Scroller'
  ],
  extend: 'Ext.dashboard.Part',
  alias: 'part.workflowCategory',
  title: '',
  store: null,

  viewTemplate: {
    height: 300,
    layout: 'fit',
    header: true,
    //iconCls: 'x-fa fa-search',
    items: [
      {
        xtype: 'dataview',
        bodyPadding: 5,
        itemTpl: [],
        store: null,
        itemSelector: 'div.thumb-wrap'
      }
    ]
  },

  createView: function (config) {
    var view = this.callParent(arguments);
    view.scrollable = Ext.scroll.Scroller.create({
      y: true, x: false
    });
    view.iconCls = config.iconCls;
    view.items[0].itemTpl = new Ext.XTemplate(
      '<div class="thumb-wrap">',
      '<tpl if="imgUrl.indexOf(\'.png\') &gt;-1">',
      '<img src="{imgUrl}" height="80">',
      '</tpl>',
      '<div class="detail">',
      '<span class="title">{name}</span></br>',
      '<span class="desc">{description}</span>',
      '<tpl if="key !=\'empty\'">',
      '<div class="readmore">&nbsp;&nbsp;</div>',
        '<div class="readmore iconfont icon-workflow-apply" data-qtip="申请" onclick="kalix.getApplication()._mainView.controller.onNavigationSpecial({appName:\'oa\',routeId:\'workflow/{key}\'})"></div>',
      '<div class="readmore iconfont icon-workflow-view" data-qtip="查看" onclick="Ext.create(\'kalix.workflow.mainWork.controller.DashboardController\')' +
      '.onOpenProcessDefinition(\'{name}\',\'{processId}\')"></div>',
      '</div>',
      '</div>',
      '</tpl>',
      '</div>');
    view.items[0].store = config.store;
    view.title = config.title;
    return view;
  }
});