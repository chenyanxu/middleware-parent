/**
 * Activiti流程图窗口
 *
 * @author majian <br/>
 *         date:2015-8-3
 * @version 1.0.0
 */
Ext.define('kalix.workflow.components.ActivitiProcessImageWindow', {
    extend: 'Ext.Window',
    width: 920,
    height: 500,
    border: false,
    modal: true,
    buttonAlign: 'center',
    imgUrl: '',
    iconCls: 'iconfont icon-view',
    constructor: function () {
        this.callParent(arguments);

        var src = this.imgUrl;
        var myMask = new Ext.LoadMask({
            msg: '加 载 中 . . .',
            target: this
        });

        this.add(
            {xtype: 'image', src: src, alt: '图像无法显示'}
        );
        this.on('show', function () {
            myMask.show();

            var runner = new Ext.util.TaskRunner();
            var scope = this;
            var task = Ext.TaskManager.start({
                run: function () {
                    var imgWidth = scope.items.getAt(0).getWidth();
                    var imgHeight = scope.items.getAt(0).getHeight();
                    if (imgWidth > 0) {
                        Ext.TaskManager.stop(task);
                        scope.setWidth(imgWidth + 30);
                        scope.setHeight(imgHeight + 100);
                        myMask.hide();
                    }
                },
                interval: 10
            });
        }, this);
    },
    buttons: [
        {
            text: '关闭',
            glyph: 'xf00d@FontAwesome',
            handler: function () {
                arguments[0].findParentByType('window').close();
            }
        }
    ]
});