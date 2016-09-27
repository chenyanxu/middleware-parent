/**
 * @author chenyanxu
 */
Ext.define('kalix.attachment.view.AttachmentGrid', {
  extend: 'kalix.view.components.common.BaseGrid',
  requires: [
    'kalix.attachment.controller.AttachmentGridController',
    'kalix.attachment.store.AttachmentStore',
    'kalix.attachment.view.AttachmentForm'
  ],
  alias: 'widget.attachmentGrid',
  xtype: 'attachmentGrid',
  autoLoad: false,
  header:false,
  plugins: ['zorderPlugin'],
  controller: {
    type: 'attachmentGridController'
  },
  store: {
    type: 'attachmentStore'
  },
  height: 445,
  columns: [
    {
      xtype: 'rownumberer',
    },
    {
      text: '编号',
      dataIndex: 'id',
      hidden: true
    },
    {
      text: '名称',
      dataIndex: 'attachmentName'
    },
    {
      text: '类型',
      dataIndex: 'attachmentType'
    },
    {
      text: '大小(MB)',
      xtype: 'templatecolumn',
      tpl: '<tpl>{[(values.attachmentSize/1048576).toFixed(3)]}</tpl>',
      renderer: null
    },
    {
      text: '上传日期',
      dataIndex: 'uploadDate',
      xtype: 'datecolumn',
      format: 'Y-m-d H:i',
      renderer: null
    },
    {
      xtype: 'securityGridColumnCommon',
      flex: 0,
      width: 80,
      verifyItems: [
        {
          iconCls: 'iconfont icon-download',
          permission: 'attachmentDownload',
          tooltip: '下载',
          handler: 'onDownload'
        },
        {
          iconCls: 'iconfont icon-delete',
          permission: 'attachmentDelete',
          tooltip: '删除',
          handler: 'onDelete'
        }
      ]
    }
  ],
  tbar: {
    xtype: 'securityToolbar',
    height: 35,
    padding: '5 0 0 10',
    verifyItems: [
      {
        xtype: 'attachmentForm',
        permission: 'attachmentUpload'
      }
    ]
  }
});
